=begin

Copyright 2012 Shared Learning Collaborative, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

=end


require 'approval'

APP_CONFIG = YAML::load_file("#{Rails.root}/config/config.yml")[Rails.env]
APP_LDAP_CLIENT = LDAPStorage.new(APP_CONFIG["ldap_host"], APP_CONFIG["ldap_port"],
      APP_CONFIG["ldap_base"],APP_CONFIG["ldap_user"],PropertyDecryptorHelper.decrypt(APP_CONFIG["ldap_pass"]))
#APP_EMAILER = Emailer.new({
#      :sender_name => APP_CONFIG["email_sender_name"],
#      :sender_email_addr => APP_CONFIG["email_sender_address"],
#      :host=>APP_CONFIG["email_host"],
#      :port=>APP_CONFIG["email_port"],
#      :replacer=>{"__URI__" => APP_CONFIG["email_replace_uri"],
#      "__PORTAL__" => APP_CONFIG["portal_url"]}
#    })

#class MyTransitionActionConfig
#  def transition(user)
#    if user[:status] == ApprovalEngine::STATE_APPROVED
#      ApplicationMailer.welcome_email(user).deliver
#    end
#  end
#end
ApprovalEngine.init(APP_LDAP_CLIENT, APP_CONFIG["is_sandbox"],APP_CONFIG["auto_approve"])

# ruby-recaptcha vars
RCC_PUB = APP_CONFIG['recaptcha_pub']
RCC_PRIV = APP_CONFIG['recaptcha_priv']

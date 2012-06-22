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


require 'net/imap'
require '../lib/emailer'

DefaultEmailAddr = 'devldapuser@slidev.org'
DefaultUser = 'devldapuser'
DefaultPassword = 'Y;Gtf@w{'
DefaultSenderName = 'Test Sender'

time = Time.now.getutc

emailer_conf = {
  :host              => 'mon.slidev.org',
  :port              => 3000,
  :sender_name       => DefaultSenderName,
  :sender_email_addr => DefaultEmailAddr
}
emailer = Emailer.new emailer_conf
emailer.send_approval_email({
  :email_addr => DefaultEmailAddr,
  :name       => 'TestFN TestLN',
  :subject    => 'This is a test',
  :content    => "Time: #{time}"
})
puts "Approval email sent"

imap = Net::IMAP.new('mon.slidev.org', 993, true, nil, false)
imap.authenticate('LOGIN', DefaultUser, DefaultPassword)
imap.examine('INBOX')

found = false
imap.search(["FROM", DefaultSenderName]).each do |message_id|
  content = imap.fetch(message_id, "BODY[TEXT]")[0].attr["BODY[TEXT]"]
  if(content.include?("#{time}"))
    found = true
  end
  break if found
end
imap.disconnect

raise "No email contains #{time}" if !found
puts "Test ran successfully"

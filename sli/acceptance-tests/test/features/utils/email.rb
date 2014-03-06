=begin

Copyright 2012-2013 inBloom, Inc. and its affiliates.

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

def check_email(config = {})
  imap_host = config[:imap_host] || Property['email_imap_host']
  imap_port = config[:imap_port] || Property['email_imap_port']
  imap_username = config[:imap_username] || Property['email_imap_registration_user']
  imap_password = config[:imap_password] || Property['email_imap_registration_pass']
  content_substring = config[:content_substring]
  subject_substring = config[:subject_substring]
  initial_wait_time = config[:initial_wait_time] || 1
  retry_attempts = config[:retry_attempts] || 30
  retry_wait_time = config[:retry_wait_time] || 1

  #if ENV['DEBUG']
    puts "imap host #{imap_host}"
    puts "imap port #{imap_port}"
    puts "imap user #{imap_username}"
    puts "imap passwd #{imap_password}"
  #end

  # remove spaces because the imap client may add unnecessary spaces
  content_substring.gsub!(/\s/, '') if content_substring
  subject_substring.gsub!(/\s/, '') if subject_substring
  
  #if ENV['DEBUG']
    puts "content substr #{content_substring}"
    puts "subject substr #{subject_substring}"
  #end

  imap = Net::IMAP.new(imap_host, imap_port, true, nil, false)
  imap.login(imap_username, imap_password)
  imap.examine('INBOX')
  past = Date.today.prev_day.prev_day
  past_date = "#{past.day}-#{Date::ABBR_MONTHNAMES[past.month]}-#{past.year}"
  messages_before = imap.search(['SINCE', past_date])

  yield

  sleep initial_wait_time
  begin
    retry_attempts.times do
      sleep retry_wait_time
      imap.examine('INBOX')

      messages_after = imap.search(['SINCE', past_date])
      messages_new = messages_after - messages_before
      messages_before = messages_after
      unless messages_new.empty?
        messages = imap.fetch(messages_new, ["BODY[HEADER.FIELDS (SUBJECT)]", "BODY[TEXT]"])
        messages.each do |message|
          content = message.attr["BODY[TEXT]"]
          subject = message.attr["BODY[HEADER.FIELDS (SUBJECT)]"]

          if ENV['DEBUG']
            puts "content is #{content}"
            puts "subject is #{subject}"
          end

          if (!content_substring || content.gsub(/\s/,'').include?(content_substring)) &&
             (!subject_substring || subject.gsub(/\s/,'').include?(subject_substring))
            return content
          else
            puts "incorrect email content = #{content}"
            puts "incorrect email subject = #{subject}"
          end
        end
      end
    end
  ensure
    imap.disconnect
  end
  fail("timed out getting email with subject substring = #{subject_substring}, content substring = #{content_substring}")
end

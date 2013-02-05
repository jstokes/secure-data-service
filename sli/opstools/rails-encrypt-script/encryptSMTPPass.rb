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

require_relative './encryptor.rb'

if ARGV.count < 2
  puts "Usage: encryptSMTPPass <keyfile> <email_password>"
  puts "\t keyfile - filename into which the key is stored, which was created by generateRailsKey.rb script"
  puts "\t email_password - Email server password to be ecrypted"
  puts "Use the specified key file to ecrypt given password, outputting the relavent properties"
  exit
else
  keyFilePath = ARGV[0]
  email_pass = ARGV[1]
  encrypt(keyFilePath, email_pass, "email_password")
end

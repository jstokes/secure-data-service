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

require 'test_helper'

class PropertyDecryptorHelperTest < ActionView::TestCase
    
    test "Should decrypt an encrypted string" do
        puts "PropertyDecryptorHelperTest: encrption test"
    
        unencryptedString = "unencrypted string"
    
        #want to test something like
        encryptedResult = PropertyDecryptorHelper.encrypt(unencryptedString)
    
        puts "Got encrypted result: " + encryptedResult
    
        decryptedResult = PropertyDecryptorHelper.decrypt(encryptedResult)
    
        puts "Got decrypted result: " + decryptedResult
        
        assert_equal unencryptedString, decryptedResult
    
        puts "encrypting stuff"
    
        puts "development"
        puts "client_id: " + PropertyDecryptorHelper.encrypt("Eg6eseKRzN")
        puts "client_secret: " + 
        PropertyDecryptorHelper.encrypt( "FqXBla26esaLGcVfhlcLnePOEmeHuez7JpmnR9pLg6RkThqF")
    
        puts "local-acceptance-tests"
        puts "client_id: " + PropertyDecryptorHelper.encrypt("Eg6eseKRzN")
        puts "client_secret: " + 
        PropertyDecryptorHelper.encrypt( "FqXBla26esaLGcVfhlcLnePOEmeHuez7JpmnR9pLg6RkThqF")
    
        puts "development_sb"
        puts "client_id: " + PropertyDecryptorHelper.encrypt("Eg6eseKRzN")
        puts "client_secret: " + 
        PropertyDecryptorHelper.encrypt( "FqXBla26esaLGcVfhlcLnePOEmeHuez7JpmnR9pLg6RkThqF")
    
        puts "team"
        puts "client_id: " + PropertyDecryptorHelper.encrypt("OY6Nnukmn6")
        puts "client_secret: " + 
        PropertyDecryptorHelper.encrypt( "UxeLZVB6rM1hBL3ZEpRSkGvfdkMTTUgJH77UyTCU462zXNIg")
    
        puts "team_sb"
        puts "client_id: " + PropertyDecryptorHelper.encrypt("OY6Nnukmn6")
        puts "client_secret: " + 
        PropertyDecryptorHelper.encrypt( "UxeLZVB6rM1hBL3ZEpRSkGvfdkMTTUgJH77UyTCU462zXNIg")
    
        puts "production"
        puts "client_id: " + PropertyDecryptorHelper.encrypt("OY6Nnukmn6")
        puts "client_secret: " + 
        PropertyDecryptorHelper.encrypt( "UxeLZVB6rM1hBL3ZEpRSkGvfdkMTTUgJH77UyTCU462zXNIg")
    
    end

end
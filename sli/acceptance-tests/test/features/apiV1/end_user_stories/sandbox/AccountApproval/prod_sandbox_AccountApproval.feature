
Feature: SLC operator approves/disables production accounts or disables sandbox accounts

Background: 
  Given I have a "live" SMTP/Email server configured
  #Given I have a "live" SMTP/Email server configured

Scenario: As a slc operator I approve pending production account request
Given a production account request for vendor "Macro Corp" 
And first name "Loraine" and last name "Plyler" 
And login name "Lplyer@macrocorp.com" pending in the account request queue
When I approve the account request
Then a new account is created in production LDAP with login name "Lplyer@macrocorp.com" and the role is "Vendor_Admin"
And an email is sent to the requestor with a link to the application registration tool

Scenario: As a slc operator I reject pending production account request
Given a production account request for vendor "Macro Corp" 
And first name "Loraine" and last name "Plyler" 
And login name "Lplyer@macrocorp.com" pending in the account request queue
When I reject the account request
Then an account exists in production LDAP with login name "Lplyer@macrocorp.com"
And state is "rejected"

Scenario: As a slc operator I disable an approved production account
Given a production account request for vendor "Macro Corp" 
And first name "Loraine" and last name "Plyler" 
And login name "Lplyer@macrocorp.com" approved in the account request queue
When I disable the account 
Then production LDAP account with login name "Lplyer@macrocorp.com" is set as inactive

Scenario: As a developer my request for sandbox account is instantly accepted
Given I submit a request for a a sandbox account request for vendor "Macro Corp" 
And first name "Loraine" and last name "Plyler" 
And login name "Lplyer@macrocorp.com" pending in the account request queue
Then a new account is created automatically in sandbox LDAP with login name "Lplyer@macrocorp.com" and the role is "Super_Admin"
And an email is sent to the requestor with a link to provision sandbox and a link for sandbox application registration tool

Scenario: As a slc operator I disable an approved sandbox account
Given an approved sandbox account for vendor "Macro Corp" 
And first name "Loraine" and last name "Plyler" 
And login name "Lplyer@macrocorp.com" 
When I disable the account 
Then sandbox LDAP account with login name "Lplyer@macrocorp.com" is set as inactive
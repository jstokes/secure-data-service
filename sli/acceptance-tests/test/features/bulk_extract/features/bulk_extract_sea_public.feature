@RALLY_US5660


Feature: As an bulk extract user, I want to be able to get the state public entities

Scenario: As a valid user unsuccessful attempt to get SEA public data extract using BEEP when extract has not been triggered
    Given The bulk extract app has been approved for "Midgar-DAYBREAK" with client id "<clientId>"
    And The X509 cert "cert" has been installed in the trust store and aliased
    And the extraction zone is empty
    And the bulk extract files in the database are scrubbed
    And in my list of rights I have BULK_EXTRACT
    When I log into "SDK Sample" with a token of "rrogers", a "Noldor" for "IL-Daybreak" in tenant "Midgar", that lasts for "300" seconds
    When I make a call to the bulk extract end point "/bulk/extract/b64ee2bcc92805cdd8ada6b7d8f9c643c9459831_id"
    Then I get back a response code of "404"

Scenario: As an bulk extract user, I want to be able to get the state public entities
  	Given the extraction zone is empty
    And the bulk extract files in the database are scrubbed
    And The bulk extract app has been approved for "Midgar-DAYBREAK" with client id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
    Then I remove the edorg with id "IL-Test" from the "Midgar" database
    Then I trigger a bulk extract
    Then I should see "1" bulk extract SEA-public data file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
    When I retrieve the path to and decrypt the SEA public data extract file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
    And I verify that an extract tar file was created for the tenant "Midgar"
    And there is a metadata file in the extract
    And the extract contains a file for each of the following entities:
      |  entityType                            |
      |  course                                |
      |  courseOffering                        |
      |  educationOrganization                 |
      |  graduationPlan                        |
      |  school                                |
      |  session                               |

Scenario Outline: Extract should have all the valid data for the SEA
    When I retrieve the path to and decrypt the SEA public data extract file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
    Then the "<entity>" has the correct number of SEA public data records
    Then I verify that the "<entity>" reference an SEA only

    Examples:
    | entity                                 |
    |  course                                |
    |  courseOffering                        |
    |  educationOrganization                 |
    |  graduationPlan                        |
    |  school                                |
    |  session                               |

Scenario: As a valid user get SEA public data extract using BEEP
    Given in my list of rights I have BULK_EXTRACT
    When I log into "SDK Sample" with a token of "rrogers", a "Noldor" for "IL-Daybreak" in tenant "Midgar", that lasts for "300" seconds
    When I make a call to the bulk extract end point "/bulk/extract/b64ee2bcc92805cdd8ada6b7d8f9c643c9459831_id"
    And the return code is 200 I get expected tar downloaded
    And I decrypt and save the extracted file
    And I verify that an extract tar file was created for the tenant "Midgar"
    And there is a metadata file in the extract
    And the extract contains a file for each of the following entities:
      |  entityType                            |
      |  course                                |
      |  courseOffering                        |
      |  educationOrganization                 |
      |  graduationPlan                        |
      |  school                                |
      |  session                               |

Scenario Outline: Extract received through the API should have all the valid data for the SEA
    When I know where the extracted tar is for tenant "Midgar"
    Then the "<entity>" has the correct number of SEA public data records
    And I verify that the "<entity>" reference an SEA only

  Examples:
    | entity                                 |
    |  course                                |
    |  courseOffering                        |
    |  educationOrganization                 |
    |  graduationPlan                        |
    |  school                                |
    |  session                               |

Scenario: API call to the SEA BEEP with an invalid edOrg
  Given in my list of rights I have BULK_EXTRACT
  When I log into "SDK Sample" with a token of "rrogers", a "Noldor" for "IL-Daybreak" in tenant "Midgar", that lasts for "300" seconds
  When I make a call to the bulk extract end point "/bulk/extract/bdb4be44075c7e7d1a7b066c81ff338ed1936_id"
  Then I get back a response code of "403"

Scenario: API call to the SEA BEEP with a non SEA but valid EdOrg
  Given in my list of rights I have BULK_EXTRACT
  When I log into "SDK Sample" with a token of "rrogers", a "Noldor" for "IL-Daybreak" in tenant "Midgar", that lasts for "300" seconds
  When I make a call to the bulk extract end point "/bulk/extract/772a61c687ee7ecd8e6d9ad3369f7883409f803b_id"
  Then I get back a response code of "404"

Scenario: Invalid user tries to access SEA public data
    Given I am a valid 'service' user with an authorized long-lived token "438e472e-a888-46d1-8087-0195f4e37089"
    And in my list of rights I have BULK_EXTRACT
    When I make a call to the bulk extract end point "/bulk/extract/b64ee2bcc92805cdd8ada6b7d8f9c643c9459831_id"
    When I get back a response code of "403"

Scenario: Bulk extract should fail if there is more than 1 SEA in the tenant.
    Given I am using local data store
    And I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And I post "ExtendedSEA.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "ExtendedSEA.zip" is completed in database
    Then a batch job log has been created
    Given the extraction zone is empty
    And the bulk extract files in the database are scrubbed
    And The bulk extract app has been approved for "Midgar-DAYBREAK" with client id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
    Then I trigger a bulk extract
    Then I should see "0" bulk extract SEA-public data file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"

Scenario: API call to SEA BEEP when there is more than one SEA in the tenant
  Given in my list of rights I have BULK_EXTRACT
  When I log into "SDK Sample" with a token of "rrogers", a "Noldor" for "IL-Daybreak" in tenant "Midgar", that lasts for "300" seconds
  When I make a call to the bulk extract end point "/bulk/extract/b64ee2bcc92805cdd8ada6b7d8f9c643c9459831_id"
  Then I get back a response code of "404"
  Then I remove the edorg with id "IL-Test" from the "Midgar" database

Scenario: One of the entity doesn't reference the SEA
  Given the extraction zone is empty
  And the bulk extract files in the database are scrubbed
  And The bulk extract app has been approved for "Midgar-DAYBREAK" with client id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
  Then I remove the edorg with id "IL-Test" from the "Midgar" database
  And I get the SEA Id for the tenant "Midgar"
  And none of the following entities reference the SEA:
    | entity                                 | path                                   |
    |  course                                | body.schoolId                          |
  Then I trigger a bulk extract
  Then I should see "1" bulk extract SEA-public data file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
  When I retrieve the path to and decrypt the SEA public data extract file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
  And I verify that an extract tar file was created for the tenant "Midgar"
  And there is a metadata file in the extract
  Then I verify that extract does not contain a file for the following entities:
  | entity                                 |
  |  course                                |


Scenario: None of the public entities reference the SEA
    Given the extraction zone is empty
    And the bulk extract files in the database are scrubbed
    And The bulk extract app has been approved for "Midgar-DAYBREAK" with client id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
    And I get the SEA Id for the tenant "Midgar"
    And none of the following entities reference the SEA:
      | entity                                 | path                                   |
      |  course                                | body.schoolId                          |
      |  courseOffering                        | body.schoolId                          |
      |  educationOrganization                 | _id                                    |
      |  educationOrganization                 | body.parentEducationAgencyReference    |
      |  graduationPlan                        | body.educationOrganizationId           |
      |  school                                | body.parentEducationAgencyReference    |
      |  session                               | body.schoolId                          |
    Then I trigger a bulk extract
    Then I should see "0" bulk extract SEA-public data file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"

Scenario: No SEA is available for the tenant
   Given the extraction zone is empty
   And the bulk extract files in the database are scrubbed
   And The bulk extract app has been approved for "Midgar-DAYBREAK" with client id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
   And There is no SEA for the tenant "Midgar"
   Then I trigger a bulk extract
   Then I should see "0" bulk extract SEA-public data file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"

Scenario: Clean up the SEA public data in the database
    Given all collections are empty

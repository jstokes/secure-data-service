Feature: Generic EdOrg Ingestion

  Scenario: Post Generic EdOrg Sample Data Set
    Given the "Midgar" tenant db is empty
    When I ingest "GenericEdOrg.zip"

    #basic SEA
    Then there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "STANDARD-SEA"
        | field                               | value                                       |
        | _id                                 | 884daa27d806c2d725bc469b273d840493f84b4d_id |
        | body.stateOrganizationId            | STANDARD-SEA                                |
    And there are only the following in the "organizationCategories" of the "educationOrganization" collection for id "884daa27d806c2d725bc469b273d840493f84b4d_id" on the "Midgar" tenant
       | value                  |
       | State Education Agency |
    #basic LEA
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-DAYBREAK"
        | field                               | value                                       |
        | _id                                 | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
        | body.stateOrganizationId            | IL-DAYBREAK                                 |
    And there are only the following in the "organizationCategories" of the "educationOrganization" collection for id "1b223f577827204a1c7e9c851dba06bea6b031fe_id" on the "Midgar" tenant
        | value                  |
        | Local Education Agency |
    And there are only the following in the "parentEducationAgencyReference" of the "educationOrganization" collection for id "1b223f577827204a1c7e9c851dba06bea6b031fe_id" on the "Midgar" tenant
        | value                                       |
        | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #basic school
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary"
        | field                               | value                                       |
        | _id                                 | 352e8570bd1116d11a72755b987902440045d346_id |
        | body.stateOrganizationId            | South Daybreak Elementary                   |
        | body.organizationCategories         | School                                      |
        | body.parentEducationAgencyReference | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | 352e8570bd1116d11a72755b987902440045d346_id |
    #nested LEA
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-DAYBREAK-SUB-LEA"
        | field                               | value                                       |
        | _id                                 | c58b7bec2c0a9496e9e6f3b85426cd2f65f23ec4_id |
        | body.stateOrganizationId            | IL-DAYBREAK-SUB-LEA                         |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
    #school off nested LEA
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 2"
        | field                               | value                                       |
        | _id                                 | e6972a8edb280114d26b1c4b919801f73c21232e_id |
        | body.stateOrganizationId            | South Daybreak Elementary 2                 |
        | body.organizationCategories         | School                                      |
        | body.parentEducationAgencyReference | c58b7bec2c0a9496e9e6f3b85426cd2f65f23ec4_id |
       #| metaData.edOrgs                     | e6972a8edb280114d26b1c4b919801f73c21232e_id |
       #| metaData.edOrgs                     | c58b7bec2c0a9496e9e6f3b85426cd2f65f23ec4_id |
       #| metaData.edOrgs                     | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #multiple parents test with LEAs at same level - regular leas
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-DAYBREAK2"
        | field                               | value                                       |
        | _id                                 | a9bef2e3a445efb50d39fa47784a0542eaff5589_id |
        | body.stateOrganizationId            | IL-DAYBREAK2                                |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-DAYBREAK3"
        | field                               | value                                       |
        | _id                                 | 612e326cff7a9f85beb9a3fa6287e31a5bd5c62d_id |
        | body.stateOrganizationId            | IL-DAYBREAK3                                |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 3"
        | field                               | value                                       |
        | _id                                 | 411f22e41453fba1c75c13b09b541520acf5be5b_id |
        | body.stateOrganizationId            | South Daybreak Elementary 3                 |
        | body.organizationCategories         | School                                      |
        | body.parentEducationAgencyReference | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
        | body.parentEducationAgencyReference | a9bef2e3a445efb50d39fa47784a0542eaff5589_id |
        | body.parentEducationAgencyReference | 612e326cff7a9f85beb9a3fa6287e31a5bd5c62d_id |
       #| metaData.edOrgs                     | 411f22e41453fba1c75c13b09b541520acf5be5b_id |
       #| metaData.edOrgs                     | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | a9bef2e3a445efb50d39fa47784a0542eaff5589_id |
       #| metaData.edOrgs                     | 612e326cff7a9f85beb9a3fa6287e31a5bd5c62d_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #multiple parents test with LEAs at same level - nested leas
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-DAYBREAK-SUB-LEA2"
        | field                               | value                                       |
        | _id                                 | fa8f2751396d8162683eecfe86bb472119fc540c_id |
        | body.stateOrganizationId            | IL-DAYBREAK-SUB-LEA2                        |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 4"
        | field                               | value                                       |
        | _id                                 | 215575a74c87251a8d28f8e02b7ebdfd547d954b_id |
        | body.stateOrganizationId            | South Daybreak Elementary 4                 |
        | body.organizationCategories         | School                                      |
        | body.parentEducationAgencyReference | e6972a8edb280114d26b1c4b919801f73c21232e_id |
        | body.parentEducationAgencyReference | fa8f2751396d8162683eecfe86bb472119fc540c_id |
       #| metaData.edOrgs                     | 215575a74c87251a8d28f8e02b7ebdfd547d954b_id |
       #| metaData.edOrgs                     | e6972a8edb280114d26b1c4b919801f73c21232e_id |
       #| metaData.edOrgs                     | fa8f2751396d8162683eecfe86bb472119fc540c_id |
       #| metaData.edOrgs                     | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #school with multiple LEAs at different levels and same levels (above two combined)
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 5"
        | field                               | value                                       |
        | _id                                 | 016a47e1a44fa2097032f82f7163eeabe6f5c377_id |
        | body.stateOrganizationId            | South Daybreak Elementary 5                 |
        | body.organizationCategories         | School                                      |
        | body.parentEducationAgencyReference | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
        | body.parentEducationAgencyReference | a9bef2e3a445efb50d39fa47784a0542eaff5589_id |
        | body.parentEducationAgencyReference | 612e326cff7a9f85beb9a3fa6287e31a5bd5c62d_id |
        | body.parentEducationAgencyReference | e6972a8edb280114d26b1c4b919801f73c21232e_id |
        | body.parentEducationAgencyReference | fa8f2751396d8162683eecfe86bb472119fc540c_id |
       #| metaData.edOrgs                     | 016a47e1a44fa2097032f82f7163eeabe6f5c377_id |
       #| metaData.edOrgs                     | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | a9bef2e3a445efb50d39fa47784a0542eaff5589_id |
       #| metaData.edOrgs                     | 612e326cff7a9f85beb9a3fa6287e31a5bd5c62d_id |
       #| metaData.edOrgs                     | e6972a8edb280114d26b1c4b919801f73c21232e_id |
       #| metaData.edOrgs                     | fa8f2751396d8162683eecfe86bb472119fc540c_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #school/LEA off SEA
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 6"
        | field                               | value                                       |
        | _id                                 | de66bbfafd994193ae6aaf019ecfa14825c32575_id |
        | body.stateOrganizationId            | South Daybreak Elementary 6                 |
        | body.organizationCategories         | School                                      |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 884daa27d806c2d725bc469b273d840493f84b4d_id |
        | body.schoolCategories               | Elementary School                           |
        | body.charterStatus                  | School Charter                              |
        | body.gradesOffered                  | Kindergarten                                |
        | body.gradesOffered                  | First grade                                 |
        | body.gradesOffered                  | Second grade                                |
        | body.gradesOffered                  | Third grade                                 |
        | body.gradesOffered                  | Fourth grade                                |
        | body.gradesOffered                  | Fifth grade                                 |
        | body.LEACategory                    | Independent                                 |
       #| metaData.edOrgs                     | de66bbfafd994193ae6aaf019ecfa14825c32575_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    And there are only the following in the "organizationCategories" of the "educationOrganization" collection for id "de66bbfafd994193ae6aaf019ecfa14825c32575_id" on the "Midgar" tenant
        | value                  |
        | Local Education Agency |
        | School                 |
    And there are only the following in the "parentEducationAgencyReference" of the "educationOrganization" collection for id "de66bbfafd994193ae6aaf019ecfa14825c32575_id" on the "Midgar" tenant
         | value                                       |
         | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #other organization categories
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 7"
       | field                               | value                                       |
       | _id                                 | eef4f5ddc466beb3ad5136587731f9350fd398ec_id |
       | body.stateOrganizationId            | South Daybreak Elementary 7                 |
       | body.schoolCategories               | Elementary School                           |
       | body.charterStatus                  | School Charter                              |
       | body.gradesOffered                  | Kindergarten                                |
       | body.gradesOffered                  | First grade                                 |
       | body.gradesOffered                  | Second grade                                |
       | body.gradesOffered                  | Third grade                                 |
       | body.gradesOffered                  | Fourth grade                                |
       | body.gradesOffered                  | Fifth grade                                 |
       | body.LEACategory                    | Independent                                 |
      #| metaData.edOrgs                     | eef4f5ddc466beb3ad5136587731f9350fd398ec_id |
      #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    And there are only the following in the "organizationCategories" of the "educationOrganization" collection for id "eef4f5ddc466beb3ad5136587731f9350fd398ec_id" on the "Midgar" tenant
       | value                        |
       | Other Local Education Agency |
       | Other School                 |
    And there are only the following in the "parentEducationAgencyReference" of the "educationOrganization" collection for id "eef4f5ddc466beb3ad5136587731f9350fd398ec_id" on the "Midgar" tenant
       | value                                       |
       | 884daa27d806c2d725bc469b273d840493f84b4d_id |

    #mixture of other and regular organization categories and duplicate parentEducationAgencyReference and both types of organization categories
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 8"
        | field                               | value                                       |
        | _id                                 | 57d18122d850992592e5a8d80832648a5b7198cd_id |
        | body.stateOrganizationId            | South Daybreak Elementary 8                 |
        | body.schoolCategories               | Elementary School                           |
        | body.charterStatus                  | School Charter                              |
        | body.gradesOffered                  | Kindergarten                                |
        | body.gradesOffered                  | First grade                                 |
        | body.gradesOffered                  | Second grade                                |
        | body.gradesOffered                  | Third grade                                 |
        | body.gradesOffered                  | Fourth grade                                |
        | body.gradesOffered                  | Fifth grade                                 |
        | body.LEACategory                    | Independent                                 |
       #| metaData.edOrgs                     | 57d18122d850992592e5a8d80832648a5b7198cd_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    And there are only the following in the "organizationCategories" of the "educationOrganization" collection for id "57d18122d850992592e5a8d80832648a5b7198cd_id" on the "Midgar" tenant
        | value                        |
        | Local Education Agency       |
        | School                       |
        | Other Local Education Agency |
        | Other School                 |
    And there are only the following in the "parentEducationAgencyReference" of the "educationOrganization" collection for id "57d18122d850992592e5a8d80832648a5b7198cd_id" on the "Midgar" tenant
       | value                                       |
       | 884daa27d806c2d725bc469b273d840493f84b4d_id |

    #cycle tests - referring to itself
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "Cycle 1"
        | field                               | value                                       |
        | _id                                 | 265cdc11571c4e28aceb6ed5fef795a9ffb2ea5c_id |
        | body.stateOrganizationId            | Cycle 1                                     |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 265cdc11571c4e28aceb6ed5fef795a9ffb2ea5c_id |
    #cycle tests - 3 way cycle
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "Cycle 2"
        | field                               | value                                       |
        | _id                                 | 2a05470ef1a2eacb408513fc646b8f39f1d9cd61_id |
        | body.stateOrganizationId            | Cycle 2                                     |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 3657ac93276c35e866e0b20b523dcca6c807cc81_id |
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "Cycle 3"
        | field                               | value                                       |
        | _id                                 | 3657ac93276c35e866e0b20b523dcca6c807cc81_id |
        | body.stateOrganizationId            | Cycle 3                                     |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 2e68cac9a6a00c44a2f314219794859b4b503e6e_id |
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "Cycle 4"
        | field                               | value                                       |
        | _id                                 | 2e68cac9a6a00c44a2f314219794859b4b503e6e_id |
        | body.stateOrganizationId            | Cycle 4                                     |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 2a05470ef1a2eacb408513fc646b8f39f1d9cd61_id |

    #update tests
    Then "0" records in the "educationOrganization" collection with field "body.nameOfInstitution" matching ".*Updated" should be in the "Midgar" tenant db
    When I ingest "GenericEdOrgUpdates.zip"
    Then "18" records in the "educationOrganization" collection with field "body.nameOfInstitution" matching ".*Updated" should be in the "Midgar" tenant db

    #added another nested LEA and removed one nested LEA to edOrg hierarchy

    #new LEA parent of IL-_DAYBREAK
        And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-DAYBREAK4"
        | field                               | value                                       |
        | _id                                 | 915d173222c1dfd0e5956f225787ea25ef506a7b_id |
        | body.stateOrganizationId            | IL-DAYBREAK4                                |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #now has parent LEA
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-DAYBREAK"
        | field                               | value                                       |
        | _id                                 | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
        | body.stateOrganizationId            | IL-DAYBREAK                                 |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 915d173222c1dfd0e5956f225787ea25ef506a7b_id |
    #now off sub LEA
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary"
        | field                               | value                                       |
        | _id                                 | 352e8570bd1116d11a72755b987902440045d346_id |
        | body.stateOrganizationId            | South Daybreak Elementary                   |
        | body.organizationCategories         | School                                      |
        | body.parentEducationAgencyReference | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | 352e8570bd1116d11a72755b987902440045d346_id |
       #| metaData.edOrgs                     | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | 915d173222c1dfd0e5956f225787ea25ef506a7b_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #no longer sub LEA
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-DAYBREAK-SUB-LEA"
        | field                               | value                                       |
        | _id                                 | c58b7bec2c0a9496e9e6f3b85426cd2f65f23ec4_id |
        | body.stateOrganizationId            | IL-DAYBREAK-SUB-LEA                         |
        | body.organizationCategories         | Local Education Agency                      |
        | body.parentEducationAgencyReference | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #school no longer off nested LEA
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 2"
        | field                               | value                                       |
        | _id                                 | e6972a8edb280114d26b1c4b919801f73c21232e_id |
        | body.stateOrganizationId            | South Daybreak Elementary 2                 |
        | body.organizationCategories         | School                                      |
        | body.parentEducationAgencyReference | c58b7bec2c0a9496e9e6f3b85426cd2f65f23ec4_id |
       #| metaData.edOrgs                     | e6972a8edb280114d26b1c4b919801f73c21232e_id |
       #| metaData.edOrgs                     | c58b7bec2c0a9496e9e6f3b85426cd2f65f23ec4_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    #updated to include new meta.edOrgs for extra LEA in hierarchy
    And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "South Daybreak Elementary 3"
        | field                               | value                                       |
        | _id                                 | 411f22e41453fba1c75c13b09b541520acf5be5b_id |
        | body.stateOrganizationId            | South Daybreak Elementary 3                 |
        | body.organizationCategories         | School                                      |
        | body.parentEducationAgencyReference | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
        | body.parentEducationAgencyReference | a9bef2e3a445efb50d39fa47784a0542eaff5589_id |
        | body.parentEducationAgencyReference | 612e326cff7a9f85beb9a3fa6287e31a5bd5c62d_id |
       #| metaData.edOrgs                     | 411f22e41453fba1c75c13b09b541520acf5be5b_id |
       #| metaData.edOrgs                     | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
       #| metaData.edOrgs                     | a9bef2e3a445efb50d39fa47784a0542eaff5589_id |
       #| metaData.edOrgs                     | 612e326cff7a9f85beb9a3fa6287e31a5bd5c62d_id |
       #| metaData.edOrgs                     | 915d173222c1dfd0e5956f225787ea25ef506a7b_id |
       #| metaData.edOrgs                     | 884daa27d806c2d725bc469b273d840493f84b4d_id |

    #unchanged entities (other than name update)
    And I re-execute saved query "STANDARD-SEA" to get "1" records
    And I re-execute saved query "IL-DAYBREAK2" to get "1" records
    And I re-execute saved query "IL-DAYBREAK3" to get "1" records
    And I re-execute saved query "IL-DAYBREAK-SUB-LEA2" to get "1" records
    And I re-execute saved query "South Daybreak Elementary 4" to get "1" records
    And I re-execute saved query "South Daybreak Elementary 5" to get "1" records
    And I re-execute saved query "Cycle 1" to get "1" records
    And I re-execute saved query "Cycle 2" to get "1" records
    And I re-execute saved query "Cycle 3" to get "1" records
    And I re-execute saved query "Cycle 4" to get "1" records

    #below switched OrganizationCategories to OtherOrganizationCategories and vice-versa
    And I re-execute saved query "South Daybreak Elementary 6" to get "1" records
    And I re-execute saved query "South Daybreak Elementary 7" to get "1" records
    And I re-execute saved query "South Daybreak Elementary 8" to get "1" records

    #duplicate detection test
    And I should see following map of entry counts in the corresponding collections:
      | collectionName                           |              count|
      | recordHash                               |                 19|
      | educationOrganization                    |                 19|
    # clear out the ingested entities to ensure we aren't upserting a second time
    Given the following collections are empty in datastore:
      | collectionName                            |
      | educationOrganization                     |
    When zip file is scp to ingestion landing zone with name "Reingest-GenericEdOrg.zip"
    Then a batch job for file "Reingest-GenericEdOrg.zip" is completed in database
    And I should see "InterchangeEducationOrganization.xml educationOrganization 19 deltas" in the resulting batch job file
    And I should not see a warning log file created
    And I should not see an error log file created
    And I should see following map of entry counts in the corresponding collections:
      | collectionName                           |              count|
      | recordHash                               |                 19|
      | educationOrganization                    |                  0|


@RALLY_US4305
@RALLY_US4306

Feature: As a student or staff I want to use apps that access the inBloom API

  Background: None

@student_endpoints
Scenario: Student has access to entities via API endpoints
Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
  And format "application/json"
  And I am using api version "v1"
 When I validate I have access to entities via the API access pattern "/v1/Entity/Id":
    | entity                    | id                                          |
    | students                  | 067198fd6da91e1aa8d67e28e850f224d6851713_id |
    | parents                   | 5f8989384287747b1960d16edd95ff2bb318e3bd_id |
    | parents                   | 7f5b783a051b72820eab5f8188c45ade72869f0f_id |
    | parents                   | 5f8989384287747b1960d16edd95ff2bb318e3bd_id,7f5b783a051b72820eab5f8188c45ade72869f0f_id |
    | studentParentAssociations | 067198fd6da91e1aa8d67e28e850f224d6851713_idc43bbfa3df05d4fd2d78a9edfee8fd63fbcf495a_id  |
    | studentParentAssociations | 067198fd6da91e1aa8d67e28e850f224d6851713_ide2f8c24b3e1ab8ead6e134d661a464d0f90e4c8e_id  |

@student_endpoints
Scenario: Student has access to non-transitive associations
Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
  And format "application/json"
  And I am using api version "v1" 
  When I validate the allowed association entities via API "/v1/students/067198fd6da91e1aa8d67e28e850f224d6851713_id/studentSectionAssociations":
    | id                                                                                     |
    | 57277fceb3592d0c8f3faadcdd824690bc2b2586_id6e94350a7db678fd3f8fddb521a2a117728c832a_id |
    | eb8663fe6856b49684a778446a0a1ad33238a86d_id9abdc5ad23afda9fca17a667c1af0f472000f2cb_id |
    | 24cdeb47d5ccfee1536dd8f6a8951baea76b82f3_id33ee33e252908a2e95eb8d0b4f85f96ffd4b0bae_id |
    | 0a96d039894bf5c9518584f11a646e53f1a9f4f6_idad60894d8bea24c6903eb973285fb9f5f39dfa18_id |
    | e9b81633cba273dc9cc567d7f0f76a1c070c150d_id84ac5a840b7d61ed3071e5e44e14f9b40192654f_id |
    | 9c5580ef4861ad2242e6ab444a52b359cb5fc516_id24ef2cc415667d2fd9d22047f25691f0aa671569_id |
    | 8ae1caa952b3b22d9f58c26760aec903bed6d31b_id874418051ec05e918f88b69ea4729e5db94702dd_id |
    | 2982f5d3840b0a46bf152c7b7243c0db8dda694f_id06f4aa0f6d84ae7ab290709fc348754cbd232cb5_id |
  When I validate the allowed association entities via API "/v1/students/067198fd6da91e1aa8d67e28e850f224d6851713_id/studentProgramAssociations":
    | id                                                                                     |
    | 067198fd6da91e1aa8d67e28e850f224d6851713_id6f14f627d36449fd4ac6d98198c621f7eee82bc5_id |
    | 067198fd6da91e1aa8d67e28e850f224d6851713_ide16cd0618778dd1f72935f6ecb54519db428a97f_id |
    | 067198fd6da91e1aa8d67e28e850f224d6851713_idbee47dcc9085b8a8193dbaa7cc2d39730d19b059_id |
    | 067198fd6da91e1aa8d67e28e850f224d6851713_idef2480c472f48a31dfa1980d4a25e3fe2466d1d9_id |
    | 067198fd6da91e1aa8d67e28e850f224d6851713_id1564ad795bbfa929b2deb7b06386ad60f50c84fc_id |
  When I validate the allowed association entities via API "/v1/students/067198fd6da91e1aa8d67e28e850f224d6851713_id/studentCohortAssociations":
    | id                                                                                     |
    | 067198fd6da91e1aa8d67e28e850f224d6851713_idc787af32ad98c7e7062619db99d233c32582d30a_id |
    | 067198fd6da91e1aa8d67e28e850f224d6851713_id35a7d3e403fa4702ee6db8cac8719eebf28a3e7e_id |
  When I validate the allowed association entities via API "/v1/students/067198fd6da91e1aa8d67e28e850f224d6851713_id/studentSchoolAssociations":
    | id                                          |
    | 139f77e73ae5f1970c5d884d4d2b90367361d1f6_id |
    | ec8b76883033432dc83b97e71fbc5bf881b4ccbb_id |
  When I validate that I am denied access to restricted endpoints via API:
    | uri                                                                                   | rc  |
    | /v1/sections/e9b81633cba273dc9cc567d7f0f76a1c070c150d_id/studentSectionAssociations   | 403 |
    | /v1/sections/57277fceb3592d0c8f3faadcdd824690bc2b2586_id/studentSectionAssociations   | 403 |
    | /v1/programs/2e19c13bd3b303c5e7a292883cd9e487a6e5eccd_id/studentProgramAssociations   | 403 |
    | /v1/cohorts/b4f9ddccc4c5c47a00541ee7c6d67fcb287316ce_id/studentCohortAssociations     | 403 |

  Scenario: I check the response to uris with query parameters
    Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
    And format "application/json"
    And I am using api version "v1"
    And I am accessing data about myself, "matt.sollars"
    Then I verify the following response body fields in "/schools?parentEducationAgencyReference=1b223f577827204a1c7e9c851dba06bea6b031fe_id&sortBy=stateOrganizationId":
      | field                                                 | value                                       |
      | 0.stateOrganizationId                                 | Daybreak Central High                       |
      | 1.stateOrganizationId                                 | East Daybreak Junior High                   |
      | 2.stateOrganizationId                                 | South Daybreak Elementary                   |
    Then I verify the following response body fields in "/schools?parentEducationAgencyReference=1b223f577827204a1c7e9c851dba06bea6b031fe_id&sortBy=stateOrganizationId&limit=1&offset=1":
      | field                                                 | value                                       |
      | 0.stateOrganizationId                                 | East Daybreak Junior High                   |
    Then I verify the following response body fields in "/students/067198fd6da91e1aa8d67e28e850f224d6851713_id/studentSectionAssociations?beginDate=2013-09-09&sortBy=beginDate":
      | field                                                 | value                                       |
      | 0.beginDate                                           | 2013-09-09                                  |

@RALLY_US5911
Feature: An edorg's extract file should contain student and staff data from previous enrollments with other schools

  Scenario: Setup the database and trigger an extract
    Given I clean the bulk extract file system and database
    And I am using local data store
    And I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And I successfully ingest "PriorDataSetDeltas.zip"
    And all edorgs in "Midgar" are authorized for "SDK Sample"
    And I trigger a delta extract

##########################################################################
#    TIMELINE OF ENROLLMENT OF CARMEN ORTIZ FOR POSITIVE/NEGATIVE CASES
#  >>--------------------------------------------------------------------->
#    [2011-08-26 -DCH- 2012-05-22]
#                                      [2012-08-26 -- SCH--- 2018-05-22]
#
#
#    TIMELINE OF EMPLOYMENT OF EMILY SHEA FOR POSITIVE/NEGATIVE CASES
#  >>--------------------------------------------------------------------------------------------->
#    [2010-08-26 -DCH- 2011-05-22]
#                                      [2013-09-03 -- SCH--- 2014-05-29]
#
#
#  a13489364c2eb015c219172d561c62350f0453f3_id - Daybreak Central High (DCH)
#  897755cae2f689c2d565a35a48ea69d5dd3928d6_id - Sunset Central High (SCH)

  Scenario: The extract for an edorg should contain data for a student or staff from a previously enrolled school
    And I request the latest bulk extract delta using the api
    And I untar and decrypt the "inBloom" delta tarfile for tenant "Midgar" and appId "19cca28d-7357-4044-8df9-caad4b1c8ee4" for "897755cae2f689c2d565a35a48ea69d5dd3928d6_id"
    And I verify that an extract tar file was created for the tenant "Midgar"
    Then the extract contains a file for each of the following entities:
      | entityType                            |
      | grade                                 |
      | reportCard                            |
      | student                               |
      | studentAcademicRecord                 |
      | studentAssessment                     |
      | studentCohortAssociation              |
      | studentDisciplineIncidentAssociation  |
      | studentParentAssociation              |
      | studentProgramAssociation             |
      | studentSchoolAssociation              |
      | studentSectionAssociation             |
    And I verify this "student" file should contain:
      | id                                          | condition            |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id | entityType = student |
    And I verify this "studentAcademicRecord" file should contain:
      | id                                                                                     | condition                          |
      | e325f180753f2f170b2826a26112f1be229cdf63_ide536c2b89ee393a7767b597601b581fd9bbfe4e0_id | entityType = studentAcademicRecord |
      | e1ddd4b5c0c531a734135ecd461b33cab842c18c_id17e81b9ff5c5c728ec22ec3f40e975eea03570a6_id | entityType = studentAcademicRecord |
    And I verify this "studentAssessment" file should contain:
      | id                                          | condition                      |
      | abf6b39f8c841a247c3e4731a821ea8b86f1c5d1_id | entityType = studentAssessment |
    And I verify this "studentParentAssociation" file should contain:
      | id                                                                                     | condition                             |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id3382e80b35990e1ea89cdde30339fb0c4b79793d_id | entityType = studentParentAssociation |
    And I verify this "studentProgramAssociation" file should contain:
      | id                                                                                     | condition                              |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id6d3f9afe4b7a6fe871bf92aa46d6ee9cca56f6e5_id | entityType = studentProgramAssociation |
    And I verify this "studentCohortAssociation" file should contain:
      | id                                                                                     | condition                             |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_idb8cb9f9619c552284b43208290b8e2455137eeed_id | entityType = studentCohortAssociation |
    And I verify this "studentDisciplineIncidentAssociation" file should contain:
      | id                                                                                     | condition                                         |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id92709ce1b529f9825bd4ab623f292c12c083df8e_id | entityType = studentDisciplineIncidentAssociation |
    And I verify this "studentSchoolAssociation" file should contain:
      | id                                          | condition                             |
      | 89c3228f05f5d88d785b4788babbf12c02c9f3f4_id | entityType = studentSchoolAssociation |
    And I verify this "studentSectionAssociation" file should contain:
      | id                                                                                     | condition                              |
      | 49e048fa9d77126a719d5719cfc08c36170981b1_idd5df60e5ffe544f23eb3167542fc582215e6a7a2_id | entityType = studentSectionAssociation |
    And I verify this "grade" file should contain:
      | id                                                                                     | condition          |
      | e325f180753f2f170b2826a26112f1be229cdf63_idd17b5f2c25d83632142b68f96eae69c7c73ccdf4_id | entityType = grade |
      | e1ddd4b5c0c531a734135ecd461b33cab842c18c_idfce1fd8e96cffd8c1dbf505a6862acfcf914b01b_id | entityType = grade |
    And I verify this "reportCard" file should contain:
      | id                                                                                     | condition               |
      | e325f180753f2f170b2826a26112f1be229cdf63_ida74c24bab9a9ef60755b46422a8d480239498581_id | entityType = reportCard |
      | e1ddd4b5c0c531a734135ecd461b33cab842c18c_id9c0b53684b9d64742c653621239bdd92c6bc4288_id | entityType = reportCard |
    And I verify this "courseTranscript" file should contain:
      | id                                          | condition                     |
      | 9d80fafba1ac36587a60002bc83df1ebe13c7c36_id | entityType = courseTranscript |
      | f0e15138c37352a57aab8d70feb6a0cad6c59741_id | entityType = courseTranscript |

  Scenario: The extract for an edorg should not contain data for a former student or staff that's dated after the person has left
    And I request the latest bulk extract delta using the api
    And I untar and decrypt the "inBloom" delta tarfile for tenant "Midgar" and appId "19cca28d-7357-4044-8df9-caad4b1c8ee4" for "a13489364c2eb015c219172d561c62350f0453f3_id"
    And I verify that an extract tar file was created for the tenant "Midgar"
    Then the extract contains a file for each of the following entities:
      | entityType                            |
      | grade                                 |
      | reportCard                            |
      | student                               |
      | studentAcademicRecord                 |
      | studentAssessment                     |
      | studentCohortAssociation              |
      | studentDisciplineIncidentAssociation  |
      | studentParentAssociation              |
      | studentProgramAssociation             |
      | studentSchoolAssociation              |
      | studentSectionAssociation             |
    And I verify this "student" file should contain:
      | id                                          | condition            |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id | entityType = student |
    And I verify this "studentAcademicRecord" file should not contain:
      | id                                                                                     |
      | e1ddd4b5c0c531a734135ecd461b33cab842c18c_id074f8af9afa35d4bb10ea7cd17794174563c7509_id |
    And I verify this "studentAssessment" file should not contain:
      | id                                          |
      | b2542b105c09130bc7d3f81b471d1f0f0e481fd8_id |
    And I verify this "studentParentAssociation" file should contain:
      | id                                                                                     | condition                             |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id3382e80b35990e1ea89cdde30339fb0c4b79793d_id | entityType = studentParentAssociation |
    And I verify this "studentProgramAssociation" file should not contain:
      | id                                                                                     |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id58e9a8ae4486e96051b33876b20a8f2cac745408_id |
    And I verify this "studentCohortAssociation" file should not contain:
      | id                                                                                     |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_idcee230069953f0b915305f33ff9f061bfc832509_id |
    And I verify this "studentDisciplineIncidentAssociation" file should not contain:
      | id                                                                                     |
      | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id2b0fbf2af85b9e850e533ded46d26d77aeaa2e75_id |
    And I verify this "studentSchoolAssociation" file should not contain:
      | id                                          |
      | c5a10351b0957620192a7b1c0e3e6fd686173579_id |
    And I verify this "studentSectionAssociation" file should not contain:
      | id                                                                                     |
      | c44eb520d29bad5d60237f6addc22f769b3448aa_idaf30e6685a85c716c26d5e559bde27017f57f304_id |
    And I verify this "grade" file should not contain:
      | id                                                                                     |
      | e1ddd4b5c0c531a734135ecd461b33cab842c18c_idc215fa1253b26479fea38c153679913544bf3ad0_id |
    And I verify this "reportCard" file should not contain:
      | id                                                                                     |
      | e1ddd4b5c0c531a734135ecd461b33cab842c18c_idfa02e1c8575067c8b43bfaee7da6108ffb4da31d_id |
    And I verify this "courseTranscript" file should not contain:
      | id                                          |
      | 0bc385d7a20aa1a9df92627cd841d545d3052b3b_id |

##########################################################################
#    TIMELINE OF ENROLLMENT OF MATT SOLLARS FOR EDGE CASES
#  >>--------------------------------------------------------------------->
#    [2009-01-01 -DCH- 2009-12-25]   [2010-08-26 -DCH- 2011-09-05]
#                                                    [2010-09-01 -----------ESH----------------------- 2014-05-22]
#                                                                [2011-09-05 --SCH--- 2013-05-22]
#                                                                 [2011-09-06 --WSH-- 2013-05-23]
#
# Student's end date in DCH is the same as the begin date of SCH
# WSH's begin date is one day after SCH's.
#
#    TIMELINE OF EMPLOYMENT OF CHARLES GRAY FOR EDGE CASES
#  >>--------------------------------------------------------------------------------------------->
#    [2007-05-06 -DCH- 2008-07-16] [2009-08-26 -DCH- 2011-07-22]
#                                             [2010-08-26 ------------ ESH --------- 2013-05-22]
#                                                              [2011-07-22 --- WSH--- 2013-05-23]
#                                                               [2011-07-23 ----- SCH---------- 2018-05-22]
#
# Staff's end date in DCH is the same as the begin date of WSH
# SCH's begin date is one day after WSH's.
#
#
#  a13489364c2eb015c219172d561c62350f0453f3_id - Daybreak Central High (DCH)
#  f63789e8d9f3c8aa4d42bdbec83ca64cc1d2ee16_id - East Side High (ESH)
#  897755cae2f689c2d565a35a48ea69d5dd3928d6_id - Sunset Central High (SCH)
#  b78524194f38795a5c2e422cb7fc8becece062d0_id - West Side High (WSH)

  Scenario: Edge Cases for student and staff enrollment
    And I request the latest bulk extract delta using the api
    And I untar and decrypt the "inBloom" delta tarfile for tenant "Midgar" and appId "19cca28d-7357-4044-8df9-caad4b1c8ee4" for "a13489364c2eb015c219172d561c62350f0453f3_id"
    And there is a metadata file in the extract
    And I verify this "student" file should contain:
      | id                                          | condition            |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id | entityType = student |
    And I verify this "studentParentAssociation" file should contain:
      | id                                                                                     | condition                             |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_idc43bbfa3df05d4fd2d78a9edfee8fd63fbcf495a_id | entityType = studentParentAssociation |
  #This extract should contain content for anything that began on or before DCH's end date with the student
  #Even data from SCH that began on the student's final day with DCH should be included
    And I verify this "studentAssessment" file should contain:
      | id                                          | condition                      |
      | 5154384148dbad6bc84a4b20b1c312e99fb3c004_id | entityType = studentAssessment |
      | 4c7a3fb655b56351f244c3d87bad76fd07b8478f_id | entityType = studentAssessment |
      | 02cbe22e355aea8e59f976247bae5389c491176d_id | entityType = studentAssessment |
    And I verify this "studentAcademicRecord" file should contain:
      | id                                                                                     | condition                          |
      | 429dc90b61707fa474005db798cec3f46807fa69_id1a62a16dd757629cf502eeeaa9fd4494a0fff115_id | entityType = studentAcademicRecord |
    And I verify this "studentProgramAssociation" file should contain:
      | id                                                                                     | condition                              |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_ide34acabe3e308a140d76b7bd2da54011be117110_id | entityType = studentProgramAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id56c2e2108230cfdd4fc0602921f4ee724ff8b1a2_id | entityType = studentProgramAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_ide2285301a2915907a047b8343f0522de2300031b_id | entityType = studentProgramAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id16ec8cd2cdf977761aa6105868be5339c12e19bc_id | entityType = studentProgramAssociation |
    And I verify this "studentCohortAssociation" file should contain:
      | id                                                                                     | condition                             |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id388413bdbb0059dd85a0451fe1c6ea8c5475d4d1_id | entityType = studentCohortAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id293b5f08004c4385b121091e2cd72a1a33e39392_id | entityType = studentCohortAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_idfe9b9e96676d530866cf5b742ea265d76f0d8a24_id | entityType = studentCohortAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id7de288576a0f32b99789d8f3a6cb773200794aa8_id | entityType = studentCohortAssociation |
    And I verify this "studentDisciplineIncidentAssociation" file should contain:
      | id                                                                                     | condition                                         |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_idd178f903e8fc7f13da40eff90fe04289f8d60180_id | entityType = studentDisciplineIncidentAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id7c81a5b5c57d1eacf611875aa87c44e57e2d4422_id | entityType = studentDisciplineIncidentAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id7776723a42cad712a6771a01aec0d7bb4b4c4ec9_id | entityType = studentDisciplineIncidentAssociation |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id126ec69e8441ecd01db21b4a68b74026e7cfb1b9_id | entityType = studentDisciplineIncidentAssociation |
    And I verify this "studentSectionAssociation" file should contain:
      | id                                                                                     | condition                              |
      | 6e9504c3061a61384cbd9591a52893f07c6af242_id56a60187f236b69252b085c0ca55c9a1cb8081ab_id | entityType = studentSectionAssociation |
      | 8887531ea737afbd49da86f201e95d1f6fc45571_id50a30c780c85361faec9ac20013d347a619958fc_id | entityType = studentSectionAssociation |
      | 5c593810111e06cd8b5a4e7f315b5b49c16c35b1_id26f3811034fc7d29bdc8ac5250ab1a9fe3ce97d7_id | entityType = studentSectionAssociation |
    And I verify this "studentSchoolAssociation" file should contain:
      | id                                          | condition                             |
      | 85021515aa5e6b0133d6830aa5a01856a78ecad3_id | entityType = studentSchoolAssociation |
      | 1c560036515238f702e031799673dbb6994d1eaf_id | entityType = studentSchoolAssociation |
      | 728e9428a82847723ed9eab66fa04003827228ee_id | entityType = studentSchoolAssociation |
      | e0c566a0bd2c94298c117c6220ddf0c2465c0945_id | entityType = studentSchoolAssociation |
    And I verify this "grade" file should contain:
      | id                                                                                     | condition          |
      | 429dc90b61707fa474005db798cec3f46807fa69_idab729c89eb9aa10765955b0da2f6c9bd4e1a2bb6_id | entityType = grade |
      | 429dc90b61707fa474005db798cec3f46807fa69_id6e4d4b52f5caa38f1ae6063fca428908f2c1575d_id | entityType = grade |
      | 429dc90b61707fa474005db798cec3f46807fa69_id3b746ce9c454a4ef2bb8b29c9672bbf1e75a704c_id | entityType = grade |
    And I verify this "reportCard" file should contain:
      | id                                                                                     | condition               |
      | 429dc90b61707fa474005db798cec3f46807fa69_id42fd81249cbc0c15bb99024e300b4d6f801d9a0f_id | entityType = reportCard |
    And I verify this "courseTranscript" file should contain:
      | id                                          | condition                     |
      | cb154b7f3fdb1ed9a62a5343c6d4d78addc8d444_id | entityType = courseTranscript |
      | c2c71979a917b74578950b6f976c4314acc9969f_id | entityType = courseTranscript |
      | 5a214a3e596887dffeaf44fdabd4535f33a96646_id | entityType = courseTranscript |

  #This extract should not contain content for anything that began after DCH's end date with the student
  #Given proper data, everything from WSH shouldn't be included
    And I verify this "studentAssessment" file should not contain:
      | id                                          |
      | 6697f6850c06b1458bebff42b9ea9b5fdb444e95_id |
    And I verify this "studentAcademicRecord" file should not contain:
      | id                                                                                     |
      | b28bc3be4667c80070094a24e1f7bc3a9b9a2893_id9f850bb17e294c429148d0b353f9e0db6c17338c_id |
    And I verify this "studentProgramAssociation" file should not contain:
      | id                                                                                     |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id3401ad622b20c8502b936844cf68293b27c1957e_id |
    And I verify this "studentCohortAssociation" file should not contain:
      | id                                                                                     |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_idb675acc4cb309496b14c25e7c3d74d07b60d68ae_id |
    And I verify this "studentDisciplineIncidentAssociation" file should not contain:
      | id                                                                                     |
      | 067198fd6da91e1aa8d67e28e850f224d6851713_id488486ca968826efacf5c1941c04e3ab30b83dc9_id |
    And I verify this "studentSectionAssociation" file should not contain:
      | id                                                                                     |
      | 7df01fe133b2605d0007dd1fecf9c8f8bc6afbee_id07bec3af9633c4bdde1a240e8b003abac7e4fc47_id |
      | 96ad14342c72b986ff6fe42556edb552abd239ca_idf146ce1a9ecb6f7852c9b48f36fee2d0f1bfcd0c_id |
    And I verify this "studentSchoolAssociation" file should not contain:
      | id                                          |
      | 6beed2cdd5386603d5b0b0e34eb1f87d091e9eb7_id |
    And I verify this "grade" file should not contain:
      | id                                                                                     |
      | b28bc3be4667c80070094a24e1f7bc3a9b9a2893_id0d1ec4954d2640bf3ae2eb758f6c8c86d820dbb1_id |
    And I verify this "reportCard" file should not contain:
      | id                                                                                     |
      | b28bc3be4667c80070094a24e1f7bc3a9b9a2893_id5d7185b665b99461cb92d18f09635d1212eda10b_id |
    And I verify this "courseTranscript" file should not contain:
      | id                                          |
      | b848986b74335a114ebee017c4f70659f96850db_id |



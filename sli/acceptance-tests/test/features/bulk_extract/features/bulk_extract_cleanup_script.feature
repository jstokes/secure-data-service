@RALLY_US5716

Feature: As an bulk extract user, I will be able to cleanup bulk extract file with the script

  Background: Prepare data and database
    Given the extraction zone is empty
    And the bulk extract files in the database are scrubbed
    And I have a fake bulk extract tar file for the following tenants and different dates:
     |  tenant     |    app   |          date              |    Edorg               |
     |  Midgar     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
     |  Midgar     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
     |  Midgar     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
     |  Hyrule     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
     |  Hyrule     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
     |  Hyrule     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |

  Scenario: I clean up all bulk extract file for the tenant
    Given the following test tenant and edorg are clean:
     |   tenant    |    Edorg             |
     |   Midgar    |  Daybreak-clean-test |
     |   Midgar    |  Sunset-clean-test   |
     |   Hyrule    |  Daybreak-clean-test |
     |   Hyrule    |  Sunset-clean-test   |
    And I add all the test edorgs
     |   tenant    |    Edorg             |
     |   Midgar    |  Daybreak-clean-test |
     |   Midgar    |  Sunset-clean-test   |
     |   Hyrule    |  Daybreak-clean-test |
     |   Hyrule    |  Sunset-clean-test   |
    When I execute cleanup script for tenant:"Midgar", edorg:"", date:"", path:""
    Then I should not see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg               |
      |  Midgar     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Midgar     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Midgar     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
    And I should see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg               |
      |  Hyrule     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |

  
  Scenario: I clean up all bulk extract file for a Edorg in tenant
    When I execute cleanup script for tenant:"Midgar", edorg:"Daybreak-clean-test", date:"", path:""
    Then I should not see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg               |
      |  Midgar     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Midgar     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
    And I should see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg    |
      |  Midgar     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
  
  Scenario: I clean up all bulk extract file for a Edorg in tenant up to certain date
    When I execute cleanup script for tenant:"Midgar", edorg:"Daybreak-clean-test", date:"2013-04-08T14:07:43.870Z", path:""
    Then I should not see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg    |
      |  Midgar     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Midgar     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
    And I should see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg    |
      |  Midgar     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |

  Scenario: I clean up all bulk extract file for a Edorg in tenant up to certain date
    When I execute cleanup script for tenant:"Midgar", edorg:"Daybreak-clean-test", date:"2013-04-08", path:""
    Then I should not see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg               |
      |  Midgar     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Midgar     |    a1    | 2013-04-08T03:59:00.870Z   |    Daybreak-clean-test |
    And I should see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg               |
      |  Midgar     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |

  Scenario: I clean up all bulk extract file for a tenant up to certain date
    When I execute cleanup script for tenant:"Midgar", edorg:"", date:"2013-05-09T14:07:43.870Z", path:""
    Then I should not see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg    |
      |  Midgar     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Midgar     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Midgar     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
    And I should see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg    |
      |  Hyrule     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |

  Scenario: I clean up a bulk extract file for a tenant
    When I execute cleanup script for tenant:"Midgar", edorg:"", date:"", path:"Daybreak-clean-test/Daybreak-clean-test-a1-2013-04-08-10-07-43.tar"
    When I execute cleanup script for tenant:"Midgar", edorg:"", date:"", path:"Daybreak-clean-test/Daybreak-clean-test-a1-2013-04-08-10-07-43.tar"
    Then I should see error message
    When I only remove bulk extract file for tenant:"Midgar", edorg:"Sunset-clean-test", app:"a1", date:"2013-05-08T14:07:43.870Z"
    When I execute cleanup script for tenant:"Midgar", edorg:"Sunset-clean-test", date:"2013-05-08T14:07:43.870Z", path:""
    Then I should see warning message
    Then I should not see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg    |
      |  Midgar     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Midgar     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
    And I should see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg    |
      |  Midgar     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |

  Scenario: I clean up all bulk extract file with incorrect input
    When I execute cleanup script for tenant:"Midgar", edorg:"Daybreak-clean-test", date:"2013-04-08T14:07:43.870Z", path:"Daybreak-clean-test/Daybreak-clean-test-a1-2013-04-08-10-07-43.tar"
    Then I should see error message
    When I execute cleanup script for tenant:"", edorg:"Daybreak-clean-test", date:"2013-04-08T14:07:43.870Z", path:"test-"
    Then I should see error message
    And I should see the following tenant bulk extract file:
      |  tenant     |    app   |          date              |    Edorg               |
      |  Midgar     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Midgar     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Midgar     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-04-08T14:07:43.870Z   |    Daybreak-clean-test |
      |  Hyrule     |    a1    | 2013-05-08T14:07:43.870Z   |    Sunset-clean-test   |
      |  Hyrule     |    a2    | 2013-03-08T14:07:43.870Z   |    Daybreak-clean-test |


  Scenario: Cleanup the old data
    Then I clean up the cleanup script test data
    And the following test tenant and edorg are clean:
     |   tenant    |    Edorg             |
     |   Midgar    |  Daybreak-clean-test |
     |   Midgar    |  Sunset-clean-test   |
     |   Hyrule    |  Daybreak-clean-test |
     |   Hyrule    |  Sunset-clean-test   |
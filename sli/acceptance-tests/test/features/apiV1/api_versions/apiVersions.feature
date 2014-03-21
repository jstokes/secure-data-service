@RALLY_US5029
Feature: As an API user, I want to be able to make requests to different versions of the
  API and receive appropriate responses.

# TODO: Review with product? Is this test legitimate? It seems weird that we can supply *any* version to the API and it will work.

Background: Nothing yet
    Given I want to use format "application/vnd.slc+json"

Scenario Outline: Validate all links returned by the API are versioned based on input
    Given I am logged in using "<user name>" "<password>" to realm "<realm>"
     When I navigate to GET "/<version>/home"
     Then I should receive a return code of 200
      And all returned links should be version "<version>"
Examples:
| user name | password      | realm | version |
| rrogers   | rrogers1234   | IL    | v1.0    |
| cgray     | cgray1234     | IL    | v1.0    |
| rrogers   | rrogers1234   | IL    | v1.1    |
| cgray     | cgray1234     | IL    | v1.1    |

Scenario Outline: Validate v1 resources
    Given I am logged in using "<user name>" "<password>" to realm "<realm>"
    When I navigate to GET "/<version>/<resource>"
    Then I should receive a return code of 200
Examples:
    | user name | password      | realm | version | resource   |
    | rrogers   | rrogers1234   | IL    | v1      | students   |
    | rrogers   | rrogers1234   | IL    | v1      | sections   |
    | rrogers   | rrogers1234   | IL    | v1      | schools    |
    | rrogers   | rrogers1234   | IL    | v1      | teachers   |
    | rrogers   | rrogers1234   | IL    | v1      | courses    |

Scenario Outline: Validate v1.0 resources
    Given I am logged in using "<user name>" "<password>" to realm "<realm>"
    When I navigate to GET "/<version>/<resource>"
    Then I should receive a return code of 200
Examples:
    | user name | password      | realm | version | resource   |
    | rrogers   | rrogers1234   | IL    | v1.0    | students   |
    | rrogers   | rrogers1234   | IL    | v1.0    | sections   |
    | rrogers   | rrogers1234   | IL    | v1.0    | schools    |
    | rrogers   | rrogers1234   | IL    | v1.0    | teachers   |
    | rrogers   | rrogers1234   | IL    | v1.0    | courses    |

Scenario Outline: Validate v1.1 resources
    Given I am logged in using "<user name>" "<password>" to realm "<realm>"
    When I navigate to GET "/<version>/<resource>"
    Then I should receive a return code of 200
Examples:
    | user name | password      | realm | version | resource   |
    | rrogers   | rrogers1234   | IL    | v1.1    | students   |
    | rrogers   | rrogers1234   | IL    | v1.1    | sections   |
    | rrogers   | rrogers1234   | IL    | v1.1    | schools    |
    | rrogers   | rrogers1234   | IL    | v1.1    | teachers   |
    | rrogers   | rrogers1234   | IL    | v1.1    | courses    |

Scenario Outline: Validate non v1.0 resources
    Given I am logged in using "<user name>" "<password>" to realm "<realm>"
    When I navigate to GET "/<version>/<resource>"
    Then I should receive a return code of 404
Examples:
    | user name | password      | realm | version | resource |
    | rrogers   | rrogers1234   | IL    | v1.0    | search   |


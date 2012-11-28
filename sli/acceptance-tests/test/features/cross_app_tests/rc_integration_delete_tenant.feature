@RALLY_US4835
@rc
Feature: Delete tenant and drop tenant database

Background: Make a connection to Mongo
  Given I have a connection to Mongo

Scenario: Delete tenant from sli.tenant and drop tenant database
  When I get the database name
  Then I will drop the whole database
  And I will drop the tenant document from the collection

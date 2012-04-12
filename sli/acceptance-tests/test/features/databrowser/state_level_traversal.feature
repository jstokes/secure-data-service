
@wip
Feature: State level traversal of data

Scenario: Leader traversing through the available state data in the Data Browser

  Given I am an authenticated SEA/LEA end user
And my role is Leader
And the Data Browser is approved for every district
When I open the Data Browser
Then I am given my home URLs
And I am able to traverse through all of the data within my district
And that data includes free or reduced lunch

Scenario: IT Administrator trying to find teacher when a teacher's district have denied the Data Browser

Given I am an authenticated SEA/LEA end user
And my role is IT Administrator
And the Data Browser is denied for <district>
When I open the Data Browser
Then I can not traverse to the <teacher> within <district>

Scenario: IT Administrator trying to edit data through API in a school within own state

Given I am an authenticated SEA end user
And my role is IT Administrator
And the <district> have approved the Data Browser
When I impersonate that I am using Data Browser 
And I make a call to edit any data in my <district> school
Then the data is edited

Scenario: Aggregate Viewer traversing through the available state data in the Data Browser when all districts have approve the Data Browser

Given I am an authenticated SEA end user
And my role is Aggregate Viewer
And every districts within my state have approved the Data Browser
When I open the Data Browser
Then I am given my home URLs
And I am able to traverse through the educational organization hierarchy
And I can see the edorg info only

Scenario: Aggregate Viewer trying to access edorg data for a district that have not approved the Data Browser

Given I am an authenticated SEA end user
And my role is Aggregate Viewer
And <district> have not approved the Data Browser
And I open the Data Browser
When I try to access an edor within (including) <district>
Then I get a message that I am not authorized 

Scenario: Aggregate Viewer trying to access non-edorg info data through API

Given I am an authenticated SEA end user
And my role is Aggregate Viewer
When I make an API call to get student within a school of my district
Then I get message that I am not authorized
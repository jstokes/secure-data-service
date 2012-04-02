@wip
Feature: Test CRUD fuctionality of Custom Entities per application 
Application is authorized using OAuth (there is an client_id and client_secret).  Custom entities are partitioned by application. Access control inherited from parent.


Background: None


Scenario:  As an IT Admin, I want to add custom entitiy to a core entity belonging to my application
   #create a new custom entity for demoClient
    Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
    And the clientID is "demoClient"
    And I am authenticated on "SLI"
   
    Given format "application/json"
    And a valid entity json object for a "educationOrganizations"
    And I add a key value pair "CustomConfig" : "<?xml version=1.0?><DisplayName>ISAT Reading Results</DisplayName>" to the object
	When I navigate to POST "/<EDUCATION ORGANIZATION URI>/<EDUCATION ORGANIZATION ID>/<CUSTOM URI>"
	Then I should receive a return code of 204
	
	#crete a new custom entity for sampleApplication
	Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
    And the clientID is "SampleApplication"
     And I am authenticated on "SLI"
     
    Given format "application/json" 
    And a valid entity json object for a "educationOrganizations"
    And I add a key value pair "CustomConfig" : "<?xml version=1.0?><DisplayName>SAT Scores</DisplayName>" to the object
	When I navigate to POST "/<EDUCATION ORGANIZATION URI>/<EDUCATION ORGANIZATION ID>/<CUSTOM URI>"
	Then I should receive a return code of 204
	
	#retrieve correct custom entity for correct application
   Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
    And the clientID is "demoClient"
    And I am authenticated on "SLI"
    
	When I navigate to GET "/<EDUCATION ORGANIZATION URI>/<EDUCATION ORGANIZATION ID>/<CUSTOM URI>"
	Then I should receive a key value pair "CustomConfig" : "<?xml version=1.0?><DisplayName>ISAT Reading Results</DisplayName>" in the result


Scenario: As an IT Admin, I want to update custom entity associated with any core entity belonging to my application

    Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
    And the clientID is "demoClient"
    And I am authenticated on "SLI"
    
    Given format "application/json" 
    And a valid entity json object for a "educationOrganizations"
    And I add a key value pair "CustomConfig" : "<?xml version=1.0?><DisplayName>ISAT Reading Results</DisplayName>" to the object
	When I navigate to POST "/<EDUCATION ORGANIZATION URI>/<EDUCATION ORGANIZATION ID>/<CUSTOM URI>"
	Then I should receive a return code of 204

	Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
    And the clientID is "SampleApplication"
    And I am authenticated on "SLI"
    
     Given format "application/json"
    And a valid entity json object for a "educationOrganizations"
    And I add a key value pair "ColumnConfig" : "<?xml version=1.0?><DisplayName>SAT Scores</DisplayName>" to the object
	When I navigate to POST "/<EDUCATION ORGANIZATION URI>/<EDUCATION ORGANIZATION ID>/<CUSTOM URI>"
	Then I should receive a return code of 204

	Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
    And the clientID is "demoClient"
    And I am authenticated on "SLI"
    
    Given format "application/json"
    And a valid entity json object for a "educationOrganizations"
    And I add a key value pair "CustomConfig" : "<?xml version=1.0?><DisplayName>ISAT Writing Results</DisplayName>" to the object
	When I navigate to PUT "/<EDUCATION ORGANIZATION URI>/<EDUCATION ORGANIZATION ID>/<CUSTOM URI>"
	Then I should receive a return code of 204
	
	When I navigate to GET "/<EDUCATION ORGANIZATION URI>/<EDUCATION ORGANIZATION ID>/<CUSTOM URI>"
	Then I should receive a key value pair "CustomConfig" : "<?xml version=1.0?><DisplayName>ISAT Writing Results</DisplayName>" in the result
	
	Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
    And the clientID is "SampleApplication"
    And I am authenticated on "SLI"
    
	When I navigate to GET "/<EDUCATION ORGANIZATION URI>/<EDUCATION ORGANIZATION ID>/<CUSTOM URI>"
	Then I should receive a key value pair "ColumnConfig" : "<?xml version=1.0?><DisplayName>SAT Scores</DisplayName>" in the result

Scenario Outline: As an educator or leader, I want to read a custom entity associated with any core entity belonging to my application 
	   Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
       And the clientID is "demoClient"
       And I am authenticated on "SLI"
	    
	    Given format "application/json"
	    And a valid entity json object for a "students"
	    And I add a key value pair "Drives" : "True" to the object
		When I navigate to POST "/<STUDENT URI>/<STUDENT ID>/<CUSTOM URI>"
		Then I should receive a return code of 204
		
		Given  I am a valid SEA/LEA end user <Username> with password <Password>
	    And the clientID is <ClientID>
	     And I am authenticated on "SLI"
	     
	When I navigate to GET "/<STUDENT URI>/<STUDENT ID>"?includeCustom="true"
		Then I should receive a return code of <Code>
		And I should recieve the "student" object with "<STUDENT ID>"
		#And additionally I should receive a key value pair <Key> : <Value> in the result
		
	When I navigate to GET "/<STUDENT URI>/<STUDENT ID>"?includeCustom="false"
		Then I should receive a return code of <Code>
		And I should recieve the "student" object with "<STUDENT ID>"
		#And there is no other custom data returned
		
		When I navigate to GET "/<STUDENT URI>/<STUDENT ID>"
		Then I should receive a return code of <Code>
		And I should recieve the "student" object with "<STUDENT ID>"
	#	And there is no other custom data returned
		
	Examples:
	| Username        | Password            | AnyDefaultSLIRole  | ClientID     | Key            | Value    | Code  |
    | "leader"        | "leader1234"        | "Leader"           | "demoClient" | "Drives"       | "True"   | 200   |
 #	| "aggregator"    | "aggregator1234"    | "AggregateViewer"  | "demoClient" | ""             | ""       | 403   |
 	
Scenario Outline: As an user, I want to delete a custom entity associated with any core entity belonging to my application
	 Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
       And the clientID is "demoClient"
       And I am authenticated on "SLI"
       
	     Given format "application/json"
	    And a valid entity json object for a "students"
	     And I add a key value pair "Drives" : "True" to the object
		When I navigate to POST "/<STUDENT URI>/<STUDENT ID>/<CUSTOM URI>"
		Then I should receive a return code of 204
		
		Given  I am a valid SEA/LEA end user <Username> with password <Password>
	    And the clientID is <ClientID>
	     And I am authenticated on "SLI"
	    
	    Given format "application/json" 
		When I navigate to DELETE "/<STUDENT URI>/<STUDENT ID>/<CUSTOM URI>"
		Then I should receive a return code of <DelCode>
		
		Given format "application/json"
		When I navigate to GET "/<STUDENT URI>/<STUDENT ID>/<CUSTOM URI>"
		Then I should receive a return code of <ReadCode>
		And I should receive a key value pair <Key> : <Value> in the result

		
	Examples:
	| Username        | Password            | AnyDefaultSLIRole  | ClientID     | Key            | Value    | DelCode   | ReadCode |
	| "leader"        | "leader1234"        | "Leader"           | "demoClient" | "Drives"       | "True"   |  403      |  200     |
	| "aggregator"    | "aggregator1234"    | "AggregateViewer"  | "demoClient" | ""             | ""       |  403      |  403     |
#	| "demo"          | "demo1234"          | "ITAdmin"          | "demoClient" | ""             | ""       |  204      |  404     |

Scenario Outline: As an user, I want to update  a custom entity associated with any core entity belonging to my application 
	 Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
       And the clientID is "demoClient"
       And I am authenticated on "SLI"
       
	     Given format "application/json"
	    And a valid entity json object for a "students"
	     And I add a key value pair "Drives" : "True" to the object
		When I navigate to POST "/<STUDENT URI>/<STUDENT ID>/<CUSTOM URI>"
		Then I should receive a return code of 204
		
		Given  I am a valid SEA/LEA end user <Username> with password <Password>
	    And the clientID is <ClientID>
	     And I am authenticated on "SLI"
	     
	     Given format "application/json" 
	   And a valid entity json object for a "students"
	    And I add a key value pair <Key> : <Value> to the object
		When I navigate to PUT "/<STUDENT URI>/<STUDENT ID>/<CUSTOM URI>"
		Then I should receive a return code of <Code>

	Examples:
	| Username        | Password            | AnyDefaultSLIRole  | ClientID     | Key            | Value    | Code   | 
	| "leader"        | "leader1234"        | "Leader"           | "demoClient" | "Drives"       | "True"   |  403   |
	| "aggregator"    | "aggregator1234"    | "AggregateViewer"  | "demoClient" | "Drives"       | "True"   |  403   |
	| "demo"          | "demo1234"          | "ITAdmin"          | "demoClient" | "Drives"       | "True"   |  204   |
	
Scenario Outline: As an user, I want to create  a custom entity associated with any association belonging to my application 		
		Given  I am a valid SEA/LEA end user <Username> with password <Password>
	     And the clientID is <ClientID>
	     And I am authenticated on "SLI"
	     
	    Given format "application/json"
	    And a valid entity json object for a "studentSchoolAssociations"
	    And I add a key value pair <Key> : <Value> to the object
		When I navigate to <Action> "/<STUDENT SCHOOL ASSOCIATION URI>/<STUDENT SCHOOL ASSOC ID>/<CUSTOM URI>"
		Then I should receive a return code of <Code>	
	Examples:
	| Username        | Password            | AnyDefaultSLIRole  | ClientID     | Key                       | Value     | Code   | Action |
     | "leader"        | "leader1234"        | "Leader"           | "demoClient" | "currentlyEnrolled"       | "False"   |  403   | POST   |
	| "aggregator"    | "aggregator1234"    | "AggregateViewer"  | "demoClient" | "currentlyEnrolled"       | "False"   |  403   | POST   |
	| "demo"          | "demo1234"          | "ITAdmin"          | "demoClient" | "currentlyEnrolled"       | "False"   |  204   | POST   | 
	

Scenario Outline: As an user, I want to update  a custom entity associated with any association belonging to my application 
	Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
       And the clientID is "demoClient"
       And I am authenticated on "SLI"
       
        Given format "application/json"
	    And a valid entity json object for a "studentSchoolAssociations"
	    And I add a key value pair "currentlyEnrolled" : "True" to the object
		When I navigate to POST "/<STUDENT SCHOOL ASSOCIATION URI>/<STUDENT SCHOOL ASSOC ID>/<CUSTOM URI>"
		Then I should receive a return code of 204
		
		Given  I am a valid SEA/LEA end user <Username> with password <Password>
	    And the clientID is <ClientID>
	     And I am authenticated on "SLI"
	    
	    Given format "application/json"  
	    And a valid entity json object for a "studentSchoolAssociations"
	    And I add a key value pair <Key> : <Value> to the object
		When I navigate to <Action> "/<STUDENT SCHOOL ASSOCIATION URI>/<STUDENT SCHOOL ASSOC ID>/<CUSTOM URI>"
		Then I should receive a return code of <Code>
	Examples:
	| Username        | Password            | AnyDefaultSLIRole  | ClientID     | Key                       | Value     | Code   | Action |
	| "leader"        | "leader1234"        | "Leader"           | "demoClient" | "currentlyEnrolled"       | "False"   |  403   | PUT    |
	| "aggregator"    | "aggregator1234"    | "AggregateViewer"  | "demoClient" | "currentlyEnrolled"       | "False"   |  403   | PUT    |
	| "demo"          | "demo1234"          | "ITAdmin"          | "demoClient" | "currentlyEnrolled"       | "False"   |  204   | PUT    | 
	

Scenario Outline: As an user, I want to delete and then read a custom entity associated with any association belonging to my application 		
		Given  I am a valid SEA/LEA end user "demo" with password "demo1234"
	    And the clientID is "demoClient"
	    And I am authenticated on "SLI"
	    
	     Given format "application/json" 
	    And a valid entity json object for a "studentSchoolAssociations"
	    And I add a key value pair "currentlyEnrolled" : "True" to the object
		When I navigate to POST "/<STUDENT SCHOOL ASSOCIATION URI>/<STUDENT SCHOOL ASSOC ID>/<CUSTOM URI>"
		Then I should receive a return code of 204
		
		Given  I am a valid SEA/LEA end user <Username> with password <Password>
	    And the clientID is <ClientID>
	    And I am authenticated on "SLI"
	    
	     Given format "application/json" 
	    And a valid entity json object for a "studentSchoolAssociations"
		When I navigate to <Action> "/<STUDENT SCHOOL ASSOCIATION URI>/<STUDENT SCHOOL ASSOC ID>/<CUSTOM URI>"
		Then I should receive a return code of <Code>	
		
		When I navigate to GET "/<STUDENT SCHOOL ASSOCIATION URI>/<STUDENT SCHOOL ASSOC ID>/<CUSTOM URI>"
		Then I should receive a return code of <ReadCode>
		And I should receive a key value pair <Key> : <Value> in the result
		
	Examples:
	| Username        | Password            | AnyDefaultSLIRole  | ClientID     | Key                       | Value     | Code   | Action | ReadCode |
	| "leader"        | "leader1234"        | "Educator"         | "demoClient" | "currentlyEnrolled"       | "True"    |  403   | DELETE |  200     |
	| "aggregator"    | "aggregator1234"    | "AggregateViewer"  | "demoClient" | ""                        | ""        |  403   | DELETE |  403     |
#	| "demo"          | "demo1234"          | "ITAdmin"          | "demoClient" | ""                        | ""        |  204   | DELETE |  404     |    



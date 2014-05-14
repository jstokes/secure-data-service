@smoke
Feature: Contextual Permissions for District level Staff (non-educators)

Scenario Outline: Staff accessing data from own district

Given I am logged in using <Username> <Password> to realm <Realm>
And I have a Role attribute that equals <Role>
And my "district" is <District>
When I try to access the data for <Data> in my "district" from the API
Then I get the data containing <Data> returned in json format
Examples:
	|Username    |Password        |Realm|Role      |District     |Data|
	|"gcanning"  |"gcanning1234"  |"NY" |"Leader"  |"NY-Parker"  |"Students in Parker Elementary"|
	|"sbantu"    |"sbantu1234"    |"IL" |"Leader"  |"IL-Daybreak"|"Teachers in South Daybreak Elementary"|
	|"mhahn"     |"mhahn1234"     |"NY" |"IT Admin"|"NY-Dusk"    |"Malcolm Haehn NY"|
	|"jstevenson"|"jstevenson1234"|"IL" |"IT Admin"|"IL-Daybreak"|"Linda Kim"|

Scenario Outline: Staff access data from another district

Given I am logged in using <Username> <Password> to realm <Realm>
And I have a Role attribute that equals <Role>
And my "district" is <District>
When I try to access the data for <Data> in another "district" from the API
Then I should receive a return code of <Code>
Examples:
	|Username    |Password        |Realm|Role      |District     |Data                                   | Code|
	|"eengland"  |"eengland1234"  |"NY" |"Leader"  |"NY-Dusk"    |"Students in Parker Elementary"        | 403 |
	|"eengland"  |"eengland1234"  |"NY" |"Leader"  |"NY-Dusk"    |"Students in South Daybreak Elementary"| 404 |
	|"sbantu"    |"sbantu1234"    |"IL" |"Leader"  |"IL-Daybreak"|"Mark Anthony"                         | 403 |
	|"llogan"    |"llogan1234"    |"IL" |"Leader"  |"IL-Sunset"  |"Matt Sollars"                         | 403 |
	|"jcarlyle"  |"jcarlyle1234"  |"NY" |"IT Admin"|"NY-Parker"  |"Teachers in Dawn Elementary"          | 403 |
	|"jstevenson"|"jstevenson1234"|"IL" |"IT Admin"|"IL-Daybreak"|"Students in AP Calculus Sec 201"      | 403 |

Scenario Outline: Staff listing teachers they have context to

Given I am logged in using <Username> <Password> to realm <Realm>
And I have a Role attribute that equals <Role>
And my "district" is <District>
When I try to access the data for "My Teachers" in my "district" from the API
Then The response contains an empty array
Examples:
  |Username    |Password        |Realm|Role      |District     |Data|
  |"eengland"  |"eengland1234"  |"NY" |"Leader"  |"NY-Dusk"    |"Teachers in Dusk District"|
  |"sbantu"    |"sbantu1234"    |"IL" |"Leader"  |"IL-Daybreak"|"Teachers in Daybreak District"|
  |"llogan"    |"llogan1234"    |"IL" |"Leader"  |"IL-Sunset"  |"Teachers in Sunset District"|
  |"jcarlyle"  |"jcarlyle1234"  |"NY" |"IT Admin"|"NY-Parker"  |"Teachers in Parker District"|

Scenario Outline: IT Administrator trying to edit data for own district

Given I am logged in using <Username> <Password> to realm <Realm>
And I have a Role attribute that equals "IT Administrator"
And my "district" is <District>
When I try to update the data for <Data> in my "district" from the API
Then I should receive a return code of 204
And the data should be updated
Examples:
	|Username|Password|Realm|District|Data|
	|"mhahn"     |"mhahn1234"     |"NY" |"NY-Dusk"    |"Malcolm Haehn NY"|
	|"jstevenson"|"jstevenson1234"|"IL" |"IL-Daybreak"|"Matt Sollars"|

Scenario Outline: IT Administrator trying to edit data for other district

Given I am logged in using <Username> <Password> to realm <Realm>
And I have a Role attribute that equals "IT Administrator"
And my "district" is <District>
When I try to update the data for <Data> in another "district" from the API
Then I should receive a return code of 403
Examples:
	|Username  |Password      |Realm|District   |Data|
	|"jcarlyle"|"jcarlyle1234"|"NY" |"NY-Parker"|"Malcolm Haehn NY"|
	|"mhahn"   |"mhahn1234"   |"NY" |"NY-Dusk"  |"Larissa Marney" |

@wip
Scenario Outline: Aggregate Viewer getting their available district data

Given I am logged in using <Username> <Password> to realm <Realm>
And I have a Role attribute that equals "Aggregate Viewer"
And my "district" is <District>
When I try to access the data for <Data> in my "district" from the API
Then I should get a response which includes the data containing <Data> returned in json format
Examples:
	|Username  |Password      |Realm|District     |Data|
	|"jjackson"|"jjackson1234"|"IL" |"IL-Daybreak"|"Schools in Daybreak District"|
	|"rlindsey"|"rlindsey1234"|"NY" |"NY-Dusk"    |"Schools in Dusk District"|
	|"jjackson"|"jjackson1234"|"IL" |"IL-Daybreak"|"South Daybreak Elementary"|

@wip
Scenario Outline: Aggregate Viewer trying to access non-school info data

Given I am logged in using <Username> <Password> to realm <Realm>
And I have a Role attribute that equals "Aggregate Viewer"
And my "district" is <District>
When I try to access the data for <Data> in my "district" from the API
Then I should receive a return code of 403
Examples:
	|Username  |Password      |Realm|District     |Data|
	|"jjackson"|"jjackson1234"|"IL" |"IL-Daybreak"|"Students in South Daybreak Elementary"|

Scenario: Aggregate Viewer trying to access non-school info data with security event checking

Given I am logged in using "rlindsey" "rlindsey1234" to realm "NY"
And I have a Role attribute that equals "Aggregate Viewer"
And my "district" is "NY-Dusk"
And the sli securityEvent collection is empty
When I try to access the data for "Teachers in Dawn Elementary" in my "district" from the API
Then I should receive a return code of 403
     And I check to find if record is in sli db collection:
        | collectionName  | expectedRecordCount | searchParameter         | searchValue                                            | searchType |
        | securityEvent   | 1                   | body.tenantId           | Hyrule                                                 | string     |
        | securityEvent   | 1                   | body.appId              | ke9Dgpo3uI                                             | string     |
        | securityEvent   | 1                   | body.className          | org.slc.sli.api.service.BasicService                   | string     |
        | securityEvent   | 1                   | body.userEdOrg          | NY-STATE-NYC                                           | string     |
        | securityEvent   | 1                   | body.targetEdOrgList    | 1000000122                                             | string     |
     And "1" security event with field "body.actionUri" matching "http.*/api/rest/v.*/schools/.*/teacherSchoolAssociations/teachers" should be in the sli db
     And "1" security event matching "Access Denied:Access to resource denied." should be in the sli db

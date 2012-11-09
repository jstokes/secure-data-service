@smoke
Feature: SLI admin smoke tests

Background:
Given I have an open web browser

Scenario: SLC Developer operations part 1
Given I am a valid SLC developer
When I authenticate on the Application Registration Tool
Then I see the list of my registered applications only
And I have clicked to the button New
And I am redirected to a new application page
When I entered the name "Smoke!" into the field titled "Name"
And I have entered data into the other required fields except for the shared secret and the app id which are read-only
And I click on the button Submit
Then I am redirected to the Application Registration Tool page
And the application "Smoke!" is listed in the table on the top
And the client ID and shared secret fields are Pending
And the Registration Status field is Pending

Scenario: SLC Operator operations
Given I am a valid SLC Operator
When I authenticate on the Application Registration Tool
And I see all the applications registered on SLI
And I see all the applications pending registration
And the pending apps are on top
When I click on 'Approve' next to application "Smoke!"
Then application "Smoke!" is registered
And the 'Approve' button is disabled for application "Smoke!"
When I navigate to the account management page
Then I see a table with headings of "Vendor" and "Username" and "Last Update" and "Status" and "Actions"
And on the next line there is vendor name in the "Vendor" column
And User Name in the "User Name" column
And last update date in the "Last Updated" column
And status in the "Status" column
And the "Action" column has 4 buttons "Approve", "Reject", "Disable", and "Enable"

Scenario: SLC Developer operations part 2
Given I am a valid SLC developer
When I authenticate on the Application Registration Tool
Then I see the list of my registered applications only
And I clicked on the button Edit for the application "Smoke!"
Then I can see the on-boarded states
When I select a state
  Then I see all of the Districts
  Then I check the Districts
When I click on Save
Then I am redirected to the Application Registration Tool page


Scenario: LEA Administrator operations
Given I am a valid district administrator
When I authenticate on the Admin Delegation Tool
And I am redirected to the delegation page for my district
And "Application Authorization" is unchecked
And I check the "Application Authorization"
And I should click the "Save" button
Then I am redirected to the delegation page for my district
And "Application Authorization" is checked
And I uncheck the "Application Authorization"
And I should click the "Save" button
Then I am redirected to the delegation page for my district
And "Application Authorization" is unchecked
When I hit the Admin Application Authorization Tool
And I see an application "Smoke!" in the table
And in Status it says "Not Approved"
And I click on the "Approve" button next to it
And I am asked 'Do you really want this application to access the district's data'
When I click on Ok
Then the application is authorized to use data of "Sunset School District"
And is put on the top of the table
And the Status becomes "Approved"
And it is colored "green"
And the Approve button next to it is disabled
And the Deny button next to it is enabled

Scenario: Realm administrator operations
Given I am a valid realm administrator
When I authenticate on the realm editing tool
#And I see pre-existing mappings
#When I click on the Reset Mapping button
#And I got warning message saying 'Are you sure you want to reset the role mappings?'
#When I click 'OK'
#Then the Leader, Educator, Aggregate Viewer and IT Administrator roles are now only mapped to themselves
#And I no longer see the pre-existing mappings
#When I hit the realm editing URL
And I should see that I am on the "Illinois Sunset School District 4526" edit page
And I should enter "Smoke" into the Display Name field
And I should click the "Save" button
Then I should be redirected back to the edit page
And I should receive a notice that the realm was successfully "updated"
And I should see that I am on the "Smoke" edit page
And I should enter "Illinois Sunset School District 4526" into the Display Name field
And I should click the "Save" button
Then I should be redirected back to the edit page


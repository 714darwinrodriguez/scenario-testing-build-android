@move
Feature: Move item

  As a user
  I want to move content to other location in my account
  so that the files and folders are place where i want

  Background: User is logged in
    Given user user1 is logged

  Scenario: Move an existent folder to another location
    Given the folder folderMove has been created in the account
    When user selects to Move the item folderMove
    And user selects Documents as target folder
    Then user should not see folderMove in the filelist anymore
    But user should see folderMove inside the folder Documents

@move
Feature: Move item

  As a user
  I want to move content to other location in my account
  so that the files and folders are place where i want

  Background: User is logged in
    Given user Alice is logged

  Scenario: Move an existent folder to another location
    Given the folder folderMove has been created in the account
    When Alice selects to Move the item folderMove
    And Alice selects Documents as target folder
    Then Alice should not see folderMove in the filelist anymore
    But Alice should see folderMove inside the folder Documents

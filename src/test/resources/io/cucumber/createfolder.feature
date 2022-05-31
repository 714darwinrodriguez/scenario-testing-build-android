@createfolder
Feature: Create a new folder

  As a user
  I want to create new folders in my account,
  so that i will put order in my file structure

  Background: User is logged in
    Given user Alice is logged

  @smoke
  Scenario: Create a new folder with a correct name
    When Alice selects the option Create Folder
    And Alice sets FolderTest as new name
    Then Alice should see FolderTest in the filelist

  Scenario: Create a new folder with an incorrect name
    Given the following items have been created in the account
      | folder   | Documents  |
    When Alice selects the option Create Folder
    And Alice sets Documents as new name
    Then Alice should see the following error
      | The resource you tried to create already exists |
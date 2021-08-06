@delete
Feature: Delete item

  As a user
  I want to be able to delete content from my account
  so that i can get rid of the files and folders i do not need anymore

  Background: User is logged in
    Given user Alice is logged

  Scenario: Delete an existent folder
    Given the following items have been created in the account
      | folder   | deleteMe  |
    When Alice selects to Remove the item deleteMe
    And Alice accepts the deletion
    Then Alice should not see deleteMe in the filelist anymore

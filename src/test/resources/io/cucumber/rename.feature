@rename
Feature: Rename an item

  As a user, i want to rename the items of my account
  so that i see clearer or different names as i need them

  Background: User is logged in
    Given user Alice is logged

  @smoke
  Scenario: Rename an item
    Given the following items have been created in the account
      | folder   |  RenameMe  |
    When Alice selects to Rename the item RenameMe
    And Alice sets Renamed as new name
    Then Alice should see Renamed in the filelist
    But Alice should not see Rename in the filelist anymore
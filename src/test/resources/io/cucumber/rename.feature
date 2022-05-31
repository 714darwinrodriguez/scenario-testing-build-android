@rename
Feature: Rename an item

  As a user, i want to rename the items of my account
  so that i see clearer or different names as i need them

  Background: User is logged in
    Given user Alice is logged

  @smoke
  Scenario Outline: Rename an item
    Given the following items have been created in the account
      | <type>   |  <name>  |
    When Alice selects to Rename the <type> <name>
    And Alice sets <newName> as new name
    Then Alice should see <newName> in the filelist
    But Alice should not see <name> in the filelist anymore

    Examples:
      | type     |  name            |  newName           |
      | folder   |  Renamefolder    |  Renamefolder2     |
      | file     |  Renamefile.txt  |  Renamefile2.txt   |

  Scenario: Rename an item with an existing name
    Given the following items have been created in the account
      | folder   | Photos     |
      | folder   | Documents  |
    When Alice selects to Rename the folder Photos
    And Alice sets Documents as new name
    Then Alice should see the following error
      | An error occurred while trying to move this file or folder |
@copy
Feature: Copy item

  As a user
  I want to copy files between different locations of my account
  So that i have different copies in different places that i can modify separately

  Background: User is logged in
    Given user Alice is logged

  @smoke
  Scenario Outline: Copy an existent item to another location
    Given the following items have been created in the account
      | <type>   | <name>  |
    When Alice selects to Copy the <type> <name>
    And Alice selects <target> as target folder
    Then Alice should see <name> in the filelist as original
    And Alice should see <name> inside the folder <target>

    Examples:
      | type     |  name          | target     |
      | folder   |  Copyfolder    | Documents  |
      | file     |  Copyfile.txt  | Documents  |

  Scenario: Copy a folder to itself
    Given the following items have been created in the account
      | folder   | copy2  |
    When Alice selects to Copy the folder copy2
    And Alice selects copy2 as target folder
    Then Alice should see the following error
      | It is not possible to copy a folder into a descendant |
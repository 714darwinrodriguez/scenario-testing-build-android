@move
Feature: Move item

  As a user
  I want to move content to other location in my account
  so that the files and folders are place where i want

  Background: User is logged in
    Given user Alice is logged

  @smoke
  Scenario Outline: Move an existent folder to another location
    Given the following items have been created in the account
      | <type>   | <name>  |
    When Alice selects to Move the <type> <name>
    And Alice selects <target> as target folder
    Then Alice should not see <name> in the filelist anymore
    But Alice should see <name> inside the folder <target>

    Examples:
      | type     |  name          | target     |
      | folder   |  Movefolder    | Documents  |
      | file     |  Movefile.txt  | Documents  |

  Scenario: Move a folder to itself
    Given the following items have been created in the account
      | folder   | move2  |
    When Alice selects to Move the folder move2
    And Alice selects move2 as target folder
    Then Alice should see the following error
      | It is not possible to move a folder into a descendant |
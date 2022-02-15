@copy
Feature: Copy item

  As a user
  I want to copy files between different locations of my account
  So that i have different copies in different places that i can modify separately

  Background: User is logged in
    Given user Alice is logged

  Scenario Outline: Copy an existent folder to another location
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
@links
Feature: Public Share

  As an user
  I want to create links on my files or folders
  So that the content is accessible for whom i send the link

  Background: User is logged in
    Given user user1 is logged

  Scenario Outline: Create a public link with name
    Given the <type> <item> has been created in the account
    When user selects to share the <type> <item>
    And user creates link on <type> <item> with the following fields
      | name | <name> |
    Then link should be created on <item> with the following fields
      | name | <name> |

    Examples:
      |  type     |  item              |  name    |
      |  folder   |  Links1            |  link1   |
      |  file     |  Links2.txt        |  link2   |

  Scenario Outline: Create a public link with password
    Given the <type> <item> has been created in the account
    When user selects to share the <type> <item>
    And user creates link on <type> <item> with the following fields
      | name     | <name>     |
      | password | <password> |
    Then link should be created on <item> with the following fields
      | name     | <name>     |
      | password | <password> |

    Examples:
      |  type     |  item        |  name    | password |
      |  folder   |  Links3      |  link3   |    a     |
      |  file     |  Links4.txt  |  link4   |    a     |

  @expiration
  Scenario Outline: Create a public link with expiration date
    Given the <type> <item> has been created in the account
    When user selects to share the <type> <item>
    And user creates link on <type> <item> with the following fields
      | name            | <name>  |
      | expiration days | <expiration>  |
    Then link should be created on <item> with the following fields
      | name            | <name>        |
      | expiration days | <expiration>  |

    Examples:
      |  type    |  item         |  name    | expiration     |
      |  folder  |  Links5       |  link5   |    7           |
      |  file    |  Links6.txt   |  link6   |    17          |

  Scenario Outline: Create a public link with permissions on a folder
    Given the folder <item> has been created in the account
    When user selects to share the folder <item>
    And user creates link on folder <item> with the following fields
      | name       | <name>        |
      | permission | <permissions> |
    Then link should be created on <item> with the following fields
      | name       | <name>        |
      | permission | <permissions> |

    Examples:
      |  item         |  name    | permissions |
      |  Links7       |  link7   |    15       |
      |  Links8       |  link8   |     4       |
      |  Links9       |  link9   |     1       |

  Scenario Outline: Edit existing share on a folder, changing permissions
    Given the folder <item> has been created in the account
    And the folder <item> has been already shared by link
    When user selects to share the folder <item>
    And user edits the link on <item> with the following fields
      | permissions | <permissions> |
      | name        | <name>        |
    Then link should be created on <item> with the following fields
      | permissions | <permissions> |
      | name        | <name>        |

    Examples:
      |  item     |  name    | permissions |
      |  Links10  |  link10  |     15      |
      |  Links11  |  link11  |     4       |
      |  Links12  |  link12  |     1       |

  @deletelink
  Scenario Outline: Delete existing link
    Given the <type> <item> has been created in the account
    And the <type> <item> has been already shared by link
    When user selects to share the <type> <item>
    And user deletes the link on <item>
    Then link on <item> should not exist anymore

    Examples:
      |  type   |  item         |
      |  folder |  Links13      |
      |  file   |  Links14.txt  |
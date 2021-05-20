@shares
Feature: Private Share

  As a user
  I want to share my content with other users in the platform
  So that the content is accessible and others can contribute

  Background: User is logged in
    Given user user1 is logged

  @smoke
  Scenario Outline: Correct share with user
    Given the <type> <item> has been created in the account
    When user selects to share the <type> <item>
    And user selects user user2 as sharee
    Then user  user2 should have access to <item>
    And share should be created on <item> with the following fields
      | sharee | user2 |

    Examples:
      |  type   |  item        |
      |  file   |  Share1.txt  |
      |  folder |  Share2      |

  Scenario Outline: Correct share with group
    Given the <type> <item> has been created in the account
    When user selects to share the <type> <item>
    And user selects group test as sharee
    Then group including user2 should have access to <item>
    And share should be created on <item> with the following fields
      | group | test |

    Examples:
      |  type   |  item        |
      |  file   |  Share3.txt  |
      |  folder |  Share4      |

  Scenario Outline: Correct federated share
    Given the <type> <item> has been created in the account
    When user selects to share the <type> <item>
    And user selects user demo@demo.owncloud.com as sharee
    Then share should be created on <item> with the following fields
      | sharee | demo@demo.owncloud.com |

    Examples:
      |  type   |  item        |
      |  file   |  Share5.txt  |
      |  folder |  Share6      |

    #Permissions
    # READ -> 1
    # UPDATE -> 2
    # CREATE -> 4
    # DELETE -> 8
    # SHARE -> 16

  Scenario Outline: Edit existing share on a file, changing permissions
    Given the file <item> has been created in the account
    And the file <item> has been already shared with <user>
    When user selects to share the file <item>
    And user edits the share on file <item> with permissions <permissions>
    Then user  <user> should have access to <item>
    And share should be created on <item> with the following fields
      | sharee        |  <user>        |
      | permissions   |  <permissions> |

    Examples:
      |  item         |   user    | permissions | Description
      |  Share7.txt   |   user2   |   3         |  only update
      |  Share8.txt   |   user2   |   17        |  only share
      |  Share9.txt   |   user2   |   19        |  both update and share
      |  Share10.txt  |   user2   |   1         |  neither update nor share

  Scenario Outline: Edit existing share on a folder, changing permissions
    Given the folder <item> has been created in the account
    And the folder <item> has been already shared with <user>
    When user selects to share the folder <item>
    And user edits the share on folder <item> with permissions <permissions>
    Then user  <user> should have access to <item>
    And share should be created on <item> with the following fields
      | sharee        |  <user>        |
      | permissions   |  <permissions> |

    Examples:
      |  item      |   user    | permissions |
      |  Share11   |   user2   |   1         |
      |  Share12   |   user2   |   9         |
      |  Share13   |   user2   |   13        |
      |  Share14   |   user2   |   17        |

  Scenario Outline: Delete existing share
    Given the <type> <item> has been created in the account
    And the <type> <item> has been already shared with user2
    When user selects to share the <type> <item>
    And user deletes the share
    Then user user2 should not have access to <item>
    And <item> should not be shared anymore with user2

    Examples:
      |  type   |  item         |
      |  file   |  Share15.txt  |
      |  folder |  Share16      |
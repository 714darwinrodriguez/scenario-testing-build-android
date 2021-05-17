@privateshare
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


  Scenario: Correct federated share
    When user selects to share the item textExample.txt
    And user selects user demo@demo.owncloud.com as sharee
    Then share should be created on textExample.txt with the following fields
      | sharee | demo@demo.owncloud.com |

  Scenario Outline: Edit existing share on a folder, removing permissions
    Given the folder <item> has been created in the account
    And the folder <item> has been already shared with <user>
    When user selects to share the folder <item>
    And user edits the share on <item> with permissions <permissions>
    Then user  <user> should have access to <item>
    And share should be created on <item> with the following fields
      | sharee        |  <user>        |
      | permissions   |  <permissions> |

#Permissions
    # READ -> 1
    # UPDATE -> 2
    # CREATE -> 4
    # DELETE -> 8
    # SHARE -> 16

    Examples:
      |  item    |   user    | permissions |
      |  Share4  |   user2   |   1         |
      |  Share5  |   user2   |   9         |
      |  Share6  |   user2   |   13        |
      |  Share7  |   user2   |   17        |

  Scenario: Delete existing share on folder
    Given the folder Share8 has been created in the account
    And the folder Share8 has been already shared with user2
    When user selects to share the folder Share8
    And user deletes the share
    Then user user2 should not have access to Share8
    And Share8 should not be shared anymore with user2
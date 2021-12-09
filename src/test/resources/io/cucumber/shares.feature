@shares
Feature: Private Share

  As a user
  I want to share my content with other users in the platform
  So that the content is accessible and others can contribute

  Background: User is logged in
    Given user Alice is logged

    @createshare
    Rule: Create a share

      @smoke
      Scenario Outline: Correct share with user
        Given the following items have been created in the account
          | <type>   | <item>  |
        When Alice selects to share the <type> <item>
        And Alice selects user Bob as sharee
        Then user Bob should have access to <item>
        And share should be created on <item> with the following fields
          | sharee | Bob |

        Examples:
          |  type   |  item        |
          |  file   |  Share1.txt  |
          |  folder |  Share2      |

      Scenario Outline: Correct share with group
        Given the following items have been created in the account
          | <type>   | <item>  |
        When Alice selects to share the <type> <item>
        And Alice selects group test as sharee
        Then group test should have access to <item>
        And share should be created on <item> with the following fields
          | group | test |

        Examples:
          |  type   |  item        |
          |  file   |  Share3.txt  |
          |  folder |  Share4      |

      Scenario Outline: Correct federated share
        Given the following items have been created in the account
          | <type>   | <item>  |
        When Alice selects to share the <type> <item>
        And Alice selects user demo@demo.owncloud.com as sharee
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

    @resharing
    Rule: Resharing

      Scenario: Reshare allowed
        Given the following items have been created in the account
          | file   | Share7.txt  |
        When Alice selects to share the file Share7.txt
        And Alice selects user Bob as sharee
        And Bob has shared file Share7.txt with Charles with permissions 31
        Then user Bob should have access to Share7.txt
        And user Charles should have access to Share7.txt
        And share should be created on Share7.txt with the following fields
          | sharee  | Bob       |

      #not an Android, keeping ftm...
      Scenario: Reshare not allowed
        Given the following items have been created in the account
          | file   | Share8.txt  |
        And Alice has shared file Share8.txt with Bob with permissions 3
        When Bob has shared file Share8.txt with Charles with permissions 31
        Then user Bob should have access to Share8.txt
        But Charles should not have access to Share8.txt

    @editshare
    Rule: Edit an existing share

      Scenario Outline: Edit existing share on a file, changing permissions
        Given the following items have been created in the account
          | file   | <item>  |
        And Alice has shared folder <item> with <user> with permissions 31
        When Alice selects to share the file <item>
        And Alice edits the share on file <item> with permissions <permissions>
        Then user <user> should have access to <item>
        And share should be created on <item> with the following fields
          | sharee        |  <user>        |
          | permissions   |  <permissions> |

        Examples:
          |  item         |   user    |  permissions | Description
          |  Share9.txt   |   Bob     |    3         |  only update
          |  Share10.txt  |   Bob     |    17        |  only share
          |  Share11.txt  |   Bob     |    19        |  both update and share
          |  Share12.txt  |   Bob     |    1         |  neither update nor share

      Scenario Outline: Edit existing share on a folder, changing permissions
        Given the following items have been created in the account
          | folder   | <item>  |
        And Alice has shared folder <item> with <user> with permissions 31
        When Alice selects to share the folder <item>
        And Alice edits the share on folder <item> with permissions <permissions>
        Then user <user> should have access to <item>
        And share should be created on <item> with the following fields
          | sharee        |  <user>        |
          | permissions   |  <permissions> |

        Examples:
          |  item      |   user    | permissions |
          |  Share13   |   Bob     |   1         |
          |  Share14   |   Bob     |   9         |
          |  Share15   |   Bob     |   13        |
          |  Share16   |   Bob     |   17        |

    @deleteshare
    Rule: Delete a share

      Scenario Outline: Delete existing share
        Given the following items have been created in the account
          | <type>   | <item>  |
        And Alice has shared folder <item> with Bob with permissions 31
        When Alice selects to share the <type> <item>
        And Alice deletes the share
        Then <item> should not be shared anymore with Bob
        And Bob should not have access to <item>

        Examples:
          |  type   |  item         |
          |  file   |  Share17.txt  |
          |  folder |  Share18      |
@avoffline
Feature: Set items as available offline (downloaded and synced)

  As a user
  I want to set content as available offline
  So that the content will be always downloaded and synced

  Background: User is logged in
    Given user user1 is logged
    And the following items have been created in the account
      | ownCloud Manual.pdf |

  Scenario: Set a file as available offline
    When user selects to Set as available offline the item ownCloud Manual.pdf
    Then user should see the item ownCloud Manual.pdf as av.offline
    And the item ownCloud Manual.pdf should be stored in the device
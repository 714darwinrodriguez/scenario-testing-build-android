@avoffline
Feature: Set items as available offline (downloaded and synced)

  As a user
  I want to set content as available offline
  So that the content will be always downloaded and synced

  Background: User is logged in
    Given user user1 is logged

    @smoke
  Scenario: Set a file as available offline
    Given the file av.offline.pdf has been created in the account
    When user selects to Set as available offline the item av.offline.pdf
    Then user should see the item av.offline.pdf as av.offline
    And the item av.offline.pdf should be stored in the device
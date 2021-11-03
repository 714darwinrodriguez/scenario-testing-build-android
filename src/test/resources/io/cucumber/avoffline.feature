@avoffline
Feature: Set items as available offline (downloaded and synced)

  As a user
  I want to set content as available offline
  So that the content will be always downloaded and synced

  Background: User is logged in
    Given user Alice is logged

    @smoke
  Scenario: Set a file as available offline
    Given the following items have been created in the account
      | file   | av.offline.pdf  |
    When Alice selects to set as av.offline the item av.offline.pdf
    Then Alice should see the item av.offline.pdf as av.offline
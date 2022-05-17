@avoffline @ignore
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

  @smoke
  Scenario: Set a folder as available offline
    Given the following items have been created in the account
      | folder   | avOffFolder              |
      | file     | avOffFolder/example.txt  |
    When Alice selects to set as av.offline the item avOffFolder
    And Alice browses into /avOffFolder
    Then Alice should see the item example.txt as av.offline

  Scenario: Available offline shortcut
    Given the following items have been created in the account
      | file     | avOff1.txt  |
      | file     | avOff2.txt  |
      | folder   | avOff3      |
      | folder   | avOff4      |
    And Alice selects to set as av.offline the item avOff1.txt
    And Alice selects to set as av.offline the item avOff3
    When Alice opens the available offline shortcut
    Then Alice should see avOff1.txt in the list
    And Alice should see avOff3 in the list
    But Alice should not see avOff2.txt in the links list
    And Alice should not see avOff4 in the links list
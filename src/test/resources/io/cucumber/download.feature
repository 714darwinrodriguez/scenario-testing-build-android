@download
Feature: Download a file in the account

  As a user
  I want to download the items on my list to my device
  so that my content is also stored in the device

  Background: User is logged in
    Given user Alice is logged

  Scenario: Download a file that is previewable
    Given the following items have been created in the account
      | file   | downloadMe.txt  |
    When Alice selects to Download the item downloadMe.txt
    Then the item downloadMe.txt should be opened and previewed
    And the item downloadMe.txt should be marked as downloaded

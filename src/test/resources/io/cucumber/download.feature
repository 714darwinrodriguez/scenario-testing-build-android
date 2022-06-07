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

  Scenario: Download a file that is not previewable
    Given the following items have been created in the account
      | file   | downloadMe.zip  |
    When Alice selects to Download the item downloadMe.zip
    Then share sheet for the item downloadMe.zip is displayed

  Scenario: Download a file from Details view
    Given the following items have been created in the account
      | file   | downloadMe2.txt  |
    When Alice selects to Details the item downloadMe2.txt
    And Alice clicks on the thumbnail
    Then the item downloadMe2.txt should be opened and previewed
    And the item downloadMe2.txt should be marked as downloaded

  Scenario: Download an updated file
    Given the following items have been created in the account
      | file   | downloadMe3.txt   |
    And Alice selects to Download the item downloadMe3.txt
    When Alice closes the preview
    And file downloadMe3.txt is modified externally adding "updated"
    Then Alice should see the file downloadMe3.txt with "updated"
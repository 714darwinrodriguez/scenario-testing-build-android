@download
Feature: Download a file in the account

  As a user
  I want to download the items on my list to my device
  so that my content is also stored in the device

  Background: User is logged in
    Given user user1 is logged

  #@NoDevice
  #Scenario Outline: Download a file that is not previewable
  #  When user selects to Download the item <itemName>
  #  Then user sees the detailed information: <itemName>, <Type>, and <Size>
  #  And the item <itemName> is stored in the device

  #  Examples:
  #    | itemName       | Type     | Size    |
  #    | Archive.zip    | ZIP file | 12.1 MB |


  Scenario: Download a file that is previewable
    Given the file download.txt has been created in the account
    When user selects to Download the item download.txt
    Then the item download.txt should be opened and previewed
    And the item download.txt should be stored in the device

@copy
Feature: Copy item

  As a user
  I want to copy files between different locations of my account
  So that i have different copies in different places that i can modify separately

  Background: User is logged in
    Given user user1 is logged

  Scenario: Copy an existent folder to another location
    Given the folder copyMe has been created in the account
    When user selects to Copy the item copyMe
    And user selects Documents as target folder
    Then user should see copyMe in the filelist as original
    And user should see copyMe inside the folder Documents
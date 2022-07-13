@upload
Feature: Upload new content

  As a user
  I want to upload files to my account
  so that those files will be synced from now on

  Background: User is logged in
    Given user Alice is logged

  @smoke @ignore
  Scenario: Upload a single file
    Given a file AAAA.txt exists in the device
    When Alice selects the option upload
    And Alice selects AAAA.txt to upload
    Then Alice should see AAAA.txt in the filelist
@list
Feature: List of files is correctly retrieved from server.

  As a user
  I want to see in my device the correct list of files in every location
  So that i know which files and folders i have stored in my server

  Background: User is logged in
    Given user Alice is logged

    Rule: Listing existing files

      Scenario Outline: Check items in the list of files of an specific folder
        Then the list of files in <path> folder should match with the server

        Examples:
          | path       |
          | /          |
          | Photos     |

      Scenario: Check items in the list of files of a created folder
        Given the following items have been created in the account
          | folder   | Many  |
        And the folder Many contains 10 files
        Then the list of files in Many folder should match with the server

    Rule: Changes in the list

      Scenario: New item created remotely
        Given the following items have been created in the account
          | file   | newFile.txt  |
        When Alice refreshes the list
        Then Alice should see newFile.txt in the filelist

      Scenario: Item deleted remotely
        Given the following items have been created in the account
          | file   | newFile2.txt  |
        When Alice refreshes the list
        And the newFile2.txt has been deleted remotely
        Then Alice should not see newFile2.txt in the filelist anymore
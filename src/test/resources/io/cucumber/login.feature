@login
Feature: Login

  As a user
  I want to log in my account basic, OAuth2 or OIDC
  So that i can manage the content inside

  Background: App has been installed from scratch
    Given app has been launched for the first time

  @smoke
  Scenario Outline: A valid login in basic auth
    Given server with basic auth is available
    And user logins as <username> with password <password> as basic auth credentials
    Then user should see the main page

    Examples:
      | username     | password |
      |  user1       |    a     |
      |  e@solid     |    $%    |
      |  hola hola   |    a     |
      |  a+a         |    a     |

  @LDAP
  Scenario: A valid login in LDAP
    Given server with LDAP is available
    When user logins as aaliyah_adams with password secret as LDAP credentials
    Then user should see the main page

  @redirect301
  Scenario: A valid login with 301 redirection
    Given server with redirection 301 is available
    When user logins as admin with password admin as basic auth credentials
    Then user should see the main page


  @redirect302
  Scenario: A valid login with 302 redirection
    Given server with redirection 302 is available
    When user logins as admin with password admin as basic auth credentials
    Then user should see the main page


  @smoke
  Scenario: An invalid login, with wrong credentials
    Given server with basic auth is available
    When user logins as user1 with password as as basic auth credentials
    Then user should see an error message


  #@OAuth2
  #Scenario: A valid login with OAuth2
  #  Given server with OAuth2 is available
  #  When user logins as user1 with password a as OAuth2 credentials
  #  Then user should see the main page


  #@OIDC
  #Scenario: A valid login with OIDC
  #  Given server with OIDC is available
  #  When user logins as einstein with password relativity as OIDC credentials
  #  Then user should see the main page
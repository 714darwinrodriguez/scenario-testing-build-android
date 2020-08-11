package io.cucumber;

import android.ChromeCustomTabPage;
import android.FileListPage;
import android.KopanoPage;
import android.LoginPage;
import android.WizardPage;

import java.util.logging.Level;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.LocProperties;
import utils.api.CommonAPI;
import utils.log.Log;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    //Involved pages
    private WizardPage wizardPage = new WizardPage();
    private LoginPage loginPage = new LoginPage();
    private FileListPage fileListPage = new FileListPage();
    private CommonAPI commonAPI = new CommonAPI();

    @Given("^user1 is logged$")
    public void i_am_logged()
            throws Throwable {
        Log.log(Level.FINE, "----STEP----: " +
                new Object(){}.getClass().getEnclosingMethod().getName());
        wizardPage.skip();
        if (loginPage.notLoggedIn()) {
            String authMethod = commonAPI.checkAuthMethod();
            String username = LocProperties.getProperties().getProperty("userName1");
            String password = LocProperties.getProperties().getProperty("passw1");
            loginPage.typeURL();
            switch (authMethod) {
                case "Basic":
                    loginPage.typeCredentials(username, password);
                    loginPage.submitLogin();
                    break;
                case "Bearer":
                    loginPage.submitLogin();
                    ChromeCustomTabPage chromeCustomTabPage = new ChromeCustomTabPage();
                    chromeCustomTabPage.enterCredentials(username,password);
                    chromeCustomTabPage.authorize();
                    break;
                case "OIDC":
                    loginPage.submitLogin();
                    KopanoPage kopanoPage = new KopanoPage();
                    kopanoPage.enterCredentials(username, password);
                    kopanoPage.authorize();
                    break;
                default:
                    break;
            }
        }
    }

    @Given("^user is a valid user$")
    public void i_am_a_valid_user()
            throws Throwable {
        Log.log(Level.FINE, "----STEP----: " +
                new Object(){}.getClass().getEnclosingMethod().getName());
        wizardPage.skip();
        //Check user is created and enabled in BE
    }

    @When("^server with (.+) is available$")
    public void server_available(String authMethod) {
        Log.log(Level.FINE, "----STEP----: " +
                new Object(){}.getClass().getEnclosingMethod().getName() + " with " + authMethod);
        //loginPage.typeURL(authMethod);
        loginPage.typeURL();
    }

    @When("^user logins as (.+) with password (.+) as (.+) credentials$")
    public void login_with_password_auth_method(String username, String password,
                                                String authMethod) {
        Log.log(Level.FINE, "----STEP----: " +
                new Object() {}.getClass().getEnclosingMethod().getName()
                + ": " + username + " - " + password + " - " + authMethod);
        switch (authMethod) {
            case "basic auth":
                loginPage.typeCredentials(username, password);
                break;
            case "OAuth2":
                loginPage.submitLogin();
                ChromeCustomTabPage chromeCustomTabPage = new ChromeCustomTabPage();
                chromeCustomTabPage.enterCredentials(username, password);
                chromeCustomTabPage.authorize();
                break;
            case "OIDC":
                loginPage.submitLogin();
                KopanoPage kopanoPage = new KopanoPage();
                kopanoPage.enterCredentials(username, password);
                kopanoPage.authorize();
                break;
            default:
                break;
        }
    }

    @Then("^user can see the main page$")
    public void i_can_see_the_main_page() {
        Log.log(Level.FINE, "----STEP----: " +
                new Object(){}.getClass().getEnclosingMethod().getName());
        assertTrue(fileListPage.isHeader());
    }

    @Then("^user sees an error message$")
    public void i_see_an_error_message() {
        Log.log(Level.FINE, "----STEP----: " +
                new Object(){}.getClass().getEnclosingMethod().getName());
        assertTrue(loginPage.isCredentialsErrorMessage());
    }
}

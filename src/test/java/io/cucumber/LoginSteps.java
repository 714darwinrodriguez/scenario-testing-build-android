package io.cucumber;

import static org.junit.Assert.assertTrue;

import android.ChromeCustomTabPage;
import android.FileListPage;
import android.LoginPage;
import android.OIDCPage;

import java.util.logging.Level;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.LocProperties;
import utils.api.CommonAPI;
import utils.api.MiddlewareAPI;
import utils.log.Log;
import utils.parser.CapabilityJSONHandler;

public class LoginSteps {

    //Involved pages
    protected LoginPage loginPage = new LoginPage();

    //APIs to call
    private CommonAPI commonAPI = new CommonAPI();
    private MiddlewareAPI middlewareAPI = new MiddlewareAPI();
    private FileListPage fileListPage = new FileListPage();

    @ParameterType("basic auth|LDAP|redirection 301|redirection 302|OAuth2|OIDC")
    public String authtype(String type){
        return type;
    }

    @Given("app has been launched for the first time")
    public void app_first_launch() {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        //In case it is installed, we remove to execute login tests
        loginPage.reinstallApp();
    }

    @Given("user {word} has been created with default attributes")
    public void user_created_default_attributes(String user)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        middlewareAPI.postMiddlewareExecute(stepName);
    }

    @Given("user {word} is logged")
    public void user_is_logged(String user)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
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
                    chromeCustomTabPage.enterCredentials(username, password);
                    chromeCustomTabPage.authorize();
                    break;
                case "OIDC":
                    loginPage.submitLogin();
                    OIDCPage OIDCPage = new OIDCPage();
                    OIDCPage.enterCredentials(username, password);
                    OIDCPage.authorize();
                    break;
                default:
                    break;
            }
        }
        //Fill capabilities object
        String capabilityJSON = commonAPI.getCapabilities();
        CapabilityJSONHandler JSONparser = new CapabilityJSONHandler(capabilityJSON);
        // Capabilities are not used ftm. But this could be useful if a programmatic way to
        // modify them is found
        //JSONparser.parsePublicLink();
    }

    @Given("server with {authtype} is available")
    public void server_authtype_available(String authMethod) {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        loginPage.typeURL(authMethod);
    }

    @When("^user logins as (.+) with password (.+) as (.+) credentials$")
    public void login_with_password_auth_method(String username, String password, String authMethod) {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        switch (authMethod) {
            case "basic auth":
            case "LDAP":
                loginPage.typeCredentials(username, password);
                loginPage.submitLogin();
                break;
            case "OAuth2":
                loginPage.submitLogin();
                ChromeCustomTabPage chromeCustomTabPage = new ChromeCustomTabPage();
                chromeCustomTabPage.enterCredentials(username, password);
                chromeCustomTabPage.authorize();
                break;
            case "OIDC":
                loginPage.submitLogin();
                OIDCPage OIDCPage = new OIDCPage();
                OIDCPage.enterCredentials(username, password);
                OIDCPage.authorize();
                break;
            default:
                break;
        }
    }

    @Then("user should see the main page")
    public void screen_main_page() {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        try {
            assertTrue(fileListPage.isHeader());
            // In case the assertion fails, we have to remove the app to keep executing other tests
            // After catching the error, it must be thrown again to return the correct test result.
            // Otherwise, the test will never fail
        } catch (AssertionError e) {
            loginPage.removeApp();
            throw e;
        }
        loginPage.removeApp();
    }

    @Then("user should see an error message")
    public void login_error_message() {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        try {
            assertTrue(loginPage.isCredentialsErrorMessage());
            // In case the assertion fails, we have to remove the app to keep executing other tests
            // After catching the error, it must be thrown again to return the correct test result.
            // Otherwise, the test will never fail
        } catch (AssertionError e) {
            loginPage.removeApp();
            throw e;
        }
        loginPage.removeApp();
    }
}

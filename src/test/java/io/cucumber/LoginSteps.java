package io.cucumber;

import android.ChromeCustomTabPage;
import android.FileListPage;
import android.OIDCPage;
import android.LoginPage;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.StepEventBus;

import java.util.logging.Level;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.LocProperties;
import utils.api.CommonAPI;
import utils.api.MiddlewareAPI;
import utils.log.Log;
import utils.parser.CapabilityJSONHandler;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    //Involved pages
    @Steps
    protected LoginPage loginPage;

    //APIs to call
    private CommonAPI commonAPI = new CommonAPI();
    private MiddlewareAPI middlewareAPI = new MiddlewareAPI();
    private FileListPage fileListPage = new FileListPage();

    @Given("^app has been launched for the first time$")
    public void first_launch()
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        //In case it is installed, we remove to execute login tests
        loginPage.reinstallApp();
    }

    @Given("^user (.+) has been created with default attributes$")
    public void user_created_default(String user) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        middlewareAPI.postMiddlewareExecute(currentStep);
    }

    @Given("^user (.+) is logged$")
    public void user_logged(String user)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
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
        JSONparser.parsePublicLink();
    }

    @Given("^server with (.+) is available$")
    public void server_available(String authMethod) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        loginPage.typeURL(authMethod);
    }

    @When("^user logins as (.+) with password (.+) as (.+) credentials$")
    public void login_with_password_auth_method(String username, String password, String authMethod) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
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

    @Then("^user should see the main page$")
    public void main_page() {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
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

    @Then("^user should see an error message$")
    public void error_message() {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
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

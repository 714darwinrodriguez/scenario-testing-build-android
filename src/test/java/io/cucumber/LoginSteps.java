package io.cucumber;

import android.ChromeCustomTabPage;
import android.ChromiumCustomTabPage;
import android.FileListPage;
import android.KopanoPage;
import android.LoginPage;

import net.thucydides.core.model.TestStep;
import net.thucydides.core.steps.StepEventBus;

import org.apache.commons.text.StringEscapeUtils;

import java.util.Optional;
import java.util.logging.Level;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import okhttp3.Response;
import utils.LocProperties;
import utils.api.CommonAPI;
import utils.api.MiddlewareAPI;
import utils.log.Log;
import utils.parser.CapabilityJSONHandler;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    //Involved pages
    private LoginPage loginPage = new LoginPage();
    private ChromeCustomTabPage chromeCustomTabPage = new ChromeCustomTabPage();
    private ChromiumCustomTabPage chromiumCustomTabPage = new ChromiumCustomTabPage();
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

    @Given ("^user (.+) has been created with default attributes$")
    public void user_default_attributes(String user) throws Throwable  {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        middlewareAPI.postMiddlewareExecute(currentStep);
    }

    @Given("^user (.+) is logged$")
    public void i_am_logged(String user)
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
                    if (loginPage.getBrowser() == 0) { //Chrome
                        Log.log(Level.FINE, "Chrome browser");
                        chromeCustomTabPage.enterCredentials(username, password);
                        chromeCustomTabPage.authorize();
                    } else { //Chromium
                        Log.log(Level.FINE, "Chromium browser");
                        chromiumCustomTabPage.enterCredentials(username, password);
                        chromiumCustomTabPage.authorize();
                    }
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
        //Fill capabilities object
        String capabilityJSON = commonAPI.getCapabilities("ocs/v2.php/cloud/capabilities?format=json");
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
    public void login_with_password_auth_method(String username, String password,
                                                String authMethod) {
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
                if (loginPage.getBrowser() == 0) { //Chrome
                    Log.log(Level.FINE, "Chrome browser");
                    chromeCustomTabPage.enterCredentials(username, password);
                    chromeCustomTabPage.authorize();
                } else { //Chromium
                    Log.log(Level.FINE, "Chromium browser");
                    chromiumCustomTabPage.enterCredentials(username, password);
                    chromiumCustomTabPage.authorize();
                }
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

    @Then("^user should see the main page$")
    public void i_can_see_the_main_page() {
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
    public void i_see_an_error_message() {
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

package io.cucumber;

import android.AppiumManager;
import android.FileListPage;
import android.LoginPage;
import android.WizardPage;

import java.net.MalformedURLException;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class StepDefinitions {

    private AndroidDriver driver;
    private final String serverURL = "http://10.40.40.198:17000";

    @Before
    public void setup() throws MalformedURLException{
        AppiumManager manager = new AppiumManager();
        manager.init();
        driver = manager.getDriver();
    }

    @Given("^I am a valid user$")
    public void i_am_a_valid_user() throws Throwable {
        WizardPage wizardPage = new WizardPage(driver);
        wizardPage.skip();
    }

    @When("^I login as (.+) with password (.+)$")
    public void i_login_as_string_with_password_string(String username, String password)
            throws Throwable {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.typeURL();
        loginPage.typeCredentials(username, password);
        loginPage.allowPermissions();
    }

    @When("^I login as (.+) with incorrect password (.+)$")
    public void i_login_as_string_with_incorrect_password_string(String username, String password)
            throws Throwable {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.typeURL();
        loginPage.typeCredentials(username, password);
    }

    @Then("^I can see the main page$")
    public void i_can_see_the_main_page() throws Throwable {
        FileListPage fileListPage = new FileListPage(driver);
        assertTrue(fileListPage.isHeader());
    }

    @Then("^I see an error message$")
    public void i_see_an_error_message() throws Throwable {
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isCredentialsErrorMessage());
    }

    @After
    public void tearDown(){
        driver.removeApp("com.owncloud.android");
        driver.quit();
    }
}
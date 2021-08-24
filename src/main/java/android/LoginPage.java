package android;

import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

public class LoginPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/hostUrlInput")
    private List<MobileElement> urlServer;

    @AndroidFindBy(id = "com.owncloud.android:id/embeddedCheckServerButton")
    private MobileElement checkServerButton;

    @AndroidFindBy(id = "com.owncloud.android:id/account_username")
    private MobileElement userNameText;

    @AndroidFindBy(id = "com.owncloud.android:id/account_password")
    private MobileElement passwordText;

    @AndroidFindBy(id = "com.owncloud.android:id/loginButton")
    private MobileElement loginButton;

    @AndroidFindBy(id = "ok")
    private MobileElement acceptCertificate;

    //Only for login tests, from env variables. Needed some instances running to check
    //whether authentication works (ftm, only basic)
    private final String serverURL = System.getProperty("login_serverURL");
    private final String oauth2URL = System.getProperty("login_oauth2URL");
    private final String oidcURL = System.getProperty("login_oidcURL");
    private final String LDAPURL = System.getProperty("login_LDAPURL");
    private final String red301URL = System.getProperty("login_red301URL");
    private final String red302URL = System.getProperty("login_red302URL");

    //For the regular tests
    private final String server = System.getProperty("server");

    private String errorcredentialstext_xpath = "//*[@text='Wrong username or password']";

    public LoginPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean notLoggedIn() {
        return !urlServer.isEmpty();
    }

    public void typeURL() {
        Log.log(Level.FINE, "Starts: Type URL.");
        waitById(15, urlServer.get(0));
        urlServer.get(0).sendKeys(server);
        checkServerButton.click();
    }

    public void typeURL(String authMethod) {
        Log.log(Level.FINE, "Starts: Type URL. " + authMethod);
        waitById(15, urlServer.get(0));
        urlServer.get(0).sendKeys(selectURL(authMethod));
        checkServerButton.click();
    }

    public void typeCredentials(String username, String password) {
        Log.log(Level.FINE, "Starts: Type credentials: username: "
                + username + " - password: " + password);
        waitById(15, userNameText);
        userNameText.sendKeys(username);
        passwordText.sendKeys(password);
    }

    public void submitLogin() {
        Log.log(Level.FINE, "Starts: Submit login");
        waitById(15, loginButton);
        loginButton.click();
    }

    public boolean isCredentialsErrorMessage() {
        return driver.findElements(MobileBy.xpath(errorcredentialstext_xpath)).size() > 0;
    }

    private String selectURL(String authMehod) {
        switch (authMehod) {
            case "basic auth":
                Log.log(Level.FINE, "URL: " + serverURL);
                return serverURL;
            case "OAuth2":
                Log.log(Level.FINE, "URL: " + oauth2URL);
                return oauth2URL;
            case "LDAP":
                Log.log(Level.FINE, "URL: " + LDAPURL);
                return LDAPURL;
            case "redirection 301":
                Log.log(Level.FINE, "URL: " + red301URL);
                return red301URL;
            case "redirection 302":
                Log.log(Level.FINE, "URL: " + red302URL);
                return red302URL;
            case "OIDC":
                Log.log(Level.FINE, "URL: " + oidcURL);
                return oidcURL;
            default:
                Log.log(Level.WARNING, "No URL");
                return null;
        }
    }
}

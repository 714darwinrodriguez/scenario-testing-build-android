package android;

import java.util.logging.Level;

import utils.log.Log;

public class OIDCPage extends CommonPage {

    //private long deviceVersion;
    //private String device = System.getProperty("device");
    private String browser;

    private String username_xpath = "//*[@id=\"oc-login-username\"]";
    private String password_xpath = "//*[@id=\"oc-login-password\"]";
    private String submit_xpath = "//*[@id=\"root\"]/div/div/div/div/form/div[3]/button";
    private String authorize_xpath = "//*[@id=\"root\"]/div/div/div/div/form/div/div[2]/button";



    public OIDCPage() {
        //deviceVersion = (long) driver.getCapabilities().getCapability("deviceApiLevel");
        /*if (device == null) {
            device = "emulator";
        }*/
        //realDevice = device.contains("emulator") ? false : true;
        waitForWebContext();
        Log.log(Level.FINE, "Browser charged");
        String browser = getBrowser();
        Log.log(Level.FINE, "Selected browser: " + browser);
        this.browser = browser;
        //Log.log(Level.FINE, "Real device?: " + realDevice);
    }

    public void enterCredentials(String username, String password) {

        if (browser.equalsIgnoreCase("chrome")) {
            driver.context("WEBVIEW_chrome");
        }

        Log.log(Level.FINE, "Starts: enter OIDC credentials");
        if (!driver.findElementsByXPath(username_xpath).isEmpty()) {
            Log.log(Level.FINE, "Entering credentials");
            findXpath(username_xpath).sendKeys(username);
            findXpath(password_xpath).sendKeys(password);
            findXpath(submit_xpath).click();
        }
    }

    public void authorize() {
        Log.log(Level.FINE, "Starts: Authorize OIDC");
        findXpath(authorize_xpath).click();
        driver.context("NATIVE_APP");
    }
}

package android;

import java.util.HashMap;
import java.util.logging.Level;

import utils.log.Log;

public class ChromeCustomTabPage extends CommonPage {

    private String username_chrome_xpath = "//*[@id=\"user\"]";
    private String password_chrome_xpath = "//*[@id=\"password\"]";
    private String submit_chrome_xpath = "//*[@id=\"submit\"]";
    private String authorize_chrome_xpath = "//*[@id=\"body-login\"]/div[1]/div/span/form/button";
    private String switch_chrome_xpath = "//*[@id=\"body-login\"]/div[1]/div/span/a/button";
    private String icon_chrome_xpath = "//android.webkit.WebView[@content-desc=\"ownCloud\"]/" +
            "android.view.View[1]/android.view.View";

    public ChromeCustomTabPage() {
        waitForWebContext();
        Log.log(Level.FINE, "Browser charged");
        String context = getContext();
        Log.log(Level.FINE, "Selected context: " + context);
    }

    public void enterCredentials(String username, String password) {
        Log.log(Level.FINE, "Starts: enter OAuth2 credentials");

        //switch button to go back to credentials

        if (findListXpath((String)switch_chrome_xpath).isEmpty()) {
            Log.log(Level.FINE, "Switch user view");
            findXpath((String)switch_chrome_xpath).click();
        }

        waitByXpath(5, (String) username_chrome_xpath);
        findXpath((String) username_chrome_xpath).sendKeys(username);
        findXpath((String) password_chrome_xpath).sendKeys(password);
        findXpath((String) submit_chrome_xpath).click();
    }

    public void authorize() {
        Log.log(Level.FINE, "Starts: Authorize OAuth2");
        waitByXpath(5, (String) authorize_chrome_xpath);
        findXpath((String) authorize_chrome_xpath).click();
        driver.context("NATIVE_APP");
    }
}
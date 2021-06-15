package android;

import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

import utils.log.Log;

public class ChromeCustomTabPage extends CommonPage {

    private String username_chrome_xpath = "//input[@name='user']";
    private String password_chrome_xpath = "//input[@name='password']";
    private String submit_chrome_xpath = "//input[@id='submit']";
    private String authorize_chrome_xpath = "//body[@id=\"body-login\"]/div[1]/div/span/form/button";
    private String switch_chrome_xpath = "//body[@id=\"body-login\"]/div[1]/div/span/a/button";
    private String icon_chrome_xpath = "//android.webkit.WebView[@content-desc=\"ownCloud\"]/" +
            "android.view.View[1]/android.view.View";

    private String username_chromium_xpath = "//android.webkit.WebView[@content-desc=\"ownCloud\"]/" +
            "android.view.View[2]/android.view.View/android.view.View[1]/android.widget.EditText";
    private String password_chromium_xpath = "//android.webkit.WebView[@content-desc=\"ownCloud\"]/" +
            "android.view.View[2]/android.view.View/android.view.View[2]/android.widget.EditText";
    private String submit_chromium_xpath = "//android.widget.Button[@content-desc=\"Login\"]";
    private String authorize_chromium_xpath = "//android.widget.Button[@content-desc=\"Authorize\"]";
    private String switch_chromium_xpath = "//android.widget.Button[@content-desc=\"Switch users to continue\"]";
    private String icon_chromium_xpath = "//android.webkit.WebView[@content-desc=\"ownCloud\"]/" +
            "android.view.View[1]/android.view.View";

    private HashMap fields;
    private HashMap fieldsSelected;
    private String browser;

    public ChromeCustomTabPage() {
        waitForWebContext();
        Log.log(Level.FINE, "Browser charged");
        populateMap();
        String browser = getBrowser();
        Log.log(Level.FINE, "Selected browser: " + browser);
        this.browser = browser;
        fieldsSelected = (HashMap) fields.get(browser);
    }

    private void populateMap() {
        Log.log(Level.FINE, "Populating map");
        fields = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> fieldsChrome = new HashMap<String, String>();
        HashMap<String, String> fieldsChromium = new HashMap<String, String>();

        fieldsChrome.put("username", username_chrome_xpath);
        fieldsChrome.put("password", password_chrome_xpath);
        fieldsChrome.put("submit", submit_chrome_xpath);
        fieldsChrome.put("authorize", authorize_chrome_xpath);
        fieldsChrome.put("switcher", switch_chrome_xpath);
        fieldsChrome.put("icon", icon_chrome_xpath);

        fieldsChromium.put("username", username_chromium_xpath);
        fieldsChromium.put("password", password_chromium_xpath);
        fieldsChromium.put("submit", submit_chromium_xpath);
        fieldsChromium.put("authorize", authorize_chromium_xpath);
        fieldsChromium.put("switcher", switch_chromium_xpath);
        fieldsChromium.put("icon", icon_chromium_xpath);

        fields.put("chrome", fieldsChrome);
        fields.put("chromium", fieldsChromium);
    }

    public void enterCredentials(String username, String password) {
        Log.log(Level.FINE, "Starts: enter OAuth2 credentials");

        //in case we use chrome, get new context
        if (browser.equals("chrome")) {
            driver.context("WEBVIEW_chrome");
        }

        //switch button to go back to credentials
        if (!driver.findElements(By.xpath((String) fieldsSelected.get("switcher"))).isEmpty())
            driver.findElementByXPath((String) fieldsSelected.get("switcher")).click();

        waitByXpath(5, (String) fieldsSelected.get("username"));
        driver.findElement(By.xpath((String) fieldsSelected.get("username"))).sendKeys(username);
        driver.findElement(By.xpath((String) fieldsSelected.get("password"))).sendKeys(password);
        driver.findElement(By.xpath((String) fieldsSelected.get("submit"))).click();

    }

    public void authorize() {
        Log.log(Level.FINE, "Starts: Authorize OAuth2");
        waitByXpath(5, (String) fieldsSelected.get("authorize"));
        driver.findElement(By.xpath((String) fieldsSelected.get("authorize"))).click();
        //in case we use chrome, returning control to the app
        if (browser.equals("chrome")) {
            driver.context("NATIVE_APP");
        }
    }

    protected Set getContexts() {
        Set contextNames = driver.getContextHandles();
        for (Object contextName : contextNames) {
            Log.log(Level.FINE, "Context found: " + contextName);
        }
        return contextNames;
    }

    protected void waitForWebContext() {
        Log.log(Level.FINE, "Waiting for browser");
        //The only way found to wait till browser loads, that is valid for all browsers,
        //emulators, devices etc... ugly
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //ftm, supporting chrome and chromium (android built-in browser)
    public String getBrowser() {
        Log.log(Level.FINE, "Getting browser");
        Set contexts = getContexts();
        for (Object contextName : contexts) {
            Log.log(Level.FINE, "Context found: " + contextName);
            if (((String) contextName).contains("chrome")) {
                return "chrome";
            }
        }
        return "chromium";
    }
}
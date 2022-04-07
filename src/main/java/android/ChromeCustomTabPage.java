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

        fields.put("chrome", fieldsChrome);
    }

    public void enterCredentials(String username, String password) {
        Log.log(Level.FINE, "Starts: enter OAuth2 credentials");

        //in case we use chrome, get new context
        if (browser.equalsIgnoreCase("chrome")) {
            driver.context("WEBVIEW_chrome");
        }

        //switch button to go back to credentials

        if (findListXpath((String) fieldsSelected.get("switcher")).isEmpty()){
        //if (!driver.findElements(By.xpath((String) fieldsSelected.get("switcher"))).isEmpty()) {
            Log.log(Level.FINE, "Switch user view");
            findXpath((String)fieldsSelected.get("switcher")).click();
            //driver.findElement(By.xpath((String)fieldsSelected.get("switcher"))).click();
        }

        waitByXpath(5, (String) fieldsSelected.get("username"));
        findXpath((String) fieldsSelected.get("username")).sendKeys(username);
        findXpath((String) fieldsSelected.get("password")).sendKeys(password);
        findXpath((String) fieldsSelected.get("submit")).click();
    }

    public void authorize() {
        Log.log(Level.FINE, "Starts: Authorize OAuth2");
        waitByXpath(5, (String) fieldsSelected.get("authorize"));
        findXpath((String) fieldsSelected.get("authorize")).click();
        driver.context("NATIVE_APP");
    }
}
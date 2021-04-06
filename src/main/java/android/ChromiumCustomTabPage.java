package android;

import org.openqa.selenium.By;

import java.util.logging.Level;

import utils.log.Log;

public class ChromiumCustomTabPage extends CommonPage {

    private String username_xpath = "//android.webkit.WebView[@content-desc=\"ownCloud\"]/android.view.View[2]/" +
            "android.view.View/android.view.View[1]/android.widget.EditText";
    private String password_xpath = "//android.webkit.WebView[@content-desc=\"ownCloud\"]/android.view.View[2]/" +
            "android.view.View/android.view.View[2]/android.widget.EditText";
    private String submit_xpath = "//android.widget.Button[@content-desc=\"Login\"]";
    private String authorize_xpath = "//android.widget.Button[@content-desc=\"Authorize\"]";
    private String switch_xpath = "//android.widget.Button[@content-desc=\"Switch users to continue\"]";

    public ChromiumCustomTabPage(){

    }

    public void enterCredentials(String username, String password){
        Log.log(Level.FINE, "Starts: enter OAuth2 credentials");

        //driver.context("WEBVIEW_org.chromium.webview_shell");

        //switch button to go back to credentials
        if (!driver.findElements(By.xpath(switch_xpath)).isEmpty())
            driver.findElementByXPath(switch_xpath).click();

        driver.findElement(By.xpath(username_xpath)).sendKeys(username);
        driver.findElement(By.xpath(password_xpath)).sendKeys(password);
        driver.findElement(By.xpath(submit_xpath)).click();
    }

    public void authorize(){
        Log.log(Level.FINE, "Starts: Authorize OAuth2");
        waitByXpath(5, authorize_xpath);
        driver.findElement(By.xpath(authorize_xpath)).click();
    }
}

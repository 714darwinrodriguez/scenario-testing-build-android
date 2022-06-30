package android;

import java.util.logging.Level;

import utils.log.Log;

public class OIDCPage extends CommonPage {

    //Xpaths... ids not working
    private final String username_xpath = "/html/body/main/div/div/div/div/form/div[1]/input";
    private final String password_xpath = "/html/body/main/div/div/div/div/form/div[2]/input";
    private final String login_xpath = "/html/body/main/div/div/div/div/form/div[3]/button/span[1]";
    private final String allow_xpath = "/html/body/main/div/div/div/div/form/div/div[2]/button/span[1]";

    public OIDCPage() {
        super();
        waitForWebContext();
        String context = getContext();
        Log.log(Level.FINE, "Selected context: " + context);
    }

    public void enterCredentials(String userName, String password) {
        if (!findListXpath(username_xpath).isEmpty()) {
            findXpath(username_xpath).sendKeys(userName);
            findXpath(password_xpath).sendKeys(password);
            findXpath(login_xpath).click();
        }
    }

    public void authorize(){
        Log.log(Level.FINE, "Starts: Authorize OIDC");
        waitByXpath(5, allow_xpath);
        findXpath(allow_xpath).click();
        driver.context("NATIVE_APP");
    }
}

package android;

import com.google.common.collect.Lists;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;
import utils.LocProperties;
import utils.log.Log;

public class AppiumManager {

    private static AppiumManager appiumManager;
    private static AndroidDriver driver;
    private static final String driverURL = LocProperties.getProperties().getProperty("appiumURL");

    private AppiumManager() {
        init();
    }

    private static void init()  {

        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, LocProperties.getProperties().getProperty("testResourcesPath"));
        File app = new File(appDir, LocProperties.getProperties().getProperty("apkName"));

        Log.init();

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability ("platformName", "Android");
        capabilities.setCapability ("deviceName", "test");
        capabilities.setCapability ("app", app.getAbsolutePath());
        capabilities.setCapability ("appPackage", LocProperties.getProperties().getProperty("appPackage"));
        capabilities.setCapability ("appActivity", "com.owncloud.android.ui.activity.SplashActivity");
        capabilities.setCapability ("appWaitPackage", LocProperties.getProperties().getProperty("appPackage"));
        capabilities.setCapability ("autoGrantPermissions", "true");
        capabilities.setCapability ("unicodeKeyboard", true);
        capabilities.setCapability ("resetKeyboard", true);
        capabilities.setCapability ("appWaitActivity", "com.owncloud.android.ui.activity.WhatsNewActivity");

        try {
            driver = new AndroidDriver (new URL(driverURL), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
    }

    public static AppiumManager getManager() {
        if (appiumManager == null) {
            appiumManager = new AppiumManager();
        } else {
        }
        return appiumManager;
    }

    public AndroidDriver getDriver(){
        return driver;
    }

    public void cleanFolder(){
        //needed Appium with option --allow-insecure=adb_shell
        //cleaning only if folder exists
        if (folderExists("/sdcard/owncloud/")) {
            Map<String, Object> args = new HashMap<>();
            args.put("command", "rm -r");
            args.put("args", Lists.newArrayList("/sdcard/owncloud/"));
            getDriver().executeScript("mobile:shell", args);
        }
    }

    private boolean folderExists(String folderLocation){
        Map<String, Object> args = new HashMap<>();
        args.put("command",  "[ ! -d \""+ folderLocation+ "\" ] && echo 1 || echo 0");
        String response = (String) getDriver().executeScript("mobile:shell", args);
        if (response.contains("0")) {
            return true;
        } else {
            return false;
        }
    }
}

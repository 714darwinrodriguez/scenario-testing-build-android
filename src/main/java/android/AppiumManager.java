package android;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import utils.LocProperties;
import utils.log.Log;

public class AppiumManager {

    private static AppiumManager appiumManager;
    private static AndroidDriver driver;
    private static final String driverDefect = LocProperties.getProperties().getProperty("appiumURL");
    private static final String driverURL = System.getProperty("appium");
    private static File app;

    private AppiumManager() {
        init();
    }

    private static void init() {

        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, "src/test/resources");
        app = new File(appDir, LocProperties.getProperties().getProperty("apkName"));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        setCapabilities(capabilities);

        try {
            if (!driverURL.isEmpty()) {
                Log.log(Level.FINE,"Appium driver located in: " + driverURL);
                driver = new AndroidDriver(new URL(driverURL), capabilities);
            } else {
                Log.log(Level.FINE,"Appium driver located in: " + driverDefect);
                driver = new AndroidDriver(new URL(driverDefect), capabilities);
            }
        } catch (MalformedURLException e) {
            Log.log(Level.SEVERE, "Driver could not be created: " + e.getMessage());
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        Log.log(Level.FINE, "Device: " +
                driver.getCapabilities().getCapability("deviceManufacturer") + " " +
                driver.getCapabilities().getCapability("deviceModel"));
        Log.log(Level.FINE, "Platform: " +
                driver.getCapabilities().getCapability("platformName") + " " +
                driver.getCapabilities().getCapability("platformVersion"));
        Log.log(Level.FINE, "API Level: " +
                driver.getCapabilities().getCapability("deviceApiLevel") + "\n");

    }

    //Singletonize
    public static AppiumManager getManager() {
        if (appiumManager == null) {
            appiumManager = new AppiumManager();
        }
        return appiumManager;
    }

    public AndroidDriver getDriver() {
        return driver;
    }

    //Check https://appium.io/docs/en/writing-running-appium/caps/
    private static void setCapabilities(DesiredCapabilities capabilities){

        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");

        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "test");

        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM);

        capabilities.setCapability("appPackage",
                LocProperties.getProperties().getProperty("appPackage"));

        capabilities.setCapability("appActivity",
                "com.owncloud.android.ui.activity.SplashActivity");

        capabilities.setCapability("appWaitPackage",
                LocProperties.getProperties().getProperty("appPackage"));

        capabilities.setCapability("autoGrantPermissions", true);

        capabilities.setCapability("unicodeKeyboard", true);

        capabilities.setCapability("resetKeyboard", true);

        capabilities.setCapability("uiautomator2ServerInstallTimeout", 60000);

        if (System.getProperty("device") != null) {
            capabilities.setCapability(MobileCapabilityType.UDID, System.getProperty("device"));
        }
    }
}

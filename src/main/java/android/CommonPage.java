package android;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.touch.offset.PointOption;
import utils.LocProperties;
import utils.log.Log;

public class CommonPage {

    protected static AndroidDriver driver = AppiumManager.getManager().getDriver();
    protected static Actions actions;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    protected final String packag = LocProperties.getProperties().getProperty("appPackage");

    public CommonPage() {
        actions = new Actions(driver);
    }

    public static void waitByXpath(int timeToWait, String resourceXpath) {
        WebDriverWait wait = new WebDriverWait(driver, timeToWait);
        wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.xpath(resourceXpath)));
    }

    public static void waitById(int timeToWait, String resourceId) {
        WebDriverWait wait = new WebDriverWait(driver, timeToWait);
        wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.id(resourceId)));
    }

    public static void waitById(int timeToWait, MobileElement mobileElement) {
        WebDriverWait wait = new WebDriverWait(driver, timeToWait);
        wait.until(ExpectedConditions.visibilityOf(mobileElement));
    }

    public static void waitByIdInvisible(int timeToWait, String resourceId) {
        WebDriverWait wait = new WebDriverWait(driver, timeToWait);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MobileBy.id(resourceId)));
    }

    public static void waitByIdInvisible(int timeToWait, MobileElement mobileElement) {
        WebDriverWait wait = new WebDriverWait(driver, timeToWait);
        wait.until(ExpectedConditions.invisibilityOf(mobileElement));
    }

    public static void waitByTextVisible(int timeToWait, String text) {
        WebDriverWait wait = new WebDriverWait(driver, timeToWait);
        MobileElement mobileElement = (MobileElement)
                driver.findElementByAndroidUIAutomator
                        ("new UiSelector().text(\"" + text + "\");");
        wait.until(ExpectedConditions.textToBePresentInElement(mobileElement, text));
    }

    // The following method should be used only in case implicit/explicit waits are not valid for the
    // scenario. Blocking the thread is not desirable and using it is not a good solution.
    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MobileElement findXpath(String xpath){
        return (MobileElement) driver.findElement(MobileBy.xpath(xpath));
    }

    public static void swipe(double startx, double starty, double endx, double endy) {
        Dimension size = driver.manage().window().getSize();
        int startY = (int) (size.height * starty);
        int endY = (int) (size.height * endy);
        int startX = (int) (size.width * startx);
        int endX = (int) (size.height * endx);
        TouchAction ts = new TouchAction(driver);
        ts.longPress(PointOption.point(startX, startY))
                .moveTo(PointOption.point(startX, endY)).release().perform();
    }

    public static void longPress(MobileElement element) {
        actions.clickAndHold(element).perform();
    }

    public static void takeScreenshot(String name) {
        try {
            String sd = sdf.format(new Timestamp(System.currentTimeMillis()).getTime());
            File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenShotFile, new File("screenshots/" + name + "_" + sd + ".png"));
            Log.log(Level.FINE, "Take screenshot " + name + " at: " + sd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startRecording() {
        AndroidStartScreenRecordingOptions androidStartScreenRecordingOptions =
                new AndroidStartScreenRecordingOptions();
        androidStartScreenRecordingOptions.withBitRate(2000000);
        androidStartScreenRecordingOptions.withVideoSize("360x640");
        driver.startRecordingScreen(androidStartScreenRecordingOptions);
    }

    public static void stopRecording(String filename) {
        String base64String = driver.stopRecordingScreen();
        byte[] data = Base64.decodeBase64(base64String);
        String destinationPath = "video/" + filename + "_" +
                sdf.format(new Timestamp(System.currentTimeMillis()).getTime()) + ".mp4";
        Path path = Paths.get(destinationPath);
        try {
            Files.write(path, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeApp() {
        driver.removeApp(packag);
    }

    public void reinstallApp() {
        if (driver.isAppInstalled(packag)) {
            driver.removeApp(LocProperties.getProperties().getProperty("appPackage"));
            driver.launchApp();
        }
    }
}
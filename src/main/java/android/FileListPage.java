package android;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.LocProperties;
import utils.entities.OCFile;
import utils.log.Log;

public class FileListPage extends CommonPage {

    private String shareoption_id = "com.owncloud.android:id/action_share_file";
    private String renameoption_id = "com.owncloud.android:id/action_rename_file";
    private String moveoption_id = "com.owncloud.android:id/action_move";
    private String copyoption_id = "com.owncloud.android:id/copy_file";
    private String removeoption_id = "com.owncloud.android:id/action_remove_file";
    private String avofflineoption_id = "com.owncloud.android:id/action_set_available_offline";
    private String footer_id = "com.owncloud.android:id/footerText";

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"com.owncloud.android:id/action_mode_close_button\");")
    private MobileElement closeSelectionMode;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_expand_menu_button")
    private MobileElement fabButton;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_mkdir")
    private MobileElement createFolder;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_upload")
    private MobileElement uploadOption;

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc=\"Show roots\"]")
    private MobileElement hamburger;

    @AndroidFindBy(id = "com.owncloud.android:id/upload_from_files_item_view")
    private MobileElement uploadFiles;

    @AndroidFindBy(id = "com.owncloud.android:id/localFileIndicator")
    private MobileElement downloadIndicator;

    @AndroidFindBy(id = "com.owncloud.android:id/localFileIndicator")
    private MobileElement avOfflineIndicator;

    @AndroidFindBy(id = "com.owncloud.android:id/action_sync_file")
    private MobileElement syncFile;

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar")
    private List<MobileElement> toolbar;

    @AndroidFindBy(id = "com.owncloud.android:id/list_root")
    private MobileElement listFiles;

    @AndroidFindBy(id = "com.owncloud.android:id/file_list_constraint_layout")
    private MobileElement fileCell;

    @AndroidFindBy(id = "com.owncloud.android:id/Filename")
    private MobileElement fileName;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_shared_by_link_files")
    private MobileElement linksShortcut;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_available_offline_files")
    private MobileElement avOffShortcut;

    private final String listFiles_id = "com.owncloud.android:id/list_root";
    private HashMap<String, String> operationsMap = new HashMap<String, String>();

    public FileListPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        //Filling up the operations to have the key-value with name and id
        operationsMap.put("share", shareoption_id);
        operationsMap.put("Rename", renameoption_id);
        operationsMap.put("Move", moveoption_id);
        operationsMap.put("Copy", copyoption_id);
        operationsMap.put("Remove", removeoption_id);
        operationsMap.put("Set as available offline", avofflineoption_id);
    }

    public void refreshList() {
        Log.log(Level.FINE, "Refresh list");
        waitById(5, (MobileElement)(toolbar.get(0)));
        swipe(0.50, 0.35, 0.50, 0.90);
    }

    public void waitToload(String itemName) {
        Log.log(Level.FINE, "Waiting to load");
        try {
            //if list of files is not loaded, we should swipe to get the file list
            waitById(15, listFiles_id);
        } catch (Exception e) {
            Log.log(Level.FINE, "Swipe needed to get the list");
            swipe(0.50, 0.20, 0.50, 0.90);
            waitByTextVisible(10, itemName);
        }
        Log.log(Level.FINE, "Loaded");
    }

    public void createFolder() {
        Log.log(Level.FINE, "Starts: create folder");
        fabButton.click();
        createFolder.click();
    }

    public void uploadFiles() {
        Log.log(Level.FINE, "Starts: upload");
        fabButton.click();
        uploadOption.click();
        uploadFiles.click();
    }

    public void pushFile(String itemName) {
        Log.log(Level.FINE, "Starts: push file: " + itemName);
        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath,"src/test/resources");
        File app = new File(appDir, "io/cucumber/example-files/AAAA.txt");
        try {
            driver.pushFile("/sdcard/Download/AAAA.txt", app);
            Log.log(Level.FINE, "File " + itemName + " pushed");
        } catch (IOException e) {
            Log.log(Level.SEVERE, "IO Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void executeOperation(String operation, String itemName) {
        Log.log(Level.FINE, "Starts: execute operation: " + operation + " " + itemName);
        selectItemList(itemName);
        selectOperation(operation);
    }

    public void downloadAction(String itemName) {
        Log.log(Level.FINE, "Starts: download action: " + itemName);
        if (!isItemInList(itemName)) {
            Log.log(Level.FINE, "Searching item... swiping: " + itemName);
            swipe(0.50, 0.90, 0.50, 0.20);
        }
        driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiSelector().text(\"" + itemName + "\");")).click();
    }

    public boolean isItemInList(String itemName) {
        Log.log(Level.FINE, "Starts: Check if item is in list: " + itemName);
        return !driver.findElementsByAndroidUIAutomator(
                "new UiSelector().text(\"" + itemName + "\");").isEmpty();
    }

    public boolean isHeader() {
        return !toolbar.isEmpty();
    }

    public void selectItemList(String itemName) {
        Log.log(Level.FINE, "Starts: select item from list: " + itemName);
        MobileElement element = getElementFromFileList(itemName);
        longPress(element);
    }

    public void selectOperation(String operationName) {
        if (operationName.equals("share")) {  //placed in toolbar
            //driver.findElement(By.id(shareoption_id)).click();
            actions.click(driver.findElement(By.id(shareoption_id))).perform();
        } else {
            Log.log(Level.FINE, "Operation: " + operationName + " placed in menu");
            selectOperationMenu(operationName);
        }
    }

    public void browse(String folderName) {
        Log.log(Level.FINE, "Starts: browse to " + folderName);
        driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiSelector().text(\"" + folderName + "\");")).click();
    }

    public void openLinkShortcut() {
        Log.log(Level.FINE, "Starts: open link shortcut");
        linksShortcut.click();
    }

    public void openAvOffhortcut() {
        Log.log(Level.FINE, "Starts: open av offline shortcut");
        avOffShortcut.click();
    }

    public void closeSelectionMode() {
        Log.log(Level.FINE, "Starts: close selection mode");
        closeSelectionMode.click();
    }

    public boolean fileIsMarkedAsDownloaded(String itemName) {
        //Badge will be removed. This will be improved then.
        return downloadIndicator.isDisplayed();
    }

    public boolean fileIsMarkedAsAvOffline(String itemName) {
        browsePath(itemName);
        //Badge will be removed. This will be improved then.
        return avOfflineIndicator.isDisplayed();
    }

    private void selectOperationMenu(String operationName) {
        Log.log(Level.FINE, "Starts: Select operation from the menu: " + operationName);
        driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiSelector().description(\"More options\");")).click();
        driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiSelector().text(\"" + operationName + "\");")).click();
    }

    public boolean displayedList(String path, ArrayList<OCFile> listServer) {
        boolean found = true;
        browsePath(path); //moving to the folder
        Iterator iterator = listServer.iterator();
        while (iterator.hasNext()) {
            OCFile ocfile = (OCFile) iterator.next();
            Log.log(Level.FINE, "Checking item in list: " + ocfile.getName());
            //Server returns the username as value. Here, we skip it.
            //in oCIS, id is returned instead of name in reference.
            //Shortcut: username > 15 = id (check a best method)
            if (ocfile.getName().equalsIgnoreCase(LocProperties.getProperties().getProperty("userName1")) ||
                    ocfile.getName().length() > 15) {
                continue;
            }
            while (!isItemInList(ocfile.getName()) && !endList(listServer.size())) {
                Log.log(Level.FINE, "Item " + ocfile.getName() + " not found yet. Swiping");
                swipe(0.50, 0.90, 0.50, 0.20);
            }
            if (!isItemInList(ocfile.getName())) {
                Log.log(Level.FINE, "Item " + ocfile.getName() + " is not in the list");
                found = false;
                break;
            }
        }
        return found;
    }

    private boolean endList(int numberItems) {
        return !driver.findElements(MobileBy.AndroidUIAutomator(
                "new UiSelector().text(\"" + Integer.toString(numberItems - 1)
                        + " files\");")).isEmpty();
    }

    private boolean endList() {
        return !driver.findElements(By.id(footer_id)).isEmpty();
    }

    public void browsePath(String path) {
        String[] route = path.split("/");
        if (route.length > 0) { //we have to browse
            for (int i = 1; i < route.length; i++) {
                Log.log(Level.FINE, "browsing to " + route[i]);
                browse(route[i]);
            }
        }
    }

    private MobileElement getElementFromFileList(String itemName) {
        Log.log(Level.FINE, "Starts: searching item in list: " + itemName);
        while (!isItemInList(itemName) && !endList()) {
            Log.log(Level.FINE, "Item " + itemName + " not found yet. Swiping");
            swipe(0.50, 0.90, 0.50, 0.20);
        }
        if (isItemInList(itemName)) {
            Log.log(Level.FINE, "Item found: " + itemName);
            return (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiSelector().text(\"" + itemName + "\");"));
        } else {
            Log.log(Level.FINE, "Item not found: " + itemName);
            return null;
        }
    }

    public void selectFileToUpload(String fileName){
        Log.log(Level.FINE, "Starts: Select File to Upload: " + fileName);
        hamburger.click();
        waitByTextVisible(2, "Downloads");
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Downloads\");")).click();
        waitByTextVisible(2, fileName);
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + fileName + "\");")).click();
    }
}

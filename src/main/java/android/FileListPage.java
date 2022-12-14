package android;

import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.LocProperties;
import utils.entities.OCFile;
import utils.log.Log;

public class FileListPage extends CommonPage {

    private String shareoption_id = "com.owncloud.android:id/action_share_file";
    private String avofflineoption_id = "com.owncloud.android:id/action_set_available_offline";
    private String unavofflineoption_id = "com.owncloud.android:id/action_set_unavailable_offline";
    private String downloadoption_id = "com.owncloud.android:id/action_download_file";
    private String syncoption_id = "com.owncloud.android:id/action_sync_file";
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

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar")
    private List<MobileElement> toolbar;

    @AndroidFindBy(id = "com.owncloud.android:id/list_root")
    private MobileElement listFiles;

    @AndroidFindBy(id = "com.owncloud.android:id/file_list_constraint_layout")
    private MobileElement fileCell;

    @AndroidFindBy(id = "com.owncloud.android:id/Filename")
    private MobileElement fileName;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_all_files")
    private MobileElement toRoot;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_shared_by_link_files")
    private MobileElement linksShortcut;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_available_offline_files")
    private MobileElement avOffShortcut;

    private final String listFiles_id = "com.owncloud.android:id/list_root";

    public FileListPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void refreshList() {
        Log.log(Level.FINE, "Refresh list");
        waitById(5, (MobileElement)(toolbar.get(0)));
        swipe(0.50, 0.40, 0.50, 0.90);
    }

    public void waitToload(String itemName) {
        Log.log(Level.FINE, "Waiting to load");
        try {
            //if list of files is not loaded, we should swipe to get the file list
            waitById(15, listFiles_id);
        } catch (Exception e) {
            Log.log(Level.FINE, "Swipe needed to get the list");
            refreshList();
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
            refreshList();
        }
        findUIAutomatorText(itemName).click();
    }

    public boolean isItemInList(String itemName) {
        Log.log(Level.FINE, "Starts: Check if item is in list: " + itemName);
        return !findListUIAutomatorText(itemName).isEmpty();
    }

    public boolean errorDisplayed(String error) {
        Log.log(Level.FINE, "Starts: Error displayed: " + error);
        return findUIAutomatorText(error).isDisplayed();
    }

    public boolean isHeader() {
        return !toolbar.isEmpty();
    }

    /*
     * Receives: Path of the item to be selected by long pressing,
     * in order to display the operations menu.
     */
    public void selectItemList(String path) {
        Log.log(Level.FINE, "Starts: select item from list: " + path);
        String fileName;
        fileName = path;
        if (path.contains("/")) { //Brwose through
            fileName = browseToFile(path);
        }
        MobileElement element = getElementFromFileList(fileName);
        longPress(element);
    }

    public void selectOperation(String operationName) {
        if (operationName.equals("share")) {  //placed in toolbar
            actions.click(findId(shareoption_id)).perform();
        } else {
            Log.log(Level.FINE, "Operation: " + operationName + " placed in menu");
            selectOperationMenu(operationName);
        }
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

    /*
     * Receives: path of the item
     * Returns: true if "Download" action is not displayed after selecting the item
     * by long pressing, and sync action is displayed (already dowloaded)
     */
    public boolean fileIsMarkedAsDownloaded(String path) {
        Log.log(Level.FINE, "Check if file is downloaded: " + path);
        selectItemList(path);
        return findListId(downloadoption_id).isEmpty() &&
                !findListId(syncoption_id).isEmpty();
    }

    /*
     * Receives: path of the item
     * Returns: true if "Set as available offline" is displayed after selecting the item
     * by long pressing (checked in menu options - need improvement)
     */
    public boolean itemIsMarkedAsAvOffline(String path) {
        selectItemList(path);
        findUIAutomatorDescription("More options").click();
        return findListId(avofflineoption_id).isEmpty();
    }

    /*
     * Receives: path of the item
     * Returns: true if "Unset as available offline" is displayed after selecting the item
     * by long pressing (checked in menu options - need improvement)
     */
    public boolean itemIsMarkedAsUnAvOffline(String path) {
        selectItemList(path);
        findUIAutomatorDescription("More options").click();
        return findListId(unavofflineoption_id).isEmpty();
    }

    private void openMenuActions(){
        findUIAutomatorDescription("More options").click();
    }

    private void selectOperationMenu(String operationName) {
        Log.log(Level.FINE, "Starts: Select operation from the menu: " + operationName);
        openMenuActions();
        findUIAutomatorText(operationName).click();
    }

    public boolean operationAvailable(String operationName){
        Log.log(Level.FINE, "Starts: Check if operation is available: " + operationName);
        openMenuActions();
        return !findListUIAutomatorText(operationName).isEmpty();
    }

    public boolean displayedList(String path, ArrayList<OCFile> listServer) {
        boolean found = true;
        browseToFolder(path); //moving to the folder
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
                refreshList();
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

        return !findListUIAutomatorText(Integer.toString(numberItems - 1) + " files")
                .isEmpty();
    }

    private boolean endList() {
        return !findListId(footer_id).isEmpty();
    }

    /*
     * Receives: name of the folder in the current list to browse into
     */
    public void browseInto(String folderName) {
        Log.log(Level.FINE, "Starts: browse to " + folderName);
        findUIAutomatorText(folderName).click();
    }

    /*
     * Browses to root folder using the shortcut in the bottom bar
     */
    public void browseRoot() {
        Log.log(Level.FINE, "Starts: browse to root");
        toRoot.click();
    }

    /*
     * Receives: path to a folder. If path does not contain "/", folder is in root.
     * Otherwise browsing to.
     */
    public void browseToFolder(String path){
        if (path.equals("/")) { //Go to Root
            browseRoot();
        } else if (path.contains("/")) { //browsing to the folder
            int i = 0;
            String[] route = path.split("/");
            for (i = 0; i < route.length ; i++) {
                Log.log(Level.FINE, "browsing to " + route[i]);
                browseInto(route[i]);
            }
        } else { //no path to browse, just clicking
            browseInto(path);
        }
    }

    /*
     * Receives: path to a file. If path does not contain "/", file is in the root folder,
     * otherwise browsing to
     * Returns: File name (last chunk of the path), after browsing to reach it.
     */
    public String browseToFile(String path) {
        String[] route = path.split("/");
        int i = 0;
        if (route.length > 0) { //browse
            for (i = 0; i < route.length -1 ; i++) {
                Log.log(Level.FINE, "browsing to " + route[i]);
                browseInto(route[i]);
            }
            Log.log(Level.FINE, "Returning: " + route[i]);
            return route[i];
        }
        return path;
    }

    /*
     * Receives: An item name
     * Returns: MobileElement object correspondent with the given item name in the current list
     */
    private MobileElement getElementFromFileList(String itemName) {
        Log.log(Level.FINE, "Starts: searching item in list: " + itemName);
        while (!isItemInList(itemName) && !endList()) {
            Log.log(Level.FINE, "Item " + itemName + " not found yet. Swiping");
            refreshList();
        }
        if (isItemInList(itemName)) {
            Log.log(Level.FINE, "Item found: " + itemName);
            return findUIAutomatorText(itemName);
        } else {
            Log.log(Level.FINE, "Item not found: " + itemName);
            return null;
        }
    }

    public void selectFileToUpload(String fileName){
        Log.log(Level.FINE, "Starts: Select File to Upload: " + fileName);
        hamburger.click();
        waitByTextVisible(2, "Downloads");
        findUIAutomatorText("Downloads");
        waitByTextVisible(2, fileName);
        findUIAutomatorText(fileName);
    }
}

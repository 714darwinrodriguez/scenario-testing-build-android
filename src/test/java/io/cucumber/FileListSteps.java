package io.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.DetailsPage;
import android.FileListPage;
import android.FolderPickerPage;
import android.InputNamePage;
import android.RemoveDialogPage;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.StepEventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.api.FilesAPI;
import utils.entities.OCFile;
import utils.log.Log;

public class FileListSteps {

    //Involved pages
    @Steps
    protected FileListPage fileListPage;

    @Steps
    protected InputNamePage inputNamePage;

    @Steps
    protected FolderPickerPage folderPickerPage;

    @Steps
    protected RemoveDialogPage removeDialogPage;

    @Steps
    protected DetailsPage detailsPage;

    //APIs to call
    protected FilesAPI filesAPI = new FilesAPI();

    @Given("there is an item called {word} in the folder Downloads of the device")
    public void push_file_to_device(String itemName) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        fileListPage.pushFile(itemName);
    }

    @Given("the following items have been created in the account")
    public void items_created_in_account(DataTable table) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            String type = rows.get(0);
            String name = rows.get(1);
            Log.log(Level.FINE, type + " " + name);
            if (!filesAPI.itemExist(name)) {
                if (type.equalsIgnoreCase("folder") || type.equalsIgnoreCase("item")) {
                    filesAPI.createFolder(name);
                } else if (type.equalsIgnoreCase("file")) {
                    filesAPI.pushFile(name);
                }
            }
        }
    }

    @Given("the folder {word} contains {int} files")
    public void folder_contains_file(String folderName, int files) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        if (!filesAPI.itemExist(folderName)) {
            filesAPI.createFolder(folderName);
        }
        for (int i = 0; i < files; i++) {
            filesAPI.pushFile(folderName + "/file_" + i + ".txt");
        }
    }

    @When("Alice selects the option Create Folder")
    public void select_create_folder() {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        fileListPage.createFolder();
    }

    @When("Alice selects to set as av.offline the item {word}")
    public void select_item_to_avoffline(String itemName) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        fileListPage.waitToload("Documents");
        fileListPage.refreshList();
        fileListPage.executeOperation("Set as available offline", itemName);
    }

    @When("Alice selects to {word} the {itemtype} {word}")
    public void select_item_to_some_operation(String operation, String type, String itemName) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        fileListPage.waitToload("Documents");
        fileListPage.refreshList();
        if (operation.equals("Download")) {
            fileListPage.downloadAction(itemName);
            detailsPage.waitFinishedDownload(30);
        } else {
            fileListPage.executeOperation(operation, itemName);
        }
    }

    @When("Alice selects {word} as target folder")
    public void select_target_folder(String targetFolder) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        folderPickerPage.selectFolder(targetFolder);
        folderPickerPage.accept();
    }

    @When("Alice selects the option upload")
    public void select_upload() {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        fileListPage.upload();
    }

    @When("Alice accepts the deletion")
    public void accept_deletion() {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        removeDialogPage.removeAll();
    }

    @When("Alice sets {word} as new name")
    public void set_new_name(String itemName) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        inputNamePage.setItemName(itemName);
    }

    @When ("Alice opens the public link shortcut")
    public void open_links_shortcut() {
        fileListPage.openLinkShortcut();
        fileListPage.refreshList();
    }

    @Then("Alice should see {word} in the (file)list")
    public void see_the_item_filelist(String itemName) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        fileListPage.waitToload("Documents");
        //Get the last token of the item path
        assertTrue(fileListPage.isItemInList(itemName.substring(itemName.lastIndexOf('/') + 1)));
        assertTrue(filesAPI.itemExist(itemName));
        filesAPI.removeItem(itemName);
    }

    @Then("Alice should not see {word} in the filelist anymore")
    public void item_out(String itemName) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        fileListPage.waitToload("Documents");
        assertFalse(fileListPage.isItemInList(itemName));
        assertFalse(filesAPI.itemExist(itemName));
    }

    @Then("Alice should not see {word} in the links list")
    public void item_not_links_list(String itemName) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertFalse(fileListPage.isItemInList(itemName));
        filesAPI.removeItem(itemName);
    }

    @Then("Alice should see {word} inside the folder {word}")
    public void item_inside_folder(String itemName, String targetFolder) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertTrue(filesAPI.itemExist(targetFolder + "/" + itemName));
        filesAPI.removeItem(targetFolder + "/" + itemName);
    }

    @Then("Alice should see {word} in the filelist as original")
    public void original_file_in_filelist(String itemName) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        //Copy keeps the selection mode. To improve.
        fileListPage.closeSelectionMode();
        fileListPage.waitToload("Documents");
        assertTrue(fileListPage.isItemInList(itemName.substring(itemName.lastIndexOf('/') + 1)));
        assertTrue(filesAPI.itemExist(itemName));
        filesAPI.removeItem(itemName);
    }

    @Then("Alice should see the detailed information: {word}, {word}, and {word}")
    public void preview_in_screen(String itemName, String type, String size) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        detailsPage.removeShareSheet();
        assertEquals(detailsPage.getName(), itemName);
        assertEquals(detailsPage.getSize(), size);
        assertEquals(detailsPage.getType(), type);
        detailsPage.backListFiles();
    }

    @Then("the item {word} should be marked as downloaded")
    public void item_as_downloaded(String itemName)
            throws IOException {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertTrue(fileListPage.fileIsMarkedAsDownloaded(itemName));
        filesAPI.removeItem(itemName);
    }

    @Then("Alice should see the item {word} as av.offline")
    public void item_as_avOffline(String itemName)
            throws IOException {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertTrue(fileListPage.fileIsMarkedAsAvOffline(itemName));
        filesAPI.removeItem(itemName);
    }

    @Then("the item {word} should be opened and previewed")
    public void item_opened_previewed(String itemName) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertTrue(detailsPage.itemPreviewed());
        detailsPage.backListFiles();
    }

    @Then("the list of files in {word} folder should match with the server")
    public void list_matches_server(String path) throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        fileListPage.waitToload("Documents");
        fileListPage.refreshList();
        ArrayList<OCFile> listServer = filesAPI.listItems(path);
        assertTrue(fileListPage.displayedList(path, listServer));
    }
}

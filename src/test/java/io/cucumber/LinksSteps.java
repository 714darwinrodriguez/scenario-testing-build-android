package io.cucumber;

import android.LinksPage;
import android.SharingPage;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.StepEventBus;

import java.util.List;
import java.util.logging.Level;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.api.FilesAPI;
import utils.api.ShareAPI;
import utils.entities.OCShare;
import utils.log.Log;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LinksSteps {

    //Involved pages
    @Steps
    protected SharingPage sharingPage;

    @Steps
    protected LinksPage linksPage;

    //APIs to call
    protected ShareAPI shareAPI = new ShareAPI();
    protected FilesAPI filesAPI = new FilesAPI();

    @Given("^the (item|file|folder) (.+) has been already shared by link$")
    public void item_already_shared_by_link(String type, String itemName)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        shareAPI.createShare(itemName, "", "3", "1", itemName + " link");
    }

    @When("^user creates link on (.+) (.+) with the following fields$")
    public void create_link_with_fields(String type, String itemName, DataTable table)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        sharingPage.addPublicLink();
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            switch (rows.get(0)) {
                case "name": {
                    linksPage.addLinkName(rows.get(1));
                    break;
                }
                case "password": {
                    linksPage.addPassword(itemName, rows.get(1));
                    break;
                }
                case "permission": {
                    linksPage.setPermission(rows.get(1));
                    break;
                }
                case "expiration days": {
                    linksPage.setExpiration(rows.get(1));
                    break;
                }
                default:
                    break;
            }
        }
        //if password is enforced, we must force to input
        if (linksPage.isPasswordEnforced(itemName)) {
            //Enter a fake password to fit the scenario
            linksPage.addPassword(itemName, "a");
        }
        linksPage.submitLink();
    }

    @When("^user edits the link on (.+) with the following fields$")
    public void edit_public_link(String itemName, DataTable table)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        List<List<String>> listItems = table.asLists();
        sharingPage.openPublicLink(itemName);
        for (List<String> rows : listItems) {
            switch (rows.get(0)) {
                case "name": {
                    linksPage.addLinkName(rows.get(1));
                    break;
                }
                case "permissions": {
                    switch (rows.get(1)) {
                        case ("1"): { //Download / View
                            Log.log(Level.FINE, "Select Download / View");
                            linksPage.selectDownloadView();
                            break;
                        }
                        case ("15"): { //Download / View / Upload
                            Log.log(Level.FINE, "Select Download / View / Upload");
                            linksPage.selectDownloadViewUpload();
                            break;
                        }
                        case ("4"): { //Upload Only (File Drop)
                            Log.log(Level.FINE, "Select Upload Only (File Drop)");
                            linksPage.selectUploadOnly();
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                case "password": {
                    linksPage.addPassword(itemName, rows.get(1));
                    break;
                }
                default:
                    break;
            }
        }
        linksPage.submitLink();
    }

    @When("^user deletes the link on (.+)$")
    public void delete_link(String itemName) {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        sharingPage.deletePublicShare();
        sharingPage.acceptDeletion();
    }

    @Then("^link should be created on (.+) with the following fields$")
    public void link_created_fields(String itemName, DataTable table)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        //Asserts in UI
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            switch (rows.get(0)) {
                case "name": {
                    assertTrue(sharingPage.isItemInListPublicShares(rows.get(1)));
                    break;
                }
                case "password": {
                    sharingPage.openPublicLink(itemName);
                    assertTrue(linksPage.isPasswordEnabled(itemName));
                    linksPage.close();
                    break;
                }
                case "user": {
                    assertTrue(sharingPage.isItemInListPublicShares(itemName));
                    break;
                }
                case "permission": {
                    Log.log(Level.FINE, "checking permissions");
                    sharingPage.openPublicLink(itemName);
                    assertTrue(linksPage.checkPermissions(rows.get(1)));
                    linksPage.close();
                    break;
                }
                case "expiration days": {
                    sharingPage.openPublicLink(itemName);
                    assertTrue(linksPage.checkExpiration(rows.get(1)));
                    linksPage.close();
                    break;
                }
                default:
                    break;
            }
        }
        //Asserts in server via API
        OCShare share = shareAPI.getShare(itemName);
        assertTrue(sharingPage.checkCorrectShare(share, listItems));
        filesAPI.removeItem(itemName);
    }

    @Then("^link on (.+) should not exist anymore$")
    public void link_not_existing(String itemName)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertFalse(sharingPage.isItemInListPublicShares(itemName + " link"));
        assertTrue(shareAPI.getShare(itemName) == null);
        filesAPI.removeItem(itemName);
    }
}

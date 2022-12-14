package io.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.LinksPage;
import android.SharingPage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.api.FilesAPI;
import utils.api.ShareAPI;
import utils.entities.OCShare;
import utils.log.Log;

public class LinksSteps {

    //Involved pages
    protected SharingPage sharingPage = new SharingPage();
    protected LinksPage linksPage = new LinksPage();

    //APIs to call
    protected ShareAPI shareAPI = new ShareAPI();
    protected FilesAPI filesAPI = new FilesAPI();

    public LinksSteps() throws IOException {
    }

    @ParameterType("item|file|folder")
    public String itemtype(String type){
        return type;
    }

    @Given("{word} has shared the {itemtype} {word} by link")
    public void item_already_shared_by_link(String sharingUser, String type, String itemName)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        shareAPI.createShare(sharingUser, itemName, "", "3", "1", itemName + " link");
    }

    @When("Alice creates link on {word} {word} with the following fields")
    public void create_link_with_some_fields(String type, String itemName, DataTable table)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
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
        linksPage.submitLink();
    }

    @When("Alice edits the link on {word} with the following fields")
    public void edit_public_link(String itemName, DataTable table)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
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

    @When("Alice deletes the link on {word}")
    public void delete_link(String itemName) {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        sharingPage.deletePublicShare();
        sharingPage.acceptDeletion();
    }

    @Then("link should be created on {word} with the following fields")
    public void link_created_fields(String itemName, DataTable table)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        //Asserts in UI
        Log.log(Level.FINE, "Checking UI asserts");
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            switch (rows.get(0)) {
                case "name": {
                    Log.log(Level.FINE, "Checking name: " + rows.get(1));
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
                    Log.log(Level.FINE, "checking user: " + itemName);
                    assertTrue(sharingPage.isItemInListPublicShares(itemName));
                    break;
                }
                case "permission": {
                    Log.log(Level.FINE, "checking permissions: " + rows.get(1));
                    sharingPage.openPublicLink(itemName);
                    assertTrue(linksPage.checkPermissions(rows.get(1)));
                    linksPage.close();
                    break;
                }
                case "expiration days": {
                    Log.log(Level.FINE, "checking expirations day: " + rows.get(1));
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
        Log.log(Level.FINE, "Checking API/server asserts");
        OCShare share = shareAPI.getShare(itemName);
        assertTrue(sharingPage.checkCorrectShare(share, listItems));
        filesAPI.removeItem(itemName);
    }

    @Then("link on {word} should not exist anymore")
    public void link_not_existing(String itemName)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        Log.log(Level.FINE, "Checking if item exists: " + itemName);
        assertFalse(sharingPage.isItemInListPublicShares(itemName + " link"));
        assertTrue(shareAPI.getShare(itemName) == null);
        filesAPI.removeItem(itemName);
    }
}

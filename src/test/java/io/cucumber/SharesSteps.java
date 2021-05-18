package io.cucumber;

import android.SharesPage;
import android.SearchShareePage;
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

public class SharesSteps {

    //Involved pages
    @Steps
    protected SharingPage sharingPage;

    @Steps
    protected SearchShareePage searchShareePage;

    @Steps
    protected SharesPage sharesPage;

    //APIs to call
    protected ShareAPI shareAPI = new ShareAPI();
    protected FilesAPI filesAPI = new FilesAPI();

    @Given("^the (file|folder|item) (.+) has been already shared with (.+)$")
    public void item_already_shared(String type, String itemName, String sharee)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        shareAPI.createShare(itemName, sharee, "0", "31", "");
    }

    @When("^user selects (user|group) (.+) as sharee$")
    public void i_select_sharee(String type, String sharee)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        sharingPage.addPrivateShare();
        searchShareePage.shareWithUser(sharee);
    }

    @When("^user edits the share on (.+) (.+) with permissions (.+)$")
    public void user_edits_share(String type, String itemName, String permissions)
            throws Throwable{
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        sharingPage.openPrivateShare(itemName);
        int permissionsToInt = Integer.parseInt(permissions);
        String permissionsToString = String.format("%5s", Integer.toBinaryString(permissionsToInt))
                .replace(" ", "0");
        Log.log(Level.FINE, "Permissions converted: " + permissionsToString);
        for (int i=0 ; i<=permissionsToString.length()-1 ; i++){
            switch(i) {
                case(0):{
                    Log.log(Level.FINE, "Check Share");
                    char status = permissionsToString.charAt(i);
                    boolean enabled = sharesPage.isShareEnabled();
                    Log.log(Level.FINE, "Status: " + status + ". Enabled: " + enabled);
                    if (enabled != (status == '1'))
                        sharesPage.switchShare();
                    break;
                }
                case(1):{
                    if (type.equals("folder")) {
                        Log.log(Level.FINE, "Check Delete");
                        char status = permissionsToString.charAt(i);
                        boolean enabled = sharesPage.isDeleteSelected();
                        Log.log(Level.FINE, "Status: " + status + ". Enabled: " + enabled);
                        if (enabled != (status == '1'))
                            sharesPage.switchDelete();
                    }
                    break;
                }
                case(2):{
                    if (type.equals("folder")) {
                        Log.log(Level.FINE, "Check Create");
                        char status = permissionsToString.charAt(i);
                        boolean enabled = sharesPage.isCreateSelected();
                        Log.log(Level.FINE, "Status: " + status + ". Enabled: " + enabled);
                        if (enabled != (status == '1'))
                            sharesPage.switchCreate();
                    }
                    break;
                }
                case(3):{
                    Log.log(Level.FINE, "Check Change");
                    char status = permissionsToString.charAt(i);
                    if (type.equals("folder")){
                        boolean enabled = sharesPage.isChangeSelected();
                        Log.log(Level.FINE, "Status Folder: " + status +". Enabled: "+ enabled);
                        if (enabled != (status=='1')) {
                            sharesPage.switchChange();
                        }
                    } else if (type.equals("file")){
                        boolean enabled = sharesPage.isEditFileEnabled();
                        Log.log(Level.FINE, "Status File: " + status +". Enabled: "+ enabled);
                        if (enabled != (status=='1')) {
                            sharesPage.switchEditFile();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
        sharesPage.close();
        //An implicit wait to be used till a close button is available. To improve.
        Thread.sleep(2000);
    }

    @When("^user deletes the share$")
    public void user_deletes_share() {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        sharingPage.deletePrivateShare();
        sharingPage.acceptDeletion();
    }

    @Then("^share should be created on (.+) with the following fields$")
    public void share_created_with_fields(String itemName, DataTable table)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        //Asserts in UI
        String groupName = null;
        List<List<String>> listItems = table.asLists();
        for (List<String> rows : listItems) {
            switch (rows.get(0)) {
                case "password": {
                    sharingPage.openPrivateShare(itemName);
                    assertTrue(sharesPage.isPasswordEnabled());
                    sharesPage.close();
                    break;
                }
                case "sharee": {
                    assertTrue(sharingPage.isItemInListPrivateShares(rows.get(1)));
                    break;
                }
                case "group": {
                    assertTrue(sharingPage.isItemInListPrivateShares(rows.get(1)+ " (group)"));
                    groupName = rows.get(1);
                    break;
                }
                case "permissions": {
                    //Not implemented yet
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
        //shareAPI.removeShare(share.getId());
    }

    @Then("^group including (.+) should have access to (.+)$")
    public void group_has_the_file (String userName, String itemName)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertTrue(shareAPI.isSharedWithMe(itemName, true));
    }

    @Then("^user (.+) should not have access to (.+)$")
    public void sharee_does_not_have_the_file (String userName, String itemName)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertFalse(shareAPI.isSharedWithMe(itemName, false));
    }

    @Then("^user (.+) should have access to (.+)$")
    public void sharee_has_the_file (String userName, String itemName)
            throws Throwable {
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertTrue(shareAPI.isSharedWithMe(itemName, false));
    }

    @Then("^(.+) should not be shared anymore with (.+)$")
    public void share_is_deleted(String itemName, String sharee)
            throws Throwable{
        String currentStep = StepEventBus.getEventBus().getCurrentStep().get().toString();
        Log.log(Level.FINE, "----STEP----: " + currentStep);
        assertFalse(sharingPage.isItemInListPrivateShares(sharee));
        filesAPI.removeItem(itemName);
    }
}

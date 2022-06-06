package io.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.SearchShareePage;
import android.SharesPage;
import android.SharingPage;

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

public class SharesSteps {

    //Involved pages
    protected SharingPage sharingPage = new SharingPage();
    protected SearchShareePage searchShareePage = new SearchShareePage();
    protected SharesPage sharesPage = new SharesPage();

    //APIs to call
    protected ShareAPI shareAPI = new ShareAPI();
    protected FilesAPI filesAPI = new FilesAPI();

    @ParameterType("user|group")
    public String usertype(String type){
        return type;
    }

    @Given("{word} has shared {itemtype} {word} with {word} with permissions {word}")
    public void item_already_shared_with_permissions(String sharingUser, String type, String itemName,
                                    String recipientUser, String permissions) throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        shareAPI.createShare(sharingUser, itemName, recipientUser, "0", permissions, "");
    }

    @When("Alice selects {usertype} {word} as sharee")
    public void select_sharee(String type, String sharee)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        sharingPage.addPrivateShare();
        searchShareePage.shareWithUser(sharee);
    }

    @When("Alice edits the share on {itemtype} {word} with permissions {word}")
    public void edit_share_with_permissions(String type, String itemName, String permissions) {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        sharingPage.openPrivateShare(itemName);
        int permissionsToInt = Integer.parseInt(permissions);
        String permissionsToString = String.format("%5s", Integer.toBinaryString(permissionsToInt))
                .replace(" ", "0");
        Log.log(Level.FINE, "Permissions converted: " + permissionsToString);
        for (int i = 0; i <= permissionsToString.length() - 1; i++) {
            switch (i) {
                case (0): {
                    Log.log(Level.FINE, "Check Share");
                    char status = permissionsToString.charAt(i);
                    boolean enabled = sharesPage.isShareEnabled();
                    Log.log(Level.FINE, "Status: " + status + ". Enabled: " + enabled);
                    if (enabled != (status == '1'))
                        sharesPage.switchShare();
                    break;
                }
                case (1): {
                    if (type.equalsIgnoreCase("folder")) {
                        Log.log(Level.FINE, "Check Delete");
                        char status = permissionsToString.charAt(i);
                        boolean enabled = sharesPage.isDeleteSelected();
                        Log.log(Level.FINE, "Status: " + status + ". Enabled: " + enabled);
                        if (enabled != (status == '1'))
                            sharesPage.switchDelete();
                    }
                    break;
                }
                case (2): {
                    if (type.equalsIgnoreCase("folder")) {
                        Log.log(Level.FINE, "Check Create");
                        char status = permissionsToString.charAt(i);
                        boolean enabled = sharesPage.isCreateSelected();
                        Log.log(Level.FINE, "Status: " + status + ". Enabled: " + enabled);
                        if (enabled != (status == '1'))
                            sharesPage.switchCreate();
                    }
                    break;
                }
                case (3): {
                    Log.log(Level.FINE, "Check Change");
                    char status = permissionsToString.charAt(i);
                    if (type.equalsIgnoreCase("folder")) {
                        boolean enabled = sharesPage.isChangeSelected();
                        Log.log(Level.FINE, "Status Folder: " + status + ". Enabled: " + enabled);
                        if (enabled != (status == '1')) {
                            sharesPage.switchChange();
                        }
                    } else if (type.equalsIgnoreCase("file")) {
                        boolean enabled = sharesPage.isEditFileEnabled();
                        Log.log(Level.FINE, "Status File: " + status + ". Enabled: " + enabled);
                        if (enabled != (status == '1')) {
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
    }

    @When("Alice deletes the share")
    public void delete_share() {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        sharingPage.deletePrivateShare();
        sharingPage.acceptDeletion();
    }

    @Then("share should be created on {word} with the following fields")
    public void share_created_with_fields(String itemName, DataTable table)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
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
                    assertTrue(sharingPage.isItemInListPrivateShares(rows.get(1) + " (group)"));
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
    }

    @Then("{word} should not have access to {word}")
    public void sharee_does_not_have_access(String userName, String itemName)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertFalse(shareAPI.isSharedWithMe(itemName, userName, false));
        filesAPI.removeItem(itemName);
    }

    @Then("{usertype} {word} should have access to {word}")
    public void sharee_access_the_file(String type, String shareeName, String itemName)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        if (type.equalsIgnoreCase("user")) {
            assertTrue(shareAPI.isSharedWithMe(itemName, shareeName, false));
        } else if (type.equalsIgnoreCase("group")) {
            assertTrue(shareAPI.isSharedWithMe(itemName, shareeName, true));
        }
    }

    @Then("{word} should not be shared anymore with {word}")
    public void share_deleted(String itemName, String sharee)
            throws Throwable {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertFalse(sharingPage.isItemInListPrivateShares(sharee));
        filesAPI.removeItem(itemName);
    }

    @Then("Alice should see {word} as recipient")
    public void recipient_list(String sharee) {
        String stepName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.log(Level.FINE, "----STEP----: " + stepName);
        assertTrue(sharesPage.isSharee(sharee));
    }
}

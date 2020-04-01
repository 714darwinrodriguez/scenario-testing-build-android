package android;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import io.appium.java_client.MobileBy;
import utils.entities.OCShare;
import utils.log.Log;

public class SharePage extends CommonPage {

    private final String addshareebutton_id = "com.owncloud.android:id/addUserButton";
    private final String addpubliclinkbutton_id = "com.owncloud.android:id/addPublicLinkButton";
    private final String editpubliclinkbutton_id = "com.owncloud.android:id/editPublicLinkButton";
    private final String editprivateshare_id = "com.owncloud.android:id/editShareButton";
    private final String sharefilename_id = "com.owncloud.android:id/shareFileName";
    private final String unshareprivate_id = "com.owncloud.android:id/unshareButton";
    private final String privatesharesectiontitle_id = "shareWithUsersSectionTitle";

    public SharePage(){
        super();
    }

    public boolean isHeader() {
        return !driver.findElementsByAndroidUIAutomator("new UiSelector().text(\"Share\");").isEmpty();
    }

    public void addPrivateShare(){
        Log.log(Level.FINE, "Starts: add private share");
        waitById(5, sharefilename_id);
        driver.findElement(MobileBy.id(addshareebutton_id)).click();
    }

    public void addPublicLink(){
        Log.log(Level.FINE, "Starts: add public link");
        waitById(5, sharefilename_id);
        driver.findElement(MobileBy.id(addpubliclinkbutton_id)).click();
    }

    public void openLink(){
        Log.log(Level.FINE, "Starts: open public link");
        driver.findElement(MobileBy.id(editpubliclinkbutton_id)).click();
    }

    public void editPrivateShare(String itemName){
        Log.log(Level.FINE, "Starts: edit private share: " + itemName);
        driver.findElement(MobileBy.id(editprivateshare_id)).click();
    }

    public boolean isItemInList(String item) {
        waitById(5, privatesharesectiontitle_id);
        return !driver.findElementsByAndroidUIAutomator("new UiSelector().text(\""+item+"\");").isEmpty();
    }

    public void deletePrivateShare(){
        driver.findElement(MobileBy.id(unshareprivate_id)).click();
    }

    public boolean checkCorrectShare(OCShare remoteShare, List<List<String>> dataList ){
        Log.log(Level.FINE, "Starts: Check correct share");
        HashMap<String, String> mapFields = turnListToHashmap(dataList);
        for (Map.Entry<String, String> entry : mapFields.entrySet()) {
            Log.log(Level.FINE, "Entry KEY: " + entry.getKey() + " - VALUE: " + entry.getValue());
            switch (entry.getKey()){
                case "id":{
                    if (!remoteShare.getId().equals(entry.getValue())){
                        Log.log(Level.FINE, "ID does not match - Remote: " + remoteShare.getId()
                                + " - Expected: " + entry.getValue());
                        return false;
                    }
                    break;
                }
                case "user":{
                    if (remoteShare.getType().equals("0")) { // private share
                        if (!remoteShare.getShareeName().equals(entry.getValue())) {
                            Log.log(Level.FINE, "Sharee does not match - Remote: " + remoteShare.getShareeName()
                                    + " - Expected: " + entry.getValue());
                            return false;
                        }
                    }
                    break;
                }
                case "password":{
                    if (!(remoteShare.getType().equals("3") && remoteShare.hasPassword())) {
                        Log.log(Level.FINE, "Password not present");
                        return false;
                    }
                    break;
                }
                case "name":{
                    if (!remoteShare.getLinkName().equals(entry.getValue())){
                        Log.log(Level.FINE, "Item name does not match - Remote: " + remoteShare.getItemName()
                                + " - Expected: " + entry.getValue());
                        return false;
                    }
                    break;
                }
                case "path":{
                    if (!remoteShare.getItemName().equals(entry.getValue())){
                        Log.log(Level.FINE, "Item name does not match - Remote: " + remoteShare.getItemName()
                                + " - Expected: " + entry.getValue());
                        return false;
                    }
                    break;
                }
                case "uid_owner":{
                    if (!remoteShare.getOwner().equals(entry.getValue())){
                        Log.log(Level.FINE, "Owner name does not match - Remote: " + remoteShare.getOwner()
                                + " - Expected: " + entry.getValue());
                        return false;
                    }
                    break;
                }
                case "permissions":{
                    if (!remoteShare.getPermissions().equals(entry.getValue())){
                        Log.log(Level.FINE, "Permissions do not match - Remote: " + remoteShare.getItemName()
                                + " - Expected: " + entry.getValue());
                        return false;
                    }
                    break;
                }
            }
        }
        Log.log(Level.FINE, "All fields match. Returning true");
        return true;
    }

    private HashMap turnListToHashmap(List<List<String>> dataList){
        HashMap<String, String> mapFields = new HashMap<String, String>();
        for (List<String> rows : dataList) {
            mapFields.put(rows.get(0),rows.get(1));
        }
        return mapFields;
    }
}

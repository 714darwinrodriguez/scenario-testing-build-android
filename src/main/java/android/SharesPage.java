package android;

import org.openqa.selenium.support.PageFactory;

import java.util.logging.Level;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.log.Log;

public class SharesPage extends CommonPage {

    private String createbox_id = "com.owncloud.android:id/canEditCreateCheckBox";
    private String changebox_id = "com.owncloud.android:id/canEditChangeCheckBox";
    private String deletebox_id = "com.owncloud.android:id/canEditDeleteCheckBox";
    private String editbox_id = "com.owncloud.android:id/canEditSwitch";
    private String sharebox_id = "com.owncloud.android:id/canShareSwitch";
    private String closeButtonid = "com.owncloud.android:id/closeButton";

    public SharesPage(){
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void switchCreate() {
        Log.log(Level.FINE, "Starts: Click create checkbox");
        waitById(5, createbox_id);
        boolean status = isCreateSelected();
        driver.findElementById(createbox_id).click();
    }

    public void switchChange() {
        Log.log(Level.FINE, "Starts: Click change checkbox");
        waitById(5, changebox_id);
        boolean status = isChangeSelected();
        driver.findElementById(changebox_id).click();
    }

    public void switchDelete() {
        Log.log(Level.FINE, "Starts: Click delete checkbox:");
        waitById(5, deletebox_id);
        boolean status = isDeleteSelected();
        driver.findElementById(deletebox_id).click();
    }

    public void switchShare() {
        Log.log(Level.FINE, "Starts: Switch share button");
        driver.findElementById(sharebox_id).click();
    }

    public void switchEditFile() {
        Log.log(Level.FINE, "Starts: Switch edit file button");
        driver.findElementById(editbox_id).click();
    }

    public boolean isCreateSelected(){
        return checkSwitch(createbox_id);
    }

    public boolean isChangeSelected(){
        return checkSwitch(changebox_id);
    }

    public boolean isDeleteSelected(){
        return checkSwitch(deletebox_id);
    }

    public boolean isEditFileSelected(){
        return checkSwitch(editbox_id);
    }

    private boolean checkSwitch(String id){
        if (!driver.findElementsById(id).isEmpty()) {
            return driver.findElementById(id).isEnabled();
        } else {
            return false;
        }
    }

    public boolean isEditPermission(){
        return isCreateSelected() || isChangeSelected() || isDeleteSelected();
    }

    public boolean isEditFileEnabled(){
        return isEditFileSelected();
    }

    public boolean isShareEnabled(){
        return driver.findElementById(sharebox_id).isEnabled();
    }

    public boolean isEditEnabled(){
        return driver.findElementById(changebox_id).isEnabled();
    }

    public boolean isPasswordEnabled () {
        return true;
    }

    public void close(){
        driver.findElementById(closeButtonid).click();
    }
}

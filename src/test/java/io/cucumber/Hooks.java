package io.cucumber;

import android.AppiumManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before
    public void setup(){
        AppiumManager.getManager().getDriver().launchApp();
    }

    @After
    public void tearDown(){
        AppiumManager.getManager().getDriver().removeApp("com.owncloud.android");
    }
}

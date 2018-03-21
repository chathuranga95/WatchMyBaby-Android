package com.example.chathus.watchmybaby;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;

import com.robotium.solo.Solo;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static junit.framework.Assert.assertTrue;

/**
 * Created by chathuranga on 3/20/2018.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                activityTestRule.getActivity());
    }

    @Test
    public void testAAViewBaby() {
        //Unlock the lock screen
        solo.unlockScreen();

        //open drawer
        solo.clickOnImageButton(0);

        //select View Baby and verify
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.waitForView(R.layout.activity_view_baby);

        //go back
        solo.goBack();

        //verify main activity
        solo.assertCurrentActivity("OK", MainActivity.class);

    }

    @Test
    public void testABScheduleLullaby() {
        //Unlock the lock screen
        solo.unlockScreen();

        //open drawer
        solo.clickOnImageButton(0);

        //select schedule lullaby and verify
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.waitForView(R.layout.activity_schecule_lullaby);

        //go back
        solo.goBack();

        //verify main activity
        solo.assertCurrentActivity("OK", MainActivity.class);

    }

    @Test
    public void testACclearNoti() {
        //Unlock the lock screen
        solo.unlockScreen();

        //open drawer
        solo.clickOnImageButton(0);

        //select select notification and wait for toast
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        assertTrue("Notification clear test passed", this.solo.waitForText("Notifications cleared."));
    }
    @Test
    public void testACclearNotiFloatingButton() {
        //Unlock the lock screen
        solo.unlockScreen();

        //click clear notification FAB and wait for toast
        View fab = solo.getCurrentActivity().findViewById(R.id.fab);
        solo.clickOnView(fab);

        assertTrue("Notification clear test passed", this.solo.waitForText("Notifications cleared."));
    }


    @Test
    public void testADSettings() {
        //Unlock the lock screen
        solo.unlockScreen();

        //open drawer
        solo.clickOnImageButton(0);

        //select settings and verify
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.waitForView(R.layout.activity_settings);

        //go back
        solo.goBack();

        //verify main activity
        solo.assertCurrentActivity("OK", MainActivity.class);
    }
    @Test
    public void testAEshare() {
        //Unlock the lock screen
        solo.unlockScreen();

        //open drawer
        solo.clickOnImageButton(0);

        //select share and verify
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.waitForText("Complete action using");

        //verify main activity
        solo.assertCurrentActivity("OK", MainActivity.class);
    }
    @Test
    public void testAFAbout() {
        //Unlock the lock screen
        solo.unlockScreen();

        //open drawer
        solo.clickOnImageButton(0);

        //select About and wait for dialog box to open
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.waitForDialogToOpen();
        solo.clickOnButton(0);

        //verify main activity
        solo.assertCurrentActivity("OK", MainActivity.class);
    }
    @Test
    public void testAGLogout() {
        //Unlock the lock screen
        solo.unlockScreen();

        //click on menu item 'Logout'
        solo.clickOnMenuItem("Logout");

        //verify Logout
        solo.waitForActivity(LoginActivity.class);
    }

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }
}
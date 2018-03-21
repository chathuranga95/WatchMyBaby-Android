package com.example.chathus.watchmybaby;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by chathuranga on 3/21/2018.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingsActivityTest {
    private Solo solo;
    private String correctNum1 = "0716905697";
    private String correctNum2 = "0774599326";
    private String wrongNum = "075489";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                activityTestRule.getActivity());

        testAAnavigate();
    }


    public void testAAnavigate() {
        //navigate to the Settings activity from main activity
        solo.unlockScreen();
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.waitForView(R.layout.activity_settings);
    }

    @Test
    public void testABsmsONwrong() {
        ToggleButton tglbtnSMS = solo.getCurrentActivity().findViewById(R.id.tglbtnSMS);
        //turn SMS on
        solo.clickOnView(tglbtnSMS);

        //just click save
        solo.clickOnButton("Save");

        assertTrue("Empty number SMS on rejected", this.solo.waitForText("Please input at least one number or turn SMS off"));
    }

    @Test
    public void testACsmsONwrongNum() {
        ToggleButton tglbtnSMS = solo.getCurrentActivity().findViewById(R.id.tglbtnSMS);
        //turn SMS on
        solo.clickOnView(tglbtnSMS);

        //input wrong number
        solo.enterText(0, wrongNum);

        //just click save
        solo.clickOnButton("Save");

        assertTrue("Setting save with one number success", this.solo.waitForText("Enter Number 1 correctly, or leave it empty"));

        //input wrong number
        solo.enterText(1, wrongNum);

        //just click save
        solo.clickOnButton("Save");

        assertTrue("Setting save with one number success", this.solo.waitForText("Enter Number 2 correctly, or leave it empty"));

    }

    @Test
    public void testADsmsONcorrectOneNum() {
        ToggleButton tglbtnSMS = solo.getCurrentActivity().findViewById(R.id.tglbtnSMS);
        //turn SMS on
        solo.clickOnView(tglbtnSMS);

        //input one number
        solo.enterText(0, correctNum1);

        //just click save
        solo.clickOnButton("Save");

        assertTrue("Setting save with one number success", this.solo.waitForText("Settings saved successfully."));
    }

    @Test
    public void testAEsmsONcorrectTwoNum() {

        //input second number
        solo.enterText(1, correctNum2);

        //click save
        solo.clickOnButton("Save");

        assertTrue("Setting save with two numbers success", this.solo.waitForText("Settings saved successfully."));
    }

    @Test
    public void testAFcheckSaved() {

        Activity activity = solo.getCurrentActivity();
        boolean sms = ((ToggleButton) activity.findViewById(R.id.tglbtnSMS)).isChecked();
        String num1 = solo.getText(0).toString().trim();
        String num2 = solo.getText(1).toString().trim();

        assertEquals("Save and retrieve SMS status success", sms, true);
        assertTrue("retrieve num success", this.solo.waitForText(correctNum1));
        assertTrue("retrieve num success", this.solo.waitForText(correctNum2));
//        assertEquals(num1, correctNum1);
//        assertEquals(num2, correctNum2);


    }

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
//        solo.finishOpenedActivities();
    }
}

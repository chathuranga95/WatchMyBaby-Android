package com.example.chathus.watchmybaby;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by chathuranga on 3/9/2018.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginActivityTest {

    private Solo solo;
    private String userName = "test";
    private String password = "12345";

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                activityTestRule.getActivity());
    }

    @Test
    public void testAANewUser() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Click Login button
        solo.clickOnView(solo.getView(R.id.btnNewUser));
        //Assert that new user creation activity is opened
        solo.assertCurrentActivity("Login passed.", CreateNewUserActivity.class);
        //click on back button
        solo.goBack();
    }

    @Test
    public void testABLogin() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//        assertEquals("com.example.chathus.watchmybaby", appContext.getPackageName());

        //Unlock the lock screen
        solo.unlockScreen();
        //Enter Username
        solo.enterText(0, userName);
        //Enter password
        solo.enterText(1, password);
        //Click Login button
        solo.clickOnView(solo.getView(R.id.btnLogin));
        //Assert that Main activity is opened
        solo.assertCurrentActivity("Login passed.", MainActivity.class);

    }

    @Test
    public void testACAutoLogin() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Assert that Main activity is opened automatically
        solo.assertCurrentActivity("Login form started", LoginActivity.class);
        solo.waitForView(R.layout.activity_main);
    }

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }
}

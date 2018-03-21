package com.example.chathus.watchmybaby;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static junit.framework.Assert.assertTrue;

/**
 * Created by chathuranga on 3/15/2018.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateNewUserActivityTest {

    private Solo solo;
    private String userName = "newTestUser";
    private String password = "3251";
    private String name = "newTestName";
    private String email = "newtestemail@gmail.com";
    private String phone = "0715623449";


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
    public void testAAInvalidCreateUser() {
        //Unlock the lock screen
        solo.unlockScreen();
        //goto create new user activity
        solo.clickOnView(solo.getView(R.id.btnNewUser));
        //Enter details
        solo.enterText(0, name);
        solo.enterText(1, email);
        //solo.enterText(2, phone);
        solo.enterText(3, userName);
        solo.enterText(4, password);
        solo.enterText(5, password);
        //Click Create button
        solo.clickOnView(solo.getView(R.id.btnOK));
        //wait for error msg toast
        assertTrue("Identified empty field",this.solo.waitForText("Please fill all the details."));
    }


    @Test
    public void testABCreateUser() {
        //Unlock the lock screen
        solo.unlockScreen();

        //goto create new user activity
        solo.clickOnView(solo.getView(R.id.btnNewUser));

        //Enter details
        solo.enterText(0, name);
        solo.enterText(1, email);
        solo.enterText(2, phone);
        solo.enterText(3, userName);
        solo.enterText(4, password);
        solo.enterText(5, password);

        //Click Create button
        solo.clickOnView(solo.getView(R.id.btnOK));

        //wait for toast
        assertTrue("user creation successful",this.solo.waitForText("New user Created successfully"));

        //go back
        solo.goBack();
    }

    @Test
    public void testACLoginWithNewUser() {
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

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }
}

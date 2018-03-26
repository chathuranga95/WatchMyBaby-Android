package com.example.chathus.watchmybaby;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import data.LocalDatabaseHandler;

import static junit.framework.Assert.assertEquals;

/**
 * Created by chathuranga on 3/22/2018.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseHandlerTest {

    private String userName = "user1";
    private LocalDatabaseHandler dbHandler;
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                activityTestRule.getActivity());

        dbHandler = new LocalDatabaseHandler(solo.getCurrentActivity().getApplicationContext());
    }

    /*
        @Test
        public void testAASaveLogin() {
            dbHandler.saveLogin(userName);
            assertEquals("Login saved successfullly", dbHandler.getLoggedUser(), userName);
        }

        @Test
        public void testABSaveLogout() {
            dbHandler.saveLogout();
            assertEquals("Logout saved successfullly", dbHandler.getLoggedUser(), null);
        }

    @Test
    public void testACclearNotification() {
        dbHandler.clearNotifications(userName);
        ArrayList<String> noti = dbHandler.retrieveNotifications(userName);
        assertEquals("noti clear succcess",noti.isEmpty(),true);
    }

    @Test
    public void testADsaveNotification() {
        dbHandler.saveNewNotification(userName,"26-03-2018","10:30:00","cry");
        dbHandler.saveNewNotification(userName,"26-03-2018","10:31:00","sms");

        ArrayList<String> noti = dbHandler.retrieveNotifications(userName);
        boolean res = false;
        res = noti.get(0).equals("26-03-2018, 10:31:00  : sms");
        res = res && noti.get(1).equals("26-03-2018, 10:30:00  : cry");
        assertEquals("noti retrieve success",res,true);
    }
    */

    @Test
    public void testAEsavesettings(){
        dbHandler.setSettingsOnDB(userName,"true","0713604485","","true");
        ArrayList<String> res = new ArrayList<>();
        res.add("true");
        res.add("0713604485");
        res.add("");
        res.add("true");
        assertEquals("settings save success",dbHandler.retrieveSettings(userName),res);
    }
}

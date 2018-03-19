package com.example.chathus.watchmybaby;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.HashMap;

import utill.User;
import utill.UserAppSettings;

import static junit.framework.Assert.assertEquals;

/**
 * Created by chathuranga on 3/19/2018.
 */


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {
    User user;

    //create a valid user object
    @Before
    public void initUser(){
        user = new User("user1","username1","1234","user@abc.com",713604485);
    }

    //some user input format agreed test cases.
    //user inputs for the file names and file times are restricted with the UI.
    @Test
    public void test(){
        //settings initialize
        UserAppSettings settings = new UserAppSettings();

        //add a file with a time
        settings.addFile("lullaby1",1230);
        user.setSettings(settings);

        //verify the file is available on the user object
        HashMap<String,Integer> fileList = new HashMap<>();
        fileList.put("lullaby1",1230);
        assertEquals(user.getSettings().getFileList(),fileList);

        //change the "lullaby1's time"
        settings.editFileTime("lullaby1",1430);
        user.setSettings(settings);

        //verify the time has edited
        fileList.clear();
        fileList.put("lullaby1",1430);
        assertEquals(user.getSettings().getFileList(),fileList);

        //add another file
        settings.addFile("lullaby2",1020);
        user.setSettings(settings);

        //verify
        fileList.put("lullaby2",1020);
        assertEquals(user.getSettings().getFileList(),fileList);

        //delete "lullaby1"
        user.getSettings().removeFile("lullaby1");

        //verify
        fileList.clear();
        fileList.put("lullaby2",1020);
        assertEquals(user.getSettings().getFileList(),fileList);
    }

}

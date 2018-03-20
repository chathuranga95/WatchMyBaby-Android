package com.example.chathus.watchmybaby;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import utill.SMSHandler;

/**
 * Created by chathuranga on 3/19/2018.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SMSHandlerTest {
    private SMSHandler smsHandler;

    @Before
    public void initSMSHandler(){
        smsHandler = new SMSHandler();
    }

    @Test
    public void testSendSms(){
        smsHandler.sendSMS("","This SMS is not supposed to sent anywhere");
        smsHandler.sendSMS("713604485","This is SMSHandlerTest success msg");
    }
}

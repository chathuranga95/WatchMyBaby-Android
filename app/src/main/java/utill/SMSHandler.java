package utill;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;

import com.example.chathus.watchmybaby.MainActivity;

/**
 * Created by chathuranga on 2/15/2018.
 */

public class SMSHandler {
    public void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
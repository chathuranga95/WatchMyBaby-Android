package utill;

import android.telephony.SmsManager;

/**
 * Created by chathuranga on 2/15/2018.
 */

public class SMSHandler {
    public void sendSMS(String phoneNumber, String message) {
        if (!phoneNumber.isEmpty()) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
        }
    }
}
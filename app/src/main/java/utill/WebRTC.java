package utill;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.chathus.watchmybaby.MainActivity;
import com.example.chathus.watchmybaby.R;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import data.LocalDatabaseHandler;

/**
 * Created by chathuranga on 2/21/2018.
 */

public class WebRTC extends Thread {

    static NotificationManager mNotificationManager;
    static String userName;
    final static String TAG = "msgcheck";
    static Context context;
    static MainActivity mainActivity;
    static int cryCounter;
    static long lastCryTime;
    static boolean isSMSON;
    static boolean isNoti;
    static boolean iscurrSMS = true;
    static String smsNum1;
    static String smsNum2;
    static LocalDatabaseHandler dbHandler;


//    public WebRTC(MainActivityOld act, NotificationManager nm, String uname){
//        activity = act;
//        mNotificationManager = nm;
//        userName = uname;
//        v = (Vibrator) activity.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//        Log.d(TAG, "paras inited");
//    }

    public void run() {
        startListening();
        Log.d(TAG, "started listening");
        Log.d(TAG, "curr sms" + iscurrSMS);
    }

    public static void setParas(Context cnt, NotificationManager nm, String uname) {
        context = cnt;
        mNotificationManager = nm;
        userName = uname;
        cryCounter = 0;
        lastCryTime = (Calendar.getInstance()).getTimeInMillis();

        dbHandler = new LocalDatabaseHandler(context);
        ArrayList<String> settings = dbHandler.retrieveSettings(userName);
        isNoti = settings.get(3).equals("true");
        isSMSON = settings.get(0).equals("true");
        smsNum1 = settings.get(1);
        smsNum2 = settings.get(2);

        Log.d(TAG, "paras inited");
    }


    public static void setCurrSMSOnOff(boolean sms) {
        iscurrSMS = sms;
    }

    public static void setMainActivity(MainActivity ma) {
        mainActivity = ma;
    }
    //start listening to the channel specified to the given user,
    //feed the notification to the main activity again

    public static void pushNotification(String msg) {
        if (isNoti) {
            //vibrator pattern
            long[] patt = new long[3];
            patt[0] = 200;
            patt[1] = 100;
            patt[2] = 200;

            //push msg to the notification area.
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.cute_ball_info)
                    .setContentTitle(msg)
                    .setContentText("Touch to open app")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(patt)
                    .setAutoCancel(true);
            Intent[] intents = new Intent[1];
            intents[0] = new Intent(context, MainActivity.class);
            PendingIntent PI = PendingIntent.getActivities(context, 0, intents, 0);
            mBuilder.setContentIntent(PI);
            mNotificationManager.notify(001, mBuilder.build());
        }
    }

    public static void startListening() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-616fc02a-063a-41cd-8185-dcf5ba936b2a");
        pnConfiguration.setSubscribeKey("sub-c-f72b3372-f5da-11e7-8098-329148162fa8");
        PubNub pubnub = new PubNub(pnConfiguration);


        // Subscribe to the channel
        try {
            pubnub.addListener(new SubscribeCallback() {
                @Override
                public void status(PubNub pubnub, PNStatus status) {
                    if (status.getCategory() == PNStatusCategory.PNUnknownCategory) {
                        System.out.println(status.getErrorData());
                        Log.d(TAG, "Error1");
                    }
                }

                @Override
                public void message(PubNub pubnub, PNMessageResult message) {
                    String msg = message.getMessage().toString();
                    Log.d(TAG, msg);


                    //setup the current date and time
                    Calendar calendar = Calendar.getInstance();

                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int min = calendar.get(Calendar.MINUTE);
                    int sec = calendar.get(Calendar.SECOND);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    int mm = calendar.get(Calendar.MONTH);
                    int yy = calendar.get(Calendar.YEAR);

                    String seconds;
                    if(sec<10){
                        seconds = "0" + sec;
                    }
                    else{
                        seconds = "" + sec;
                    }

                    String minutes;
                    if(min<10){
                        minutes = "0" + min;
                    }
                    else{
                        minutes = "" + min;
                    }

                    String date = yy + "-" + mm + "-" + dd;
                    String time = hour + ":" + minutes + ":" + seconds;

                    synchronized (new Object()) {
                        //save notification on the local database
                        dbHandler.saveNewNotification(userName, date, time, "baby cried");
                    }


                    if (cryCounter < 3) {
                        long cryInterval = calendar.getTimeInMillis() - lastCryTime;
                        if (cryInterval < 60000) {
                            cryCounter++;
                            lastCryTime = calendar.getTimeInMillis();

                            Log.d(TAG, "Cry time " + cryCounter + "  cried within " + cryInterval);

                            if (cryCounter >= 3) {
                                if (isSMSON && iscurrSMS) {
                                    SMSHandler smsHandler = new SMSHandler();
                                    int noOfSMS = 0;
                                    if (!smsNum1.equals("")) {
//                                        smsHandler.sendSMS(smsNum1, "Please watch the baby at next door \n Automated message from Watch My Baby");
                                        Log.d(TAG, "SMS sent");
                                        noOfSMS++;
                                        pushNotification(noOfSMS + " SMS's Sent");
                                        dbHandler.saveNewNotification(userName, date, time, "SMS sent to "+smsNum1);
                                    }
                                    if (!smsNum2.equals("")) {
//                                        smsHandler.sendSMS(smsNum2, "Please watch the baby at next door \n Automated message from Watch My Baby");
                                        Log.d(TAG, "SMS sent");
                                        noOfSMS++;
                                        pushNotification(noOfSMS + " SMS's Sent");
                                        dbHandler.saveNewNotification(userName, date, time, "SMS sent to "+smsNum2);
                                    }
                                }
                                cryCounter = 0;
                            } else {
                                pushNotification("Baby Cried " + cryCounter + " times");
                            }
                        } else {
                            cryCounter = 1;
                        }
                    }

//                    MainActivityOld ac = new MainActivityOld();
//                    ac.refreshNotificationView();

                    //bv.setBoo(!bv.isBoo());

                    //change main activity's view
                    try {
                        mainActivity.refreshNotificationView();
                    } catch (Exception ex) {
                        //
                    }
                }

                @Override
                public void presence(PubNub pubnub, PNPresenceEventResult presence) {

                }
            });

            pubnub.subscribe()
                    .channels(Arrays.asList("watchMyBaby" + userName))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

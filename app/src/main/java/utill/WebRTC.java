package utill;


import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;

import com.example.chathus.watchmybaby.MainActivity;
import com.example.chathus.watchmybaby.R;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import data.LocalDatabaseHandler;

/**
 * Created by chathuranga on 2/21/2018.
 */

public class WebRTC extends Thread {

    static NotificationManager mNotificationManager;
    static String userName;
    final static String TAG = "msgcheck";
    static Context context;
    static BooVariable bv;
    static MainActivity mainActivity;
    static int cryCounter;
    static long lastCryTime;
    static boolean isSMSON;
    static String smsNum1;
    static String smsNum2;


//    public WebRTC(MainActivity act, NotificationManager nm, String uname){
//        activity = act;
//        mNotificationManager = nm;
//        userName = uname;
//        v = (Vibrator) activity.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//        Log.d(TAG, "paras inited");
//    }

    public void run() {
        startListening();
        Log.d(TAG, "started listening");
    }

    public static void setParas(Context cnt, NotificationManager nm, String uname) {
        context = cnt;
        mNotificationManager = nm;
        userName = uname;
        cryCounter = 0;
        lastCryTime = (Calendar.getInstance()).getTimeInMillis();
        Log.d(TAG, "paras inited");
        isSMSON = false;
    }

    public static void setBooForWebRTC(BooVariable booVariable) {
        bv = booVariable;
    }

    public static void setSMSSettings(boolean sms, String n1, String n2) {
        isSMSON = sms;
        smsNum1 = n1;
        smsNum2 = n2;
    }

    public static void setSMSOnOff(boolean sms) {
        isSMSON = sms;
    }

    public static void setMainActivity(MainActivity ma) {
        mainActivity = ma;
    }
    //start listening to the channel specified to the given user,
    //feed the notification to the main activity again

    public static void pushNotification(String msg){
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
                .setVibrate(patt);
        Intent[] intents = new Intent[1];
        intents[0] = new Intent(context, MainActivity.class);
        PendingIntent PI = PendingIntent.getActivities(context, 0, intents, 0);
        mBuilder.setContentIntent(PI);
        mNotificationManager.notify(001, mBuilder.build());
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

                    String date = yy + "-" + mm + "-" + dd;
                    String time = hour + ":" + min + ":" + sec;

                    synchronized (new Object()) {
                        //save notification on the local database
                        LocalDatabaseHandler dbHandler = new LocalDatabaseHandler(context);
                        dbHandler.saveNewNotification(userName, date, time, "baby cried");
                    }


                    if (cryCounter < 3) {
                        long cryInterval = calendar.getTimeInMillis() - lastCryTime;
                        if (cryInterval < 60000) {
                            cryCounter++;
                            lastCryTime = calendar.getTimeInMillis();

                            Log.d(TAG, "Cry time " + cryCounter + "  cried within " + cryInterval);

                            if (cryCounter >= 3) {
                                if (isSMSON || true) {
                                    SMSHandler smsHandler = new SMSHandler();
//                                    smsHandler.sendSMS(smsNum1, "Please watch the baby at next door \n Automated message from Watch My Baby");
//                                    smsHandler.sendSMS(smsNum2, "Please watch the baby at next door \n Automated message from Watch My Baby");
                                    Log.d(TAG, "SMS sent");
                                    pushNotification("SMS's Sent");
                                }
                                cryCounter = 0;
                            }
                            else{
                                pushNotification("Baby Cried " +cryCounter+ " times");
                            }
                        } else {
                            cryCounter = 1;
                        }
                    }

//                    MainActivity ac = new MainActivity();
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

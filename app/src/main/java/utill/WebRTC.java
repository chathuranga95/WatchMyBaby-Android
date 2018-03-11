package utill;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
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

import java.util.Arrays;
import java.util.Calendar;

import data.LocalDatabaseHandler;

/**
 * Created by chathuranga on 2/21/2018.
 */

public class WebRTC {

    static MainActivity activity;
    static NotificationManager mNotificationManager;
    static String userName;
    final static String TAG = "msgcheck";
    static Vibrator v;


    public static void setParas(MainActivity act, NotificationManager nm, String uname) {
        activity = act;
        mNotificationManager = nm;
        userName = uname;
        v = (Vibrator) activity.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        Log.d(TAG, "paras inited");
    }

    //start listening to the channel specified to the given user,
    //feed the notification to the main activity again
    //TODO: implement in a service
    public static void startListening() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-616fc02a-063a-41cd-8185-dcf5ba936b2a");
        pnConfiguration.setSubscribeKey("sub-c-f72b3372-f5da-11e7-8098-329148162fa8");
        PubNub pubnub = new PubNub(pnConfiguration);


        /* Subscribe to the demo_tutorial channel */
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

                    //push msg to the notification area.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(activity.getApplicationContext())
                                    .setSmallIcon(R.drawable.cute_ball_info)
                                    .setContentTitle("Watch My Baby")
                                    .setContentText(msg);
                    mNotificationManager.notify(001, mBuilder.build());


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
                        LocalDatabaseHandler dbHandler = new LocalDatabaseHandler(activity.getApplicationContext());
                        dbHandler.saveNewNotification(userName, date, time, "baby cried");
                    }

                    MainActivity ac = new MainActivity();
                    ac.refreshNotificationView();

                    //vibrate phone
                    v.vibrate(500);

                    //ring the defaut ringtone
                    //TODO: replace media player code with phone ringing code.
                    MediaPlayer player = MediaPlayer.create(activity, Settings.System.DEFAULT_NOTIFICATION_URI);
                    player.start();
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

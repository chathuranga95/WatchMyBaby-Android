package utill;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import data.LocalDatabaseHandler;

/**
 * Created by chathuranga on 2/21/2018.
 */
public class NotificationService extends Service {

    String TAG = "notificationInfo";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread thread = new Thread(new WebRTC()); //different thread for the WebRTC listener

        //prepare parameters for WebRTC object
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        LocalDatabaseHandler dbHandler = new LocalDatabaseHandler(getApplicationContext());
        String uname = dbHandler.getLoggedUser();
        Log.d(TAG, "username received by notification listener :" + uname);

        //initialize webRTC and start listening to the user specified channel, if the user is not logged-out
        if (uname != null) {
            WebRTC.setParas(getApplicationContext(), mNotificationManager, uname);
            thread.run();
        }
        return START_STICKY;//super.onStartCommand(intent, flags, startId);
    }
}
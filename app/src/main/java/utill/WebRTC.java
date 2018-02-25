package utill;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.chathus.watchmybaby.MainActivity;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

/**
 * Created by chathuranga on 2/21/2018.
 */

public class WebRTC {

    MainActivity activity;

    public WebRTC(MainActivity activity) {
        this.activity = activity;
    }

    //start listening to the channel specified to the given user,
    //feed the notification to the main activity again
    //TODO: implement in a service
    public void startListening(String userName) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-616fc02a-063a-41cd-8185-dcf5ba936b2a");
        pnConfiguration.setSubscribeKey("sub-c-f72b3372-f5da-11e7-8098-329148162fa8");
        PubNub pubnub = new PubNub(pnConfiguration);
        final String TAG = "msgcheck";

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

                    //NotificationService.setMsg(msg);
                    activity.pushNotification(msg);
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

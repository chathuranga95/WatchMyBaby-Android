package com.example.chathus.watchmybaby;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONObject;

import java.util.Arrays;

import utill.NotificationService;
import utill.WebRTC;

public class MainActivity extends AppCompatActivity {
    String userName ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //retrieve username
        userName = getIntent().getStringExtra("userName");

        //Create a webRTC object and start listening to the user specified channel
        WebRTC webRTC = new WebRTC(MainActivity.this);
        webRTC.startListening(userName);

        //TODO: append notification here
        ListView listView = (ListView) findViewById(R.id.lstNotifications);
        listView.setSelection(1);
        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        startService(new Intent(this, NotificationService.class));
//    }

    //publish a given message on the notification area
    //TODO: implement this part in a seperate class
    public void pushNotification(String msg){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.cute_ball_info)
                        .setContentTitle("Watch My Baby")
                        .setContentText(msg);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }

    public void logout(View view) {

    }

    public void viewBaby(View view) {
        Intent myIntent = new Intent(MainActivity.this, ViewBabyActivity.class);
        startActivity(myIntent);
    }

    public void scheduleLullaby(View view) {
        Intent myIntent = new Intent(MainActivity.this, ScheculeLullabyActivity.class);
        myIntent.putExtra("userName",userName);
        startActivity(myIntent);
    }

    public void settings(View view) {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(myIntent);
    }

    public void logout1(View view) {
        final String TAG = "pubnubExample";

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-f72b3372-f5da-11e7-8098-329148162fa8");
        pnConfiguration.setPublishKey("pub-c-616fc02a-063a-41cd-8185-dcf5ba936b2a");

        PubNub pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                    Log.d(TAG, "radio / connectivity is lost");

                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc
                    Log.d(TAG, "Connected");

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                        pubnub.publish().channel("channelChathuWatchBaby").message("hello WebRTC!!").async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // Check whether request successfully completed or not.
                                if (!status.isError()) {

                                    // Message successfully published to specified channel.
                                    Log.d(TAG, "Message successfully published to specified channel.");
                                }
                                // Request processing failed.
                                else {

                                    // Handle message publish error. Check 'category' property to find out possible issue
                                    // because of which request did fail.
                                    //
                                    // Request can be resent using: [status retry];
                                    Log.d(TAG, "request processing Failed");
                                }
                            }
                        });
                    }
                } else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                    Log.d(TAG, "radio / connectivity is lost, then regained.");
                } else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                    Log.d(TAG, "encrypt messages and on live data feed it received plain text.");
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    message.getChannel();
                    Log.d(TAG, "Channel took");
                } else {
                    // Message has been received on channel stored in
                    message.getSubscription();
                    Log.d(TAG, "Subscription took");
                }

            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()

            */

                Log.d(TAG, message.getMessage().toString());
                Log.d(TAG, message.getSubscription().toString());
                Log.d(TAG, message.getTimetoken().toString());
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                Log.d(TAG, presence.getState().toString());
            }
        });

        pubnub.subscribe().channels(Arrays.asList("channelChathuWatchBaby")).withPresence().execute();


    } //currently not in use code
}

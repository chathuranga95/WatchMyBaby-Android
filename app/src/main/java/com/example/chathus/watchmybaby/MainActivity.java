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

import data.LocalDatabaseHandler;
import utill.NotificationService;
import utill.SMSHandler;
import utill.WebRTC;

public class MainActivity extends AppCompatActivity {
    String userName = "";
    LocalDatabaseHandler dbHandler;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get database Handler Object
        dbHandler = new LocalDatabaseHandler(this);

        //retrieve username
        userName = getIntent().getStringExtra("userName");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Create a webRTC object and start listening to the user specified channel
        WebRTC.setParas(MainActivity.this, mNotificationManager, userName);

        //start notification listening service.
        startService(new Intent(this, NotificationService.class));
        refreshNotificationView();
    }

    public void refreshNotificationView() {
        //fill up listview with last 10 notifications.
        listView = (ListView) findViewById(R.id.lstNotifications);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, dbHandler.retrieveNotifications(userName));
        listView.setAdapter(adapter);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        startService(new Intent(this, NotificationService.class));
//    }

    public void logout(View view) {
        LocalDatabaseHandler dbHandler = new LocalDatabaseHandler(this);
        dbHandler.saveLogout(userName);
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(myIntent);
    }

    public void viewBaby(View view) {
        Intent myIntent = new Intent(MainActivity.this, ViewBabyActivity.class);
        myIntent.putExtra("userName", userName);
        startActivity(myIntent);
    }

    public void scheduleLullaby(View view) {
        Intent myIntent = new Intent(MainActivity.this, ScheculeLullabyActivity.class);
        myIntent.putExtra("userName", userName);
        startActivity(myIntent);
    }

    public void settings(View view) {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(myIntent);
    }

    public void clearNotifications(View view) {
        dbHandler.clearNotifications(userName);
        refreshNotificationView();
    }
}
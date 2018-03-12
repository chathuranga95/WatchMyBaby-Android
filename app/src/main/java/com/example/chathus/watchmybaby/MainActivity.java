package com.example.chathus.watchmybaby;

import android.app.NotificationManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import data.LocalDatabaseHandler;
import utill.NotificationService;
import utill.SMSHandler;
import utill.WebRTC;

public class MainActivity extends AppCompatActivity {

    String userName = "";
    LocalDatabaseHandler dbHandler;
    ListView listView;
    NotificationManager mNotificationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get database Handler Object
        dbHandler = new LocalDatabaseHandler(this);

        //retrieve username
        userName = getIntent().getStringExtra("userName");

        //notification service object
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //start notification listening service.
        startService(new Intent(this, NotificationService.class));

        //refresh notification viewing list view
        refreshNotificationView();
    }

    //fill up listview with last 10 notifications.
    public void refreshNotificationView() {
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

    //logout button click event
    public void logout(View view) {
        dbHandler.saveLogout(userName); //user logout store on local db
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class); //show login window
        startActivity(myIntent);
    }

    //show video call window; parse the username through the intent
    public void viewBaby(View view) {
        Intent myIntent = new Intent(MainActivity.this, ViewBabyActivity.class);
        myIntent.putExtra("userName", userName);
        startActivity(myIntent);
    }

    //show schedule lullaby window; parse the username through the intent
    public void scheduleLullaby(View view) {
        Intent myIntent = new Intent(MainActivity.this, ScheculeLullabyActivity.class);
        myIntent.putExtra("userName", userName);
        startActivity(myIntent);
    }

    //show settings window
    public void settings(View view) {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(myIntent);
    }

    //clear notification on view and on the local storage
    public void clearNotifications(View view) {
        dbHandler.clearNotifications(userName);
        refreshNotificationView();
    }
}
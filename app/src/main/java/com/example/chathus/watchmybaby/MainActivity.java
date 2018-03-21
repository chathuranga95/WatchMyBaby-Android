package com.example.chathus.watchmybaby;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import data.LocalDatabaseHandler;
import utill.FileHandler;
import utill.NotificationService;
import utill.WebRTC;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String userName = "";
    LocalDatabaseHandler dbHandler;
    ListView listView;
    NotificationManager mNotificationManager;

    final String TAG = "MainActivityInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                dbHandler.clearNotifications(userName);
                refreshNotificationView();
                Toast.makeText(getApplicationContext(), "Notifications cleared.", Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //get database Handler Object
        dbHandler = new LocalDatabaseHandler(this);

        //retrieve username
        userName = dbHandler.getLoggedUser();

        //redirect if the user is not logged in i.e. username is empty
        if (userName == null) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            return;
        }

        //notification service object
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        WebRTC.setMainActivity(MainActivity.this);
        WebRTC.setCurrSMSOnOff(false);

        //start notification listening service.
        startService(new Intent(this, NotificationService.class));

        //refresh notification viewing list view
        refreshNotificationView();

        Log.d(TAG, " main activity on create called");


//        bv.setListener(new BooVariable.ChangeListener()
//        {
//            @Override
//            public void onChange () {
//                Toast.makeText(MainActivityOld.this, "blah", Toast.LENGTH_LONG).show();
//                //refreshNotificationView();
//            }
//        });
    }

    //fill up listview with last 10 notifications.
    public void refreshNotificationView() {
        listView = (ListView) findViewById(R.id.lstNotifications);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, dbHandler.retrieveNotifications(userName));
        listView.setAdapter(adapter);
        Log.d(TAG, "notification view refresh called. username " + userName);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.view_baby) {
            Intent myIntent = new Intent(MainActivity.this, ViewBabyActivity.class);
            myIntent.putExtra("userName", userName);
            startActivity(myIntent);
        } else if (id == R.id.schedule_lullaby) {
            Intent myIntent = new Intent(MainActivity.this, ScheculeLullabyActivity.class);
            myIntent.putExtra("userName", userName);
            startActivity(myIntent);
        } else if (id == R.id.clear_notification) {
            dbHandler.clearNotifications(userName);
            refreshNotificationView();
            Toast.makeText(this, "Notifications cleared.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.settings) {
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Find the Watch My Baby Web Application and Android Application Here\nhttps://watchmybaby-52d18.firebaseapp.com");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.about) {
            //show about dialog box
            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("Watch My Baby - Version 1.0 (2018)" +
                            "\n\nThis app is used to monitor children coupled with the web application you can find by the URL," +
                            "\n" +
                            Html.fromHtml("<a href=\"https://watchmybaby-52d18.firebaseapp.com\">https://watchmybaby-52d18.firebaseapp.com</a>") +
                            "\n\nFeatures" +
                            "\n\t"+
                            Html.fromHtml("<b>&bull</b>")+
                            " Watch the baby live - View Baby" +
                            "\n\t"+
                            Html.fromHtml("<b>&bull</b>")+
                            " Automatically detect baby cry and alerting" +
                            "\n\t"+
                            Html.fromHtml("<b>&bull</b>")+
                            " Receive notifications even when the app is closed\n\tUpload and schedule lullabies to be played for the baby" +
                            "\n\t"+
                            Html.fromHtml("<b>&bull</b>")+
                            " Automatically send messages to the neighbours when you are busy" +
                            "\n\nThe app was developed by Chathuranga Siriwardhana for the 3rd year SEP at University of Moratuwa.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //close and do nothing
                        }
                    })
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void logout(MenuItem item) {
        //logout button click event
        dbHandler.saveLogout(userName); //user logout store on local db
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class); //show login window
        startActivity(myIntent);
    }
}

package com.example.chathus.watchmybaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import data.LocalDatabaseHandler;

public class SettingsActivity extends AppCompatActivity {

    EditText num1;
    EditText num2;
    ToggleButton tglbtnSMS;
    ToggleButton tglbtnNoti;
    LocalDatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        num1 = findViewById(R.id.txtNum1);
        num2 = findViewById(R.id.txtNum2);
        tglbtnSMS = findViewById(R.id.tglbtnSMS);
        tglbtnNoti = findViewById(R.id.tglbtnNoti);
        dbHandler = new LocalDatabaseHandler(getApplicationContext());

        //append current settings on the view
        retrieveSettingsDatafromDB();
    }

    private void retrieveSettingsDatafromDB() {
        ArrayList<String> settings = dbHandler.retrieveSettings(dbHandler.getLoggedUser());
        tglbtnSMS.setChecked(settings.get(0).equals("true"));
        tglbtnNoti.setChecked(settings.get(3).equals("true"));
        num1.setText(settings.get(1));
        num2.setText(settings.get(2));
    }

    public void saveOnDB(View view) {
        String sms;
        String noti;

        if (tglbtnSMS.isChecked()) {
            sms = "true";
        } else {
            sms = "false";
        }

        if (tglbtnNoti.isChecked()) {
            noti = "true";
        } else {
            noti = "false";
        }


        boolean success =  dbHandler.setSettingsOnDB(dbHandler.getLoggedUser(), sms, num1.getText().toString().trim(), num2.getText().toString().trim(), noti);
        if(success){
            Toast.makeText(getApplicationContext(),"Settings saved successfully.",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Error occured, please try again",Toast.LENGTH_SHORT).show();
        }
    }
}

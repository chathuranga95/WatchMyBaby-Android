package com.example.chathus.watchmybaby;

import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import utill.FileHandler;
import utill.User;

public class ScheculeLullabyActivity extends AppCompatActivity {
    ArrayList<String> values = new ArrayList<>();
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    String format; //time format
    Button btnSetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schecule_lullaby);
        btnSetTime  = (Button) findViewById(R.id.btnSetTime);
        setTitle("Schedule Lullabies");

        //disable set time button at loading
        btnSetTime.setEnabled(false);

        //refresh file list
        refreshFileList();
    }

    private void refreshFileList(){
        ListView listView = (ListView) findViewById(R.id.lstFiles);
        final String TAG = "fillingfilelist";
        // get database instance and slot
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(getIntent().getStringExtra("userName"));
        Log.d(TAG, "instance and ref retrieved");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create User Object out of database data
                User user = (User) dataSnapshot.getValue(User.class);
                Log.d(TAG, "user object received...");
                Map<String, String> fileDetalis = user.getSettings().getFileList();
                for (String key : fileDetalis.keySet()) {
                    values.add(key + "   :" + fileDetalis.get(key));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value, log details.
                Log.w(TAG, "Failed to read value.", error.toException());

                // show a toast to user
                CharSequence text = "Login Error, Please Try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });


//        values = new String[]{"File 001", "file 002", "file 003", "file 004", "file 005", "file 006"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        // ListView on item selected listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                btnSetTime.setEnabled(true);
                Toast.makeText(ScheculeLullabyActivity.this, values.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void chooseFile(View view) {
        Intent intent = new Intent();
        intent.setType("*/*"); //TODO: set file type to audio formats.
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 302);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 302) {
                if (null == data) {
                    return;
                }
                Uri file = data.getData();
                FileHandler fileHandler = new FileHandler(this, getIntent().getStringExtra("userName"));
                fileHandler.upload(file);
            }
        }
    }

    public void showContent(View view) {
//        ListView listView = (ListView) findViewById(R.id.lstFiles);
//        CharSequence text = listView.getSelectedItem().toString();
//        int duration = Toast.LENGTH_SHORT;
//        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//        toast.show();

    }

    public void setTime(View view) {
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);


        timepickerdialog = new TimePickerDialog(ScheculeLullabyActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        Log.d("timechoose", hourOfDay + ":" + minute + format);
                    }
                }, hour, min, false);
        timepickerdialog.show();
    }
}
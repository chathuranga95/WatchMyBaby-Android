package com.example.chathus.watchmybaby;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.Authenticator;

import utill.FileHandler;

public class ScheculeLullabyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schecule_lullaby);
        setTitle("Schedule Lullabies");

        ListView listView = (ListView) findViewById(R.id.lstFiles);
        //listView.setItemChecked(1,true);
        //listView.setSelection(0);
        //listView.getSelectedView().setSelected(true);
        //TODO: make firebase storage location's files append here.
        String[] values = new String[]{"File 001", "file 002", "file 003", "file 004", "file 005", "file 006"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
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
                if (null == data){
                    return;
                }
                Uri file = data.getData();
                FileHandler fileHandler = new FileHandler(this);
                fileHandler.upload(file);
            }
        }
    }
}

package utill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chathus.watchmybaby.LoginActivity;
import com.example.chathus.watchmybaby.MainActivity;
import com.example.chathus.watchmybaby.R;
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

import java.util.Map;

/**
 * Created by chathuranga on 2/15/2018.
 */

public class FileHandler {
    Context context;
    String userName;
    Map<String, String> fileDetails = null;
    public FileHandler(Context context,String userName){
        this.context = context;
        this.userName = userName;
    }


    public void setFileDataOnDB(final String fileName, String userName){
        final String TAG = "FileMetaData";


        // get database instance and slot
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(userName);
        Log.d(TAG, "instance and ref retrieved");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create User Object out of database data
                User user = (User) dataSnapshot.getValue(User.class);
                Log.d(TAG, "user object received...");

                //add the filename to the settings section
                user = addNewAttributes(user,fileName);
                Log.d(TAG, "user object modified...");
                //Log.d(TAG, "Value is: " + user.getDetailString());

                //write new User object to the database
                myRef.setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value, log details.
                Log.w(TAG, "Failed to read value.", error.toException());

                // show a toast to user
                CharSequence text = "Login Error, Please Try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    private User addNewAttributes(User user,String fileName){
        UserAppSettings settings = new UserAppSettings();
        settings.addFile(fileName,0000);
        user.setSettings(settings);
        return user;
    }

    public void upload(Uri file) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        final String filename = file.getLastPathSegment();


        //Uri file = Uri.fromFile(new File("/sdcard/Subani_Harshani_Ekama_Eka_Warak.mp3"));
        StorageReference abcfileRef = storageRef.child("songs/" + filename);
        UploadTask uploadTask;
        uploadTask = abcfileRef.putFile(file);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d("File-upload","Upload is " + progress + "% done");
                ((TextView) ((Activity)context).findViewById(R.id.lblProgress)).setText("Upload is " + progress + "% done");
            }
        });

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("File-upload","Upload completed");
                ((TextView) ((Activity)context).findViewById(R.id.lblProgress)).setText("Upload Completed");
                setFileDataOnDB(filename,userName);
            }
        });
    }
}

package utill;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.example.chathus.watchmybaby.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by chathuranga on 2/15/2018.
 */

public class FileHandler {
    Context context;
    public FileHandler(Context context){
        this.context = context;
    }

    public void upload(Uri file) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        String filename = file.getLastPathSegment();


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
            }
        });
    }
}

package com.example.chathus.watchmybaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import data.LocalDatabaseHandler;
import utill.ProductCipher;
import utill.User;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText txtOldPass;
    EditText txtNewPass;
    EditText txtConfirmNewPass;
    ProductCipher ps;
    LocalDatabaseHandler dbHandler;
    String username;
    String oldPass;
    String newPass;
    String confirmNewPass;
    final String TAG = "changePassInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //initialize the text boxes
        txtOldPass = findViewById(R.id.txtOldPass);
        txtNewPass = findViewById(R.id.txtNewPass);
        txtConfirmNewPass = findViewById(R.id.txtConfirmNewPass);

        //initialize Product Cipher object
        ps = new ProductCipher();

        //initialize database handler object
        dbHandler = new LocalDatabaseHandler(this);

        //get username
        username = dbHandler.getLoggedUser();

        //set Title with username
        setTitle("Account Settings: " + username);
    }

    public void changePsw(View view) {

        //get String values
        oldPass = txtOldPass.getText().toString().trim();
        newPass = txtNewPass.getText().toString().trim();
        confirmNewPass = txtConfirmNewPass.getText().toString().trim();

        // get database instance and slot
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(username);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create User Object out of database data
                User user = (User) dataSnapshot.getValue(User.class);
                String encPsw;

                if (!oldPass.isEmpty()) {
                    //check whether the old password matches
                    ProductCipher ps = new ProductCipher();
                    encPsw = ps.Encrypt("watch my baby username " + username, oldPass);
                } else {
                    encPsw = "";
                }

                if (encPsw.equals(user.getPassword())) { //if old psw correct,
                    //check whether the new password is empty
                    if (newPass.isEmpty()) {
                        Toast.makeText(ChangePasswordActivity.this, "Pleasse enter a new password!", Toast.LENGTH_SHORT).show();
                        txtNewPass.requestFocus();
                    } else { //if new password is not empty;
                        //check the new password and confirm matches
                        if (newPass.equals(confirmNewPass)) { //if matches
                            user.setPassword(ps.Encrypt("watch my baby username " + username, newPass)); //assign new psw to the object
                            myRef.setValue(user); //update object on the db
                            Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            finish(); //guide user back to the main activity
                        } else { //not matches
                            Toast.makeText(ChangePasswordActivity.this, "Password confirmation not match!", Toast.LENGTH_SHORT).show();
                            txtConfirmNewPass.setText("");
                            txtConfirmNewPass.requestFocus();
                        }
                    }
                } else { //if old psw incorrect
                    Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect!", Toast.LENGTH_SHORT).show();
                    txtOldPass.setText("");
                    txtOldPass.requestFocus();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChangePasswordActivity.this, "Error occured! try again!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}

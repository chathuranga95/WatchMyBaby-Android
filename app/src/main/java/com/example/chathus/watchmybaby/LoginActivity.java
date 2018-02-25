package com.example.chathus.watchmybaby;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import utill.User;

public class LoginActivity extends AppCompatActivity {
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        findViewById(R.id.txtUserName).requestFocus();
    }

    public void Login(View view) {

        final String TAG = "firebase-login";
        final Context context = getApplicationContext();

        //get user inputs
        userName = ((EditText) findViewById(R.id.txtUserName)).getText().toString();


        // get database instance and slot
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(userName);
        Log.d(TAG, "instance and ref retrieved");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create User Object out of database data
                User user = (User) dataSnapshot.getValue(User.class);
                Log.d(TAG, "user object received...");
                //Log.d(TAG, "Value is: " + user.getDetailString());

                //Login logic here
                if (compare(user)) {

                    //Login success, show main page.
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    myIntent.putExtra("userName",userName);
                    startActivity(myIntent);


                } else {
                    // show a toast to user
                    CharSequence text = "Username or Password incorrect!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    //Let user to try again
                    clearInputs();
                }

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

    public boolean compare(User user) {
        //compare password with database data
        String psw = ((EditText) findViewById(R.id.txtPass)).getText().toString();
        if (user == null) {
            return false;
        } else if (user.getPassword().equals(psw)) {
            return true;
        } else {
            return false;
        }
    }

    public void clearInputs() {
        //clear inputs and focus on username input field
        ((EditText) findViewById(R.id.txtUserName)).setText("");
        ((EditText) findViewById(R.id.txtPass)).setText("");
        findViewById(R.id.txtUserName).requestFocus(); //focus Usename textbox
    }

    public void signIn(View view) {
        Intent myIntent = new Intent(LoginActivity.this, CreateNewUserActivity.class);
        startActivity(myIntent);
    }
}

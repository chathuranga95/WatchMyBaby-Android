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

import java.util.ArrayList;

import utill.ProductCipher;
import utill.User;
import data.LocalDatabaseHandler;

public class LoginActivity extends AppCompatActivity {

    private String userName;
    LocalDatabaseHandler dbHandler;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        findViewById(R.id.txtUserName).requestFocus();

        dbHandler = new LocalDatabaseHandler(this); //new database handler object

        context = getApplicationContext(); //set context

        userName = dbHandler.getLoggedUser(); //check if there is a logged user.
        if (userName != null) { //if so, auto log in.
            autoLogin(userName);
        }
    }

    //auto login needs no verification.
    private void autoLogin(String userName) {
        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        myIntent.putExtra("userName", userName);
        startActivity(myIntent);
    }

    //Login button click method
    public void Login(View view) {

        final String TAG = "firebase-login";

        //get user inputs
        userName = ((EditText) findViewById(R.id.txtUserName)).getText().toString().trim();

        // get database instance and slot
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(userName);
        Log.d(TAG, "instance and ref retrieved");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create User Object out of database data
                User user = (User) dataSnapshot.getValue(User.class);
                Log.d(TAG, "user object received...");

                //Login logic here
                if (compare(user)) {

                    //Login success, save the username for future auto logins
                    dbHandler.saveLogin(userName);

                    //check for settings
                    ArrayList<String> settings = dbHandler.retrieveSettings(userName);

                    if (settings.get(0).equals("false")) {
                        //show toast to set the sms settings
                        Toast.makeText(getApplicationContext(), "SMS alerts turned OFF. Goto Settings to turn ON", Toast.LENGTH_LONG).show();
                    }

                    //show main page.
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    myIntent.putExtra("userName", userName);
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
        //compare encrypted password with database data
        String psw = ((EditText) findViewById(R.id.txtPass)).getText().toString().trim();
        ProductCipher ps = new ProductCipher();
        String encPsw = ps.Encrypt("watch my baby username " + userName, psw);
        if (user == null) {
            return false;
        } else if (user.getPassword().equals(encPsw)) {
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

    //show new user create page
    public void signIn(View view) {
        Intent myIntent = new Intent(LoginActivity.this, CreateNewUserActivity.class);
        startActivity(myIntent);
    }
}

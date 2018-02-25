package com.example.chathus.watchmybaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import utill.User;
import utill.Validate;

public class CreateNewUserActivity extends AppCompatActivity {

    private static DatabaseReference ref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
    }

    public void create(View view) {

        //make User object
        String userName = ((EditText) findViewById(R.id.txtUserName)).getText().toString();
        String name = ((EditText) findViewById(R.id.txtName)).getText().toString();
        String password = ((EditText) findViewById(R.id.txtPass)).getText().toString();
        String retypedPassword = ((EditText) findViewById(R.id.txtConfirmPass)).getText().toString();
        String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
        int tel = Integer.parseInt(((EditText) findViewById(R.id.txtPhone)).getText().toString());

        //validate user details
        Validate validation = new Validate();

        if (validation.isUsernameValid(userName)) {
            if (validation.isEmailVaid(email)) {
                if (validation.isTelValid(tel)) {
                    if (validation.isPswMatch(password, retypedPassword)) {
                        //create a new User object
                        User user = new User(name, userName, password, email, tel);

                        // get database instance and slot
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(userName);

                        //write User object to the database
                        myRef.setValue(user);
                    } else {
                        showToast("Password confirmation doesn't match.");
                        ((EditText) findViewById(R.id.txtConfirmPass)).setText("");
                        findViewById(R.id.txtConfirmPass).requestFocus();
                    }
                } else {
                    showToast("Please enter a valid Tel Number.");
                    ((EditText) findViewById(R.id.txtPhone)).setText("");
                    findViewById(R.id.txtPhone).requestFocus();
                }
            } else {
                showToast("Please enter a valid email.");
                ((EditText) findViewById(R.id.txtEmail)).setText("");
                findViewById(R.id.txtEmail).requestFocus();
            }
        } else {
            showToast("The username is taken. Try another one.");
            ((EditText) findViewById(R.id.txtUserName)).setText("");
            findViewById(R.id.txtUserName).requestFocus();
        }
    }

    public static DatabaseReference getRef() {
        return ref;
    }

    public void showToast(String msg) {
        // show a toast to user for short time period
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
        toast.show();
    }
}
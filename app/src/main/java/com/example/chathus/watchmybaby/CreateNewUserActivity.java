package com.example.chathus.watchmybaby;

import android.content.Intent;
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

import utill.ProductCipher;
import utill.User;
import utill.Validate;

public class CreateNewUserActivity extends AppCompatActivity {

    private EditText txtUserName;
    private EditText txtName;
    private EditText txtPass;
    private EditText txtConfirmPass;
    private EditText txtEmail;
    private EditText txtPhone;
    private Validate validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtName = (EditText) findViewById(R.id.txtName);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtConfirmPass = (EditText) findViewById(R.id.txtConfirmPass);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
    }

    public void create(View view) {

        //collect data to create the user object
        final String userName = txtUserName.getText().toString().trim();
        final String name = txtName.getText().toString().trim();
        final String password = txtPass.getText().toString().trim();
        final String retypedPassword = txtConfirmPass.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String telStr = txtPhone.getText().toString().trim();

        //encrypt some text using password.
        ProductCipher ps = new ProductCipher();
        final String encPsw = ps.Encrypt("watch my baby username " + userName, password);

        //validate user details
        validation = new Validate();

        // get database instance and slot to check whether the user already exists
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(userName);
        if (userName.isEmpty()|| name.isEmpty() || password.isEmpty() || retypedPassword.isEmpty()|| email.isEmpty() || telStr.isEmpty()) {
            //show toast to fill all the fields
            showToast("Please fill all the details.");
        } else {

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Create User Object out of database data
                    User user = (User) dataSnapshot.getValue(User.class);

                    if (user == null) { //user name available to use
                        if (validation.isEmailVaid(email)) {
                            if (validation.isTelValid(Integer.parseInt(telStr))) {
                                if (validation.isPswMatch(password, retypedPassword)) {
                                    //create a new User object
                                    user = new User(name, userName, encPsw, email, Integer.parseInt(telStr));

                                    // get database instance and slot
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference(userName);

                                    //write User object to the database
                                    myRef.setValue(user);

                                    //show toast of succcess
                                    showToast("New user Created successfully");

                                    //clear components
                                    clearComponents();

                                    //guide user back
                                    Intent intent = new Intent(CreateNewUserActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                } else {
                                    showToast("Password confirmation doesn't match.");
                                    txtConfirmPass.setText("");
                                    txtConfirmPass.requestFocus();
                                }
                            } else {
                                showToast("Please enter a valid Tel Number.");
                                txtPhone.setText("");
                                txtPhone.requestFocus();
                            }
                        } else {
                            showToast("Please enter a valid email.");
                            txtEmail.setText("");
                            txtEmail.requestFocus();
                        }
                    } else {
                        showToast("The username is taken. Try another one.");
                        txtUserName.setText("");
                        txtUserName.requestFocus();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value, log details.
                }
            });
        }
    }

    private void showToast(String msg) {
        // show a toast to user for short time period
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
        toast.show();
    }

    private void clearComponents() {
        txtUserName.setText("");
        txtName.setText("");
        txtPass.setText("");
        txtConfirmPass.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
    }

}
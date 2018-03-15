package utill;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.chathus.watchmybaby.LoginActivity;
import com.example.chathus.watchmybaby.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static utill.WebRTC.dbHandler;
import static utill.WebRTC.userName;

/**
 * Created by chathuranga on 2/2/2018.
 */

public class Validate {


    public boolean validateUser(User user, String reTypePsw) {
        return isEmailVaid(user.getEmail()) && isPswMatch(user.getPassword(), reTypePsw) && isTelValid(user.getTel());
    }

    public boolean isTelValid(int tel) {
        String teltxt = String.valueOf(tel);
        return (teltxt.length() == 9);
    }

    public boolean isPswMatch(String psw, String reTypePsw) {
        return (psw.equals(reTypePsw));
    }

    public boolean isEmailVaid(String email) {
        return email.contains("@");
    }

}
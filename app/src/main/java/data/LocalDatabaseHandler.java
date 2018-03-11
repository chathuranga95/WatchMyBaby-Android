package data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by chathuranga on 3/11/2018.
 */

public class LocalDatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "databaseInfo";

    //version number to upgrade database version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "150600L.db";

    public LocalDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createTableLogin = "CREATE TABLE login (userName text primary key, logged text)";
            String createTableNotifications = "CREATE TABLE notifications (username text, date text, time text, msg text)";
            db.execSQL(createTableLogin);
            db.execSQL(createTableNotifications);
            Log.d(TAG, "Table created");

        } catch (SQLiteException ee) {
            Log.d(TAG, "Table exists already");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed, all data will be gone!!!
        try {
            //db.execSQL("DROP TABLE IF EXISTS login");
        } catch (SQLiteException ee) {
            //do nothing.
            Log.d(TAG, "SQLite database upgrade failed");
        }
        // Create tables again
        onCreate(db);
    }

    public String getLoggedUser() {
        String loggedUser = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from login", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
//                    Log.d(TAG, cursor.getString(0) + "  " + cursor.getString(1));
                    if (cursor.getString(1).equals("true")) {
                        loggedUser = cursor.getString(0);
                    }
                    cursor.moveToNext();
                }
            }
        } catch (SQLiteException ee) {
            Log.d(TAG, "sql exception occured");
        }
        return loggedUser;
    }

    public void saveLogin(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("delete from login");
            db.execSQL("insert into login values('" + userName + "','true')");
            Log.d(TAG, "saved logged user in db");
        } catch (SQLiteException ex) {
            Log.d(TAG, "error saving user in db");
        }
    }

    public void saveLogout(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("delete from login");
            db.execSQL("insert into login values('" + userName + "','false')");
            Log.d(TAG, "saved user logout in db");
        } catch (SQLiteException ex) {
            Log.d(TAG, "error saving user logout in db");
        }
    }

    public void saveNewNotification(String userName, String date, String time, String msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("insert into notifications values('" + userName + "','" + date + "','" + time + "','" + msg + "')");
            Log.d(TAG, "saved notification" + msg);
        } catch (SQLiteException ex) {
            Log.d(TAG, "error saving noti: " + msg + " " + ex.toString());
        }
    }

    public ArrayList<String> retrieveNotifications(String userName) {
        ArrayList<String> noti = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String compositeNoti;
        try {
            Cursor cursor = db.rawQuery("select * from notifications where userName = '" + userName + "' order by date,time limit 10", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    compositeNoti = cursor.getString(1) + ", " + cursor.getString(2) + "  : " + cursor.getString(3);
                    noti.add(compositeNoti);
                    cursor.moveToNext();
                }
            }
        } catch (SQLiteException ee) {
            Log.d(TAG, "sql exception occured" + ee.toString());
        }
        return noti;
    }

    public void clearNotifications(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("delete from notifications where userName = '" + userName + "'");
            Log.d(TAG, "cleared the notifications from db");
        } catch (SQLiteException ex) {
            Log.d(TAG, "error clearing notifications from db");
        }
    }

}
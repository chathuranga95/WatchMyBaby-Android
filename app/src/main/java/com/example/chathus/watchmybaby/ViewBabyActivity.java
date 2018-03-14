package com.example.chathus.watchmybaby;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//Import the WebView and WebViewClient classes//
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ViewBabyActivity extends Activity {
    WebView myWebView;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_baby);

        //retrieve username
        userName = getIntent().getStringExtra("userName");

        //Get a reference to the WebView
        myWebView = (WebView) findViewById(R.id.webview);

        //Obtain the WebSettings object
        WebSettings webSettings = myWebView.getSettings();

        //javascript enable
        webSettings.setJavaScriptEnabled(true);

        // Grant permissions for cam
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            final String TAG = "camaccess";

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                ViewBabyActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        Log.d(TAG, request.getOrigin().toString());
                        request.grant(request.getResources());
                        Log.d(TAG, "CAM ACCESS GRANTED");
                        myWebView.loadUrl("javascript:clearInfoDiv()");
                        Log.d(TAG, "connecting.. text cleared.");
                    }
                });
            }
        });
        myWebView.loadUrl("https://watchmybaby-52d18.firebaseapp.com/videoCallView.html");
        final String uname = "watchMyBabyMobile" + userName;
        final String callName = "watchMyBabyWeb" + userName;

        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                myWebView.loadUrl("javascript:initiateCall('" + uname + "', '" + callName + "');");
            }
        });
    }

    public void changeColor(View view) {
//        myWebView.loadUrl("javascript:changeBackgroundColor()");
        String color = "red";
        myWebView.loadUrl("javascript:changeBackgroundColor1('" + color + "')");
        Log.d("changeColor", "Color change request");
    }

    public void hangup(View view) {
        try {
            myWebView.loadUrl("javascript:hangUpCall()");
        }
        catch (Exception ex){

        }
        Log.d("videoCallHangup", "Call disconnected...");
        //show main page.
        Intent myIntent = new Intent(ViewBabyActivity.this, MainActivity.class);
        startActivity(myIntent);
    }
}
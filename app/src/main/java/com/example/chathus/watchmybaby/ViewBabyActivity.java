package com.example.chathus.watchmybaby;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageButton;


public class ViewBabyActivity extends Activity {
    WebView myWebView;
    String userName = "";
    ImageButton btnHangup;

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

        ////Get a reference to the hangup button
        btnHangup = findViewById(R.id.btnHangup);

        //set onclick listerner
        btnHangup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hangupGoBack(v);
            }
        });

        //Obtain the WebSettings object
        WebSettings webSettings = myWebView.getSettings();

        //javascript enable
        webSettings.setJavaScriptEnabled(true);

        final String uname = "watchMyBabyMobile" + userName;
        final String callName = "watchMyBabyWeb" + userName;

        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                myWebView.loadUrl("javascript:initiateCall('" + uname + "', '" + callName + "');");
            }

            public void onReceivedError(WebView localWebView, int errorCode, String description, String failingUrl) {
                localWebView.loadUrl("file:///android_asset/error.html");
            }
        });

        // Grant permissions for cam
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
//        myWebView.clearCache(true);
        myWebView.loadUrl("about:blank");
        myWebView.loadUrl("https://watchmybaby-52d18.firebaseapp.com/videoCallView.html");

//        myWebView.setKeepScreenOn(true);
//        myWebView.setHorizontalScrollBarEnabled(false);
//        myWebView.setVerticalScrollBarEnabled(false);
    }

    public void changeColor(View view) {
//        myWebView.loadUrl("javascript:changeBackgroundColor()");
        String color = "red";
        myWebView.loadUrl("javascript:changeBackgroundColor1('" + color + "')");
        Log.d("changeColor", "Color change request");
    }

    public void hangupGoBack(View view) {
        try {
//            myWebView.loadUrl("javascript:disconnectCall()");
            myWebView.loadUrl("file:///android_asset/disconnect.html");
            Log.d("videoCallHangup", "Call disconnected...");
        } catch (Exception ex) {
            Log.d("videoCallHangup", "error 1");
            Log.d("videoCallHangup", ex.toString());
        }
        try{
            //show main page.
            Intent myIntent = new Intent(ViewBabyActivity.this, MainActivity.class);
            startActivity(myIntent);

        }
        catch (Exception ee){
            Log.d("videoCallHangup", "error 2");
            Log.d("videoCallHangup", ee.toString());
        }
    }
}
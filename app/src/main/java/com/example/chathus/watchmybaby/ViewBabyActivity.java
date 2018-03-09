package com.example.chathus.watchmybaby;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//Import the WebView and WebViewClient classes//
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ViewBabyActivity extends AppCompatActivity {
    WebView myWebView;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    }
                });
            }
        });
        myWebView.loadUrl("https://watchmybaby-52d18.firebaseapp.com/videoCallView.html");
        final String uname = "watchMyBabyMobile" + userName;
        final String callName = "watchMyBabyWeb" + userName;

        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                myWebView.loadUrl("javascript:initiateCall('"+uname+"', '"+callName+"');");
            }
        });

    }

    public void changeColor(View view) {
//        myWebView.loadUrl("javascript:changeBackgroundColor()");
        String color = "red";
        myWebView.loadUrl("javascript:changeBackgroundColor1('"+color+"')");
        Log.d("changeColor", "Color change request");
    }
}
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_baby);
        //Get a reference to your WebView//
        myWebView = (WebView) findViewById(R.id.webview);

        //Specify the URL you want to display

//        webView.loadUrl("http://beta.html5test.com/");
//        webView.loadUrl("192.168.8.101:8000/index.html");

        //Obtain the WebSettings object
        WebSettings webSettings = myWebView.getSettings();

        //javascript enable
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            final String TAG = "camaccess";

            // Grant permissions for cam
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
        myWebView.loadUrl("https://watchmybaby-52d18.firebaseapp.com/");
//        myWebView.loadUrl("https://www.google.lk");
//        myWebView.loadUrl("javascript:initiateCall(\"chathuMobia\", \"Lapchathu\")");
        myWebView.loadUrl("javascript:initiateCall1()");
    }

    public void changeColor(View view) {
        myWebView.loadUrl("javascript:changeBackgroundColor()");
        Log.d("changeColor", "Color change request");
    }
}
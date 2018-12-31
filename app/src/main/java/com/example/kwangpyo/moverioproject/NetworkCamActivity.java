package com.example.kwangpyo.moverioproject;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

public class NetworkCamActivity extends Activity {

    private FrameLayout mTargetView;
    private FrameLayout mContentView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private View mCustomView;

    private TextView back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_cam);

        mContentView = (FrameLayout) findViewById(R.id.main_content);
        mTargetView = (FrameLayout)findViewById(R.id.target_view);
        back_btn = (TextView) findViewById(R.id.Net_back_btn);


        back_btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {


                onBackPressed();

            }
        });







        WebView mWebView = (WebView) findViewById(R.id.webView1);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                mCustomViewCallback = callback;
                mTargetView.addView(view);
                mCustomView = view;
                mContentView.setVisibility(View.GONE);
                mTargetView.setVisibility(View.VISIBLE);
                mTargetView.bringToFront();
            }

            @Override
            public void onHideCustomView() {
                if (mCustomView == null)
                    return;

                mCustomView.setVisibility(View.GONE);
                mTargetView.removeView(mCustomView);
                mCustomView = null;
                mTargetView.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();
                mContentView.setVisibility(View.VISIBLE);
            }


        });
        mWebView.loadUrl("http://dev.frontis.co.kr:1111/videostream.cgi?user=admin&pwd=888888");


    }








}







package com.example.usersmanagement.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.usersmanagement.R;

public class CustomWebView extends WebView {

    private ProgressDialog progressDialog;
    private Context context;
    private boolean hasError = false;


    public CustomWebView(Context context) {
        super(context);
        this.context = context;
        initDefaultSetting();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initDefaultSetting();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initDefaultSetting();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initDefaultSetting();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        this.context = context;
        initDefaultSetting();
    }

    private void initDefaultSetting() {
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new MyWebViewClient());
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    /**
     * Load Web View with url
     */
    public void load(String url) {
        this.loadUrl(url);
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(hasError) {
                return;
            }
            if(progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            hasError = true;
            if(progressDialog != null) {
                progressDialog.dismiss();
            }
            Toast.makeText(context, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
        }
    }
}

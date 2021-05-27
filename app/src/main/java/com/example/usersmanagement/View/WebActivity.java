package com.example.usersmanagement.View;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.usersmanagement.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webview);
        showWebView();
    }

    private void showWebView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String url = bundle.getString("url");
            if (!TextUtils.isEmpty(url)) {
                webView.loadUrl(url);
            } else {
                Toast.makeText(this, getResources().getString(R.string.error_url), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

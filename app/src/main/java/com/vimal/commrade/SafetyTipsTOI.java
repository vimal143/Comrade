package com.vimal.commrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SafetyTipsTOI extends AppCompatActivity {
    WebView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_tips_t_o_i);
        tips=findViewById(R.id.webView);
        tips.setWebViewClient(new WebViewClient());
        tips.loadUrl("https://timesofindia.indiatimes.com/blogs/the-next-step/safety-tips-for-women/");
        WebSettings webSettings=tips.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(SafetyTipsTOI.this,MainActivity.class);
        startActivity(i);
        finish();

    }
}
package com.spoof.lm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.spoof.MyMethods.Methods;

import java.util.Objects;

public class WebActivity extends AppCompatActivity {

    private WebView web;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_web);
        web=findViewById(R.id.web_view_f_web_view);
        progressBar=findViewById(R.id.progressBar_f_web);
        GetIntend();
    }
    private void GetIntend()
    {
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        loadUrl(url);
    }
    private void loadUrl(String url)
    {
        web.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        web.getProgress();
        web.loadUrl(url);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Methods.status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Methods.status("Offline");
    }
}
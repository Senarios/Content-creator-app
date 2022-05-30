package com.senarios.coneqtlive.stripe;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.Settings.SettingsActivity;


public class CreateStripeAccountActivity extends AppCompatActivity {
    private WebView webView;
    private String WebLink;
    private String returnUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stripe_account);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        WebLink = getIntent().getStringExtra("url");
        returnUrl = getIntent().getStringExtra("returnUrl");
        webView.loadUrl(WebLink);

        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Assuming you are giving link to some PDF file.
                if (url.contains(returnUrl)) {
                    // Now do what you want to with the url here
                    startActivity(new Intent(CreateStripeAccountActivity.this, SettingsActivity.class));
                    finish();
                }

                return true;
            }
        });
    }
}
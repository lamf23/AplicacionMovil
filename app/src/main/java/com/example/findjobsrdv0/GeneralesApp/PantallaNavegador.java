package com.example.findjobsrdv0.GeneralesApp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.findjobsrdv0.R;

public class PantallaNavegador extends AppCompatActivity {
    private WebView webView;
    private String probandopage="www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_navegador);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        webView =(WebView) findViewById(R.id.navegador);
        webView.setWebViewClient(new WebViewClient());

        if (bundle != null) {
            if (!probandopage.isEmpty()) {
                probandopage = getIntent().getStringExtra("sPaginaWebDE");
                webView.loadUrl("https://"+probandopage);
            }
        }
//despues tengo que validar la url
        webView.loadUrl("https://"+probandopage);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

}

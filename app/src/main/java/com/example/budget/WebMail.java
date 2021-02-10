package com.example.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebMail extends AppCompatActivity {
    public WebView wb;
    public WebSettings ws;
    public Button tologin;
    public String email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        initviews();
        try {
            email = getIntent().getStringExtra("email");
        }
        catch (Exception e){}

        ws = wb.getSettings();
        ws.setJavaScriptEnabled(true);
        wb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        wb.loadUrl("https://mail.google.com/mail/u/0/#inbox"); //the pay link

        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WebMail.this, MainActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
    }
    public void initviews(){
        wb = findViewById(R.id.webviewmercado);
        tologin = findViewById(R.id.btnLogin);
    }
}

package com.site.mobile.mobilesite;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Если происходит загрузка сайта, который нужен нам, то все нормально
                if (Uri.parse(getString(R.string.load_url)).getHost().equals(Uri.parse(url).getHost())) return false;

                //Иначе запрашиваем, чтобы система открыла эту ссылку в браузере
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
                return true;
            }

            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE); //Делаем крутилку загрузки видимой
            }

            @Override
            public void onPageFinished (WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE); //Делаем крутилку загрузки невидимой
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //При возникновении ошибки, выводим сообщение
                Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                result.confirm();
                return true;
            }
        });

        //Менять ссылку в папке res/values/strings.xml
        //Для быстрого перехода можно нажать ctrl + ЛКМ
        webView.loadUrl(getString(R.string.load_url));
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}


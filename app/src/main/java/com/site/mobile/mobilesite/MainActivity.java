package com.site.mobile.mobilesite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private TextView tvError;
    private ImageButton ibRefresh;
    private String loadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvError = findViewById(R.id.tvError);
        ibRefresh = findViewById(R.id.tvRefresh);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webview);

        loadUrl = getString(R.string.load_url);

        //Если активити открывается после push уведомления, загружаем ссылку, которая пришла в уведомлении
        String url = getIntent().getStringExtra("url");
        if(url!=null) {
            loadUrl = url;
        }

        initWebView();  //Инициализация webView
        setListeners(); //Добавляем слушателя на нажатие кнопки
        hideTextAndButton(); //Скрываем текст с ошибкой и кнопку

        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    //Если активити уже было открыто, вызывается этот метод
    @Override
    protected void onNewIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        if(url!=null) {
            loadUrl = url;
        }
        initWebView();
        super.onNewIntent(intent);
    }

    private void initWebView() {
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
                if (webView.getVisibility() == View.INVISIBLE) webView.setVisibility(View.VISIBLE);
                hideTextAndButton();
            }

            @Override
            public void onPageFinished (WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE); //Делаем крутилку загрузки невидимой
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //При возникновении ошибки, выводим сообщение
                Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
                tvError.setText(description); // Сюда пиши ошибку
                showTextAndButton();
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
        webView.loadUrl(loadUrl);

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void showTextAndButton() {
        tvError.setVisibility(View.VISIBLE);
        ibRefresh.setVisibility(View.VISIBLE);

        webView.setVisibility(View.INVISIBLE);
    }

    private void hideTextAndButton() {
        tvError.setVisibility(View.GONE);
        ibRefresh.setVisibility(View.GONE);
    }

    private void setListeners() {
        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });
    }
}


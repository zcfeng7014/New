package com.cfeng.anew;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);
        String new_url=intent.getStringExtra("url");
        wv = (WebView) findViewById(R.id.webview);
        TextView tv= (TextView) findViewById( R.id.title);
        tv.setText(title);
//
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                wv=view;
//                view.getSettings().setJavaScriptEnabled(true);
//                view.loadUrl(url);
//
//                return true;
//            }
//        });
        wv.loadDataWithBaseURL(null,new_url,"text/html","utf-8",null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
           if(wv.canGoBack()){
               wv.goBack();
           }
           else
               finish();
        }
        return true;
    }
}

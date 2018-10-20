package com.zbmf.StocksMatch.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;

public class IssueActivity extends ExActivity {

    private WebView webview;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        String pay = (String) getIntent().getSerializableExtra("pay");
        webview = (WebView) findViewById(R.id.webview);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(R.string.issue);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if("pay".equals(pay)){
            tv_title.setText(R.string.shengmi);
        }


        webview.loadUrl("http://www.7878.com/apps/contact/");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //设置webview自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showDialog(IssueActivity.this, R.string.loading);
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                DialogDismiss();
                super.onPageFinished(view, url);
            }
        });

    }
}

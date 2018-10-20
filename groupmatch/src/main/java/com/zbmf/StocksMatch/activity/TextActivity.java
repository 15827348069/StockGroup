package com.zbmf.StocksMatch.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.utils.UiCommon;

public class TextActivity extends ExActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();


        String str = getIntent().getStringExtra("web_url");
        String title = getIntent().getStringExtra("title");
        Log.e("tag",str);
        final TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                deleteDatabase("webview.db");
                deleteDatabase("webviewCache.db");
            }
        });
        /*TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(Html.fromHtml(str));
        tv.setMovementMethod(LinkMovementMethod.getInstance());*/

        WebView webView = (WebView)findViewById(R.id.webview);
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar); // 进度条
        final Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.progress_bar_alpha); // 渐变动画
        mProgressBar.setMax(100);

        webView = (WebView) findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true); // 设置出现缩放工具
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setSupportZoom(true);// 设置是否支持缩放
        webView.getSettings().setUseWideViewPort(true);//扩大比例缩放
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    if (mProgressBar.getVisibility() == View.GONE)
                        mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(progress);
                } else {
                    mProgressBar.setProgress(100);
                    mProgressBar.startAnimation(alphaAnimation);
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                tv_title.setText(UiCommon.INSTANCE.subTitle(title));
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        webView.loadData(str, "text/html;charset=UTF-8",null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}

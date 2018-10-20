package com.zbmf.StocksMatch.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kwlstock.sdk.activity.KwlOpenActivity;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.utils.UiCommon;


/**
 * 内嵌网页
 *
 * @author atan
 */
public class WebActivity extends ExActivity {
    private int soure_act = -1;    //从哪里来
    private String web_url = "";    //内嵌网页地址
    private WebView webView;
    private String title = "";    //标题
    private TextView tvTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {//此参数必须要传，不得为空
            web_url = bundle.getString("web_url");
            if (bundle.containsKey("soure_act"))
                soure_act = bundle.getInt("soure_act");
            if (bundle.containsKey("title"))
                title = bundle.getString("title");
            if (bundle.containsKey("soure_act"))
                soure_act = bundle.getInt("soure_act");
        }
        addBtnEvent();
    }

    private void addBtnEvent() {
        ImageView btnLeft = (ImageView) findViewById(R.id.iv_back);
        if (soure_act == 9)
            btnLeft.setImageResource(R.drawable.icon_close);
        btnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(UiCommon.INSTANCE.subTitle(title));


        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar); // 进度条
        final Animation alphaAnimation = AnimationUtils.loadAnimation(WebActivity.this, R.anim.progress_bar_alpha); // 渐变动画
        mProgressBar.setMax(100);

        webView = (WebView) findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false); // 设置是否支持缩放
        webView.getSettings().setSupportZoom(true);
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
                if (2 == soure_act || soure_act == 4 || soure_act==9)
                    tvTitle.setText(UiCommon.INSTANCE.subTitle(title));
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("tag", "------------" + url);
                if (url.startsWith("app:match_create")) {
                    view.loadUrl("http://m.zbmf.cn/match/create/?user_id=" + UiCommon.INSTANCE.getiUser().getUser_id());
                } else if (url.startsWith("apps:match/1039/")) {
                    finish();
                }else if(url.endsWith(".apk")){
                    UiCommon.INSTANCE.openOther("com.zbmf.StockGroup000",getApplicationContext(),WebActivity.this,url);
                }else if(url.contains("app:jwl")){
                    Log.e(">>>>>",url+"....");
                    try{
                        String[] URLarray = url.split("-");
                        KwlOpenActivity.show(WebActivity.this,URLarray[1],"证券开户");
                    }catch(Exception e){
                        e.printStackTrace();
                        Log.e(">>>>",e.getMessage());
                    }
                    return true;
                }
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

            webView.loadUrl(web_url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if(soure_act==UiCommon.ACTIVITY_IDX_PAYMENT){
//    		WebActivity.this.finish();
//    		return true;
    	}else*/
        if (webView != null && webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return true;
        }/* else if (soure_act == 1 || soure_act==4) {
            finish();
            overridePendingTransition(0, R.anim.slide_out_down);
        }*/
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
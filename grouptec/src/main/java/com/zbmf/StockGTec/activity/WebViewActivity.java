package com.zbmf.StockGTec.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGTec.R;

public class WebViewActivity extends ExActivity {
    private WebView jianghu_webview;
    private String URL;
    private ImageButton web_view_back,web_view_close;
    private TextView web_title;
    private Dialog mTextSetteingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        URL=getIntent().getStringExtra("web_url");
        jianghu_webview= (WebView) findViewById(R.id.jianghu_webview);
        final WebSettings settings=jianghu_webview.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        jianghu_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        jianghu_webview.loadUrl(URL);
        jianghu_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String[] web_url = url.split("/");
                if(url.startsWith("mqqwpa://")){
                    //跳转到客服qq
                    String qq_url="mqqwpa://im/chat?chat_type=crm&uin=2852273339&version=1&src_type=web&web_src=http:://wpa.b.qq.com";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
                }else if(web_url.length==4&&url.startsWith("app://group/")){

                    //进入圈子
                }else if(web_url.length==7&&url.startsWith("app://app/people/")){

                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        });
        web_title= (TextView) findViewById(R.id.web_title);
        web_title.setText(getIntent().getStringExtra("web_title"));
        web_view_back= (ImageButton) findViewById(R.id.web_view_back);
        web_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                if(jianghu_webview.canGoBack())
//                {
//                    jianghu_webview.goBack();//返回上一页面
//                }
//                else
//                {
//                    finish();
//                }
            }
        });
//        web_share_id.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mTextSetteingDialog == null)
//                    mTextSetteingDialog = showSetText(settings);
//                mTextSetteingDialog.show();
//            }
//        });
        web_view_close= (ImageButton) findViewById(R.id.web_view_close);
        web_view_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        jianghu_webview.setWebChromeClient(new WebChromeClient(){
            //设置加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
            //设置头部
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                web_title.setText(title);
            }
        });
        jianghu_webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.swiperefresh_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                jianghu_webview.reload();
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(jianghu_webview.canGoBack())
            {
                jianghu_webview.goBack();//返回上一页面
                return true;
            }
            else
            {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}

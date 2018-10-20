package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.StocksMatch.bean.BlogBean;
import com.zbmf.StocksMatch.bean.Group;
import com.zbmf.StocksMatch.bean.Stock;
import com.zbmf.StocksMatch.bean.Video;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.listener.IWebView;
import com.zbmf.StocksMatch.listener.OnUrlClick;
import com.zbmf.StocksMatch.presenter.WebViewPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowWebShareLayout;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.StocksMatch.webclient.GroupWebViewClient;
import com.zbmf.worklibrary.util.Logx;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

import butterknife.BindView;

/**
 * Created by xuhao
 * on 2016/6/20.
 */
public class WebViewActivity extends BaseActivity<WebViewPresenter> implements IWebView, View.OnClickListener, OnUrlClick {
    @BindView(R.id.jianghu_webview)
    WebView jianghu_webview;
    @BindView(R.id.swiperefresh_id)
    SwipeRefreshLayout swiperefresh_id;
    @BindView(R.id.loadingImageView)
    ProgressBar loadingImageView;
    @BindView(R.id.id_tv_loadingmsg)
    TextView id_tv_loadingmsg;
    @BindView(R.id.dialog_layout)
    LinearLayout dialog_layout;

    private boolean isFinish;
    private ShowWebShareLayout showWebShareLayout;
    private String mUrl;
    private String mWebTitle;
    private CookieManager mCookieManager;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (showWebShareLayout == null) {
            showWebShareLayout = new ShowWebShareLayout(this);
        }
        showWebShareLayout.onNewIntent(intent);
    }

    /**
     * 设置Cookie
     *
     * @param cookie 格式：uid=21233 如需设置多个，需要多次调用
     */
    private void synCookies(String url, String cookie) {
        Logx.e(url + "设置cookies:" + cookie);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);//指定要修改的cookies
        CookieSyncManager.getInstance().sync();//调用该方法会立即执行Cookie的同步
    }

    private void remoCookies() {
        CookieSyncManager.createInstance(this);
        mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        if (!isGoBack) {
            mCookieManager.removeSessionCookie();//移除,如果移除Cookie,返回上一页时上一页内容会清空(因为Cookie被清除了)
        }
        CookieSyncManager.getInstance().sync();
    }

    private boolean isGoBack = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (jianghu_webview.canGoBack()) {
                jianghu_webview.goBack();//返回上一页面
                isGoBack = true;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int getLayout() {
        return R.layout.webview_layout;
    }

    @Override
    protected String initTitle() {
        return TextUtils.isEmpty(mWebTitle) ? ""/*getString(R.string.run_tactics)*/ : mWebTitle;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        ShowOrHideProgressDialog.showProgressDialog(this, this, getString(R.string.hard_loading));
        ShowOrHideProgressDialog.setDialogCancel(false);
        if (bundle != null) {
            mUrl = bundle.getString(IntentKey.WEB_URL);
            mWebTitle = bundle.getString("web_title");
        }
        if (showWebShareLayout == null) {
            showWebShareLayout = new ShowWebShareLayout(this);
        }
        jianghu_webview = (WebView) findViewById(R.id.jianghu_webview);
        final WebSettings settings = jianghu_webview.getSettings();
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
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        jianghu_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        String agent = settings.getUserAgentString();
        settings.setUserAgentString(agent.replace("Android", "runtraderapp"));

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                jianghu_webview.reload();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        jianghu_webview.setWebChromeClient(new WebChromeClient() {
            //设置加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    DissLoading();
                    ShowOrHideProgressDialog.disMissProgressDialog();
                } else {
//                    ShowOrHideProgressDialog.showProgressDialog(WebViewActivity.this,
//                            WebViewActivity.this, getString(R.string.hard_loading));
//                    initProgressDialog();
//                    setProgressDialogMsg(getString(R.string.hard_loading));
////                    ShowLoading();
//                    setCancelDialog(false);
                }
            }

            //设置头部
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mWebTitle = title;
            }
        });
        jianghu_webview.setWebViewClient(new GroupWebViewClient().setUrlClick(this));
        jianghu_webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    @Override
    protected WebViewPresenter initPresent() {
        WebViewPresenter webViewPresenter = new WebViewPresenter(this, mUrl);
        isFinish = true;
        return webViewPresenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCookieManager!=null){
            mCookieManager.removeSessionCookie();
            mCookieManager = null;
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onGroup(Group group) {
    }


    @Override
    public void onBolg(BlogBean blogBean) {
    }

    @Override
    public void onStock(Stock stock) {
    }

    @Override
    public void onVideo(Video video) {
    }

    @Override
    public void onWeb(String url) {
        isFinish = false;
        remoCookies();
        isGoBack = true;
        synCookies(url, "token=" + SharedpreferencesUtil.getInstance().getString(SharedKey.AUTH_TOKEN, ""));
        synCookies(url, "api_key=" + HostUrl.API_KEY);
        jianghu_webview.loadUrl(url);
    }

    @Override
    public void onImage(String url) {
    }

    @Override
    public void onPay() {
    }

    @Override
    public void loadResult(int gainStatus) {
        ShowOrHideProgressDialog.disMissProgressDialog();
    }
}

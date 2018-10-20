package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.AppUrl;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.beans.ShareBean;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.AppConfig;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.interfaces.OnUrlClick;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.ShowWebShareLayout;
import com.zbmf.StockGroup.utils.WebClickUitl;
import com.zbmf.StockGroup.webclient.GroupWebViewClient;

/**
 * Created by xuhao
 * on 2016/6/20.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener, OnUrlClick {
    private WebView jianghu_webview;
    private ImageButton imbShare;
    private String share_title;
    private boolean isFinish;
    private ShowWebShareLayout showWebShareLayout;
    @Override
    public int getLayoutResId() {
        return R.layout.webview_layout;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(showWebShareLayout==null){
            showWebShareLayout=new ShowWebShareLayout(this);
        }
        showWebShareLayout.onNewIntent(intent);
    }

    @Override
    public void initView() {
        initTitle(getIntent().getStringExtra("web_title"));
        if(showWebShareLayout==null){
            showWebShareLayout=new ShowWebShareLayout(this);
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
        settings.setUserAgentString(agent.replace("Android", "StockGroup"));
        jianghu_webview.addJavascriptInterface(new JsOperation(), "StockGroup");
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
        imbShare = getView(R.id.group_tiitle_share);
        imbShare.setVisibility(View.VISIBLE);
        imbShare.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String url = getIntent().getStringExtra(IntentKey.WEB_URL);
        UriClick(url);
    }

    @Override
    public void addListener() {
        jianghu_webview.setWebChromeClient(new WebChromeClient() {
            //设置加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    dialogDiss();
                } else {
                    dialogShow();
                }
            }

            //设置头部
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                share_title = title;
                initTitle(title);
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

    private void UriClick(String url) {
        isFinish=true;
        WebClickUitl.UrlClick(this, url);
    }
    /**
     * 设置Cookie
     * @param cookie  格式：uid=21233 如需设置多个，需要多次调用
     */
    private void synCookies(String url,String cookie) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);//指定要修改的cookies
        CookieSyncManager.getInstance().sync();
    }
    private void remoCookies() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        CookieSyncManager.getInstance().sync();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (jianghu_webview.canGoBack()) {
                jianghu_webview.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toVideo(final Video video) {
        if (video.getIs_live()) {
            DWLive.getInstance().setDWLiveLoginParams(new DWLiveLoginListener() {
                @Override
                public void onLogin(TemplateInfo templateInfo, Viewer viewer, RoomInfo roomInfo, PublishInfo publishInfo) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
                            ShowActivity.showActivityForResult(WebViewActivity.this, bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                        }
                    });
                }

                @Override
                public void onException(final DWLiveException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
                            ShowActivity.showActivityForResult(WebViewActivity.this, bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                        }
                    });
                }
            }, Constants.CC_USERID, video.getBokecc_id() + "", SettingDefaultsManager.getInstance().NickName(), "");
            DWLive.getInstance().startLogin();
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
            ShowActivity.showActivity(WebViewActivity.this, bundle, VideoPlayActivity.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_tiitle_share:
                showWebShareLayout.showShareLayout(new ShareBean(share_title != null ? share_title : getString(R.string.app_name), AppConfig.SHARE_LOGO_IMG, jianghu_webview.getUrl()));
                break;
        }
    }

    @Override
    public void onGroup(Group group) {
        ShowActivity.showGroupDetailActivity(WebViewActivity.this, group);
        if(isFinish){
            finish();
        }
    }

    @Override
    public void onBolg(BlogBean blogBean) {
        ShowActivity.showBlogDetailActivity(WebViewActivity.this, blogBean);
        if(isFinish){
            finish();
        }
    }

    @Override
    public void onStock(Stock stock) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentKey.STOCKHOLDER,stock);
        ShowActivity.showActivity(this,bundle, SimulateOneStockCommitActivity.class);
    }

    @Override
    public void onScreen() {
        ShowActivity.showActivity(this,ScreenActivity.class);
    }

    @Override
    public void onScreenDetail(Screen screen) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentKey.SCREEN,screen);
        ShowActivity.showActivity(this,bundle,ScreenDetailActivity.class);
    }

    @Override
    public void onModeStock() {
        ShowActivity.showActivity(this,StockModeActivity.class);
    }

    @Override
    public void onDongAsk() {
        ShowActivity.showActivity(this,DongAskActivity.class);
    }

    @Override
    public void onQQ(String url) {
        ShowActivity.showQQ(this);
    }

    @Override
    public void onVideo(Video video) {
        toVideo(video);
        if(isFinish){
            finish();
        }
    }

    @Override
    public void onWeb(String url) {
        if(url.startsWith("http://a.app.qq.com")){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
            finish();
        }else{
            isFinish=false;
            remoCookies();
            synCookies(url,"token="+SettingDefaultsManager.getInstance().authToken());
            synCookies(url,"api_key="+ AppUrl.API_KEY);
            jianghu_webview.loadUrl(url);
        }
    }

    @Override
    public void onImage(String url) {
        ShowActivity.ShowBigImage(WebViewActivity.this, url);
        if(isFinish){
            finish();
        }
    }

    @Override
    public void onPay() {
        ShowActivity.showPayDetailActivity(WebViewActivity.this);
        if(isFinish){
            finish();
        }
    }
    public class JsOperation {
        @JavascriptInterface
        public void login() {
            ShowActivity.showActivity(WebViewActivity.this,LoginActivity.class);
        }
    }

}

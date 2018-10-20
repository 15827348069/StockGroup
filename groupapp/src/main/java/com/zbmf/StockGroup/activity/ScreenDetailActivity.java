package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.AppUrl;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.dialog.BuyTipDialog;
import com.zbmf.StockGroup.listener.ClickAgreeRiskListener;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.UTFUtils;

import org.json.JSONObject;

/**
 * Created by xuhao
 * on 2016/6/20.
 */
public class ScreenDetailActivity extends BaseActivity implements View.OnClickListener ,ClickAgreeRiskListener {
    private WebView jianghu_webview;
    private TextView tv_screen_date,tv_screen_commit,tv_screen_commit1,tv_screen_price,tv_screen_price_mfb;
    private Button group_title_right_button;
    private Bundle bundle;
    private String screen_id;
    private Screen screen;

    private LinearLayout ll_date_layout,ll_price_layout,rl_bottom_id;
    private boolean setTitle;
    private BuyTipDialog mBuyTipDialog;
    private int mIntFlag;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_screem_webview_layout;
    }


    @Override
    public void initView() {
        jianghu_webview = getView(R.id.jianghu_webview);
        tv_screen_date=getView(R.id.tv_screen_date);
        tv_screen_commit=getView(R.id.tv_screen_commit);
        tv_screen_commit1=getView(R.id.tv_screen_commit1);
        tv_screen_price=getView(R.id.tv_screen_price);
        tv_screen_price_mfb=getView(R.id.tv_screen_price_mfb);
        group_title_right_button=getView(R.id.group_title_right_button);
        group_title_right_button.setText("产品简介");

        ll_date_layout=getView(R.id.ll_date_layout);
        ll_price_layout=getView(R.id.ll_price_layout);
        rl_bottom_id=getView(R.id.rl_bottom_id);
        final SwipeRefreshLayout swipeRefreshLayout = getView(R.id.swiperefresh_id);
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
        setWebSetting();
        if (mBuyTipDialog==null){
            mBuyTipDialog = new BuyTipDialog(this,R.style.riskTipDialog);
        }
        mBuyTipDialog.createDialog().clickAgreeRiskBtn(this);
    }
    private void setWebSetting(){
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
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        jianghu_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        jianghu_webview.addJavascriptInterface(new JsOperation(), "group");
        jianghu_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(Constants.STOCK_FLAG_NAME)&&url.contains(Constants.STOCK_FLAG_SYMBOL)){
                    int i = url.indexOf("stock/")+6;
                    int i1 = url.indexOf("/name");
                    String name = url.substring(i, i1);
                    int i2 = url.indexOf("name/") + 5;
                    int i3 = url.indexOf("/symbol");
                    String symbol = url.substring(i2, i3);
                    String stockName = UTFUtils.jmUTF_8(name);
                    skipStockDetailActivity(new DealSys(stockName, symbol));
                    return true;
                }else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
    }
    private void skipStockDetailActivity(DealSys dealSys) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCKHOLDER, dealSys);
        ShowActivity.showActivity(this, bundle, StockDetailActivity.class);
    }
    @Override
    public void initData() {
        bundle=getIntent().getExtras();
        if (bundle!=null){
            screen = (Screen) bundle.getSerializable(IntentKey.SCREEN);
            mIntFlag = bundle.getInt(IntentKey.FLAG);
            if (screen.getScreen_id().equals("1004")){
                tv_screen_commit1.setVisibility(View.VISIBLE);
            }else{
                tv_screen_commit1.setVisibility(View.GONE);
            }
            int isVip = SettingDefaultsManager.getInstance().getIsVip();
            if (isVip==1){
                tv_screen_commit.setText(getString(R.string.preSubscribe));
            }else {
                tv_screen_commit.setText(getString(R.string.subscribe));
            }
        }
        if(screen!=null&&screen.getPrceList()!=null){
            setTitle=true;
            initTitle(screen.getName());
            setScreenMessage();
        }else if(screen!=null&&screen.getScreen_id()!=null){
            screen_id=screen.getScreen_id();
            getScreenMessage();
        }
    }
    private void setScreenMessage(){
        jianghu_webview.stopLoading();
        jianghu_webview.clearHistory();
        jianghu_webview.clearCache(true);
        String url;
        screen_id=screen.getScreen_id();
        if(screen.getOrder_status()){
            group_title_right_button.setVisibility(View.VISIBLE);
            group_title_right_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    group_title_right_button.setVisibility(View.GONE);
                    String url=HtmlUrl.SCREEN_DESC.replace("1000",screen_id);
                    synCookies(url,"token="+ SettingDefaultsManager.getInstance().authToken());
                    synCookies(url,"api_key="+ AppUrl.API_KEY);
                    jianghu_webview.loadUrl(url);
                    setTv_screen_date();
                }
            });
            url=HtmlUrl.SCREEN;
            tv_screen_commit.setText(getString(R.string.preSubscribe));
            if(screen.getM_price()==0){
                tv_screen_commit.setVisibility(View.GONE);
                tv_screen_commit1.setVisibility(View.GONE);
            }
        }else{
            url=HtmlUrl.SCREEN_DESC;
            group_title_right_button.setVisibility(View.GONE);
            tv_screen_commit.setText(getString(R.string.subscribe));
        }
        rl_bottom_id.setVisibility(screen.getIs_buy()?View.VISIBLE:View.GONE);
        setTv_screen_date();
        url=url.replace("1000",screen.getScreen_id());
        synCookies(url,"token="+ SettingDefaultsManager.getInstance().authToken());
        synCookies(url,"api_key="+ AppUrl.API_KEY);
        LogUtil.e(url);

        jianghu_webview.loadUrl(url);

    }
    private void setTv_screen_date(){
        if(group_title_right_button.getVisibility()==View.VISIBLE){
            ll_date_layout.setVisibility(View.VISIBLE);
            ll_price_layout.setVisibility(View.GONE);
            tv_screen_date.setText(DateUtil.getTime(screen.getExpire_at()*1000, Constants.YYYY年MM月dd日));
        }else{
            ll_date_layout.setVisibility(View.GONE);
            ll_price_layout.setVisibility(View.VISIBLE);
            if(screen.getM_price()==0){
                tv_screen_price.setText("限时免费");
                tv_screen_price_mfb.setVisibility(View.GONE);
            }else{
                tv_screen_price_mfb.setVisibility(View.VISIBLE);
                tv_screen_price.setText(DoubleFromat.getDouble(screen.getM_price(),2));
            }
        }
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
        });
        jianghu_webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        jianghu_webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tv_screen_commit.setOnClickListener(this);
        tv_screen_commit1.setOnClickListener(this);
        getView(R.id.group_title_return).setOnClickListener(this);
    }
    /**
     * 设置Cookie
     * @param cookie  格式：uid=21233 如需设置多个，需要多次调用
     */
    private void synCookies(String url,String cookie) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);
        CookieSyncManager.getInstance().sync();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (jianghu_webview.canGoBack()) {
                jianghu_webview.goBack();//返回上一页面
                group_title_right_button.setVisibility(View.VISIBLE);
                setTv_screen_date();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_screen_commit:
                //在 此处弹出提示框
                mBuyTipDialog.showRiskTipDialog();
                break;
            case R.id.group_title_return:
                if (jianghu_webview.canGoBack()) {
                    jianghu_webview.goBack();//返回上一页面
                    group_title_right_button.setVisibility(View.VISIBLE);
                    setTv_screen_date();
                } else {
                    finish();
                }
                break;
            case R.id.tv_screen_commit1:
                if (ShowActivity.isLogin(this)){
                    ShowActivity.skipVIPActivity(this/*,screen*/);
                }
                break;
        }
    }
    //同意风险协议-->跳转购买页面
    @Override
    public void agreeRisk() {
        showBuyActivity();
        mBuyTipDialog.dismissRiskTipDialog();
    }

    public class JsOperation {
        @JavascriptInterface
        public void commit(String Url) {
            LogUtil.e("图片="+Url);
        }
    }
    private void showBuyActivity(){
        if(ShowActivity.isLogin(this)){
            ShowActivity.showActivityForResult(this,bundle,BuyStockActivity.class, RequestCode.SCREEN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        if(resultCode==RESULT_OK&&requestCode==RequestCode.SCREEN){
            getScreenMessage();
        }
    }
    private void getScreenMessage(){
        WebBase.loadScreenProduct(screen_id, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                screen= JSONParse.getScreen(obj);
                if(screen!=null){
                    if(!setTitle){
                        initTitle(screen.getName());
                    }
                    if(screen!=null){
                        setScreenMessage();
                        bundle.putSerializable(IntentKey.SCREEN,screen);
                    }
                }
            }
            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
}

package com.zbmf.StockGroup.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.AskDongAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.AskBean;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;
import com.zbmf.StockGroup.view.WebViewMod;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao
 * on 2018/1/24.
 * 个股详情页
 */

public class StockDetailActivity extends BaseActivity implements View.OnClickListener {
    private ListViewForScrollView listViewForScrollView;
    private AskDongAdapter askDongAdapter;
    private TextView tvStockChat;
    private WebViewMod stockWebView;
    private boolean isClose,isRush;
    private PullToRefreshScrollView pull_scrollview;
    private StockMode stockMode;
    private int page;
    private List<AskBean> askBeans;
    private MatchInfo matchInfo;
    private RelativeLayout mKLineChart;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_stock_detail_layout;
    }

    @Override
    public void initView() {
        stockWebView=getView(R.id.stock_web_view);
        tvStockChat=getView(R.id.tv_stock_chat);
        mKLineChart = getView(R.id.kLineChart);
        listViewForScrollView=getView(R.id.list_view);
        askDongAdapter=new AskDongAdapter(this);
        listViewForScrollView.setAdapter(askDongAdapter);
        setWebViewSetting();
        pull_scrollview=getView(R.id.pull_scrollview);
        pull_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page=1;
                isRush=true;
                getStockList();
                stockWebView.setVisibility(View.INVISIBLE);
                stockWebView.reload();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page+=1;
                getStockList();
            }
        });
        tvStockChat.setOnClickListener(this);
        getView(R.id.tv_ask).setOnClickListener(this);
        getView(R.id.tv_buy).setOnClickListener(this);
        getView(R.id.tv_match_buy).setOnClickListener(this);
        //是否显示K线图 (0:不显示，1：显示) 默认不显示
        int isShowKLineChart = SettingDefaultsManager.getInstance().getIsShowKLineChart();
        if (isShowKLineChart==Constants.NOT_SHOW_K_LINE_CHART){//K线图隐藏
            mKLineChart.setVisibility(View.GONE);
        }else if (isShowKLineChart==Constants.IS_SHOW_K_LINE_CHART){//K线图显示
            mKLineChart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof StockMode){
                stockMode= (StockMode) bundle.getSerializable(IntentKey.STOCKHOLDER);
            }else if(bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof AskBean){
                AskBean askBean= (AskBean) bundle.getSerializable(IntentKey.STOCKHOLDER);
                stockMode=new StockMode(askBean.getStock_name(),askBean.getSymbol());
            }else if(bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof StockholdsBean){
                StockholdsBean stockholdsBean= (StockholdsBean) bundle.getSerializable(IntentKey.STOCKHOLDER);
                stockMode=new StockMode(stockholdsBean.getName(),stockholdsBean.getSymbol());
            }else if(bundle.getSerializable(IntentKey.STOCKHOLDER)instanceof DealSys){
                DealSys dealSys= (DealSys) bundle.getSerializable(IntentKey.STOCKHOLDER);
                stockMode=new StockMode(dealSys.getStock_name(),dealSys.getSumbol());
            }else if(bundle.getSerializable(IntentKey.STOCKHOLDER)instanceof Stock){
                Stock stock= (Stock) bundle.getSerializable(IntentKey.STOCKHOLDER);
                stockMode=new StockMode(stock.getName(),stock.getSymbol());
            }
            initTitle(stockMode.getStockNmae());
            String url=String.format(HtmlUrl.STOCK_URL,stockMode.getSymbol().startsWith("60")?("sh"+stockMode.getSymbol()):("sz"+stockMode.getSymbol()));
            stockWebView.loadUrl(url);
            stockWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if(isClose){
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            isClose = true;
                            while (isClose){
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.sendEmptyMessage(0x001);
                            }
                        }
                    }).start();
                }
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        stockWebView.setVisibility(View.INVISIBLE);
                        stockWebView.loadUrl(getClearAdDivJs());
                        stockWebView.loadUrl("javascript:hideAd();"); //调用js方法
                    }
                };
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    isClose = false;
                }
            });
        }
        stockWebView.setWebChromeClient(new WebChromeClient() {
            //设置加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    dialogDiss();
                    stockWebView.setVisibility(View.VISIBLE);
                }else{
                    dialogShow();
                }
            }
        });
        isRush=true;
        page=1;
        getStockList();
    }
    private void setWebViewSetting(){
        WebSettings webSettings = stockWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams layoutParams=stockWebView.getLayoutParams();
        layoutParams.width= dm.widthPixels;
        layoutParams.height= (int) (0.95*dm.widthPixels);
        stockWebView.setLayoutParams(layoutParams);
    }
    @Override
    public void addListener() {

    }

    public  String getClearAdDivJs(){
        String js = "javascript:function hideAd() {";
        String[] adDivs =getResources().getStringArray(R.array.adBlockDiv);
        for (int i = 0; i < adDivs.length; i++) {
            js += "var adDiv" + i + "= document.getElementsByClassName('" + adDivs[i] + "');if(adDiv" + i + " != null)" +
                    "{var x; for (x = 0; x < adDiv" + i + ".length; x++) {adDiv" + i + "[x].remove();}}";
        }
        js += "}";
        return js;
    }
    private void getStockList(){
        WebBase.stockAskList(stockMode.getSymbol(), page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (askBeans == null) {
                    askBeans = new ArrayList<>();
                }
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    if(isRush){
                        askBeans.clear();
                        isRush=false;
                    }
                    page = result.optInt("page");
                    if (result.has("asks")) {
                        askBeans.addAll(JSONParse.getAskBeans(result.optJSONArray("asks")));
                        askDongAdapter.setList(askBeans);
                    }
                }
                onRushList();
            }

            @Override
            public void onFailure(String err_msg) {
                onRushList();
            }
        });
    }
    private void onRushList(){
        if(pull_scrollview!=null&&pull_scrollview.isRefreshing()){
            pull_scrollview.onRefreshComplete();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle=null;
        switch (v.getId()){
            case R.id.tv_stock_chat:
                bundle=new Bundle();
                bundle.putSerializable(IntentKey.STOCKHOLDER, stockMode);
                ShowActivity.showActivity(this,bundle,SimulateOneStockCommitActivity.class);
                break;
            case R.id.tv_buy:
                ShowActivity.showWebViewActivity(this, HtmlUrl.TRADER_BUY+stockMode.getSymbol());
                break;
            case R.id.tv_ask:
                bundle=new Bundle();
                bundle.putInt(IntentKey.FLAG,3);
                bundle.putString(IntentKey.STOCK_NAME,stockMode.getStockNmae());
                bundle.putString(IntentKey.STOCK_SYMOL,stockMode.getSymbol());
                ShowActivity.showActivity(this,bundle,AskStockSendActivity.class);
                break;
            case R.id.tv_match_buy:
                if(matchInfo==null){
                    getMatchInfo(true);
                }else{
                    bundle=new Bundle();
                    bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                    bundle.putSerializable(IntentKey.STOCKHOLDER, stockMode);
                    ShowActivity.showActivity(StockDetailActivity.this, bundle, StockBuyActivity.class);
                }
                break;
        }
    }
    private void getMatchInfo(final boolean isShow){
//        WebBase.getPlayer(new JSONHandler() {
        WebBase.getMatchPlayer(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
//                matchInfo=JSONParse.getMatchMessage(obj);
                matchInfo=JSONParse.getMatchMessage1(obj);
                if(isShow){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                    bundle.putSerializable(IntentKey.STOCKHOLDER, stockMode);
                    ShowActivity.showActivity(StockDetailActivity.this, bundle, StockBuyActivity.class);
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
}

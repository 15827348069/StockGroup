package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.StockAskAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.MatchRank;
import com.zbmf.StocksMatch.bean.StockAskBean;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.HtmlUrl;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.listener.IStockDetailView;
import com.zbmf.StocksMatch.presenter.StockDetailPresenter;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.DrawableCenterTextView;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.StocksMatch.view.WebViewMod;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个股详情页
 */
public class StockDetailActivity extends BaseActivity<StockDetailPresenter> implements IStockDetailView {
    @BindView(R.id.stock_web_view)
    WebViewMod stockWebView;
    @BindView(R.id.loadingImageView)
    ProgressBar loadingImageView;
    @BindView(R.id.id_tv_loadingmsg)
    TextView id_tv_loadingmsg;
    @BindView(R.id.dialog_layout)
    LinearLayout dialog_layout;
    @BindView(R.id.tv_stock_chat)
    TextView tv_stock_chat;
    @BindView(R.id.list_view)
    ListViewForScrollView list_view;
    @BindView(R.id.pull_scrollview)
    PullToRefreshScrollView pull_scrollview;
    @BindView(R.id.tv_ask)
    DrawableCenterTextView tv_ask;
    @BindView(R.id.tv_buy)
    DrawableCenterTextView tv_buy;
    @BindView(R.id.tv_match_buy)
    DrawableCenterTextView tv_match_buy;
    @BindView(R.id.kLineChart)
    RelativeLayout kLineChart;
    private StockMode stockMode;
    private boolean isClose, isError, isSuccess;
    private StockDetailPresenter mStockDetailPresenter;
    private int mPage;
    private int mTotal;
    private StockAskAdapter mStockAskAdapter;
    private MatchInfo mMatchInfo;
    private String mMatchID;

    @Override
    protected int getLayout() {
        return R.layout.activity_stock_detail;
    }

    @Override
    protected String initTitle() {
        return stockMode.getStockNmae();
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        ShowOrHideProgressDialog.showProgressDialog(StockDetailActivity.this,
                StockDetailActivity.this,getString(R.string.hard_loading));
        ShowOrHideProgressDialog.setDialogCancel(false);
        setWebViewSetting();
        if (bundle!=null){
            mMatchID = bundle.getString(IntentKey.MATCH_ID);
            if (bundle.getSerializable(IntentKey.STOCK_HOLDER) instanceof MatchRank.Result.Yields.Last_deal) {
                if (bundle.getSerializable(IntentKey.STOCK_HOLDER) != null) {
                    MatchRank.Result.Yields.Last_deal lastDeal = (MatchRank.Result.Yields.Last_deal)
                            bundle.getSerializable(IntentKey.STOCK_HOLDER);
                    stockMode = new StockMode(lastDeal.getName(), lastDeal.getSymbol());
                }
            } else if (bundle.getSerializable(IntentKey.STOCK_HOLDER) instanceof StockMode) {
                stockMode = (StockMode) bundle.getSerializable(IntentKey.STOCK_HOLDER);
            }

            if (bundle.getSerializable(IntentKey.MATCH_INFO) instanceof MatchInfo) {
                mMatchInfo = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_INFO);
            }
        }
        //是否显示K线图 (0:不显示，1：显示) 默认不显示
        int isShowKLineChart = SharedpreferencesUtil.getInstance().getInt(SharedKey.IS_SHOW_KLINE_CHART,
                Constans.IS_SHOW_K_LINE_CHART);
        if (isShowKLineChart==Constans.NOT_SHOW_K_LINE_CHART){
            kLineChart.setVisibility(View.GONE);
        }else if (isShowKLineChart==Constans.IS_SHOW_K_LINE_CHART){
            kLineChart.setVisibility(View.VISIBLE);
        }
        mStockAskAdapter = new StockAskAdapter(this);
        list_view.setAdapter(mStockAskAdapter);
        pull_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPage = 1;
                getPresenter().setFirst(true);
                getPresenter().getDatas();
                stockWebView.setVisibility(View.INVISIBLE);
                stockWebView.reload();
                ShowOrHideProgressDialog.showProgressDialog(StockDetailActivity.this,StockDetailActivity.this,
                        getString(R.string.hard_loading));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTotal!=-1){
                    if (mStockAskAdapter!=null){
                        if (mStockAskAdapter.getList()!=null){
                            if (mTotal > mStockAskAdapter.getList().size()) {
                                mPage += 1;
                                mStockDetailPresenter.getAskStockList(stockMode.getSymbol(), String.valueOf(mPage));
                            } else {
                                showToast("没有更多了");
                            }
                        }
                    }
                }
            }
        });
        //https://gupiao.baidu.com/stock/sz300058
        String symbol = stockMode.getSymbol();
        boolean b = stockMode.getSymbol().startsWith("60");
        String url = String.format(HtmlUrl.STOCK_URL, stockMode.getSymbol().startsWith("60") ?
                ("sh" + stockMode.getSymbol()) : ("sz" + stockMode.getSymbol()));
        stockWebView.loadUrl(url);
        stockWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isClose) {
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isClose = true;
                        while (isClose) {
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

            @SuppressLint("HandlerLeak")
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (stockWebView!=null){
                        stockWebView.setVisibility(View.INVISIBLE);
                        stockWebView.loadUrl(getClearAdDivJs());
                        stockWebView.loadUrl("javascript:hideAd();"); //调用js方法
                    }
                }
            };

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isClose = false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不另跳浏览器
                // 在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    stockWebView.stopLoading();
                    return true;
                }
                return false;
            }
        });
        stockWebView.setWebChromeClient(new WebChromeClient() {
            //设置加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    ShowOrHideProgressDialog.disMissProgressDialog();
//                    DissLoading();
                    if (stockWebView!=null){
                        stockWebView.setVisibility(View.VISIBLE);
                    }
                } else {
//                    ShowOrHideProgressDialog.showProgressDialog(StockDetailActivity.this,
//                            StockDetailActivity.this,getString(R.string.hard_loading));
////                    initProgressDialog();
////                    setProgressDialogMsg(getString(R.string.hard_loading));
////                    ShowLoading();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stockWebView.loadUrl("about:blank");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebViewSetting() {
        WebSettings webSettings = stockWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams layoutParams = stockWebView.getLayoutParams();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = (int) (0.95 * dm.widthPixels);
        stockWebView.setLayoutParams(layoutParams);
    }

    public String getClearAdDivJs() {
        String js = "javascript:function hideAd() {";
        String[] adDivs = getResources().getStringArray(R.array.adBlockDiv);
        for (int i = 0; i < adDivs.length; i++) {
            js += "var adDiv" + i + "= document.getElementsByClassName('" + adDivs[i] + "');if(adDiv" + i + " != null)" +
                    "{var x; for (x = 0; x < adDiv" + i + ".length; x++) {adDiv" + i + "[x].remove();}}";
        }
        js += "}";
        return js;
    }

    @Override
    protected StockDetailPresenter initPresent() {
        mStockDetailPresenter = new StockDetailPresenter(stockMode.getSymbol(), String.valueOf(ParamsKey.D_PAGE));
        return mStockDetailPresenter;
    }

    private List<StockAskBean.Result.Asks> askList = new ArrayList<>();

    //获取互动列表的数据
    @Override
    public void stockAskList(StockAskBean.Result o) {
       ShowOrHideProgressDialog.disMissProgressDialog();
        if (pull_scrollview != null && pull_scrollview.isRefreshing()) {
            pull_scrollview.onRefreshComplete();
        }
        mPage = o.getPage();
        mTotal = o.getTotal();
        List<StockAskBean.Result.Asks> asks = o.getAsks();
        if (mStockAskAdapter.getList() == null) {
            mStockAskAdapter.setList(asks);
        } else {
            if (mPage == 1) {
                askList.addAll(asks);
                mStockAskAdapter.clearList();
                mStockAskAdapter.addList(askList);
                askList.clear();
            } else {
                if (mTotal > mStockAskAdapter.getList().size()) {
                    mStockAskAdapter.addList(asks);
                    mStockAskAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void stockAskErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        ShowOrHideProgressDialog.disMissProgressDialog();
    }

    @Override
    public void matchInfo(MatchInfo matchInfo) {
        assert matchInfo != null;
        this.mMatchInfo = matchInfo;
        ShowActivity.showStockBuyActivity(this, mMatchInfo,-1, stockMode,Constans.BUY_FLAG,mMatchID);
    }

    @Override
    public void matchInfoErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @OnClick({R.id.tv_stock_chat, R.id.tv_buy, R.id.tv_ask, R.id.tv_match_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_stock_chat:
                ShowActivity.showStockCommentActivity(this, stockMode);
                break;
            case R.id.tv_buy:
                ShowActivity.showWebViewActivity(this, HtmlUrl.TRADER_BUY + stockMode.getSymbol(),getString(R.string.run_tactics));
                break;
            case R.id.tv_ask:
                askDong();
                break;
            case R.id.tv_match_buy:
                if (mMatchInfo == null) {
                    mStockDetailPresenter.getMatchInfo(Constans.MATCH_ID, MatchSharedUtil.UserId());
                } else {
                    ShowActivity.showStockBuyActivity(this, mMatchInfo,-1, stockMode,Constans.BUY_FLAG,mMatchID);
                }
                break;
        }
    }

    private void askDong() {
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom))
                .setMessage(R.string.sbmf_group_tip)
                .setNegativeButton(R.string.wait, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.load_app, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowActivity.goToAppGroup(StockDetailActivity.this);
                    }
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.
                getColor(this,R.color.black));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.
                getColor(this,R.color.apply_textcolor));
    }
}

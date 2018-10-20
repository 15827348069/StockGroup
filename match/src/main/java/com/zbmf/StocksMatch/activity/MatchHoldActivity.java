package com.zbmf.StocksMatch.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MatchHoldAdapter;
import com.zbmf.StocksMatch.adapter.MatchHoldTitleAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.BuyClick;
import com.zbmf.StocksMatch.listener.IMatchHold;
import com.zbmf.StocksMatch.listener.SellClick;
import com.zbmf.StocksMatch.presenter.MatchHoldPresenter;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.util.StockSellEvent;
import com.zbmf.StocksMatch.view.BuyDialog;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.view.ListViewForScrollView;
import com.zbmf.worklibrary.view.SyncHorizontalScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xuhao
 * on 2017/12/18.
 * 操盘高手持仓的展示页面
 */

public class MatchHoldActivity extends BaseActivity<MatchHoldPresenter> implements IMatchHold, AdapterView.OnItemClickListener,
        SellClick, BuyClick {
    @BindView(R.id.matchOperate_rightTitleScrollView)
    SyncHorizontalScrollView matchOperateRightTitleScrollView;
    @BindView(R.id.matchOperate_leftTitleListView)
    ListViewForScrollView matchOperateLeftTitleListView;
    @BindView(R.id.matchOperate_rightContentListView)
    ListViewForScrollView matchOperateRightContentListView;
    @BindView(R.id.matchOperate_rightContentScrollView)
    SyncHorizontalScrollView matchOperateRightContentScrollView;
    @BindView(R.id.myscrllview)
    PullToRefreshScrollView myscrllview;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.no_message_text)
    TextView noMessage;
    private MatchHoldAdapter holdAdapter;
    private MatchHoldTitleAdapter titleAdapter;
    private String userId, matchId;
    private int mPage;
    private int mTotal;
    private BuyDialog mBuyDialog;
    private MatchInfo mMatchInfo;
    private MatchHoldPresenter mMatchHoldPresenter;
    private Handler mHandler = new Handler();
    private int mFlag;
    //    private int mFlag;

    @Override
    protected int getLayout() {
        return R.layout.activity_match_holds;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.match_hold_title);
    }

    @Override
    protected void initData(Bundle bundle) {
        // 注册订阅者
        EventBus.getDefault().register(this);
        MyActivityManager.getMyActivityManager().pushAct(this);
        ShowOrHideProgressDialog.showProgressDialog(this, this, getString(R.string.hard_loading));
        if (bundle != null) {
            mFlag = bundle.getInt(IntentKey.FLAG);
            userId = bundle.getString(IntentKey.USER);
            matchId = bundle.getString(IntentKey.MATCH_ID);
            mMatchInfo = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_INFO);
//            mFlag = bundle.getInt(IntentKey.FLAG);//区分持仓和卖出
        }
        if (mBuyDialog == null) {
            mBuyDialog = new BuyDialog(this, R.style.Buy_Dialog).createDialog();
        }
        matchOperateRightTitleScrollView.setScrollView(matchOperateRightContentScrollView);
        matchOperateRightContentScrollView.setScrollView(matchOperateRightTitleScrollView);
        titleAdapter = new MatchHoldTitleAdapter(this);
        matchOperateLeftTitleListView.setAdapter(titleAdapter);
        matchOperateLeftTitleListView.setOnItemClickListener(this);
        holdAdapter = new MatchHoldAdapter(this);
        matchOperateRightContentListView.setAdapter(holdAdapter);
        matchOperateRightContentListView.setOnItemClickListener(this);
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getPresenter().getDatas();
            }
        });
        setTitleView();
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (titleAdapter.getList() != null && holdAdapter.getList() != null) {
                    titleAdapter.getList().clear();
                    holdAdapter.getList().clear();
                    stocksList.clear();
                    stocksList1.clear();
                    getPresenter().setFirst(true);
                    getData();
                    ShowOrHideProgressDialog.showProgressDialog(MatchHoldActivity.this, MatchHoldActivity.this, getString(R.string.hard_loading));
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myscrllview.onRefreshComplete();
                        }
                    }, 1000);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTotal > titleAdapter.getList().size() && mTotal > holdAdapter.getList().size()) {
                    mPage += 1;
                    getData();
                    ShowOrHideProgressDialog.showProgressDialog(MatchHoldActivity.this, MatchHoldActivity.this, getString(R.string.hard_loading));
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myscrllview.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }

    private void getData() {
        if (mFlag == IntentKey.DEFAULT_FLAG_INT) {
            mMatchHoldPresenter.getHolderList(String.valueOf(ParamsKey.D_PAGE), Integer.parseInt(matchId), MatchSharedUtil.UserId());
        } else if (mFlag == IntentKey.TRADER_FLAG_INT) {
            mMatchHoldPresenter.traderHolderPositionRecord(userId);
        }
    }

    @Override
    protected MatchHoldPresenter initPresent() {
        mMatchHoldPresenter = new MatchHoldPresenter(matchId, userId, mFlag);
        return mMatchHoldPresenter;
    }

    private List<HolderPositionBean.Result.Stocks> stockList = new ArrayList<>();
    private List<HolderPositionBean.Result.Stocks> stockList1 = new ArrayList<>();

    @Override
    public void RushHoldList(HolderPositionBean.Result result) {
        if (mFlag == IntentKey.DEFAULT_FLAG_INT) {
            ShowOrHideProgressDialog.disMissProgressDialog();
            if (myscrllview.isRefreshing()) {
                myscrllview.onRefreshComplete();
            }
            if (result != null) {
                mPage = result.getPage();
                mTotal = result.getTotal();
                List<HolderPositionBean.Result.Stocks> stocks = result.getStocks();
                if (stocks == null || stocks.size() == 0) {
                    noMessage.setText(getString(R.string.no_holder_record));
                    llNone.setVisibility(View.VISIBLE);
                }
                if (titleAdapter.getList() == null) {
                    titleAdapter.setList(stocks);
                } else {
                    if (mPage == ParamsKey.D_PAGE) {
                        stockList.addAll(stocks);
                        titleAdapter.clearList();
                        titleAdapter.addList(stockList);
                        stockList.clear();
                    } else {
                        titleAdapter.addList(stocks);
                    }
                }
                if (holdAdapter.getList() == null) {
                    holdAdapter.setList(stocks);
                } else {
                    if (mPage == ParamsKey.D_PAGE) {
                        stockList1.addAll(stocks);
                        holdAdapter.clearList();
                        holdAdapter.addList(stockList1);
                        stockList1.clear();
                    } else {
                        holdAdapter.addList(stocks);
                    }
                }
            }
        }
    }

    @Override
    public void holderListErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    private List<HolderPositionBean.Result.Stocks> stocksList = new ArrayList<>();
    private List<HolderPositionBean.Result.Stocks> stocksList1 = new ArrayList<>();

    //持仓
    @Override
    public void onRefreshHolderRecord(List<TraderHolderPosition.Holds> holds) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (myscrllview.isRefreshing()) {
            myscrllview.onRefreshComplete();
        }
        if (holds == null || holds.size() == 0) {
            noMessage.setText(getString(R.string.no_holder_record));
            llNone.setVisibility(View.VISIBLE);
        }

//        hold.holdsTextViewCount.setText(String.valueOf(stockholdsBean.getVolumn_total()));//持仓股数
//        hold.holdsTextViewAvailable.setText(String.valueOf(stockholdsBean.getVolumn_unfrozen()));//可用股数
//        hold.holdsTextViewTransactionPrice.setText(String.format("%.2f", stockholdsBean.getPrice_buy()));//成本价
//        hold.holdsTextViewCurrentPrice.setText(String.format("%.2f", stockholdsBean.getCurrent()));//当前价
//        hold.holdsTextViewFloating.setText(String.format("%.2f", stockholdsBean.getProfit()));//浮动盈亏
//        hold.holdsTextViewRate.setText(String.format("%s", DoubleFromat.getStockDouble(stockholdsBean.getYield_float() * 100, 2) + "%"));
//        hold.holdsTextViewFloating.setTextColor(stockholdsBean.
//                getProfit() > 0 ? mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.green));
//        hold.holdsTextViewRate.setTextColor(stockholdsBean.
//                getYield_float() > 0 ? mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.green));
//        convertView.setBackgroundDrawable(position % 2 == 0 ? mContext.getResources().
//                getDrawable(R.drawable.list_item_seletor_gray) : mContext.getResources().getDrawable(R.drawable.list_item_seletor_white));

        if (mFlag == IntentKey.TRADER_FLAG_INT) {
            if (getPresenter().isFirst() && holds != null/*&&holds.size()>stocksList.size()*/) {
                for (int i = 0; i < holds.size(); i++) {
                    HolderPositionBean.Result.Stocks stocks = new HolderPositionBean.Result.Stocks();
                    TraderHolderPosition.Holds holds1 = holds.get(i);
                    stocks.setName(holds1.getName());
                    stocks.setVolumn_total(holds1.getVolumn_total());
                    stocks.setVolumn_unfrozen(holds1.getVolumn_unfrozen());
                    stocks.setPrice_buy(Double.parseDouble(holds1.getPrice()));
                    stocks.setCurrent(Double.parseDouble(holds1.getCurrent()));
                    stocks.setProfit(Double.parseDouble(holds1.getGain()));
                    stocks.setYield_float(Double.parseDouble(holds1.getGain_yield()));

//                    stocks.setPrice_float(Double.parseDouble(holds1.getPrice()));
//                        String current = holds1.getCurrent();
//                        if (current!=null&&current.contains(".")) {
//                            String s = current.substring(0, current.indexOf("."));
//                            stocks.setCommnet_count(Integer.parseInt(s));
//                        }else {
//                    stocks.setCommnet_count(Double.parseDouble(holds1.getCurrent()));
//                        }
//                    stocks.setPrice_float(Double.parseDouble(holds1.getGain()));
//                    stocks.setYield_float(Double.parseDouble(holds1.getGain_yield()));
                    stocksList.add(stocks);
                    stocksList1.add(stocks);
                }
                if (titleAdapter.getList() == null) {
                    titleAdapter.setList(stocksList);
                } else {
                    if (mPage == ParamsKey.D_PAGE) {
                        stockList.addAll(stocksList);
                        titleAdapter.clearList();
                        titleAdapter.addList(stockList);
                        stockList.clear();
                    } else {
                        titleAdapter.addList(stocksList);
                    }
                }
                if (holdAdapter.getList() == null) {
                    holdAdapter.setList(stocksList1);
                } else {
                    if (mPage == ParamsKey.D_PAGE) {
                        stockList.addAll(stocksList1);
                        holdAdapter.clearList();
                        holdAdapter.addList(stocksList1);
                        stockList.clear();
                    } else {
                        holdAdapter.addList(stocksList1);
                    }
                }
//                stocksList.clear();
            }
        }
    }

    private void setTitleView() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        String rightTitles[] = getResources().getStringArray(R.array.match_hold);
        for (int i = 0; i < rightTitles.length; i++) {
            View convertView = LayoutInflater.from(MatchHoldActivity.this).inflate(R.layout.match_title_layout, null);
            TextView title = convertView.findViewById(R.id.trusts_cell_name);
            title.setText(rightTitles[i]);
            layout.addView(convertView);
        }
        matchOperateRightTitleScrollView.addView(layout);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //弹出买入或者卖出
        HolderPositionBean.Result.Stocks item = holdAdapter.getItem(i);
        mBuyDialog.setStocks(item).showI().setSellClick(this).setBuyClick(this);
    }

    @Override
    public void buyClick(HolderPositionBean.Result.Stocks stocks) {
        if (stocks != null) {
            ShowActivity.showStockBuyActivity(this, mMatchInfo, stocks.getVolumn_unfrozen(),
                    new StockMode(stocks.getName(), stocks.getSymbol()), Constans.BUY_FLAG, matchId);
            mBuyDialog.dissMissI();
        }
    }

    @Override
    public void sellClick(HolderPositionBean.Result.Stocks stocks) {
        if (stocks != null) {
            ShowActivity.showStockBuyActivity(this, mMatchInfo,
                    stocks.getVolumn_unfrozen(), new StockMode(stocks.getName(), stocks.getSymbol()),
                    Constans.SELL_FLAG, matchId);
            mBuyDialog.dissMissI();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StockSellEvent event) {
        // 更新持股信息
        mMatchHoldPresenter.getHolderList(String.valueOf(ParamsKey.D_PAGE), Integer.parseInt(matchId), MatchSharedUtil.UserId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}

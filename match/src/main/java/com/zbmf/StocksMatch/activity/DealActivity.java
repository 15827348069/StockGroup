package com.zbmf.StocksMatch.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.DealsContentAdapter;
import com.zbmf.StocksMatch.adapter.DealsTitleAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.model.imode.DealModeView;
import com.zbmf.StocksMatch.presenter.DealRecordPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.view.ListViewForScrollView;
import com.zbmf.worklibrary.view.SyncHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 交易记录activity
 */
public class DealActivity extends BaseActivity<DealRecordPresenter> implements DealModeView, AdapterView.OnItemClickListener {
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
    private DealsContentAdapter contentAdapter;
    private DealsTitleAdapter titleAdapter;
    private String userId;
    private int mPage;
    private int mTotal;
    private Handler mHandler = new Handler();
    private DealRecordPresenter mDealRecordPresenter;
    private String mFlag, matchID;
    private String mId;

    @Override
    protected int getLayout() {
        return R.layout.activity_match_holds;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.deal_title);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            userId = bundle.getString(IntentKey.USER);
            mFlag = bundle.getString(IntentKey.FLAG);
            matchID = bundle.getString(IntentKey.MATCH_ID);
            mId = bundle.getString(IntentKey.ID);
        }
        matchOperateRightTitleScrollView.setScrollView(matchOperateRightContentScrollView);
        matchOperateRightContentScrollView.setScrollView(matchOperateRightTitleScrollView);
        titleAdapter = new DealsTitleAdapter(this);
        matchOperateLeftTitleListView.setAdapter(titleAdapter);
        matchOperateLeftTitleListView.setOnItemClickListener(this);
        contentAdapter = new DealsContentAdapter(this);
        matchOperateRightContentListView.setAdapter(contentAdapter);
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
                if (titleAdapter.getList() != null && contentAdapter.getList() != null) {
                    titleAdapter.getList().clear();
                    contentAdapter.getList().clear();
                    if (mFlag.equals(IntentKey.NOR_FLAG)) {
                        mDealRecordPresenter.getNormalDealRecord(matchID, String.valueOf(ParamsKey.D_PAGE), mId);
                    } else if (mFlag.equals(IntentKey.TRADER_FLAG)) {
                        mDealRecordPresenter.traderDealRecord(userId, ParamsKey.D_PAGE, ParamsKey.D_PERPAGE);
                    }
                    ShowOrHideProgressDialog.showProgressDialog(DealActivity.this, DealActivity.this, getString(R.string.hard_loading));
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
                if (mTotal > titleAdapter.getList().size() && mTotal > contentAdapter.getList().size()) {
                    mPage += 1;
                    if (mFlag.equals(IntentKey.NOR_FLAG)) {
                        mDealRecordPresenter.getNormalDealRecord(matchID, String.valueOf(mPage), mId);
                    } else if (mFlag.equals(IntentKey.TRADER_FLAG)) {
                        mDealRecordPresenter.traderDealRecord(userId, mPage, ParamsKey.D_PERPAGE);
                    }
                    ShowOrHideProgressDialog.showProgressDialog(DealActivity.this, DealActivity.this, getString(R.string.hard_loading));
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

    @Override
    protected DealRecordPresenter initPresent() {
        mDealRecordPresenter = new DealRecordPresenter(matchID, userId, mFlag, mId);
        return mDealRecordPresenter;
    }

    private void setTitleView() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        String rightTitles[] = getResources().getStringArray(R.array.match_deals);
        for (int i = 0; i < rightTitles.length; i++) {
            View convertView = LayoutInflater.from(DealActivity.this).inflate(R.layout.match_title_layout, null);
            TextView title = convertView.findViewById(R.id.trusts_cell_name);
            title.setText(rightTitles[i]);
            layout.addView(convertView);
        }
        matchOperateRightTitleScrollView.addView(layout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private List<TraderDeals.Result.Deals> newDeals = new ArrayList<>();

    //更新交易记录的数据
    @Override
    public void onRefreshDealRecord(TraderDeals.Result result) {
        if (myscrllview.isRefreshing()) {
            myscrllview.onRefreshComplete();
        }
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (result != null) {
            mPage = result.getPage();
            mTotal = result.getTotal();
            List<TraderDeals.Result.Deals> deals = result.getDeals();
            if (deals == null || deals.size() == 0) {
                noMessage.setText(getString(R.string.no_holder_record));
                llNone.setVisibility(View.VISIBLE);
            }
            if (titleAdapter.getList() == null) {
                titleAdapter.setList(deals);
                contentAdapter.setList(deals);
            } else {
                if (mPage == ParamsKey.D_PAGE) {
                    newDeals.addAll(deals);
                    titleAdapter.clearList();
                    contentAdapter.clearList();
                    contentAdapter.setList(newDeals);
                    titleAdapter.addList(newDeals);
                    newDeals.clear();
                } else {
                    titleAdapter.addList(deals);
                    contentAdapter.addList(deals);
                }
            }
        }
    }

    //普通用户的交易记录
    @Override
    public void queryData(DealsRecordList.Result result) {
        if (myscrllview.isRefreshing()) {
            myscrllview.onRefreshComplete();
        }
        if (mFlag.equals(IntentKey.NOR_FLAG)) {
            ShowOrHideProgressDialog.disMissProgressDialog();
            if (result != null) {
                mPage = result.getPage();
                mTotal = result.getTotal();
                List<DealsRecordList.Result.Stocks> stocks = result.getStocks();
                List<TraderDeals.Result.Deals> dealList = new ArrayList<>();
                for (int i = 0; i < stocks.size(); i++) {
                    dealList.clear();
                    TraderDeals.Result.Deals deals = new TraderDeals.Result.Deals();
                    deals.setName(stocks.get(i).getName());
                    deals.setPrice(String.valueOf(stocks.get(i).getPrice()));
                    deals.setVolumn(stocks.get(i).getVolumn());
                    deals.setGain(String.valueOf(stocks.get(i).getProfit()));
                    double v = stocks.get(i).getPrice_sell() - stocks.get(i).getPrice_buy();
                    double v1 = v / stocks.get(i).getPrice_buy();
                    deals.setGain_yield(String.valueOf(v1));
                    deals.setAction("");
                    deals.setCreated_at(stocks.get(i).getContract_id());
                    dealList.add(deals);
                }
                if (dealList == null || dealList.size() == 0) {
                    noMessage.setText(getString(R.string.no_holder_record));
                    llNone.setVisibility(View.VISIBLE);
                }
                if (titleAdapter.getList() == null) {
                    titleAdapter.setList(dealList);
                } else {
                    if (mPage == ParamsKey.D_PAGE) {
                        newDeals.addAll(dealList);
                        titleAdapter.clearList();
                        titleAdapter.addList(newDeals);
                        newDeals.clear();
                    } else {
                        titleAdapter.addList(dealList);
                    }
                }
                if (contentAdapter.getList() == null) {
                    contentAdapter.setList(dealList);
                } else {
                    if (mPage == ParamsKey.D_PAGE) {
                        newDeals.addAll(dealList);
                        contentAdapter.clearList();
                        contentAdapter.addList(newDeals);
                        newDeals.clear();
                    } else {
                        contentAdapter.addList(dealList);
                    }
                }
            }
        }
    }

    @Override
    public void queryErr(String msg) {
    }
}

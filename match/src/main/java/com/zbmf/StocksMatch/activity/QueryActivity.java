package com.zbmf.StocksMatch.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.QueryAdapter;
import com.zbmf.StocksMatch.adapter.QueryContentAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IQueryView;
import com.zbmf.StocksMatch.presenter.QueryPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.view.ListViewForScrollView;
import com.zbmf.worklibrary.view.SyncHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class QueryActivity extends BaseActivity<QueryPresenter> implements IQueryView, AdapterView.OnItemClickListener {
    @BindView(R.id.matchOperate_rightTitleScrollView)
    SyncHorizontalScrollView matchOperate_rightTitleScrollView;
    @BindView(R.id.matchOperate_leftTitleListView)
    ListViewForScrollView matchOperate_leftTitleListView;
    @BindView(R.id.matchOperate_rightContentListView)
    ListViewForScrollView matchOperate_rightContentListView;
    @BindView(R.id.matchOperate_rightContentScrollView)
    SyncHorizontalScrollView matchOperate_rightContentScrollView;
    @BindView(R.id.myscrllview)
    PullToRefreshScrollView myscrllview;
    @BindView(R.id.no_message_text)
    TextView noMessage;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    private QueryPresenter mQueryPresenter;
    private MatchInfo mMatchInfo;
    private int mPage;
    private int mTotal;
    private QueryContentAdapter mQueryContentAdapter;
    private QueryAdapter mQueryAdapter;
    private String mMatchId;

    @Override
    protected int getLayout() {
        return R.layout.activity_match_holds;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.record);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        assert bundle != null;
        if (bundle != null) {
            mMatchId = bundle.getString(IntentKey.MATCH_ID);
            if (bundle.getSerializable(IntentKey.MATCH_INFO) instanceof MatchInfo) {
                mMatchInfo = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_INFO);
            }
        }
        mQueryAdapter = new QueryAdapter(this);
        mQueryContentAdapter = new QueryContentAdapter(this);
        matchOperate_leftTitleListView.setAdapter(mQueryAdapter);
        matchOperate_rightContentListView.setAdapter(mQueryContentAdapter);
        matchOperate_leftTitleListView.setOnItemClickListener(this);
        matchOperate_rightContentListView.setOnItemClickListener(this);
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mQueryAdapter.getList() != null) {
                    mPage = ParamsKey.D_PAGE;
                    mQueryAdapter.clearList();
                    getPresenter().setFirst(true);
                    getPresenter().getDatas();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTotal > mQueryContentAdapter.getList().size()) {
                    mPage += 1;
                    mQueryPresenter.getDealList(Constans.MATCH_ID, String.valueOf(mPage),
                            String.valueOf(mMatchInfo.getResult().getMatch_id()));
                } else {
                    showToast(getString(R.string.nomore_loading));
                    myscrllview.onRefreshComplete();
                }
            }
        });
        setTitleView();
    }

    @Override
    protected QueryPresenter initPresent() {
        mQueryPresenter = new QueryPresenter(mMatchId, String.valueOf(ParamsKey.D_PAGE),
                String.valueOf(mMatchInfo.getResult().getMatch_id()));
        return mQueryPresenter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DealsRecordList.Result.Stocks item = mQueryContentAdapter.getItem(position);
        if (item != null) {
            ShowActivity.showStockDetail2(this, new StockMode(item.getName(), item.getSymbol()), Constans.MATCH_ID);
        }
    }

    private List<DealsRecordList.Result.Stocks> stocksList = new ArrayList<>();

    @Override
    public void queryData(DealsRecordList.Result result) {
        if (myscrllview.isRefreshing()) {
            myscrllview.onRefreshComplete();
        }
        if (result != null) {
            mPage = result.getPage();
            mTotal = result.getTotal();
            List<DealsRecordList.Result.Stocks> stocks = result.getStocks();
            if (stocks == null || stocks.size() == 0) {
                noMessage.setText(getString(R.string.no_holder_record));
                llNone.setVisibility(View.VISIBLE);
            }
            if (mQueryAdapter.getList() == null) {
                mQueryAdapter.setList(stocks);
            } else {
                if (mPage == ParamsKey.D_PAGE) {
                    stocksList.addAll(stocks);
                    mQueryAdapter.clearList();
                    mQueryAdapter.addList(stocksList);
                    stocksList.clear();
                } else {
                    mQueryAdapter.addList(stocks);
                }
            }
            if (mQueryContentAdapter.getList() == null) {
                mQueryContentAdapter.setList(stocks);
            } else {
                if (mPage == ParamsKey.D_PAGE) {
                    stocksList.addAll(stocks);
                    mQueryContentAdapter.clearList();
                    mQueryContentAdapter.addList(stocksList);
                    stocksList.clear();
                } else {
                    mQueryContentAdapter.addList(stocks);
                }
            }
        }
    }

    @Override
    public void queryErr(String msg) {
        if (myscrllview.isRefreshing()) {
            myscrllview.onRefreshComplete();
        }
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    private void setTitleView() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        String rightTitles[] = getResources().getStringArray(R.array.match_deals);
        for (int i = 0; i < rightTitles.length; i++) {
            View convertView = LayoutInflater.from(QueryActivity.this).inflate(R.layout.match_title_layout, null);
            TextView title = convertView.findViewById(R.id.trusts_cell_name);
            title.setText(rightTitles[i]);
            layout.addView(convertView);
        }
        matchOperate_rightTitleScrollView.addView(layout);
    }
}

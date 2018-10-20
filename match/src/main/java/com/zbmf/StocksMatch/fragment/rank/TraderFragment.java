package com.zbmf.StocksMatch.fragment.rank;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.TradingAdapter;
import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.fragment.BaseFragment;
import com.zbmf.StocksMatch.listener.ITraderRankView;
import com.zbmf.StocksMatch.presenter.TraderRankPresenter;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * 操盘高手排行
 */
public class TraderFragment extends BaseFragment<TraderRankPresenter> implements ITraderRankView {
    @BindView(R.id.trader_title_layout)
    LinearLayout trader_title_layout;
    @BindView(R.id.list_view)
    ListViewForScrollView list_view;
    @BindView(R.id.trader_bottom_layout)
    LinearLayout trader_bottom_layout;
    @BindView(R.id.trading_scrollview)
    PullToRefreshScrollView trading_scrollview;

    private TradingAdapter tradingAdapter;

    public static TraderFragment newInstance(String matchID) {
        TraderFragment traderFragment = new TraderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.MATCH_ID, matchID);
        traderFragment.setArguments(bundle);
        return traderFragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_trader;
    }

    @Override
    protected void initView() {
        ShowOrHideProgressDialog.showProgressDialog(getActivity(),getActivity(),getString(R.string.hard_loading));
        trading_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //下拉刷新
        trading_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                ShowOrHideProgressDialog.showProgressDialog(getActivity(),getActivity(),getString(R.string.hard_loading));
                getPresenter().setFirst(true);
                getPresenter().getDatas();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        trading_scrollview.onRefreshComplete();
                    }
                }, 1200);
            }
        });
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Traders traders = (Traders) tradingAdapter.getItem(position);
                ShowActivity.showMatchTraderInfoActivity(getActivity(), traders);
            }
        });
    }

    @Override
    protected void initData() {
        tradingAdapter = new TradingAdapter(getActivity());
        list_view.setAdapter(tradingAdapter);
    }

    @Override
    protected TraderRankPresenter initPresent() {
        return new TraderRankPresenter();
    }


    @Override
    public void traderRank(List<Traders> traders) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (trading_scrollview.isRefreshing()) {
            trading_scrollview.onRefreshComplete();
        }
        if (tradingAdapter.getList() == null) {
            tradingAdapter.setList(traders);
        }
    }
}

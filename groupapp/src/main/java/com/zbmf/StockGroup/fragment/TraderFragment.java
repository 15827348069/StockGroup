package com.zbmf.StockGroup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.TraderDetailActivity;
import com.zbmf.StockGroup.adapter.TradingAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.Traders;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/11/9.
 */

public class TraderFragment extends BaseFragment {
    private TradingAdapter tradingAdapter;
    private ListViewForScrollView list_view;
    private PullToRefreshScrollView trading_scrollview;
    private List<Traders>infolst;
    private MatchInfo matchInfo;
    public static TraderFragment newInstance(MatchInfo matchInfo){
        TraderFragment fragment=new TraderFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
        fragment.setArguments(bundle);
        return fragment;
    }
    public void OnRushMatch(MatchInfo match){
        this.matchInfo=match;
    }
    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_stock_trading,null);
    }

    @Override
    protected void initView() {
        list_view=getView(R.id.list_view);
        trading_scrollview=getView(R.id.trading_scrollview);
        trading_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        trading_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getTrader();
            }
        });
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Traders traders= (Traders) tradingAdapter.getItem(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
                bundle.putSerializable(IntentKey.TRADER, traders);
                ShowActivity.showActivity(getActivity(), bundle,TraderDetailActivity.class);
            }
        });
    }

    @Override
    protected void initData() {
        if(getArguments()!=null){
            matchInfo= (MatchInfo) getArguments().getSerializable(IntentKey.MATCH_BAEN);
        }
        infolst=new ArrayList<>();
        tradingAdapter=new TradingAdapter(getActivity());
        tradingAdapter.setList(infolst);
        list_view.setAdapter(tradingAdapter);
        getTrader();
    }
    private void getTrader(){
        WebBase.traderRanks(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                trading_scrollview.onRefreshComplete();
                if(obj.has("traders")){
                    infolst.clear();
                    infolst.addAll( JSONParse.getTradersList(obj.optJSONArray("traders")));
                }
               if(infolst.size()>0){
                   tradingAdapter.notifyDataSetChanged();
               }
            }

            @Override
            public void onFailure(String err_msg) {
                trading_scrollview.onRefreshComplete();
            }
        });
    }
}

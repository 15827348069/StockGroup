package com.zbmf.StockGroup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.SimulateOneStockCommitActivity;
import com.zbmf.StockGroup.activity.StockBuyActivity;
import com.zbmf.StockGroup.activity.StockDetailActivity;
import com.zbmf.StockGroup.adapter.LatestTradeAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.interfaces.OnAdapterClickListener;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LatestTradeFragment extends BaseFragment implements OnAdapterClickListener {


    private PullToRefreshListView mPullToRefreshListView;
    private List<DealSys> list = new ArrayList<>();
    LatestTradeAdapter adapter;
    private int page, pages;
    private boolean isRush;
    private LinearLayout ll_none;
    private TextView no_message_text;
    private MatchInfo matchInfo;

    public LatestTradeFragment() {
    }

    public static LatestTradeFragment newInstance(MatchInfo matchInfo) {
        LatestTradeFragment fragment = new LatestTradeFragment();
        Bundle args = new Bundle();
        args.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchInfo= (MatchInfo) getArguments().getSerializable(IntentKey.MATCH_BAEN);
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_latest_trade, null);
    }

    @Override
    protected void initView() {
        mPullToRefreshListView = getView(R.id.listview);
        ll_none = getView(R.id.ll_none);
        no_message_text = getView(R.id.no_message_text);
        no_message_text.setText("暂无交易记录");
        adapter = new LatestTradeAdapter(getActivity(), list);
        adapter.setOnAdapterClickListener(this);
        mPullToRefreshListView.setAdapter(adapter);

        //跳转至个股评论汇总
        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.STOCKHOLDER, list.get(position-1));
                ShowActivity.showActivity(getActivity(), bundle, StockDetailActivity.class);
            }
        });
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                isRush = true;
                getDealSys();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getDealSys();
            }
        });
    }
    public  void scrollTop(){
        mPullToRefreshListView.ScrollTop();
    }
    public void OnRushMatch(MatchInfo match){
        this.matchInfo=match;
    }
    @Override
    protected void initData() {
        page = 1;
        getDealSys();
    }

    private void getDealSys() {
        WebBase.dealSys(matchInfo.getMatch_id(),page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                mPullToRefreshListView.onRefreshComplete();
                if (isRush) {
                    list.clear();
                    isRush = false;
                }
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    page = result.optInt("page");
                    pages = result.optInt("pages");
                    if (result.has("deals_sys")) {
                        list.addAll(JSONParse.getDealSysList(result.optJSONArray("deals_sys")));
                    }
                    if (list.size() > 0) {
                        adapter.notifyDataSetChanged();
                    }
                }
                if (pages == page && list.size() == 0) {
                    mPullToRefreshListView.setVisibility(View.GONE);
                    ll_none.setVisibility(View.VISIBLE);
                } else {
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                    ll_none.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String err_msg) {
                mPullToRefreshListView.onRefreshComplete();
                showToast(err_msg);
            }
        });
    }

    @Override
    public void onClickListener(View v, DealSys dealSys) {
        Bundle bundle =null;
        switch (v.getId()) {
            case R.id.tv_trade_follow_buy:
                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.TRADER_BUY+dealSys.getSumbol());
                break;
            case R.id.tv_follow_buy:
                bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                bundle.putSerializable(IntentKey.STOCKHOLDER, dealSys);
                ShowActivity.showActivity(getActivity(), bundle, StockBuyActivity.class);
                break;
            case R.id.tv_comment:
                bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                bundle.putSerializable(IntentKey.STOCKHOLDER, dealSys);
                ShowActivity.showActivity(getActivity(), bundle, SimulateOneStockCommitActivity.class);
                break;
        }
    }
}

package com.zbmf.StockGroup.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.StockDetailActivity;
import com.zbmf.StockGroup.adapter.StockRankAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.Yield;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.interfaces.OnAdapterClickListener;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StockRankFragment extends BaseFragment implements OnAdapterClickListener, View.OnClickListener {
    private TextView tv_update_time;
    private ListViewForScrollView mPullToRefreshListView, mWeekPullToRefreshListView;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private List<Yield> list = new ArrayList<>();
    private List<Yield> weeklist = new ArrayList<>();
    private StockRankAdapter adapter, weekadapter;
    private boolean day, week, isFirst;
    private MatchInfo matchInfo;
    private Button mOpenVIPBtn_week;
    private Button mOpenVIPBtn_day;
    private int page = 0;//分页加载的标记
    private int mIsVip;

    public StockRankFragment() {
    }

    public static StockRankFragment newInstance(MatchInfo matchInfo) {
        StockRankFragment fragment = new StockRankFragment();
        Bundle args = new Bundle();
        args.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchInfo = (MatchInfo) getArguments().getSerializable(IntentKey.MATCH_BAEN);
        }
    }


    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_stock_rank, null);
    }

    public void scrollTop() {
        mPullToRefreshScrollView.ScrollTop();
    }

    public void OnRushMatch(MatchInfo match) {
        this.matchInfo = match;
    }

    @Override
    protected void initView() {
        tv_update_time = getView(R.id.tv_update_date);
        mOpenVIPBtn_day = getView(R.id.openVIPBtn_day);
        mOpenVIPBtn_week = getView(R.id.openVIPBtn_week);
        mPullToRefreshListView = getView(R.id.listview);
        mWeekPullToRefreshListView = getView(R.id.week_listview);
        mPullToRefreshScrollView = getView(R.id.mPullToRefreshScrollView);
        adapter = new StockRankAdapter(getActivity(), list, 1);
        adapter.setAdapterClickListener(this);
        mOpenVIPBtn_week.setOnClickListener(this);
        mOpenVIPBtn_day.setOnClickListener(this);
        mPullToRefreshListView.setAdapter(adapter);
        weekadapter = new StockRankAdapter(getActivity(), weeklist, 7);
        weekadapter.setAdapterClickListener(this);
        mWeekPullToRefreshListView.setAdapter(weekadapter);
        mOpenVIPBtn_week.setVisibility(View.GONE);
        mOpenVIPBtn_day.setVisibility(View.GONE);
//        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                getRank(String.valueOf(page));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page += 1;
                getRank(String.valueOf(page));
                if (mOpenVIPBtn_day.getVisibility() == View.VISIBLE && mOpenVIPBtn_week.getVisibility() == View.VISIBLE) {
                    mOpenVIPBtn_week.setVisibility(View.GONE);
                    mOpenVIPBtn_day.setVisibility(View.GONE);
                }
            }
        });
        //判断当前用户是否开通了VIP会员,如果没有则页面不可上拉加载更多,否则提示用户可以上拉加载更多
        mIsVip = SettingDefaultsManager.getInstance().getIsVip();
        if (mIsVip == 1) {
//            mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);//可加载可刷新
            mOpenVIPBtn_day.setText(getString(R.string.load_more));
            mOpenVIPBtn_week.setText(getString(R.string.load_more));
            mOpenVIPBtn_day.setVisibility(View.VISIBLE);
            mOpenVIPBtn_week.setVisibility(View.VISIBLE);
        } else {
            mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);//只可下拉刷新
        }
    }

    private void showActivity(DealSys dealSys) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCKHOLDER, dealSys);
        ShowActivity.showActivity(getActivity(), bundle, StockDetailActivity.class);
    }

    @Override
    protected void initData() {
        isFirst = true;
        page = 1;
        getRank(String.valueOf(page));
    }

    private void getRank(final String page) {
        WebBase.getYieldList(page, new JSONHandler(isFirst, getActivity(), getString(R.string.loading)) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(JSONObject obj) {
                day = true;
                Rush();
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    tv_update_time.setText("数据最后更新时间" + DateUtil.getTime(obj.optLong("date") * 1000, Constants.MM_dd_HH_mm));
                    if (result.has("yields")) {
                        if (Integer.parseInt(page) == 1) {
                            list.clear();
                        }
                        list.addAll(JSONParse.getYieldList(result.optJSONArray("yields")));
                    }
                    if (list.size() > 0) {
                        mOpenVIPBtn_week.setVisibility(View.VISIBLE);
                        mOpenVIPBtn_day.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                }
                if (isFirst) {
                    isFirst = false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                day = true;
                Rush();
            }
        });
        WebBase.getYieldList("7", new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                week = true;
                Rush();
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    if (result.has("yields")) {
                        weeklist.clear();
                        weeklist.addAll(JSONParse.getYieldList(result.optJSONArray("yields")));
                    }
                    if (weeklist.size() > 0) {
                        weekadapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                week = true;
                Rush();
            }
        });
    }

    private void Rush() {
        if (day & week) {
            mPullToRefreshScrollView.onRefreshComplete();
        }
    }

    @Override
    public void onClickListener(View v, DealSys dealSys) {
        showActivity(dealSys);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openVIPBtn_day:
                if (mIsVip == 0) {
                    //跳转开通VIP页面
                    ShowActivity.skipVIPActivity(getActivity());
                } else {
                    //跳转到日排行榜页面
                    ShowActivity.skipMatchRank(getActivity(),1);
                }
                break;
            case R.id.openVIPBtn_week:
                if (mIsVip == 0) {
                    //跳转开通VIP页面
                    ShowActivity.skipVIPActivity(getActivity());
                } else {
                    //跳转到周排行榜
                    ShowActivity.skipMatchRank(getActivity(),7);
                }
                break;
        }
    }
}

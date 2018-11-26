package com.zbmf.StockGroup.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.StockRankAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.Yield;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.interfaces.OnAdapterClickListener;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 训练排行榜页面
 */
public class MatchRankActivity extends BaseActivity implements View.OnClickListener, OnAdapterClickListener {
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private TextView mTv_update_date;
    private Button mOpenVIPBtn;
    private boolean isFirst;
    private int page = 0;//分页加载的标记
    private List<Yield> list = new ArrayList<>();
    private StockRankAdapter adapter;
    private int mAnInt;
    /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_rank);
    }*/

    @Override
    public int getLayoutResId() {
        return R.layout.activity_match_rank;
    }

    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAnInt = extras.getInt(IntentKey.FLAG);
            if (mAnInt == 7) {
                initTitle("周榜");
            } else if (mAnInt == 1) {
                initTitle("日榜");
            }
        }
        mPullToRefreshScrollView = getView(R.id.mPullToRefreshScrollView);
        mTv_update_date = getView(R.id.tv_update_date);
        ListViewForScrollView listview = getView(R.id.listview);
        mOpenVIPBtn = getView(R.id.openVIPBtn);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new StockRankAdapter(this, list, mAnInt);
        adapter.setAdapterClickListener(this);
        listview.setAdapter(adapter);
        mOpenVIPBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        isFirst = true;
        page = 1;
        getRankList(String.valueOf(page), isFirst);
    }

    @Override
    public void addListener() {
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                isFirst = true;
                getRankList(String.valueOf(page), isFirst);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page += 1;
                isFirst = false;
                getRankList(String.valueOf(page), isFirst);
                if (mOpenVIPBtn.getVisibility() == View.VISIBLE) {
                    mOpenVIPBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getRankList(final String page, boolean is_first) {
        WebBase.getYieldList1(page, String.valueOf(mAnInt), new JSONHandler(is_first, this, getString(R.string.loading)) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    mTv_update_date.setText("数据最后更新时间" + DateUtil.getTime(obj.optLong("date") * 1000, Constants.MM_dd_HH_mm));
                    if (result.has("yields")) {
                        if (Integer.parseInt(page) == 1) {
                            list.clear();
                        }
                        list.addAll(JSONParse.getYieldList(result.optJSONArray("yields")));
                        adapter.notifyDataSetChanged();
                    }
                    if (list.size() > 0 && Integer.parseInt(page) == 1) {
                        mOpenVIPBtn.setVisibility(View.VISIBLE);
                    } else if (Integer.parseInt(page) > 1) {
                        mOpenVIPBtn.setVisibility(View.GONE);
                    }
                }
                if (isFirst) {
                    isFirst = false;
                }
                mPullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                mPullToRefreshScrollView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.openVIPBtn) {
            page += 1;
            getRankList(String.valueOf(page), isFirst);
            if (mOpenVIPBtn.getVisibility() == View.VISIBLE) {
                mOpenVIPBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClickListener(View v, DealSys dealSys) {
        showActivity(dealSys);
    }

    private void showActivity(DealSys dealSys) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCKHOLDER, dealSys);
        ShowActivity.showActivity(this, bundle, StockDetailActivity.class);
    }
}

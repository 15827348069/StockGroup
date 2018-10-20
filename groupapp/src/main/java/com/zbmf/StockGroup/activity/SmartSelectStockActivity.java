package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.HomeScreenAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SmartSelectStockActivity extends BaseActivity {

    private RecyclerView mHome_screen;
    private List<Screen> infolist;
    private HomeScreenAdapter adapter;
    private PullToRefreshScrollView mScrollview_screen;
    private LinearLayout mLl_none;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_smart_select_stock;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.smart_select_stock));
        mHome_screen = getView(R.id.home_screen);
        TextView no_message_text = getView(R.id.no_message_text);
        mLl_none = getView(R.id.ll_none);
        mScrollview_screen = getView(R.id.scrollview_screen);
        infolist = new ArrayList<>();
        no_message_text.setText("下拉刷新...");
        mHome_screen.setVisibility(View.GONE);
        mLl_none.setVisibility(View.VISIBLE);
        mHome_screen.setNestedScrollingEnabled(false);
        mHome_screen.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        getScreenList();

    }

    @Override
    public void initData() {
        adapter = new HomeScreenAdapter(this, infolist);
        mHome_screen.setAdapter(adapter);
        adapter.setItemClick(new HomeScreenAdapter.onItemClick() {
            @Override
            public void onItemClick(Screen screen,int flag) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.SCREEN, screen);
                bundle.putInt(IntentKey.FLAG, flag);
                ShowActivity.showActivityForResult(SmartSelectStockActivity.this,
                        bundle, ScreenDetailActivity.class, RequestCode.SCREEN);
            }
        });
        adapter.setSubscribe(new HomeScreenAdapter.SubscribeVIP() {
            @Override
            public void onSubscribeVIP(Screen screen, int flag) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.SCREEN, screen);
                bundle.putInt(IntentKey.FLAG,flag);
                ShowActivity.showActivityForResult(SmartSelectStockActivity.this,
                        bundle, ScreenDetailActivity.class, RequestCode.SCREEN);
            }
        });
        mScrollview_screen.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getScreenList();
            }
        });
    }

    @Override
    public void addListener() {

    }

    private void getScreenList() {
        WebBase.getScreenProducts(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result = obj.optJSONObject("result");
                mScrollview_screen.onRefreshComplete();
                if (!result.isNull("screen")) {
                    infolist.clear();
                    infolist.addAll(JSONParse.getScreenList(result.optJSONArray("screen")));
                    adapter.notifyDataSetChanged();
                    mLl_none.setVisibility(View.GONE);
                    mHome_screen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String err_msg) {
                mScrollview_screen.onRefreshComplete();
            }
        });
    }
}

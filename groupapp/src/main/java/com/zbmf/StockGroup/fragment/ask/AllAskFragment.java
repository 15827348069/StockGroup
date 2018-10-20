package com.zbmf.StockGroup.fragment.ask;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.AskStockSendActivity;
import com.zbmf.StockGroup.activity.CompanySecretaryActivity;
import com.zbmf.StockGroup.activity.StockDetailActivity;
import com.zbmf.StockGroup.adapter.AskDongAdapter;
import com.zbmf.StockGroup.adapter.StockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.AskBean;
import com.zbmf.StockGroup.beans.DongmiBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.listener.ScrollViewChangeListener;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.DrawableCenterTextView;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.OverscrollHelper;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao
 * on 2018/1/30.
 */

public class AllAskFragment extends BaseFragment implements View.OnClickListener, ScrollViewChangeListener {
    private ListViewForScrollView pull_listView;
    private AskDongAdapter askDongAdapter;
    private List<AskBean> askBeans;
    private int page;
    private AutoCompleteTextView autoCompleteTextView;
    private StockAdapter stockAdapter;
    private String searchKey;
    private ImageView imv_clear_et;
    private boolean isRush;
    private LinearLayout ll_no_setting;
    private TextView textView, dong_mi_btn;
    private PullToRefreshScrollView mRefreshView;
    private DrawableCenterTextView mTv_ask_stock;
    private DongmiBean mDongmiInfo;

    public static AllAskFragment newsIntance() {
        AllAskFragment fragment = new AllAskFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_all_ask, null);
    }

    @Override
    protected void initView() {
        pull_listView = getView(R.id.pull_scrollview);
        mRefreshView = getView(R.id.refreshView);
        final RelativeLayout searchViewR = getView(R.id.searchViewR);
        mTv_ask_stock = getView(R.id.tv_ask_stock);
        dong_mi_btn = getView(R.id.dong_mi_btn);
        dong_mi_btn.setVisibility(View.GONE);

        textView = getView(R.id.tv_qure);
        askDongAdapter = new AskDongAdapter(getActivity());
        pull_listView.setAdapter(askDongAdapter);
        //如果Android版本大于M
        new OverscrollHelper().setScrollViewChangeListener(this);
        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                isRush = true;
                getStockList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page += 1;
                getStockList();
            }
        });
        pull_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.STOCKHOLDER, askDongAdapter.getItem(position));
                ShowActivity.showActivity(getActivity(), bundle, StockDetailActivity.class);
            }
        });
        stockAdapter = new StockAdapter(getActivity());
        autoCompleteTextView = getView(R.id.aet_stock);
        autoCompleteTextView.setAdapter(stockAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchKey = stockAdapter.getSymbolAtPosition(position);
                isRush = true;
                page = 1;
                getStockList();
                if (!TextUtils.isEmpty(searchKey)) {
                    dong_mi_btn.setVisibility(View.VISIBLE);
                }
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    searchKey = null;
                    page = 1;
                    isRush = true;
                    textView.setSelected(false);
                    imv_clear_et.setVisibility(View.GONE);
                    getStockList();
                } else {
                    textView.setSelected(true);
                    imv_clear_et.setVisibility(View.VISIBLE);
                }
            }
        });
        ll_no_setting = getView(R.id.ll_no_setting);
        imv_clear_et = getView(R.id.imv_clear_et);
        imv_clear_et.setOnClickListener(this);
        getView(R.id.tv_qure).setOnClickListener(this);
        getView(R.id.tv_ask_stock).setOnClickListener(this);
        dong_mi_btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        askBeans = new ArrayList<>();
        page = 1;
        getStockList();
    }

    private boolean isFirst = true;

    private void getStockList() {
        WebBase.stockAskList(searchKey, page, new JSONHandler(isFirst, getActivity(), getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                onRushList();
                if (askBeans == null) {
                    askBeans = new ArrayList<>();
                }
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    if (isRush) {
                        askBeans.clear();
                    }
                    page = result.optInt("page");
                    if (result.has("asks")) {
                        askBeans.addAll(JSONParse.getAskBeans(result.optJSONArray("asks")));
                        askDongAdapter.setList(askBeans);
                    }
                    if (page == 1 && askBeans.size() == 0) {
                        ll_no_setting.setVisibility(View.VISIBLE);
                    } else {
                        ll_no_setting.setVisibility(View.GONE);
                    }
                    if (isRush) {
                        isRush = false;
                        mRefreshView.ScrollTop();
                    }
                    isFirst = false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                onRushList();
            }
        });
    }

    private void onRushList() {
        if (pull_listView != null && mRefreshView.isRefreshing()) {
            mRefreshView.onRefreshComplete();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_clear_et:
                autoCompleteTextView.setText("");
                searchKey = null;
                break;
            case R.id.tv_qure:
                isRush = true;
                page = 1;
                getStockList();
                break;
            case R.id.tv_ask_stock:
                if (ShowActivity.isLogin(getActivity())) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(IntentKey.FLAG, 2);
                    ShowActivity.showActivityForResult(getActivity(), bundle, AskStockSendActivity.class, RequestCode.ASK_DONG);
                }
                break;
            case R.id.dong_mi_btn:
                Intent intent = new Intent(getActivity(), CompanySecretaryActivity.class);
                intent.putExtra("symbol", searchKey);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onScrollChanged(PullToRefreshScrollView scrollView, int x, int y, int oldx, int oldy) {
    }

    @Override
    public void scrollTop() {
        if (mTv_ask_stock.getVisibility() == View.VISIBLE) {
            mTv_ask_stock.setVisibility(View.GONE);
        }
        if (dong_mi_btn.getVisibility() == View.VISIBLE) dong_mi_btn.setVisibility(View.GONE);
    }

    @Override
    public void scrollDown() {
        if (mTv_ask_stock.getVisibility() == View.GONE) {
            mTv_ask_stock.setVisibility(View.VISIBLE);
        }
        if (dong_mi_btn.getVisibility() == View.GONE) dong_mi_btn.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrollBottom() {}

    @Override
    public void onScroll(int x, int y) {
//        int top = mSearchViewR.getTop();
//        if (y > top) {
//            if (mSearchViewR.getVisibility() == View.VISIBLE) mSearchViewR.setVisibility(View.GONE);
//        } else {
//            if (mSearchViewR.getVisibility() == View.GONE) mSearchViewR.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void scrollStop() {
    }
}

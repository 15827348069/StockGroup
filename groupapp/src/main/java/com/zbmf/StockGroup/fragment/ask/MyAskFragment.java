package com.zbmf.StockGroup.fragment.ask;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.AskStockSendActivity;
import com.zbmf.StockGroup.activity.StockDetailActivity;
import com.zbmf.StockGroup.adapter.AskDongAdapter;
import com.zbmf.StockGroup.adapter.StockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Ask;
import com.zbmf.StockGroup.beans.AskBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2018/1/30.
 */

public class MyAskFragment extends BaseFragment implements View.OnClickListener {
    private PullToRefreshListView pull_listView;
    private AskDongAdapter askDongAdapter;
    private List<AskBean> askBeans;
    private int page;
    private boolean isRush;
    private RadioGroup radioGroup;
    private boolean isAll=true;
    private List<AskBean>askBeanList;
    private LinearLayout ll_no_setting;
    public static MyAskFragment newsIntance() {
        MyAskFragment fragment = new MyAskFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_my_ask, null);
    }

    @Override
    protected void initView() {
        pull_listView = getView(R.id.pull_scrollview);
        ll_no_setting=getView(R.id.ll_no_setting);
        askDongAdapter = new AskDongAdapter(getActivity());
        pull_listView.setAdapter(askDongAdapter);
        pull_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                isRush = true;
                getStockList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getStockList();
            }
        });
        pull_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(IntentKey.STOCKHOLDER, askDongAdapter.getItem(position-1));
                ShowActivity.showActivity(getActivity(),bundle,StockDetailActivity.class);
            }
        });
        getView(R.id.tv_ask_stock).setOnClickListener(this);
        radioGroup = getView(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radio_all:
                        isAll=true;
                        break;
                    case R.id.radio_replay:
                        isAll=false;
                        break;
                }
                arrowList();
            }
        });
        askBeanList=new ArrayList<>();
        askBeans = new ArrayList<>();
    }
    public void rushList(){
        page = 1;
        isRush = true;
        getStockList();
    }
    @Override
    protected void initData() {
        askBeans = new ArrayList<>();
        page = 1;
        getStockList();
    }
    private void arrowList(){
        askBeanList.clear();
        if(!isAll){
            for(AskBean askBean:askBeans){
                if(!askBean.getReply_content().isEmpty()){
                    askBeanList.add(askBean);
                }
            }
        }else{
            askBeanList.addAll(askBeans);
        }
        if(page==1&&askBeanList.size()==0){
            ll_no_setting.setVisibility(View.VISIBLE);
        }else{
            ll_no_setting.setVisibility(View.GONE);
        }
        askDongAdapter.setList(askBeanList);
    }
    private void getStockList() {
        WebBase.userList(page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                onRushList();
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    if (isRush) {
                        askBeans.clear();
                    }
                    page = result.optInt("page");
                    if (result.has("asks")) {
                        askBeans.addAll(JSONParse.getAskBeans(result.optJSONArray("asks")));
                        arrowList();
                    }
                    if(isRush){
                        isRush = false;
                        pull_listView.ScrollTop();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                onRushList();
            }
        });
    }

    private void onRushList() {
        if (pull_listView != null && pull_listView.isRefreshing()) {
            pull_listView.onRefreshComplete();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ask_stock:
                Bundle bundle = new Bundle();
                bundle.putInt(IntentKey.FLAG, 2);
                ShowActivity.showActivityForResult(getActivity(), bundle, AskStockSendActivity.class, RequestCode.ASK_DONG);
                break;
        }
    }
}

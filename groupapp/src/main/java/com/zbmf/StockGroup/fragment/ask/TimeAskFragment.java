package com.zbmf.StockGroup.fragment.ask;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.StockDetailActivity;
import com.zbmf.StockGroup.activity.TimeAskSettingActivity;
import com.zbmf.StockGroup.adapter.AskDongAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.AskBean;
import com.zbmf.StockGroup.beans.StockBean;
import com.zbmf.StockGroup.constans.Commons;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.dialog.TimeCheckPopWindow;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2018/1/30.
 */

public class TimeAskFragment extends BaseFragment implements View.OnClickListener, TimeCheckPopWindow.onItemClickListener<StockBean> {
    private PullToRefreshListView pull_listView;
    private AskDongAdapter askDongAdapter;
    private List<AskBean> askBeans;
    private int page;
    private boolean isRush;
    private RadioGroup radioGroup;
    private RadioButton allRadio,radioStock,radioTag;
    private int tagType;
    private String symbol,tagId;
    private TimeCheckPopWindow popWindow;
    private View ll_tag;
    private LinearLayout ll_no_setting;
    public static TimeAskFragment newsIntance() {
        TimeAskFragment fragment = new TimeAskFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_time_ask, null);
    }

    @Override
    protected void initView() {
        ll_tag=getView(R.id.ll_tag);
        pull_listView = getView(R.id.pull_scrollview);
        ll_no_setting=getView(R.id.ll_no_setting);
        askDongAdapter = new AskDongAdapter(getActivity());
        pull_listView.setAdapter(askDongAdapter);
        pull_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                isRush = true;
                getNoticeList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getNoticeList();
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

        getView(R.id.tv_ask_setting).setOnClickListener(this);
        radioGroup = getView(R.id.radio_group);
        allRadio=getView(R.id.radio_all);
        radioStock=getView(R.id.radio_stock);
        radioTag=getView(R.id.radio_key);
        radioStock.setOnClickListener(this);
        radioTag.setOnClickListener(this);
        popWindow=new TimeCheckPopWindow(getContext());
        popWindow.setOnItemClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radio_all:
                        tagType=0;
                        radioTag.setText("关键词");
                        radioStock.setText("个股");
                        if(popWindow.isShowing()){
                            popWindow.dismiss();
                        }
                        popWindow.setMessageType(-1);
                        getNoticeList();
                        break;
                }

            }
        });
        askBeans = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        ll_tag.post(new Runnable() {
            @Override
            public void run() {
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                int scrheight = wm.getDefaultDisplay().getHeight();
                int[] position = new int[2];
                ll_tag.getLocationOnScreen(position);
                popWindow.setHeight(scrheight-position[1]-5);
            }
        });
    }

    @Override
    protected void initData() {
        askBeans = new ArrayList<>();
        page = 1;
        getNoticeList();
    }
    public void rushList(){
        allRadio.toggle();
    }
    private void getNoticeList() {
        WebBase.noticeList(page,tagType,symbol,tagId,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                onRushList();
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    if (isRush) {
                        askBeans.clear();
                        isRush = false;
                    }
                    page = result.optInt("page");
                    if (result.has("notices")) {
                        askBeans.addAll(JSONParse.getAskBeans(result.optJSONArray("notices")));
                        askDongAdapter.setList(askBeans);
                    }
                    if(page==1&&askBeans.size()==0){
                        ll_no_setting.setVisibility(View.VISIBLE);
                    }else{
                        ll_no_setting.setVisibility(View.GONE);
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
            case R.id.tv_ask_setting:
                ShowActivity.showActivityForResult(getActivity(),null, TimeAskSettingActivity.class, RequestCode.ADD_SETTING);
                break;
            case R.id.radio_stock:
                tagType=1;
                if(popWindow.getMessageType()!=Commons.STOCK){
                    popWindow.setMessageType(Commons.STOCK);
                    popWindow.showAsDropDown(ll_tag);
                }else{
                    if(!popWindow.isShowing()){
                        popWindow.showAsDropDown(ll_tag);
                    }
                }
                break;
            case R.id.radio_key:
                tagType=2;
                if(popWindow.getMessageType()!=Commons.TAGKEY){
                    popWindow.setMessageType(Commons.TAGKEY);
                    popWindow.showAsDropDown(ll_tag);
                }else{
                    if(!popWindow.isShowing()){
                        popWindow.showAsDropDown(ll_tag);
                    }
                }
                break;
        }
    }
    @Override
    public void onItemClick(StockBean stockBean) {
        switch (tagType){
            case 1:
                symbol=stockBean.getF_symbol();
                radioStock.setText(stockBean.getF_symbolName());
                radioTag.setText("关键词");
                page=1;
                isRush=true;
                getNoticeList();
                break;
            case 2:
                tagId=stockBean.getF_symbol();
                radioTag.setText(stockBean.getF_symbolName());
                radioStock.setText("个股");
                page=1;
                isRush=true;
                getNoticeList();
                break;
        }
    }
    public void DissMissPop(){
        if(popWindow!=null&&popWindow.isShowing()){
            tagType=0;
            radioTag.setText("关键词");
            radioStock.setText("个股");
            if(popWindow.isShowing()){
                popWindow.dismiss();
            }
            popWindow.setMessageType(-1);
        }
    }
}

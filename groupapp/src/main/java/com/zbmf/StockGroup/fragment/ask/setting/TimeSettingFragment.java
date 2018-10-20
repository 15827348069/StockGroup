package com.zbmf.StockGroup.fragment.ask.setting;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.TimeSettingAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.StockBean;
import com.zbmf.StockGroup.constans.Commons;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2018/1/30.
 */

public class TimeSettingFragment extends BaseFragment {
    private PullToRefreshListView pull_listView;
    private TimeSettingAdapter timeSettingAdapter;
    private List<StockBean> stockBeans;
    private int page;
    private boolean isRush = true;
    private int flags;
    private TextDialog textDialog;
    private LinearLayout ll_no_setting;
    public static TimeSettingFragment newsIntance(int flag) {
        TimeSettingFragment fragment = new TimeSettingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.FLAG, flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_time_setting, null);
    }

    @Override
    protected void initView() {
        pull_listView = getView(R.id.pull_scrollview);
        ll_no_setting=getView(R.id.ll_no_setting);
        timeSettingAdapter = new TimeSettingAdapter(getActivity());
        pull_listView.setAdapter(timeSettingAdapter);
        pull_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                isRush = true;
                getMessage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getMessage();
            }
        });
        pull_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final StockBean stockBean = timeSettingAdapter.getItem(position - 1);
                if (textDialog == null) {
                    textDialog = TextDialog.createDialog(getActivity());
                    textDialog.setLeftButton(getString(R.string.cancel))
                            .setRightButton(getString(R.string.delete))
                    ;
                }
                textDialog.setRightClick(new DialogYesClick() {
                    @Override
                    public void onYseClick() {
                        switch (flags) {
                            case Commons.STOCK:
                                deleteStock(stockBean);
                                break;
                            case Commons.TAGKEY:
                                deleteTag(stockBean);
                                break;
                        }
                    }
                })
                        .setMessage("是否删除" + stockBean.getF_symbolName()).show();
            }
        });

    }

    private void deleteStock(final StockBean stockBean) {
        WebBase.delStock(stockBean.getF_symbol(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                showToast("删除成功");
                stockBeans.remove(stockBean);
                getActivity().setResult(Activity.RESULT_OK);
                timeSettingAdapter.setList(stockBeans);
                onRushListView();
            }

            @Override
            public void onFailure(String err_msg) {
                showToast("删除失败");
            }
        });
    }

    private void deleteTag(final StockBean stockBean) {
        WebBase.delTag(stockBean.getF_symbol(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                showToast("删除成功");
                getActivity().setResult(Activity.RESULT_OK);
                stockBeans.remove(stockBean);
                timeSettingAdapter.setList(stockBeans);
                onRushListView();
            }

            @Override
            public void onFailure(String err_msg) {
                showToast("删除失败");
            }
        });
    }

    @Override
    protected void initData() {
        stockBeans = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            flags = bundle.getInt(IntentKey.FLAG);
            page = 1;
            isRush = true;
            getMessage();
        }
    }

    public void onRushList() {
        isRush = true;
        page = 1;
        getMessage();
    }

    private void getMessage() {
        switch (flags) {
            case Commons.STOCK:
                getStockList();
                break;
            case Commons.TAGKEY:
                getTagList();
                break;
        }
    }

    private void getTagList() {
        WebBase.tagList(page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (isRush) {
                    stockBeans.clear();
                    isRush = false;
                }
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    page = result.optInt("page");
                    if (result.has("tags")) {
                        stockBeans.addAll(JSONParse.getTagList(result.optJSONArray("tags")));
                    }
                    timeSettingAdapter.setList(stockBeans);
                }
                onRushListView();
            }

            @Override
            public void onFailure(String err_msg) {
                onRushListView();
            }
        });
    }

    private void getStockList() {
        WebBase.stockList(page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (isRush) {
                    stockBeans.clear();
                    isRush = false;
                }
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    page = result.optInt("page");
                    if (result.has("stocks")) {
                        stockBeans.addAll(JSONParse.getStockBeanList(result.optJSONArray("stocks")));
                    }
                    timeSettingAdapter.setList(stockBeans);
                }
                onRushListView();
            }

            @Override
            public void onFailure(String err_msg) {
                onRushListView();
            }
        });
    }

    private void onRushListView() {
        if (pull_listView != null && pull_listView.isRefreshing()) {
            pull_listView.onRefreshComplete();
        }
        if(stockBeans.size()==0){
            if(ll_no_setting.getVisibility()==View.GONE){
                ll_no_setting.setVisibility(View.VISIBLE);
            }
        }else{
            if(ll_no_setting.getVisibility()==View.VISIBLE){
                ll_no_setting.setVisibility(View.GONE);
            }
        }
    }
}

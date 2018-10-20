package com.zbmf.StockGTec.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.MfbAdapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.MFBean;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/3/6.
 */

public class MfbFragment extends BaseFragment {
    private PullToRefreshListView mfb_list_view;
    private MfbAdapter adapter;
    private List<MFBean>infolist;
    private String month;
    private int page,pages;
    private static final String ARG_PARAM="SELECT_MOTH";
    public static MfbFragment newIntence( String str){
        MfbFragment fragment=new MfbFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, str);
        fragment.setArguments(args);
        return  fragment;
    }
    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.mfb_layout,null);
    }

    @Override
    protected void initView() {
        mfb_list_view=getView(R.id.mfb_list_view);
        mfb_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mfb_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                page+=1;
                getMFBmessage();
            }
        });
    }

    @Override
    protected void initData() {
        if(getArguments()!=null){
            month=getArguments().getString(ARG_PARAM);
            infolist=new ArrayList<>();
            adapter=new MfbAdapter(getContext(),infolist);
            mfb_list_view.setAdapter(adapter);
            getMFBmessage();
        }
    }
    public void getMFBmessage(){
        WebBase.payLogs(month, page, new JSONHandler(getActivity()) {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                JSONArray pay_logs=result.optJSONArray("pay_logs");
                int size=pay_logs.length();
                for(int i=0;i<size;i++){
                    JSONObject pay=pay_logs.optJSONObject(i);
                    MFBean mfBean=new MFBean();
                    mfBean.setDate(pay.optString("created_at"));
                    mfBean.setTitle(pay.optString("notes"));
                    mfBean.setCount(pay.optString("pay"));
                    mfBean.setDesc(pay.optString("desc"));
                    infolist.add(mfBean);
                }
                adapter.notifyDataSetChanged();
                mfb_list_view.onRefreshComplete();
                if(page==pages){
                    mfb_list_view.setMode(PullToRefreshBase.Mode.DISABLED);
                    showToast(getString(R.string.already_add_all));
                }
            }

            @Override
            public void onFailure(String err_msg) {
                mfb_list_view.onRefreshComplete();
            }
        });
    }
}

package com.zbmf.StockGTec.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.SystemMessageAdapter;
import com.zbmf.StockGTec.beans.SystemMessage;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/3/6.
 */

public class MessageFragment extends BaseFragment {
    private PullToRefreshListView messagelist;
    private List<SystemMessage>infolist;
    private SystemMessageAdapter adapter;
    private String select_type;
    private static final String ARG_PARAM1="SELECT_TYPE";
    private int page,pages;
    public static MessageFragment newIntence(String type){
        MessageFragment fragment=new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return  fragment;
    }
    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.message_fragment_layout,null);
    }

    @Override
    protected void initView() {
        infolist=new ArrayList<>();
        adapter=new SystemMessageAdapter(getContext(),infolist);
        messagelist=getView(R.id.message_list);
        messagelist.setAdapter(adapter);
        messagelist.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        messagelist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if(page==pages){
//                    showToast("已加载全部数据");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messagelist.onRefreshComplete();
                        }
                    },200);
                }else{
                    page+=1;
                    getData();
                }
            }
        });
    }

    @Override
    protected void initData() {
        if(getArguments()!=null){
            select_type=getArguments().getString(ARG_PARAM1);
        }
        page=1;
        pages=3;
        infolist.clear();
        getData();
    }
    public void getData(){
        int size=infolist.size();
        switch (select_type){
            case "全部":
                for(int i=size;i<20*page;i++){
                    SystemMessage sm=new SystemMessage();
                    if(i%2==0){
                        sm.setAvatar("http://up.qqya.com/allimg/201710-t/17-101803_106599.jpg");
                        sm.setCountent(i+"张三评论了您的博文");
                        sm.setMessage_type("系统通知");
                    }else{
                        sm.setAvatar("http://tupian.qqjay.com/tou2/2017/0119/455fb370186003c89aff34213e4453a1.jpg");
                        sm.setCountent(i+"李四续费了铁粉");
                        sm.setMessage_type("铁粉");
                    }
                    sm.setDate("今天");
                    infolist.add(sm);
                }
                break;
            case "系统通知":
                for(int i=size;i<20*page;i++){
                    SystemMessage sm=new SystemMessage();
                    sm.setAvatar("http://up.qqya.com/allimg/201710-t/17-101803_106599.jpg");
                    sm.setCountent(i+"张三评论了您的博文");
                    sm.setMessage_type("系统通知");
                    infolist.add(sm);
                }
                break;
            case "铁粉":
                for(int i=size;i<20*page;i++){
                    SystemMessage sm=new SystemMessage();
                    sm.setAvatar("http://tupian.qqjay.com/tou2/2017/0119/455fb370186003c89aff34213e4453a1.jpg");
                    sm.setCountent(i+"李四续费了铁粉");
                    sm.setMessage_type("铁粉");
                    infolist.add(sm);
                }
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                messagelist.onRefreshComplete();
            }
        },100);
    }
}

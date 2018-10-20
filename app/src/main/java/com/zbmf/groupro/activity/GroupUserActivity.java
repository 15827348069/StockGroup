package com.zbmf.groupro.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.GroupUserAdapter;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.view.ListViewForScrollView;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuhao on 2016/12/14.
 */

public class GroupUserActivity extends Activity {
    private PullToRefreshScrollView main_scrollview;
    private GroupUserAdapter adapter;
    private ListViewForScrollView group_user_list;
    private List<Group>infolist;
    private SimpleDateFormat dataformat;
    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_user_activity);
        init();
        getGroupUser(true);
    }

    private void init() {
        main_scrollview= (PullToRefreshScrollView) findViewById(R.id.have_scrollview);
        group_user_list= (ListViewForScrollView) findViewById(R.id.group_user_list);
        infolist=new ArrayList<>();
        adapter=new GroupUserAdapter(GroupUserActivity.this,infolist);
        group_user_list.setAdapter(adapter);
        main_scrollview.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
        main_scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在刷新数据...");
        main_scrollview.getLoadingLayoutProxy().setReleaseLabel("松开刷新数据...");
        //上拉、下拉设定
        main_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        main_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                infolist.clear();
                getGroupUser(false);
            }
        });
    }
    public void getGroupUser(boolean show_dialog){

    }
    public void RushScrollview(){
        if(dataformat==null){
            dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        }
        if(date==null){
            date=new Date();
        }
        if(main_scrollview!=null){
            main_scrollview.getLoadingLayoutProxy().setLastUpdatedLabel("上次更新时间："+dataformat.format(date));
            main_scrollview.onRefreshComplete();
        }
        date=null;
        dataformat=null;
    }
}

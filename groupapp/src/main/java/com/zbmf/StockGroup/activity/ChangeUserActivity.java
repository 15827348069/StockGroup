package com.zbmf.StockGroup.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.GroupActivity;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ChangeUserAdapter;
import com.zbmf.StockGroup.beans.User;
import com.zbmf.StockGroup.db.DBManager;
import com.zbmf.StockGroup.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;


public class ChangeUserActivity extends BaseActivity {
    private ChangeUserAdapter adapter;
    private ListView change_user_list_view;
    private List<User>infolist;
    private DBManager dbManager;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_change_user;
    }

    @Override
    public void initView() {
        initTitle("切换用户");
        change_user_list_view= (ListView) findViewById(R.id.change_user_list_view);
    }

    @Override
    public void initData() {
        dbManager=new DBManager(ChangeUserActivity.this);
        infolist=new ArrayList<>();
        adapter=new ChangeUserAdapter(ChangeUserActivity.this,infolist);
        change_user_list_view.setAdapter(adapter);
        getUserMessage();
    }

    @Override
    public void addListener() {
        change_user_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dbManager.addUser(infolist.get(i));
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void getUserMessage(){
        infolist.addAll(dbManager.queryUser());
        adapter.notifyDataSetChanged();
    }
}

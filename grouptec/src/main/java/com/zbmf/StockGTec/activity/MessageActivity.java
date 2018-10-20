package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGTec.fragment.MessageFragment;

import java.util.ArrayList;
import java.util.List;


public class MessageActivity extends ExActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private List<Fragment> mList;
    private List<String> title_list;
    private PagerAdapter adapter;
    private SlidingTabLayout tablayout;
    private TextView group_title_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        initFragment();
        init();
    }
    public void initFragment(){
        title_list=new ArrayList<>();
        mList=new ArrayList<>();
        title_list.add("全部");
        title_list.add("系统通知");
        title_list.add("铁粉");
        for(int i=0;i<title_list.size();i++){
            mList.add(MessageFragment.newIntence(title_list.get(i)));
        }
    }
    private void init() {
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        group_title_name.setVisibility(View.VISIBLE);
        group_title_name.setText("消息中心");
        findViewById(R.id.group_title_return).setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tablayout = (SlidingTabLayout)findViewById(R.id.tablayout);
        adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        viewpager.setAdapter(adapter);
        tablayout.setViewPager(viewpager);
        viewpager.setOffscreenPageLimit(title_list.size());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_title_return:
                finish();
                break;
        }
    }
}

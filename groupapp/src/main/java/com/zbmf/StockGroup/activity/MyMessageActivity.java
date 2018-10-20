package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.fragment.HuiFuFragment;
import com.zbmf.StockGroup.fragment.SystemMessage;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.R.id.viewpager;


public class MyMessageActivity extends BaseActivity implements OnTabSelectListener {

    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        initFragment();
        setupView();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListener() {

    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("回复");
        title_list.add("系统消息");
        mList.add(HuiFuFragment.newInstance());
        mList.add(SystemMessage.newInstance());
    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("消息");
        tv_title.setVisibility(View.VISIBLE);
        mTab = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewpager = (ViewPager) findViewById(viewpager);

        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mTab.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}

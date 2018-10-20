package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.teacher.RankingFragment;
import com.zbmf.StockGroup.interfaces.TeacherToStudy;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.ShowActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuhao on 2017/8/16.
 */

public class FindTeacherActivity extends BaseActivity implements OnTabSelectListener, TeacherToStudy, View.OnClickListener {
    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_find_teacher;
    }

    @Override
    public void initView() {
        initFragment();
        setupView();
    }

    @Override
    public void initData() {
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(mList.size());
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            int flag=bundle.getInt(IntentKey.FLAG);
            mViewpager.setCurrentItem(flag, false);
        }
    }

    @Override
    public void addListener() {
        mTab.setOnTabSelectListener(this);
    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list =  Arrays.asList(getResources().getStringArray(R.array.find_teacher));
        mList.add(RankingFragment.newInstance(Constants.PEOPLE_RECOMED));
        mList.add(RankingFragment.newInstance(Constants.NOW_LIVE).setTeacherToStudy(this));
        mList.add(RankingFragment.newInstance(Constants.EXCLUSIVE));
        mList.add(RankingFragment.newInstance(Constants.PEOPLE_ARROW));
    }

    private void setupView() {
        initTitle("圈子");
        getSearchButton().setOnClickListener(this);
        mTab = (SlidingTabLayout) findViewById(R.id.teacher_tab_layout);
        mViewpager = (ViewPager) findViewById(R.id.viewpager_teacher);
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void toStudy() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:
                ShowActivity.showActivity(FindTeacherActivity.this,SearchActivity.class);
                break;
        }
    }
}

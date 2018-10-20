package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.fragment.commit.CommitVideoFragment;
import com.zbmf.StockGroup.fragment.commit.MyTraderFragment;
import com.zbmf.StockGroup.fragment.commit.TieFansFragment;
import com.zbmf.StockGroup.interfaces.TeacherToStudy;
import com.zbmf.StockGroup.constans.Constants;
//import com.zbmf.StockGroup.fragment.TieFansFragment;
//import com.zbmf.StockGroup.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.R.id.viewpager;

/**
 * 我的订阅 -- 铁粉、视频。
 */
public class MySubscripActivity extends BaseActivity implements OnTabSelectListener, TeacherToStudy {

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
        initTitle("我的订阅");
        initFragment();
        mTab = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewpager = (ViewPager) findViewById(viewpager);
        mTab.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(mList.size());
        Intent answer_intent=new Intent(Constants.USER_RED_NEW_MESSAGE);
        answer_intent.putExtra("type", Constants.PUSH_BOX_TYPE);
        this.sendBroadcast(answer_intent);
    }

    @Override
    public void addListener() {
        mTab.setOnTabSelectListener(this);
    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("铁粉");
        title_list.add("视频");
        title_list.add("操盘高手");
        TieFansFragment unusedFragment = TieFansFragment.newInstance();
        CommitVideoFragment usedFragment = CommitVideoFragment.newInstance();
        usedFragment.setTeacherToStudy(this);
        mList.add(unusedFragment);
        mList.add(usedFragment);
        mList.add(MyTraderFragment.newInstance());
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
}

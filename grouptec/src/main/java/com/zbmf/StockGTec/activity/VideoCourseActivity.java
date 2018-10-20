package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGTec.fragment.VideoLiveFragment;

import java.util.ArrayList;
import java.util.List;

public class VideoCourseActivity extends ExActivity implements View.OnClickListener, OnTabSelectListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private List<Fragment> mList;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_course);

        TextView tv_title = (TextView) findViewById(R.id.group_title_name);
        tv_title.setText("视频课程");tv_title.setVisibility(View.VISIBLE);
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
//        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        tv_right.setOnClickListener(this);
        tv_right.setText("专辑");

        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("直播");
        title_list.add("录播");
        VideoLiveFragment videoLiveFragment = VideoLiveFragment.newInstance(1);
        VideoLiveFragment videoRecordFragment = VideoLiveFragment.newInstance(0);
        mList.add(videoLiveFragment);
        mList.add(videoRecordFragment);
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
//        tableLayout.setupWithViewPager(viewpager);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnTabSelectListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_title_return:
                finish();
                break;
            case R.id.tv_right:

                break;
        }
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

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
import com.zbmf.StockGTec.beans.Videos;
import com.zbmf.StockGTec.fragment.VideoChatFragment;
import com.zbmf.StockGTec.fragment.VideoInfoFragment;

import java.util.ArrayList;
import java.util.List;

public class VideoLiveActivity extends ExActivity implements ViewPager.OnPageChangeListener, OnTabSelectListener {

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private List<Fragment> mList;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_live);

        Videos mVideos = (Videos) getIntent().getSerializableExtra("video");
        TextView tv_title = (TextView) findViewById(R.id.group_title_name);
        TextView tv_video_title = (TextView) findViewById(R.id.tv_video_title);
        tv_title.setText("直播视频管理");
        tv_title.setVisibility(View.VISIBLE);
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("视频信息");
        title_list.add("观众群聊");
        tv_video_title.setText(mVideos.getTitle());
        VideoInfoFragment videoInfoFragment = VideoInfoFragment.newInstance(mVideos);
        VideoChatFragment chatFragment = VideoChatFragment.newInstance(mVideos);

        mList.add(videoInfoFragment);
        mList.add(chatFragment);
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
//        tableLayout.setupWithViewPager(viewpager);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnTabSelectListener(this);

        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }
}

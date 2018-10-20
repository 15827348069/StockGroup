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
import com.zbmf.StockGroup.fragment.UserCollectsBlogFragment;
import com.zbmf.StockGroup.fragment.UserCollectsVedioFragment;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.R.id.viewpager;

//import com.zbmf.StockGroup.fragment.TieFansFragment;
//import com.zbmf.StockGroup.fragment.VideoFragment;

/**
 * 我的订阅 -- 铁粉、视频。
 */
public class UserCollectsActivity extends BaseActivity implements OnTabSelectListener {

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
        initTitle("我的收藏");
        setupView();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListener() {
        mTab.setOnTabSelectListener(this);
    }

    private void setupView() {
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("文章");
//        title_list.add("视频");
        mList.add(UserCollectsBlogFragment.newInstance());
//        mList.add(UserCollectsVedioFragment.newInstance());
        mTab = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewpager = (ViewPager) findViewById(viewpager);
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mTab.setVisibility(View.GONE);
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }
}

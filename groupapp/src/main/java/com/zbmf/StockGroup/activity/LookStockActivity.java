package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.api.AppUrl;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.BlogFragment;
import com.zbmf.StockGroup.fragment.WebFragment;
import com.zbmf.StockGroup.constans.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zbmf.StockGroup.R.id.viewpager;

/**
 * 看股市
 */
public class LookStockActivity extends BaseActivity {

    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;
    private int flag;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        initTitle("看股市");
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
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            flag=bundle.getInt(IntentKey.FLAG);
            mViewpager.setCurrentItem(flag, false);
        }
    }

    @Override
    public void addListener() {

    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list =  Arrays.asList(getResources().getStringArray(R.array.look_stock));
        mList.add(WebFragment.newInstance(HtmlUrl.STOCK_LIVE));
        mList.add(BlogFragment.newInstance(Constants.MFTT));
        mList.add(BlogFragment.newInstance(Constants.ZBMFTT));
    }

}

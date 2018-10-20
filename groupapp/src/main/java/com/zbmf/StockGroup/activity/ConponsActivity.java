package com.zbmf.StockGroup.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.api.AppUrl;
import com.zbmf.StockGroup.beans.CouponsBean;
import com.zbmf.StockGroup.fragment.conpons.CanUseConponsFragment;
import com.zbmf.StockGroup.fragment.conpons.LoseTimeConponsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.R.id.viewpager;

public class ConponsActivity extends BaseActivity implements OnTabSelectListener {
    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_coupon;
    }

    @Override
    public void initView() {
        initTitle("优惠券");
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
        title_list.add("已领取");
        title_list.add("已使用");
        title_list.add("已过期");
        mList.add(CanUseConponsFragment.newInstance());
        mList.add(LoseTimeConponsFragment.newInstance(AppUrl.getHistCoupons));
        mList.add(LoseTimeConponsFragment.newInstance(AppUrl.getExpireCoupons));
    }

    private void setupView() {
        mTab = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewpager = (ViewPager) findViewById(viewpager);
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mTab.setOnTabSelectListener(this);
        findViewById(R.id.to_get_conpons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"领券中心敬请期待",Toast.LENGTH_SHORT).show();
            }
        });
        mViewpager.setOffscreenPageLimit(mList.size());
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }
}

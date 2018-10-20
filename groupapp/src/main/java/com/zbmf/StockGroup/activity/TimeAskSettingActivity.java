package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.constans.Commons;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.fragment.ask.setting.TimeSettingFragment;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.ShowActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuhao on 2018/1/31.
 */

public class TimeAskSettingActivity extends BaseActivity implements View.OnClickListener {
    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;
    private TimeSettingFragment stockFragment, tagFragment;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_time_setting_layout;
    }

    @Override
    public void initView() {
        initTitle("监控管理");
        mTab = getView(R.id.teacher_tab_layout);
        mViewpager = getView(R.id.viewpager_teacher);
        getView(R.id.tv_ask_setting).setOnClickListener(this);
        initFragment();
    }

    @Override
    public void initData() {
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(mList.size());
    }

    @Override
    public void addListener() {

    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = Arrays.asList(getResources().getStringArray(R.array.time_setting));
        stockFragment = TimeSettingFragment.newsIntance(Commons.STOCK);
        tagFragment = TimeSettingFragment.newsIntance(Commons.TAGKEY);
        mList.add(stockFragment);
        mList.add(tagFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ask_setting:
                ShowActivity.showActivityForResult(this, null, TimeSettingAddActivity.class, RequestCode.ADD_SETTING);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        if (resultCode == RESULT_OK && requestCode == RequestCode.ADD_SETTING) {
            int flag = data.getIntExtra(IntentKey.FLAG, 0);
            switch (flag) {
                case Commons.STOCK:
                    stockFragment.onRushList();
                    break;
                case Commons.TAGKEY:
                    tagFragment.onRushList();
                    break;
            }
        }
    }
}

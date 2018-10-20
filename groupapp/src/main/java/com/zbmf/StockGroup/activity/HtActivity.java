package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.HtPagerAdapter;
import com.zbmf.StockGroup.beans.Types;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.GzFragment;
import com.zbmf.StockGroup.fragment.HtFragment;
import com.zbmf.StockGroup.utils.ShowActivity;

import java.util.ArrayList;

public class HtActivity extends BaseActivity {

    private ArrayList<Types> mTypesList;
    private ArrayList<Fragment> mFragmentList;
    private int mNo_read_msg_count;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_ht;
    }

    @Override
    public void initView() {
        initTitle("话题");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTypesList = extras.getParcelableArrayList(IntentKey.TYPES);
            mNo_read_msg_count = extras.getInt("no_read_msg_count");
        }
        TabLayout tab_layout_menu = getView(R.id.tab_layout_menu);
        ViewPager vp = getView(R.id.vp);
        FrameLayout rightTipR = getView(R.id.rightTipR);
        Button group_title_right_button = getView(R.id.group_title_right_button);
        TextView msgCount = getView(R.id.msgCount);
        if (mNo_read_msg_count>0){
            msgCount.setText(String.valueOf(mNo_read_msg_count));
            msgCount.setVisibility(View.VISIBLE);
        }else {
            msgCount.setVisibility(View.GONE);
        }
        group_title_right_button.setText("消息");
        rightTipR.setVisibility(View.VISIBLE);

        mFragmentList = new ArrayList<>();
        GzFragment gzFragment = GzFragment.GzInstance();
        mFragmentList.add(gzFragment);
        for (int i = 0; i < mTypesList.size(); i++) {
            HtFragment htFragment = HtFragment.HtInstance(mTypesList.get(i).getId());
            mFragmentList.add(htFragment);
        }

        HtPagerAdapter htPagerAdapter = new HtPagerAdapter(getSupportFragmentManager(), mFragmentList, mTypesList);
        vp.setAdapter(htPagerAdapter);

        tab_layout_menu.setupWithViewPager(vp);
        vp.setCurrentItem(1);
        tab_layout_menu.getTabAt(1).select();

        group_title_right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.skipNoReadActivity(HtActivity.this);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListener() {

    }
}

package com.zbmf.StockGroup.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pq
 * on 2018/7/6.
 */

public class TopicCommentTabAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> mFragmentList;
    private List<String> mTitles;
    public TopicCommentTabAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, List<String> titles) {
        super(fm);
        mFragmentList=fragmentList;
        mTitles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}

package com.zbmf.worklibrary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xuhao on 2017/2/14.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> infolist;
    public ViewPageAdapter(FragmentManager fm, List<Fragment> infolist) {
        super(fm);
        this.infolist=infolist;
    }

    @Override
    public Fragment getItem(int position) {
        return infolist.get(position);
    }

    @Override
    public int getCount() {
        return infolist.size();
    }

}

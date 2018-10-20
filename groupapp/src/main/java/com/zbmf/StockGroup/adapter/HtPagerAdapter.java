package com.zbmf.StockGroup.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zbmf.StockGroup.beans.Types;

import java.util.ArrayList;

/**
 * Created by pq
 * on 2018/7/2.
 */

public class HtPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> mFragmentList;
    /*private ArrayList<Types> mTypesList;*/
    private ArrayList<String> strList=new ArrayList<>();

    public HtPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList,ArrayList<Types> typesList) {
        super(fm);
        mFragmentList = fragmentList;
      /*  mTypesList=typesList;*/
        strList.add("关注");
        for (int i = 0; i < typesList.size(); i++) {
            strList.add(typesList.get(i).getName());
        }
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
        return strList.get(position);
    }
}

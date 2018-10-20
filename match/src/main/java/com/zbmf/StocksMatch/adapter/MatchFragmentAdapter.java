package com.zbmf.StocksMatch.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zbmf.StocksMatch.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xuhao
 * on 2017/11/28.
 */

public class MatchFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment>infolist;
    private List<String>tagTitle;
    public MatchFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        tagTitle= Arrays.asList(context.getResources().getStringArray(R.array.match_tag));
    }
    public MatchFragmentAdapter(Context context, FragmentManager fm,List<Fragment>infolist,List<String>tagTitle) {
        super(fm);
        this.tagTitle= tagTitle;
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

    @Override
    public CharSequence getPageTitle(int position) {
        if(tagTitle!=null){
            return tagTitle.get(position);
        }else{
            return null;
        }

    }
}

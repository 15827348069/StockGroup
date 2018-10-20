package com.zbmf.groupro.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by iMac on 2016/12/26.
 */

public class GiftAdapter1 extends PagerAdapter {

    private List<GridView> mGridViews;
    private Activity cxt;

    public GiftAdapter1(Activity activity,List<GridView> gridViews){
        cxt = activity;
        mGridViews=  gridViews;
    }
    @Override
    public int getCount() {
        return mGridViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View gridView = mGridViews.get(position);
        container.addView(gridView);
        return gridView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

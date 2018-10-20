package com.zbmf.worklibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xuhao on 2017/3/10.
 */

public class ViewPageNoScroll extends ViewPager {
    public ViewPageNoScroll(Context context) {
        super(context);
    }

    public ViewPageNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}

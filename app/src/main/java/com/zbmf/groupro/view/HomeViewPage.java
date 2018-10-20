package com.zbmf.groupro.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xuhao on 2017/3/10.
 */

public class HomeViewPage extends ViewPager{
    private int childId;
    public HomeViewPage(Context context) {
        super(context);
    }

    public HomeViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        if (childId > 0) {
//            View scroll = findViewById(childId);
//            if (scroll != null) {
//                Rect rect = new Rect();
//                scroll.getHitRect(rect);
//                if (rect.contains((int) event.getX(), (int) event.getY())) {
//
//                }
//            }
//        }
//        return super.onInterceptTouchEvent(event);
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}

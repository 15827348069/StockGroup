package com.zbmf.StockGroup.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

 //不滚动的viewpager
public class NoViewPager extends ViewPager {

	public NoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoViewPager(Context context) {
		super(context);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}

}

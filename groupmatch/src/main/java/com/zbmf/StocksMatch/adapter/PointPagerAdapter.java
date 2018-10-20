package com.zbmf.StocksMatch.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PointPagerAdapter extends PagerAdapter {
	private ArrayList<ImageView> imageList;
	
	public PointPagerAdapter(ArrayList<ImageView> imageList) {
		this.imageList = imageList;
	}

	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageview = imageList.get(position);
		container.addView(imageview);
		// 返回一个和该view相对的object
		return imageview;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		object = null;
	}

	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}

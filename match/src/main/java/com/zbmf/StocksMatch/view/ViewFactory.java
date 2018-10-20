package com.zbmf.StocksMatch.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 *
	 * @param
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		Glide.with(context).load(url).apply(GlideOptionsManager.getInstance().getBannerOptions(0)).into(imageView);
		return imageView;
	}
}

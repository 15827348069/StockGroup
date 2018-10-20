package com.zbmf.groupro.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;

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
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_image)  // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_image)       // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
//	            .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
				.showStubImage(R.drawable.banner_loading)     // 设置图片下载期间显示的图片
				.showImageOnFail(R.drawable.default_image)
				.build();                                   // 创建配置过得DisplayImageOption对象
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		ImageLoader.getInstance().displayImage(url,imageView,options);
		return imageView;
	}

}

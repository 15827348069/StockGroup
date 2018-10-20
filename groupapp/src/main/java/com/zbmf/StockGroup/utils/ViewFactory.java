package com.zbmf.StockGroup.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zbmf.StockGroup.R;

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
	public static ImageView getImgView(Context context,String url){
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		Glide.with(context).load(url).into(imageView).onLoadStarted(context.getResources().getDrawable(R.drawable.default_image));
		return imageView;
	}
	public static ImageView getImgView(Context context,String url,ImageView imageView){
//		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		Glide.with(context).load(url).into(imageView).onLoadStarted(context.getResources().getDrawable(R.drawable.dia_loading));
		return imageView;
	}
	public static ImageView getRoundImgView(Context context,String url){
//		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		ImageView imageView = new ImageView(context);
//		ImageLoader.getInstance().displayImage(url, imageView, ImageLoaderOptions.ProgressOptions());
		RequestOptions options = new RequestOptions();
		options.transform(new GlideRoundTransform(context,3/*DisplayUtil.dip2px(context,5f)*/));
		Glide.with(context).load(url).apply(options)
				.into(imageView);
		return imageView;
	}
	public static void getRoundImgView(Context context,String url,ImageView img){
		RequestOptions options = new RequestOptions();
		options.transform(new GlideRoundTransform(context,3/*DisplayUtil.dip2px(context,5f)*/));
		Glide.with(context).load(url)
				.apply(options)
				.into(img);
	}

	/**
	 * 圆形图片
	 */
	public static void imgCircleView(Context context,String url,ImageView imageView){
		RequestOptions options = new RequestOptions();
		options.transform(new GlideCircleTransform(context));
		Glide.with(context).load(url)
				.apply(options)
				.into(imageView).onLoadStarted(context.getResources().getDrawable(R.drawable.avatar_default));
	}

	public static ImageView getImageView(Context context, String url) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_image)  // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_image)       // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)   // 设置下载的图片是否缓存在内存中
				.showImageOnLoading(R.drawable.banner_loading)
				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
				.showStubImage(R.drawable.banner_loading)     // 设置图片下载期间显示的图片
				.showImageOnFail(R.drawable.default_image)
				.build();                                   // 创建配置过得DisplayImageOption对象
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		ImageLoader.getInstance().displayImage(url,imageView,options);
		return imageView;
	}
	public static ImageView getLanchImageView(Context context, String url) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
				.build();                                   // 创建配置过得DisplayImageOption对象
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		ImageLoader.getInstance().displayImage(url,imageView,options);
		return imageView;
	}
	public static ImageView getActivityImageView(Context context, String url) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_image)  // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_image)       // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false)                        // 设置下载的图片是否缓存在内存中
				.showImageOnLoading(R.drawable.banner_loading)
				.cacheOnDisk(false)// 设置下载的图片是否缓存在SD卡中
	            .displayer(/*new RoundedVignetteBitmapDisplayer(20,0)*/new RoundedBitmapDisplayer(20))  // 设置成圆角图片
				.showStubImage(R.drawable.banner_loading)     // 设置图片下载期间显示的图片
				.showImageOnFail(R.drawable.default_image)
				.build();                                   // 创建配置过得DisplayImageOption对象
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		ImageLoader.getInstance().displayImage(url,imageView,options);
		return imageView;
	}
}

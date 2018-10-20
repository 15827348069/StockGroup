package com.zbmf.groupro;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zbmf.groupro.utils.FileUtils;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.utils.SettingDefaultsManager;

import java.io.File;

/**
 * Created by xuhao on 2016/12/12.
 */

public class GroupApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        SettingDefaultsManager.getInstance().setSharedPreferences(this);
        ImageLoader.getInstance().init(getImageLoaderConfig());
        context=this;
    }

    private ImageLoaderConfiguration getImageLoaderConfig() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                // implementation/你可以通过自己的内存缓存实现
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                // 将保存的时候的URI名称用MD5 加密
                // .discCacheFileNameGenerator(new
                // Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .discCacheFileCount(100) //缓存的文件数量
                 .diskCache(new UnlimitedDiskCache(new File(new FileUtils().DEFAULT_DATA_IMAGEPATH)))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                // connectTimeout (5 s), readTimeout (30 s)超时时间
                // .writeDebugLogs() // Remove for release app
                .defaultDisplayImageOptions(ImageLoaderOptions.AvatarOptions())
                .build();// 开始构建
        return config;
    }
}

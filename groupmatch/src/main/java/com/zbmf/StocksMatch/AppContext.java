package com.zbmf.StocksMatch;

import com.kwlstock.sdk.KwlOpenConfig;
import com.kwlstock.sdk.KwlOpenConstants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zbmf.StocksMatch.utils.UiCommon;

import java.io.File;

/**
 *
 */
public class AppContext extends android.app.Application {


    private static AppContext mInstance;

    public static AppContext getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCache(new UnlimitedDiscCache(new File(UiCommon.INSTANCE.DEFAULT_DATA_IMAGEPATH)))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
//        initHttpApi();
//        SDKInitializer.initialize(this);
        initSdk();
    }

    /**
     * 初始化
     */
    private void initSdk(){
        //开户
        KwlOpenConfig.getInstance().initOpenSDK("");
        //设置开户环境
        KwlOpenConfig.getInstance().setUrlType(KwlOpenConstants.URL_TYPE_SZT_PROD);

//		KwlOpenConfig.getInstance().setTitleBarLeftRes(R.drawable.base_back_up);
//		KwlOpenConfig.getInstance().setTitleBarRightRes(R.drawable.personal_refresh);
//		KwlOpenConfig.getInstance().setTitleBarRes(R.drawable.base_title_bar_bg_trade);

    }

//    private void initHttpApi() {
//        HttpUtils.getInstance().init(this);
//        HttpUtils.getInstance().SetHeaders("Accept", "application/json");
//        HttpUtils.getInstance().SetHeaders("Content-Type", "text/xml;charset=UTF-8");
//        HttpUtils.getInstance().SetHeaders("Accept-Encoding", "gzip,deflate");
//    }
}

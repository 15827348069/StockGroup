package com.zbmf.StockGTec.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;
import com.zbmf.StockGTec.utils.PhotoViewAttacher;

public class BigImageActivity extends ExActivity {
    private String img_url;
    private ImageView big_img_id;
    private PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        init();
    }

    private void init() {
        big_img_id= (ImageView) findViewById(R.id.big_img_id);
        img_url=getIntent().getStringExtra("img_url");
        if(img_url!=null&&!img_url.equals("")){
            ImageLoader.getInstance().displayImage(img_url,big_img_id, ImageLoaderOptions.BigProgressOptions(),new ImageLoadingListener(){

                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    //加载成功后放入放大缩小控件
                    ViewGroup.LayoutParams lp = big_img_id.getLayoutParams();
                    lp.width=ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.height=ViewGroup.LayoutParams.MATCH_PARENT;
                    big_img_id.setLayoutParams(lp);
                    mAttacher=new PhotoViewAttacher(big_img_id);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mAttacher != null)
        {
            mAttacher.cleanup();
        }
    }
}

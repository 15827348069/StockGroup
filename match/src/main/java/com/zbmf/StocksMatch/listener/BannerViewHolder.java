package com.zbmf.StocksMatch.listener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;

/**
 * Created by pq
 * on 2018/3/20.
 */

public class BannerViewHolder implements MZViewHolder<Integer> {
    private ImageView mImageView;
    private TextView mMTv;
    private int imgs;
    private BannerPageClickListener mBannerPageClickListener;

    public BannerViewHolder(int imgs) {
        this.imgs = imgs;
    }
    public void setBannerClickListener(BannerPageClickListener bannerClickListener){
        this.mBannerPageClickListener=bannerClickListener;
    }

    @Override
    public View createView(Context context) {
        // 返回页面布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        mMTv = (TextView) view.findViewById(R.id.banner_tv);
        return view;
    }

    @Override
    public void onBind(Context context, final int position, Integer data, Integer text) {
        // 数据绑定
        mImageView.setImageResource(data);
        mMTv.setText(text);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = position % imgs;
                if (mBannerPageClickListener!=null){
                    mBannerPageClickListener.onPageClick(v,i);
                }
            }
        });
    }
}
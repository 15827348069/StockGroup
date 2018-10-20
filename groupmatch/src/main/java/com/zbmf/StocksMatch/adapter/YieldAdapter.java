package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.Yield;
import com.zbmf.StocksMatch.widget.CircleImageView;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/1/13.
 */
public class YieldAdapter extends ListAdapter<Yield>{

    private DecimalFormat df= new DecimalFormat("######0.00");
    private ImageLoader imageLoader;
    DisplayImageOptions options;
    public YieldAdapter(Activity context) {
        super(context);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_avatar)
                .showImageForEmptyUri(R.drawable.default_avatar)
                .showImageOnFail(R.drawable.default_avatar)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888) // 设置图片的解码类型
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.rank_item,null);
            holder = new ViewHolder();
            holder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_rank = (TextView) convertView.findViewById(R.id.tv_rank);
            holder.civ = (CircleImageView)convertView.findViewById(R.id.civ);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Yield yield = mList.get(position);
        holder.tv_index.setText(position+1+"");
        holder.tv_username.setText(yield.getNickname());
        holder.tv_rank.setText("");
        if(yield.getYield()<0){
            holder.tv_rank.setTextColor(Color.rgb(7, 152, 0));
            holder.tv_rank.setText(df.format(yield.getYield()*100)+"%");
        }else{
            holder.tv_rank.setTextColor(Color.rgb(255, 24, 0));
            holder.tv_rank.setText(df.format(yield.getYield()*100)+"%");
        }

        imageLoader.displayImage(yield.getAvatar(),holder.civ,options);
        return convertView;
    }

    static class ViewHolder{
        TextView tv_index,tv_username,tv_rank;CircleImageView civ;
    }
}

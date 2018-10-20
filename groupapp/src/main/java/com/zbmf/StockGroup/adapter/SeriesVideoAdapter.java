package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Video;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xuhao on 2017/7/7.
 */

public class SeriesVideoAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Video>videos;

    public SeriesVideoAdapter(Context context, List<Video>videoList){
        this.inflater=LayoutInflater.from(context);
        this.videos=videoList;
    }
    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SeriesHolder item=null;
        if(view==null){
            view=inflater.inflate(R.layout.item_series_layout,null);
            item=new SeriesHolder(view);
            view.setTag(item);
        }else{
            item= (SeriesHolder) view.getTag();
        }
        Video video=videos.get(position);
        item.video_position.setText((position+1)+"、");
        item.video_name.setText(video.getVideoName());

        if(video.getVideoPriceType()!=100){
            item.video_price.getPaint().setAntiAlias(true);//抗锯齿
            item.video_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            item.video_new_price.setVisibility(View.VISIBLE);
            if(video.getVideonewPrice()==0){
                item.video_new_price.setText("限时免费");
            }else{
                item.video_new_price.setText(getDoubleormat(video.getVideonewPrice()));
            }

        }else{
            item.video_price.getPaint().setFlags(0);
            item.video_new_price.setVisibility(View.GONE);
        }
        item.video_price.setText(getDoubleormat(video.getVideoPrice()));
        return view;
    }
    private class SeriesHolder{
        public TextView video_name,video_price,video_new_price,video_position;
        public SeriesHolder(View view){
            video_name= (TextView) view.findViewById(R.id.video_name);
            video_price= (TextView) view.findViewById(R.id.video_price);
            video_new_price= (TextView) view.findViewById(R.id.video_new_price);
            video_position= (TextView) view.findViewById(R.id.video_position);
        }
    }
    DecimalFormat df = new DecimalFormat("");
    DecimalFormat double_df = new DecimalFormat("######0.00");
    public String getDoubleormat(double vealue) {
        if (double_df.format(vealue).contains(".00")) {
            double ve = Double.valueOf(double_df.format(vealue));
            return df.format(ve);
        } else {
            return double_df.format(vealue);
        }
    }
}

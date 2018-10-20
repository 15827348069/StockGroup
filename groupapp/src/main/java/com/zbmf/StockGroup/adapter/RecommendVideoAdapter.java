package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

import java.util.List;

/**
 * Created by xuhao on 2017/7/5.
 */

public class RecommendVideoAdapter extends BaseAdapter {
    private List<Video>infolist;
    private LayoutInflater inflater;
    public RecommendVideoAdapter(Context context, List<Video>info){
        this.inflater=LayoutInflater.from(context);
        this.infolist=info;
    }
    @Override
    public int getCount() {
        return infolist.size();
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
        VideoHolder holder=null;
        if(view==null){
            view=inflater.inflate(R.layout.video_item_layout,null);
            holder=new VideoHolder(view);
            view.setTag(holder);
        }else{
            holder= (VideoHolder) view.getTag();
        }
        Video video=infolist.get(position);
        if(video.getIs_series()){
            Video.SeriesVideo seriesVideo=video.getSeriesVideo();
            holder.video_group_name.setText(seriesVideo.getTeacher_name());
            holder.video_date.setText(seriesVideo.getCreated_at());
            holder.video_title.setText(seriesVideo.getName());
//            ViewFactory.getImgView(parent.getContext(),seriesVideo.getPic_play(),holder.video_img);
            ImageLoader.getInstance().displayImage(seriesVideo.getPic_play(),holder.video_img, ImageLoaderOptions.BigProgressOptions());
        }else{
            holder.video_group_name.setText(video.getVideoGroupname());
            holder.video_date.setText(video.getVideoDate());
            holder.video_title.setText(video.getVideoName());
//            ViewFactory.getImgView(parent.getContext(),video.getVideoImg(),holder.video_img);
            ImageLoader.getInstance().displayImage(video.getVideoImg(),holder.video_img, ImageLoaderOptions.BigProgressOptions());
        }
        return view;
    }
    private class VideoHolder{
        private TextView video_group_name,video_date,video_title;
        private ImageView video_img;
        public VideoHolder(View view){
            this.video_title= (TextView) view.findViewById(R.id.video_title);
            this.video_date= (TextView) view.findViewById(R.id.video_date);
            this.video_group_name= (TextView) view.findViewById(R.id.video_group_name);
            this.video_img= (ImageView) view.findViewById(R.id.video_img);
        }
    }
}

package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.VideoItemType;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xuhao on 2017/6/22.
 */

public class VideoItemAdapter extends BaseAdapter {
    private List<Video> videoList;
    private LayoutInflater inflater;
    DecimalFormat df = new DecimalFormat("");
    DecimalFormat double_df = new DecimalFormat("######0.00");
    private int width;
    private int videoViewType;
    public VideoItemAdapter(Activity context, List<Video> info) {
        this.inflater = LayoutInflater.from(context);
        this.videoList = info;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
    }

    public void setVideoViewType(int videoViewType) {
        this.videoViewType = videoViewType;
    }

    @Override
    public int getItemViewType(int position) {
        return videoViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int type=getItemViewType(position);
        switch (type){
            case VideoItemType.VIDEO:
                view=getVideoView(position,view,parent);
                break;
            case VideoItemType.SERIES:
                view=getSeriesView(position,view,parent);
                break;
        }
        return view;
    }

    public String getDoubleormat(double vealue) {
        if (double_df.format(vealue).contains(".00")) {
            double ve = Double.valueOf(double_df.format(vealue));
            return df.format(ve);
        } else {
            return double_df.format(vealue);
        }
    }

    private View getVideoView(int position, View view, ViewGroup parent) {
        VideoHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_study_video, null);
            holder = new VideoHolder(view);
            int screenWidth = width;
            ViewGroup.LayoutParams lp = holder.videoImg.getLayoutParams();
            lp.width = screenWidth;
            lp.height = screenWidth * 9 / 16;
            holder.videoImg.setLayoutParams(lp);
            ViewGroup.LayoutParams rp = holder.video_relative.getLayoutParams();
            rp.width = screenWidth;
            rp.height = screenWidth * 9 / 16;
            holder.video_relative.setLayoutParams(rp);
            view.setTag(holder);
        } else {
            if(view.getTag() instanceof VideoHolder){
                holder = (VideoHolder) view.getTag();
            }else{
                view = inflater.inflate(R.layout.item_study_video, null);
                holder = new VideoHolder(view);
                int screenWidth = width;
                ViewGroup.LayoutParams lp = holder.videoImg.getLayoutParams();
                lp.width = screenWidth;
                lp.height = screenWidth * 9 / 16;
                holder.videoImg.setLayoutParams(lp);
                ViewGroup.LayoutParams rp = holder.video_relative.getLayoutParams();
                rp.width = screenWidth;
                rp.height = screenWidth * 9 / 16;
                holder.video_relative.setLayoutParams(rp);
                view.setTag(holder);
            }
        }
        Video video = videoList.get(position);
        holder.videoDate.setText(video.getVideoDate());
        holder.videoName.setText(video.getVideoName());
        holder.videoGroupName.setText(video.getVideoGroupname());
        holder.videoParticipation.setText(video.getVideoParticipation());

//        ViewFactory.getImgView(parent.getContext(),video.getVideoImg(), holder.videoImg);
        ImageLoader.getInstance().displayImage(video.getVideoImg(), holder.videoImg, ImageLoaderOptions.BigProgressOptions());
        if (video.getVideoPriceType() != 100) {
            holder.videoOldprice.getPaint().setAntiAlias(true);//抗锯齿
            holder.videoOldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.videoPrice.setVisibility(View.VISIBLE);
            if (video.getVideonewPrice() == 0) {
                holder.videoPrice.setText("限时免费");
            } else {
                holder.videoPrice.setText(getDoubleormat(video.getVideonewPrice()) + "魔方宝");
            }
        } else {
            holder.videoOldprice.getPaint().setFlags(0);
            holder.videoPrice.setVisibility(View.GONE);
        }
        holder.videoOldprice.setText(getDoubleormat(video.getVideoPrice()) + "魔方宝");
        if (video.getVideoPlayType() != null) {
            holder.videoPlaytype.setVisibility(View.VISIBLE);
            holder.videoPlaytype.setText(video.getVideoPlayType());
        } else {
            holder.videoPlaytype.setVisibility(View.GONE);
        }
        if (video.getVideo_tag() != null && !video.getVideo_tag().equals("")) {
            holder.videoType.setVisibility(View.VISIBLE);
            holder.videoType.setText(video.getVideo_tag());
        } else {
            holder.videoType.setVisibility(View.GONE);
        }
        return view;
    }

    private View getSeriesView(int position, View view, ViewGroup parent) {
        SeriesVideoHolder holder=null;
        if(view==null){
            view=inflater.inflate(R.layout.item_series_video_layout,null);
            holder=new SeriesVideoHolder(view);
            view.setTag(holder);
        }else{
            if(view.getTag() instanceof SeriesVideoHolder){
                holder= (SeriesVideoHolder) view.getTag();
            }else{
                view=inflater.inflate(R.layout.item_series_video_layout,null);
                holder=new SeriesVideoHolder(view);
                view.setTag(holder);
            }
        }
        Video.SeriesVideo seriesVideo=videoList.get(position).getSeriesVideo();
        if(seriesVideo!=null){
            holder.tv_series_name.setText(seriesVideo.getTeacher_name());
            holder.tv_series_num.setText("共"+seriesVideo.getNew_phase()+"期");
            holder.tv_series_title.setText(seriesVideo.getName());
//            ViewFactory.getImgView(parent.getContext(),seriesVideo.getTeacher_avatar(),holder.imv_series_avatar);
            ImageLoader.getInstance().displayImage(seriesVideo.getTeacher_avatar(),holder.imv_series_avatar, ImageLoaderOptions.BigProgressOptions());
            holder.video_old_price.setText(getDoubleormat(seriesVideo.getSeries_price()) +"魔方宝");
            if (seriesVideo.getDiscount() != 100) {
                holder.video_old_price.getPaint().setAntiAlias(true);//抗锯齿
                holder.video_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.video_price.setVisibility(View.VISIBLE);
                if (seriesVideo.getNew_series_price() == 0) {
                    holder.video_price.setText("限时免费");
                } else {
                    holder.video_price.setText(getDoubleormat(seriesVideo.getNew_series_price()) + "魔方宝");
                }
            } else {
                holder.video_old_price.getPaint().setFlags(0);
                holder.video_price.setVisibility(View.GONE);
            }
        }
        return view;
    }

    private class VideoHolder {
        ImageView videoImg, video_hot;
        TextView videoName, videoGroupName, videoDate, videoPrice, videoOldprice, videoParticipation, videoType, videoPlaytype;
        RelativeLayout video_relative;

        public VideoHolder(View view) {
            this.videoName = (TextView) view.findViewById(R.id.video_name);
            this.videoGroupName = (TextView) view.findViewById(R.id.video_group_name);
            this.videoDate = (TextView) view.findViewById(R.id.video_date);
            this.videoPrice = (TextView) view.findViewById(R.id.video_price);
            this.videoOldprice = (TextView) view.findViewById(R.id.video_old_price);
            this.videoParticipation = (TextView) view.findViewById(R.id.video_participation);
            this.videoType = (TextView) view.findViewById(R.id.video_type);
            this.videoPlaytype = (TextView) view.findViewById(R.id.video_play_type);
            this.videoImg = (ImageView) view.findViewById(R.id.video_img);
            this.video_hot = (ImageView) view.findViewById(R.id.video_hot);
            this.video_relative = (RelativeLayout) view.findViewById(R.id.video_relative);
        }
    }

    private class SeriesVideoHolder {
        private TextView tv_series_name, tv_series_num, tv_series_title,video_old_price,video_price;
        private RoundedCornerImageView imv_series_avatar;

        public SeriesVideoHolder(View view) {
            this.tv_series_name = (TextView) view.findViewById(R.id.tv_series_name);
            this.tv_series_num = (TextView) view.findViewById(R.id.tv_series_num);
            this.tv_series_title = (TextView) view.findViewById(R.id.tv_series_title);
            this.video_old_price = (TextView) view.findViewById(R.id.video_old_price);
            this.video_price = (TextView) view.findViewById(R.id.video_price);
            this.imv_series_avatar = (RoundedCornerImageView) view.findViewById(R.id.imv_series_avatar);
        }
    }
}

package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.utils.DateUtil;

/**
 * Created by xuhao on 2017/11/13.
 */

public class VideoLivingAdapter extends ListAdapter<Video> {
    public VideoLivingAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_video_live_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Video video= (Video) getItem(position);
        holder.name.setText(video.getVideoName());
        holder.date.setText(DateUtil.getLiveTime(video.getStart_time()));
        Glide.with(mContext).load(R.drawable.icon_living).into(holder.imv_icon_living);
        return convertView;
    }
    private class ViewHolder{
        TextView name,date;
        ImageView imv_icon_living;
        public ViewHolder(View view){
            name= (TextView) view.findViewById(R.id.tv_live_name);
            date= (TextView) view.findViewById(R.id.tv_live_date);
            imv_icon_living= (ImageView) view.findViewById(R.id.imv_icon_living);
        }
    }
}

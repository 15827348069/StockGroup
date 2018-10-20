package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.HomeImage;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

/**
 * Created by xuhao on 2017/11/13.
 */

public class ChouseVideoAdapter extends ListAdapter {
    public ChouseVideoAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_chouse_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        if(getItem(position) instanceof  Video){
            Video video= (Video) getItem(position);
            if(video.getIs_series()){
                Video.SeriesVideo seriesVideo=video.getSeriesVideo();
                holder.tv_group_name.setVisibility(View.VISIBLE);
                holder.name.setText(seriesVideo.getName());
                holder.tv_group_name.setText(seriesVideo.getTeacher_name());
//                ViewFactory.getRoundImgView(parent.getContext(),seriesVideo.getPic_play(),holder.imv_icon_living);
                ImageLoader.getInstance().displayImage(seriesVideo.getPic_play(),holder.imv_icon_living, ImageLoaderOptions.RoundedBitMapoptios());
            }else{
                holder.tv_group_name.setVisibility(View.GONE);
                holder.name.setText(video.getVideoName());
//                ViewFactory.getRoundImgView(parent.getContext(),video.getVideoImg(),holder.imv_icon_living);
                ImageLoader.getInstance().displayImage(video.getVideoImg(),holder.imv_icon_living, ImageLoaderOptions.RoundedBitMapoptios());
            }
        }else if(getItem(position) instanceof HomeImage){
            HomeImage homeImage= (HomeImage) getItem(position);
            holder.name.setText(homeImage.getTitle());
//            ViewFactory.getRoundImgView(parent.getContext(),homeImage.getImg_url(),holder.imv_icon_living);
            ImageLoader.getInstance().displayImage(homeImage.getImg_url(),holder.imv_icon_living, ImageLoaderOptions.RoundedBitMapoptios());
        }

        return convertView;
    }
    private class ViewHolder{
        TextView name,tv_group_name;
        ImageView imv_icon_living;
        public ViewHolder(View view){
            name= (TextView) view.findViewById(R.id.tv_name);
            tv_group_name= (TextView) view.findViewById(R.id.tv_group_name);
            imv_icon_living= (ImageView) view.findViewById(R.id.imv_video_img);
        }
    }
}

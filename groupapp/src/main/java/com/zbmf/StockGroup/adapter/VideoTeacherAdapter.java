package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.VideoTeacher;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xuhao on 2017/8/22.
 */

public class VideoTeacherAdapter extends BaseAdapter {
    private List<VideoTeacher> info;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick=onItemClick;
    }

    public VideoTeacherAdapter(Context context){
        this.inflater=LayoutInflater.from(context);
        this.info=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder=null;
        if(view==null){
            view=inflater.inflate(R.layout.item_video_teacher_layout,null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        final VideoTeacher teacher=info.get(position);
//        ViewFactory.imgCircleView(parent.getContext(),teacher.getTeacher_head(),holder.item_teacher_avatar);
        ImageLoader.getInstance().displayImage(teacher.getTeacher_head(),holder.item_teacher_avatar, ImageLoaderOptions.AvatarOptions());
        holder.tv_video_teacher_name.setText(teacher.getName());
        holder.recommend_item_button.setText(teacher.getAttention()?"已关注":"关注");
        holder.recommend_item_button.setVisibility(teacher.getIs_group()?View.VISIBLE:View.GONE);
        holder.btn_into_group.setVisibility(teacher.getIs_group()?View.VISIBLE:View.GONE);
        holder.btn_into_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onEnterGroup(teacher);
                }
            }
        });
        holder.recommend_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!teacher.getAttention()) {
                    onItemClick.onCareClick(position);
                } else {
                    onItemClick.onCancelCareClick(position);
                }
            }
        });
        holder.tv_teacher_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onDescClick(teacher);
                }
            }
        });
        holder.teacher_video_num.setText(teacher.getCount_video()+"");
        holder.item_video_num_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onVideoClick(teacher);
                }
            }
        });
        return view;
    }
    public void rushAdapter(List<VideoTeacher>infolist){
        this.info.clear();
        this.info.addAll(infolist);
        notifyDataSetChanged();
    }
    private class ViewHolder{
        RoundedCornerImageView item_teacher_avatar;
        TextView recommend_item_button;
        TextView tv_video_teacher_name,btn_into_group,tv_teacher_desc,teacher_video_num;
        LinearLayout item_video_num_layout;
        public ViewHolder(View view){
            this.item_teacher_avatar= (RoundedCornerImageView) view.findViewById(R.id.item_teacher_avatar);
            this.tv_video_teacher_name= (TextView) view.findViewById(R.id.tv_video_teacher_name);
            this.recommend_item_button= (TextView) view.findViewById(R.id.recommend_item_button);
            this.btn_into_group= (TextView) view.findViewById(R.id.btn_into_group);
            this.tv_teacher_desc= (TextView) view.findViewById(R.id.tv_teacher_desc);
            this.teacher_video_num= (TextView) view.findViewById(R.id.teacher_video_num);
            this.item_video_num_layout= (LinearLayout) view.findViewById(R.id.item_video_num_layout);
        }
    }
    public interface OnItemClick{
        void onCancelCareClick(int position);
        void onCareClick(int position);
        void onEnterGroup(VideoTeacher teacher);
        void onDescClick(VideoTeacher teacher);
        void onVideoClick(VideoTeacher teacher);
    }
}

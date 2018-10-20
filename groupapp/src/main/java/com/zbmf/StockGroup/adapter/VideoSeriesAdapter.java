package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.VideoTeacher;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

public class VideoSeriesAdapter extends RecyclerView.Adapter<VideoSeriesAdapter.ViewHolder> {
    private List<VideoTeacher> info;

    public interface OnItemClickLitener {
        void onAskCilck(VideoTeacher group);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private Context mContext;
    public VideoSeriesAdapter(Context context, List<VideoTeacher> datats) {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        info = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            avatar = (RoundedCornerImageView) view.findViewById(R.id.home_teacher_avatar);
            name = (TextView) view.findViewById(R.id.home_teacher_name);
        }

        RoundedCornerImageView avatar;
        TextView name;
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_video_series_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final VideoTeacher group = info.get(i);
//        ViewFactory.imgCircleView(mContext,group.getTeacher_head(), viewHolder.avatar);
        ImageLoader.getInstance().displayImage(group.getTeacher_head(), viewHolder.avatar, ImageLoaderOptions.AvatarOptions());
        viewHolder.name.setText(group.getName());
        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onAskCilck(group);
                }
            }
        });
    }

}
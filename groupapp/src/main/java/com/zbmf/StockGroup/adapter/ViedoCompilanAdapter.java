package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

import java.text.DecimalFormat;
import java.util.List;

public class ViedoCompilanAdapter extends RecyclerView.Adapter<ViedoCompilanAdapter.ViewHolder>{
    private List<Video>info;
    public interface OnItemClickLitener
    {
        void onItemClick(View view, Video video);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private Context mContext;
    public ViedoCompilanAdapter(Context context, List<Video> datats)
    {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        info = datats;
    }
    public static  class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);
            this.video_title= (TextView) view.findViewById(R.id.video_title);
            this.video_date= (TextView) view.findViewById(R.id.video_date);
            this.video_price= (TextView) view.findViewById(R.id.video_price);
            this.video_img= (ImageView) view.findViewById(R.id.video_img);
        }
        private TextView video_price,video_date,video_title;
        private ImageView video_img;
    }

    @Override
    public int getItemCount()
    {
        return info.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.item_video_compilan_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i)
    {
        final Video video=info.get(i);
        holder.video_price.setText(getDoubleormat(video.getVideoPrice()));
        holder.video_date.setText(video.getVideoDate());
        holder.video_title.setText(video.getVideoName());
//        ViewFactory.getImgView(mContext,video.getVideoImg(),holder.video_img);
        ImageLoader.getInstance().displayImage(video.getVideoImg(),holder.video_img, ImageLoaderOptions.BigProgressOptions());
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(holder.itemView,video);
                }
            });

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
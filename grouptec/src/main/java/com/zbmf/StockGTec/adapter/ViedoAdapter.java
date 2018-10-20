package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Videos;
import com.zbmf.StockGTec.utils.DateUtil;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;

import java.util.List;

public class ViedoAdapter extends BaseAdapter {

    private List<Videos> list;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public ViedoAdapter(Context cxt, List<Videos> list) {
        this.list = list;
        mContext = cxt;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_video, null);
            holder = new ViewHolder();
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.root = (RelativeLayout) convertView.findViewById(R.id.root);
            holder.tv_subscribe = (TextView) convertView.findViewById(R.id.tv_subscribe);
            holder.tv_zhuanji = (TextView) convertView.findViewById(R.id.tv_zhuanji);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            convertView.setTag(holder);
        }

        Videos videos = list.get(position);
        String startTime = videos.getStart_at() * 1000 + "";
        holder.tv_date.setText(DateUtil.getDate4(Long.parseLong(startTime)));
        holder.tv_subscribe.setText(videos.getSubscribe()+"");
        holder.tv_price.setText(videos.getPrice()+"");
        holder.tv_title.setText(videos.getTitle());
        holder.tv_zhuanji.setText(videos.getSeries_name());
//        ImageLoader.getInstance().loadImageSync(videos.getShare_img());
        ImageLoader.getInstance().displayImage(videos.getPic_play(),holder.iv_img, ImageLoaderOptions.AvatarOptions());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tv_date, tv_subscribe, tv_title,tv_price,tv_zhuanji,tv_fans;
        ImageView iv_img;
        RelativeLayout root;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}

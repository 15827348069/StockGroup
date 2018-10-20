package com.zbmf.StockGroup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.DzUser;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pq
 * on 2018/7/9.
 */

public class DzAdapter extends ListAdapter<DzUser> {
    public DzAdapter(Context context) {
        super(context);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dz_item_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DzUser item = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = null;
        if (!TextUtils.isEmpty(item.getCreated_at())) {
            time = dateFormat.format(new Date(Long.parseLong(item.getCreated_at()) * 1000L));
        }
        holder.mDateTV.setText(time);
        ImageLoader.getInstance().displayImage(item.getAvatar(), holder.mDzAvatar, ImageLoaderOptions.AvatarOptions());
        holder.mDzUserName.setText(item.getNickname());
        return convertView;
    }

    public class ViewHolder {

        private final ImageView mDzAvatar;
        private final TextView mDzUserName;
        private final TextView mDateTV;

        ViewHolder(View view) {
            mDzAvatar = view.findViewById(R.id.dzAvatar);
            mDzUserName = view.findViewById(R.id.dzUserName);
            mDateTV = view.findViewById(R.id.dateTV);
        }
    }
}

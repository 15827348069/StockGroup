package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.Announcement;
import com.zbmf.StocksMatch.utils.GetTime;

/**
 * Created by lulu on 2016/1/13.
 */
public class AnnouncementAdapter extends ListAdapter<Announcement>{
    public AnnouncementAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.announcement_item,null);
            holder = new ViewHolder();
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Announcement announcement = mList.get(position);
        holder.tv_date.setText(GetTime.getTime1(announcement.getPosted_at()));
        holder.tv_title.setText(announcement.getSubject());
        return convertView;
    }

    static class ViewHolder{
        TextView tv_title,tv_date;
    }
}

package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.MatchAnnouncements;

/**
 * Created by xuhao on 2018/1/8.
 */

public class MatchNoticeAdapter extends ListAdapter<MatchAnnouncements> {

    public MatchNoticeAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_notice_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        MatchAnnouncements announcements=getItem(position);
        holder.tvDate.setText(announcements.getPosted_at());
        holder.tvTitle.setText(announcements.getSubject());
        return convertView;
    }
    public class ViewHolder{
        private TextView tvTitle,tvDate;
        public ViewHolder(View view){
            this.tvTitle= (TextView) view.findViewById(R.id.tv_title);
            this.tvDate= (TextView) view.findViewById(R.id.tv_date);
        }
    }
}

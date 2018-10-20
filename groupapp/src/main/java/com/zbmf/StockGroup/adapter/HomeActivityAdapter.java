package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.NewsFeed;

import java.util.List;

/**
 * Created by xuhao on 2017/8/25.
 */

public class HomeActivityAdapter extends BaseAdapter {
    private List<NewsFeed>infolist;
    private LayoutInflater inflater;
    public HomeActivityAdapter(Context context,List<NewsFeed>info){
        this.inflater=LayoutInflater.from(context);
        this.infolist=info;

    }
    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public Object getItem(int position) {
        return infolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_home_activity_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        NewsFeed newsFeed=infolist.get(position);
        holder.subject.setText(newsFeed.getSubject());
        return convertView;
    }
    private class ViewHolder{
        TextView subject;
        public ViewHolder (View view){
            this.subject= (TextView) view.findViewById(R.id.item_activity_subject);
        }
    }
}

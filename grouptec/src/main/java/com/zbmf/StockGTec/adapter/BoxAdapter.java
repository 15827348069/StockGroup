package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.BoxDetailBean;

import java.util.List;

public class BoxAdapter extends BaseAdapter{

    private List<BoxDetailBean.Items> list;
    private Context mContext;

    public BoxAdapter(Context cxt, List<BoxDetailBean.Items> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_box, null);
            holder = new ViewHolder();
            holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }
        BoxDetailBean.Items b = list.get(position);
        holder.tv_content.setText(b.getContent());
        holder.tv_time.setText(b.getCreated_at());
        return convertView;
    }

    static class ViewHolder{
        TextView tv_content,tv_time;
    }
}

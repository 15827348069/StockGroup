package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Fans;
import com.zbmf.StockGTec.view.RoundedCornerImageView;

import java.util.List;

public class PrivateAdapter extends BaseAdapter{

    private List<Fans> list;
    private Context mContext;

    public PrivateAdapter(Context cxt, List<Fans> list){
        this.list = list;
        mContext = cxt;
    }

    @Override
    public int getCount() {
        return 6;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
            holder.rcv = (RoundedCornerImageView) convertView.findViewById(R.id.rcv);
            convertView.setTag(holder);
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tv_name,tv_content;
        RoundedCornerImageView rcv;
    }
}

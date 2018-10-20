package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Record;

/**
 * 用户获奖记录adapter
 * Created by lulu on 16/1/9.
 */
public class RecordAdapter extends ListAdapter<Record>{

    private ImageLoader imageLoader;
    public RecordAdapter(Activity context) {
        super(context);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_focus_layout, null);
            holder = new ViewHolder();
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            convertView.setTag(holder);
        }
        Record record = mList.get(position);
        holder.tv_username.setText(record.getWin_at()+mContext.getString(R.string.get1)+record.getAward());
        return convertView;
    }


    static class ViewHolder{
        TextView tv_username;
    }


}

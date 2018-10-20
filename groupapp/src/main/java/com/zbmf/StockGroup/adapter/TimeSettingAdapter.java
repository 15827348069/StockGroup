package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockBean;

/**
 * Created by xuhao on 2018/1/31.
 */

public class TimeSettingAdapter extends ListAdapter<StockBean>
{

    public TimeSettingAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_time_setting_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        StockBean stockBean=getItem(position);
        holder.name.setText(stockBean.getF_symbolName());
        return convertView;
    }
    private class ViewHolder{
        TextView name;
        public ViewHolder(View view){
            name= (TextView) view.findViewById(R.id.tv_name);
        }
    }
}

package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockBean;

/**
 * Created by xuhao on 2018/1/31.
 */

public class TimeCheckAdapter extends ListAdapter<StockBean> {

    public TimeCheckAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_time_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        StockBean stockBean=getItem(position);
        holder.name.setText(stockBean.getF_symbolName());
        holder.name.setSelected(stockBean.isCheck());
        return convertView;
    }
    private class ViewHolder{
        TextView name;
        public ViewHolder(View view){
            name= (TextView) view.findViewById(R.id.tv_name);
        }
    }
    public void checkPosition(int position){
        if(position>=0){
            for(StockBean stockBean:getList()){
                stockBean.setCheck(false);
            }
            getItem(position).setCheck(true);
            notifyDataSetChanged();
        }
    }
}

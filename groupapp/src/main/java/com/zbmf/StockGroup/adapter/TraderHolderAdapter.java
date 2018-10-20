package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.utils.DoubleFromat;

/**
 * Created by xuhao on 2017/11/10.
 */

public class TraderHolderAdapter extends ListAdapter<DealSys> {
    public TraderHolderAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_trader_hold,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        DealSys dealSys= (DealSys) getItem(position);
        holder.name.setText(dealSys.getStock_name()+"("+dealSys.getSumbol()+")");
        holder.price.setText(DoubleFromat.getDouble(dealSys.getPrice(),2));
        holder.yield.setText(DoubleFromat.getDouble(dealSys.getGain_yield()*100,2)+"%");
        holder.num.setText(dealSys.getVolumn_total()+"");
        holder.yield.setTextColor(dealSys.getGain_yield()>0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));

        return convertView;
    }
    private class ViewHolder{
        TextView name,price,num,yield;
        public ViewHolder(View view){
            this.name= (TextView) view.findViewById(R.id.tv_stock_name);
            this.price= (TextView) view.findViewById(R.id.tv_stock_price);
            this.num= (TextView) view.findViewById(R.id.tv_stock_num);
            this.yield= (TextView) view.findViewById(R.id.tv_gain_yield);
        }
    }
}

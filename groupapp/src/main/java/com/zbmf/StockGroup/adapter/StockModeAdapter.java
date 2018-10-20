package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.StockDetailActivity;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.ShowActivity;

/**
 * Created by xuhao on 2017/12/4.
 */

public class StockModeAdapter extends ListAdapter<StockMode> {
    private Activity mActivity;
    public StockModeAdapter(Activity context) {
        super(context);
        this.mActivity=context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_stock_mode_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final StockMode sm=getItem(position);
        holder.tv_price.setText(DoubleFromat.getStockDouble(sm.getPrice(),2));
        holder.tv_yield.setText(DoubleFromat.getStockDouble(sm.getYield()*100,2)+"%");
        holder.tv_yellow.setText(DoubleFromat.getStockDouble(sm.getYellow()/10,2));
        holder.tv_yieldAll.setText(DoubleFromat.getStockDouble(sm.getAllYield(),2)+"%");
        if(sm.getYield()==0){
            holder.tv_yield.setTextColor(mContext.getResources().getColor(R.color.black_99));
        }else{
            setTextColor(sm.getYield()>0,holder.tv_yield);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShowActivity.isLogin(mActivity)){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(IntentKey.STOCKHOLDER, sm);
                    ShowActivity.showActivity(mActivity,bundle,StockDetailActivity.class);
                }
            }
        });
        return convertView;
    }
    private void setTextColor(boolean isRed,TextView textView){
       textView.setTextColor(isRed? mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
    }
    private class ViewHolder{
        TextView tv_price,tv_yield,tv_yellow,tv_yieldAll;
        public ViewHolder(View view){
            tv_price=(TextView) view.findViewById(R.id.tv_item_price);
            tv_yield= (TextView) view.findViewById(R.id.tv_item_yield);
            tv_yellow= (TextView) view.findViewById(R.id.tv_item_yellow_white);
            tv_yieldAll= (TextView) view.findViewById(R.id.tv_item_all_yield);
        }
    }
}

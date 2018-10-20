package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.StockDetailActivity;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.ShowActivity;

/**
 * Created by xuhao on 2017/12/4.
 */

public class StockModeNameAdapter extends ListAdapter<StockMode> {
    private Activity mActivity;
    public StockModeNameAdapter(Activity context) {
        super(context);
        this.mActivity=context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_stock_name_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final StockMode sm=getItem(position);
        holder.tv_name.setText(sm.getStockNmae());
        holder.tv_symbol.setText(sm.getSymbol());
        holder.tv_hot_stock.setVisibility(sm.getRepeat()<3?View.GONE:View.VISIBLE);
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
        holder.shareStockModelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showNuggetsShareActivity(mActivity,sm,null,1);
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView tv_name,tv_symbol;
        ImageView tv_hot_stock;
        LinearLayout shareStockModelView;
        public ViewHolder(View view){
            tv_name= (TextView) view.findViewById(R.id.tv_stock_name);
            tv_symbol= (TextView) view.findViewById(R.id.tv_stock_sybmol);
            tv_hot_stock= (ImageView) view.findViewById(R.id.tv_hot_stock);
            shareStockModelView = (LinearLayout) view.findViewById(R.id.shareStockModelView);
        }
    }
}

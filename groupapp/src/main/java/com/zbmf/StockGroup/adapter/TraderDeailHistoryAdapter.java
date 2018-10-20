package com.zbmf.StockGroup.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.DealSys;

/**
 * Created by xuhao on 2017/11/9.
 */

public class TraderDeailHistoryAdapter extends ListAdapter<DealSys> {

    public TraderDeailHistoryAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_trader_deal_history,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        DealSys dealSys= (DealSys) getItem(position);
        holder.tv_date.setText(dealSys.getDate());
        holder.tv_dealSym.setText(dealSys.getAction()+" "+dealSys.getStock_name());
        return convertView;
    }
    private class ViewHolder{
        TextView tv_date,tv_dealSym;
        public ViewHolder(View view){
            tv_date= (TextView) view.findViewById(R.id.tv_deal_date);
            tv_dealSym= (TextView) view.findViewById(R.id.tv_stock_sybmol);
        }
    }
}

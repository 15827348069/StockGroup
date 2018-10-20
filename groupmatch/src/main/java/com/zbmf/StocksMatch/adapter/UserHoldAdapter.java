package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.StockholdsBean;

import java.text.DecimalFormat;

/**
 * Created by lulu on 16/1/17.
 */
public class UserHoldAdapter extends ListAdapter<StockholdsBean>{
    public UserHoldAdapter(Activity context) {
        super(context);
    }
    private DecimalFormat df = new DecimalFormat("######0.00");
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.userhold_item,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_symbol = (TextView) convertView.findViewById(R.id.tv_symbol);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);//成本价
            holder.tv_syl = (TextView) convertView.findViewById(R.id.tv_syl);//收益率
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        StockholdsBean sb = mList.get(position);
        if("0".equals(sb.getIs_show())&& "0".equals(sb.getIs_buy())){
            holder.tv_name.setText("***");
            holder.tv_symbol.setText(sb.getSymbol().substring(0,3)+"***");
            holder.tv_price.setText("***");
            holder.tv_syl.setText("***");
        }else{
            holder.tv_name.setText(sb.getName());
            holder.tv_symbol.setText(sb.getSymbol());
//            holder.tv_syl.setText(String.format("%.2f", sb.getYield_float())+"%");
            holder.tv_syl.setText(df.format(sb.getYield_float() * 100) + "%");

            holder.tv_price.setText(String.format("%.2f", sb.getCuurent()));
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tv_name,tv_symbol,tv_price,tv_syl;
    }
}

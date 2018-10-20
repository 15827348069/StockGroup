package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.Record;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.widget.CircleImageView;

import java.text.DecimalFormat;

/**
 * 股票详情adapter
 * Created by lulu on 16/1/9.
 */
public class StockDetailAdapter extends ListAdapter<Stock>{

    private String[] nums = {"一","二","三","四","五"};
    DecimalFormat df=new DecimalFormat("#0.00");
    private boolean isup;

    public void setIsup(boolean isup) {
        this.isup = isup;
    }

    public StockDetailAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(mContext).inflate(R.layout.stock_item, null);
            holder = new ViewHolder();
            holder.tv_buy = (TextView) convertView.findViewById(R.id.tv_buy);
            holder.tv_buy_price = (TextView) convertView.findViewById(R.id.tv_buy_price);
            holder.tv_buy_num = (TextView) convertView.findViewById(R.id.tv_buy_num);
            holder.tv_sell = (TextView) convertView.findViewById(R.id.tv_sell);
            holder.tv_sell_price = (TextView) convertView.findViewById(R.id.tv_sell_price);
            holder.tv_sell_num = (TextView) convertView.findViewById(R.id.tv_sell_num);
            convertView.setTag(holder);
        }
        Stock s = mList.get(position);

        holder.tv_buy.setText(mContext.getString(R.string.buy) + nums[position]);
        holder.tv_buy_price.setText(df.format(s.getBuy()));
        holder.tv_buy_num.setText(s.getVolumn_buy());
        holder.tv_sell.setText(mContext.getString(R.string.sale)+nums[position]);
        holder.tv_sell_price.setText(df.format(s.getSell()));
        holder.tv_sell_num.setText(s.getVolumn_sell());

        if(isup){
//            rl_title.setBackgroundColor(Color.rgb(255, 24, 0));
            holder.tv_buy_price.setTextColor(Color.rgb(255, 24, 0));
            holder.tv_sell_price.setTextColor(Color.rgb(255, 24, 0));
        }else{
//            rl_title.setBackgroundColor(Color.rgb(7, 152, 0));
            holder.tv_buy_price.setTextColor(Color.rgb(7, 152, 0));
            holder.tv_sell_price.setTextColor(Color.rgb(7, 152, 0));
        }
        return convertView;
    }


    static class ViewHolder{
        TextView tv_buy,tv_buy_price,tv_buy_num,tv_sell,tv_sell_price,tv_sell_num;
    }
}

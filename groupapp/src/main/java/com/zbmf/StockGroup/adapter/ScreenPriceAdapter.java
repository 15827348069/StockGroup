package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.DoubleFromat;

import java.util.List;

/**
 * Created by xuhao on 2017/10/17.
 */

public class ScreenPriceAdapter extends BaseAdapter {
    private List<Screen.Prce>info;
    private LayoutInflater inflater;
    private Drawable drawable_m,drawable_j,drawable_y;
    public ScreenPriceAdapter(Context context ,List<Screen.Prce>info){
        this.inflater=LayoutInflater.from(context);
        this.info=info;
        this.drawable_m=context.getResources().getDrawable(R.drawable.icon_stock_m);
        this.drawable_j=context.getResources().getDrawable(R.drawable.icon_stock_j);
        this.drawable_y=context.getResources().getDrawable(R.drawable.icon_stock_y);
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_buy_stock,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Screen.Prce prce=info.get(position);
        switch (prce.getDays()/31){
            case 1:
                holder.tv_stock_m_price.setVisibility(View.GONE);
                holder.imv_left_id.setImageDrawable(drawable_m);
                break;
            case 3:
                holder.tv_stock_m_price.setVisibility(View.VISIBLE);
                holder.imv_left_id.setImageDrawable(drawable_j);
                holder.tv_stock_m_price.setText("（月均"+DoubleFromat.getDouble(prce.getPrice()/3,2)+")");
                break;
            case 12:
                holder.imv_left_id.setImageDrawable(drawable_y);
                holder.tv_stock_m_price.setVisibility(View.VISIBLE);
                holder.tv_stock_m_price.setText("（月均"+DoubleFromat.getDouble(prce.getPrice()/12,2)+")");
                break;
        }
        holder.screen_price_check.setSelected(prce.getIs_check());
        holder.tv_stock_price.setText(DoubleFromat.getDouble(prce.getPrice(),0));
        holder.tv_screen_date.setText("到期时间："+ DateUtil.getTime(prce.getExpire_at()*1000, Constants.yy_MM_dd));
        holder.imv_discount.setVisibility(prce.getIs_discount()? View.VISIBLE:View.GONE);
        holder.tv_screen_date.setVisibility(prce.getIs_check()?View.VISIBLE:View.GONE);
        return convertView;
    }
    private class ViewHolder{
        ImageView imv_left_id,screen_price_check,imv_discount;
        TextView tv_stock_price,tv_screen_date,tv_stock_m_price;
        public ViewHolder(View view){
            imv_left_id= (ImageView) view.findViewById(R.id.imv_left_id);
            screen_price_check= (ImageView) view.findViewById(R.id.screen_price_check);
            imv_discount= (ImageView) view.findViewById(R.id.imv_discount);
            tv_stock_price= (TextView) view.findViewById(R.id.tv_stock_price);
            tv_screen_date= (TextView) view.findViewById(R.id.tv_screen_date);
            tv_stock_m_price= (TextView) view.findViewById(R.id.tv_stock_m_price);
        }
    }
    public void setCheck(int position, ResultCallback<Screen.Prce> callback){
        for(Screen.Prce p:info){
            p.setIs_check(false);
        }
        info.get(position).setIs_check(true);
        callback.onSuccess(info.get(position));
        notifyDataSetChanged();
    }
}

package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockModeMenu;

/**
 * Created by xuhao on 2017/12/4.
 */

public class StockModeMenuAdapter extends ListAdapter<StockModeMenu> {
    private Drawable drawable;

    public StockModeMenuAdapter(Activity context) {
        super(context);
        drawable=context.getResources().getDrawable(R.drawable.icon_stock_mode);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_stock_mode_menu,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(getItem(position).getName());
        holder.textView.setSelected(getItem(position).isSelect());
        if(getItem(position).isSelect()){
            holder.textView.setCompoundDrawables(null,null,drawable,null);
        }else{
            holder.textView.setCompoundDrawables(null,null,null,null);
        }
        return convertView;
    }
    public void setSelextPosition(int position){
        for(StockModeMenu menu:getList()){
            menu.setSelect(false);
        }
        getItem(position).setSelect(true);
        notifyDataSetChanged();
    }
    private class ViewHolder{
        TextView textView;
        public ViewHolder(View view){
            this.textView= (TextView) view.findViewById(R.id.tv_stock_menu);
        }
    }
}

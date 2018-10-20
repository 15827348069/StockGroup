package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.utils.DoubleFromat;

import java.util.List;

/**
 * 找股票产品列表
 */

public class ScreenProductAdatper extends BaseAdapter {
    private List<Screen> info;
    private LayoutInflater mInflater;
    private ScreenAdapter.onItemClick itemClick;
    private int red,green;
    public void setItemClick(ScreenAdapter.onItemClick itemClick){
        this.itemClick=itemClick;
    }

    public interface onItemClick
    {
        void onItemClick(Screen screen);
    }

    public ScreenProductAdatper(Context context, List<Screen> infolist) {
        this.info = infolist;
        mInflater = LayoutInflater.from(context);
        red=context.getResources().getColor(R.color.red);
        green=context.getResources().getColor(R.color.green);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View view, ViewGroup parent) {
       ViewHolder viewHolder=null;
        if(view==null){
            view=mInflater.inflate(R.layout.item_screen_product_layout,null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        final Screen screen=info.get(position);
        switch (position%4){
            case 0:
                viewHolder.rl_screen_layout.setBackgroundResource(R.drawable.icon_product1);
                viewHolder.name.setBackgroundResource(R.drawable.icon_screen_title1);
                break;
            case 1:
                viewHolder.rl_screen_layout.setBackgroundResource(R.drawable.icon_product2);
                viewHolder.name.setBackgroundResource(R.drawable.icon_screen_title1);
                break;
            case 2:
                viewHolder.rl_screen_layout.setBackgroundResource(R.drawable.icon_product3);
                viewHolder.name.setBackgroundResource(R.drawable.icon_screen_title2);
                break;
            case 3:
                viewHolder.rl_screen_layout.setBackgroundResource(R.drawable.icon_product4);
                viewHolder.name.setBackgroundResource(R.drawable.icon_screen_title2);
                break;
        }
        viewHolder.tv_total_yield.setTextColor(screen.getTotal_yield()>=0?red:green);
        viewHolder.tv_yield.setTextColor(screen.getSh_yield()>0?red:green);
        viewHolder.name.setText(screen.getName());
        viewHolder.tv_yield.setText((screen.getSh_yield()>=0?"+":"")+(DoubleFromat.getDouble(screen.getSh_yield()*100,2)+"%"));
        viewHolder.tv_total_yield.setText((screen.getTotal_yield()>=0?"+":"")+(DoubleFromat.getDouble(screen.getTotal_yield()*100,2)+"%"));
        viewHolder.tv_win_rate.setText(DoubleFromat.getDouble(screen.getWin_rate()*100,2)+"%");
        viewHolder.tv_screen_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null){
                    itemClick.onItemClick(screen);
                }
            }
        });
        return view;
    }
    private class ViewHolder{
        RelativeLayout rl_screen_layout;
        TextView name, tv_total_yield,tv_win_rate,tv_screen_detail,tv_yield;
        public ViewHolder(View view){
            rl_screen_layout = (RelativeLayout) view.findViewById(R.id.rl_screen_layout);
            name = (TextView) view.findViewById(R.id.tv_screen_name);
            tv_total_yield = (TextView) view.findViewById(R.id.tv_total_yield);
            tv_win_rate = (TextView) view.findViewById(R.id.tv_win_rate);
            tv_screen_detail= (TextView) view.findViewById(R.id.tv_screen_detail);
            tv_yield= (TextView) view.findViewById(R.id.tv_yield);
        }
    }
}


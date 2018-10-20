package com.zbmf.StocksMatch.adapter;

import java.text.DecimalFormat;
import java.util.List;


import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.Stock1;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BuyDetailAdapter extends BaseAdapter {
    private Context ct;
    private List<Stock1> list;
    private LayoutInflater inflater;


    public BuyDetailAdapter(Context context, List<Stock1> data) {
        this.ct = context;
        this.list = data;
        inflater = LayoutInflater.from(ct);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        ListItem item = null;
        if (view == null) {
            view = inflater.inflate(R.layout.buy_sale_item, null);
            item = new ListItem();
            item.buy_type = (TextView) view.findViewById(R.id.tv_volumn_buy);
            item.buy_price = (TextView) view.findViewById(R.id.tv_volumn_buy_price);
            item.buy_number = (TextView) view.findViewById(R.id.tv_volumn_buy_number);
            item.viewline = view.findViewById(R.id.item_view_line);
            view.setTag(item);
        } else {
            item = (ListItem) view.getTag();
        }
        if (position == 4) {
            item.viewline.setVisibility(View.VISIBLE);
        } else {
            item.viewline.setVisibility(View.GONE);
        }
        if (list.size()!=0 && list.get(position) != null) {
            Stock1 up = list.get(position);
            if(!up.isup()){
                item.buy_price.setTextColor(Color.rgb(7, 152, 0));
            } else {
                item.buy_price.setTextColor(Color.rgb(255, 24, 0));
            }

            item.buy_type.setText(up.getType());
            item.buy_price.setText(up.getPrice());
            item.buy_number.setText(up.getNumber());
        }
        return view;
    }

    public static class ListItem {
        TextView buy_type, buy_price, buy_number;
        View viewline;
    }


}

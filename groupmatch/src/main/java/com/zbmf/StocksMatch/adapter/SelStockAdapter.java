package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Quotation;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.SwipeLayout;

import org.json.JSONException;

import java.util.HashSet;

/**
 * 股票搜索单独的adapter
 * Created by lulu on 2016/1/4.
 */
public class SelStockAdapter extends ListAdapter<Stock> {
    public SelStockAdapter(Activity context) {
        super(context);
    }

    private HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.sear_stock_item1, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_symbol = (TextView) convertView.findViewById(R.id.tv_symbol);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Stock stock = mList.get(position);
        holder.tv_name.setText(stock.getName());
        holder.tv_symbol.setText(stock.getSymbol());


        return convertView;
    }


    static class ViewHolder {
        TextView tv_name,  tv_symbol;

    }
}

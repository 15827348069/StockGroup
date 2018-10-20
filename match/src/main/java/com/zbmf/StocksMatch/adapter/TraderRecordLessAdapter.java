package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.TraderInfo;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/3/23.
 */

public class TraderRecordLessAdapter extends ListAdapter<TraderInfo.Deals> {
    public TraderRecordLessAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_trader_deal_history;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getHolderView(int position, View convertView, TraderInfo.Deals traderDeals) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tvDealDate.setText(traderDeals.getCreated_at());
        holder.tvStockSybmol.setText(traderDeals.getAction() + " " + traderDeals.getName());
        return convertView;
    }
    public class ViewHolder {
        @BindView(R.id.tv_deal_date)
        TextView tvDealDate;
        @BindView(R.id.tv_stock_sybmol)
        TextView tvStockSybmol;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.zbmf.StocksMatch.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.DealSys;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xuhao
 * on 2017/11/9.  TraderDeailHistoryAdapter
 * 操盘高手不完整的成交记录 TraderRecordLessAdapter
 */

public class TraderDeailHistoryAdapter extends ListAdapter<DealSys> {

    public TraderDeailHistoryAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_trader_deal_history;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getHolderView(int position, View convertView, DealSys dealSys) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if (!TextUtils.isEmpty(dealSys.getDate())){
            holder.tvDealDate.setText(dealSys.getDate());
        }
        if (!TextUtils.isEmpty(dealSys.getAction())&&!TextUtils.isEmpty(dealSys.getStock_name())){
            holder.tvStockSybmol.setText(dealSys.getAction() + " " + dealSys.getStock_name());
        }
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

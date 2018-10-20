package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xuhao
 * on 2017/11/10.
 */

public class TraderHolderAdapter extends ListAdapter<TraderHolderPosition.Holds> {

    public TraderHolderAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_trader_hold;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public View getHolderView(int position, View convertView, TraderHolderPosition.Holds dealSys) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tvStockName.setText(dealSys.getName() + "(" + /*dealSys.getSymbol()+*/ ")");
        holder.tvStockPrice.setText(String.format("%.2f", Double.parseDouble(dealSys.getPrice())));
        holder.tvGainYield.setText(String.format("%+.2f%%", Double.parseDouble(dealSys.getGain_yield())));
        holder.tvStockNum.setText(dealSys.getVolumn_total() + "");
        if(Double.parseDouble(dealSys.getGain_yield())==0){
            holder.tvGainYield.setTextColor(mContext.getResources().getColor(R.color.black_66));
        }else{
            holder.tvGainYield.setTextColor(Double.parseDouble(dealSys.getGain_yield()) > 0 ?
                    mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.green));
        }
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.tv_stock_name)
        TextView tvStockName;
        @BindView(R.id.tv_stock_num)
        TextView tvStockNum;
        @BindView(R.id.tv_stock_price)
        TextView tvStockPrice;
        @BindView(R.id.tv_gain_yield)
        TextView tvGainYield;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

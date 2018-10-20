package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class QueryContentAdapter extends ListAdapter<DealsRecordList.Result.Stocks> {
    private Context mContext;
    public QueryContentAdapter(Activity context) {
        super(context);
        this.mContext=context;
    }

    @Override
    protected int getLayout() {
        return R.layout.item_match_hold;
    }

    @Override
    public View getHolderView(int position, View convertView, DealsRecordList.Result.Stocks stocks) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder==null){
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.holds_textView_count.setText(sellOrBuy(stocks.getType()));//2买入3卖出
        holder.holds_textView_available.setText(String.valueOf(stocks.getVolumn()));
        holder.holds_textView_currentPrice.setText(String.valueOf(stocks.getProfit()));
        double v = stocks.getPrice_sell() - stocks.getPrice_buy();
        double v1 = v / stocks.getPrice_buy();
        holder.holds_textView_floating.setText(stocks.getPrice_sell()==0?"--":String.format("%+.2f%%",v1 * 100));
        holder.holds_textView_transactionPrice.setText(stocks.getPrice_buy()==0?"--":String.valueOf(stocks.getPrice_buy()));
        holder.holds_textView_rate.setText(stocks.getPosted_at());
        holder.holds_textView_floating.setTextColor(v1 >= 0 ?
                mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.green));
        holder.holds_textView_currentPrice.setTextColor(stocks.getProfit() >= 0 ?
                mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.green));
        return convertView;
    }

    private String sellOrBuy(int i){
        return i==2?mContext.getString(R.string.buy_enter):mContext.getString(R.string.sell);
    }

    public class ViewHolder {
        @BindView(R.id.holds_textView_count)
        TextView holds_textView_count;
        @BindView(R.id.holds_textView_available)
        TextView holds_textView_available;
        @BindView(R.id.holds_textView_transactionPrice)
        TextView holds_textView_transactionPrice;
        @BindView(R.id.holds_textView_currentPrice)
        TextView holds_textView_currentPrice;
        @BindView(R.id.holds_textView_floating)
        TextView holds_textView_floating;
        @BindView(R.id.holds_textView_rate)
        TextView holds_textView_rate;
        public ViewHolder(View mView) {
            ButterKnife.bind(this, mView);
        }
    }
}

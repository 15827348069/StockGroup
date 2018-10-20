package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.OrderList;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/4/4.
 */

public class OrderContentListAdapter extends ListAdapter<OrderList.Result.Stocks> {
    public OrderContentListAdapter(Activity context) {
        super(context);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_match_hold;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getHolderView(int position, View convertView, OrderList.Result.Stocks stocks) {
        ViewHold hold= (ViewHold) convertView.getTag();
        if(hold==null){
            hold=new ViewHold(convertView);
            convertView.setTag(hold);
        }
        hold.holdsTextViewRate.setVisibility(View.GONE);
        hold.holdsTextViewCount.setText(stocks.getType()==0?"委托买入":"委托卖出");//持仓股数
        hold.holdsTextViewAvailable.setText(String.valueOf(stocks.getVolumn()));//可用股数
        hold.holdsTextViewTransactionPrice.setText(String.format("%.2f", stocks.getPrice()));//成本价
//        hold.holdsTextViewCurrentPrice.setText(String.format("%.2f", Float.parseFloat(stocks.getFrozen())));//当前价
        hold.holdsTextViewCurrentPrice.setText(stocks.getFrozen());//当前价
        String posted_at = stocks.getPosted_at();
        hold.holdsTextViewFloating.setText(posted_at);//浮动盈亏
//        hold.holdsTextViewRate.setText(String.format("%s", DoubleFromat.getStockDouble(Double.parseDouble(stocks.getGain_yield())*100,2)+"%"));
//        hold.holdsTextViewFloating.setTextColor(Double.parseDouble(stocks.
//                getGain())>0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
//        hold.holdsTextViewRate.setTextColor(Double.parseDouble(stocks.
//                getGain_yield())>0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
//        convertView.setBackgroundDrawable(position%2==0?mContext.getResources().
//                getDrawable(R.drawable.list_item_seletor_gray):mContext.getResources().getDrawable(R.drawable.list_item_seletor_white));
        return convertView;
    }
    public class ViewHold {
        @BindView(R.id.holds_textView_count)
        TextView holdsTextViewCount;
        @BindView(R.id.holds_textView_available)
        TextView holdsTextViewAvailable;
        @BindView(R.id.holds_textView_transactionPrice)
        TextView holdsTextViewTransactionPrice;
        @BindView(R.id.holds_textView_currentPrice)
        TextView holdsTextViewCurrentPrice;
        @BindView(R.id.holds_textView_floating)
        TextView holdsTextViewFloating;
        @BindView(R.id.holds_textView_rate)
        TextView holdsTextViewRate;
        public ViewHold(View view){
            ButterKnife.bind(this, view);
        }
    }
}

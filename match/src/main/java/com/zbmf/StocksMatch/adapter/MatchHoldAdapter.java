package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.worklibrary.adapter.ListAdapter;
import com.zbmf.worklibrary.util.DoubleFromat;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MatchHoldAdapter extends ListAdapter<HolderPositionBean.Result.Stocks> {

    public MatchHoldAdapter(Activity context) {
        super(context);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_match_hold;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getHolderView(int position, View convertView, HolderPositionBean.Result.Stocks stockholdsBean) {
        ViewHold hold= (ViewHold) convertView.getTag();
        if(hold==null){
            hold=new ViewHold(convertView);
            convertView.setTag(hold);
        }
        hold.holdsTextViewCount.setText(String.valueOf(stockholdsBean.getVolumn_total()));//持仓股数
        hold.holdsTextViewAvailable.setText(String.valueOf(stockholdsBean.getVolumn_unfrozen()));//可用股数
        hold.holdsTextViewTransactionPrice.setText(String.format("%.2f", stockholdsBean.getPrice_buy()));//成本价
        hold.holdsTextViewCurrentPrice.setText(String.format("%.2f", stockholdsBean.getCurrent()));//当前价
        hold.holdsTextViewFloating.setText(String.format("%.2f", stockholdsBean.getProfit()));//浮动盈亏
        hold.holdsTextViewRate.setText(String.format("%s", DoubleFromat.getStockDouble(stockholdsBean.getYield_float()*100,2)+"%"));
        hold.holdsTextViewFloating.setTextColor(stockholdsBean.
                getProfit()>0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
        hold.holdsTextViewRate.setTextColor(stockholdsBean.
                getYield_float()>0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
        convertView.setBackgroundDrawable(position%2==0?mContext.getResources().
                getDrawable(R.drawable.list_item_seletor_gray):mContext.getResources().getDrawable(R.drawable.list_item_seletor_white));
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

package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class DealsContentAdapter extends ListAdapter<TraderDeals.Result.Deals> {
    public DealsContentAdapter(Activity context) {
        super(context);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_match_hold;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getHolderView(int position, View convertView, TraderDeals.Result.Deals deals) {
        ViewHold hold= (ViewHold) convertView.getTag();
        if(hold==null){
            hold=new ViewHold(convertView);
            convertView.setTag(hold);
        }
        hold.holdsTextViewCount.setText(String.valueOf(deals.getAction()));//类型
        hold.holdsTextViewAvailable.setText(String.valueOf(deals.getVolumn()));//数量
        hold.holdsTextViewTransactionPrice.setText(String.format("%.2f", Float.parseFloat(deals.getPrice())));//成交价格
        if (deals.getGain().equals("-")||deals.getGain().contains("-")&&!deals.getGain().contains(".")){
            hold.holdsTextViewCurrentPrice.setText(deals.getGain());//单笔盈亏额
        }else {
            hold.holdsTextViewCurrentPrice.setText(deals.getGain());//单笔盈亏额
            hold.holdsTextViewCurrentPrice.setTextColor(Double.parseDouble(deals.
                    getGain())>=0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
        }
        if (deals.getGain_yield().equals("-")||deals.getGain_yield().contains("-")&&!deals.getGain_yield().contains(".")){
            hold.holdsTextViewFloating.setText(deals.getGain_yield());
        }else {
            double v = Double.parseDouble(deals.getGain_yield());
            hold.holdsTextViewFloating.setText(String.format("%+.2f%%",v * 100));//单笔盈亏比
            hold.holdsTextViewFloating.setTextColor(v>=0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
        }
        hold.holdsTextViewRate.setText(String.format("%s",deals.getCreated_at()));//成交时间

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

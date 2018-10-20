package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.MatchBean;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 我的比赛列表适配器-
 * Created by lulu on 15/12/28.
 */
public class MatchAdapter extends ListAdapter<MatchBean> {

    private LayoutInflater inflater;
    private DecimalFormat df = new DecimalFormat("######0.00");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    public MatchAdapter(Activity context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.match_item1, null);
            holder.name = (TextView) view.findViewById(R.id.match_name);//赛名
            holder.people_number = (TextView) view.findViewById(R.id.match_people_number);//参与数
            holder.arrow = (TextView) view.findViewById(R.id.match_arrow);//排名
            holder.all_money = (TextView) view.findViewById(R.id.all_money);//总资产
            holder.yield = (TextView) view.findViewById(R.id.match_yield);//收益率
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MatchBean match = mList.get(position);
        holder.name.setText(match.getTitle());
        holder.people_number.setText(match.getPlayers());
        holder.arrow.setText(mContext.getResources().getString(R.string.rank) + match.getTotal_rank());
        holder.all_money.setText(currencyFormat.format(match.getMoney()));
        if (match.getYield() > 0) {
            holder.yield.setTextColor(mContext.getResources().getColor(R.color.match_all_money));
            holder.all_money.setTextColor(mContext.getResources().getColor(R.color.match_all_money));
//			holder.money.setTextColor(context.getResources().getColor(R.color.match_all_money));
            holder.yield.setText("+" + df.format(match.getYield() * 100) + "%");
        } else if (match.getYield() == 0) {
            holder.yield.setTextColor(mContext.getResources().getColor(R.color.c6));
            holder.all_money.setTextColor(mContext.getResources().getColor(R.color.c6));
//			holder.money.setTextColor(context.getResources().getColor(R.color.match_all_money));
            holder.yield.setText("+" + df.format(match.getYield() * 100) + "%");
        } else {
            holder.yield.setTextColor(mContext.getResources().getColor(R.color.match_all_money_green));
            holder.all_money.setTextColor(mContext.getResources().getColor(R.color.match_all_money_green));
//			holder.money.setTextColor(context.getResources().getColor(R.color.match_all_money_green));
            holder.yield.setText(df.format(match.getYield() * 100) + "%");
        }

        return view;
    }

    static class ViewHolder {
        TextView name, people_number, arrow, all_money, yield;
    }
}

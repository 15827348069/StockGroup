package com.zbmf.StockGroup.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Rank;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.ShowActivity;

/**
 * Created by pq
 * on 2018/6/4.
 */

public class UserDetailRankAdapter extends ListAdapter<Rank> {
    private Context mContext;
    private Activity mActivity;

    public UserDetailRankAdapter(Context context, Activity activity) {
        super(context);
        mContext = context;
        mActivity=activity;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_rank_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Rank rank = getItem(position);
        holder.comment.setText(String.format("选股理由:%s",rank.getReason()));
        holder.mStockName.setText(rank.getStock_name());
        holder.mStockSymbol.setText(rank.getSymbol());
        holder.mTime.setText(String.format(rank.getStart_at()+"%s%s","~", rank.getEnd_at()));
        holder.mWeekRank.setText(String.format("第%s", rank.getWeek_rank() + "名"));
        holder.mQi.setText(String.format("(第%s", rank.getRound() + "周)"));
        holder.mRiskSize.setText(rank.getHigh_yield()>=0?"+"+(DoubleFromat.getDouble(rank.getHigh_yield()*100,2))+"%":
                (DoubleFromat.getDouble(rank.getHigh_yield()*100,2))+"%");
        holder.mTotalRiskSize.setText(rank.getWeek_yield()>=0?"+"+(DoubleFromat.getDouble(rank.getWeek_yield()*100,2))+"%":
                (DoubleFromat.getDouble(rank.getWeek_yield()*100,2))+"%");
        holder.mRiskSize.setTextColor(rank.getHigh_yield()>=0?mContext.getResources()
                .getColor(R.color.red):mContext.getResources().getColor(R.color.green));
        holder.mTotalRiskSize.setTextColor(rank.getWeek_yield()>=0?mContext.getResources()
                .getColor(R.color.red):mContext.getResources().getColor(R.color.green));
        holder.share_stock_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showNuggetsShareActivity(mActivity,null,rank,2);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private View mView;
        private ImageView share_stock_model;
        private final TextView mStockName, mRiskSize, mTotalRiskSize, mWeekRank, mStockSymbol, mTime, mQi, comment;

        ViewHolder(View view) {
            mView = view;
            mStockName = getView(R.id.stockName);
            mRiskSize = getView(R.id.riskSize);
            mTotalRiskSize = getView(R.id.totalRiskSize);
            mWeekRank = getView(R.id.weekRank);
            mStockSymbol = getView(R.id.stockSymbol);
            mTime = getView(R.id.time);
            comment = getView(R.id.comment);
            share_stock_model = getView(R.id.share_stock_model);
            mQi = getView(R.id.qi);
        }

        protected <T extends View> T getView(int resourcesId) {
            try {
                return (T) mView.findViewById(resourcesId);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

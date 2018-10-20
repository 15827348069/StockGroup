package com.zbmf.StockGroup.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Rank;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

/**
 * Created by pq
 * on 2018/6/4.
 */

public class StockMatchAdapter extends ListAdapter<Rank> {
    private Context mContext;
    private Activity mActivity;

    public StockMatchAdapter(Context context, Activity activity) {
        super(context);
        mContext = context;
        mActivity = activity;
    }

    @SuppressLint({"ViewHolder", "DefaultLocale"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_match_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Rank rank = getItem(position);
        int isVip = SettingDefaultsManager.getInstance().getIsVip();
        if (isVip==1 &&!TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())){
            if (position == 0) {
                holder.share2.setVisibility(View.GONE);
                holder.mRiskIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_gold));
                holder.share1.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                holder.share1.setVisibility(View.VISIBLE);
                holder.mRiskIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_silver));
                holder.share2.setVisibility(View.GONE);
            } else if (position == 2) {
                holder.share1.setVisibility(View.VISIBLE);
                holder.mRiskIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_copper));
                holder.share2.setVisibility(View.GONE);
            } else if (position>2){
                holder.share1.setVisibility(View.GONE);
                holder.share2.setVisibility(View.VISIBLE);
                holder.rankTV.setText(String.valueOf(position + 1));
            }
        }else {
            holder.share1.setVisibility(View.GONE);
            holder.share2.setVisibility(View.VISIBLE);
            holder.rankTV.setText(String.valueOf(position + 4));
        }
        holder.mStockName.setText(rank.getStock_name());
        holder.mStockSymbol.setText(rank.getSymbol());
        holder.userName.setText(rank.getNickname());
        holder.comment.setText(String.format("选股理由:%s", rank.getReason()));
        holder.mRiskSize.setText(rank.getWeek_yield()>=0?"+"+(DoubleFromat.getDouble(rank.getWeek_yield()*100,2))+"%":
                (DoubleFromat.getDouble(rank.getWeek_yield()*100,2))+"%");
        holder.mWeek_jifen.setText(String.format("%.2f", rank.getWeek_score()));
        holder.mRiskSize.setTextColor(rank.getWeek_yield() >=0 ? mContext.getResources()
                .getColor(R.color.red) : mContext.getResources().getColor(R.color.green));
//        ViewFactory.getRoundImgView(parent.getContext(),rank.getAvatar(), holder.mAvator);
        ImageLoader.getInstance().displayImage(rank.getAvatar(), holder.mAvator, ImageLoaderOptions.RoundedBitMapoptios());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserDetail != null/*&& SettingDefaultsManager.getInstance().getIsVip()==1*/) {
                    mUserDetail.skipUserDetail(rank.getUser_id(),position);
                }
            }
        });
        holder.share_stock_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showNuggetsShareActivity(mActivity,null,rank,2);
            }
        });
        holder.share_stock_model1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showNuggetsShareActivity(mActivity,null,rank,2);
            }
        });
        return convertView;
    }


    public static class ViewHolder {
        private View mView;
        private final ImageView mRiskIV,share_stock_model,share_stock_model1;
        private final RoundedCornerImageView mAvator;
        private final RelativeLayout itemView;
        private final TextView mStockName, mStockSymbol, mRiskSize, mWeek_jifen, rankTV, userName, comment;
        private final LinearLayout share1,share2;
        ViewHolder(View view) {
            mView = view;
            mRiskIV = getView(R.id.rank_iv);
            mAvator = getView(R.id.avator);
            mStockName = getView(R.id.stockName);
            mStockSymbol = getView(R.id.stockSymbol);
            mRiskSize = getView(R.id.riskSize);
            mWeek_jifen = getView(R.id.week_jifen);
            rankTV = getView(R.id.rankTV);
            userName = getView(R.id.userName);
            comment = getView(R.id.comment);
            itemView = getView(R.id.itemView);
            share1 = getView(R.id.share1);
            share2 = getView(R.id.share2);
            share_stock_model = getView(R.id.share_stock_model);
            share_stock_model1 = getView(R.id.share_stock_model1);
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

    public interface UserDetail {
        void skipUserDetail(int userID,int position);
    }

    private UserDetail mUserDetail;

    public void setUserDetail(UserDetail user) {
        mUserDetail = user;
    }
}

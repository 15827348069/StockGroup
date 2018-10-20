package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.interfaces.OnAdapterClickListener;
import com.zbmf.StocksMatch.bean.MatchRank;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 根据条目进行分类
 */
public class MatchRankAdapter extends ListAdapter<MatchRank.Result.Yields> {

    private OnAdapterClickListener adapterClickListener;
    private Context mContext;
    private int flag;
    public MatchRankAdapter(Activity context) {
        super(context);
        this.mContext=context;
    }

    @Override
    protected int getLayout() {
        return R.layout.stock_rank_flow;
    }

    public void setFlag(int flag){
        this.flag=flag;
    }
    public void setAdapterClickListener(OnAdapterClickListener adapterClickListener) {
        this.adapterClickListener = adapterClickListener;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public View getHolderView(int position, View convertView, MatchRank.Result.Yields yield) {
        ViewHolder holder=(ViewHolder) convertView.getTag();
        if (holder == null) {
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if(position==0){
            holder.imvRankImg.setVisibility(View.VISIBLE);
            holder.imvRankImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_gold));
            holder.tvRank.setVisibility(View.INVISIBLE);
        }else if(position==1){
            holder.imvRankImg.setVisibility(View.VISIBLE);
            holder.imvRankImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_silver));
            holder.tvRank.setVisibility(View.INVISIBLE);
        }else if(position==2){
            holder.imvRankImg.setVisibility(View.VISIBLE);
            holder.imvRankImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_copper));
            holder.tvRank.setVisibility(View.INVISIBLE);
        }else{
            holder.tvRank.setVisibility(View.VISIBLE);
            holder.imvRankImg.setVisibility(View.GONE);
            holder.tvRank.setText(String.valueOf(position+1));
        }

        holder.tvNickname.setText(yield.getNickname());
        String yieldStr="";
        switch (flag){
            case Constans.DAY_RANK:
                double day_yield = yield.getDay_yield();
                holder.tvYield.setTextColor(day_yield>=0?mContext.getResources().getColor(R.color.err_red)
                        :mContext.getResources().getColor(R.color.green));
                yieldStr=String.format("%+.2f%%", day_yield*100);
                break;
            case Constans.WEEK_RANK:
                double week_yield = yield.getWeek_yield();
                holder.tvYield.setTextColor(week_yield>=0?mContext.getResources().getColor(R.color.err_red)
                        :mContext.getResources().getColor(R.color.green));
                yieldStr=String.format("%+.2f%%", week_yield*100);
                break;
            case Constans.MOUNTH_RANK:
                double month_yield = yield.getMonth_yield();
                holder.tvYield.setTextColor(month_yield>=0?mContext.getResources().getColor(R.color.err_red)
                        :mContext.getResources().getColor(R.color.green));
                yieldStr=String.format("%+.2f%%", month_yield*100);
                break;
            case Constans.ALL_RANK:
                double total_yield = yield.getTotal_yield();
                holder.tvYield.setTextColor(total_yield>=0?mContext.getResources().getColor(R.color.err_red)
                        :mContext.getResources().getColor(R.color.green));
                yieldStr=String.format("%+.2f%%", total_yield*100);
                break;
        }
        if (!TextUtils.isEmpty(yieldStr)){
            holder.tvYield.setText(yieldStr);
        }else {
            holder.tvYield.setText(yieldStr);
        }
        MatchRank.Result.Yields.Last_deal last_deal = yield.getLast_deal();
        if(last_deal!=null){
            holder.tvAction.setText(last_deal.getType()!=null?last_deal.getType().replace(" ",""):"");
            holder.tvSymbol.setText(last_deal.getName()!=null?last_deal.getName():last_deal.getName()!=null?last_deal.getName():"");
        }
        Glide.with(mContext).load(yield.getAvatar()).apply(GlideOptionsManager.getInstance().getRequestOptions()).into(holder.ivRcv);
        return convertView;
    }


    public class ViewHolder {
        @BindView(R.id.imv_rank_img)
        ImageView imvRankImg;
        @BindView(R.id.tv_rank)
        TextView tvRank;
        @BindView(R.id.iv_rcv)
        ImageView ivRcv;
        @BindView(R.id.tv_nickname)
        TextView tvNickname;
        @BindView(R.id.tv_yield)
        TextView tvYield;
        @BindView(R.id.tv_action)
        TextView tvAction;
        @BindView(R.id.tv_symbol)
        TextView tvSymbol;
        @BindView(R.id.tv_common)
        TextView tvCommon;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

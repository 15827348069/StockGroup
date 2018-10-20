package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.worklibrary.adapter.ListAdapter;
import com.zbmf.worklibrary.util.DoubleFromat;

/**
 * Created by pq
 * on 2018/3/28.
 */

public class TradingAdapter extends ListAdapter<Traders> {
    public TradingAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_trading_layout;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getHolderView(int position, View convertView, Traders traders) {
        ViewHolder holder= (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
//        Traders traders= (Traders) getItem(position);
        if(traders.getRank()==1){
            holder.imv_rank_img.setVisibility(View.VISIBLE);
            holder.imv_rank_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_gold));
            holder.tv_rank.setVisibility(View.INVISIBLE);
        }else if(traders.getRank()==2){
            holder.imv_rank_img.setVisibility(View.VISIBLE);
            holder.imv_rank_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_silver));
            holder.tv_rank.setVisibility(View.INVISIBLE);
        }else if(traders.getRank()==3){
            holder.imv_rank_img.setVisibility(View.VISIBLE);
            holder.imv_rank_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_copper));
            holder.tv_rank.setVisibility(View.INVISIBLE);
        }else{
            holder.tv_rank.setVisibility(View.VISIBLE);
            holder.imv_rank_img.setVisibility(View.GONE);
            holder.tv_rank.setText(String.valueOf(traders.getRank()));
        }
        holder.tv_yield.setTextColor(traders.getTotal_yield()>0?mContext.getResources().
                getColor(R.color.colorAccent):mContext.getResources().getColor(R.color.green));
        Glide.with(mContext).load(traders.getAvatar()).apply(GlideOptionsManager.getInstance().getRequestOptions()).into(holder.iv_rcv);
        holder.tv_nickname.setText(traders.getNickname());
        holder.tv_yield.setText((traders.getTotal_yield()>0?"+":"")+(DoubleFromat.getStockDouble(traders.getTotal_yield()*100,2)+"%"));
        holder.tv_action.setText((DoubleFromat.getStockDouble(traders.getDeal_success()*100,2))+"%");
        holder.tv_total_num.setText(traders.getDeal_total());
        return convertView;
    }
    private class ViewHolder{
        ImageView imv_rank_img,iv_rcv;
        TextView tv_rank,tv_nickname,tv_yield,tv_action,tv_total_num;
        public ViewHolder(View view){
            imv_rank_img= (ImageView) view.findViewById(R.id.imv_rank_img);
            iv_rcv= (ImageView) view.findViewById(R.id.iv_rcv);
            tv_rank= (TextView) view.findViewById(R.id.tv_rank);
            tv_nickname= (TextView) view.findViewById(R.id.tv_nickname);
            tv_yield= (TextView) view.findViewById(R.id.tv_yield);
            tv_action= (TextView) view.findViewById(R.id.tv_action);
            tv_total_num= (TextView) view.findViewById(R.id.tv_total_num);
        }
    }
}

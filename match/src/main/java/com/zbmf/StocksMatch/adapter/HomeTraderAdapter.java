package com.zbmf.StocksMatch.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.worklibrary.pullrefreshrecycle.adapter.BaseListRecyclerAdapter;
import com.zbmf.worklibrary.pullrefreshrecycle.holder.RecyclerHolder;
import com.zbmf.worklibrary.util.DoubleFromat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuhao on 2017/11/27.
 */

public class HomeTraderAdapter extends BaseListRecyclerAdapter<Traders, HomeTraderAdapter.ViewHolder> {


    public HomeTraderAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.item_trader_layout;
    }

    @Override
    public ViewHolder createViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, final Traders traders, int position) {
        holder.tvTraderName.setText(traders.getNickname());
        holder.tvTraderYield.setText((traders.getDeal_success() >= 0 ? "+" : "") + (DoubleFromat.getDouble(traders.getDeal_success() * 100, 2) + "%"));
        Glide.with(getContext()).load(traders.getAvatar()).apply(GlideOptionsManager.getInstance().getRequestOptions()).into(holder.imvTraderAvatar);
        holder.llTrader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemClickListener() != null) {
                    getItemClickListener().onItemClick(traders);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerHolder {
        @BindView(R.id.imv_trader_avatar)
        ImageView imvTraderAvatar;
        @BindView(R.id.tv_trader_name)
        TextView tvTraderName;
        @BindView(R.id.tv_trader_yield)
        TextView tvTraderYield;
        @BindView(R.id.ll_trader)
        LinearLayout llTrader;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private onItemClickListener itemClickListener;

    public onItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(Traders traders);
    }
}

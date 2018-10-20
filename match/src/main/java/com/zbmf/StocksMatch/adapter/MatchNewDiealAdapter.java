package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.interfaces.OnAdapterClickListener;
import com.zbmf.StocksMatch.bean.DealSys;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public class MatchNewDiealAdapter extends ListAdapter<DealSys> {
    private Context mContext;

    public MatchNewDiealAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected int getLayout() {
        return R.layout.item_new_dieal;
    }

    @Override
    public View getHolderView(int position, View convertView, final DealSys/*final RecentDeal.Result.Deals*/ dealSys) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        String s = "";
        if (dealSys.getType() == 1) {
            s = "卖出";
        } else if (dealSys.getType() == 2) {
            s = "买入";
        }
        String posted_at = dealSys.getPosted_at();
        int i = posted_at.indexOf("-");
        int i1 = posted_at.lastIndexOf(":");
        String date = posted_at.substring(i+1, i1);
        holder.tvNickname.setText(dealSys.getNickname());
        holder.tvDealContent.setText(String.format(mContext.getResources().getString(R.string.match_dieal_stock), date, s,
                dealSys.getName(), String.valueOf(dealSys.getSymbol())));
        Glide.with(mContext).load(dealSys.getAvatar()).apply(GlideOptionsManager.getInstance()
                .getRequestOptions()).into(holder.ivRcv1);
        holder.tvTradeFollowBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAdapterClickListener != null) {
                    onAdapterClickListener.onClickClBuyListener(dealSys);
                }
            }
        });
        holder.tvFollowBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAdapterClickListener != null) {
                    onAdapterClickListener.onClickBuyListener(dealSys);
                }
            }
        });
//        holder.tvComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(onAdapterClickListener!=null){
//                    onAdapterClickListener.onClickCommentListener(dealSys);
//                }
//            }
//        });
        return convertView;
    }

    public static class ViewHolder {

        @BindView(R.id.iv_rcv1)
        ImageView ivRcv1;
        @BindView(R.id.tv_nickname)
        TextView tvNickname;
        @BindView(R.id.tv_deal_content)
        TextView tvDealContent;
        @BindView(R.id.tv_trade_follow_buy)
        TextView tvTradeFollowBuy;
        @BindView(R.id.tv_follow_buy)
        TextView tvFollowBuy;

        //        @BindView(R.id.tv_comment)
//        TextView tvComment;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private OnAdapterClickListener onAdapterClickListener;

    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        this.onAdapterClickListener = onAdapterClickListener;
    }
}

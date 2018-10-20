package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockComments;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

/**
 * Created by xuhao on 2017/10/25.
 */

public class LatestCommentAdapter extends ListAdapter {
    OnAdapterClickListener onAdapterClickListener;
    public interface  OnAdapterClickListener{
        void onAdapterClickListener(View v,StockholdsBean stockholdsBean);
    }
    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        this.onAdapterClickListener = onAdapterClickListener;
    }

    public LatestCommentAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder=null;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_stock_latest_layout,null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        StockComments stockComments= (StockComments) getList().get(position);
        holder.tv_nickname.setText(stockComments.getNickname());
        holder.tv_content.setText(stockComments.getDesc());
        holder.tv_date.setText(stockComments.getCreate_at());
        final StockholdsBean dealSys=stockComments.getDealSys();
        holder.tv_symbol.setText(dealSys.getName()+"("+dealSys.getSymbol()+")");
        holder.tv_comment.setText(dealSys.getComment_count()+"");
//        ViewFactory.imgCircleView(mContext,stockComments.getUser_img(),holder.iv_rcv);
        ImageLoader.getInstance().displayImage(stockComments.getUser_img(),holder.iv_rcv, ImageLoaderOptions.AvatarOptions());
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAdapterClickListener!=null){
                    onAdapterClickListener.onAdapterClickListener(v,dealSys);
                }
            }
        };
        holder.tv_trade_follow_buy.setOnClickListener(onClickListener);
        holder.tv_follow_buy.setOnClickListener(onClickListener);
        holder.tv_comment.setOnClickListener(onClickListener);
        return view;
    }
    private class ViewHolder{
        RoundedCornerImageView iv_rcv;
        TextView tv_nickname,tv_content,tv_date,tv_symbol,tv_trade_follow_buy,tv_follow_buy,tv_comment;
        public ViewHolder(View view){
            iv_rcv= (RoundedCornerImageView) view.findViewById(R.id.iv_rcv);
            tv_nickname= (TextView) view.findViewById(R.id.tv_nickname);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            tv_date= (TextView) view.findViewById(R.id.tv_date);
            tv_symbol= (TextView) view.findViewById(R.id.tv_symbol);
            tv_trade_follow_buy= (TextView) view.findViewById(R.id.tv_trade_follow_buy);
            tv_follow_buy= (TextView) view.findViewById(R.id.tv_follow_buy);
            tv_comment= (TextView) view.findViewById(R.id.tv_comment);
        }
    }
}

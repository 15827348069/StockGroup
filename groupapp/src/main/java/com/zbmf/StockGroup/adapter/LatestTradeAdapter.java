package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.interfaces.OnAdapterClickListener;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;


public class LatestTradeAdapter extends BaseAdapter {

    private List<DealSys> oneStockCommits;
    private Context cxt;
    private OnAdapterClickListener onAdapterClickListener;

    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        this.onAdapterClickListener = onAdapterClickListener;
    }

    public LatestTradeAdapter(Context cxt, List<DealSys> oneStockCommits) {
        this.oneStockCommits = oneStockCommits;
        this.cxt = cxt;
    }

    @Override
    public int getCount() {
        return oneStockCommits.size();
    }

    @Override
    public Object getItem(int position) {
        return oneStockCommits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(cxt).inflate(R.layout.latest_stock_buy, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        final DealSys dealSys=oneStockCommits.get(position);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAdapterClickListener!=null){
                    onAdapterClickListener.onClickListener(v,dealSys);
                }
            }
        };
        holder.tv_trade_follow_buy.setOnClickListener(onClickListener);
        holder.tv_follow_buy.setOnClickListener(onClickListener);
        holder.tv_comment.setOnClickListener(onClickListener);
        holder.tv_comment.setText(dealSys.getCount_comments());
        holder.tv_nickname.setText(dealSys.getNickname());
        holder.tv_deal_content.setText(dealSys.getDate()+dealSys.getAction()+dealSys.getStock_name()+"("+dealSys.getSumbol()+")");
//        ViewFactory.imgCircleView(parent.getContext(),dealSys.getUser_img(),holder.iv_rcv1);
        ImageLoader.getInstance().displayImage(dealSys.getUser_img(),holder.iv_rcv1, ImageLoaderOptions.AvatarOptions());
        return convertView;
    }

    private class ViewHolder {
        private RoundedCornerImageView iv_rcv1;
        private TextView tv_trade_follow_buy, tv_follow_buy, tv_comment,tv_nickname,tv_deal_content;
        public ViewHolder(View view){
            this.tv_trade_follow_buy = (TextView) view.findViewById(R.id.tv_trade_follow_buy);
            this.tv_follow_buy = (TextView) view.findViewById(R.id.tv_follow_buy);
            this.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            this.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            this.tv_deal_content = (TextView) view.findViewById(R.id.tv_deal_content);
            this.iv_rcv1 = (RoundedCornerImageView) view.findViewById(R.id.iv_rcv1);
        }
    }

}

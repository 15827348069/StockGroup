package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockholdsBean;

/**
 * Created by xuhao on 2017/10/24.
 */

public class ChatStockHoldAdapter extends ListAdapter<StockholdsBean> {
    private LayoutInflater inflater;
    private OnCommit onClickListener;
    public void setOnClickListener(OnCommit onClickListener){
        this.onClickListener=onClickListener;
    }
    public ChatStockHoldAdapter(Activity context) {
        super(context);
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder=null;
        if(view==null){
            view=inflater.inflate(R.layout.chat_stock_hold_i,null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        final StockholdsBean sb=getList().get(position);
        double buy = sb.getPrice_buy();
        double current = sb.getCuurent();
        double rate = sb.getYield_float()*100;
        holder.tv_price.setText(String.format("%.2f", buy));
        holder.tv_price2.setText(String.format("%.2f", current));
        holder.tv_yield.setText(String.format("%+.2f%%", rate));
        holder.tv_name.setText(sb.getName());
        holder.tv_commit.setText(sb.getComment_count()+"");
        if (rate >= 0)
        {
            holder.tv_yield.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            holder.tv_yield.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        if(onClickListener!=null){
            holder.tv_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onCommit(sb);
                }
            });
        }
        return view;
    }
    private class ViewHolder{
        private TextView tv_name,tv_yield,tv_price,tv_price2,tv_commit;
        public ViewHolder(View view){
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_yield= (TextView) view.findViewById(R.id.tv_yield);
            tv_price= (TextView) view.findViewById(R.id.tv_price);
            tv_price2= (TextView) view.findViewById(R.id.tv_price2);
            tv_commit= (TextView) view.findViewById(R.id.tv_commit);
        }
    }
    public interface OnCommit{
        void onCommit(StockholdsBean stockholdsBean);
    }
}

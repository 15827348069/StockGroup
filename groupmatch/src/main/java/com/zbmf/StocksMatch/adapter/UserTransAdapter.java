package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.utils.UiCommon;

/**
 * Created by lulu on 16/1/17.
 */
public class UserTransAdapter extends ListAdapter<StockholdsBean>{
    private String match_id;

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public UserTransAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.usertrans_item,null);
            holder = new ViewHolder();
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_symbol = (TextView) convertView.findViewById(R.id.tv_symbol);//股票名字+代码
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);//成交价
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);// 买/卖
            holder.tv_transnum = (TextView) convertView.findViewById(R.id.tv_transnum);//交易数量
            holder.btn_op = (TextView) convertView.findViewById(R.id.tv_op);//交易数量
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final StockholdsBean sb = mList.get(position);

        holder.tv_date.setText(sb.getPosted_at().substring(5, 10));


        if("0".equals(sb.getIs_show())){//需要购买
            holder.tv_symbol.setText("***"+"("+sb.getSymbol().substring(0,3)+"***)");
            holder.tv_price.setText(mContext.getString(R.string.cjj, "***"));
            holder.btn_op.setVisibility(View.VISIBLE);
            holder.btn_op.setText("查看");
            holder.tv_type.setText(R.string.buy_in);

        } else if (sb.getType().equals("2")){
            holder.tv_type.setText(R.string.buy_in);
            holder.btn_op.setVisibility(View.VISIBLE);
            holder.tv_symbol.setText(sb.getName() + "(" + sb.getSymbol() + ")");
            holder.tv_price.setText(mContext.getString(R.string.cjj, String.format("%.2f", sb.getPrice_buy())));
            holder.btn_op.setText("跟买");

        }else{
            holder.btn_op.setVisibility(View.INVISIBLE);
            holder.tv_type.setText(R.string.sell_out);
            holder.tv_symbol.setText(sb.getName() + "(" + sb.getSymbol() + ")");
            holder.tv_price.setText(mContext.getString(R.string.cjj, String.format("%.2f", sb.getPrice_sell())));

        }

        holder.tv_transnum.setText(mContext.getString(R.string.tran_num, sb.getVolumn()));
        return convertView;
    }

    static class ViewHolder{
        TextView tv_date,tv_symbol,tv_price,tv_type,tv_transnum,btn_op;
    }


}

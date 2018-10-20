package com.zbmf.StocksMatch.adapter;

import java.util.List;

import javax.crypto.spec.PSource;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.StockholdsBean;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RightContentAdapter extends ListAdapter<StockholdsBean> {
	private LayoutInflater inflater;

	public RightContentAdapter(Activity context) {
		super(context);
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItem item=null;
		if(view==null){
			view=inflater.inflate(R.layout.hold_item, null);
			item=new ListItem();
			item.holds_textView_count=(TextView) view.findViewById(R.id.holds_textView_count);
			item.holds_textView_available=(TextView) view.findViewById(R.id.holds_textView_available);
			item.holds_textView_transactionPrice=(TextView) view.findViewById(R.id.holds_textView_transactionPrice);
			item.holds_textView_currentPrice=(TextView) view.findViewById(R.id.holds_textView_currentPrice);
			item.holds_textView_floating=(TextView) view.findViewById(R.id.holds_textView_floating);
			item.holds_textView_rate=(TextView) view.findViewById(R.id.holds_textView_rate);
			view.setTag(item);
		}else{
			item=(ListItem) view.getTag();
		}
		StockholdsBean sb=mList.get(position);
		double buy = sb.getPrice_buy();
		double current = sb.getCuurent();
		double change = sb.getProfit();
		double rate = sb.getYield_float()*100;
		item.holds_textView_count.setText(sb.getVolumn_total());
		item.holds_textView_available.setText(sb.getVolumn_infrozen());
		item.holds_textView_transactionPrice.setText(String.format("%.2f", buy));
		item.holds_textView_currentPrice.setText(String.format("%.2f", current));
		item.holds_textView_floating.setText(String.format("%+.2f", change));
		item.holds_textView_rate.setText(String.format("%+.2f%%", rate));
		if (change >= 0) 
		{
			item.holds_textView_floating.setTextColor(mContext.getResources().getColor(R.color.red));
			item.holds_textView_rate.setTextColor(mContext.getResources().getColor(R.color.red));
		} else {
			item.holds_textView_floating.setTextColor(mContext.getResources().getColor(R.color.green));
			item.holds_textView_rate.setTextColor(mContext.getResources().getColor(R.color.green));
		}
		if (position%2!=0) {
			view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_item_seletor_white));
		}else{
			view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_item_seletor_gray));
		}
		return view;
	}
	public class ListItem{
		TextView holds_textView_count ;

		// 可用股
		TextView holds_textView_available;

		//成交价
		TextView holds_textView_transactionPrice ;

		//当前价
		TextView holds_textView_currentPrice ;

		// 浮动盈亏
		TextView holds_textView_floating ;

		// 盈亏比率
		TextView holds_textView_rate ;
	}
}

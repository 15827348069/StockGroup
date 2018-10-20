package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockholdsBean;

public class TrustsAdapter extends ListAdapter<StockholdsBean> {

	public TrustsAdapter(Activity context) {
		super(context);
	}


	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItem item=null;
		if(view==null){
			item=new ListItem();
			view=LayoutInflater.from(mContext).inflate(R.layout.item_trusts_layout, null);
			 item.trustsType = (TextView) view.findViewById(R.id.trusts_cell_type);
			 item.trustsCount = (TextView) view.findViewById(R.id.trusts_textView_count);
			 item.trustsPrice = (TextView) view.findViewById(R.id.trusts_textView_price);
			 item.trustsMoney = (TextView) view.findViewById(R.id.trusts_textView_money);
			 item.trustsDate = (TextView) view.findViewById(R.id.trusts_textView_date);
			view.setTag(item);
		}else{
			item=(ListItem) view.getTag();
		}
		StockholdsBean sb=mList.get(position);
		if(sb.getType().equals("1")){
			item.trustsType.setText("卖出");
		}else{
			item.trustsType.setText("买入");
		}
		// 数量
		item.trustsCount.setText(sb.getVolumn());

		// 成交价格
		item.trustsPrice.setText(String.format("%.2f", sb.getPrice_buy()));
		// 数量
		item.trustsCount.setText(sb.getVolumn());

		// 委托价格
		item.trustsPrice.setText(sb.getPrice2()+"");

		// 冻结金额
		item.trustsMoney.setText(sb.getFrozen());

		// 委托时间
		item.trustsDate.setText(sb.getPosted_at().substring(5, 16));
		// 成交时间
		item.trustsDate.setText(sb.getPosted_at().substring(5, 16));
		if (position%2!=0) {
			view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_item_seletor_white));
		}else{
			view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_item_seletor_gray));
		}
		return view;
	}
	public class ListItem{
		// 类型
		TextView trustsType ;

		// 数量
		TextView trustsCount;

		// 成交价格
		TextView trustsPrice;

		//单笔盈亏额
		TextView trustsMoney;

		// 成交时间
		TextView trustsDate;
	}
}

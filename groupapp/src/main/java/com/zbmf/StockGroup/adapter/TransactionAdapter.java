package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.utils.DisplayUtil;


public class TransactionAdapter extends ListAdapter<StockholdsBean> {

	public TransactionAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItem item=null;
		if(view==null){
			item=new ListItem();
			view=LayoutInflater.from(mContext).inflate(R.layout.item_transactions_layout, null);
			// 类型
			item.transactions_textView_type = (TextView) view.findViewById(R.id.transactions_textView_type);

			// 数量
			item.transactions_textView_count = (TextView) view.findViewById(R.id.transactions_textView_count);

			// 成交价格
			item.transactions_textView_price = (TextView) view.findViewById(R.id.transactions_textView_price);

			//单笔盈亏额
			item.transactions_textView_money = (TextView) view.findViewById(R.id.transactions_textView_money);

			// 单笔盈亏比
			item.transactions_textView_rate = (TextView) view.findViewById(R.id.transactions_textView_rate);

			// 成交时间
			item.transactions_textView_date = (TextView) view.findViewById(R.id.transactions_textView_date);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item.transactions_textView_date.getLayoutParams();
			params.width = (int) DisplayUtil.convertDip2Pixel(mContext,120);
			item.transactions_textView_date.setLayoutParams(params);
			view.setTag(item);
		}else{
			item=(ListItem) view.getTag();
		}
		StockholdsBean sb=mList.get(position);
		if(sb.getType().equals("2")){
			item.transactions_textView_type.setText("买入");
			// 成交价格
			item.transactions_textView_price.setText(String.format("%.2f", sb.getPrice_buy()));
		}else{
			item.transactions_textView_type.setText("卖出");
			// 成交价格
			item.transactions_textView_price.setText(String.format("%.2f", sb.getPrice_sell()));
		}
		// 数量
		item.transactions_textView_count.setText(sb.getVolumn());

		double profit = sb.getProfit();
		double price = sb.getPrice_buy();
		double volumn =Double.valueOf(sb.getVolumn());
		double singleRal = profit / (price * volumn) * 100;
		if (profit == 0)
		{
			item.transactions_textView_money.setText("--");
			item.transactions_textView_rate.setText("--");
			item.transactions_textView_money.setTextColor(mContext.getResources().getColor(R.color.black));
			item.transactions_textView_rate.setTextColor(mContext.getResources().getColor(R.color.black));

		} else {
			item.transactions_textView_money.setText(sb.getProfit()+"");
			item.transactions_textView_rate.setText(String.format("%+.2f%%", singleRal));

			if (singleRal >= 0)
			{
				item.transactions_textView_money.setTextColor(mContext.getResources().getColor(R.color.red));
				item.transactions_textView_rate.setTextColor(mContext.getResources().getColor(R.color.red));

			} else {

				item.transactions_textView_money.setTextColor(mContext.getResources().getColor(R.color.green));
				item.transactions_textView_rate.setTextColor(mContext.getResources().getColor(R.color.green));
			}
		}
		// 成交时间
		item.transactions_textView_date.setText(sb.getPosted_at().substring(0, 16));


		if (position%2!=0) {
			view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_item_seletor_white));
		}else{
			view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_item_seletor_gray));
		}
		return view;
	}
	public class ListItem{
		// 类型
		TextView transactions_textView_type ;

		// 数量
		TextView transactions_textView_count;

		// 成交价格
		TextView transactions_textView_price;

		//单笔盈亏额
		TextView transactions_textView_money;

		// 单笔盈亏比
		TextView transactions_textView_rate ;

		// 成交时间
		TextView transactions_textView_date;
	}
}

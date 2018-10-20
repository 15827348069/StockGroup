package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.StockholdsBean;

import java.util.List;

public class LeftTitleAdapter extends ListAdapter<StockholdsBean> {
	private LayoutInflater inflater;

	public LeftTitleAdapter(Activity context) {
		super(context);
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ListItem item=null;
		if(view==null){
			view=inflater.inflate(R.layout.trusts_single_cell, null);
			item=new ListItem();
			item.trustsTypeTextView=(TextView) view.findViewById(R.id.trusts_cell_name);
			view.setTag(item);
		}else{
			item=(ListItem) view.getTag();
		}
		item.trustsTypeTextView.setText(mList.get(position).getName());
		if (position%2!=0) {
			view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_item_seletor_white));
		}else{
			view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_item_seletor_gray));
		}
		return view;
	}
	public class ListItem{
		TextView trustsTypeTextView;
	}
}

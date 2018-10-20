package com.zbmf.StocksMatch.adapter;


import com.zbmf.StocksMatch.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

public class StockAdapter extends CursorAdapter implements Filterable{

	public StockAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		TextView tv_name=(TextView) view.findViewById(R.id.tv_name);
		TextView tv_code=(TextView) view.findViewById(R.id.tv_code);
		tv_name.setText(cursor.getString(cursor.getColumnIndex("name")));
		tv_code.setText(cursor.getString(cursor.getColumnIndex("code")));
	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.sear_stock_item, null);
		return view;
	}

}

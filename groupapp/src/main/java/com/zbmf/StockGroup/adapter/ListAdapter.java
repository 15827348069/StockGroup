package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapter<T> extends BaseAdapter{
	
	protected List<T> mList;
	protected Context mContext;
	protected ListView mListView;
	protected LayoutInflater inflater;
	public ListAdapter(Context context){
		this.mContext = context;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(mList != null)
			return mList.size();
		else
			return 0;
	}

	@Override
	public T getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);
	
	public void setList(List<T> list){
		this.mList = list;
		notifyDataSetChanged();
	}
	
	public void addList(List<T> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}
	
	public List<T> getList(){
		return mList;
	}
	
	public void setList(T[] list){
		List<T> arrayList = new ArrayList<T>(list.length);  
		for (T t : list) {  
			arrayList.add(t);  
		}  
		setList(arrayList);
	}
	
	public ListView getListView(){
		return mListView;
	}
	
	public void setListView(ListView listView){
		mListView = listView;
	}

}

package com.zbmf.worklibrary.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapter<T> extends BaseAdapter{
	
	protected List<T> mList;
	protected Activity mContext;
	protected ListView mListView;
	protected LayoutInflater inflater;
	public ListAdapter(Activity context){
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
	public View getView(int i, View view, ViewGroup viewGroup) {
		if(view==null){
			view= LayoutInflater.from(mContext).inflate(getLayout(),null);
		}
		return getHolderView(i,view,getItem(i));
	}
	protected abstract int getLayout();
	public abstract View getHolderView(int position, View convertView, T t);
	@SuppressWarnings("unchecked")
	public void setList(List<T> list){
		if (list!=null){
			this.mList = list;
			notifyDataSetChanged();
		}
	}
    @SuppressWarnings("unchecked")
	public void clearList(){
		if (mList.size()>0){
			mList.clear();
			notifyDataSetChanged();
		}
	}
	@SuppressWarnings("unchecked")
	public void addList(List<T> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}
	public void removeList(int position){
		if (mList!=null&&mList.size()>0){
			mList.remove(position);
			notifyDataSetChanged();
		}
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
	public class BaseHolder{

	}
}

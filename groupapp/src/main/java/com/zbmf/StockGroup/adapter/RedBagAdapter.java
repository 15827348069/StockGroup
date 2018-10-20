package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.RedBagUserMessage;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

public class RedBagAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<RedBagUserMessage>infolist;
	public RedBagAdapter(Context context,List<RedBagUserMessage>info){
		this.inflater=LayoutInflater.from(context);
		this.infolist=info;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infolist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infolist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		RedBagItem item=null;
		RedBagUserMessage bm=infolist.get(position);
		if(view==null){
			view=inflater.inflate(R.layout.red_bag_item, null);
			item=new RedBagItem(view);
			view.setTag(item);
		}else{
			item=(RedBagItem) view.getTag();
		}
		item.name.setText(bm.getUser_name());
		item.time.setText(bm.getUser_time());
		item.countent.setText(bm.getCountent());
		if(bm.isBast()){
			item.red_bag_bast.setVisibility(View.VISIBLE);
		}else{
			item.red_bag_bast.setVisibility(View.INVISIBLE);
		}
//		ViewFactory.imgCircleView(parent.getContext(),bm.getUser_avatar(), item.avatar);
		ImageLoader.getInstance().displayImage(bm.getUser_avatar(), item.avatar, ImageLoaderOptions.AvatarOptions());
		return view;
	}
	public class RedBagItem{
		TextView name,time,countent,red_bag_bast;
		RoundedCornerImageView avatar;
		public RedBagItem(View view){
			name=(TextView) view.findViewById(R.id.red_bag_item_name);
			time=(TextView) view.findViewById(R.id.red_bag_item_time);
			red_bag_bast=(TextView) view.findViewById(R.id.red_bag_bast);
			countent=(TextView) view.findViewById(R.id.red_bag_item_countent);
			avatar=(RoundedCornerImageView) view.findViewById(R.id.have_red_bag_user_avatar);
		}
	}
}

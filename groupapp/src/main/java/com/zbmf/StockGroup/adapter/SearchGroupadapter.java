package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchGroupadapter extends BaseAdapter {
	private Context ctx;
	private ViewHolder holder;
	List<Group> list;
	private Map<String,ImageView>img=new HashMap<String, ImageView>();
	public SearchGroupadapter(Context context, List<Group> list) {
		this.ctx = context;
		this.list = list;	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			Group item = list.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.search_list_circle_item, null);
				holder.circle_name = (TextView) convertView.findViewById(R.id.circle_name);
				holder.circle_code = (TextView) convertView.findViewById(R.id.circle_code);
				holder.avatar = (RoundedCornerImageView) convertView.findViewById(R.id.circle_avatar_img);
				holder.circle_only = (TextView) convertView.findViewById(R.id.circle_only);
				convertView.setTag(holder);
				img.put(item.getAvatar(), holder.avatar);
			} else {
				holder = (ViewHolder) convertView.getTag();
				img.put(item.getAvatar(), holder.avatar);
			}
			holder.circle_name.setText(item.getName());
			holder.circle_code.setText(item.getId());
			if(item.getExclusives()==0){
				holder.circle_only.setVisibility(View.GONE);
			}else{
				holder.circle_only.setVisibility(View.VISIBLE);
			}
//			ViewFactory.imgCircleView(parent.getContext(),item.getAvatar(), holder.avatar);
		    ImageLoader.getInstance().displayImage(item.getAvatar(), holder.avatar, ImageLoaderOptions.AvatarOptions());
		} catch (OutOfMemoryError e) {
			Runtime.getRuntime().gc();
		} catch (Exception ex) {
			// handler.sendEmptyMessage(CommonMessage.PARSE_ERROR);
			ex.printStackTrace();
		}
		return convertView;
	}

	class ViewHolder {
		RoundedCornerImageView avatar;
		TextView circle_name;
		TextView circle_code;
		TextView circle_only;
	}

}

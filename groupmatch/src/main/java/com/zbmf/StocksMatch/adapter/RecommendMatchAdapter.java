package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.MatchBean;


/**
 * 首页 --推荐比赛适配器--go
 * @author kubo_android
 *
 */
public class RecommendMatchAdapter extends ListAdapter<MatchBean> {

	public RecommendMatchAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView != null){
			holder = (ViewHolder) convertView.getTag();
		}else{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.match_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
			holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			
			convertView.setTag(holder);
		}

		MatchBean mb = mList.get(position);
		holder.tv_name.setText(mb.getTitle());
		holder.tv_num.setText(mb.getPlayers());
		holder.tv_time.setText(mb.getEnd_apply());

		//match_type 0公开赛, 1邀请赛
		if("0".equals(mb.getIs_match_player())){
			if(0==mb.getMatch_type())
				holder.tv_status.setText(R.string.free);
			else
				holder.tv_status.setText(R.string.invite_match);
//			holder.tv_status.setText(R.string.noplayer);
		}else
			holder.tv_status.setText(R.string.isplayer);
		holder.tv_desc.setText(mb.getDesc());
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView tv_name,tv_num,tv_time,tv_desc,tv_status;
	}

}

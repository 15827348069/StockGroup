package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.utils.GetTime;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * 首页 --推荐比赛适配器、所有比赛
 * @author kubo_android
 *
 */
public class AllMatchAdapter extends ListAdapter<MatchBean> {

	private LayoutInflater inflater;

	public AllMatchAdapter(Activity context) {
		super(context);
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder = null;

		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.match_item2, null);
			holder.name = (TextView) view.findViewById(R.id.match_name);//赛名
			holder.money = (TextView) view.findViewById(R.id.match_money);//desc
			holder.tv_status = (TextView) view.findViewById(R.id.tv_status);//时间
			holder.tv_paiming = (TextView) view.findViewById(R.id.tv_paiming);//隐藏
			holder.people_number = (TextView) view.findViewById(R.id.match_people_number);//参与数

			holder.arrow = (TextView) view.findViewById(R.id.match_arrow);//比赛状态,进行中
			holder.yield = (TextView) view.findViewById(R.id.match_yield);//已参赛，收费等


			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		MatchBean match = mList.get(position);
		holder.tv_paiming.setVisibility(View.GONE);
		holder.name.setText(match.getTitle());
		holder.people_number.setText(match.getPlayers());
		holder.tv_status.setText(match.getStart_at()+mContext.getString(R.string.sperator)+match.getEnd_at());

		if(GetTime.getTimeIsTrue(match.getEnd_at())){
			holder.yield.setText(R.string.running);
		}else{
			holder.yield.setText(R.string.finished);
		}

		//match_type 0公开赛, 1邀请赛
		if("0".equals(match.getIs_match_player())){
			if("0".equals(match.getMpay()))
				if(0==match.getMatch_type())
					holder.arrow.setText(R.string.free);
				else
					holder.arrow.setText(R.string.invite_match);
			else
				holder.arrow.setText(match.getMpay()+mContext.getString(R.string.mfb));
		}else
			holder.arrow.setText(R.string.isplayer);

		holder.money.setText(match.getDesc());

 		return view;
	}

	static class ViewHolder {
		TextView name, people_number, arrow, money, yield,tv_status,tv_paiming;
	}

}

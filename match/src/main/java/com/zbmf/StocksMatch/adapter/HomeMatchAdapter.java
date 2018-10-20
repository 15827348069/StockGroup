package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuhao
 * on 2017/11/24.
 */

public class HomeMatchAdapter extends ListAdapter<MatchNewAllBean.Result.Matches> {
    private Context mContext;
    public HomeMatchAdapter(Activity context) {
        super(context);
        this.mContext=context;
    }

    @Override
    protected int getLayout() {
        return R.layout.item_super_match_layout;
    }

    @Override
    public View getHolderView(int position, View convertView, MatchNewAllBean.Result.Matches userMatch) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tvMatchName.setText(userMatch.getMatch_name());
        holder.tvMatchPlayer.setText(String.format(mContext.getResources()
                .getString(R.string.match_players), userMatch.getPlayers()));
        //0 公开 1邀请  2私密赛
        if (userMatch.getMatch_type()==0){
            holder.tvMatchType.setText(mContext.getString(R.string.gk));
            holder.tvMatchType.setBackgroundResource(R.drawable.tag_yellow);
            holder.tvMatchType.setTextColor(mContext.getResources().getColor(R.color.gk));
        }else if (userMatch.getMatch_type()==1){
            holder.tvMatchType.setText(mContext.getString(R.string.invite));
            holder.tvMatchType.setBackgroundResource(R.drawable.inivate_bg);
            holder.tvMatchType.setTextColor(mContext.getResources().getColor(R.color.err_red));
        }else if (userMatch.getMatch_type()==2){
            holder.tvMatchType.setText(mContext.getString(R.string.private_match));
        }
        if (userMatch.getIs_end()){
            holder.tvMatchAction.setBackgroundResource(R.drawable.end_bg);
            holder.tvMatchAction.setTextColor(mContext.getResources().getColor(R.color.black_grey));
        }else {
            holder.tvMatchAction.setBackgroundResource(R.drawable.tag_blue);
            holder.tvMatchAction.setTextColor(mContext.getResources().getColor(R.color.blue1));
        }
        holder.tvMatchAction.setText(userMatch.getIs_end()?"已结束":"进行中");
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.tv_match_name)
        TextView tvMatchName;
        @BindView(R.id.tv_match_type)
        TextView tvMatchType;
        @BindView(R.id.tv_match_action)
        TextView tvMatchAction;
        @BindView(R.id.tv_match_player)
        TextView tvMatchPlayer;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

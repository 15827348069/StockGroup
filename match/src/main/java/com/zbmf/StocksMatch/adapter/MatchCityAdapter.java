package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.MatchList3Bean;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.worklibrary.adapter.ListAdapter;
import com.zbmf.worklibrary.util.DoubleFromat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuhao
 * on 2017/12/12.
 */

public class MatchCityAdapter extends ListAdapter<MatchList3Bean.Result.Matches> {


    public MatchCityAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_match_city_layout;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getHolderView(int position, View convertView, MatchList3Bean.Result.Matches result) {
        ViewHolder holder= (ViewHolder) convertView.getTag();
        if(holder==null){
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if(position==0){
            holder.imvRankImg.setVisibility(View.VISIBLE);
            holder.imvRankImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_gold));
            holder.tvRank.setVisibility(View.INVISIBLE);
        }else if(position==1){
            holder.imvRankImg.setVisibility(View.VISIBLE);
            holder.imvRankImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_silver));
            holder.tvRank.setVisibility(View.INVISIBLE);
        }else if(position==2){
            holder.imvRankImg.setVisibility(View.VISIBLE);
            holder.imvRankImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_copper));
            holder.tvRank.setVisibility(View.INVISIBLE);
        }else{
            holder.tvRank.setVisibility(View.VISIBLE);
            holder.imvRankImg.setVisibility(View.GONE);
            holder.tvRank.setText(String.valueOf(position+1));
        }
        holder.tvName.setText(result.getMatch_name());
//        Log.i("--TAG","--------------- yield "+result.getWeek_yield());
        if (result.getWeek_yield()>=0){
            holder.tvYield.setText("+"+ DoubleFromat.getStockDouble(result.getWeek_yield()*100,2)+"%");
        } else {
            holder.tvYield.setText(DoubleFromat.getStockDouble(result.getWeek_yield()*100,2)+"%");
        }
        holder.tvYield.setTextColor(result.getWeek_yield()>=0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
         //这里注销图像的显示
        //        List<String> topPlayers = result.getTop_players();
//        for (int i = 0; i < topPlayers.size(); i++) {
//            if (i==0){
//                setAvatar(topPlayers,i,holder.imvOneAvatar);
//            }else if (i==1){
//                setAvatar(topPlayers,i,holder.imvTwoAvatar);
//            }else if (i==2){
//                setAvatar(topPlayers,i,holder.imvThreeAvatar);
//            }
//        }

//        Glide.with(mContext).load(result.getTop_players().get(0)).apply(GlideOptionsManager.getInstance().getRequestOptionsMatch()).into(holder.imvOneAvatar);
//        Glide.with(mContext).load(result.getTop_players().get(1)).apply(GlideOptionsManager.getInstance().getRequestOptionsMatch()).into(holder.imvTwoAvatar);
//        Glide.with(mContext).load(result.getTop_players().get(2)).apply(GlideOptionsManager.getInstance().getRequestOptionsMatch()).into(holder.imvThreeAvatar);
        return convertView;
    }
    private void setAvatar(List<String> topPlayers,int i,ImageView avartar){
        Glide.with(mContext).load(topPlayers.get(i)).apply(GlideOptionsManager.getInstance().getRequestOptionsMatch()).into(avartar);

    }
    public class ViewHolder{
        @BindView(R.id.imv_rank_img)
        ImageView imvRankImg;
        @BindView(R.id.tv_rank)
        TextView tvRank;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_yield)
        TextView tvYield;
        @BindView(R.id.imv_three_avatar)
        ImageView imvThreeAvatar;
        @BindView(R.id.imv_two_avatar)
        ImageView imvTwoAvatar;
        @BindView(R.id.imv_one_avatar)
        ImageView imvOneAvatar;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}

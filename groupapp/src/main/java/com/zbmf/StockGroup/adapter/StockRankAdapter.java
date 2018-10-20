package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.Yield;
import com.zbmf.StockGroup.interfaces.OnAdapterClickListener;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;


/**
 * 根据条目进行分类
 */
public class StockRankAdapter extends BaseAdapter {

    private List<Yield> oneStockCommits;
    private Context cxt;
    private int flag;
    private OnAdapterClickListener adapterClickListener;

    public void setAdapterClickListener(OnAdapterClickListener adapterClickListener) {
        this.adapterClickListener = adapterClickListener;
    }

    public StockRankAdapter(Context cxt, List<Yield> oneStockCommits, int falg) {
        this.oneStockCommits = oneStockCommits;
        this.cxt = cxt;
        this.flag=falg;
    }

    @Override
    public int getCount() {
        return oneStockCommits.size();
    }

    @Override
    public Object getItem(int position) {
        return oneStockCommits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(cxt).inflate(R.layout.stock_rank_flow, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Yield yield=oneStockCommits.get(position);
        if(position==0){
            holder.imv_rank_img.setVisibility(View.VISIBLE);
            holder.imv_rank_img.setImageDrawable(cxt.getResources().getDrawable(R.drawable.icon_gold));
            holder.tv_rank.setVisibility(View.INVISIBLE);
        }else if(position==1){
            holder.imv_rank_img.setVisibility(View.VISIBLE);
            holder.imv_rank_img.setImageDrawable(cxt.getResources().getDrawable(R.drawable.icon_silver));
            holder.tv_rank.setVisibility(View.INVISIBLE);
        }else if(position==2){
            holder.imv_rank_img.setVisibility(View.VISIBLE);
            holder.imv_rank_img.setImageDrawable(cxt.getResources().getDrawable(R.drawable.icon_copper));
            holder.tv_rank.setVisibility(View.INVISIBLE);
        }else{
            holder.tv_rank.setVisibility(View.VISIBLE);
            holder.imv_rank_img.setVisibility(View.GONE);
            holder.tv_rank.setText((position+1)+"");
        }

        holder.tv_nickname.setText(yield.getNickname());
        if(flag==1){
            holder.tv_yield.setText(String.format("%+.2f%%", yield.getDay_yield()*100));
        }else{
            holder.tv_yield.setText(String.format("%+.2f%%", yield.getWeek_yield()*100));
        }
        final DealSys dealSys=yield.getDealSys();
        if(dealSys!=null){
            holder.tv_action.setText(dealSys.getAction()!=null?dealSys.getAction().replace(" ",""):"");
            holder.tv_symbol.setText(dealSys.getStock_name()!=null?dealSys.getStock_name():"");
        }
//        holder.tv_common.setText(dealSys.getCount_comments());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapterClickListener!=null){
                    adapterClickListener.onClickListener(v,dealSys);
                }
            }
        });
//        ViewFactory.imgCircleView(parent.getContext(),yield.getAvatar(),holder.iv_rcv);
        ImageLoader.getInstance().displayImage(yield.getAvatar(),holder.iv_rcv, ImageLoaderOptions.AvatarOptions());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_rank,tv_nickname,tv_yield,tv_action,tv_symbol,tv_common;
        private ImageView imv_rank_img;
        RoundedCornerImageView iv_rcv;
        public ViewHolder(View view){
            tv_rank= (TextView) view.findViewById(R.id.tv_rank);
            tv_nickname= (TextView) view.findViewById(R.id.tv_nickname);
            tv_yield= (TextView) view.findViewById(R.id.tv_yield);
            tv_action= (TextView) view.findViewById(R.id.tv_action);
            tv_symbol= (TextView) view.findViewById(R.id.tv_symbol);
            imv_rank_img= (ImageView) view.findViewById(R.id.imv_rank_img);
//            tv_common= (TextView) view.findViewById(R.id.tv_common);
            iv_rcv= (RoundedCornerImageView) view.findViewById(R.id.iv_rcv);
        }
    }
}

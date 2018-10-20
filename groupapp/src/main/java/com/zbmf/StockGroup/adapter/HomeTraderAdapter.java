package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Traders;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

public class HomeTraderAdapter extends RecyclerView.Adapter<HomeTraderAdapter.ViewHolder> {
    private List<Traders> info;

    private LayoutInflater mInflater;
    private onItemClick itemClick;
    public void setItemClick(onItemClick itemClick){
        this.itemClick=itemClick;
    }

    public interface onItemClick
    {
        void onItemClick(Traders traders);
    }
    private Context mContext;
    public HomeTraderAdapter(Context context, List<Traders> datats) {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        info = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            tv_total_yield = (TextView) view.findViewById(R.id.tv_yield);
            tv_more_button = (TextView) view.findViewById(R.id.tv_more_button);
            item_screen_layout_id= (RelativeLayout) view.findViewById(R.id.item_screen_layout_id);
            imv_avatar= (RoundedCornerImageView) view.findViewById(R.id.imv_avatar);
            tv_number= (TextView) view.findViewById(R.id.tv_number);
            trader_yield_layout= (LinearLayout) view.findViewById(R.id.trader_yield_layout);
        }
        TextView name, tv_total_yield,tv_more_button,tv_number;
        RelativeLayout item_screen_layout_id;
        RoundedCornerImageView imv_avatar;
        LinearLayout trader_yield_layout;
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_home_trader_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Traders traders=info.get(i);
        if(!traders.isMore()){
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.tv_number.setVisibility(View.VISIBLE);
            viewHolder.trader_yield_layout.setVisibility(View.VISIBLE);
            viewHolder.imv_avatar.setVisibility(View.VISIBLE);
            viewHolder.tv_more_button.setVisibility(View.GONE);
//            ViewFactory.imgCircleView(mContext,traders.getAvatar(),viewHolder.imv_avatar);
            ImageLoader.getInstance().displayImage(traders.getAvatar(),viewHolder.imv_avatar, ImageLoaderOptions.AvatarOptions());
            viewHolder.name.setText(traders.getNickname());
            viewHolder.tv_number.setText("操作"+traders.getDeal_total()+"次");
//            viewHolder.trader_yield_layout.setSelected(traders.getTotal_yield()>=0);
            viewHolder.tv_total_yield.setText((traders.getDeal_success()>=0?"+":"")+(DoubleFromat.getDouble(traders.getDeal_success()*100,2)+"%"));
        }else{
            viewHolder.tv_more_button.setVisibility(View.VISIBLE);
            viewHolder.name.setVisibility(View.INVISIBLE);
            viewHolder.tv_number.setVisibility(View.INVISIBLE);
            viewHolder.trader_yield_layout.setVisibility(View.INVISIBLE);
            viewHolder.imv_avatar.setVisibility(View.INVISIBLE);
        }
        viewHolder.item_screen_layout_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null){
                    itemClick.onItemClick(traders);
                }
            }
        });
    }

}
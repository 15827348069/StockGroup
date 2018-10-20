package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.ViewHolder> {
    private List<Screen> info;

    private LayoutInflater mInflater;
    private onItemClick itemClick;

    public void setItemClick(onItemClick itemClick){
        this.itemClick=itemClick;
    }

    public interface onItemClick
    {
        void onItemClick(Screen screen);
    }
    public ScreenAdapter(Context context, List<Screen> datats) {
        mInflater = LayoutInflater.from(context);
        info = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            rl_screen_layout = (RelativeLayout) view.findViewById(R.id.rl_screen_layout);
            name = (TextView) view.findViewById(R.id.tv_screen_name);
            tv_total_yield = (TextView) view.findViewById(R.id.tv_total_yield);
            tv_win_rate = (TextView) view.findViewById(R.id.tv_win_rate);
            tv_screen_detail= (TextView) view.findViewById(R.id.tv_screen_detail);
        }

        RelativeLayout rl_screen_layout;
        TextView name, tv_total_yield,tv_win_rate,tv_screen_detail;
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_screen_product_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Screen screen=info.get(i);
        switch (i%4){
            case 0:
                viewHolder.rl_screen_layout.setBackgroundResource(R.drawable.icon_product1);
                viewHolder.name.setBackgroundResource(R.drawable.icon_screen_title1);
                break;
            case 1:
                viewHolder.rl_screen_layout.setBackgroundResource(R.drawable.icon_product2);
                viewHolder.name.setBackgroundResource(R.drawable.icon_screen_title1);
                break;
            case 2:
                viewHolder.rl_screen_layout.setBackgroundResource(R.drawable.icon_product3);
                viewHolder.name.setBackgroundResource(R.drawable.icon_screen_title2);
                break;
            case 3:
                viewHolder.rl_screen_layout.setBackgroundResource(R.drawable.icon_product4);
                viewHolder.name.setBackgroundResource(R.drawable.icon_screen_title2);
                break;
        }
        viewHolder.name.setText(screen.getName());
        viewHolder.tv_total_yield.setText(screen.getTotal_yield()+"");
        viewHolder.tv_win_rate.setText(screen.getWin_rate()+"");
        viewHolder.tv_screen_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null){
                    itemClick.onItemClick(screen);
                }
            }
        });

    }

}
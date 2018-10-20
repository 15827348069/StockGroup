package com.zbmf.StockGroup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.view.CustomMyCProgress;

import java.util.List;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.ViewHolder> {
    private List<Screen> info;
    private Context mContext;
    private LayoutInflater mInflater;
    private onItemClick itemClick;
    private int red, green;
    private SubscribeVIP mSubscribeVIP;

    public void setItemClick(onItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public interface onItemClick {
        void onItemClick(Screen screen,int flag);
    }

    public interface SubscribeVIP {
        void onSubscribeVIP(Screen screen, int flag);

//        void checkDetail(Screen screen);
    }

    public void setSubscribe(SubscribeVIP subscribe) {
        mSubscribeVIP = subscribe;
    }

    public HomeScreenAdapter(Context context, List<Screen> datats) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        info = datats;
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            tv_total_yield = (TextView) view.findViewById(R.id.tv_yield);
            tv_dapan = (TextView) view.findViewById(R.id.tv_dapan);
            vipSum = (TextView) view.findViewById(R.id.vipSum);
            subscribeBtn = (Button) view.findViewById(R.id.subscribeBtn);
            screen_progress = (CustomMyCProgress) view.findViewById(R.id.cc_screen_progress);
            item_screen_layout_id = (RelativeLayout) view.findViewById(R.id.item_content_layout);
        }

        TextView name, tv_total_yield, tv_dapan, vipSum;
        Button subscribeBtn;
        RelativeLayout item_screen_layout_id;
        CustomMyCProgress screen_progress;
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_home_vers_screen_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Screen screen = info.get(i);
        boolean order_status = screen.getOrder_status();//判断是否订阅了该服务
        String name = screen.getName();
        if (name.equals("九天揽月")) {
            if (order_status) {
                viewHolder.subscribeBtn.setText("查看");
                viewHolder.subscribeBtn.setTextColor(mContext.getResources().getColor(R.color.item_message));
                viewHolder.subscribeBtn.setBackground(mContext.getResources().getDrawable(R.drawable.subbtn1));
            } else {
                viewHolder.subscribeBtn.setText("会员免费");
            }
        } else if (name.equals("逐浪投机") || name.equals("山高月小")) {
            if (order_status) {
                viewHolder.subscribeBtn.setText("查看");
                viewHolder.subscribeBtn.setTextColor(mContext.getResources().getColor(R.color.item_message));
                viewHolder.subscribeBtn.setBackground(mContext.getResources().getDrawable(R.drawable.subbtn1));
            } else {
                viewHolder.subscribeBtn.setText("订阅");
            }
        } else if (name.equals("牛动乾坤")) {
            viewHolder.subscribeBtn.setText("暂未开放");
            viewHolder.subscribeBtn.setTextColor(mContext.getResources().getColor(R.color.item_message));
            viewHolder.subscribeBtn.setBackground(mContext.getResources().getDrawable(R.drawable.subbtn1));
        }
        viewHolder.name.setText(screen.getName());
        viewHolder.tv_total_yield.setTextColor(screen.getTotal_yield() >= 0 ? red : green);
        viewHolder.tv_dapan.setTextColor(screen.getSh_yield() >= 0 ? red : green);
        viewHolder.tv_total_yield.setText((screen.getTotal_yield() >= 0 ? "+" : "") + (DoubleFromat.getDouble(screen.getTotal_yield() * 100, 2) + "%"));
        viewHolder.tv_dapan.setText((screen.getSh_yield() >= 0 ? "+" : "") + (DoubleFromat.getDouble(screen.getSh_yield() * 100, 2) + "%"));
//        ObjectAnimator anim = ObjectAnimator.ofFloat(viewHolder.screen_progress, "progress", 0f, (float) (screen.getWin_rate() * 100));
//        anim.setDuration(2000);
//        anim.start();
        viewHolder.item_screen_layout_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null) {
                    if (screen.getName().equals(mContext.getString(R.string.zltj)) ||
                            screen.getName().equals(mContext.getString(R.string.sgyx))) {
                        itemClick.onItemClick(screen,5);
                    } else if (screen.getName().equals(mContext.getString(R.string.jtly))) {
                        itemClick.onItemClick(screen,6);
                    }
                }
            }
        });
        viewHolder.subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubscribeVIP != null) {
                    if (screen.getIs_buy()) {
                        if (screen.getName().equals(mContext.getString(R.string.zltj)) ||
                                screen.getName().equals(mContext.getString(R.string.sgyx))) {
                            mSubscribeVIP.onSubscribeVIP(screen, 5);
                        } else if (screen.getName().equals(mContext.getString(R.string.jtly))) {
                            mSubscribeVIP.onSubscribeVIP(screen, 6);
                        }
                    }
                }
            }
        });
    }

}
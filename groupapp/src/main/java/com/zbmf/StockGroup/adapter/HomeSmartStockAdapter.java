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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pq
 * on 2018/6/14.
 */

public class HomeSmartStockAdapter extends RecyclerView.Adapter<HomeSmartStockAdapter.MyHolder> {

    private Context mContext;
    private List<Screen> info;
    private SmartItemClickListener smartItemClickListener;
    private SubscribeVIP mSubscribeVIP;

    public void setSmartItemClickListener(SmartItemClickListener sListener) {
        smartItemClickListener = sListener;
    }

    public void setSubscribe(SubscribeVIP subscribe) {
        mSubscribeVIP = subscribe;
    }

    public HomeSmartStockAdapter(Context context) {
        this.mContext = context;
        info = new ArrayList<>();
    }

    public void addData(List<Screen> newList) {
        if (newList != null && newList.size() > 0) {
            info.addAll(newList);
            notifyDataSetChanged();
        }
    }

    public void clearDataList() {
        if (info != null && info.size() > 0) {
            info.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.smart_stock_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final Screen screen = info.get(position);
        boolean order_status = screen.getOrder_status();//判断是否订阅了该服务
        String name = screen.getName();
        if (name.equals("九天揽月")) {
            if (order_status) {
                holder.subscribeBtn.setText("查看");
                holder.subscribeBtn.setTextColor(mContext.getResources().getColor(R.color.item_message));
                holder.subscribeBtn.setBackground(mContext.getResources().getDrawable(R.drawable.subbtn1));
            } else {
                holder.subscribeBtn.setText("会员免费");
            }
        } else if (name.equals("逐浪投机") || name.equals("山高月小")) {
            if (order_status) {
                holder.subscribeBtn.setText("查看");
                holder.subscribeBtn.setTextColor(mContext.getResources().getColor(R.color.item_message));
                holder.subscribeBtn.setBackground(mContext.getResources().getDrawable(R.drawable.subbtn1));
            } else {
                holder.subscribeBtn.setText("订阅");
            }
        } else if (name.equals("牛动乾坤")) {
            holder.subscribeBtn.setText("暂未开放");
            holder.subscribeBtn.setTextColor(mContext.getResources().getColor(R.color.item_message));
            holder.subscribeBtn.setBackground(mContext.getResources().getDrawable(R.drawable.subbtn1));
        }
        holder.name.setText(screen.getName());
        holder.tv_total_yield.setTextColor(screen.getTotal_yield() >= 0 ? mContext.getResources().getColor(R.color.red)
                : mContext.getResources().getColor(R.color.green));
        holder.tv_dapan.setTextColor(screen.getSh_yield() >= 0 ? mContext.getResources().getColor(R.color.red)
                : mContext.getResources().getColor(R.color.green));
        holder.tv_total_yield.setText((screen.getTotal_yield() >= 0 ? "+" : "") +
                (DoubleFromat.getDouble(screen.getTotal_yield() * 100, 2) + "%"));
        holder.tv_dapan.setText((screen.getSh_yield() >= 0 ? "+" : "") +
                (DoubleFromat.getDouble(screen.getSh_yield() * 100, 2) + "%"));
        holder.subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubscribeVIP != null) {
                    if (screen.getName().equals(mContext.getString(R.string.zltj)) ||
                            screen.getName().equals(mContext.getString(R.string.sgyx))) {
                        mSubscribeVIP.onSubscribeVIP(screen, 5);
                    } else if (screen.getName().equals(mContext.getString(R.string.jtly))) {
                        mSubscribeVIP.onSubscribeVIP(screen, 6);
                    }
                }
            }
        });
        holder.item_screen_layout_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (smartItemClickListener != null) {
                    if (screen.getName().equals(mContext.getString(R.string.zltj)) ||
                            screen.getName().equals(mContext.getString(R.string.sgyx))) {
                        smartItemClickListener.sItemClick(screen, 5);
                    } else if (screen.getName().equals(mContext.getString(R.string.jtly))) {
                        smartItemClickListener.sItemClick(screen, 6);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, tv_total_yield, tv_dapan, vipSum;
        Button subscribeBtn;
        RelativeLayout item_screen_layout_id;

        MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_total_yield = (TextView) itemView.findViewById(R.id.tv_yield);
            tv_dapan = (TextView) itemView.findViewById(R.id.tv_dapan);
            vipSum = (TextView) itemView.findViewById(R.id.vipSum);
            subscribeBtn = (Button) itemView.findViewById(R.id.subscribeBtn);
            item_screen_layout_id = (RelativeLayout) itemView.findViewById(R.id.item_content_layout);
        }
    }

    public interface SmartItemClickListener {
        void sItemClick(Screen screen, int flag);
    }

    public interface SubscribeVIP {
        void onSubscribeVIP(Screen screen, int flag);
    }
}

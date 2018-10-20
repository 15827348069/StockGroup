package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.StockholdsBean;
import com.zbmf.StocksMatch.listener.RemarkBtnClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/4/3.
 */

public class MyStockAdapter extends RecyclerView.Adapter<MyStockAdapter.MyViewHolder> {
    private List<StockholdsBean> stockList;
    private RemarkBtnClick mRemarkBtnClick;
    private Context mContext;
    private final LayoutInflater mInflater;
    private MyViewHolder mMyViewHolder;

    public MyStockAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setStockList(List<StockholdsBean> stockList) {
        this.stockList = stockList;
        notifyDataSetChanged();
    }

    public void addStockList(List<StockholdsBean> newStockList) {
        if (newStockList != null) {
            stockList.addAll(newStockList);
            notifyDataSetChanged();
        }
    }

    public List<StockholdsBean> getStockList() {
        return stockList;
    }

    public void clearStockList() {
        if (stockList != null && stockList.size() > 0) {
            stockList.clear();
        }
        notifyDataSetChanged();
    }

    public void removeStockList(int position) {
        if (stockList != null && stockList.size() > 0 && position < stockList.size()) {
            stockList.remove(position);
            notifyDataSetChanged();
        }
    }

//    @Override
//    public View onCreateContentView(ViewGroup parent, int viewType) {
//        return mInflater.inflate(R.layout.item_mine_stock_layout, parent, false);
//    }
//
//    @Override
//    public MyViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
//        MyViewHolder holder = (MyViewHolder) realContentView.getTag();
//        if (holder==null){
//            holder=new MyViewHolder(realContentView);
//            realContentView.setTag(holder);
//        }
//        return holder;
//    }
//
//    @SuppressLint("DefaultLocale")
//    @Override
//    public void onCompatBindViewHolder(MyViewHolder holder, int position) {
//
//    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_mine_stock_layout, parent, false);
        if (mMyViewHolder==null){
            mMyViewHolder = new MyViewHolder(view);
        }
        return mMyViewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final StockholdsBean stockholdsBean = stockList.get(position);
        holder.tvName.setText(stockholdsBean.getName());
        holder.tvSymbol.setText(stockholdsBean.getSymbol());
        holder.tvPrice.setText(String.format("%.2f", stockholdsBean.getCurrent()));
        double yield=stockholdsBean.getClose()!=0?(stockholdsBean.getCurrent()-stockholdsBean.getClose())/stockholdsBean.getClose()*100:0;
        holder.tvYield.setText(String.format("%+.2f%%",yield));
        if(yield==0){
            holder.tvYield.setTextColor(mContext.getResources().getColor(R.color.black_66));
        }else{
            holder.tvYield.setTextColor(yield>0?mContext.getResources().getColor(R.color.red):mContext.getResources().getColor(R.color.green));
        }
        holder.remarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemarkBtnClick!=null){
                    mRemarkBtnClick.skipRemarkList(stockholdsBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return /*stockList==null?0:*/stockList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_symbol)
        TextView tvSymbol;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_yield)
        TextView tvYield;
        @BindView(R.id.remarkBtn)
        TextView remarkBtn;
        @BindView(R.id.left_item)
        LinearLayout leftItem;
        @BindView(R.id.item_delete_btn)
        Button itemDeleteBtn;
        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public void setRemarkBtnClick(RemarkBtnClick remarkBtnClick){
        this.mRemarkBtnClick=remarkBtnClick;
    }
}

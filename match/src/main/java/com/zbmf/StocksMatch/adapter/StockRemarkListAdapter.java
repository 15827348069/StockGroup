package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.StockRemarkListBean;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/3/30.
 */

public class StockRemarkListAdapter extends ListAdapter<StockRemarkListBean.Result.Remarks> {
    public StockRemarkListAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.stock_remark_list_item;
    }

    @Override
    public View getHolderView(int position, View convertView, StockRemarkListBean.Result.Remarks remarks) {
        ViewHolder holder= (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.stock_remark_time.setText(remarks.getSubject());
        holder.stock_remark_name.setText(remarks.getContent());
        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.stock_remark_time)
        TextView stock_remark_time;
        @BindView(R.id.stock_remark_name)
        TextView stock_remark_name;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

}

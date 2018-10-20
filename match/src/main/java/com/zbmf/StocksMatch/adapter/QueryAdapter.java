package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class QueryAdapter extends ListAdapter<DealsRecordList.Result.Stocks> {
    public QueryAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.trusts_single_cell;
    }

    @Override
    public View getHolderView(int position, View convertView, DealsRecordList.Result.Stocks stocks) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.trusts_cell_name.setText(stocks.getName());
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.trusts_cell_name)
        TextView trusts_cell_name;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}

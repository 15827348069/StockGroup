package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.PrizeListBean;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class PrizeAdapter extends ListAdapter<PrizeListBean.Result.Records> {
    private Context mContext;
    public PrizeAdapter(Activity context) {
        super(context);
        this.mContext=context;
    }

    @Override
    protected int getLayout() {
        return R.layout.item_focus_layout;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getHolderView(int position, View convertView, PrizeListBean.Result.Records records) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder==null){
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tv_username.setText(records.getWin_at()+mContext.getString(R.string.gain)+records.getAward());
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.tv_username)
        TextView tv_username;
        @BindView(R.id.ll_line)
        LinearLayout ll_line;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

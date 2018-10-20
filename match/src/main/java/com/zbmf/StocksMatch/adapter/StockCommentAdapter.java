package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.StockCommentsBean;
import com.zbmf.StocksMatch.util.DateUtil;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/3/31.
 */

public class StockCommentAdapter extends ListAdapter<StockCommentsBean.Result.StockComments> {

    public StockCommentAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.one_stock_commit_i;
    }

    @Override
    public View getHolderView(int position, View convertView, StockCommentsBean.Result.StockComments o) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder ==null){
            holder =new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tvCommitTime.setText(DateUtil.getTime(o.getCreate_at(),"yyyy-MM-dd HH:mm"));
        holder.tvUserName.setText(o.getNickname());
        holder.tvDesc.setText(o.getDesc());
        Glide.with(mContext).load(o.getUser_img()).apply(GlideOptionsManager.getInstance()
                .getRequestOptionsMatch()).into(holder.ivRcv);
        return convertView;
    }
    public class ViewHolder{
        @BindView(R.id.iv_rcv)
        ImageView ivRcv;
        @BindView(R.id.tv_userName)
        TextView tvUserName;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_commit_time)
        TextView tvCommitTime;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}

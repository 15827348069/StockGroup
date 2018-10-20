package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xuhao
 * on 2017/10/24.
 * 持仓
 */

public class MatchDetailStockHoldAdapter extends ListAdapter<HolderPositionBean.Result.Stocks> {
    private OnCommit onClickListener;
    private ViewHolder mHolder;

    public void setOnClickListener(OnCommit onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MatchDetailStockHoldAdapter(Activity context) {
        super(context);
//        LayoutInflater inflater = LayoutInflater.from(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_match_stock_hold;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getHolderView(int position, View convertView, final HolderPositionBean.Result.Stocks stockholdsBean) {
        mHolder = (ViewHolder) convertView.getTag();
        if (mHolder == null) {
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        }
        mHolder.tvPrice.setText(String.format("%.2f", stockholdsBean.getPrice_buy()));
        mHolder.tvPrice2.setText(String.format("%.2f", stockholdsBean.getCurrent()));
        mHolder.tvYield.setText(String.format("%+.2f%%", stockholdsBean.getYield_float() * 100));
        mHolder.tvName.setText(stockholdsBean.getName());
        mHolder.tvCommit.setText(String.valueOf(stockholdsBean.getCommnet_count()));
        mHolder.tvYield.setTextColor(stockholdsBean.getYield_float()>0?mContext.getResources().
                getColor(R.color.red):mContext.getResources().getColor(R.color.green));
        if (onClickListener != null) {
            mHolder.tvCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onCommit(stockholdsBean);
                }
            });
        }
        return convertView;
    }

    public void setCommentCount(int comments){
        if (mHolder!=null){
            mHolder.tvCommit.setText(String.valueOf(comments));
        }
    }

    public class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_price2)
        TextView tvPrice2;
        @BindView(R.id.tv_yield)
        TextView tvYield;
        @BindView(R.id.tv_commit)
        TextView tvCommit;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnCommit {
        void onCommit(HolderPositionBean.Result.Stocks stockholdsBean);
    }
}

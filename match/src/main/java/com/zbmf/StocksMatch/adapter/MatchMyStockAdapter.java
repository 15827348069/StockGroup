package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.StockholdsBean;
import com.zbmf.StocksMatch.listener.RemarkBtnClick;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public class MatchMyStockAdapter extends ListAdapter<StockholdsBean> implements View.OnClickListener {
    private RemarkBtnClick mRemarkBtnClick;
    private Handler mHandler;
    private Message mMessage;

    public MatchMyStockAdapter(Activity context, Handler handler) {
        super(context);
        this.mHandler = handler;
        mMessage = Message.obtain();
    }

    @Override
    protected int getLayout() {
        return R.layout.item_mine_stock_layout;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getHolderView(final int position, View convertView, final StockholdsBean stockholdsBean) {
        ViewHold hold = (ViewHold) convertView.getTag();
        if (hold == null) {
            hold = new ViewHold(convertView);
            convertView.setTag(hold);
        }
        hold.tvName.setText(stockholdsBean.getStock_name());
        hold.tvSymbol.setText(stockholdsBean.getSymbol());
        hold.tvPrice.setText(String.format("%.2f", stockholdsBean.getCurrent()));
        double yield = stockholdsBean.getClose() != 0 ? (stockholdsBean.getCurrent() - stockholdsBean.getClose()) / stockholdsBean.getClose() * 100 : 0;
        hold.tvYield.setText(String.format("%+.2f%%", yield));
        if (yield == 0) {
            hold.tvYield.setTextColor(mContext.getResources().getColor(R.color.black_66));
        } else {
            hold.tvYield.setTextColor(yield >= 0 ? mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.green));
        }
        hold.remarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemarkBtnClick != null) {
                    mRemarkBtnClick.skipRemarkList(stockholdsBean);
                }
            }
        });
//        ((SwipeMenuLayout) mHold.swipeItem).setIos(false).setLeftSwipe(position % 2 == 0).setSwipeEnable(false);
        hold.swipeItem.setIos(true);
        //delete
        hold.itemDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessage = mHandler.obtainMessage();
                mMessage.what = 1;
                mMessage.arg1 = position;
                mHandler.sendMessage(mMessage);
                //这里调用模拟点击item menu外部内容来关闭item
                SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                if (null != viewCache) {
                    viewCache.smoothClose();
                }
                notifyDataSetChanged();
            }
        });
        //item的点击事件
        hold.leftItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessage = mHandler.obtainMessage();
                mMessage.what = 2;
                mMessage.arg2 = position;
                mHandler.sendMessage(mMessage);
            }
        });

        return convertView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_item:

                break;
            case R.id.item_delete_btn:

                break;
        }
    }

    public class ViewHold {
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
        @BindView(R.id.swipe_item)
        SwipeMenuLayout swipeItem;

        public ViewHold(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public void setRemarkBtnClick(RemarkBtnClick remarkBtnClick) {
        this.mRemarkBtnClick = remarkBtnClick;
    }

}

package com.zbmf.StocksMatch.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.listener.BuyClick;
import com.zbmf.StocksMatch.listener.SelectPhoto;
import com.zbmf.StocksMatch.listener.SellClick;
import com.zbmf.StocksMatch.listener.TakePhoto;

/**
 * Created by pq
 * on 2018/4/9.
 */

public class BuyDialog extends Dialog {
    private Context mContext;
    private TextView mBuy, mSell;
    private Button mCancel;
    private HolderPositionBean.Result.Stocks stocks;

    public BuyDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public BuyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected BuyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public BuyDialog createDialog() {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.buy_dialog, null);
        //获得dialog的window窗口
        Window window = getWindow();
        //设置dialog在屏幕底部
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.bottomDialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        this.setContentView(dialogView);
        mBuy = dialogView.findViewById(R.id.buy);
        mSell = dialogView.findViewById(R.id.sell);
        mCancel = dialogView.findViewById(R.id.cancel);
        return this;
    }

    public BuyDialog setTopTv(String str) {
        if (!TextUtils.isEmpty(str)) {
            mBuy.setText(str);
        }
        return this;
    }

    public BuyDialog setBottomTv(String str) {
        if (!TextUtils.isEmpty(str)) {
            mSell.setText(str);
        }
        return this;
    }

    public BuyDialog setStocks(HolderPositionBean.Result.Stocks stocks) {
        this.stocks = stocks;
        return this;
    }

    public BuyDialog setBuyClick(final SelectPhoto selectPhoto) {
        if (selectPhoto != null) {
            mBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPhoto.skipSelectPhotos();
                }
            });
        }
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissMissI();
            }
        });
        return this;
    }

    public BuyDialog setSellClick(final TakePhoto takePhoto) {
        if (takePhoto != null) {
            mSell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto.skipTakePhoto();
                }
            });
        }
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissMissI();
            }
        });
        return this;
    }

    public BuyDialog setBuyClick(final BuyClick buyClick) {
        mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyClick.buyClick(stocks);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissMissI();
            }
        });
        return this;
    }

    public BuyDialog setSellClick(final SellClick sellClick) {
        mSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellClick.sellClick(stocks);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissMissI();
            }
        });
        return this;
    }

    public BuyDialog showI() {
        show();
        return this;
    }

    public BuyDialog dissMissI() {
        if (isShowing()) {
            dismiss();
        }
        return this;
    }
}

package com.zbmf.StockGroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockMode;

/**
 * Created by xuhao on 2017/7/27.
 */

public class StockModeDialog extends Dialog {
    private OnBtnClick btnClick;
    private StockMode stockMode;

    public StockModeDialog setStockMode(StockMode stockMode) {
        this.stockMode = stockMode;
        return this;
    }

    public StockModeDialog setBtnClick(OnBtnClick btnClick) {
        this.btnClick = btnClick;
        return this;
    }

    public StockModeDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }
    public StockModeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initDialog();
    }
    public static StockModeDialog createDialog(Context context) {
        return new StockModeDialog(context, R.style.myDialogTheme);
    }

    public void initDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_stock_mdoe_layout, null);
        setContentView(view);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.dialoganimstyle);
        setCancelable(true);
        win.findViewById(R.id.btn_cause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        win.findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnClick!=null){
                    btnClick.buy(stockMode);
                    dismiss();
                }
            }
        });
        win.findViewById(R.id.btn_buy_incl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnClick!=null){
                    btnClick.buyincl(stockMode);
                    dismiss();
                }
            }
        });
        win.findViewById(R.id.btn_common).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnClick!=null){
                    btnClick.common(stockMode);
                    dismiss();
                }
            }
        });
        win.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnClick!=null){
                    btnClick.share(stockMode);
                    dismiss();
                }
            }
        });
        win.findViewById(R.id.btn_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnClick!=null){
                    btnClick.copy(stockMode);
                    dismiss();
                }
            }
        });
    }
    public interface OnBtnClick{
        void common(StockMode stockMode);
        void buy(StockMode stockMode);
        void share(StockMode stockMode);
        void buyincl(StockMode stockMode);
        void copy(StockMode stockMode);
    }
}

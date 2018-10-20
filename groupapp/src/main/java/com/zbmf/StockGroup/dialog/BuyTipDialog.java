package com.zbmf.StockGroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.listener.ClickAgreeRiskListener;

/**
 * Created by pq
 * on 2018/5/23.
 */

public class BuyTipDialog extends Dialog {
    private Context mContext;
    private Button agree_risk_btn;
    private ImageView risk_no_agree_btn;
    public BuyTipDialog(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    public BuyTipDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
    }

    protected BuyTipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
    }

    public BuyTipDialog createDialog() {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.buy_tip_dialog, null);
        //获得dialog的window窗口
        Window window = getWindow();
        //设置dialog在屏幕底部
        assert window != null;
        window.setGravity(Gravity.CENTER);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
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
        agree_risk_btn = (Button)dialogView.findViewById(R.id.agree_risk_btn);
        risk_no_agree_btn = (ImageView) dialogView.findViewById(R.id.risk_no_agree_btn);
        return this;
    }
    public BuyTipDialog clickAgreeRiskBtn(final ClickAgreeRiskListener clickAgreeRiskListener){
        agree_risk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickAgreeRiskListener!=null){
                    clickAgreeRiskListener.agreeRisk();
                }
            }
        });
        risk_no_agree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissRiskTipDialog();
            }
        });
        return this;
    }
    public void showRiskTipDialog(){
        if (!isShowing()){
            show();
        }
    }
    public void dismissRiskTipDialog(){
        if (isShowing()){
            dismiss();
        }
    }
}

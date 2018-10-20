package com.zbmf.StocksMatch.view;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class BottomDialog extends Dialog {
    private Context mContext;
    private TextView mTv1,mTv2,mTv3,mTv4;
    private Button mBottomBtn,mBottomBtn1;
    private ImageView mIvClose;

    public BottomDialog(@NonNull Context context) {
        super(context);
        this.mContext=context;
    }

    public BottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;

    }

    protected BottomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BottomDialog showDialog(){
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.bottom_dailog, null);
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
        mIvClose = dialogView.findViewById(R.id.iv_close);
        mTv1 = dialogView.findViewById(R.id.bottom_tv1);
        mTv2 = dialogView.findViewById(R.id.bottom_tv2);
        mTv3 = dialogView.findViewById(R.id.bottom_tv3);
        mTv4 = dialogView.findViewById(R.id.bottom_mfb);
        mBottomBtn = dialogView.findViewById(R.id.bottom_btn);
        mBottomBtn1 = dialogView.findViewById(R.id.bottom_btn1);
        return this;
    }
    public BottomDialog setText4(String text1){
        mTv4.setText(text1);
        return this;
    }
    public BottomDialog setText1(String text1){
        mTv1.setText(text1);
        return this;
    }
    public BottomDialog setText2(String text2){
        mTv2.setText(text2);
        return this;
    }
    public BottomDialog setText2Color(int color){
        mTv2.setTextColor(color);
        return this;
    }
    @SuppressLint("SetTextI18n")
    public BottomDialog setText3(String text3){
        mTv3.setText(mContext.getString(R.string.account_sum) + text3 + mContext.getString(R.string.mfb));
        return this;
    }
    public BottomDialog setOnClickListener(View.OnClickListener onClickListener){
        mBottomBtn.setOnClickListener(onClickListener);
        return this;
    };
    public BottomDialog setOnBtn1ClickListener(View.OnClickListener onClickListener){
        mBottomBtn1.setOnClickListener(onClickListener);
        return this;
    };
    public BottomDialog setBtnTv(String btnTv){
        if (!TextUtils.isEmpty(btnTv)){
            mBottomBtn.setText(btnTv);
        }
        return this;
    }
    public BottomDialog setBtn1Tv(String btnTv){
        if (!TextUtils.isEmpty(btnTv)){
            mBottomBtn1.setText(btnTv);
        }
        return this;
    }
    public BottomDialog setBtn1Visibility(int visibility){
        mBottomBtn1.setVisibility(visibility);
        return this;
    }
    public BottomDialog setBtnVisibility(int visibility){
        mBottomBtn.setVisibility(visibility);
        return this;
    }
    public BottomDialog showI(){
        show();
        return this;
    }
    public void closeDialog(View.OnClickListener clickListener){
        mIvClose.setOnClickListener(clickListener);
    }

    public BottomDialog dissMissI(){
        if (isShowing()) dismiss();
        return this;
    }
}

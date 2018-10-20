package com.zbmf.StocksMatch.view;

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
import android.widget.EditText;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.listener.PublishCommentsStr;

/**
 * Created by pq
 * on 2018/4/1.
 */

public class StockCommentsDialog extends Dialog {
    private Context mContext;
    private EditText mInputComment;
    private Button mMPublishBtn;
    private Button mCancelBtn;

    public StockCommentsDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public StockCommentsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected StockCommentsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public StockCommentsDialog getDialog() {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.comment_bottom_edit_dailog, null);
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
        mInputComment = dialogView.findViewById(R.id.input_comment);
        mMPublishBtn = dialogView.findViewById(R.id.publishBtn);
        mCancelBtn = dialogView.findViewById(R.id.cancelBtn);
        return this;
    }

    public StockCommentsDialog showC() {
        if (!this.isShowing()) {
            this.show();
        }
        return this;
    }

    public void dissC() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }
    private boolean isDisMiss=true;
    public void setInputCommentTipBg(){
        mInputComment.setBackgroundResource(R.drawable.add_stock_et_bg_tip);
        isDisMiss=false;
    }
    public void setInputCommentNormalBg(){
        mInputComment.setBackgroundResource(R.drawable.add_stock_et_bg);
    }
    public void setOnPublishComments(final PublishCommentsStr publishCommentsStr){
        mMPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publishCommentsStr!=null){
                    publishCommentsStr.getPublishComments(mInputComment.getText().toString());
                }
                if (isDisMiss){
                    dissC();
                    isDisMiss=true;
                }
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputComment.getText().clear();
                dissC();
            }
        });
    }
}

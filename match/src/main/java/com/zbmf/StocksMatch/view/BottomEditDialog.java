package com.zbmf.StocksMatch.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.listener.RemarkEditStr;

/**
 * Created by pq
 * on 2018/3/30.
 */

public class BottomEditDialog extends Dialog {
    private Context mContext;
    private EditText mInput_remark;
    private Button mAddBtn,cancelBtn;
    private RemarkEditStr mRemarkEditStr;

    public BottomEditDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public BottomEditDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected BottomEditDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public BottomEditDialog getDialog() {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.bottom_edit_dailog, null);
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
        mInput_remark = dialogView.findViewById(R.id.input_remark);
        mAddBtn = dialogView.findViewById(R.id.addBtn);
        cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        return this;
    }

    public BottomEditDialog showI() {
        if (!this.isShowing()) {
            this.show();
        }
        return this;
    }

    public void dissI() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

    public void setOnAddClick(final RemarkEditStr remarkEditStr) {
        this.mRemarkEditStr=remarkEditStr;
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remarkEditStr!=null){
                    remarkEditStr.editRemark(mInput_remark.getText().toString());
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInput_remark.getText().clear();
                dissI();
            }
        });
    }
}

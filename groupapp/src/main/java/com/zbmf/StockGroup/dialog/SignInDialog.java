package com.zbmf.StockGroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zbmf.StockGroup.R;

/**
 * Created by xuhao on 2017/7/27.
 */

public class SignInDialog extends Dialog {
    public SignInDialog setMessage(String message){
        TextView point1= (TextView) getWindow().findViewById(R.id.tv_sign_point);
        point1.setText("+"+message);
        TextView point2= (TextView) getWindow().findViewById(R.id.tv_sign_add_point);
        point2.setText("获得"+message+"积分");
        return this;
    }
    public SignInDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }

    public SignInDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    public static SignInDialog createDialog(Context context) {
        return new SignInDialog(context, R.style.myDialogTheme);
    }

    public void initDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sign_in_layout, null);
        setContentView(view);
        Window win = getWindow();
        win.setGravity(Gravity.CENTER);
        win.setWindowAnimations(R.style.mz_dialoganimstyle);
        setCancelable(true);
        win.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}

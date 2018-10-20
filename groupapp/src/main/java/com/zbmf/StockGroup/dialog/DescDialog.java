package com.zbmf.StockGroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zbmf.StockGroup.R;

/**
 * Created by xuhao on 2017/7/27.
 */

public class DescDialog extends Dialog {
    public DescDialog setMessage(String message){
        TextView textView= (TextView) getWindow().findViewById(R.id.dialog_message);
        textView.setText(message);
        return this;
    }
    public DescDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }

    public DescDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    public static DescDialog createDialog(Context context) {
        return new DescDialog(context, R.style.myDialogTheme);
    }

    public void initDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_desc_layout, null);
        setContentView(view);
        Window win = getWindow();
        win.setGravity(Gravity.CENTER);
        win.setWindowAnimations(R.style.mz_dialoganimstyle);
        setCancelable(true);
    }

}

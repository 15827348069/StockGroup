package com.zbmf.StocksMatch.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.listener.DialogNoClick;
import com.zbmf.StocksMatch.listener.DialogYesClick;

/**
 * Created by xuhao
 * on 2017/7/27.
 */

public class TextDialog extends Dialog implements View.OnClickListener {
    private DialogNoClick leftClick;
    private DialogYesClick rightClick;

    public TextDialog setLeftClick(DialogNoClick leftClick) {
        this.leftClick = leftClick;
        return this;
    }

    public TextDialog setRightClick(DialogYesClick rightClick) {
        this.rightClick = rightClick;
        return this;
    }

    public TextDialog setTitle(String message){
        TextView textView= (TextView) getWindow().findViewById(R.id.tv_title);
        textView.setVisibility(View.VISIBLE);
        textView.setText(message);
        return this;
    }
    public TextDialog setMessage(String message){
        TextView textView= (TextView) getWindow().findViewById(R.id.tv_message);
        textView.setText(message);
        return this;
    }
    public TextDialog setLeftButton(String message){
        TextView textView= (TextView) getWindow().findViewById(R.id.tv_no);
        textView.setText(message);
        textView.setOnClickListener(this);
        return this;
    }
    public TextDialog setRightButton(String message){
        TextView textView= (TextView) getWindow().findViewById(R.id.tv_yes);
        textView.setText(message);
        textView.setOnClickListener(this);
        return this;
    }
    public TextDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }

    public TextDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    public static TextDialog createDialog(Context context) {
        return new TextDialog(context, R.style.myDialogTheme);
    }

    public void initDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tip, null);
        setContentView(view);
        Window win = getWindow();
        win.setGravity(Gravity.CENTER);
        win.setWindowAnimations(R.style.mz_dialoganimstyle);
        setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()){
            case R.id.tv_no:
                if(leftClick!=null){
                    leftClick.onNoClick();
                }
                break;
            case R.id.tv_yes:
                if(rightClick!=null){
                    rightClick.onYseClick();
                }
                break;
        }
        this.dismiss();
    }
}

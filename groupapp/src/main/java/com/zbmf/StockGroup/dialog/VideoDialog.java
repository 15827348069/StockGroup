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

public class VideoDialog extends Dialog implements View.OnClickListener {
    private OnCommitClick onCommitClick;

    public VideoDialog setOnCommitClick(OnCommitClick onCommitClick) {
        this.onCommitClick = onCommitClick;
        return this;
    }
    public VideoDialog setMessage(String message){
        TextView textView= (TextView) getWindow().findViewById(R.id.dialog_message);
        textView.setText(message);
        return this;
    }
    public VideoDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }

    public VideoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    public static VideoDialog CreateDialog(Context context) {
        return new VideoDialog(context, R.style.myDialogTheme);
    }

    public void initDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_video_layout, null);
        setContentView(view);
        view.findViewById(R.id.sure_button).setOnClickListener(this);
        view.findViewById(R.id.cause_button).setOnClickListener(this);
        Window win = getWindow();
        win.setGravity(Gravity.CENTER);
        win.setWindowAnimations(R.style.mz_dialoganimstyle);
        setCancelable(true);
    }

    public static final int COMMIT = 1;
    public static final int AGAIN = 0;

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.sure_button && onCommitClick != null) {
            onCommitClick.onComint(COMMIT);
        }
        if (v.getId() == R.id.cause_button && onCommitClick != null) {
            onCommitClick.onComint(AGAIN);
        }
    }

    public interface OnCommitClick {
        void onComint(int flag);
    }
}

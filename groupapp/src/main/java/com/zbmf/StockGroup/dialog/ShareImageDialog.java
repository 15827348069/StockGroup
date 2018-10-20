package com.zbmf.StockGroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.utils.BitmapUtil;

/**
 * Created by xuhao on 2017/7/27.
 */

public class ShareImageDialog extends Dialog implements View.OnClickListener {
    private OnBtnClick btnClick;
    public ShareImageDialog setBtnClick(OnBtnClick btnClick) {
        this.btnClick = btnClick;
        return this;
    }
    public ShareImageDialog setImage(final Bitmap bitmap) {
        final ImageView imageView= (ImageView) getWindow().findViewById(R.id.share_image);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
        return this;
    }
    public ShareImageDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }
    public ShareImageDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initDialog();
    }
    public static ShareImageDialog createDialog(Context context) {
        return new ShareImageDialog(context, R.style.myDialogTheme);
    }

    public void initDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.share_img_dialog, null);
        setContentView(view);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.dialoganimstyle);
        setCancelable(true);
        win.findViewById(R.id.tv_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_send:
                dismiss();
                if(btnClick!=null){
                    btnClick.send();
                }
                break;
        }
    }

    public interface OnBtnClick{
        void send();
    }
}

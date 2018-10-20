package com.zbmf.StockGroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

/**
 * Created by xuhao on 2017/7/27.
 */

public class StockModeDescDialog extends Dialog {
    public StockModeDescDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }

    public StockModeDescDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    public static StockModeDescDialog createDialog(Context context) {
        return new StockModeDescDialog(context, R.style.myDialogTheme);
    }

    public void initDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_stockmode_img_layout, null);
        setContentView(view);
        Window win = getWindow();
        win.setGravity(Gravity.CENTER);
        win.setWindowAnimations(R.style.mz_dialoganimstyle);
        view.findViewById(R.id.imv_mxxg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setCancelable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SettingDefaultsManager.getInstance().setStockModeDesc(true);
            }
        });
    }

}

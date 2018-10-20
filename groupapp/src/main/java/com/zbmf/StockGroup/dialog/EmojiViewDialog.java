package com.zbmf.StockGroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.view.EmojiView;

/**
 * Created by pq
 * on 2018/7/4.
 */

public class EmojiViewDialog extends Dialog {
    private OnDiss onDiss;
    private String mEmoji="";

    public EmojiViewDialog setDiss(OnDiss onDiss) {
        this.onDiss = onDiss;
        return this;
    }

    public static EmojiViewDialog createDialog(Context context) {
        return new EmojiViewDialog(context);
    }

    public static EmojiViewDialog createDialog(Context context, int thmeResid) {
        return new EmojiViewDialog(context, thmeResid);
    }

    public EmojiViewDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public EmojiViewDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private void initView(final Context context) {
        setContentView(R.layout.emoji_view_layout);
        Window win = getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        EmojiView ll_expand = (EmojiView) win.findViewById(R.id.emoji_view);
        ll_expand.setVisibility(View.VISIBLE);
        ll_expand.setOnItemClickListener(new EmojiView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEmoji = (String) parent.getItemAtPosition(position);
                if (onDiss != null) {
                    onDiss.onDiss(EditTextUtil.getContent(context, mEmoji.trim()),mEmoji.length());
                }
            }
        });
    }

    public interface OnDiss {
        void onDiss(SpannableString content,int length);
    }
}

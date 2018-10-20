package com.zbmf.StockGroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.view.EmojiView;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by xuhao on 2017/7/26.
 */

public class EditTextDialog extends Dialog {
    OnSendClick sendClick;
    OnDiss onDiss;
    private FbComment mFbComment;
    EditText et_input;
    Button send,send_private_layout,send_comment_btn;
    EmojiView ll_expand;
    ImageView iv_emoji;
    private String content="";
    private InputMethodManager imm;
    private boolean is_show;
    public static EditTextDialog createDialog(Context context){
        return new EditTextDialog(context);
    }
    public static EditTextDialog createDialog(Context context,int thmeResid){
        return new EditTextDialog(context,thmeResid);
    }
    public EditTextDialog setDiss(OnDiss onDiss) {
        this.onDiss = onDiss;
        return this;
    }
    public EditTextDialog setSendClick(OnSendClick sendClick) {
        this.sendClick = sendClick;
        return this;
    }
    public EditTextDialog setFbComment(FbComment fbComment) {
        this.mFbComment = fbComment;
        return this;
    }

    public EditTextDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public EditTextDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView(context);
    }
    private void initView(final Context context){
        setContentView(R.layout.blog_detail_pinglun_layout);
        imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        Window win =getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        ll_expand= (EmojiView) win.findViewById(R.id.emoji_view);
        et_input=(EditText) win.findViewById(R.id.blog_detail_pinglun_edit);
        send= (Button) win.findViewById(R.id.send_pinglun_layout);
        send_comment_btn= (Button) win.findViewById(R.id.send_comment_btn);
        send_private_layout= (Button) findViewById(R.id.send_private_layout);
        iv_emoji= (ImageView) win.findViewById(R.id.iv_emoji);
        iv_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_expand.getVisibility()==View.VISIBLE){
                    iv_emoji.setSelected(false);
                    ll_expand.setVisibility(View.GONE);
                }else{
                    ll_expand.setVisibility(View.VISIBLE);
                    iv_emoji.setSelected(true);
                    et_input.requestFocus();
                    imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
                }
            }
        });
        ll_expand.setOnItemClickListener(new EmojiView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getAdapter().getCount() - 1) {
                    et_input.dispatchKeyEvent(new KeyEvent(
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {
                    String emoji = (String) parent.getItemAtPosition(position);
                    int selection = et_input.getSelectionStart();
                    StringBuilder sb = new StringBuilder();
                    sb.append(content.substring(0, selection)).append(emoji).append(content.substring(selection, content.length()));
                    et_input.setText(EditTextUtil.getContent(context,sb.toString()));
                    et_input.setSelection(selection + emoji.length());
                }
            }
        });
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.equals("")||editable.length()==0){
                    send.setEnabled(false);
                    send_comment_btn.setEnabled(false);
                    send.setBackground(context.getResources().getDrawable(R.drawable.shape_layout_gray_g15dp));
                    send_private_layout.setEnabled(false);
                    send_private_layout.setBackground(context.getResources().getDrawable(R.drawable.shape_layout_gray_g15dp));
                }else{
                    send.setEnabled(true);
                    send_comment_btn.setEnabled(true);
                    send.setBackground(context.getResources().getDrawable(R.drawable.shape_layout_red_button));
                    send_private_layout.setEnabled(true);
                    send_private_layout.setBackground(context.getResources().getDrawable(R.drawable.shape_layout_red_button));
                }
                content = editable.toString().trim();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sendClick!=null){
                    sendClick.onSend(et_input.getText().toString(),0);
                }
                et_input.setText("");
                if(onDiss!=null){
                    onDiss.onDiss(new SpannableString(""));
                }
            }
        });
        send_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFbComment!=null){
                    mFbComment.fbComment(et_input.getText().toString());
                    et_input.getText().clear();
                }
            }
        });
        send_private_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //私密提问
                if(sendClick!=null){
                    sendClick.onSend(et_input.getText().toString(),1);
                }
            }
        });
       setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_input.getWindowToken(),0); //强制隐藏键盘
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                is_show=false;
                if(onDiss!=null){
                    onDiss.onDiss(EditTextUtil.getContent(context,et_input.getText().toString()));
                }
                iv_emoji.setSelected(false);
                if(ll_expand!=null&ll_expand.getVisibility()==View.VISIBLE){
                    ll_expand.setVisibility(View.GONE);
                }
            }
        });
       setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if(!is_show){
                    showSoftInputFromWindow(et_input);
                }
            }
        });
        et_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_expand.setVisibility(View.GONE);
                iv_emoji.setSelected(false);
                et_input.setFocusableInTouchMode(true);
                et_input.setFocusable(true);
                et_input.requestFocus();
                return false;
            }
        });
    }
    /**
     * EditText获取焦点并显示软键盘
     */
    private void showSoftInputFromWindow(EditText editText) {
        LogUtil.e("显示软键盘");
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    /**
     * 设置输入框HINT
     * @param hint
     */
    public EditTextDialog setEditHint(String hint){
        et_input.setHint(hint);
        return this;
    }
    public void showEmail(){
        is_show=true;
        ll_expand.setVisibility(View.VISIBLE);
        iv_emoji.setSelected(true);
        et_input.setFocusable(false);
        et_input.setFocusableInTouchMode(false);
        et_input.requestFocus();
        imm.hideSoftInputFromWindow(et_input.getWindowToken(),0);
        show();
    }
    /**
     * 设置右侧按钮信息
     * @param message
     */
    public EditTextDialog setRightButton(String message){
        send.setVisibility(View.VISIBLE);
        send.setText(message);
        send.setEnabled(false);
        return this;
    }
    //设置右侧发送评论的按钮是否显示
    public EditTextDialog setRightSendMsgBtnShow(String tv){
        send_comment_btn.setVisibility(View.VISIBLE);
        send_comment_btn.setText(tv);
        send_comment_btn.setEnabled(true);
        return this;
    }

    /**
     * 设置是否显示表情按钮
     * @param visibility
     * @return
     */
    public EditTextDialog setEmailVisibility(int visibility){
        iv_emoji.setVisibility(visibility);
        return this;
    }

    /**
     * 设置左侧按钮信息
     * @param message
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public EditTextDialog setLeftButton(String message){
        send_private_layout.setVisibility(View.VISIBLE);
        send_private_layout.setEnabled(false);
        send_private_layout.setText(message);
        return this;
    }
    public interface  OnSendClick{
        void onSend(String message,int type);
    }
    public interface OnDiss{
        void onDiss(SpannableString content);
    }
    public interface FbComment{
        void fbComment(String comment);
    }
}

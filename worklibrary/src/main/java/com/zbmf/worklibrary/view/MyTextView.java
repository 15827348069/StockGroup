package com.zbmf.worklibrary.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zbmf.worklibrary.R;
import com.zbmf.worklibrary.util.Logx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuhao on 2018/1/5.
 */

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    private String format;
    private String message;
    private OnTextClickListener clickListener;//点击回调
    public MyTextView(Context context) {
        super(context);
    }
    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initStyle(attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(attrs);
    }
    private void initStyle(AttributeSet attrs){
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
        format = mTypedArray.getString(R.styleable.MyTextView_format);
        message= mTypedArray.getString(R.styleable.MyTextView_message);
        mTypedArray.recycle();
        if(message!=null){
            setMessageText(message);
        }
        avoidHintColor(this);
    }
    public void setTextClickListener(OnTextClickListener listener) {
        clickListener = listener;
    }
    public interface OnTextClickListener<T> {
        void OnTextClickListener(T t);
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setMessageText(String textStr){
        Logx.e("format"+format);
        setText(format==null?textStr:getspannableMessage(textStr));
        setMovementMethod(LinkMovementMethod.getInstance());
    }
    public SpannableString getspannableMessage(String content){
        SpannableString spanableInfo = new SpannableString(content);
        Pattern message_p = Pattern.compile(format);
        Matcher message_p_m = message_p.matcher(content);
        boolean message_find=message_p_m.find();
        if(message_find){
            while(message_find){
                spanableInfo.setSpan(new Clickable(clickListener,message_p_m.group()),message_p_m.start(),message_p_m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                message_find=message_p_m.find(message_p_m.end());
            }
        }
        return spanableInfo;
    }
    private void avoidHintColor(View view){
        if(view instanceof TextView)
            ((TextView)view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }
    class Clickable<T> extends ClickableSpan {
        private final OnTextClickListener mListener;
        private T t;
        public Clickable(OnTextClickListener l,T t1) {
            mListener = l;
            t=t1;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.OnTextClickListener(t);
            }
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.blue1));
        }
    }
}

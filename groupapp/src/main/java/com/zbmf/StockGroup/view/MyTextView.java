package com.zbmf.StockGroup.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.LiveTypeMessage;
import com.zbmf.StockGroup.constans.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by xuhao
 * on 2016/12/26.
 */

public class MyTextView extends TextView {
    private OnTextClickListener clickListener;//点击回调
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
    public void setMessageText(String countent){
         setText(getspannableMessage(countent));
         setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void setBoxMessage(String name,String box_name,String box_id,int box_leaver){
        String countent=name+box_name;
        SpannableString spanableInfo = new SpannableString(countent);
        LiveTypeMessage message=new LiveTypeMessage();
        message.setMessage(box_id);
        message.setBox_leaver(box_leaver);
        message.setMessage_type("box_message");
        int start=0;
        if(name!=null){
            start=name.length();
        }
        int end=countent.length();
        spanableInfo.setSpan(new Clickable(clickListener,message),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spanableInfo);
        setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void setBlogMessage(String blog_message,String blog_name,String blog_id){
        String countent=blog_message+blog_name;
        SpannableString spanableInfo = new SpannableString(countent);
        LiveTypeMessage message=new LiveTypeMessage();
        message.setMessage(blog_id);
        message.setMessage_type("blog_message");
        int start=0;
        if(blog_message!=null){
            start=blog_message.length();
        }
        int end=countent.length();
        spanableInfo.setSpan(new Clickable(clickListener,message),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spanableInfo);
        setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void setScreenMessage(String content,String screen_id){

        String Screen=getResources().getString(R.string.screen_message);
        Pattern Screen_p = Pattern.compile(Screen);
        Matcher Screen_m = Screen_p.matcher(content);
        boolean Screen_find=Screen_m.find();
        if(Screen_find){
            content=content.replace(Screen_m.group(),getResources().getString(R.string.commit_screen));
        }
        SpannableString spanableInfo = new SpannableString(content);
        Pattern message_p = Pattern.compile(getResources().getString(R.string.commit_screen));
        Matcher message_p_m = message_p.matcher(content);
        boolean message_find=message_p_m.find();
        while(message_find){
            LiveTypeMessage message=new LiveTypeMessage();
            message.setMessage(screen_id);
            message.setMessage_type("screen_message");
            spanableInfo.setSpan(new Clickable(clickListener,message),message_p_m.start(),message_p_m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            message_find=message_p_m.find(message_p_m.end());
        }
        setText(spanableInfo);
        setMovementMethod(LinkMovementMethod.getInstance());
    }
    public SpannableString getspannableMessage(String countent){
        if(countent==null){
            return null;
        }
        SpannableString spanableInfo = new SpannableString(countent);

        String Stock="(((002|000|300|600|601)[\\d]{3})|60[\\d]{4})";
        Pattern Stock_p = Pattern.compile(Stock);
        Matcher Stock_m = Stock_p.matcher(countent);
        boolean Stock_find=Stock_m.find();

        //判断是否有链接
        String url="(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        Pattern url_p = Pattern.compile(url);
        Matcher url_m = url_p.matcher(countent);
        boolean url_find=url_m.find();

        String regex = "\\[[\u4e00-\u9fa5\\w]+\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(countent);
        boolean regex_find=matcher.find();

        if(countent.equals(getResources().getString(R.string.add_to_tf))){
            Pattern message_p = Pattern.compile(getResources().getString(R.string.tf_zz));
            Matcher message_p_m = message_p.matcher(countent);
            boolean message_find=message_p_m.find();
            while(message_find){
                LiveTypeMessage message=new LiveTypeMessage();
                message.setMessage(message_p_m.group());
                message.setMessage_type("tf_message");
                spanableInfo.setSpan(new Clickable(clickListener,message),message_p_m.start(),message_p_m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                message_find=message_p_m.find(message_p_m.end());
            }
        }else if(countent.contains(getResources().getString(R.string.tf_commit))) {
            //立即订阅
            Pattern message_p = Pattern.compile(getResources().getString(R.string.tf_commit));
            Matcher message_p_m = message_p.matcher(countent);
            boolean message_find=message_p_m.find();
            while(message_find){
                LiveTypeMessage message=new LiveTypeMessage();
                message.setMessage(message_p_m.group());
                message.setMessage_type("commit_fans");
                spanableInfo.setSpan(new Clickable(clickListener,message),message_p_m.start(),message_p_m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                message_find=message_p_m.find(message_p_m.end());
            }
        }else if (countent.contains(getResources().getString(R.string.tf_gift))){
            Pattern message_p = Pattern.compile(getResources().getString(R.string.tf_gift));
            Matcher message_p_m = message_p.matcher(countent);
            boolean message_find=message_p_m.find();
            while(message_find){
                LiveTypeMessage message=new LiveTypeMessage();
                message.setMessage(message_p_m.group());
                message.setMessage_type("gift");
                spanableInfo.setSpan(new Clickable(clickListener,message),message_p_m.start(),message_p_m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                message_find=message_p_m.find(message_p_m.end());
            }
        }else if(Stock_find) {
            //判断是否是股票
            while (Stock_find) {
                LiveTypeMessage message = new LiveTypeMessage();
                message.setMessage(Stock_m.group());
                message.setMessage_type("stock");
                spanableInfo.setSpan(new Clickable(clickListener, message), Stock_m.start(), Stock_m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                Stock_find = Stock_m.find(Stock_m.end());
            }
        }else if(url_find) {
            while (url_find) {
                LiveTypeMessage message = new LiveTypeMessage();
                message.setMessage(url_m.group());
                message.setMessage_type("url");
                spanableInfo.setSpan(new Clickable(clickListener, message), url_m.start(), url_m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                url_find = url_m.find(url_m.end());
            }
        }else if(regex_find){
            while (matcher.find()) {
                String emojiStr = matcher.group();
                int start = matcher.start();
                Integer res = Constants.getEmojiIconMaps().get(emojiStr);
                if (res != null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
                    ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
                    spanableInfo.setSpan(imageSpan, start, start + emojiStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spanableInfo;
    }
    public void setTextClickListener(OnTextClickListener listener) {
        clickListener = listener;
    }
    public interface OnTextClickListener {
        void OnTextClickListener(LiveTypeMessage message);
    }
    class Clickable extends ClickableSpan {
        private final OnTextClickListener mListener;
        private LiveTypeMessage message;
        public Clickable(OnTextClickListener l,LiveTypeMessage click_message) {
            mListener = l;
            message=click_message;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            if(mListener==null){
                return;
            }
            mListener.OnTextClickListener(message);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            switch (message.getMessage_type()){
                case "url":
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case "stock":
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case "tf_message":
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case "box_message":
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case "coupons":
                    ds.setColor(getResources().getColor(R.color.colorAccent));
                    break;
                case "commit_fans":
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case "blog_message":
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case "gift":
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case "screen_message":
                    ds.setUnderlineText(true);
                    ds.setColor(getResources().getColor(R.color.colorAccent));
                    break;
            }

        }
    }
}

package com.zbmf.StockGroup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.constans.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by xuhao on 2016/12/12.
 */

public class EditTextUtil {
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }
    /***
     * 判断EditText是否为空
     *
     * @param et
     * @return
     */
    public static boolean isEmpty(EditText et) {
        if (TextUtils.isEmpty(et.getText())) {
            return false;
        } else {
            return true;
        }
    }
    public static SpannableString getContent(Context context, String source) {
        String regex = "\\[[\u4e00-\u9fa5\\w]+\\]";
        SpannableString spannableString = new SpannableString(source);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String emojiStr = matcher.group();
            int start = matcher.start();
            Integer res = Constants.getEmojiIconMaps().get(emojiStr);
            if (res != null) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                spannableString.setSpan(imageSpan, start, start + emojiStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }
    public static SpannableString getContent(Context context, TextView textView, String source) {
        String regex = "\\[[\u4e00-\u9fa5\\w]+\\]";
        SpannableString spannableString = new SpannableString(source);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String emojiStr = matcher.group();
            int start = matcher.start();
            Integer res = Constants.getEmojiIconMaps().get(emojiStr);
            if (res != null) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
//                bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                spannableString.setSpan(imageSpan, start, start + emojiStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }


    public static SpannableString rebackAt(Context context, String string) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.item_message)), 0, string.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
    public static boolean isMoreLines(TextView textView ,int lines){
        float allTextPx = textView.getPaint().measureText(textView.getText().toString());
        float showViewPx = (textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight()) * lines;
        return allTextPx > showViewPx;
    }
    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除4的任意数
     * 17+除9的任意数
     * 14+除0，2，3的任意数
     * 198 199
     */
    public static boolean isMobileNO(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[^4])|(17[0-8])|(14[1,4-9])|(19[8,9])|(16[6]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    public static boolean checkNum(String str) {
        String regexStr = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$";
        return Pattern.matches(regexStr, str);
    }
}

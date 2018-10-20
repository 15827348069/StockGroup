package com.zbmf.StockGTec.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuhao on 2016/12/12.
 */

public class EditTextUtil {
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

    public static SpannableString getContent(Context context, TextView textView, String source) {
        String regex = "\\[[\u4e00-\u9fa5\\w]+\\]";
        SpannableString spannableString = new SpannableString(source);
//        if (!TextUtils.isEmpty(part)) {
//            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.item_message)), 0, part.length(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ImageSpan imageSpan = new ImageSpan(context,createABitmap(context,part, (int) textView.getTextSize()));
//            spannableString.setSpan(imageSpan,0,part.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String emojiStr = matcher.group();
            int start = matcher.start();
            int size = (int) textView.getTextSize();
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

    public static Bitmap createABitmap(Context context,String string,int textSize) {
//        Rect mRect = new Rect();
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setTextSize(textSize);
//        paint.setColor(ContextCompat.getColor(context,R.color.item_message));
//        paint.getTextBounds(string,0,string.length(),mRect);
//
//        Bitmap bitmap = Bitmap.createBitmap(mRect.width()+4, mRect.height()+6, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        canvas.drawColor(Color.TRANSPARENT);
//        canvas.drawText(string, 2, textSize-2, paint);
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();
//        return bitmap;
        return null;
    }

    public static SpannableString rebackAt(Context context, String string) {
//        SpannableString spannableString = new SpannableString(string);
//        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.item_message)), 0, string.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        return spannableString;
        return null;
    }
}

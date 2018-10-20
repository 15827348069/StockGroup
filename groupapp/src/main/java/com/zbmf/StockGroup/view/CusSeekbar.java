package com.zbmf.StockGroup.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.zbmf.StockGroup.R;

import java.util.ArrayList;

/**
 * Created by iMac on 2017/1/11.
 */

public class CusSeekbar extends View {
    private final String TAG = "CustomSeekbar";
    private int width;
    private int height;
    private int downX = 0;
    private int downY = 0;
    private int upX = 0;
    private int upY = 0;
    private int moveX = 0;
    private int moveY = 0;
    private int perWidth = 0;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint buttonPaint;
    private Bitmap thumb;
    private Bitmap spot;
    private int cur_sections = 2;
    private ResponseOnTouch responseOnTouch;
    private int color = 0x80989898;
    private Rect mRect = new Rect();
    private String text = "标准";
    private int textSize1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22, getResources().getDisplayMetrics());
    private int textSize2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
    private int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());

    private ArrayList<String> section_title;

    public CusSeekbar(Context context) {
        super(context);
    }

    public CusSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cur_sections = 0;
        thumb = BitmapFactory.decodeResource(getResources(), R.drawable.shijian);
        spot = BitmapFactory.decodeResource(getResources(), R.mipmap.xian);
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        mTextPaint = new Paint(Paint.DITHER_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(0xff989898);
        buttonPaint = new Paint(Paint.DITHER_FLAG);
        buttonPaint.setAntiAlias(true);

    }

    public void initData(ArrayList<String> section) {
        if (section != null) {
            section_title = section;
        } else {
            String[] str = new String[]{"小", "中", "大"};
            section_title = new ArrayList<String>();
            for (int i = 0; i < str.length; i++) {
                section_title.add(str[i]);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        width = widthSize;
        height = heightSize;
        setMeasuredDimension(widthSize, heightSize);

        perWidth = (width - section_title.size() * spot.getWidth() - thumb.getWidth() / 2) / (section_title.size() - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(thumb.getWidth() / 2, height * 2 / 3, width - thumb.getWidth() /2, height * 2 / 3, mPaint);
        int section = 0;
        while (section < section_title.size()) {
            text = section_title.get(section);
            if (section == section_title.size() - 1)
                canvas.drawBitmap(spot, width - spot.getWidth() - thumb.getWidth()  / 2, height * 2 / 3 - spot.getHeight()/2, mPaint);
             else
                canvas.drawBitmap(spot, thumb.getWidth() / 2 + section * perWidth + section * spot.getWidth(), height * 2 / 3 - spot.getHeight()/2, mPaint);

            if (section == 0){
                mTextPaint.setTextSize(textSize2);
                canvas.drawText("A", thumb.getWidth() / 4 +  - measureTextHalfWidth(text), height * 2 / 3 - 80, mTextPaint);
            }

            if (section == section_title.size()-1){
                mTextPaint.setTextSize(textSize1);
                canvas.drawText("A", width-thumb.getWidth() , height * 2 / 3 - 80, mTextPaint);
            }

            if (section == 1)
                canvas.drawText(text, thumb.getWidth() / 4 + section * perWidth + section * spot.getWidth() - measureTextHalfWidth(text), height * 2 / 3 - 80, mTextPaint);
            section++;
        }
        if (cur_sections == section_title.size() - 1)
            canvas.drawBitmap(thumb, width - spot.getWidth() - thumb.getWidth(),height * 2 / 3 - thumb.getHeight()/2 , buttonPaint);
        else
            canvas.drawBitmap(thumb, thumb.getWidth() / 4 + cur_sections * perWidth + cur_sections * spot.getWidth(),height * 2 / 3 - thumb.getHeight()/2 , buttonPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                thumb = BitmapFactory.decodeResource(getResources(), R.drawable.shijian);
                downX = (int) event.getX();
                downY = (int) event.getY();
                responseTouch(downX, downY);
                break;
            case MotionEvent.ACTION_MOVE:
                thumb = BitmapFactory.decodeResource(getResources(), R.drawable.shijian);
                moveX = (int) event.getX();
                moveY = (int) event.getY();
                responseTouch(moveX, moveY);
                break;
            case MotionEvent.ACTION_UP:
                thumb = BitmapFactory.decodeResource(getResources(), R.drawable.shijian);
                upX = (int) event.getX();
                upY = (int) event.getY();
                responseTouch(upX, upY);
                responseOnTouch.onTouchResponse(cur_sections);
                break;
        }
        return true;
    }

    private void responseTouch(int x, int y) {
        if (x <= width - thumb.getWidth()  / 2) {
            cur_sections = (x + perWidth / 3) / perWidth;
        } else {
            cur_sections = section_title.size() - 1;
        }
        invalidate();
    }

    public void setResponseOnTouch(ResponseOnTouch response) {
        responseOnTouch = response;
    }

    private int measureTextHalfWidth(String text) {
        mTextPaint.getTextBounds(text, 0, text.length(), mRect);
        return mRect.width() / 3;
    }

    public void setCur_sections(int cur_sections) {
        this.cur_sections = cur_sections;
    }

    public interface ResponseOnTouch {
        public void onTouchResponse(int volume);
    }
}

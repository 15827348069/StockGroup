package com.zbmf.worklibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by xuhao on 2018/1/3.
 */

@SuppressLint("AppCompatCustomView")
public class ProcessImageView extends ImageView implements View.OnClickListener {
    private Status status;
    private Paint mPaint;// 画笔
    Context context = null;
    int progress = 0;
    public ProcessImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ProcessImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessImageView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
        setOnClickListener(this);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(status!=null){
            Rect rect = new Rect();
            switch (status){
                case FAIL:
                    mPaint.setAntiAlias(true); // 消除锯齿
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(Color.parseColor("#70000000"));// 半透明
                    canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                    mPaint.setTextSize(30);
                    mPaint.setColor(Color.RED);
                    mPaint.setStrokeWidth(2);
                    mPaint.getTextBounds("上传失败", 0, "上传失败".length(), rect);// 确定文字的宽度
                    canvas.drawText("上传失败", getWidth() / 2 - rect.width() / 2,
                            getHeight() / 2, mPaint);
                    break;
                case SUCCESS:
                    mPaint.setTextSize(30);
                    mPaint.setColor(Color.WHITE);
                    mPaint.setStrokeWidth(2);
                    canvas.drawText("", getWidth() / 2 - rect.width() / 2,
                            getHeight() / 2, mPaint);
                    break;
                case UPLOADING:
                    mPaint.setAntiAlias(true); // 消除锯齿
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(Color.parseColor("#50000000"));// 半透明
                    canvas.drawRect(0, 0, getWidth(), getHeight() - getHeight() * progress
                            / 100, mPaint);

                    mPaint.setColor(Color.parseColor("#00000000"));// 全透明
                    canvas.drawRect(0, getHeight() - getHeight() * progress / 100,
                            getWidth(), getHeight(), mPaint);
                    mPaint.getTextBounds("100%", 0, "100%".length(), rect);// 确定文字的宽度
                    canvas.drawText(progress + "%", getWidth() / 2 - rect.width() / 2,
                            getHeight() / 2, mPaint);
                    break;
            }
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }
    public void setStatus(Status status){
        this.status=status;
        postInvalidate();
    }

    @Override
    public void onClick(View view) {
        if(onFailClick!=null&&status==Status.FAIL){
            onFailClick.onFailClick(view);
        }
    }

    public enum Status {
        SUCCESS,
        FAIL,
        UPLOADING
    }
    private onFailClick onFailClick;

    public void setOnFailClick(ProcessImageView.onFailClick onFailClick) {
        this.onFailClick = onFailClick;
    }

    public interface onFailClick{
        void onFailClick(View view);
    }
}
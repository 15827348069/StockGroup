package com.zbmf.StocksMatch.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


import com.zbmf.StocksMatch.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义横向进度条
 * 可设置进度条速率
 * 可以实现是否初始进度动画，如需可加
 *
 * @author lu
 */
public class CustomMyProgress extends View {

    private Paint mReachedBarPaint;
    private Paint mUnreachedBarPaint;
    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);
    private final int default_reached_color = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(204, 204, 204);
    private final float default_reached_bar_height = dp2px(8f);
    private final float default_unreached_bar_height = dp2px(8f);
    private int mMaxProgress = 1100;
    private int progress = 0, initProgress = 0;
    private int mReachedBarColor, mReachedStartColor, mReachedEndColor;//默认bar的颜色、开始与结束颜色
    private int mUnreachedBarColor;
    private float mReachedBarHeight;
    private float mUnreachedBarHeight;
    private int mDymcResId = 0;
    private int runRate = 300;


    private boolean mDrawReachedBar = true;
    private boolean mDrawDymcResource = true;
    private int[] mColors = new int[2];
    private Bitmap[] mNiuBitmaps = new Bitmap[6];
    private Timer mTimer = null;

    public CustomMyProgress(Context context) {
        this(context, null);
    }

    public CustomMyProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomMyProgress, defStyleAttr, 0);
        mReachedBarColor = typedArray.getColor(R.styleable.CustomMyProgress_cus_progress_reached_color, default_reached_color);
        mReachedStartColor = typedArray.getColor(R.styleable.CustomMyProgress_cus_progress_reached_start_color, default_reached_color);
        mReachedEndColor = typedArray.getColor(R.styleable.CustomMyProgress_cus_progress_reached_end_color, default_reached_color);
        mUnreachedBarColor = typedArray.getColor(R.styleable.CustomMyProgress_cus_progress_unreached_color, default_unreached_color);
        mReachedBarHeight = typedArray.getDimension(R.styleable.CustomMyProgress_cus_progress_reached_bar_height, default_reached_bar_height);
        mUnreachedBarHeight = typedArray.getDimension(R.styleable.CustomMyProgress_cus_progress_unreached_bar_height, default_unreached_bar_height);
        mDymcResId = typedArray.getResourceId(R.styleable.CustomMyProgress_cus_inner_drawable, 0);
        runRate = typedArray.getInt(R.styleable.CustomMyProgress_cus_run_rate, 300);
        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setColor(mReachedBarColor);
        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setColor(mUnreachedBarColor);
        mColors[0] = mReachedStartColor;
        mColors[1] = mReachedEndColor;
        mNiuBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.niu1);
        mNiuBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.niu2);
        mNiuBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.niu3);
        mNiuBitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.niu4);
        mNiuBitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.niu3);
        mNiuBitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.niu2);
        typedArray.recycle();
        mTimer = new Timer();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) BitmapFactory.decodeResource(getResources(), mDymcResId).getHeight(), Math.max((int) mReachedBarHeight, (int) mUnreachedBarHeight));
    }

    /**
     * 根据测量规格计算实际的宽和高，注意宽高与建议宽高有区别，分别计算
     *
     * @param measureSpec 测量（包含测量模式与值）
     * @param isWidth     是否宽度
     * @return
     */
    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mReachedRectF.left = getPaddingLeft();
        mReachedRectF.top = getHeight() - mReachedBarHeight;
        mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + getPaddingLeft();
        mReachedRectF.bottom = getHeight();

        LinearGradient linearGradient = new LinearGradient(0, 0, mReachedRectF.right, 0, mColors, null, Shader.TileMode.CLAMP);
        mReachedBarPaint.setShader(linearGradient);
        mUnreachedRectF.left = getPaddingLeft();
        mUnreachedRectF.right = getWidth() - getPaddingRight();
        mUnreachedRectF.top = mReachedRectF.top;
        mUnreachedRectF.bottom = mReachedRectF.bottom;
        canvas.drawRoundRect(mUnreachedRectF, 50, 50, mUnreachedBarPaint);
        if (mDrawReachedBar) {
            canvas.drawRoundRect(mReachedRectF, 50, 50, mReachedBarPaint);
        }

        if (mDrawDymcResource && mDymcResId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mDymcResId);
            if (currIdx<mNiuBitmaps.length){
                canvas.drawBitmap(mNiuBitmaps[currIdx], mReachedRectF.right - bitmap.getWidth() * 0.7f, mReachedRectF.top - bitmap.getHeight() * 0.8f, null);
            }
        }

    }

    private int currIdx = 0;

    public void setProgress(int progress) {
        if (progress <= getMax() && progress >= 0) {
            this.progress = progress;
            invalidate();
        }
    }

    public void setInitProgress(int initProgress) {
        this.initProgress = initProgress;
        startRunBarBrother();
    }

    public int getInitProgress() {
        return initProgress;
    }

    public void displayNiuAnim() {
        if(mTimer!=null){
            mTimer.cancel();
            mTimer=new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currIdx++;
                if (currIdx > mNiuBitmaps.length - 1)
                    currIdx = 0;
                postInvalidate();
            }
        }, 0, runRate);
    }

    /**
     * 开始奔跑吧兄弟
     */
    private void startRunBarBrother() {
        ObjectAnimator anim = ObjectAnimator.ofInt(this, "progress", 0, getInitProgress());
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(2000);
        anim.setStartDelay(800);
        anim.start();
    }

    public int getProgress() {
        return progress;
    }

    public void setMax(int maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public void cancelTimer() {
        if (mTimer != null)
            mTimer.cancel();
    }

    public int getMax() {
        return mMaxProgress;
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public interface ProgressBarListener {
        void onProgress(int curr, int max);
    }

    private ProgressBarListener mListener;

    public void setListener(ProgressBarListener listener) {
        mListener = listener;
    }
}

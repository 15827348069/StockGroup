package com.zbmf.worklibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.zbmf.worklibrary.R;
import com.zbmf.worklibrary.util.Logx;

/**
 * Created by xuhao on 2017/12/22.
 */

public class MatchProgressGradeView extends View {
    private Paint mTextPaint;
    private Paint mLine;
    private Paint mReachedBarPaint;
    private Paint mUnreachedBarPaint;

    private final int default_reached_color = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(204, 204, 204);
    private final float default_reached_bar_height = dp2px(8f);
    private final int default_max_progress = 100;
    private final int default_progress = 100;
    private final int default_init_progress = 0;
    private float progressHeight;
    private final int default_text_color = default_unreached_color;
    private int mMaxProgress = 1100;
    private int progress = 0;
    private int mReachedBarColor, mReachedStartColor, mReachedEndColor;//默认bar的颜色、开始与结束颜色
    private int mUnreachedBarColor;

    private float textSize;
    private int text_color;
    private float text_two_textSize;
    private float default_text_size=dp2px(13);
    private double stop_loss;
    private int[] mColors = new int[2];
    private int startProgress,stopLossProgress,yanqiLossProgress,endProgress;
    public MatchProgressGradeView(Context context) {
        super(context);
        initPoint(context,null,0);
    }

    public MatchProgressGradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPoint(context,attrs,0);
    }

    public MatchProgressGradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPoint(context,attrs,defStyleAttr);
    }
    private void initPoint(Context context, AttributeSet attrs,int defStyleAttr){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MatchProgress, defStyleAttr, 0);
        mReachedBarColor = typedArray.getColor(R.styleable.MatchProgress_cus_progress_reached_color, default_reached_color);
        mReachedStartColor = typedArray.getColor(R.styleable.MatchProgress_cus_progress_reached_start_color, default_reached_color);
        mReachedEndColor = typedArray.getColor(R.styleable.MatchProgress_cus_progress_reached_end_color, default_reached_color);
        mUnreachedBarColor = typedArray.getColor(R.styleable.MatchProgress_cus_progress_unreached_color, default_unreached_color);
        mMaxProgress = typedArray.getInt(R.styleable.MatchProgress_cus_max_progress, default_max_progress);
        progress = typedArray.getInt(R.styleable.MatchProgress_cus_progress, default_progress);
        text_color = typedArray.getColor(R.styleable.MatchProgress_cus_text_color, default_text_color);
        textSize = typedArray.getDimension(R.styleable.MatchProgress_cus_text_size, default_text_size);
        text_two_textSize = typedArray.getDimension(R.styleable.MatchProgress_cus_text_size, default_text_size);
        progressHeight=typedArray.getDimension(R.styleable.MatchProgress_cus_progress_height,default_reached_bar_height);

        stopLossProgress=typedArray.getInt(R.styleable.MatchProgress_cus_loss_text,default_init_progress);
        startProgress=typedArray.getInt(R.styleable.MatchProgress_cus_start_text,default_init_progress);
        endProgress=typedArray.getInt(R.styleable.MatchProgress_cus_end_progress,default_init_progress);

        mColors[0] = mReachedStartColor;
        mColors[1] = mReachedEndColor;
        initPoint();
    }
    public void setProgress(int initProgress) {
        this.progress = initProgress;
        postInvalidate();
    }

    private void initPoint() {
        mTextPaint = new TextPaint();
        mTextPaint.setColor(text_color);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setAntiAlias(true);

        mLine=new Paint();
        mLine.setColor(Color.WHITE);
        mLine.setStyle(Paint.Style.FILL);
        mLine.setStrokeWidth(2f);

        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setColor(mReachedBarColor);

        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setColor(mUnreachedBarColor);
    }

    public int getStartProgress() {
        return startProgress;
    }

    public void setStartProgress(int startProgress) {
        this.startProgress = startProgress;
    }


    public int getStopLossProgress() {
        return stopLossProgress;
    }
    public void setYanqiLossProgress(int yanqi_loss) {
        this.yanqiLossProgress=yanqi_loss;
    }

    public void setStopLossProgress(int stopLossProgress) {
        this.stopLossProgress = stopLossProgress;
    }

    public int getEndProgress() {
        return endProgress;
    }

    public void setEndProgress(int endProgress) {
        this.endProgress = endProgress;
    }
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float bottom=getHeight()/2+progressHeight/2;
        float top=getHeight()/2-progressHeight/2;
        Logx.e("top()="+top);
        Logx.e("bottom()="+bottom);
         RectF mReachedRectF = new RectF( getPaddingLeft(),
                 top,
                (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + getPaddingLeft()
                ,bottom);
        RectF mUnreachedRectF = new RectF(getPaddingLeft(),
                 mReachedRectF.top, getWidth() - getPaddingRight(), mReachedRectF.bottom);

        LinearGradient linearGradient = new LinearGradient(0, 0, mReachedRectF.right,0, mColors, null, Shader.TileMode.CLAMP);
        mReachedBarPaint.setShader(linearGradient);
        mUnreachedRectF.left = getPaddingLeft();
        mUnreachedRectF.right = getWidth() - getPaddingRight();
        mUnreachedRectF.top = mReachedRectF.top;
        mUnreachedRectF.bottom = mReachedRectF.bottom;

        canvas.drawRoundRect(mUnreachedRectF, 50, 50, mUnreachedBarPaint);
        canvas.drawRoundRect(mReachedRectF, 50, 50, mReachedBarPaint);
        double unlength=endProgress-startProgress;
        double reachLength=stopLossProgress-startProgress;
        stop_loss=reachLength/unlength;
        canvas.drawLine((mUnreachedRectF.right /3),mUnreachedRectF.top,(mUnreachedRectF.right /3),mReachedRectF.bottom,mLine);
        canvas.drawLine((mUnreachedRectF.right *2/3),mUnreachedRectF.top,(mUnreachedRectF.right *2/3),mReachedRectF.bottom,mLine);
        String title="降级";
        String title2="升级";
        canvas.drawText(title,
                (mUnreachedRectF.right /3-mTextPaint.measureText(title)/2.0f),
                mUnreachedRectF.top-10, mTextPaint);
        canvas.drawText(title2,
                (mUnreachedRectF.right *2/3-mTextPaint.measureText(title2)/ 2.0f),
                mUnreachedRectF.top-10,
                mTextPaint);
    }
    public int getMax() {
        return mMaxProgress;
    }

    public int getProgress() {
        return progress>100?getMax():progress;
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }
}

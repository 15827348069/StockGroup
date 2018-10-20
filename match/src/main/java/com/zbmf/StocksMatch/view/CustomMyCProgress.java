package com.zbmf.StocksMatch.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.zbmf.StocksMatch.R;

import java.text.DecimalFormat;

/**
 * 圆的进度转一圈而已
 * @author lu
 */

public class CustomMyCProgress extends View {

    private Paint mFinishedPaint;
    private Paint munfinishedPaint;
    private Paint mInnerCirclePaint;
    private Paint mTextPaint;
    private Paint mProgressTextPaint;

    private RectF finishedOuterRectF = new RectF();
    private RectF unfinishedOuterRectF = new RectF();


    private static final int[] SECTION_COLORS ={0xFFff754b,0xFFea3535};


    private float textSize;
    private int text_color;
    private int progress_color;
    private float progress_textSize;

    private float progress = 0;
    private int mMax;
    private int mFinishedStrokeColor;
    private int mUnfinishedStrokeColor;
    private int startingDegree;
    private float mFinishedStrokeWidth;
    private float mUnfinishedStrokeWidthWidth;
    private int innerBackgroundColor;
    private String type="progress";
    private StateListener mListener;
    public void setStateListener(StateListener listener){
        mListener = listener;
    }
    private String defaultTextStr = "";

    private float default_stroke_width;
    private final int default_finished_color = Color.rgb(255, 82, 82);
    private final int default_unfinished_color = Color.rgb(164, 170, 179);
    private final int default_text_color = default_unfinished_color;
    private final int default_inner_background_color = Color.TRANSPARENT;
    private final int default_max = 100;
    private final int default_startingDegree = 270;
    private float default_text_size;
    private int min_size;
    private boolean isLoading = true;
    public CustomMyCProgress(Context context) {
        this(context, null);
    }

    public CustomMyCProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMyCProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = sp2px(getResources(), 18);
        min_size = (int) dp2px(getResources(), 100);
        default_stroke_width = dp2px(getResources(), 10);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomMyCircleProgress, defStyleAttr, 0);
        initAttributes(typedArray);
        typedArray.recycle();

        initPaints();
    }

    private void initAttributes(TypedArray typedArray) {
        mFinishedStrokeColor = typedArray.getColor(R.styleable.CustomMyCircleProgress_finished_color1, default_finished_color);
        mUnfinishedStrokeColor = typedArray.getColor(R.styleable.CustomMyCircleProgress_unfinished_color1, default_unfinished_color);
        text_color = typedArray.getColor(R.styleable.CustomMyCircleProgress_text_color1, default_text_color);
        textSize = typedArray.getDimension(R.styleable.CustomMyCircleProgress_text_size1, default_text_size);
        progress_color = typedArray.getColor(R.styleable.CustomMyCircleProgress_progress_text_color1, default_text_color);
        progress_textSize = typedArray.getDimension(R.styleable.CustomMyCircleProgress_progress_text_size1, default_text_size);
        setMax(typedArray.getInt(R.styleable.CustomMyCircleProgress_progress_max1, default_max));
        setProgress(typedArray.getFloat(R.styleable.CustomMyCircleProgress_custom_progress1, 0));
        mFinishedStrokeWidth = typedArray.getDimension(R.styleable.CustomMyCircleProgress_finished_stroke_width1, default_stroke_width);
        mUnfinishedStrokeWidthWidth = typedArray.getDimension(R.styleable.CustomMyCircleProgress_unfinished_stroke_width1, default_stroke_width);
        startingDegree = typedArray.getInt(R.styleable.CustomMyCircleProgress_circle_starting_degree1, default_startingDegree);
        innerBackgroundColor = typedArray.getColor(R.styleable.CustomMyCircleProgress_background_color1, default_inner_background_color);
        type=typedArray.getString(R.styleable.CustomMyCircleProgress_type1);
        defaultTextStr=typedArray.getString(R.styleable.CustomMyCircleProgress_text1);
    }

    private void initPaints() {
        mTextPaint = new TextPaint();
        mTextPaint.setColor(text_color);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setAntiAlias(true);

        mProgressTextPaint=new TextPaint();
        mProgressTextPaint.setColor(progress_color);
        mProgressTextPaint.setTextSize(progress_textSize);
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        munfinishedPaint = new Paint();
        munfinishedPaint.setColor(mUnfinishedStrokeColor);
        munfinishedPaint.setStyle(Paint.Style.STROKE);
        munfinishedPaint.setAntiAlias(true);
        munfinishedPaint.setStrokeWidth(mUnfinishedStrokeWidthWidth);

        mFinishedPaint = new Paint();
        mFinishedPaint.setColor(mFinishedStrokeColor);
        mFinishedPaint.setStyle(Paint.Style.STROKE);
        mFinishedPaint.setAntiAlias(true);
        mFinishedPaint.setStrokeWidth(mFinishedStrokeWidth);

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setColor(innerBackgroundColor);
        mInnerCirclePaint.setAntiAlias(true);
    }

    public void setMax(int max) {
        if (max > 0) {
            this.mMax = max;
            invalidate();
        }
    }
    public void setdefaultTextStr(String defaultTextStr){
        this.defaultTextStr=defaultTextStr;
    }
    public void setProgress(float progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        if(progress >= 100){
            isLoading = false;
            if(mListener!=null){
                mListener.loadFinished();
            }
        }
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public int getStartingDegree() {
        return startingDegree;
    }

    private float getProgressAngle() {
        return getProgress() / (float) mMax * 360f;
    }

    public int getMax() {
        return mMax;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;//宽高一样的,画圆
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMeasureMode == MeasureSpec.EXACTLY) {
            width = widthMeasureSize;
        } else {
            width = min_size;
            if (widthMeasureMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthMeasureSize);
            }
        }

        if (heightMeasureMode == MeasureSpec.EXACTLY) {
            height = heightMeasureSize;
        } else {
            height = min_size;
            if (heightMeasureMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightMeasureSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float delta = Math.max(mFinishedStrokeWidth, mUnfinishedStrokeWidthWidth);
        finishedOuterRectF.set(delta, delta, getWidth() - delta, getHeight() - delta);
        unfinishedOuterRectF.set(delta, delta, getWidth() - delta, getHeight() - delta);

        LinearGradient shader = new LinearGradient(delta,delta, getWidth() - delta,
                getHeight() - delta, SECTION_COLORS, null, Shader.TileMode.MIRROR);
        mFinishedPaint.setShader(shader);
        mFinishedPaint.setStrokeCap(Paint.Cap.ROUND);
        float innerCircleRadius = (getWidth() - Math.min(mFinishedStrokeWidth, mUnfinishedStrokeWidthWidth) + Math.abs(mFinishedStrokeWidth - mUnfinishedStrokeWidthWidth)) / 2f;
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, mInnerCirclePaint);
        canvas.drawArc(unfinishedOuterRectF, getStartingDegree() + getProgressAngle(), 360 - getProgressAngle(), false, munfinishedPaint);
        canvas.drawArc(finishedOuterRectF, getStartingDegree(), getProgressAngle(), false, mFinishedPaint);

        float textHeight = mTextPaint.descent() + mTextPaint.ascent();
        float progressHeight=mProgressTextPaint.descent()+mProgressTextPaint.ascent();
        if(type.equals("progress")){
            if (isLoading()){
                canvas.drawText(defaultTextStr, (getWidth() - mTextPaint.measureText(defaultTextStr)) / 2.0f, (getWidth() - textHeight) / 2.0f, mTextPaint);
            }else{
                canvas.drawText("down", (getWidth() - mTextPaint.measureText(defaultTextStr)) / 2.0f, (getWidth() - textHeight) / 2.0f, mTextPaint);
            }
        }else{
            canvas.drawText(defaultTextStr, (getWidth() - mTextPaint.measureText(defaultTextStr)) / 2.0f, (getHeight() - textHeight) /2.7f, mTextPaint);
            DecimalFormat fnum  =   new  DecimalFormat("##0.00");
            String progress_str="+"+fnum.format(progress/getMax()*100)+"%";
            canvas.drawText(progress_str,(getWidth() - mProgressTextPaint.measureText(progress_str))/2.0f, (getHeight() - progressHeight) / 1.8f, mProgressTextPaint);
        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }


    @Override
    public void invalidate() {
        initPaints();
        super.invalidate();
    }


    private float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public interface StateListener{
        void loadFinished();
    }
}

package com.zbmf.StocksMatch.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.zbmf.StocksMatch.R;

import java.text.DecimalFormat;

/**
 * Created by xuhao
 * on 2017/11/28.
 */

public class CustomCircleProgress extends View {
    private Paint mTextPaint;
    private Paint mTextPaint2;
    private Paint mProgressTextPaint;
    Paint mFinishedPaint;
    Paint munfinishedPaint;
    private RectF finishedOuterRectF = new RectF();
    private RectF unfinishedOuterRectF = new RectF();
    private Paint mInnerCirclePaint;
    private float progress = 0;
    private int mMax;
    private int mFinishedStrokeColor;
    private int mUnfinishedStrokeColor;
    private int mFinishedDefaultStrokeColor;
    private int startingDegree;
    private float mFinishedStrokeWidth;
    private float mUnfinishedStrokeWidthWidth;
    private int innerBackgroundColor;
    private float default_stroke_width;
    private final int default_finished_default_color = Color.rgb(79, 177, 15);
    private final int default_finished_color = Color.rgb(255, 82, 82);
    private final int default_unfinished_color = Color.rgb(164, 170, 179);
    private final int default_inner_background_color = Color.TRANSPARENT;
    private final int default_max = 100;
    private final int default_startingDegree = 270;
    private int min_size;
    private static final int[] SECTION_COLORS = {0xFFff754b, 0xFFea3535};
    private float textSize, textSize2;
    private int text_color, text_color2;
    private int progress_color;
    private float progress_textSize;
    private int type = -1;
    private StateListener mListener;

    public void setStateListener(StateListener listener) {
        mListener = listener;
    }

    private String defaultTextStr = "";
    private String tagTextStr = "";
    private final int default_text_color = default_unfinished_color;
    private float default_text_size;
    private boolean isLoading = true;

    public CustomCircleProgress(Context context) {
        super(context);
    }

    public CustomCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = dp2px(getResources(), 15);
        default_stroke_width = dp2px(getResources(), 10);
        min_size = (int) dp2px(getResources(), 100);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCircleProgress, defStyleAttr, 0);
        initAttributes(typedArray);
        typedArray.recycle();
        initPaints();
    }

    private void initAttributes(TypedArray typedArray) {
        mFinishedStrokeColor = typedArray.getColor(R.styleable.CustomCircleProgress_circle_finished_color, default_finished_color);
        mUnfinishedStrokeColor = typedArray.getColor(R.styleable.CustomCircleProgress_circle_unfinished_color, default_unfinished_color);
        mFinishedDefaultStrokeColor=typedArray.getColor(R.styleable.CustomCircleProgress_circle_finished_default_color, default_finished_default_color);

        setMax(typedArray.getInt(R.styleable.CustomCircleProgress_circle_progress_max, default_max));
        setProgress(typedArray.getFloat(R.styleable.CustomCircleProgress_circle_custom_progress, 0));
        mFinishedStrokeWidth = typedArray.getDimension(R.styleable.CustomCircleProgress_circle_finished_stroke_width, default_stroke_width);
        mUnfinishedStrokeWidthWidth = typedArray.getDimension(R.styleable.CustomCircleProgress_circle_unfinished_stroke_width, default_stroke_width);
        startingDegree = typedArray.getInt(R.styleable.CustomCircleProgress_circle_starting_degree, default_startingDegree);
        innerBackgroundColor = typedArray.getColor(R.styleable.CustomCircleProgress_circle_background_color, default_inner_background_color);

        text_color = typedArray.getColor(R.styleable.CustomCircleProgress_text_color, default_text_color);
        textSize = typedArray.getDimension(R.styleable.CustomCircleProgress_text_size, default_text_size);

        text_color2 = typedArray.getColor(R.styleable.CustomCircleProgress_text_title_color, default_text_color);
        textSize2 = typedArray.getDimension(R.styleable.CustomCircleProgress_text_title_size, default_text_size);

        progress_color = typedArray.getColor(R.styleable.CustomCircleProgress_progress_text_color, default_text_color);
        progress_textSize = typedArray.getDimension(R.styleable.CustomCircleProgress_progress_text_size, default_text_size);

        setMax(typedArray.getInt(R.styleable.CustomCircleProgress_circle_progress_max, default_max));
        setProgress(typedArray.getFloat(R.styleable.CustomCircleProgress_circle_custom_progress, 0));

        startingDegree = typedArray.getInt(R.styleable.CustomCircleProgress_circle_starting_degree, default_startingDegree);
        innerBackgroundColor = typedArray.getColor(R.styleable.CustomCircleProgress_background_color, default_inner_background_color);
        type = typedArray.getInt(R.styleable.CustomCircleProgress_type, -1);
        defaultTextStr = typedArray.getString(R.styleable.CustomCircleProgress_text);
        tagTextStr = typedArray.getString(R.styleable.CustomCircleProgress_title);


    }
    public void setContentText(String contentText){
        defaultTextStr=contentText;
    }

    public void runAnimat(float value){
        setProgress(value);
        ObjectAnimator anim = ObjectAnimator.ofFloat(this,"progress", 0f,value);
        anim.setDuration(800);
        anim.start();
    }

    private void initPaints() {
        mTextPaint = new TextPaint();
        mTextPaint.setColor(text_color);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setAntiAlias(true);

        mTextPaint2 = new TextPaint();
        mTextPaint2.setColor(text_color2);
        mTextPaint2.setTextSize(textSize2);
        mTextPaint2.setAntiAlias(true);

        mProgressTextPaint = new TextPaint();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;
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
//        LinearGradient shader = new LinearGradient(delta,delta, getWidth() - delta,getHeight() - delta, SECTION_COLORS, null, Shader.TileMode.MIRROR);
        float innerCircleRadius = (getWidth() - Math.min(mFinishedStrokeWidth, mUnfinishedStrokeWidthWidth) + Math.abs(mFinishedStrokeWidth - mUnfinishedStrokeWidthWidth)) / 2f;
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, mInnerCirclePaint);
        canvas.drawArc(unfinishedOuterRectF, getStartingDegree() + getProgressAngle(), 360 - getProgressAngle(), false, munfinishedPaint);
//        mFinishedPaint.setShader(shader);
        mFinishedPaint.setColor(progress>0?mFinishedStrokeColor:mFinishedDefaultStrokeColor);
        mFinishedPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, mInnerCirclePaint);
        canvas.drawArc(finishedOuterRectF, getStartingDegree(), getProgressAngle(), false, mFinishedPaint);

        DecimalFormat fnum = new DecimalFormat("##0.00");
        String progress_str = progress>0?"+" + fnum.format(progress / getMax() * 100) + "%":fnum.format(progress / getMax() * 100) + "%";

        float textHeight = mTextPaint.descent() + mTextPaint.ascent();
        float tagHeight = mTextPaint2.descent() + mTextPaint2.ascent();
        float progressHeight = mProgressTextPaint.descent() + mProgressTextPaint.ascent();
        switch (type) {
            case -1:
                if (defaultTextStr != null) {
                    if (isLoading()) {
                        canvas.drawText(defaultTextStr, (getWidth() - mTextPaint.measureText(defaultTextStr)) / 2.0f, (getWidth() - textHeight) / 2.0f, mTextPaint);
                    } else {
                        canvas.drawText(defaultTextStr, (getWidth() - mTextPaint.measureText(defaultTextStr)) / 2.0f, (getWidth() - textHeight) / 2.0f, mTextPaint);
                    }
                }
                break;
            case 0:
                if (defaultTextStr != null) {
                    canvas.drawText(defaultTextStr, (getWidth() - mTextPaint.measureText(defaultTextStr)) / 2.0f,
                            (getHeight() - textHeight) / 2.5f, mTextPaint);
                }
                if(progress_str!=null){
                    canvas.drawText(progress_str, (getWidth() - mProgressTextPaint.measureText(progress_str)) / 2.0f, (getHeight() - progressHeight) / 1.8f, mProgressTextPaint);
                }
                break;
            case 1:
                if(tagTextStr!=null){
                    canvas.drawText(tagTextStr, (getWidth() - mTextPaint2.measureText(tagTextStr)) / 2.0f, (getHeight() - tagHeight) / 3.0f, mTextPaint2);
                }
                if(progress_str!=null){
                    canvas.drawText(progress_str, (getWidth() - mProgressTextPaint.measureText(progress_str)) / 2.0f, (getHeight() - progressHeight) / 1.8f, mProgressTextPaint);
                }
                break;
            case 2:
                if(tagTextStr!=null){
                    canvas.drawText(tagTextStr,
                            (getWidth() - mTextPaint2.measureText(tagTextStr)) / 2.0f,
                            (getHeight() - tagHeight) / 3.0f, mTextPaint2);
                }
                if(defaultTextStr!=null){
                    canvas.drawText(defaultTextStr,
                            (getWidth() - mTextPaint.measureText(defaultTextStr)) / 2.0f,
                            (getHeight() - textHeight) / 2.0f, mTextPaint);
                }
                if(progress_str!=null){
                    canvas.drawText(progress_str,
                            (getWidth() - mProgressTextPaint.measureText(progress_str)) / 2.0f,
                            (getHeight() - progressHeight) / 1.5f, mProgressTextPaint);
                }
                break;
        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public int getStartingDegree() {
        return startingDegree;
    }

    private float getProgressAngle() {
        return Math.abs(getProgress())/ (float) mMax * 360f;
    }

    public float getProgress() {
        return progress;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.mMax = max;
            invalidate();
        }
    }

    public int getMax() {
        return mMax;
    }

    public void setProgress(float progress) {
        this.progress = progress;
//        if (this.progress > getMax()) {
//            this.progress = getMax();
//        }
        invalidate();
    }

    private float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }


    public interface StateListener {
        void loadFinished();
    }

}

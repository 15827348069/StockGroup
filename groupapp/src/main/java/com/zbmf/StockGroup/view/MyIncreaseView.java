package com.zbmf.StockGroup.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * @author lu
 */

@SuppressLint("AppCompatCustomView")
public class MyIncreaseView extends TextView {

    private int max;

    public MyIncreaseView(Context context) {
        this(context, null);
    }

    public MyIncreaseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyIncreaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    /**
     * 增长吧兄弟
     */
    public void increaseBarBrother() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, getMax());
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());//使用线性的
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setText(animation.getAnimatedValue().toString()+"人");
            }
        });
        valueAnimator.setStartDelay(800);
        valueAnimator.start();
    }

}

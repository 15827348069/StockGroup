package com.zbmf.groupro.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by xuhao on 2017/1/20.
 */

public class SwipeToFinishView {
    private MySwipeView mySwipeView;
    private boolean close_activity;
    public SwipeToFinishView(Activity activity) {
        mySwipeView = new MySwipeView(activity);
        mySwipeView.setActivity(activity);
    }
    public void setclose_activity(boolean close_activity){
        this.close_activity=close_activity;
    }
    /**
     * 核心View
     *
     * @author
     *
     */
    private class MySwipeView extends FrameLayout {

        private Activity activity;// 绑定的Activity
        private ViewGroup decorView;
        private View contentView;// activity的ContentView
        private float intercept_X = 0;// onInterceptTouchEvent刚触摸时的X坐标
        private float intercept_Y = 0;// onInterceptTouchEvent手指刚触摸时的y坐标
        private int touchSlop = 0;// 产生滑动的最小值

        public MySwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            touchSlop = ViewConfiguration.get(getContext())
                    .getScaledTouchSlop();
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            close_activity=true;
        }

        public MySwipeView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MySwipeView(Context context) {
            this(context, null);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if(close_activity){
                return shouldInterceptEvent(ev);
            }else{
                return close_activity;
            }

        };

        @Override
        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouchEvent(MotionEvent event) {
            if(close_activity){
                processTouchEvent(event);
            }
            return true;
        };
        /**
         * 绑定Activity
         *
         * @param activity
         */
        public void setActivity(Activity activity) {
            this.activity = activity;
            initCoverView();
        }

        /**
         * 将contentView从DecorView中移除，并添加到CoverView中，最后再将CoverView添加到DecorView中
         */
        private void initCoverView() {
            decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.setBackgroundColor(Color.parseColor("#33000000"));
            contentView = (ViewGroup) decorView
                    .findViewById(android.R.id.content);
            ViewGroup contentParent = (ViewGroup) contentView.getParent();
            contentParent.removeView(contentView);
            addView(contentView);
            contentView.setBackgroundColor(Color.WHITE);
            contentParent.addView(this);
        }

        /**
         * 判断是否应该拦截事件
         *
         * @param event
         *            事件对象
         * @return true表示拦截，false反之
         */
        private boolean shouldInterceptEvent(MotionEvent event) {
            boolean shouldInterceptEvent = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    intercept_X = event.getX();
                    intercept_Y = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float offsetY = Math.abs(event.getY() - intercept_Y);
                    float offsetX = Math.abs(event.getX() - intercept_X);
                    if (offsetY >= touchSlop * 3 || offsetY > offsetX) {
                        shouldInterceptEvent = false;
                    } else if (event.getX() - intercept_X >= touchSlop * 3) {
                        shouldInterceptEvent = true;
                    } else {
                        shouldInterceptEvent = false;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    shouldInterceptEvent = false;
                    break;
                default:
                    break;
            }
            return shouldInterceptEvent;
        }

        /**
         * 对onTouchEvent事件进行处理
         *
         * @param event
         *            事件对象
         */
        private void processTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    float offsetX = event.getX() - intercept_X;
                    if (offsetX > 0) {
                        contentView.setTranslationX(offsetX);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (contentView.getTranslationX() >= contentView.getMeasuredWidth() / 3) {
                        collapse();
                    } else {
                        open();
                    }
                    break;

                default:
                    break;
            }
        }

        /**
         * 展开Activity
         */
        private void open() {
            contentView.clearAnimation();
            ObjectAnimator anim = ObjectAnimator.ofFloat(contentView,
                    View.TRANSLATION_X, 0);
            anim.start();

        }

        /**
         * 折叠Activity(finish掉)
         */
        private void collapse() {
            decorView.setBackgroundColor(Color.parseColor("#00000000"));
            contentView.clearAnimation();
            ObjectAnimator anim = ObjectAnimator.ofFloat(contentView,
                    View.TRANSLATION_X, contentView.getMeasuredWidth());
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    activity.finish();
                }
            });
            anim.start();

        }

    }
}
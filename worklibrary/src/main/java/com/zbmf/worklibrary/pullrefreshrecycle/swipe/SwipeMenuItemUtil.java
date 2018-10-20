package com.zbmf.worklibrary.pullrefreshrecycle.swipe;

import android.content.Context;

/**
 * Created by xiaote on 2017/4/18.
 */

public class SwipeMenuItemUtil {
    public static SwipeMenuItem createSwipeMenuItem(Context context, int bgResId, int imgResId, String text,
                                                    int width, int height) {
        SwipeMenuItem swipeMenuItem = new SwipeMenuItem(context).setBackgroundDrawable(bgResId).setImage(imgResId)
                .setText(text).setWidth(width).setHeight(height);
        return swipeMenuItem;
    }

    public static SwipeMenuItem createSwipeMenuItem(Context context, int bgResId, int imgResId, String text,
                                                    int width, int height, int weight) {
        SwipeMenuItem swipeMenuItem = new SwipeMenuItem(context).setBackgroundDrawable(bgResId).setImage(imgResId)
                .setText(text).setWidth(width).setHeight(height).setWeight(weight);
        return swipeMenuItem;
    }

    public static SwipeMenuItem createSwipeMenuItem(Context context, int bgResId, int imgResId, int txtResId,
                                                    int width, int height) {
        SwipeMenuItem swipeMenuItem = new SwipeMenuItem(context).setBackgroundDrawable(bgResId).setImage(imgResId)
                .setText(txtResId).setWidth(width).setHeight(height);
        return swipeMenuItem;
    }

    public static SwipeMenuItem createSwipeMenuItem(Context context, int bgResId, int imgResId, int txtResId,
                                                    int width, int height, int weight) {
        SwipeMenuItem swipeMenuItem = new SwipeMenuItem(context).setBackgroundDrawable(bgResId).setImage(imgResId)
                .setText(txtResId).setWidth(width).setHeight(height).setWeight(weight);
        return swipeMenuItem;
    }
}

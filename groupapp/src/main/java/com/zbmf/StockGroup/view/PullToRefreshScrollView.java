/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.zbmf.StockGroup.view;



import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.zbmf.StockGroup.R;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {

    public PullToRefreshScrollView(Context context) {
        super(context);
    }
    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PullToRefreshScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

    }

    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        ScrollView scrollView;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            scrollView = new InternalScrollViewSDK9(context, attrs);
        } else {
            scrollView = new ScrollView(context,attrs);
        }
        scrollView.setId(R.id.scrollview);
//        if (VERSION.SDK_INT >= VERSION_CODES.M) {
//            scrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    if(onScrolls!=null){
//                        onScrolls.onScroll(scrollX,scrollY);
//                    }
//                    if (oldScrollY > scrollY && oldScrollY -scrollY > SCROLLLIMIT) {// 向下
//                        if (onScrolls != null)
//                            onScrolls.scrollDown();
//                    } else if (oldScrollY < scrollY && scrollY - oldScrollY > SCROLLLIMIT) {// 向上
//                        if (onScrolls != null)
//                            onScrolls.scrollTop();
//                    }
//                }
//            });
//        }


        return scrollView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }
    public void ScrollTop(){
        mRefreshableView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshableView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }
    @TargetApi(9)
    final class InternalScrollViewSDK9 extends ScrollView {

        public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshScrollView.this, deltaX, scrollX, deltaY, scrollY,
                    getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        public int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }
    /**
     * 最小的滑动距离
     */
    private static final int SCROLLLIMIT = 20;
    private onScrolls onScrolls;
    public void setOnScroll(onScrolls onScroll) {
        this.onScrolls = onScroll;
    }

    public interface onScrolls{
        void scrollTop();
        void scrollDown();
        void scrollBottom();
        void onScroll(int x,int y);
    }
}

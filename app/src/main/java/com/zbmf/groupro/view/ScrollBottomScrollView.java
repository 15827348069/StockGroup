package com.zbmf.groupro.view;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.zbmf.groupro.utils.DisplayUtil;


/**
 * Created by xuhao on 2016/7/21.
 */
public class ScrollBottomScrollView extends ScrollView {

    private ScrollBottomListener scrollBottomListener;
    private int index;
    public ScrollBottomScrollView(Context context) {
        super(context);
    }

    public ScrollBottomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollBottomScrollView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt){
        if(t + getHeight() + DisplayUtil.dip2px(getContext(),60)>=  computeVerticalScrollRange()){
            //ScrollView滑动到底部了
            index++;
            scrollBottomListener.scrollBottom();
        }else if(t==0){
            scrollBottomListener.scrollTop();
        }else{
            scrollBottomListener.scrollnoBottom();
        }
    }

    public void setScrollBottomListener(ScrollBottomListener scrollBottomListener){
        this.scrollBottomListener = scrollBottomListener;
    }

    public interface ScrollBottomListener{
        public void scrollBottom();
        public void scrollnoBottom();
        public void scrollTop();
    }

}
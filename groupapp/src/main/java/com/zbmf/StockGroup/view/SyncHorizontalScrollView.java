package com.zbmf.StockGroup.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.zbmf.StockGroup.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class SyncHorizontalScrollView extends HorizontalScrollView
{
    private List<View> mView;
    public SyncHorizontalScrollView(Context context)
    {  
        super(context);  
    }
    @SuppressLint("Instantiatable")
	public SyncHorizontalScrollView(Context context, AttributeSet attrs) 
    {  
        super(context, attrs);  
    }
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {    
        super.onScrollChanged(l, t, oldl, oldt);
        if(mView!=null&&mView.size()>0)
        {
            for(View view:mView){
                view.scrollTo(l, t);
            }
           
        }    
    }  
    public void setScrollView(View view)
    {
        if(mView==null){
            mView=new ArrayList<>();
        }
        mView.add(view);

    }

    public void scrollX(View view,int x){
        view.scrollTo(x,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(maxScrollX!=0){
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_MOVE:
                    int scrollX = getScrollX();
                    LogUtil.e("scrollX=="+scrollX);
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }
    private int maxScrollX;

    public void setMaxScrollX(int maxScrollX) {
        this.maxScrollX = maxScrollX;
    }
    private OnScrollX scrollXListenter;
    interface OnScrollX{
        void onScrollX(int x);
    }
}
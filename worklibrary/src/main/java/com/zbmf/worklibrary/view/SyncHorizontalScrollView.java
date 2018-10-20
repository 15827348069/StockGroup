package com.zbmf.worklibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;


public class SyncHorizontalScrollView extends HorizontalScrollView
{
    private List<View> mView=new ArrayList<>();
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
        if(mView!=null)
        {
            for (View view : mView) {
                view.scrollTo(l, t);
            }
        }
    }  
    public void setScrollView(View view)
    {
        if (view!=null){
            mView.add(view);
        }
    }
  
}  
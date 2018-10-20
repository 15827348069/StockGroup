package com.zbmf.StocksMatch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;

/**
 * Created by pq
 * on 2018/7/5.
 */

public class ExpandableTextView extends LinearLayout {
    private TextView mTextView;
    private ImageView mOpenBtn;
    private boolean isOpen = false;
    private int foldLines = 3; //大于3行的时候折叠
    private int lineCounts;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void initView() {

        lineCounts = mTextView.getLineCount();
        if(lineCounts <= foldLines){
            mOpenBtn.setVisibility(GONE);
        }
        if(isOpen && mTextView.getHeight() != lineCounts * mTextView.getLineHeight()){
            mTextView.setHeight(mTextView.getLineHeight() * mTextView.getLineCount());
        }else if(!isOpen && mTextView.getHeight() != foldLines * mTextView.getLineHeight()){
            mTextView.setHeight(mTextView.getLineHeight() * foldLines);
        }
        mOpenBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    //to close
                    mTextView.setHeight(mTextView.getLineHeight() * foldLines);
                    mOpenBtn.setImageResource(R.drawable.arrow_up);
                    isOpen = false;

                }else{
                    //to open
                    mTextView.setHeight(mTextView.getLineHeight() * mTextView.getLineCount());
                    mOpenBtn.setImageResource(R.drawable.arrow_down);
                    isOpen = true;

                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("zyr","onMeasure");
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("zyr","onFinishInflate");
        if(mTextView==null || mOpenBtn == null){
            mTextView = (TextView)getChildAt(0);
            mOpenBtn = (ImageView)getChildAt(1);
        }

    }
}
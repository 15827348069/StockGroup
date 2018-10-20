package com.zbmf.StocksMatch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 *
 * 可解决 与 ScrollView 冲突
 * Created by kubo on 15/12/29.
 */
public class CustomListView extends ListView{

    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}

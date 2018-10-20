package com.zbmf.StocksMatch.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zbmf.worklibrary.util.UIUtil;


/**
 * Created by xiaote on 2017/6/16.
 */

public class CardItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int size = UIUtil.dip2px(5);
        outRect.set(0, size, 0, size);
    }
}

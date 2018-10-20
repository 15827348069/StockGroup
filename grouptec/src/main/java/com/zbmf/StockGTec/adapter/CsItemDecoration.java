package com.zbmf.StockGTec.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by qnmb on 2017/7/6.
 * 最简易的处理方式，当然还有更复杂的，sb请自行Google吧
 */

public class CsItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public CsItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) % 3 == 0) {//第一个左边距取零
            outRect.left = 0;
        }
    }

}

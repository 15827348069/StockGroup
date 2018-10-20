package com.zbmf.worklibrary.pullrefreshrecycle.listener;

import android.view.View;

import com.zbmf.worklibrary.pullrefreshrecycle.holder.RecyclerHolder;

public interface OnItemClickListener {
    void onItemClick(RecyclerHolder holder, View view, int position);
}

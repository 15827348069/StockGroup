package com.zbmf.StocksMatch.listener;

import android.content.Context;
import android.view.View;

/**
 * Created by pq
 * on 2018/3/20.
 */

public interface MZViewHolder<T> {
    /**
     *  创建View
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data,T text);
}

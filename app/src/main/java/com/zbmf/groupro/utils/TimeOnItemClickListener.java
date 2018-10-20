package com.zbmf.groupro.utils;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by xuhao on 2017/3/23.
 */

public abstract class TimeOnItemClickListener implements AdapterView.OnItemClickListener{
    public static final int DELAY = 2000;  //连击事件间隔
    private long lastClickTime = 0; //记录最后一次时间

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > DELAY) {
            lastClickTime = currentTime;
            onNoDoubleClick(position);
        }
    }
    public abstract void onNoDoubleClick(int position);
}

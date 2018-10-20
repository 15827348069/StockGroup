package com.zbmf.StockGroup.activity;

import android.animation.ObjectAnimator;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.view.CustomMyCProgress;

/**
 * Created by xuhao on 2017/11/15.
 */

public class ProgressActivity extends BaseActivity {
    private CustomMyCProgress progress,progress2,progress3;
    @Override
    public int getLayoutResId() {
        return R.layout.item_progress;
    }

    @Override
    public void initView() {
        progress=getView(R.id.cc_progress);
        ObjectAnimator anim = ObjectAnimator.ofFloat(progress, "progress", 0f, 30f);
        anim.setDuration(2000);
        anim.start();
        progress2=getView(R.id.cc_progress_2);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(progress2, "progress", 0f, 50f);
        anim2.setDuration(2000);
        anim2.start();
        progress3=getView(R.id.cc_progress_3);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(progress3, "progress", 0f, 100f);
        anim3.setDuration(2000);
        anim3.start();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListener() {

    }
}

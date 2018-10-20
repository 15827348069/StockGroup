package com.zbmf.StockGroup.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.TopticBean;

/**
 * Created by xuhao on 2017/4/25.
 */

public class TopTicActivity extends BaseActivity {
    private TextView toptic_text;
    private TopticBean topticBean;
    @Override
    public int getLayoutResId() {
        return R.layout.toptic_layout;
    }

    @Override
    public void initView() {
        topticBean= (TopticBean) getIntent().getSerializableExtra("TopticBean");
        initTitle(topticBean.getSubject());
        toptic_text= (TextView) findViewById(R.id.toptic_text);
    }

    @Override
    public void initData() {
        toptic_text.setText(topticBean.getContent());
    }

    @Override
    public void addListener() {

    }
}

package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.constans.IntentKey;

/**
 * Created by xuhao on 2017/8/23.
 */

public class VideoTeacherDescActivity extends BaseActivity {
    private TextView descText;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_video_teacher_desc_layout;
    }

    @Override
    public void initView() {
        initTitle("简介");
        descText=getView(R.id.tv_teacher_desc);
    }

    @Override
    public void initData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String desc=bundle.getString(IntentKey.VDIEO_TEACHER_DESC,"");
            descText.setText(desc);
        }
    }

    @Override
    public void addListener() {

    }
}

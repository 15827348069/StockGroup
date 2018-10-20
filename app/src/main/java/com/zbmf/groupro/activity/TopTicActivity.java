package com.zbmf.groupro.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.TopticBean;

/**
 * Created by xuhao on 2017/4/25.
 */

public class TopTicActivity extends Activity {
    private TextView group_title_name,toptic_text;
    private TopticBean topticBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toptic_layout);
        topticBean= (TopticBean) getIntent().getSerializableExtra("TopticBean");
        init();
    }
    private void init(){
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        toptic_text= (TextView) findViewById(R.id.toptic_text);
        group_title_name.setText(topticBean.getSubject());
        toptic_text.setText(topticBean.getContent());
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

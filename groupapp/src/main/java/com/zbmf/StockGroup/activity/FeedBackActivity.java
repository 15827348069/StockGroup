package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.R;

/**
 * 意见反馈
 */
public class FeedBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}

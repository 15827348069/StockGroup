package com.zbmf.groupro.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplication(),GroupActivity.class));// 这个线程的作用3秒后就是进入到你的主界面
                LaunchActivity.this.finish();// 把当前的LaunchActivity结束掉
            }
        },3000);
    }
}

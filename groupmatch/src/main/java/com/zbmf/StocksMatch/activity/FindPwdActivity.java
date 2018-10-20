package com.zbmf.StocksMatch.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;

public class FindPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_phone,ed_code,ed_pwd;
    private TextView tv_title;
    private Button btn_submit,btn_getyzm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        setupView();
    }

    private void setupView() {
        ed_phone = (EditText)findViewById(R.id.ed_phone);
        ed_code = (EditText)findViewById(R.id.ed_code);
        ed_pwd = (EditText)findViewById(R.id.ed_pwd);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(R.string.registe);
        findViewById(R.id.iv_back).setOnClickListener(this);
        btn_submit= (Button) findViewById(R.id.btn_login);
        btn_submit.setOnClickListener(this);
        btn_submit.setText(getString(R.string.get_pwd));
        btn_getyzm = (Button)findViewById(R.id.btn_getyzm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_getyzm:

                break;
        }
    }
}

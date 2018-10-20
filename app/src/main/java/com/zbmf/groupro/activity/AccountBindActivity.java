package com.zbmf.groupro.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.User;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.ShowActivity;

import org.json.JSONObject;

public class AccountBindActivity extends AppCompatActivity {

    private LinearLayout ll_none, ll_account;
    private TextView tv_username;
    private TextView tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_bind);
        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebBase.isBind(new JSONHandler(true, this, "加载中....") {
            @Override
            public void onSuccess(JSONObject obj) {
                User user = JSONParse.isBind(obj);
                if ("1".equals(user.getIs_bind())) {
                    tv_username.setText(user.getUsername());
                    String phone = user.getPhone();
                    tv_phone.setText(phone.substring(0, 3) + "****" + phone.substring(7, 11));
                } else {
                    ll_none.setVisibility(View.VISIBLE);
                    ll_account.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_username = ((TextView) findViewById(R.id.tv_username));
        tv_phone = ((TextView) findViewById(R.id.tv_phone));

        tv_title.setText("账号和绑定设置");
        tv_title.setVisibility(View.VISIBLE);

        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_none = (LinearLayout) findViewById(R.id.ll_none);
        ll_account = (LinearLayout) findViewById(R.id.ll_account);
        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivity(AccountBindActivity.this, BindPhoneActivity.class);
            }
        });

        findViewById(R.id.rl_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountBindActivity.this, "暂不支持修改绑定手机号，请联系客服", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

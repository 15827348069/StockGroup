package com.zbmf.StockGTec.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;

import org.json.JSONObject;

public class LiveSettingActivity extends ExActivity implements View.OnClickListener {
    private EditText live_title, live_notice;
    private TextView tv_right, group_title_name;
    private SwitchCompat sc_set;
    private boolean mShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_setting);
        mShow = SettingDefaultsManager.getInstance().isGroupChatManager();
        init();
    }

    private void init() {
        live_title = (EditText) findViewById(R.id.live_title);
        live_notice = (EditText) findViewById(R.id.live_notice);
        group_title_name = (TextView) findViewById(R.id.group_title_name);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);
        tv_right.setText("发布");
        tv_right.setVisibility(View.VISIBLE);
        group_title_name.setText("直播设置");
        group_title_name.setVisibility(View.VISIBLE);
        sc_set = (SwitchCompat) findViewById(R.id.sc_set);
        ;
        findViewById(R.id.group_title_return).setOnClickListener(this);
        getNotice();
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager inManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 600);

        final SettingDefaultsManager sp = SettingDefaultsManager.getInstance();
        sc_set.setChecked(sp.isGroupManager());
        sc_set.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sp.setGroupManager(b);
            }
        });

        if (!mShow) {
            sc_set.setVisibility(View.GONE);
        }
    }

    private void getNotice() {
        WebBase.notice(new JSONHandler(false, LiveSettingActivity.this, "正在加载...") {
            @Override
            public void onSuccess(JSONObject obj) {
                String content = obj.optJSONObject("notice").optString("content");
                live_notice.setText(content);
                live_notice.setSelection(content.length());
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_title_return:
                finish();
                break;
            case R.id.tv_right:
                String content = live_notice.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    WebBase.setNotice(SettingDefaultsManager.getInstance().UserId(), content, new JSONHandler(true, LiveSettingActivity.this, "正在发布...") {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            Toast.makeText(LiveSettingActivity.this, "公告已发布", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(String err_msg) {

                        }
                    });
                }
                break;
        }
    }
}

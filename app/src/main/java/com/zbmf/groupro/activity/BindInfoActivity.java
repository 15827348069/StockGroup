package com.zbmf.groupro.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.zbmf.groupro.R;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.utils.EditCheckUtil;
import com.zbmf.groupro.utils.EditTextUtil;
import com.zbmf.groupro.utils.SettingDefaultsManager;

import org.json.JSONObject;

public class BindInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_phone, ed_yzm, ed_pwd, ed_truename,ed_idcard;
    private TextView tv_yzm;
    private RelativeLayout rl_pwd;
    private String turename;
    private RelativeLayout mRl_yzm,rl_idcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);

        setupView();

    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("购买");
        tv_title.setVisibility(View.VISIBLE);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_yzm = (EditText) findViewById(R.id.ed_yzm);
        ed_pwd = (EditText) findViewById(R.id.ed_pwd);
        ed_truename = (EditText) findViewById(R.id.ed_truename);
        ed_idcard = (EditText) findViewById(R.id.ed_idcard);
        tv_yzm = (TextView) findViewById(R.id.tv_yzm);
        rl_pwd = (RelativeLayout) findViewById(R.id.rl_pwd);
        rl_pwd.setVisibility(View.GONE);
        tv_yzm.setOnClickListener(this);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        Button button = (Button) findViewById(R.id.btn_bind);
        button.setOnClickListener(this);
        button.setText("提交");
        findViewById(R.id.rl_truename).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_text_tip1).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_text_tip2).setVisibility(View.VISIBLE);
        mRl_yzm = (RelativeLayout) findViewById(R.id.rl_yzm);
        rl_idcard = (RelativeLayout) findViewById(R.id.rl_idcard);
        rl_pwd.setVisibility(View.GONE);
        rl_idcard.setVisibility(View.VISIBLE);
        phone = SettingDefaultsManager.getInstance().getUserPhone();
        if (!TextUtils.isEmpty(phone)) {

            ed_phone.setText(phone);
            ed_phone.setEnabled(false);
            mRl_yzm.setVisibility(View.GONE);
        }

        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone = s.toString();
                if (phone.length() == 11 && !EditTextUtil.isMobileNO(phone)) {
                    Toast.makeText(getBaseContext(), "手机号格式错误", 0).show();
                }
            }
        });


    }

    private String phone = "";
    private TimeCount timecount;

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_yzm.setText("重新发送");
            tv_yzm.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tv_yzm.setClickable(false);
            tv_yzm.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_title_return:
                finish();
                break;
            case R.id.tv_yzm://验证码
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getBaseContext(), "请先输入手机号", 0).show();
                    return;
                }

                if (phone.length() != 11) {
                    Toast.makeText(getBaseContext(), "请输入正确的手机号", 0).show();
                    return;
                }

                WebBase.codeBind(phone, PushManager.getInstance().getClientid(getBaseContext()), new JSONHandler(true, BindInfoActivity.this, "加载中...") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        if (timecount == null) {
                            timecount = new TimeCount(60000, 1000);
                        }
                        timecount.start();
                        Toast.makeText(getBaseContext(), "验证码已发送！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.btn_bind:
                turename = ed_truename.getText().toString();
                if(TextUtils.isEmpty(turename)){
                    Toast.makeText(getBaseContext(), "请输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String idcard = ed_idcard.getText().toString().trim();
                if (!TextUtils.isEmpty(EditCheckUtil.IDCardValidate(idcard))) {
                    Toast.makeText(getBaseContext(), EditCheckUtil.IDCardValidate(idcard), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getBaseContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.length() != 11) {
                    Toast.makeText(getBaseContext(), "请输入11位手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mRl_yzm.getVisibility() == View.VISIBLE && TextUtils.isEmpty(ed_yzm.getText().toString())) {
                    Toast.makeText(getBaseContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mRl_yzm.getVisibility() == View.VISIBLE && ed_yzm.getText().toString().length() < 6) {
                    Toast.makeText(getBaseContext(), "请输入6位数验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                WebBase.bindName(ed_phone.getText().toString(), ed_yzm.getText().toString(), turename,idcard, PushManager.getInstance().getClientid(getBaseContext()), new JSONHandler(true, BindInfoActivity.this, "加载中...") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        Toast.makeText(getBaseContext(), "提交成功", 0).show();
                        SettingDefaultsManager.getInstance().setUserPhone(ed_phone.getText().toString());
                        SettingDefaultsManager.getInstance().setTrueName(turename);
                        SettingDefaultsManager.getInstance().setIdcard(idcard);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        Toast.makeText(getBaseContext(), err_msg, 0).show();
                    }
                });

                break;
        }
    }


}

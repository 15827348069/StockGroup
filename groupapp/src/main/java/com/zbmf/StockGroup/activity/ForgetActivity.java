package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

import org.json.JSONObject;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_pwd1, ed_phone1, ed_yzm;
    private TextView tv_yzm_tip, tv_phone_tip, tv_pwd_tip, tv_yzm;
    private String phone = "", log_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        setupView();
    }

    private void setupView() {

        tv_yzm_tip = (TextView) findViewById(R.id.tv_yzm_tip);
        tv_phone_tip = (TextView) findViewById(R.id.tv_phone_tip);
        tv_pwd_tip = (TextView) findViewById(R.id.tv_pwd_tip);
        tv_yzm = (TextView) findViewById(R.id.tv_yzm);
        tv_yzm.setOnClickListener(this);
        ed_phone1 = (EditText) findViewById(R.id.ed_phone1);
        ed_yzm = (EditText) findViewById(R.id.ed_yzm);
        ed_pwd1 = (EditText) findViewById(R.id.ed_pwd1);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        ed_phone1.addTextChangedListener(new TextWatcher() {
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
                    tv_phone_tip.setVisibility(View.VISIBLE);
                    tv_phone_tip.setText("手机号格式错误");
                } else
                    tv_phone_tip.setVisibility(View.INVISIBLE);

            }
        });

        ed_yzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tv_yzm_tip.setVisibility(View.INVISIBLE);
                }

                if (!TextUtils.isEmpty(phone) && phone.length() == 11 && s.toString().length() == 6) {
                    WebBase.verifyForget(SettingDefaultsManager.getInstance().PUSH_CILENT_ID(), ed_yzm.getText().toString(), phone, new JSONHandler() {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            log_id = obj.optString("log_id");
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        ed_pwd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tv_pwd_tip.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private TimeCount timecount;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yzm:
                if (TextUtils.isEmpty(phone)) {
                    tv_phone_tip.setVisibility(View.VISIBLE);
                    tv_phone_tip.setText("请先输入手机号");
                    return;
                }

                if (phone.length() != 11) {
                    tv_phone_tip.setVisibility(View.VISIBLE);
                    tv_phone_tip.setText("请输入正确的手机号");
                    return;
                }
                String push_cilent_id = SettingDefaultsManager.getInstance().PUSH_CILENT_ID();
                Log.i("===TAG","   圈子的   个推clientId  ："+push_cilent_id);

                WebBase.code_forget(phone, push_cilent_id, new JSONHandler(true, ForgetActivity.this, "获取验证码") {
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
            case R.id.btn_reg:
            case R.id.btn_submit:
                if (TextUtils.isEmpty(phone)) {
                    tv_phone_tip.setVisibility(View.VISIBLE);
                    tv_phone_tip.setText("手机号不能为空");
                    return;
                }

                if (phone.length() != 11) {
                    tv_phone_tip.setVisibility(View.VISIBLE);
                    tv_phone_tip.setText("请输入11位手机号");
                    return;
                }

                if (TextUtils.isEmpty(ed_yzm.getText().toString())) {
                    tv_yzm_tip.setVisibility(View.VISIBLE);
                    tv_yzm_tip.setText("验证码不能为空");
                    return;
                }

                if (ed_yzm.getText().toString().length() < 6) {
                    tv_yzm_tip.setVisibility(View.VISIBLE);
                    tv_yzm_tip.setText("请输入6位数验证码");
                    return;
                }

                String pwd = ed_pwd1.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    tv_pwd_tip.setText("密码不能为空");
                    tv_pwd_tip.setVisibility(View.VISIBLE);
                    return;
                }

                if (pwd.length() < 6) {
                    tv_pwd_tip.setText("请输入大于6位数密码");
                    tv_pwd_tip.setVisibility(View.VISIBLE);
                    return;
                }

                if (!EditTextUtil.checkNum(pwd)) {
                    tv_pwd_tip.setText("请输入6-18位数字和字母组合");
                    tv_pwd_tip.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(log_id)) {
                    Toast.makeText(getBaseContext(), "验证码校验失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                //重置忘记密码
                WebBase.resetForget(SettingDefaultsManager.getInstance().PUSH_CILENT_ID(), log_id, phone, pwd,
                        new JSONHandler(true, ForgetActivity.this, "正在重置密码...") {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                Toast.makeText(getBaseContext(), "密码重置成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(String err_msg) {
                                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }


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

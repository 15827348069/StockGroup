package com.zbmf.groupro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.User;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.EditTextUtil;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.utils.ThirdPartyUtil;
import com.zbmf.groupro.view.CustomProgressDialog;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_bottom, ll_reg, ll_login;//底部分享,注册布局,登录布局
    private TextView tv_login, tv_reg;//标签文字 颜色
    private TextView tv_yzm_tip, tv_phone_tip, tv_pwd_tip, tv_yzm,mianze_text_message;
    private View v_reg, v_login;//标签线 显示 隐藏
    private EditText ed_phone, ed_phone1, ed_pwd, ed_pwd1, ed_yzm;
    private String phone = "";
    private boolean ic_checked;
    private CheckedTextView tv_checked;
    private DBManager dbManager;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
        dbManager = new DBManager(LoginActivity.this);
        ThirdPartyUtil.getInstance().init(LoginActivity.this);
        ActivityUtil.getInstance().putActivity(ActivityUtil.LoginActivity, this);
    }

    private void setupView() {
        ic_checked = true;
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_login = (LinearLayout) findViewById(R.id.ll_login);
        ll_reg = (LinearLayout) findViewById(R.id.ll_reg);
        tv_reg = (TextView) findViewById(R.id.tv_reg);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_yzm_tip = (TextView) findViewById(R.id.tv_yzm_tip);
        tv_phone_tip = (TextView) findViewById(R.id.tv_phone_tip);
        tv_pwd_tip = (TextView) findViewById(R.id.tv_pwd_tip);
        tv_yzm = (TextView) findViewById(R.id.tv_yzm);
        mianze_text_message = (TextView) findViewById(R.id.mianze_text_message);
        tv_yzm.setOnClickListener(this);
        mianze_text_message.setOnClickListener(this);
        v_reg = findViewById(R.id.v_reg);
        v_login = findViewById(R.id.v_login);
        ed_pwd = (EditText) findViewById(R.id.ed_pwd);
        ed_pwd1 = (EditText) findViewById(R.id.ed_pwd1);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_phone1 = (EditText) findViewById(R.id.ed_phone1);
        ed_yzm = (EditText) findViewById(R.id.ed_yzm);
        tv_checked = (CheckedTextView) findViewById(R.id.tv_checked);
        tv_checked.setOnClickListener(this);
        tv_checked.setChecked(ic_checked);
        tv_reg.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_reg).setOnClickListener(this);
        findViewById(R.id.iv_weixin).setOnClickListener(this);
        findViewById(R.id.iv_xinlang).setOnClickListener(this);
        findViewById(R.id.iv_qq).setOnClickListener(this);
        findViewById(R.id.tv_forget).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);

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

    public void showDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(LoginActivity.this);
        }
        progressDialog.setMessage("努力加载中...");
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mianze_text_message:
                ShowActivity.showWebViewActivity(this,"https://center.zbmf.com/app/system/rule/register/");
                break;
            case R.id.tv_forget:
                ShowActivity.showActivity(this, ForgetActivity.class);
                break;
            case R.id.iv_close:
                finish();
                break;
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

                WebBase.send_code(phone, SettingDefaultsManager.getInstance().PUSH_CILENT_ID(), new JSONHandler(true, LoginActivity.this, "获取验证码") {
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
            case R.id.iv_weixin:
                showDialog();
                ThirdPartyUtil.getInstance().WX_Login(LoginActivity.this);
                break;
            case R.id.iv_qq:
                showDialog();
                ThirdPartyUtil.getInstance().QQ_Login(LoginActivity.this);
                break;
            case R.id.iv_xinlang:
                showDialog();
                ThirdPartyUtil.getInstance().Sina_Login(LoginActivity.this);
                break;
            case R.id.btn_login:

                if (TextUtils.isEmpty(ed_phone.getText().toString())) {
                    Toast.makeText(getBaseContext(), "请输入手机号或用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ed_pwd.getText().toString())) {
                    Toast.makeText(getBaseContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                WebBase.login(ed_phone.getText().toString(), ed_pwd.getText().toString(), PushManager.getInstance().getClientid(getBaseContext()), new JSONHandler(true, LoginActivity.this, "正在登录...") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        JSONObject user = obj.optJSONObject("user");
                        User userbean = new User();
                        userbean.setAuth_token(obj.optString("auth_token"));
                        userbean.setAvatar(user.optString("avatar"));
                        userbean.setUsername(user.optString("username"));
                        userbean.setUser_id(user.optString("user_id"));
                        userbean.setTruename(user.optString("truename"));
                        userbean.setRole(user.optString("role"));
                        userbean.setNickname(user.optString("nickname"));
                        userbean.setPhone(user.optString("phone"));
                        userbean.setIdcard(user.optString("idcard"));

                        dbManager.addUser(userbean);
                        dbManager.closeDB();
                        GroupActivity activity = (GroupActivity) ActivityUtil.getInstance().getActivity(ActivityUtil.GROUPACTIVITY);
                        activity.bind_client_id();
                        finish();
                        Toast.makeText(getBaseContext(), "登录成功！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        err_msg = "用户名或者密码错误";
                        Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.btn_reg:
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
                if (!ic_checked) {
                    Toast.makeText(LoginActivity.this, "请同意注册协议", Toast.LENGTH_SHORT).show();
                    return;
                }
                WebBase.register(SettingDefaultsManager.getInstance().PUSH_CILENT_ID(),
                        ed_yzm.getText().toString(), phone, pwd, new JSONHandler(true, LoginActivity.this, "正在注册") {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                Log.e("TAG", "onSuccess....");
                                JSONObject user = obj.optJSONObject("user");
                                User userbean = new User();
                                userbean.setAuth_token(obj.optString("auth_token"));
                                userbean.setAvatar(user.optString("avatar"));
                                userbean.setUsername(user.optString("username"));
                                userbean.setUser_id(user.optString("user_id"));
                                userbean.setTruename(user.optString("truename"));
                                userbean.setRole(user.optString("role"));
                                userbean.setNickname(user.optString("nickname"));
                                userbean.setPhone(user.optString("phone"));
                                userbean.setIdcard(user.optString("idcard"));
                                dbManager.addUser(userbean);
                                dbManager.closeDB();
                                ShowActivity.showActivity(LoginActivity.this, RegInfoActivity.class);
                                Toast.makeText(getBaseContext(), "注册成功，请完善信息", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(String err_msg) {

                                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.tv_reg:
                exc1(View.VISIBLE, View.INVISIBLE);
                exc2(R.color.cb8, R.color.c88);
                ed_phone.setText("");
                ed_pwd.setText("");
                break;
            case R.id.tv_login:
                exc1(View.INVISIBLE, View.VISIBLE);
                exc2(R.color.c88, R.color.cb8);
                ed_phone1.setText("");
                ed_pwd1.setText("");
                ed_pwd1.setText("");
                break;
            case R.id.tv_checked:
                ic_checked = !ic_checked;
                tv_checked.setChecked(ic_checked);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ed_phone.setText("");
//        ed_pwd.setText("");
//        ed_phone1.setText("");
//        ed_pwd1.setText("");
//        ed_yzm.setText("");
    }

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

    private void exc2(int a, int b) {
        tv_reg.setTextColor(getResources().getColor((a)));
        tv_login.setTextColor(getResources().getColor((b)));
    }

    private void exc1(int a, int b) {
        v_reg.setVisibility(a);
        ll_reg.setVisibility(a);
        v_login.setVisibility(b);
        ll_login.setVisibility(b);
//        ll_bottom.setVisibility(b);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ThirdPartyUtil.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        progressDialogDiss();
    }

    public void progressDialogDiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


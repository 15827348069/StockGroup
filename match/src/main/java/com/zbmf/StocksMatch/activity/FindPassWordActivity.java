package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.MainActivity;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.bean.VerifyCodeBean;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.ResetPassword;
import com.zbmf.StocksMatch.presenter.ForgetPassWordPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.util.Logx;
import com.zbmf.worklibrary.util.PhoneFormatCheckUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPassWordActivity extends BaseActivity<ForgetPassWordPresenter> implements ResetPassword {
    @BindView(R.id.et_user_name)
    TextInputEditText etUserName;
    @BindView(R.id.ti_user_name)
    TextInputLayout tiUserName;
    @BindView(R.id.ed_yzm)
    TextInputEditText ed_yzm;
    @BindView(R.id.tv_yzm)
    TextView tv_yzm;
    @BindView(R.id.et_user_pass)
    TextInputEditText etUserPass;
    @BindView(R.id.it_user_pass)
    TextInputLayout itUserPass;
    @BindView(R.id.btn_reg)
    TextView btn_reg;
    @BindView(R.id.registerProtocol)
    TextView registerProtocol;
    private ForgetPassWordPresenter mForgetPassWordPresenter;
    private TimeCount mTimecount;
    private int mLog_id;
    private String mPhone;

    @Override
    protected int getLayout() {
        return R.layout.activity_find_pass_word;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.find_password);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            String phone = bundle.getString(IntentKey.USER);
            etUserName.setText(phone);
        }
    }

    @Override
    protected ForgetPassWordPresenter initPresent() {
        mForgetPassWordPresenter = new ForgetPassWordPresenter();
        return mForgetPassWordPresenter;
    }

    @OnClick({R.id.btn_reg, R.id.tv_yzm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reg:
                String username = etUserName.getText().toString().trim();
                String pass = etUserPass.getText().toString().trim();
                if (validateAccount(username) && validatePassword(pass) && !TextUtils.isEmpty(String.valueOf(mLog_id))) {
                    mForgetPassWordPresenter.resetPassword(String.valueOf(mLog_id), username, pass);
                }
                break;
            case R.id.tv_yzm:
                mPhone = etUserName.getText().toString();
                if (!validateAccount(mPhone)) {
                    showToast(getString(R.string.niput_phone_tip));
                    etUserName.setBackgroundResource(R.drawable.add_stock_et_bg_tip);
                    return;
                }
                mForgetPassWordPresenter.forget(mPhone);
                break;
        }
    }

    /**
     * 验证用户名
     *
     * @param account
     * @return
     */
    private boolean validateAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            showError(tiUserName, "用户名不能为空");
            return false;
        }
        clearError(tiUserName);
        return PhoneFormatCheckUtils.isPhoneLegal(account);
    }

    /**
     * 验证密码
     *
     * @param password
     * @return
     */
    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            showError(itUserPass, "密码不能为空");
            return false;
        }

        if (password.length() < 6 || password.length() > 18) {
            showError(itUserPass, "密码长度为6-18位");
            return false;
        }
        clearError(itUserPass);
        return true;
    }

    /**
     * 显示错误提示，并获取焦点
     *
     * @param textInputLayout
     * @param error
     */
    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    private void clearError(TextInputLayout textInputLayout) {
        textInputLayout.setErrorEnabled(false);
    }

    @Override
    public void resetPassword(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
            String username = etUserName.getText().toString().trim();
            String pass = etUserPass.getText().toString().trim();
            if (validateAccount(username) && validatePassword(pass) ){
                mForgetPassWordPresenter.login(username, pass);
                ShowOrHideProgressDialog.showProgressDialog(this,this,getString(R.string.login_loading));
            }
        }
    }

    @Override
    public void resetPasswordErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @Override
    public void verifyCodeSuccess(VerifyCodeBean verifyCodeBean) {
        if (verifyCodeBean != null) {
            mLog_id = verifyCodeBean.getLog_id();
        }
    }

    @Override
    public void verifyCodeBeanFail(String msg) {
        Log.i("--TAG","------ msg 校验验证码  "+msg);
//        if (!TextUtils.isEmpty(msg)) {
//            showToast(msg);
//        }
    }

    @Override
    public void sendCodeSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
            if (mTimecount == null) {
                mTimecount = new TimeCount(60000, 1000);
            }
            mTimecount.start();
        }
        ed_yzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mForgetPassWordPresenter.verifyCode(etUserName.getText().toString().trim(), ed_yzm.getText().toString().trim());
            }
        });
    }

    @Override
    public void sendCodeFail(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @Override
    public void onLoginSucceed(LoginUser user) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        Logx.e(user.getAuth_token());
        showToast("登录成功");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onLoginErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)){
            showToast(msg+"登录失败");
        }
    }

    @Override
    public void forgetPassword(String msg) {
        if (!TextUtils.isEmpty(msg)){
            mForgetPassWordPresenter.sendCode(mPhone);
        }
    }

    class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            if (tv_yzm != null) {
                tv_yzm.setText(getString(R.string.reverfiy));
                tv_yzm.setEnabled(true);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            if (tv_yzm != null) {
                tv_yzm.setEnabled(false);
                tv_yzm.setText(millisUntilFinished / 1000 + getString(R.string.miao));
            }
        }
    }
}

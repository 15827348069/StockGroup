package com.zbmf.StocksMatch.fragment.login;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.HtmlUrl;
import com.zbmf.StocksMatch.fragment.BaseFragment;
import com.zbmf.StocksMatch.listener.IRegisterFragmentView;
import com.zbmf.StocksMatch.presenter.RegisterFragmentPresenter;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.util.ToastUtils;
import com.zbmf.worklibrary.util.Logx;
import com.zbmf.worklibrary.util.PhoneFormatCheckUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/12/7.
 * 注册页面
 */

public class RegisterFragment extends BaseFragment<RegisterFragmentPresenter> implements IRegisterFragmentView {
    @BindView(R.id.et_user_name)
    TextInputEditText etUserName;
    @BindView(R.id.ti_user_name)
    TextInputLayout tiUserName;
    @BindView(R.id.et_user_pass)
    TextInputEditText etUserPass;
    @BindView(R.id.it_user_pass)
    TextInputLayout itUserPass;
    @BindView(R.id.tv_yzm)
    TextView tv_yzm;
    @BindView(R.id.ed_yzm)
    TextInputEditText ed_yzm;
    @BindView(R.id.tv_checked)
    CheckedTextView tvChecked;
    @BindView(R.id.clear_phone)
    ImageView clearPhone;
    @BindView(R.id.clear_pw)
    ImageView clearPw;
    private TimeCount mTimecount;
    private boolean ic_checked;

    public static RegisterFragment newInstance() {
        RegisterFragment registerFragment = new RegisterFragment();
        return registerFragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_register_layout;
    }

    @Override
    protected void initView() {
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearPhone.setVisibility(View.VISIBLE);
                } else {
                    clearPhone.setVisibility(View.GONE);
                }
            }
        });
        etUserPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearPw.setVisibility(View.VISIBLE);
                } else {
                    clearPw.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected RegisterFragmentPresenter initPresent() {
        return new RegisterFragmentPresenter();
    }

    //点击注册
    @OnClick({R.id.btn_reg, R.id.tv_yzm, R.id.tv_checked, R.id.mianze_text_message, R.id.clear_pw, R.id.clear_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_phone:
                etUserName.getText().clear();
                break;
            case R.id.clear_pw:
                etUserPass.getText().clear();
                break;
            case R.id.btn_reg:
                String username = etUserName.getText().toString();
                String pass = etUserPass.getText().toString();
                String code = ed_yzm.getText().toString().trim();
                if (validateAccount(username) && validatePassword(pass) && !TextUtils.isEmpty(code)) {
                    getPresenter().mRegister(username, pass, code);
                }
                break;
            case R.id.tv_yzm:
                String phone = etUserName.getText().toString();
                if (!validateAccount(phone)) {
                    showToast(getString(R.string.niput_phone_tip));
                    etUserName.setBackgroundResource(R.drawable.add_stock_et_bg_tip);
                    return;
                }
                getPresenter().sendRegisterCode(phone);
                break;
            case R.id.mianze_text_message:
                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.REGISTER_PROTOCOL, getString(R.string.protocol));
                break;
            case R.id.tv_checked:
                ic_checked = !ic_checked;
                tvChecked.setChecked(ic_checked);
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
        Log.i("--TAG", "------验证手机号合法性  " + PhoneFormatCheckUtils.isPhoneLegal(account));
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
    public void onRegisterSucceed(LoginUser loginUser) {
        if (loginUser != null) {
            Logx.e(loginUser.getAuth_token());
//            showToast("注册成功");
            ToastUtils.showSquareImgSingleToast(getString(R.string.register_success)
                    ,getResources().getDrawable(R.drawable.success));
            LoginActivity activity = (LoginActivity) getActivity();
            activity.skipMainActivityToHome();
        }
    }

    @Override
    public void onRegisterErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
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
    }

    @Override
    public void sendCodeFail(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (msg.equals(Constans.ACCOUNT_EXIT)) {
                Toast.makeText(getActivity(), getString(R.string.register_login), Toast.LENGTH_LONG).show();
                ((LoginActivity) getActivity()).setDefaultFragment();
            } else {
                showToast(msg);
            }
            tiUserName.getEditText().getText().clear();
            itUserPass.getEditText().getText().clear();
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

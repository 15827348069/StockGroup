package com.zbmf.StocksMatch.fragment.login;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.fragment.BaseFragment;
import com.zbmf.StocksMatch.listener.ILoginFragmentView;
import com.zbmf.StocksMatch.presenter.LoginFragmentPresenter;
import com.zbmf.StocksMatch.util.EditUtil;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.util.ToastUtils;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.util.Logx;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/12/7.
 */

public class LoginFragment extends BaseFragment<LoginFragmentPresenter> implements ILoginFragmentView ,View.OnClickListener{
    @BindView(R.id.et_user_name)
    TextInputEditText etUserName;
    @BindView(R.id.ti_user_name)
    TextInputLayout tiUserName;
    @BindView(R.id.et_user_pass)
    TextInputEditText etUserPass;
    @BindView(R.id.it_user_pass)
    TextInputLayout itUserPass;
    @BindView(R.id.clear_phone)
    ImageView clear_phone;
    @BindView(R.id.clear_pw)
    ImageView clear_pw;

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_login_layout;
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
                    clear_phone.setVisibility(View.VISIBLE);
                } else {
                    clear_phone.setVisibility(View.GONE);
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
                    clear_pw.setVisibility(View.VISIBLE);
                } else {
                    clear_pw.setVisibility(View.GONE);
                }
            }
        });
        clear_phone.setOnClickListener(this);
        clear_pw.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected LoginFragmentPresenter initPresent() {
        return new LoginFragmentPresenter();
    }

    @OnClick({R.id.btn_reg, R.id.forgetPassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reg:
                String username = etUserName.getText().toString();
                String pass = etUserPass.getText().toString();
                if (validateAccount(username) && validatePassword(pass) && getPresenter() != null) {
                    getPresenter().Login(etUserName.getText().toString(), etUserPass.getText().toString());
                    ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.login_loading));
                }
                break;
            case R.id.forgetPassword:
                ShowActivity.showFindPasswordActivity(getActivity(), etUserName.getText().toString());
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
        return !EditUtil.isEmpty(tiUserName, account, "用户名不能为空");
    }

    /**
     * 验证密码
     *
     * @param password
     * @return
     */
    private boolean validatePassword(String password) {
        if (!EditUtil.isEmpty(itUserPass, password, "密码不能为空")) {
            if (password.length() < 6 || password.length() > 18) {
                showError(itUserPass, "密码长度为6-18位");
                return false;
            }
            clearError(itUserPass);
            return true;
        }
        return false;
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
    public void onLoginSucceed(LoginUser user) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        Logx.e(user.getAuth_token());
        ToastUtils.showSquareImgSingleToast(getString(R.string.login_success),
                getResources().getDrawable(R.drawable.success));
        LoginActivity activity = (LoginActivity) getActivity();
        activity.skipMainActivity();
    }

    @Override
    public void onLoginErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(getString(R.string.login_err));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear_pw:
                etUserPass.getText().clear();
                break;
            case R.id.clear_phone:
                etUserName.getText().clear();
                break;
        }
    }
}

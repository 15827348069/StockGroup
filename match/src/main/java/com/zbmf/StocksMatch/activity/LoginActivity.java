package com.zbmf.StocksMatch.activity;


import android.app.Dialog;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.FragmentTransaction;
        import android.util.DisplayMetrics;
        import android.view.Gravity;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.LinearLayout;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.zbmf.StocksMatch.MainActivity;
        import com.zbmf.StocksMatch.R;
        import com.zbmf.StocksMatch.api.ParamsKey;
        import com.zbmf.StocksMatch.fragment.login.LoginFragment;
        import com.zbmf.StocksMatch.fragment.login.RegisterFragment;
        import com.zbmf.StocksMatch.listener.WXLoginResultCallBack;
        import com.zbmf.StocksMatch.util.MyActivityManager;
        import com.zbmf.StocksMatch.util.ThirdLoginUtil;
        import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
        import com.zbmf.worklibrary.presenter.BasePresenter;

        import butterknife.BindView;
        import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/11/21.
 */

public class LoginActivity extends BaseActivity<BasePresenter> {

    @BindView(R.id.rg_login)
    RadioGroup rgLogin;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private int mFgFlag;
    private Dialog mDialog;

    @Override
    public BasePresenter initPresent() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected String initTitle() {
        return null;
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        ThirdLoginUtil.getInstance().init(this);
        mFgFlag = bundle.getInt(ParamsKey.FG_FLAG,-1);
        setDefaultFragment();
        rgLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_register:
                        llBottom.setVisibility(View.GONE);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
                        if (registerFragment.isAdded()) {
                            fragmentTransaction.hide(loginFragment).show(registerFragment).commit();
                        } else {
                            fragmentTransaction.add(R.id.fragment_login, registerFragment).commit();
                        }
                        break;
                    case R.id.rb_login:
                        llBottom.setVisibility(View.VISIBLE);
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
                        if (loginFragment.isAdded()) {
                            fragmentTransaction2.hide(registerFragment).show(loginFragment).commit();
                        } else {
                            fragmentTransaction2.add(R.id.fragment_login, loginFragment).commit();
                        }
                        break;
                }
            }
        });
    }
    //供旗下的fragment跳转到mainActivity调用
    public void skipMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ParamsKey.FG_FLAG,mFgFlag);
        new MainActivity().setIsExit(false);
        startActivity(intent);
        this.finish();
    }
    //跳转mainActivity的homeFragment
    public void skipMainActivityToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ParamsKey.FG_FLAG,0);
        new MainActivity().setIsExit(false);
        startActivity(intent);
        this.finish();
    }

    //进入登录页面，默认呈现登录的fragment,默认高亮登录按钮
    public void setDefaultFragment() {
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
        }
        if (registerFragment == null) {
            registerFragment = RegisterFragment.newInstance();
        }
        llBottom.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
        if (!loginFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_login, loginFragment).commit();
        } else {
            fragmentTransaction.hide(registerFragment).show(loginFragment).commit();
        }
        rgLogin.check(R.id.rb_login);
    }

    @OnClick({R.id.iv_close, R.id.iv_weixin, R.id.iv_qq, R.id.iv_xinlang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                if (mDialog==null){
                    mDialog = dialog2();
                }
                mDialog.show();
                break;
            case R.id.iv_weixin:
                ThirdLoginUtil.getInstance().WX_Login(new WXLoginResultCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        showToast(msg);
                        ShowOrHideProgressDialog.showProgressDialog(LoginActivity.this,
                                LoginActivity.this,getString(R.string.wx_logining));
                        ShowOrHideProgressDialog.setDialogCancel(false);
                    }

                    @Override
                    public void onFail(String msg) {
                        showToast(msg);
                        ShowOrHideProgressDialog.disMissProgressDialog();
                    }
                });
                break;
            case R.id.iv_qq:
                ShowOrHideProgressDialog.showProgressDialog(this,this,getString(R.string.qq_logining));
                ShowOrHideProgressDialog.setDialogCancel(false);
                ThirdLoginUtil.getInstance().QQ_Login(LoginActivity.this);
                break;
            case R.id.iv_xinlang:
                ShowOrHideProgressDialog.showProgressDialog(this,this,getString(R.string.sina_logining));
                ShowOrHideProgressDialog.setDialogCancel(false);
                ThirdLoginUtil.getInstance().Sina_Login(LoginActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ThirdLoginUtil.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    //只退出账号，跳转到首页
    private Dialog dialog2() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.out_dialog, null);
        TextView cancel_tv = (TextView) layout.findViewById(R.id.cancel_tv);
        TextView confirm_tv = (TextView) layout.findViewById(R.id.confirm_tv);
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss(); //
            }
        });
        confirm_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MyActivityManager.getMyActivityManager().removeAllAct();//退出应用的方法
            }
        });
        dialog.setContentView(layout);
        dialog.setCancelable(false);//设置dialog不可取消
        dialog.setCanceledOnTouchOutside(false);//设置dialog点击dialog以外的区域不消失
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager.LayoutParams lp = win.getAttributes();
        win.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        lp.width = (int) (metrics.widthPixels * 0.85);//获取屏幕的像素宽度
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.bottomDialogStyle);
        dialog.setCancelable(true);
        return dialog;
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MyActivityManager.getMyActivityManager().removeAllAct();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

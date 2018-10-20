package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.listener.MyTextWatcher;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;
import org.w3c.dom.Text;

public class RegistActivity extends ExActivity implements View.OnClickListener {

    private EditText ed_phone,ed_code,ed_pwd;
    private TextView tv_title;
    private Button btn_code;
    private String phone,password,code;
    private Get2Api server;
    private TimeCount timecount;
    private SharedPreferencesUtils sp;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        setupView();
    }

    private void setupView() {
        sp = new SharedPreferencesUtils(this);account = sp.getAccount();
        ed_phone = (EditText)findViewById(R.id.ed_phone);
        ed_code = (EditText)findViewById(R.id.ed_code);
        ed_pwd = (EditText)findViewById(R.id.ed_pwd);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(R.string.registe);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_reg).setOnClickListener(this);
        findViewById(R.id.btn_return).setOnClickListener(this);
        btn_code = (Button)findViewById(R.id.btn_getyzm);
        btn_code.setOnClickListener(this);

        MyTextWatcher watcher = new MyTextWatcher(ed_phone, 11);
        ed_phone.addTextChangedListener(watcher);

        RelativeLayout rl_title = (RelativeLayout)findViewById(R.id.rl_title);
        rl_title.setBackgroundResource(R.color.transparent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_return:
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_getyzm:
                phone = ed_phone.getText().toString().trim();
                if(phone.length() == 0){
                    UiCommon.INSTANCE.showTip(getString(R.string.phone_tip1));
                    return;
                }
                if(phone.length() != 11){
                    UiCommon.INSTANCE.showTip(getString(R.string.phone_tip2));
                    return;
                }

                new GetVerifyCode(this,R.string.getting_yzm,R.string.yzm_err,true).execute(phone);

                break;
            case R.id.btn_reg:
                phone = ed_phone.getText().toString().trim();
                if(phone.length() == 0){
                    UiCommon.INSTANCE.showTip(getString(R.string.phone_tip1));
                    return;
                }
                if(phone.length() != 11){
                    UiCommon.INSTANCE.showTip(getString(R.string.phone_tip2));
                    return;
                }
                password = ed_pwd.getText().toString();
                if(TextUtils.isEmpty(password) && password.length()<6){
                    UiCommon.INSTANCE.showTip(getString(R.string.input_pwd_tip));
                    return;
                }

                code = ed_code.getText().toString();
                if(TextUtils.isEmpty(code)){
                    UiCommon.INSTANCE.showTip(getString(R.string.input_yzm));
                    return;
                }

                new RegistTask(this,R.string.registing,R.string.registerr,true).execute();

                break;
        }
    }

    private class GetVerifyCode extends LoadingDialog<String, General> {


        public GetVerifyCode(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public General doInBackground(String... params) {

            General ret = null;

            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getPhonecode(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void doStuffWithResult(General ret) {
            if (ret != null && ret.code != -1) {
                if (ret.getStatus() == 1) {
                    if (timecount == null) {
                        timecount = new TimeCount(60000, 1000);
                    }
                    timecount.start();
                    UiCommon.INSTANCE.showTip(getString(R.string.yzm_ok));
                }else
                    UiCommon.INSTANCE.showTip(ret.msg);
            } else {
                UiCommon.INSTANCE.showTip(RegistActivity.this.getString(R.string.load_fail));
            }
        }
    }

    private class RegistTask extends LoadingDialog<String, User> {


        public RegistTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public User doInBackground(String... params) {
            User user = null;

            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                user = server.signphone(phone,password,code);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return user;
        }

        @Override
        public void doStuffWithResult(User result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    UiCommon.INSTANCE.setiUser(result);
                    sp.setAccount(result.getAuth_token());
                    new DatabaseImpl(RegistActivity.this).addUser(result);
                    if(TextUtils.isEmpty(account)){
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MAIN,null);
                    }

                    Intent intent= new Intent();
                    intent.setAction(Constants.FINISH);
                    sendBroadcast(intent);

                    UiCommon.INSTANCE.showTip(getString(R.string.registok));
                    finish();
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(RegistActivity.this.getString(R.string.load_fail));
            }
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btn_code.setText(getString(R.string.reverfiy));
            btn_code.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btn_code.setEnabled(false);
            btn_code.setText(millisUntilFinished / 1000 + getString(R.string.miao));
        }
    }
}

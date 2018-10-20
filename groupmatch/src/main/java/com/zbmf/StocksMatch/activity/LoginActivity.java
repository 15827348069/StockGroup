package com.zbmf.StocksMatch.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.receiver.FinishReceiver;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.utils.UiHelper;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.weibo.AccessTokenKeeper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class LoginActivity extends ExActivity implements View.OnClickListener {
    //TODO 合作账号登陆
    private TextView tv_title;
    private EditText ed_username, ed_pwd;
    private Get2Api server;
    private String username,password;
    private SharedPreferencesUtils sp;
    private String account;
    private FinishReceiver receiver;

    public static IWXAPI api;
    AuthInfo mAuthInfo;
    SsoHandler mSsoHandler;

//    private Weibo mWeibo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = new SharedPreferencesUtils(this);
        account = sp.getAccount();
        setupView();
    }

    private void setupView() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        if(TextUtils.isEmpty(account)){
            iv_back.setVisibility(View.GONE);
        }
        iv_back.setOnClickListener(this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.login);
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_pwd = (EditText) findViewById(R.id.ed_pwd);
        findViewById(R.id.btn_find).setOnClickListener(this);
        findViewById(R.id.btn_reg).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.wx_login_button).setOnClickListener(this);
        findViewById(R.id.sina_login).setOnClickListener(this);

        RelativeLayout rl_title = (RelativeLayout)findViewById(R.id.rl_title);
        rl_title.setBackgroundResource(R.color.transparent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiver == null){
            receiver=new FinishReceiver();
            UiHelper.RegistBroadCast(this,receiver, Constants.FINISH);
        }
        UiCommon.INSTANCE.DialogDismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if(TextUtils.isEmpty(sp.getAccount())){
                    UiCommon.INSTANCE.finishAppNow();
                }else{
                    finish();
                }
                break;
            case R.id.btn_login:
                username = ed_username.getText().toString();
                password = ed_pwd.getText().toString();
                if(TextUtils.isEmpty(username)){
                    UiCommon.INSTANCE.showTip(getString(R.string.input_username));
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    UiCommon.INSTANCE.showTip(getString(R.string.input_pwd));
                    return;
                }
                if(password.length()<6){
                UiCommon.INSTANCE.showTip(getString(R.string.input_pwd_tip));
                return;
            }

                new LoginTask(this,R.string.loging,R.string.load_fail).execute(username,password);
                break;
            case R.id.btn_reg:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_REGISTER, null);
                break;
            case R.id.btn_find:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_FINDPWD,null);
                break;
            case R.id.wx_login_button:
                UiCommon.INSTANCE.showDialog(this,R.string.wx_login);
                if (api == null) {
                    api = WXAPIFactory.createWXAPI(LoginActivity.this, Constants.WEI_APK_KEY, false);
                    api.registerApp(Constants.WEI_APK_KEY);
                }
                if (!api.isWXAppInstalled()) {
                    //提醒用户没有按照微信
                    UiCommon.INSTANCE.showTip("没有安装微信，请先安装微信");
                    UiCommon.INSTANCE.DialogDismiss();
                    return;
                }
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "zbmf_wx_login"+Math.random();
                api.sendReq(req);
                break;
            case R.id.sina_login:
//                if(mWeibo==null){
//                    mWeibo = Weibo.getInstance(UiCommon.WBSDKAppKey, REDIRECT_URL);
//                }
//                mWeibo.authorize(LoginActivity.this, new AuthDialogListener());
                UiCommon.INSTANCE.showDialog(this,R.string.wb_login);
                mAuthInfo = new AuthInfo(this,Constants.WBSDKAppKey,Constants.REDIRECT_URL, Constants.SCOPE);
                mSsoHandler = new SsoHandler(this, mAuthInfo);
                mSsoHandler.authorize(new AuthListener());
                break;
        }
    }

    private class LoginTask extends LoadingDialog<String, User> {

        public LoginTask(Context activity, int loadingMsg, int failMsg) {
            super(activity, loadingMsg, failMsg);
        }

        @Override
        public User doInBackground(String... params) {
            User user = null;

            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                user = server.Login(params[0],params[1]);

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
                    new DatabaseImpl(LoginActivity.this).addUser(result);
                    if(TextUtils.isEmpty(account)){
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MAIN,null);
                    }
                    finish();

                } else {
                    UiCommon.INSTANCE.showTip(getString(R.string.logerr));
                }
            } else {
                UiCommon.INSTANCE.showTip(LoginActivity.this.getString(R.string.load_fail));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHelper.UnRegistBroadCast(this,receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if(TextUtils.isEmpty(sp.getAccount())){
                UiCommon.INSTANCE.finishAppNow();
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            UiCommon.INSTANCE.DialogDismiss();
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
                new OpenLoginByAccessToken(LoginActivity.this,R.string.loading,R.string.load_fail,true).execute(accessToken.getToken(), accessToken.getUid(), "sina");
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            UiCommon.INSTANCE.DialogDismiss();
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            UiCommon.INSTANCE.DialogDismiss();
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }
    private class OpenLoginByAccessToken extends LoadingDialog<String,String>{

        public OpenLoginByAccessToken(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public String doInBackground(String... params) {
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                String s =  server.getAccessTokenByOpenapi(params[0],params[1],params[2]);
                JSONObject obj = new JSONObject(s);
                if(obj.has("auth_token")){
                    return obj.getString("auth_token");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(String s) {
            if(!TextUtils.isEmpty(s))
                new GetUserinfo2Task(LoginActivity.this,R.string.loging,R.string.logerr,true).execute(s);
            else
                UiCommon.INSTANCE.showTip(getString(R.string.logexcpeion));
        }
    }

    private class GetUserinfo2Task extends LoadingDialog<String,User>{

        public GetUserinfo2Task(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public User doInBackground(String... params) {
            User user = null;
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                user= server.getuserinfo2(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        public void doStuffWithResult(User user) {
            if (user != null && user.code != -1) {
                if (user.getStatus() == 1) {
                    sp.setAccount(user.getAuth_token());
                    UiCommon.INSTANCE.setiUser(user);
                    new DatabaseImpl(LoginActivity.this).addUser(user);
                    if(TextUtils.isEmpty(account)){
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MAIN,null);
                    }
                    finish();
                } else {
                    UiCommon.INSTANCE.showTip(user.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(LoginActivity.this.getString(R.string.load_fail));
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}

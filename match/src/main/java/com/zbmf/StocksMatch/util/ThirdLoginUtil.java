package com.zbmf.StocksMatch.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.bean.QQSinaLoginBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.listener.WXLoginResultCallBack;
import com.zbmf.StocksMatch.model.PassUserMode;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.Logx;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pq
 * on 2018/4/26.
 */

public class ThirdLoginUtil {
    private static ThirdLoginUtil instance;
    // 应用申请的高级权限
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    private Context context;
    private static final String QQ = "qzone";
    private static final String SINA = "sina";
    private Tencent mTencent;
    private IWXAPI wx_api;
    private AuthInfo mWeibo = null;
    private BaseUiListener tencentLoginListener;
    private SsoHandler mSsoHandler;
    private LoginActivity mActivity;

    public static ThirdLoginUtil getInstance() {
        synchronized (new Object()) {
            if (instance == null) {
                instance = new ThirdLoginUtil();
            }
            return instance;
        }
    }
    public void init(Context context){
        this.context=context;
        mActivity = MyActivityManager.getMyActivityManager().getAct(LoginActivity.class);
//        mActivity = ActivityUtil.getActivity(LoginActivity.class);
        mTencent=Tencent.createInstance(Constans.TencentSDKAppKey, context);
        wx_api = WXAPIFactory.createWXAPI(context, Constans.WEI_APK_KEY, false);
        mWeibo=new AuthInfo(context, Constans.WBSDKAppKey,Constans.REDIRECT_URL,SCOPE);
        wx_api.registerApp(Constans.WEI_APK_KEY);
        tencentLoginListener=new BaseUiListener();
//        dbManager=new DBManager(context);
    }
    public void QQ_Login(Activity activity){
        if (!mTencent.isSessionValid())
        {
            mTencent.login(activity, "all",tencentLoginListener);
        }else{
            mTencent.logout(activity);
            mTencent.login(activity, "all", tencentLoginListener);
        }
    }
    public void WX_Login(WXLoginResultCallBack callback){
        if (!wx_api.isWXAppInstalled()) {
            //提醒用户没有按照微信
            callback.onFail("请安装微信最新版客户端");
        }else{
            callback.onSuccess("调用微信登录成功");
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "zbmf_wx_login"+Math.random();
            wx_api.sendReq(req);
        }
    }
    public void Sina_Login(Activity activity){
        mSsoHandler=new SsoHandler(activity);
        mSsoHandler.authorize(new AuthDialogListener());
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Tencent.onActivityResultData(requestCode, resultCode, data, tencentLoginListener);
        if(requestCode ==com.tencent.connect.common.Constants.REQUEST_API ||
                requestCode == com.tencent.connect.common.Constants.REQUEST_APPBAR) {
            Tencent.handleResultData(data, tencentLoginListener);
        }
        if(mSsoHandler!=null){
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    private class BaseUiListener implements IUiListener
    {
        void doComplete(JSONObject values)
        {
            try {
                if (values.getInt("ret")==0) {
                    String token = values.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                    String openID = values.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                    Loging_Group(openID,token,QQ.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError e)
        {
            Logx.e(e.toString());
        }

        @Override
        public void onCancel() {
            Toast.makeText(context,"取消操作",Toast.LENGTH_SHORT).show();
            if(MyActivityManager.getMyActivityManager().isActivityExist(LoginActivity.class)){
                if(mActivity!=null){
                    ShowOrHideProgressDialog.disMissProgressDialog();
                }
            }
        }

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Logx.e("返回为空登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Logx.e("返回为空登录失败");
                return;
            }
            doComplete(jsonResponse);
        }
    }
    private class AuthDialogListener implements WbAuthListener
    {
        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
            Loging_Group(oauth2AccessToken.getUid(),oauth2AccessToken.getToken(),SINA);
        }

        @Override
        public void cancel() {
            Toast.makeText(context,"取消操作",Toast.LENGTH_SHORT).show();
            if(MyActivityManager.getMyActivityManager().isActivityExist(LoginActivity.class)){
                if(mActivity!=null){
                    ShowOrHideProgressDialog.disMissProgressDialog();
                }
            }
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            if(MyActivityManager.getMyActivityManager().isActivityExist(LoginActivity.class)){
                if(mActivity!=null){
                    ShowOrHideProgressDialog.disMissProgressDialog();
                }
            }
        }
    }
    private void Loging_Group(String open_id, String token, String api_type){
        new PassUserMode().loginQQSina(open_id, token, api_type, new CallBack<QQSinaLoginBean>() {
            @Override
            public void onSuccess(QQSinaLoginBean o) {
                LoginUser loginUser = new LoginUser();
                loginUser.setAuth_token(o.getAuth_token());
                loginUser.setUser(o.getUser());
                MatchSharedUtil.saveUser(loginUser);
                mActivity.showToast(context.getString(R.string.login_success));
                mActivity.skipMainActivityToHome();
            }

            @Override
            public void onFail(String msg) {
                mActivity.showToast(context.getString(R.string.login_fail));
            }
        });
    }
}

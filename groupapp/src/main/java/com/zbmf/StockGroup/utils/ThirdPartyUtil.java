package com.zbmf.StockGroup.utils;

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
import com.zbmf.StockGroup.activity.LoginActivity;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.User;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.db.DBManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by xuhao on 2017/1/17.
 */

public class ThirdPartyUtil {
    public static ThirdPartyUtil instance;
    // 应用申请的高级权限
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    private Context context;
    private static final String QQ="qzone";
    private static final String SINA="sina";
    private BaseUiListener tencentLoginListener;
    private DBManager dbManager;
    public  static ThirdPartyUtil getInstance(){
       synchronized (new Object()) {
           if(instance==null){
               instance=new ThirdPartyUtil();
           }
           return  instance;
       }
   }
    private Tencent mTencent;
    private IWXAPI wx_api;
    private AuthInfo mWeibo = null;
    private SsoHandler mSsoHandler;
    public void init(Context context){
        this.context=context;
        mTencent=Tencent.createInstance(Constants.TencentSDKAppKey, context);
        wx_api = WXAPIFactory.createWXAPI(context, Constants.WEI_APK_KEY, false);
        mWeibo=new AuthInfo(context, Constants.WBSDKAppKey,Constants.REDIRECT_URL,SCOPE);
        wx_api.registerApp(Constants.WEI_APK_KEY);
        tencentLoginListener=new BaseUiListener();
        dbManager=new DBManager(context);
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

    public void WX_Login(ResultCallback callback){
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

        protected void doComplete(JSONObject values)
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
            LogUtil.e(e.toString());
        }

        @Override
        public void onCancel() {
            Toast.makeText(context,"取消操作",Toast.LENGTH_SHORT).show();
            if(ActivityUtil.isActivityExist(LoginActivity.class)){
                LoginActivity activity= ActivityUtil.getActivity(LoginActivity.class);
                if(activity!=null){
                    activity.progressDialogDiss();
                }
            }
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            if (null == response) {
                LogUtil.e("返回为空登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                LogUtil.e("返回为空登录失败");
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
            if(ActivityUtil.isActivityExist(LoginActivity.class)){
                LoginActivity activity= ActivityUtil.getActivity(LoginActivity.class);
                if(activity!=null){
                    activity.progressDialogDiss();
                }
            }
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            if(ActivityUtil.isActivityExist(LoginActivity.class)){
                LoginActivity activity= ActivityUtil.getActivity(LoginActivity.class);
                if(activity!=null){
                    activity.progressDialogDiss();
                }
            }
        }
    }

    public void Loging_Group(String open_id,String token,String api_type){
        WebBase.qq_sina_login(open_id,token,api_type, new JSONHandler(true,context,"正在登录...") {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject user=obj.optJSONObject("user");
                User userbean=new User();
                userbean.setAvatar(user.optString("avatar"));
                userbean.setUsername(user.optString("username"));
                userbean.setUser_id(user.optString("user_id"));
                userbean.setTruename(user.optString("truename"));
                userbean.setAuth_token(obj.optString("auth_token"));
                userbean.setRole(user.optString("role"));
                userbean.setNickname(user.optString("nickname"));
                userbean.setPhone(user.optString("phone"));
                userbean.setIdcard(user.optString("idcard"));
                userbean.setIs_super(user.optInt("is_super"));
                userbean.setIs_vip(user.optInt("is_vip"));
                userbean.setVip_end_at(user.optString("vip_end_at"));
                dbManager.addUser(userbean);
                dbManager.closeDB();
                Intent intent = new Intent(Constants.NEW_LIVE_MSG_READ);
                context.sendBroadcast(intent);
                Intent intent2 = new Intent(Constants.UPDATE_VIDEO_LIST);
                context.sendBroadcast(intent2);
                Intent intent3 = new Intent(Constants.UP_DATA_MESSAGE);
                context.sendBroadcast(intent3);
                Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                if(ActivityUtil.isActivityExist(LoginActivity.class)){
                    LoginActivity activity= ActivityUtil.getActivity(LoginActivity.class);
                    if(activity!=null){
                        activity.finish();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(context,err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}


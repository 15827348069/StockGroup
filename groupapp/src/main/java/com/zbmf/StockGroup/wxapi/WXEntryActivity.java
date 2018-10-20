package com.zbmf.StockGroup.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.LoginActivity;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.User;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.db.DBManager;
import com.zbmf.StockGroup.utils.ActivityUtil;
import com.zbmf.StockGroup.utils.LogUtil;

import org.json.JSONObject;

/**
 * Created by xuhao on 2016/12/14.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public IWXAPI api;

    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_layout);
        dbManager=new DBManager(this);
        if(api==null){
            api = WXAPIFactory.createWXAPI(this, Constants.WEI_APK_KEY, false);
            api.handleIntent(getIntent(),this);
        }else{
            api.handleIntent(getIntent(),this);
        }
    }
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(ActivityUtil.isActivityExist(LoginActivity.class)){
            LoginActivity activity= ActivityUtil.getActivity(LoginActivity.class);
            if(activity!=null){
                activity.progressDialogDiss();
            }
        }
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //正确返回
                switch (baseResp.getType()){
                    case 1:
                        final String code = ((SendAuth.Resp) baseResp).code;
                        WebBase.weChat(code, new JSONHandler(true,WXEntryActivity.this,"正在登录...") {
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
                                Toast.makeText(WXEntryActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                if(ActivityUtil.isActivityExist(LoginActivity.class)){
                                    LoginActivity activity= ActivityUtil.getActivity(LoginActivity.class);
                                    if(activity!=null){
                                        activity.finish();
                                    }
                                }
                                Intent intent = new Intent(Constants.NEW_LIVE_MSG_READ);
                                sendBroadcast(intent);
                                Intent intent2 = new Intent(Constants.UPDATE_VIDEO_LIST);
                                sendBroadcast(intent2);
                                Intent intent3 = new Intent(Constants.UP_DATA_MESSAGE);
                                sendBroadcast(intent3);
                                finish();
                            }
                            @Override
                            public void onFailure(String err_msg) {
                                LogUtil.e(err_msg);
                                finish();
                                Toast.makeText(getApplicationContext(),err_msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 2:
                        Toast.makeText(WXEntryActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }

                break;
            case BaseResp.ErrCode.ERR_COMM:
                //	一般错误
                Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_COMM+baseResp.errStr, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_AUTH_DENIED+"微信认证被否决", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED :
                //发送失败
                Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_SENT_FAILED+baseResp.errStr, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持错误
                Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_UNSUPPORT+baseResp.errStr, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL :
                //用户取消
                Toast.makeText(getApplicationContext(),"取消操作", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Toast.makeText(getApplicationContext(), "返回错误"+baseResp.errStr, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}

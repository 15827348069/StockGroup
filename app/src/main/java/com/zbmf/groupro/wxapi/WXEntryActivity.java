package com.zbmf.groupro.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.LoginActivity;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.User;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;

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
                                dbManager.addUser(userbean);
                                dbManager.closeDB();
                                Toast.makeText(WXEntryActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                ActivityUtil.getInstance().getActivity(ActivityUtil.LoginActivity).finish();
                                GroupActivity activity= (GroupActivity) ActivityUtil.getInstance().getActivity(ActivityUtil.GROUPACTIVITY);
                                activity.getWolle();
                                finish();
                            }
                            @Override
                            public void onFailure(String err_msg) {
                                Log.e("err_msg====","==="+err_msg);
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
                LoginActivity activity= (LoginActivity) ActivityUtil.getInstance().getActivity(ActivityUtil.LoginActivity);
                if(activity!=null){
                    activity.progressDialogDiss();
                }
                finish();
                break;
            default:
                Toast.makeText(getApplicationContext(), "返回错误"+baseResp.errStr, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}

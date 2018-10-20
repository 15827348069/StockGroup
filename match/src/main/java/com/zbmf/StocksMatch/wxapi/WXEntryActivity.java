package com.zbmf.StocksMatch.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.bean.WeChatLoginBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.model.PassUserMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by xuhao
 * on 2016/12/14.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public IWXAPI api;
    private LoginActivity mActivity;
    //    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_layout);
        mActivity=MyActivityManager.getMyActivityManager().getAct(LoginActivity.class);
//        dbManager=new DBManager(this);
        if(api==null){
            api = WXAPIFactory.createWXAPI(this, Constans.WEI_APK_KEY, false);
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
        if(MyActivityManager.getMyActivityManager().isActivityExist(LoginActivity.class)){
            if(mActivity!=null){
                mActivity.DissLoading();
            }
        }
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //正确返回
                switch (baseResp.getType()){
                    case 1:
                        final String code = ((SendAuth.Resp) baseResp).code;
                        new PassUserMode().loginWeChat(code, new CallBack<WeChatLoginBean>() {
                            @Override
                            public void onSuccess(WeChatLoginBean o) {
                                ShowOrHideProgressDialog.disMissProgressDialog();
                                LoginUser loginUser = new LoginUser();
                                loginUser.setAuth_token(o.getAuth_token());
                                loginUser.setUser(o.getUser());
                                MatchSharedUtil.saveUser(loginUser);
                                mActivity.showToast(getString(R.string.login_success));
                                mActivity.skipMainActivityToHome();
                                WXEntryActivity.this.finish();
                            }

                            @Override
                            public void onFail(String msg) {
                                ShowOrHideProgressDialog.disMissProgressDialog();
                                mActivity.showToast(getString(R.string.login_fail));
                                WXEntryActivity.this.finish();
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

package com.zbmf.StockGTec.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.utils.Constants;

/**
 * Created by xuhao on 2016/12/14.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_layout);
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
                Toast.makeText(getApplicationContext(),R.string.weibosdk_demo_toast_share_success,Toast.LENGTH_LONG).show();
                finish();

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
//                Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_USER_CANCEL+baseResp.errStr, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),R.string.weibosdk_demo_toast_share_canceled,Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                Toast.makeText(getApplicationContext(), "返回错误"+baseResp.errStr, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}

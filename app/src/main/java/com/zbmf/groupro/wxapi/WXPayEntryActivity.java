package com.zbmf.groupro.wxapi;


import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.SendBrodacast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private IWXAPI api;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(api==null){
			api = WXAPIFactory.createWXAPI(this, Constants.WEI_APK_KEY, false);
			api.handleIntent(getIntent(),this);
		}else{
			api.handleIntent(getIntent(),this);
		}
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}
	@Override
	public void onReq(BaseReq req) {

	}
	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
			if(resp.errCode==0){
				Toast.makeText(getApplicationContext(), "充值成功",Toast.LENGTH_SHORT).show();
				SendBrodacast.send(getBaseContext(),Constants.UP_DATA_MESSAGE);
				finish();
				ActivityUtil.getInstance().getActivity(ActivityUtil.PayDetailActivity).finish();
			}
			if(resp.errCode==-1){
				Toast.makeText(getApplicationContext(), "充值失败:"+resp.errCode+"===="+resp.toString(),Toast.LENGTH_SHORT).show();
				finish();
			}
			if(resp.errCode==-2){
				Toast.makeText(getApplicationContext(), "取消支付",Toast.LENGTH_SHORT).show();
				finish();
			}
		}else{
			finish();
		}
	}
}                                                                                                                                                 
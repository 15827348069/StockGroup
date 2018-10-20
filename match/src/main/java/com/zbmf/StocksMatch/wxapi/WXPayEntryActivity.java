package com.zbmf.StocksMatch.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.util.SendBrodacast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private IWXAPI api;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(api==null){
			api = WXAPIFactory.createWXAPI(this, Constans.WEI_APK_KEY, false);
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
				SendBrodacast.send(getBaseContext(), Method.UP_DATA_MESSAGE);
				finish();
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
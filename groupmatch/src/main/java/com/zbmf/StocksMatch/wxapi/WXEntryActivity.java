package com.zbmf.StocksMatch.wxapi;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.ExActivity;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends ExActivity implements IWXAPIEventHandler{

	private String auth_token;
	private SharedPreferencesUtils sp;
	private String account;
	public static IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wx_layout);
		if(api==null)
			api = WXAPIFactory.createWXAPI(this, Constants.WEI_APK_KEY, false);
		api.handleIntent(getIntent(),this);
		sp = new SharedPreferencesUtils(this);
		account = sp.getAccount();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			//用户同意
		}
	}
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResp(BaseResp arg0) {
		UiCommon.INSTANCE.DialogDismiss();
		switch (arg0.errCode) {
			case BaseResp.ErrCode.ERR_OK://正确返回
				if(arg0.toString().contains("SendAuth")){
					String code = ((SendAuth.Resp) arg0).code;
					new OpenLoginByWX(WXEntryActivity.this).execute(code);
				}else{
					UiCommon.INSTANCE.showTip("分享成功");
					finish();
				}
				break;
			case BaseResp.ErrCode.ERR_COMM:
				//	一般错误
				Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_COMM+arg0.errStr, 0).show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				//认证被否决
//				Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_AUTH_DENIED+"微信认证被否决", 0).show();
				Toast.makeText(getApplicationContext(), "微信认证被否决", 0).show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_SENT_FAILED :
				//发送失败
				Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_SENT_FAILED+arg0.errStr, 0).show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				//不支持错误
				Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_UNSUPPORT+arg0.errStr, 0).show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL :
				//用户取消
//				Toast.makeText(getApplicationContext(), "返回错误"+BaseResp.ErrCode.ERR_USER_CANCEL+arg0.toString(), 0).show();
				if(arg0.toString().contains("SendAuth")){
					Toast.makeText(getApplicationContext(),R.string.weibosdk_demo_toast_auth_canceled,Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(),R.string.weibosdk_demo_toast_share_canceled,Toast.LENGTH_LONG).show();
				}
				finish();
				break;
			default:
				Toast.makeText(getApplicationContext(), "返回错误"+arg0.errStr, 0).show();
				finish();
				break;
		}
	}

	private Get2Api server = null;
	private class OpenLoginByWX extends LoadingDialog<String,String> {

		public OpenLoginByWX(Context activity) {
			super(activity, false, true);
		}

		@Override
		public String doInBackground(String... params) {
			if(server == null){
				server = new Get2ApiImpl();
			}
			try {
				String s =  server.matchWechat(params[0]);
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
				new GetUserinfo2Task(WXEntryActivity.this,R.string.loging,R.string.logerr,true).execute(s);
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
					new DatabaseImpl(WXEntryActivity.this).addUser(user);
					if(TextUtils.isEmpty(account)){
						UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MAIN,null);
					}

					Intent intent= new Intent();
					intent.setAction(Constants.FINISH);
					sendBroadcast(intent);
					finish();
				} else {
					UiCommon.INSTANCE.showTip(user.msg);
				}
			} else {
				UiCommon.INSTANCE.showTip(WXEntryActivity.this.getString(R.string.load_fail));
			}
		}}
}

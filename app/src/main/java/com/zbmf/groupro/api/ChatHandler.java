package com.zbmf.groupro.api;

import android.app.Activity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * JSON解析类，Activity调用onSuccess，onFailure函数
 */
public abstract class ChatHandler extends AsyncHttpResponseHandler {

     private String receptId;

    Activity aty;

     public ChatHandler(Activity aty,String receptId){
         this.receptId = receptId;
         this.aty = aty;
     }

    @Override
    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        String result = null;

        try {
            result = new String(bytes, "UTF-8");
            Log.e("服务器数据>>>>>>>>>",result);
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.optString("status").equals("ok")) {
                    onSuccess(obj);
                }/*else if (obj.getJSONObject("err").optInt("code") == 1004) {
//                    Intent intent = new Intent(aty,LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  |  Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    aty.startActivity(intent);
                    SettingDefaultsManager.getInstance().setAuthtoken("");
//                    Toast.makeText(aty,"登录已过期！请退出重新登录",0).show();
                }*/ else {
                    onFailure(ErrMessage.GetErrMessage(obj),receptId);
                }
            } catch (JSONException e) {
                onFailure("服务器返回数据格式错误",receptId);
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            onFailure("服务器返回数据错误",receptId);
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        try {
            if (throwable.getMessage().contains("UnknownHostException exception")) {
                onFailure("获取数据失败，请检查网络是否连接",receptId);
            } else {
                onFailure(throwable.getMessage(),receptId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(e.getMessage(),receptId);
        }
    }

    public abstract void onSuccess(JSONObject obj);

    public abstract void onFailure(String err_msg,String receptId);
}

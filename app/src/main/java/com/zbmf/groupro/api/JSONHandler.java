package com.zbmf.groupro.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zbmf.groupro.GroupApplication;
import com.zbmf.groupro.view.CustomProgressDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * JSON解析类，Activity调用onSuccess，onFailure函数
 */
public abstract class JSONHandler extends AsyncHttpResponseHandler {
    private  CustomProgressDialog progressDialog;

    private Context context;
    public JSONHandler(boolean show_dialog, Context context, String dialog_message){
        this.context = context;
        if(show_dialog){
            if(progressDialog==null){
                progressDialog=CustomProgressDialog.createDialog(context);
            }
            progressDialog.setMessage(dialog_message);
            if(!progressDialog.isShowing()){
                progressDialog.show();
            }
        }
    }

    /**
     * 不显示dialog调用该方法
     */
    public JSONHandler() {
        this.context = GroupApplication.context;
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
                }/*else if (obj.getJSONObject("err").optInt("code") == 1004||obj.getJSONObject("err").optInt("code") == 1005) {
                    SettingDefaultsManager.getInstance().setAuthtoken("");
//                    Intent intent = new Intent(context,LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  |  Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(intent);
//                    ShowActivity.showActivity(context, LoginActivity.class);
//                    Toast.makeText(context, "登录已过期！", Toast.LENGTH_SHORT).show();
                } */else {
                    onFailure(ErrMessage.GetErrMessage(obj));
                }
            } catch (JSONException e) {
                onFailure("数据获取异常");
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            onFailure("数据获取异常");
        }
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        try {
            if (throwable.getMessage().contains("UnknownHostException exception")) {
                onFailure("网络不可用");
            } else {
                onFailure("数据获取异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure("数据获取异常");
        }
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    public abstract void onSuccess(JSONObject obj);

    public abstract void onFailure(String err_msg);
}

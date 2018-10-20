package com.zbmf.StockGTec.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zbmf.StockGTec.activity.LoginActivity;
import com.zbmf.StockGTec.view.CustomProgressDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * JSON解析类，Activity调用onSuccess，onFailure函数
 */
public abstract class JSONHandler extends AsyncHttpResponseHandler {
    private CustomProgressDialog progressDialog;
    private Context context;

    public JSONHandler(boolean show_dialog, Context context, String dialog_message) {
        this.context = context;
        if (show_dialog) {
            if (progressDialog == null) {
                progressDialog = CustomProgressDialog.createDialog(context);
            }
            progressDialog.setMessage(dialog_message);
            progressDialog.show();
        }
    }

    /**
     * 不显示dialog调用该方法
     */
    public JSONHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        String result = null;

        try {
            result = new String(bytes, "UTF-8");
            Log.e("服务器数据>>>>>>>>>", result);
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.optString("status").equals("ok")) {
                    onSuccess(obj);
                } else if (obj.getJSONObject("err").optInt("code") == 1004) {
                    Intent intent = new Intent(context,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  |  Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    Toast.makeText(context,"登录已过期！",0).show();
                } else{
                    onFailure(ErrMessage.GetErrMessage(obj));
                }

            } catch (JSONException e) {
                onFailure("服务器返回数据格式错误");
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            onFailure("服务器返回数据错误");
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        try {
            if (throwable.getMessage().contains("UnknownHostException exception")) {
                onFailure("获取数据失败，请检查网络是否连接");
            } else {
                onFailure(throwable.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(e.getMessage());
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public abstract void onSuccess(JSONObject obj);

    public abstract void onFailure(String err_msg);
}

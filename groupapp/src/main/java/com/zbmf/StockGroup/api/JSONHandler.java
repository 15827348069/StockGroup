package com.zbmf.StockGroup.api;

import android.content.Context;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.view.CustomProgressDialog;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

/**
 * JSON解析类，Activity调用onSuccess，onFailure函数
 */
public abstract class JSONHandler extends AsyncHttpResponseHandler {
    private  CustomProgressDialog progressDialog;

    public JSONHandler(boolean show_dialog, Context context, String dialog_message){
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

    }

    @Override
    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        String result = null;
        try {
            result = new String(bytes, "UTF-8");
            LogUtil.e(result);
            LogUtil.json(result);
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.optString("status").equals("ok")) {
                    onSuccess(obj);
                }else {
                    onFailure(ErrMessage.GetErrMessage(obj));
                }
            } catch (JSONException e) {
                onFailure("数据获取异常");
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
            onFailure("数据获取异常");
        }
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        LogUtil.e(throwable.getMessage());
//        if (throwable.getMessage().contains("UnknownHostException exception")) {
//            onFailure("网络不可用");
//        } else {
            onFailure("数据获取异常");
//        }
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public abstract void onSuccess(JSONObject obj);

    public abstract void onFailure(String err_msg);
}

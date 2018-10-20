package com.zbmf.StocksMatch.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zbmf.StocksMatch.bean.StockBean;
import com.zbmf.worklibrary.dialog.CustomProgressDialog;
import com.zbmf.worklibrary.util.Logx;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON解析类，Activity调用onSuccess，onFailure函数
 */
public abstract class StockHandler extends AsyncHttpResponseHandler {
    private CustomProgressDialog progressDialog;

    public StockHandler(boolean show_dialog, Context context, String dialog_message){
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
    public StockHandler() {
    }

    @Override
    public void onSuccess(int s, Header[] headers, byte[] bytes) {
        String result = null;
        try {
            result = new String(bytes, "UTF-8");
            Logx.i(result);
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.optString("errorMsg").equals("SUCCESS")) {
                    if(!obj.isNull("data")){
                        JSONArray stock_data=obj.optJSONObject("data").optJSONArray("stock_data");
                        List<StockBean>stockList=new ArrayList<>();
                        for(int i=0;i<stock_data.length();i++){
                            JSONObject stock=stock_data.optJSONObject(i);
                            stockList.add(new StockBean(stock.optString("f_symbolName"),stock.optString("f_symbol")));
                        }
                        onSuccess(stockList);
                    }else{
                        onFailure(obj.toString());
                    }
                }/*else if (obj.getJSONObject("err").optInt("code") == 1004||obj.getJSONObject("err").optInt("code") == 1005) {
                    SettingDefaultsManager.getInstance().setAuthtoken("");
//                    Intent intent = new Intent(context,LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  |  Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(intent);
//                    ShowActivity.showActivity(context, LoginActivity.class);
//                    Toast.makeText(context, "登录已过期！", Toast.LENGTH_SHORT).show();
                } */else {
                    onFailure(obj.optString("errorMsg"));
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
        Logx.e(throwable.getMessage());
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
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public abstract void onSuccess(List<StockBean> stockList);

    public abstract void onFailure(String err_msg);
}

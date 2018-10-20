package com.zbmf.groupro.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zbmf.groupro.beans.RedPackgedBean;
import com.zbmf.groupro.view.CustomProgressDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * JSON解析类，Activity调用onSuccess，onFailure函数
 */
public abstract class OpenRedPackHandler extends AsyncHttpResponseHandler {
    private  CustomProgressDialog progressDialog;
    public OpenRedPackHandler(boolean show_dialog, Context context, String dialog_message){
        if(show_dialog){
            if(progressDialog==null){
                progressDialog=CustomProgressDialog.createDialog(context);
            }
            progressDialog.setMessage(dialog_message);
            progressDialog.show();
        }
    }

    /**
     * 不显示dialog调用该方法
     */
    public OpenRedPackHandler(){

    }
    @Override
    public void onSuccess(int k, Header[] headers, byte[] bytes) {
        String result = null;

        try {
            result = new String(bytes, "UTF-8");
            Log.e("服务器数据>>>>>>>>>",result);
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.optString("status").equals("ok")) {
                    RedPackgedBean redbean=new RedPackgedBean();
                    JSONObject oj=obj.optJSONObject("packet");
                    redbean.setRed_message(oj.optString("title"));
                    redbean.setUser_name(oj.optString("nickname"));
                    if(oj.optInt("status")==1){
                        JSONObject my_packet=obj.optJSONObject("my_packet");
                        if(!my_packet.isNull("mpay")||!my_packet.isNull("coupon")||!my_packet.isNull("coupon")){
                            switch (my_packet.optInt("type")) {
                                case 1:
                                    redbean.setRed_bag_money(my_packet.optString("mpay"));
                                    redbean.setRed_type("魔方宝");
                                    break;
                                case 2:
                                    //积分
                                    redbean.setRed_bag_money(my_packet.optString("score"));
                                    redbean.setRed_type("积分");
                                    break;
                                case 3:
                                    //兑换券
                                    redbean.setRed_bag_money(my_packet.optString("coupon"));
                                    redbean.setRed_type("兑换券");
                                    redbean.setCoupon_url(my_packet.optString("coupon_url"));
                                    break;
                            }
                            //抢过并且抢到了
                            redbean.setRed_status(4);
                        }else{
                            //抢过但是没抢到
                            redbean.setRed_status(3);
                        }
                    }else{
                        redbean.setRed_status(oj.optInt("status"));
                    }
                    redbean.setUser_avatar(oj.optString("avatar"));
                    redbean.setRed_id(oj.optString("packet_id"));
                    redbean.setStatus(1);
                    onSuccess(redbean);
                } else {
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
        if(progressDialog!=null){
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
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    public abstract void onSuccess(RedPackgedBean obj);

    public abstract void onFailure(String err_msg);
}

package com.zbmf.StockGTec.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zbmf.StockGTec.view.CustomProgressDialog;
import com.zbmf.StockGTec.beans.RedBagUserMessage;
import com.zbmf.StockGTec.beans.RedPackgedBean;

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
public abstract class GetRedPackHandler extends AsyncHttpResponseHandler {
    private CustomProgressDialog progressDialog;
    public GetRedPackHandler(boolean show_dialog, Context context, String dialog_message){
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
    public GetRedPackHandler(){

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
                    JSONObject packet=obj.optJSONObject("packet");
                    RedPackgedBean redbean=new RedPackgedBean();
                    redbean.setRed_id(packet.optString("packet_id"));
                    redbean.setRed_message(packet.optString("title"));
                    redbean.setUser_name(packet.optString("nickname"));
                    redbean.setRed_status(packet.optInt("status"));
                    redbean.setUser_avatar(packet.optString("avatar"));
                    redbean.setTotal_num(packet.optInt("total_num"));
                    redbean.setReceive_num(packet.optInt("receive_num"));

                    JSONObject my_packet=obj.optJSONObject("my_packet");
                    redbean.setRed_bag_money(my_packet.optString("mpay"));
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
                            redbean.setRed_type("优惠券");
                            redbean.setCoupon_url(my_packet.optString("coupon_url"));
                            break;
                    }

                    JSONArray packet_list=obj.optJSONArray("packet_list");
                    List<RedBagUserMessage> infolist=new ArrayList<RedBagUserMessage>();
                    for(int i=0;i<packet_list.length();i++){
                        JSONObject o=packet_list.optJSONObject(i);
                        RedBagUserMessage rm=new RedBagUserMessage();
                        if(o.optInt("is_luck")==1){
                            rm.setBast(true);
                        }else{
                            rm.setBast(false);
                        }
                        switch (my_packet.optInt("type")) {
                            case 1:
                                rm.setCountent(o.optString("mpay")+"魔方宝");
                                break;
                            case 2:
                                //积分
                                rm.setCountent(o.optString("score")+"积分");
                                break;
                            case 3:
                                //兑换券
                                rm.setCountent(o.optString("coupon")+"兑换券");
                                break;
                        }
                        rm.setUser_name(o.optString("nickname"));
                        rm.setUser_avatar(o.optString("avatar"));
                        rm.setUser_time(o.optString("created_at"));
                        infolist.add(rm);
                    }
                    redbean.setInfolist(infolist);
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

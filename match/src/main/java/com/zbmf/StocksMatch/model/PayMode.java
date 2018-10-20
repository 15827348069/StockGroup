package com.zbmf.StocksMatch.model;

import com.google.gson.Gson;
import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.PayListBean;
import com.zbmf.StocksMatch.model.imode.IPayMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pq
 * on 2018/4/9.
 */

public class PayMode extends BaseWalletMode implements IPayMode {
    @Override
    public void getPayList(final CallBack callBack) {
        postSubscrube(Method.PAY_LIST, SendParam.getPayList(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                PayListBean payListBean = GsonUtil.parseData(o, PayListBean.class);
                if (payListBean.getStatus()) {
                    callBack.onSuccess(payListBean);
                } else {
                    callBack.onFail(payListBean.getErr().getMsg());
                }

            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void wxPay(String id, String pro_num, final CallBack callBack) {
        postSubscrube(Method.WEIXIN_PAY, SendParam.wxPay(id, pro_num), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                Gson gson = new Gson();
                String toJson = gson.toJson(o);
                //解析
                JSONObject jsonObject = null;
                Map<String,String> map=new HashMap<>();
                try {
                    //将json数据当作不确定数据来解析
                    jsonObject=new JSONObject(toJson);
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()){
                        String key = keys.next();
                        String value = jsonObject.getString(key);
                        map.put(key,value);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (map.get("status").equals("ok")){
                    callBack.onSuccess(map);
                }else {
                    callBack.onFail("支付失败,请重试!");
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}

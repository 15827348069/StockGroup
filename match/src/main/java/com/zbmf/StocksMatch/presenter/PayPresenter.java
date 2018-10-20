package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.bean.PayListBean;
import com.zbmf.StocksMatch.listener.PayList;
import com.zbmf.StocksMatch.model.PayMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pq
 * on 2018/4/9.
 */

public class PayPresenter extends BasePresenter<PayMode, PayList> {
    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        getPayList();
    }

    @Override
    public PayMode initMode() {
        return new PayMode();
    }

    public void getPayList() {
        getMode().getPayList(new CallBack<PayListBean>() {
            @Override
            public void onSuccess(PayListBean o) {
                if (getView() != null) {
                    getView().payList(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().payListErr(msg);
                }
            }
        });
    }

    public void wxPay(String id, String proNum) {
        getMode().wxPay(id, proNum, new CallBack<Map<String,String>>() {
            @Override
            public void onSuccess(Map<String,String> map) {
                if (map!=null){
                    JSONObject jsonObject = null;
                    Map<String, String> valueMap = new HashMap<>();
                    try {
                        jsonObject = new JSONObject(map.get("order"));
                        Iterator<String> keys1 = jsonObject.keys();
                        while (keys1.hasNext()) {
                            String next = keys1.next();
                            String value0 = jsonObject.getString(next);
                            valueMap.put(next, value0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (getView() != null) {
                        getView().wxPayResult(valueMap);
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().wxPayErr(msg);
                }
            }
        });
    }
}

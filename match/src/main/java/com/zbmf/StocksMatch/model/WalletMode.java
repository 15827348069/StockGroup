package com.zbmf.StocksMatch.model;

import android.util.Log;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class WalletMode extends BaseWalletMode {

    public void getUserMoney(final CallBack callBack) {
        postSubscrube(Method.GET_WALLET, SendParam.getUserWallet(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                Log.i("--TAG","---- 更新mfb ");
                UserWallet userWallet = GsonUtil.parseData(o, UserWallet.class);
                if (userWallet.getStatus()){
                    callBack.onSuccess(userWallet);
                }else {
                    callBack.onFail(userWallet.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}

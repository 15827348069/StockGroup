package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.PrizeListBean;
import com.zbmf.StocksMatch.model.imode.IWinAPrizeMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class WinAPrizeMode extends BaseStockMode implements IWinAPrizeMode {
    @Override
    public void getUserPrizeList(String match_id, String user_id, final String page, final CallBack callBack) {
       postSubscrube(Method.PRIZE_LIST, SendParam.getUserPrize(match_id, user_id, page), new CallBack() {
           @Override
           public void onSuccess(Object o) {
               PrizeListBean prizeListBean = GsonUtil.parseData(o, PrizeListBean.class);
               if (prizeListBean.getStatus()){
                   callBack.onSuccess(prizeListBean.getResult());
               }else {
                   callBack.onFail(prizeListBean.getErr().getMsg());
               }

           }

           @Override
           public void onFail(String msg) {
               callBack.onFail(msg);
           }
       });
    }
}

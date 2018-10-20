package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.StocksMatch.model.imode.IQueryMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class QueryMode extends BaseStockMode implements IQueryMode {

    @Override
    public void recordList(String matchID, String page, String id, final CallBack callBack) {
        postSubscrube(Method.RECORD_LIST,SendParam.getRecordList(matchID,page,id),new CallBack() {
            @Override
            public void onSuccess(Object o) {
                DealsRecordList dealsRecordList = GsonUtil.parseData(o, DealsRecordList.class);
                if (dealsRecordList.getStatus()){
                    callBack.onSuccess(dealsRecordList.getResult());
                }else {
                    callBack.onFail(dealsRecordList.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
              callBack.onFail(msg);
            }
        });
    }
    //申请比赛，发送验证码
    public void applyMatchCode(int match_id, String mobile, final CallBack callBack) {
        postSubscrube(Method.SEND_CODE, SendParam.getMatchCode(match_id,mobile), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                assert baseBean != null;
                if (baseBean.getStatus()){
                    callBack.onSuccess(baseBean);
                }else {
                    callBack.onFail(baseBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}

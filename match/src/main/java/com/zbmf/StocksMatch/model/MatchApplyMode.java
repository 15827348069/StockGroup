package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.model.imode.IMatchApplyMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

import java.util.Map;

/**
 * Created by xuhao
 * on 2017/12/12.
 */

public class MatchApplyMode extends BaseMatchMode implements IMatchApplyMode {
    //申请比赛
    @Override
    public void onApplyMatch(Map<String, String> map, final CallBack callBack) {
        postSubscrube(Method.VERIFY_CODE, SendParam.getApplyMatch(map), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                callBack.onSuccess(o);
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
    //申请比赛，发送验证码
    @Override
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

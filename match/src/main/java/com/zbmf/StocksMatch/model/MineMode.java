package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.MatchList;
import com.zbmf.StocksMatch.bean.User;
import com.zbmf.StocksMatch.model.imode.IMineMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by xuhao
 * on 2017/11/30.
 */

public class MineMode extends BaseMatchMode implements IMineMode{

    @Override
    public void getMineDetail(final CallBack callBack) {
        postSubscrube(Method.GET_USERINFO, SendParam.getUserInfo(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                User user=GsonUtil.parseData(o,User.class);
                if(user.getStatus()){
                    callBack.onSuccess(user);
                }else{
                    if(user.getErr().getCode()==1004){
                        callBack.onFail(String.valueOf(user.getErr().getCode()));
                    }else{
                        callBack.onFail(user.getErr().getMsg());
                    }
                }
            }
            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void getMatchList(RefreshStatus status, final CallBack callBack) {
        postSubscrube(Method.GET_RUNMATCHS, SendParam.getRunMatchList(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                MatchList matchList= GsonUtil.parseData(o,MatchList.class);
                if(matchList.getStatus()){
                    callBack.onSuccess(matchList);
                }else{
                    callBack.onFail(matchList.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }


}

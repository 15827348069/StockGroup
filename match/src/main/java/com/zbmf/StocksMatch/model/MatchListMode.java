package com.zbmf.StocksMatch.model;


import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.model.imode.INewAllMatchMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * 获取比赛列表
 * All_MATCH 所有比赛
 * NEW_MATCH 最新比赛
 * Created by xuhao on 2017/11/28.
 */

public class MatchListMode extends BaseStockMode implements INewAllMatchMode {
    @Override
    public void newAllMatchMode(int page, int perPage,String method,final CallBack callBack) {
     postSubscrube(method, SendParam.getMatchNewAll(page,perPage,method), new CallBack() {
         @Override
         public void onSuccess(Object o) {
             MatchNewAllBean matchNewAllBean = GsonUtil.parseData(o, MatchNewAllBean.class);
             assert matchNewAllBean != null;
             if (matchNewAllBean.getStatus()){
                 if (matchNewAllBean.getResult()!=null){
                     callBack.onSuccess(matchNewAllBean.getResult());
                 }
             }else {
                 callBack.onFail(matchNewAllBean.getErr().getMsg());
             }
         }

         @Override
         public void onFail(String msg) {
             callBack.onFail(msg);
         }
     });
    }

    public void getNotice(String match_id, String page, final CallBack callBack){
        postSubscrube(Method.MATCH_NOTICE, SendParam.getMatchNotice(match_id, page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                NoticeBean noticeBean = GsonUtil.parseData(o, NoticeBean.class);
                if (noticeBean.getStatus()){
                    callBack.onSuccess(noticeBean.getResult());
                }else {
                    callBack.onFail(noticeBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}

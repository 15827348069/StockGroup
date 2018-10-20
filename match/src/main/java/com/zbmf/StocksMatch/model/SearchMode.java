package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.SearchMatchBean;
import com.zbmf.StocksMatch.model.imode.ISearchMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/21.
 */

public class SearchMode extends BaseStockMode implements ISearchMode {
    //根据比赛名称搜索比赛
    @Override
    public void searchMatch(String keyWord,int page,int per_page, final CallBack callBack) {
        postSubscrube(Method.SEARCH_MATCH,SendParam.searchMatchFromName(keyWord,page,per_page),new CallBack() {
            @Override
            public void onSuccess(Object o) {
                SearchMatchBean searchMatchBean = GsonUtil.parseData(o, SearchMatchBean.class);
                assert searchMatchBean != null;
                if (searchMatchBean.getStatus()) {
                    callBack.onSuccess(searchMatchBean);
                } else {
                    callBack.onFail(searchMatchBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}

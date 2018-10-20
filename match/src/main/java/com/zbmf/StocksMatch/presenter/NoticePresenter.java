package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.listener.INoticeView;
import com.zbmf.StocksMatch.model.NoticeMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/4/11.
 */

public class NoticePresenter extends BasePresenter<NoticeMode, INoticeView> {
    private String matchID;

    public NoticePresenter(String matchID) {
        this.matchID = matchID;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
//        if (!TextUtils.isEmpty(matchID)) {
//            getNoticeList(matchID, String.valueOf(ParamsKey.D_PAGE));
//        }
    }

    @Override
    public NoticeMode initMode() {
        return new NoticeMode();
    }

    public void getNoticeList(String matchID, String page) {
        getMode().getMatchNoticeList(matchID, page, new CallBack<NoticeBean.Result>() {
            @Override
            public void onSuccess(NoticeBean.Result result) {
                if (getView() != null) {
                    getView().noticeList(result);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().noticeErr(msg);
                }
            }
        });
    }
}

package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/4/11.
 */

public interface INoticeView extends BaseView {
    void noticeList(NoticeBean.Result notice);
    void noticeErr(String msg);
}

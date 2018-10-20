package com.zbmf.StocksMatch.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.worklibrary.presenter.BasePresenter;

import butterknife.BindView;

public class ShowNoticeContentActivity extends BaseActivity {

    @BindView(R.id.notice_title)
    TextView noticeTitle;
    @BindView(R.id.notice_content)
    TextView noticeContent;

    @Override
    protected int getLayout() {
        return R.layout.activity_show_notice_content;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.notice_content);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle!=null){
            NoticeBean.Result.Announcements announcements = (NoticeBean.Result.Announcements)
                    bundle.getSerializable(IntentKey.NOTICE);
            noticeTitle.setText(Html.fromHtml(announcements.getSubject()));
            noticeContent.setText(Html.fromHtml(announcements.getContent()));
        }
    }

    @Override
    protected BasePresenter initPresent() {
        return null;
    }
}

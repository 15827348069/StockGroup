package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.NoticeAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.INoticeView;
import com.zbmf.StocksMatch.presenter.NoticePresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NoticeActivity extends BaseActivity<NoticePresenter> implements INoticeView, AdapterView.OnItemClickListener {

    @BindView(R.id.ptr)
    ListViewForScrollView noticeList;
    @BindView(R.id.pull_to_refresh_sub_text)
    PullToRefreshScrollView ptr;
    @BindView(R.id.ll_none)
    LinearLayout mLlNone;
    private NoticeAdapter mNoticeAdapter;
    private int mTotal;
    private int mPage;
    private NoticePresenter mNoticePresenter;
    private Handler mHandler = new Handler();
    private List<NoticeBean.Result.Announcements> ans = new ArrayList<>();
    private List<NoticeBean.Result.Announcements> mAnnouncements;

    @SuppressLint("InflateParams")
    @Override
    protected int getLayout() {
        return R.layout.activity_notice;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.notice);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            NoticeBean.Result notice = (NoticeBean.Result) bundle.getSerializable(IntentKey.NOTICE);
            if (notice!=null){
                mAnnouncements = notice.getAnnouncements();
                mTotal = notice.getTotal();
                mPage = notice.getPage();
            }
        }
        mNoticeAdapter = new NoticeAdapter(this);
        noticeList.setAdapter(mNoticeAdapter);
        if (mAnnouncements!=null){
            if (mAnnouncements.size() == 0) {
                noticeList.setVisibility(View.GONE);
                mLlNone.setVisibility(View.VISIBLE);
            } else {
                noticeList.setVisibility(View.VISIBLE);
                mNoticeAdapter.setList(mAnnouncements);
            }
        }
        noticeList.setOnItemClickListener(this);
        ptr.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mNoticeAdapter.getList() != null) {
                    mNoticeAdapter.clearList();
                }
                getPresenter().setFirst(true);
                mNoticePresenter.getNoticeList(Constans.MATCH_ID,String.valueOf(ParamsKey.D_PAGE));
                ShowOrHideProgressDialog.showProgressDialog(NoticeActivity.this,
                        NoticeActivity.this, getString(R.string.hard_loading));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTotal > mNoticeAdapter.getList().size()) {
                    mPage += 1;
                    mNoticePresenter.getNoticeList(Constans.MATCH_ID, String.valueOf(mPage));
                } else {
                    showToast(getString(R.string.nomore_loading));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptr.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    protected NoticePresenter initPresent() {
        mNoticePresenter = new NoticePresenter(Constans.MATCH_ID);
        return mNoticePresenter;
    }

    @Override
    public void noticeList(NoticeBean.Result notice) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (ptr.isRefreshing()){
            ptr.onRefreshComplete();
        }
        if (notice != null) {
            mPage = notice.getPage();
            mTotal = notice.getTotal();
            List<NoticeBean.Result.Announcements> announcements = notice.getAnnouncements();
            if (announcements.size() > 0) {
                mLlNone.setVisibility(View.GONE);
            } else {
                mLlNone.setVisibility(View.VISIBLE);
            }
            if (mNoticeAdapter.getList() == null) {
                mNoticeAdapter.setList(announcements);
            } else {
                if (mPage == ParamsKey.D_PAGE) {
                    ans.addAll(announcements);
                    mNoticeAdapter.addList(ans);
                    ans.clear();
                } else {
                    mNoticeAdapter.addList(announcements);
                }
            }
        }
    }

    @Override
    public void noticeErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoticeBean.Result.Announcements item = mNoticeAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.NOTICE, item);
        ShowActivity.showActivity(this, bundle, ShowNoticeContentActivity.class);
    }
}

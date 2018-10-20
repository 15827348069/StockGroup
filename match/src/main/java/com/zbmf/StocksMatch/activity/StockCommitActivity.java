package com.zbmf.StocksMatch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.StockCommentAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.ShareBean;
import com.zbmf.StocksMatch.bean.StockCommentsBean;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.common.AppConfig;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IStockCommentView;
import com.zbmf.StocksMatch.listener.PublishCommentsStr;
import com.zbmf.StocksMatch.presenter.StockCommitPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.StocksMatch.view.ShowWebShareLayout;
import com.zbmf.StocksMatch.view.StockCommentsDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StockCommitActivity extends BaseActivity<StockCommitPresenter> implements IStockCommentView, PublishCommentsStr {
    @BindView(R.id.no_message_text)
    TextView no_message_text;
    @BindView(R.id.tv_left_button)
    TextView tv_left_button;
    @BindView(R.id.tv_right_button)
    TextView tv_right_button;
    @BindView(R.id.ll_none)
    LinearLayout ll_none;
    @BindView(R.id.listview)
    ListViewForScrollView listView;
    @BindView(R.id.mPullToRefreshScrollView)
    PullToRefreshScrollView ptrsv;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.tv_share)
    TextView tv_share;
    @BindView(R.id.bottom_layout)
    LinearLayout bottom_layout;
    @BindView(R.id.imb_title_return)
    ImageButton imb_title_return;
    private StockCommitPresenter mStockCommitPresenter;
    private StockMode stockMode;
    private StockCommentAdapter mStockCommentAdapter;
    private StockCommentsDialog mStockCommentsDialog;
    private int mPage;
    private int mTotal;
    private ShowWebShareLayout mShowWebShareLayout;
    private String mShare_title;
    private Handler mHandler = new Handler();

    @Override
    protected int getLayout() {
        return R.layout.activity_stock_commit;
    }

    @Override
    protected String initTitle() {
        return stockMode.getStockNmae() + "(" + stockMode.getSymbol() + ")";
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            if (bundle.getSerializable(IntentKey.STOCK_HOLDER) instanceof StockMode) {
                stockMode = (StockMode) bundle.getSerializable(IntentKey.STOCK_HOLDER);
            }
        }
        if (mShowWebShareLayout == null) {
            mShowWebShareLayout = new ShowWebShareLayout(this);
        }
        mShare_title = stockMode.getStockNmae() + "(" + stockMode.getSymbol() + ")" + "好不好，来资本魔方聊牛股！";
        mStockCommentAdapter = new StockCommentAdapter(this);
        listView.setAdapter(mStockCommentAdapter);
        ptrsv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mStockCommentAdapter.getList() != null) {
                    mStockCommentAdapter.clearList();
                }
                mPage = ParamsKey.D_PAGE;
                getPresenter().setFirst(true);
                getPresenter().getDatas();
                ShowOrHideProgressDialog.showProgressDialog(StockCommitActivity.
                        this,StockCommitActivity.
                        this, getString(R.string.hard_loading));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTotal < mStockCommentAdapter.getList().size()) {
                    mPage += 1;
                    mStockCommitPresenter.getStockCommentList(stockMode.getSymbol(), String.valueOf(mPage));
                } else {
                    showToast(getString(R.string.nomore_loading));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrsv.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    protected StockCommitPresenter initPresent() {
        mStockCommitPresenter = new StockCommitPresenter(stockMode.getSymbol(), stockMode.getStockNmae());
        return mStockCommitPresenter;
    }

    private List<StockCommentsBean.Result.StockComments> commentsList = new ArrayList<>();

    @Override
    public void getStockCommentList(StockCommentsBean.Result o) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (ptrsv != null && ptrsv.isRefreshing()) {
            ptrsv.onRefreshComplete();
        }
        mPage = o.getPage();
        mTotal = o.getTotal();
        List<StockCommentsBean.Result.StockComments> stock_comments = o.getStock_comments();
        if (mPage == ParamsKey.D_PAGE && stock_comments.size() == 0) {
            ll_none.setVisibility(View.VISIBLE);
            no_message_text.setText(getString(R.string.no_comments));
            return;
        }
        if (mStockCommentAdapter.getList() == null) {
            assert stock_comments != null;
            mStockCommentAdapter.setList(stock_comments);
        } else {
            if (mStockCommentAdapter.getList().size() > 0 && mPage == 1) {
                commentsList.addAll(stock_comments);
                mStockCommentAdapter.clearList();
                mStockCommentAdapter.addList(commentsList);
                commentsList.clear();
            } else {
                mStockCommentAdapter.addList(stock_comments);
                mStockCommentAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getStockCommentErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    //添加股票评论成功后刷新评论列表
    @Override
    public void addStockCommentStatus(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        getPresenter().getDatas();
        ShowOrHideProgressDialog.showProgressDialog(this,this, getString(R.string.hard_loading));
    }

    @OnClick({R.id.tv_msg, R.id.tv_share,R.id.imb_title_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_msg:
                publishComments();
                break;
            case R.id.tv_share:
                mShowWebShareLayout.showShareLayout(
                        new ShareBean(
                                mShare_title != null ? mShare_title : getString(R.string.app_name),
                                AppConfig.SHARE_LOGO_IMG,
                                AppConfig.SHARE_STOCK_URL));
                break;
            case R.id.imb_title_return:
                //将评论的数量返回到上一个页面刷新评论按钮的数字
                Intent intent = new Intent();
                intent.putExtra(IntentKey.COMMENT_COUNT, mTotal);
                setResult(Constans.ADD_COMMENT_RESPONSE, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mShowWebShareLayout == null) {
            mShowWebShareLayout = new ShowWebShareLayout(this);
        }
        mShowWebShareLayout.onNewIntent(intent);
    }

    private void publishComments() {
        if (mStockCommentsDialog == null) {
            mStockCommentsDialog = new StockCommentsDialog(this, R.style.stock_comments_dialog);
        }
        mStockCommentsDialog.getDialog().showC().setOnPublishComments(this);
    }

    //提交股票评论
    @Override
    public void getPublishComments(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            mStockCommentsDialog.setInputCommentNormalBg();
            ShowOrHideProgressDialog.showProgressDialog(this,this, getString(R.string.comment_commit));
            mStockCommitPresenter.addStockComments(stockMode.getSymbol(), msg);
        } else {
            mStockCommentsDialog.setInputCommentTipBg();
            showToast(getString(R.string.stock_comment_empty_tip));
        }
    }
}

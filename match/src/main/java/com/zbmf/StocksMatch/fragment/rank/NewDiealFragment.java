package com.zbmf.StocksMatch.fragment.rank;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MatchNewDiealAdapter;
import com.zbmf.StocksMatch.adapter.interfaces.OnAdapterClickListener;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.DealSys;
import com.zbmf.StocksMatch.bean.DealsList;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.HtmlUrl;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.fragment.BaseFragment;
import com.zbmf.StocksMatch.listener.FlowItemClickToBuy;
import com.zbmf.StocksMatch.listener.IMatchNeaDealView;
import com.zbmf.StocksMatch.presenter.MatchNewDealPresenter;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.FlowBuyStockDialog;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by xuhao
 * on 2017/12/1.
 * 最新交易
 */

public class NewDiealFragment extends BaseFragment<MatchNewDealPresenter> implements OnAdapterClickListener,
        IMatchNeaDealView, FlowItemClickToBuy {

    @BindView(R.id.new_dieal_listview)
    PullToRefreshListView newDiealListview;
    private MatchNewDiealAdapter adapter;
    private int mPage;
    private int mTotal;
    private MatchNewDealPresenter mMatchNewDealPresenter;
    private String mMatchID;
    private Handler mHandler = new Handler();
    private FlowBuyStockDialog mFlowBuyStockDialog;
    private DealSys dealSys;
    private MatchNewAllBean.Result userMatch;

    public static NewDiealFragment newInstance(String matchID) {
        NewDiealFragment newDiealFragment = new NewDiealFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.MATCH_ID, matchID);
        newDiealFragment.setArguments(bundle);
        return newDiealFragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_new_dieal_layout;
    }

    @Override
    protected void initView() {
        ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
        newDiealListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DealSys dealSys = adapter.getItem(i - 1);
                ShowActivity.showStockDetail2(getActivity(), new StockMode(dealSys.getStock_name(), dealSys.getSymbol()), mMatchID);
            }
        });
        newDiealListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
                //下拉刷新
                if (adapter.getList() != null) {
                    adapter.clearList();
                }
                mPage = ParamsKey.D_PAGE;
                getPresenter().setFirst(true);
                getPresenter().setstatus(RefreshStatus.PULL_TO_REFRESH);
                getPresenter().getDatas();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //张拉加载
                if (adapter.getList().size() < mTotal) {
                    mPage += 1;
                    getPresenter().setstatus(RefreshStatus.LOAD_MORE);
                    getPresenter().loadMore(mPage);
                } else {
                    showToast("最新交易 没有更多");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            newDiealListview.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mMatchID = getArguments().getString(IntentKey.MATCH_ID);
        adapter = new MatchNewDiealAdapter(getActivity());
        adapter.setOnAdapterClickListener(this);
        newDiealListview.setAdapter(adapter);
    }

    @Override
    protected MatchNewDealPresenter initPresent() {
        mMatchNewDealPresenter = new MatchNewDealPresenter(RefreshStatus.LOAD_DEFAULT/*,mPage,mPerpage*/,mMatchID);
        return mMatchNewDealPresenter;
    }

    //跟买
    @Override
    public void onClickBuyListener(DealSys dealSys) {
        this.dealSys = dealSys;
        mFlowBuyStockDialog = new FlowBuyStockDialog(getActivity(), R.style.Theme_Light_Dialog, getActivity());
        if (userMatch!=null){
            mFlowBuyStockDialog.showFlowDialog(userMatch).showFlow().setFlowItemClickToBuy(this).clickCancelFlow();
        }
    }

    //策买
    @Override
    public void onClickClBuyListener(DealSys dealSys) {
        ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.TRADER_BUY + dealSys.getSymbol(), "");
    }

    @Override
    public void flowItemClickToBuy(MatchNewAllBean.Result.Matches matches) {
        if (matches != null) {
            mMatchNewDealPresenter.getMatchInfo(String.valueOf(matches.getMatch_id()), MatchSharedUtil.UserId());
            ShowOrHideProgressDialog.showProgressDialog(getActivity(),getActivity(),getString(R.string.hard_loading));
            mFlowBuyStockDialog.dismissFlow();
        }
    }

    @Override
    public void onClickCommentListener(DealSys dealSys) {

    }

    @Override
    public void RushDealList(DealsList.Result deals_sys) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (newDiealListview.isRefreshing()) {
            newDiealListview.onRefreshComplete();
        }
        mPage = deals_sys.getPage();
        mTotal = deals_sys.getTotal();
        List<DealSys> deals = deals_sys.getDeals();
        if (adapter.getList() == null) {
            adapter.setList(deals);
        } else {
            adapter.addList(deals);
        }
    }

    @Override
    public void rushMatchBean(MatchInfo matchBean) {
        ShowOrHideProgressDialog.disMissProgressDialog();
//        MatchInfo matchInfo = matchBean;
        if (matchBean != null) {
            ShowActivity.showStockBuyActivity(getActivity(), matchBean, -1,
                    new StockMode(dealSys.getStock_name(), dealSys.getSymbol()), Constans.BUY_FLAG, mMatchID);
        } else {
            showToast(getString(R.string.reload));
        }
    }

    @Override
    public void RushMatchList(MatchNewAllBean.Result userMatch) {
        if (userMatch!=null){
            this.userMatch = userMatch;
        }
    }

    @Override
    public void RushMatchListErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
    }
}

package com.zbmf.StocksMatch.fragment.drill;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.NewAllMatchAdapter;
import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.fragment.BaseFragment;
import com.zbmf.StocksMatch.listener.IMatchFragment;
import com.zbmf.StocksMatch.presenter.MatchFaragmentPresenter;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xuhao
 * on 2017/11/28.
 * 最新比赛、全部fragment
 */

public class MatchListFragment extends BaseFragment<MatchFaragmentPresenter> implements IMatchFragment {

    @BindView(R.id.plv_match_list)
    PullToRefreshListView plvMatchList;
    private int flag;
    private NewAllMatchAdapter adapter;
    private int page;
    private MatchFaragmentPresenter mMatchFaragmentPresenter;
    private int mTotal;
    private Handler mHandler = new Handler();

    public static MatchListFragment newInstance(int flag) {
        MatchListFragment fragment = new MatchListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.FLAG, flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_matchlist_layout;
    }

    @Override
    protected MatchFaragmentPresenter initPresent() {
        if (getArguments() != null) {
            flag = getArguments().getInt(IntentKey.FLAG);
        }
        mMatchFaragmentPresenter = new MatchFaragmentPresenter(flag);
        return mMatchFaragmentPresenter;
    }

    @Override
    protected void initView() {
        ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
        plvMatchList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter!=null){
                    if (adapter.getList() != null) {
                        adapter.clearList();
                    }
                    page = ParamsKey.D_PAGE;
                    getData();
                    ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (flag==Constans.NEW_MATCH){
                    if (adapter!=null){
                        if (adapter.getList().size()>=10){
                            showToastMsg("没有更多");
                            plvMatchList.setPullLabel("没有更多", PullToRefreshBase.Mode.PULL_FROM_END);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    plvMatchList.onRefreshComplete();
                                }
                            }, 1000);
                            return;
                        }
                    }
                }
                //上拉加载更多
                if (adapter.getList().size() == mTotal) {
                    showToastMsg("没有更多");
                    plvMatchList.setPullLabel("没有更多", PullToRefreshBase.Mode.PULL_FROM_END);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            plvMatchList.onRefreshComplete();
                        }
                    }, 1000);
                } else {
                    page += 1;
                    getData();
//                    if (flag== Constans.NEW_MATCH){
//                        mMatchFaragmentPresenter.getMatchList(page,ParamsKey.D_PERPAGE,Method.MATCH_NEW);
//                    }else if (flag==Constans.All_MATCH){
//                        mMatchFaragmentPresenter.getMatchList(page,ParamsKey.D_PERPAGE,Method.MATCH_ALL);
//                    }
                }
            }
        });
        //listView点击事件
        plvMatchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowActivity.showMatchDetail(getActivity(), adapter.getItem(i - 1));
            }
        });
    }

    private void getData() {
        int perPage = ParamsKey.D_PERPAGE;
        if (flag == Constans.NEW_MATCH) {
            mMatchFaragmentPresenter.getMatchList(page, perPage, Method.MATCH_NEW);
        } else if (flag == Constans.All_MATCH) {
            mMatchFaragmentPresenter.getMatchList(page, perPage, Method.MATCH_ALL);
        }
    }

    @Override
    protected void initData() {
        adapter = new NewAllMatchAdapter(getActivity());
        plvMatchList.setAdapter(adapter);
    }

    @Override
    public void RushMatchList(MatchNewAllBean.Result result) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (plvMatchList.isRefreshing()) {
            plvMatchList.onRefreshComplete();
        }
        page = result.getPage();
        mTotal = result.getTotal();
        List<MatchNewAllBean.Result.Matches> matches = result.getMatches();
        if (adapter.getList() == null) {
            if (flag == Constans.NEW_MATCH) {
                List<MatchNewAllBean.Result.Matches> newMatch = new ArrayList<>();
                if (matches.size() > 10) {
                    for (int i = 0; i < matches.size(); i++) {
                        if (i < 10) {
                            newMatch.add(matches.get(i));
                        }
                    }
                } else {
                    newMatch.addAll(matches);
                }
                adapter.setList(newMatch);
            } else {
                adapter.setList(matches);
            }
        } else {
            if (page == ParamsKey.D_PAGE) {
                if (flag == Constans.NEW_MATCH) {
                    List<MatchNewAllBean.Result.Matches> newMatch = new ArrayList<>();
                    if (matches.size() > 10) {
                        for (int i = 0; i < matches.size(); i++) {
                            if (i < 10) {
                                newMatch.add(matches.get(i));
                            }
                        }
                    } else {
                        newMatch.addAll(matches);
                    }
                    adapter.addList(newMatch);
                }else {
                    adapter.addList(matches);
                }
            } else if (mTotal > adapter.getList().size()) {
                if (flag == Constans.All_MATCH) {
                    adapter.addList(matches);
                }
            }
        }
    }

    @Override
    public void RushMatchListErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (plvMatchList.isRefreshing()) {
            plvMatchList.onRefreshComplete();
        }
        if (!TextUtils.isEmpty(msg)) {
            showToast(getString(R.string.login_err_tip));
        }
    }
}

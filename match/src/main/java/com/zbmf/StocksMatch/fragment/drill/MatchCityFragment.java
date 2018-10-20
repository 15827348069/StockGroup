package com.zbmf.StocksMatch.fragment.drill;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MatchCityAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchList3Bean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.fragment.BaseFragment;
import com.zbmf.StocksMatch.listener.IMatchCityFragment;
import com.zbmf.StocksMatch.presenter.MatchCityPresenter;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by xuhao
 * on 2017/11/28.
 * 比赛Fragment下的城市、高校、券商fragment
 */

public class MatchCityFragment extends BaseFragment<MatchCityPresenter> implements IMatchCityFragment {
    @BindView(R.id.plv_match_list)
    PullToRefreshListView plvMatchList;
    @BindView(R.id.tv_name)
    TextView tvName;
    private int page;
    private int flag;
    private MatchCityAdapter adapter;
    private MatchCityPresenter mMatchCityPresenter;
    private int mTotal;

    public static MatchCityFragment newInstance(int flag) {
        MatchCityFragment fragment = new MatchCityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.FLAG, flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_citylist_layout;
    }

    @Override
    protected MatchCityPresenter initPresent() {
        mMatchCityPresenter = new MatchCityPresenter();
        if (getArguments() != null) {
            flag = getArguments().getInt(IntentKey.FLAG);
            String name = "";
            switch (flag) {
                case Constans.CITY:
                    name = "城市";
                    mMatchCityPresenter.setFlag(ParamsKey.MATCH_ORG_CITY);
                    break;
                case Constans.SCHOOL:
                    name = "学校";
                    mMatchCityPresenter.setFlag(ParamsKey.MATCH_ORG_SCHOOL);
                    break;
                case Constans.BUSINESS:
                    name = "券商";
                    mMatchCityPresenter.setFlag(ParamsKey.MATCH_ORG_BUSINESS);
                    break;
            }
            tvName.setText(name);
        }
        return mMatchCityPresenter;
    }

    private Handler mHandler = new Handler();

    @Override
    protected void initView() {
        plvMatchList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ShowOrHideProgressDialog.showProgressDialog(getActivity(),getActivity(),getString(R.string.hard_loading));
                //清空list数据
                if (adapter.getList() != null && adapter.getList().size() > 0) {
                    adapter.clearList();
                }
                page = ParamsKey.D_PAGE;
                mMatchCityPresenter.getCityList(flag,page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                if (mTotal > adapter.getList().size()) {
                    page += 1;
                    mMatchCityPresenter.getCityList(flag,page);
                    ShowOrHideProgressDialog.setProgressDialogMsg(getString(R.string.loading));
                    ShowOrHideProgressDialog.showProgressDialog();
                } else {
                    plvMatchList.setRefreshingLabel("没有更多...");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            plvMatchList.onRefreshComplete();
                        }
                    },1000);
                }
            }
        });
        plvMatchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowActivity.showMatch3(getActivity(), adapter.getItem(position - 1));
            }
        });
    }

    @Override
    protected void initData() {
        ShowOrHideProgressDialog.showProgressDialog(getActivity(),getActivity(),getString(R.string.hard_loading));
        adapter = new MatchCityAdapter(getActivity());
        plvMatchList.setAdapter(adapter);
    }

    @Override
    public void RushCityList(MatchList3Bean.Result result) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (plvMatchList.isRefreshing()) {
            plvMatchList.onRefreshComplete();
        }
        if (result != null) {
            page = result.getPage();
            List<MatchList3Bean.Result.Matches> matches = result.getMatches();
            mTotal = result.getTotal();
            if (adapter.getList() == null) {
                adapter.setList(matches);
            } else {
                if (mTotal > adapter.getList().size()) {
                    adapter.addList(matches);
                }
            }
        }
    }

    @Override
    public void RushCityErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)){
            if (ShowActivity.checkLoginStatus(getActivity(), msg)){
                showToast(getString(R.string.login_err_tip));
            }
        }
    }

}

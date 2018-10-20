package com.zbmf.StocksMatch.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.AddStockActivity;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.adapter.MatchMyStockAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.bean.StockholdsBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IStockFragmentView;
import com.zbmf.StocksMatch.listener.RemarkBtnClick;
import com.zbmf.StocksMatch.presenter.StockFragmentPresenter;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.util.ToastUtils;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/11/27.
 * 自选股
 */

public class StockFragment extends BaseFragment<StockFragmentPresenter> implements IStockFragmentView
        , RemarkBtnClick {
    @BindView(R.id.pull_listview)
    PullToRefreshListView pullListview;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.no_message_text)
    TextView noMessageText;
    private MatchMyStockAdapter adapter;
    private StockFragmentPresenter mStockFragmentPresenter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://item的删除
                    mArg1 = msg.arg1;
                    mStockFragmentPresenter.deleteStockItem(adapter.getItem(mArg1).getSymbol());
                    adapter.notifyDataSetChanged();
                    break;
                case 2://item的点击
                    int arg2 = msg.arg2;
                    StockholdsBean holder = adapter.getItem(arg2);
                    ShowActivity.showStockDetail2(getActivity(), new StockMode(holder.getName(), holder.getSymbol()), Constans.MATCH_ID);
                    break;
            }
        }
    };
    private int mArg1;

    public static StockFragment newInstance() {
        return new StockFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_stock_layout;
    }

    @Override
    protected StockFragmentPresenter initPresent() {
        mStockFragmentPresenter = new StockFragmentPresenter();
        return mStockFragmentPresenter;
    }

    @Override
    protected void initView() {
        setTitleMessage(getString(R.string.stock));
    }

    @Override
    protected void initData() {
        llNone.setVisibility(View.VISIBLE);
        pullListview.setVisibility(View.GONE);
        ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
        tvTitleRight.setVisibility(View.VISIBLE);
        adapter = new MatchMyStockAdapter(getActivity(), mHandler);
        adapter.setRemarkBtnClick(this);
        pullListview.setAdapter(adapter);
        //嵌套了item删除的控件后，item的点击也相应的与第三方控件绑定
//        pullListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                StockholdsBean holder = adapter.getItem(position - 1);
//                ShowActivity.showStockDetail2(getActivity(), new StockMode(holder.getName(), holder.getSymbol()));
//            }
//        });
        pullListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getList() != null && adapter.getList().size() > 0) {
                    adapter.clearList();
                }
                getPresenter().setFirst(true);
                getPresenter().getDatas();
                ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (total > adapter.getList().size()) {
                    getPresenter().loadMore();
                } else {
                    showToast(getString(R.string.nomore_loading));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullListview.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }

    private List<StockholdsBean> newList = new ArrayList<>();
    private int total;
    private int page;

    @Override
    public void onRushStockList(List<StockholdsBean> stockholdsBeans, int mTotal, int page) {
        if (stockholdsBeans != null && stockholdsBeans.size() > 0) {
            llNone.setVisibility(View.GONE);
            pullListview.setVisibility(View.VISIBLE);
            this.total = mTotal;
            this.page = page;
            ShowOrHideProgressDialog.disMissProgressDialog();
            if (pullListview != null && pullListview.isRefreshing()) {
                pullListview.onRefreshComplete();
            }
            if (adapter.getList() == null) {
                adapter.setList(stockholdsBeans);
            } else {
                int size = adapter.getList().size();
                if (page == ParamsKey.D_PAGE && size > 0) {
                    newList.addAll(stockholdsBeans);
                    adapter.clearList();
                    adapter.addList(newList);
                    adapter.notifyDataSetChanged();
                    newList.clear();
                } else if (mTotal > size) {
                    adapter.addList(stockholdsBeans);
                    adapter.notifyDataSetChanged();
                }
            }
        }else {
            noMessageText.setText(getString(R.string.no_data));
            llNone.setVisibility(View.VISIBLE);
            pullListview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFail(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            if (!TextUtils.isEmpty(msg)) {
                if (ShowActivity.checkLoginStatus(getActivity(), msg)) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putInt(ParamsKey.FG_FLAG,ParamsKey.FG_STOCK_V);
                            ShowActivity.showActivity(getActivity(),bundle, LoginActivity.class);
//                            showToast(getString(R.string.need_login));
                            ToastUtils.rectangleSingleToast(getString(R.string.need_login));
                            getActivity().finish();
                        }
                    }, Constans.DELAY_TIME);
                }
            }
        }
    }

    @Override
    public void deleteStockStatus(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
            if (msg.equals("删除成功")) {
                adapter.removeList(mArg1);
            }
        }
    }

    //添加自选股
    @OnClick(R.id.tv_title_right)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title_right:
                startActivityForResult(new Intent(getActivity(),
                        AddStockActivity.class), IntentKey.ADD_STOCK_RESULT_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.ADD_STOCK_RESULT_CODE && resultCode == IntentKey.ADD_STOCK_RESULT_CODE
                /*&& data != null*/) {
            mStockFragmentPresenter.getMyStock(ParamsKey.D_PAGE);
        }
    }

    //跳转到备注列表页面
    @Override
    public void skipRemarkList(StockholdsBean stockHoldsBean) {
        ShowActivity.showRemarkListActivity(getActivity(), stockHoldsBean);
    }
}

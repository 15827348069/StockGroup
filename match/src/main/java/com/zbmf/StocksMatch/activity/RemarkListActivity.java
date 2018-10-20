package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.StockRemarkListAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.StockRemarkListBean;
import com.zbmf.StocksMatch.bean.StockholdsBean;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IStockRemarkView;
import com.zbmf.StocksMatch.listener.RemarkEditStr;
import com.zbmf.StocksMatch.presenter.StockRemarkListPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.BottomEditDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 备注列表页同新增自选股页面共用一个presenter
 */
public class RemarkListActivity extends BaseActivity<StockRemarkListPresenter> implements IStockRemarkView, RemarkEditStr {
    @BindView(R.id.remark_stock_name)
    TextView remark_stock_name;
    @BindView(R.id.stock_code)
    TextView stock_code;
    @BindView(R.id.remark_count)
    TextView remark_count;
    @BindView(R.id.plv_match_list)
    PullToRefreshListView plv_match_list;
    @BindView(R.id.input_remark)
    TextView input_remark;
    private StockholdsBean mStockholdsBean;
    private StockRemarkListAdapter mStockRemarkListAdapter;
    private int mPage;
    private int mTotal = 0;
    private StockRemarkListPresenter mStockRemarkListPresenter;
    private BottomEditDialog mBottomEditDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_remark_list;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.remark);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            mStockholdsBean = (StockholdsBean) bundle.getSerializable(IntentKey.REMARK_BEAN_LIST);
        }
        remark_stock_name.setText(mStockholdsBean.getName());
        stock_code.setText(mStockholdsBean.getSymbol());
        remark_count.setText("共" + mTotal + "条备注");
        mStockRemarkListAdapter = new StockRemarkListAdapter(this);
        plv_match_list.setAdapter(mStockRemarkListAdapter);
        plv_match_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mStockRemarkListAdapter.getList() != null) {
                    mStockRemarkListAdapter.clearList();
                }
                mStockRemarkListPresenter.setFirst(true);
                getPresenter().getDatas();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mTotal > mStockRemarkListAdapter.getList().size()) {
                    mPage += 1;
                    mStockRemarkListPresenter.stockRemarkList(mStockholdsBean.getSymbol(), String.valueOf(mPage));
                } else {
                    showToast("没有更多");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            plv_match_list.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    protected StockRemarkListPresenter initPresent() {
        mStockRemarkListPresenter = new StockRemarkListPresenter(mStockholdsBean.getSymbol());
        return mStockRemarkListPresenter;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshStockRemarkList(StockRemarkListBean.Result o) {
        if (plv_match_list.isRefreshing()) {
            plv_match_list.onRefreshComplete();
        }
        mPage = o.getPage();
        mTotal = o.getTotal();
        remark_count.setText("共" + mTotal + "条备注");
        List<StockRemarkListBean.Result.Remarks> remarks = o.getRemarks();
        if (mStockRemarkListAdapter.getList() == null && remarks != null) {
            mStockRemarkListAdapter.setList(remarks);
        } else {
            if (mPage == ParamsKey.D_PAGE) {
                mStockRemarkListAdapter.clearList();
                mStockRemarkListAdapter.addList(remarks);
            } else if (mTotal > mStockRemarkListAdapter.getList().size()) {
                mStockRemarkListAdapter.addList(remarks);
            }
        }
    }

    @Override
    public void refreshStockRemarkListStatus(String msg) {
        DissLoading();
        showToast(msg);
    }

    @Override
    public void addStockRemarkStatus(String msg) {
        DissLoading();
        if (msg.equals(getString(R.string.add_remark_success))) {
            mStockRemarkListPresenter.stockRemarkList(mStockholdsBean.getSymbol(), String.valueOf(ParamsKey.D_PAGE));
        }
        showToast(msg);
    }

    @Override
    public void deleteStockRemarkStatus(String msg) {
        DissLoading();
        showToast(msg);
    }

    @OnClick({R.id.input_remark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.input_remark:
                getNewRemark();
                break;
        }
    }

    private void getNewRemark() {
        if (mBottomEditDialog == null) {
            mBottomEditDialog = new BottomEditDialog(this, R.style.My_Edit_Dialog);
        }
        //添加备注
        mBottomEditDialog.getDialog().showI().setOnAddClick(this);
    }

    @Override
    public void editRemark(String newRemark) {
        //获取到添加的备注信息，执行添加备注的网络请求
        if (TextUtils.isEmpty(newRemark)) {
            showToast(getString(R.string.warn_tip));
            return;
        }
        mStockRemarkListPresenter.addStockRemark(mStockholdsBean.getSymbol(), newRemark);
        mBottomEditDialog.dissI();
    }
}

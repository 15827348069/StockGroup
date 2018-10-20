package com.zbmf.StocksMatch.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.OrderContentListAdapter;
import com.zbmf.StocksMatch.adapter.OrderTitleListAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.OrderList;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.DialogYesClick;
import com.zbmf.StocksMatch.listener.ITrustListView;
import com.zbmf.StocksMatch.presenter.MyTrustPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.TextDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.view.ListViewForScrollView;
import com.zbmf.worklibrary.view.SyncHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MatchTrustsActivity extends BaseActivity<MyTrustPresenter> implements ITrustListView,AdapterView.OnItemClickListener{
    @BindView(R.id.matchOperate_rightTitleScrollView)
    SyncHorizontalScrollView matchOperateRightTitleScrollView;
    @BindView(R.id.matchOperate_leftTitleListView)
    ListViewForScrollView matchOperateLeftTitleListView;
    @BindView(R.id.matchOperate_rightContentListView)
    ListViewForScrollView matchOperateRightContentListView;
    @BindView(R.id.matchOperate_rightContentScrollView)
    SyncHorizontalScrollView matchOperateRightContentScrollView;
    @BindView(R.id.myscrllview)
    PullToRefreshScrollView myscrllview;
    @BindView(R.id.no_message_text)
    TextView noMessage;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    private MatchInfo mMatchInfo;
    private int position;
    private OrderTitleListAdapter mTitleAdapter;
    private OrderContentListAdapter mHoldAdapter;
    private int mPage;
    private int mTotal;
    private MyTrustPresenter mMyTrustPresenter;
    private String mMatchID;

    @Override
    protected int getLayout() {
        return R.layout.activity_match_holds;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.my_order);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            mMatchInfo = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_INFO);
            mMatchID = bundle.getString(IntentKey.MATCH_ID);
            matchOperateRightTitleScrollView.setScrollView(matchOperateRightContentScrollView);
            matchOperateRightContentScrollView.setScrollView(matchOperateRightTitleScrollView);
            mTitleAdapter = new OrderTitleListAdapter(this);
            matchOperateLeftTitleListView.setAdapter(mTitleAdapter);
            matchOperateLeftTitleListView.setOnItemClickListener(this);
            mHoldAdapter = new OrderContentListAdapter(this);
            matchOperateRightContentListView.setAdapter(mHoldAdapter);
            matchOperateRightContentListView.setOnItemClickListener(this);
            myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    if (mTitleAdapter.getList()!=null&&mHoldAdapter.getList()!=null){
                        mTitleAdapter.clearList();
                        mHoldAdapter.clearList();
                    }
                    mPage=ParamsKey.D_PAGE;
                    getPresenter().setFirst(true);
                    getPresenter().getDatas();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    if (mTotal>mTitleAdapter.getList().size()){
                        mPage+=1;
                        mMyTrustPresenter.getMyOrderList(mMatchID,String.valueOf(mPage));
                    }else {
                        showToast(getString(R.string.nomore_loading));
                        myscrllview.onRefreshComplete();
                    }
                }
            });
            setTitleView();
        }
    }

    @Override
    protected MyTrustPresenter initPresent() {
        mMyTrustPresenter = new MyTrustPresenter(mMatchID);
        return mMyTrustPresenter;
    }

    private void setTitleView(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        String rightTitles[]=getResources().getStringArray(R.array.order_list);
        for (int i = 0; i < rightTitles.length; i++) {
            View convertView = LayoutInflater.from(MatchTrustsActivity.this).inflate(R.layout.match_title_layout, null);
            TextView title = convertView.findViewById(R.id.trusts_cell_name);
            title.setText(rightTitles[i]);
            layout.addView(convertView);
        }
        matchOperateRightTitleScrollView.addView(layout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderList.Result.Stocks stocks = (OrderList.Result.Stocks) parent.getItemAtPosition(position);
        showDialog(stocks,position);
    }
    private List<OrderList.Result.Stocks> mStocksList=new ArrayList<>();
    @Override
    public void getTrustList(OrderList.Result result) {
        if(myscrllview!=null&&myscrllview.isRefreshing()){
            myscrllview.onRefreshComplete();
        }
        if (result!=null){
            mPage = result.getPage();
            mTotal = result.getTotal();
            if (mTotal==0){
//                myscrllview.setVisibility(View.GONE);
                noMessage.setText(getString(R.string.no_trust_record));
                llNone.setVisibility(View.VISIBLE);
                return;
            }
            List<OrderList.Result.Stocks> stocks = result.getStocks();
            setAdapterData(mTitleAdapter,stocks);
            setAdapterData(mHoldAdapter,stocks);
        }
    }

    @Override
    public void err(String msg) {
        if (!TextUtils.isEmpty(msg)){
            showToast(msg);
        }
    }

    @Override
    public void revokeResult(String msg) {
        if (!TextUtils.isEmpty(msg)){
            showToast(msg);
            if (msg.equals("撤单成功!")){
                mTitleAdapter.removeList(position);
                mHoldAdapter.removeList(position);
            }
        }
    }

    private void setAdapterData(ListAdapter adapter,List<OrderList.Result.Stocks> stocks ){
        if (adapter instanceof OrderTitleListAdapter){
            if (mTitleAdapter.getList()==null){
                mTitleAdapter.setList(stocks);
            }else {
                if (mPage== ParamsKey.D_PAGE){
                    mStocksList.addAll(stocks);
                    mTitleAdapter.clearList();
                    mTitleAdapter.addList(mStocksList);
                    mStocksList.clear();
                }else {
                    mTitleAdapter.addList(stocks);
                }
            }
        }else if (adapter instanceof OrderContentListAdapter){
            if (mHoldAdapter.getList()==null){
                mHoldAdapter.setList(stocks);
            }else {
                if (mPage== ParamsKey.D_PAGE){
                    mStocksList.addAll(stocks);
                    mHoldAdapter.clearList();
                    mHoldAdapter.addList(mStocksList);
                    mStocksList.clear();
                }else {
                    mHoldAdapter.addList(stocks);
                }
            }
        }
    }

    private void showDialog(final OrderList.Result.Stocks stocks,int position) {
        this.position=position;
        TextDialog.createDialog(this)
                .setMessage(getString(R.string.revoke)+stocks.getName())
                .setLeftButton(getString(R.string.cancel))
                .setRightButton(getString(R.string.revoke))
                .setRightClick(new DialogYesClick() {
                    @Override
                    public void onYseClick() {
                        mMyTrustPresenter.revoke(stocks.getId(), mMatchID);
                    }
                })
                .show();
    }
}

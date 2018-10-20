package com.zbmf.StocksMatch.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.LeftTitleAdapter;
import com.zbmf.StocksMatch.adapter.RightContentAdapter;
import com.zbmf.StocksMatch.adapter.TrustsAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.Stock1;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.listener.DialogListener;
import com.zbmf.StocksMatch.listener.DialogListener1;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CustomListView;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.SyncHorizontalScrollView;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshScrollView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MatchHoldActivity extends ExActivity {
    private PullToRefreshScrollView myscrllview;
    private CustomListView leftTitleListView, rightContentListView;
    private SyncHorizontalScrollView rightTitleScrollView, rightContentScrollView;
    private TextView tv_title;
    private Get2Api server = null;
    private static final int PAGE_SIZE = 20;//每页显示数量
    private static int PAGE_INDEX = 1;//当前页码
    private List<StockholdsBean> list = new ArrayList<StockholdsBean>();
    String[] rightTitles = {"持仓股数", "可用股", "成本价", "当前价", "浮动盈亏", "盈亏比率"};
    private RightContentAdapter adapter;
    private LeftTitleAdapter adapter1;
    private MatchBean matchBean;
    private Group group;//查看别人的持仓数据
    private String user_id = UiCommon.INSTANCE.getiUser().getUser_id();
    private boolean isFirst=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_trusts);

        getData();
        setupView();
    }

    private void getData() {
        user_id = UiCommon.INSTANCE.getiUser().getUser_id();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchBean = (MatchBean) bundle.getSerializable("matchbean");
            group = (Group)bundle.getSerializable("group");
            if (group != null)
                user_id = group.getId();
        }
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.my_hold);
        myscrllview = (PullToRefreshScrollView)findViewById(R.id.myscrllview);
        myscrllview.setMode(PullToRefreshBase.Mode.BOTH);
        rightTitleScrollView = (SyncHorizontalScrollView) findViewById(R.id.matchOperate_rightTitleScrollView);
        rightContentScrollView = (SyncHorizontalScrollView) findViewById(R.id.matchOperate_rightContentScrollView);
        rightTitleScrollView.setScrollView(rightContentScrollView);
        rightContentScrollView.setScrollView(rightTitleScrollView);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetOrderList(MatchHoldActivity.this).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetOrderList(MatchHoldActivity.this).execute(DataLoadDirection.LoadMore,PAGE_INDEX);
            }
        });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < rightTitles.length; i++) {
            View convertView = LayoutInflater.from(MatchHoldActivity.this).inflate(R.layout.trusts_single_cell, null);
            TextView title = (TextView) convertView.findViewById(R.id.trusts_cell_name);

            title.setText(rightTitles[i]);
            layout.addView(convertView);
        }
        rightTitleScrollView.addView(layout);

        leftTitleListView = (CustomListView) findViewById(R.id.matchOperate_leftTitleListView);
        rightContentListView = (CustomListView) findViewById(R.id.matchOperate_rightContentListView);
        adapter = new RightContentAdapter(this);
        adapter.setList(list);
        adapter1 = new LeftTitleAdapter(this);
        adapter1.setList(list);

        rightContentListView.setAdapter(adapter);
        leftTitleListView.setAdapter(adapter1);


        // 设置左侧股票名称触发事件
        leftTitleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                showBuyDiaog((StockholdsBean)arg0.getItemAtPosition(arg2));
            }
        });
        // 设置右侧股票内容触发事件
        rightContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                showBuyDiaog((StockholdsBean)arg0.getItemAtPosition(arg2));
            }
        });


    }

    int opreat=1;
    public void showBuyDiaog(final StockholdsBean stockholdbean){

        UiCommon.INSTANCE.showDialog(this, stockholdbean.getName(), new DialogListener1() {
            @Override
            public void onCancl(Dialog dialog) {
                dialog.cancel();
            }

            @Override
            public void onConfirm(Dialog dialog) {//sell
                opreat=2;
                new GetStockRealtimeInfoTask(MatchHoldActivity.this,R.string.stock_detail_getting,R.string.load_fail,true).execute(stockholdbean.getSymbol());
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("matchbean", matchBean);
//                bundle.putSerializable("stockholdbean", stockholdbean);
//                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_SALE, bundle);
                dialog.cancel();
            }

            @Override
            public void onConfirm1(Dialog dialog) {//buy
                opreat = 1;
                new GetStockRealtimeInfoTask(MatchHoldActivity.this,R.string.stock_detail_getting,R.string.load_fail,true).execute(stockholdbean.getSymbol());
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("matchbean", matchBean);
//                bundle.putSerializable("stockholdbean", stockholdbean);
//                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_BUY, bundle);
                dialog.cancel();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFirst)
            showDialog(this,R.string.loading);
            new GetOrderList(this).execute(DataLoadDirection.Refresh, 1);

    }

    private class GetOrderList extends LoadingDialog<Integer, StockholdsBean> {

        private int operation;
        private int page;
        private List<StockholdsBean> tempList;

        public GetOrderList(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public StockholdsBean doInBackground(Integer... params) {
            operation = params[0];
            page = params[1];
            StockholdsBean ret = null;

            if (operation == DataLoadDirection.Refresh) {
                page = 1;
            } else {
                page++;
            }
            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getHoldlist(matchBean.getId(), user_id, page, PAGE_SIZE);
            } catch (JSONException e) {
                e.printStackTrace();
            }




            return ret;
        }

        @Override
        public void onPostExecute(StockholdsBean ret) {
            super.onPostExecute(ret);
            myscrllview.onRefreshComplete();
            DialogDismiss();isFirst=false;
        }

        @Override
        public void doStuffWithResult(StockholdsBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    tempList = result.getInfolist();
                    if(tempList.size()>0){
                        if (operation == DataLoadDirection.Refresh)
                            list.clear();
                        adapter.addList(tempList);
                        adapter1.notifyDataSetChanged();
                        if (page != result.getPages()) {
                            //还有数据
                            myscrllview.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            myscrllview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }

                        PAGE_INDEX = page;
                    }else{
                        UiCommon.INSTANCE.showTip(getString(R.string.hold_tip));
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    private class GetStockRealtimeInfoTask extends LoadingDialog<String, Stock> {

        public GetStockRealtimeInfoTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public Stock doInBackground(String... params) {
            if (server == null) {
                server = new Get2ApiImpl();
            }
            try {
                if(opreat==1)
                     return server.getStockRealtimeInfo(params[0]);
                else
                     return server.getRealtimeInfo(matchBean.getId(),params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(Stock result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("matchbean", matchBean);
                    bundle.putSerializable("stock",result);
//                    bundle.putSerializable("stockholdbean", stockholdbean);
                    if(opreat==1)
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_BUY, bundle);
                    else
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_SALE, bundle);
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

}

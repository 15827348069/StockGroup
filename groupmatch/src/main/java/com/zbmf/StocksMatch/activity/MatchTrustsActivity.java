package com.zbmf.StocksMatch.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.LeftTitleAdapter;
import com.zbmf.StocksMatch.adapter.TrustsAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.listener.DialogListener;
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

public class MatchTrustsActivity extends ExActivity {
    private PullToRefreshScrollView myscrllview;
    private CustomListView leftTitleListView, rightContentListView;
    private SyncHorizontalScrollView rightTitleScrollView, rightContentScrollView;
    private TextView tv_title;
    private Get2Api server = null;
    private static final int PAGE_SIZE = 20;//每页显示数量
    private static int PAGE_INDEX = 1;//当前页码
    private List<StockholdsBean> list = new ArrayList<StockholdsBean>();
    String[] rightTitles = {"委托类型", "数量", "委托价格", "冻结金额", "委托时间"};
    private TrustsAdapter adapter;
    private LeftTitleAdapter adapter1;
    private MatchBean matchBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_trusts);

        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchBean = (MatchBean) bundle.getSerializable("matchbean");
        }
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.my_weituo);
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

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < rightTitles.length; i++) {
            View convertView = LayoutInflater.from(MatchTrustsActivity.this).inflate(R.layout.trusts_single_cell, null);
            TextView title = (TextView) convertView.findViewById(R.id.trusts_cell_name);

            title.setText(rightTitles[i]);
            layout.addView(convertView);
        }
        rightTitleScrollView.addView(layout);

        leftTitleListView = (CustomListView) findViewById(R.id.matchOperate_leftTitleListView);
        rightContentListView = (CustomListView) findViewById(R.id.matchOperate_rightContentListView);
        adapter = new TrustsAdapter(this);
        adapter.setList(list);
        adapter1 = new LeftTitleAdapter(this);
        adapter1.setList(list);
        leftTitleListView.setAdapter(adapter1);
        rightContentListView.setAdapter(adapter);

        rightContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                StockholdsBean sb = (StockholdsBean) arg0.getItemAtPosition(arg2);

                showDialog(sb);
            }
        });
        leftTitleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                StockholdsBean sb = (StockholdsBean) arg0.getItemAtPosition(arg2);

                showDialog(sb);

            }
        });


        if (matchBean != null) {
            if(isFirst){
                showDialog(this,R.string.loading);
            }
            new GetOrderList(this).execute(DataLoadDirection.Refresh, 1);
        }

        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetOrderList(MatchTrustsActivity.this).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetOrderList(MatchTrustsActivity.this).execute(DataLoadDirection.LoadMore, PAGE_INDEX);
            }
        });
    }
    private boolean isFirst = true;

    private void showDialog(final StockholdsBean sb) {
        final String tip2 = "卖出[" + sb.getName() + "]" + sb.getVolumn() + "股";
        UiCommon.INSTANCE.showDialog(MatchTrustsActivity.this, R.string.revoke_tip, tip2, new DialogListener() {

            @Override
            public void onConfirm(Dialog dialog) {
                new Withdraw(MatchTrustsActivity.this, R.string.withdrawing, R.string.withdraw_tip2, true).execute(matchBean.getId(), sb.getId());
                dialog.cancel();
            }

            @Override
            public void onCancl(Dialog dialog) {
               dialog.cancel();
            }
        }, R.string.revoke, R.string.cancel);
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
                ret = server.getOrderList(matchBean.getId(), page, PAGE_SIZE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void onPostExecute(StockholdsBean ret) {
            super.onPostExecute(ret);
            myscrllview.onRefreshComplete();
            isFirst = false;DialogDismiss();
        }

        @Override
        public void doStuffWithResult(StockholdsBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    tempList = result.getInfolist();
                    if (tempList.size() > 0) {
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
                    } else {
                        UiCommon.INSTANCE.showTip(getString(R.string.trust_tip));
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    private class Withdraw extends LoadingDialog<String, General> {

        String id;
        public Withdraw(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public General doInBackground(String... params) {
            id = params[1];
            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                return server.withdraw(params[0], id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(General result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    for(int index=0;index<list.size();index++){
                        if(id.equals(list.get(index).getId())){
                            list.remove(index);
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    adapter1.notifyDataSetChanged();
                    UiCommon.INSTANCE.showTip(getString(R.string.withdraw_tip1));
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }
}

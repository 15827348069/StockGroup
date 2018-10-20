package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.AllMatchAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.receiver.ApplySuccessReceiver;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.utils.UiHelper;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有比赛
 *
 * @author Administrator
 */
public class AllMatchActivity extends ExActivity implements View.OnClickListener {
    private TextView tv_title;
//    private CustomListView listView;
    private PullToRefreshListView listview;
    private boolean isFirstIn = true;
    private static final int PAGE_SIZE = 15;
    private static int PAGE_INDEX = 1;
    private List<MatchBean> matchList = new ArrayList<MatchBean>();//全部比赛列表
    private Get2Api server = null;
    //    private RecommendMatchAdapter adapter;
    private AllMatchAdapter adapter;
    private ApplySuccessReceiver receiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_match);

        setupView();
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.all_match));
        listview = (PullToRefreshListView) findViewById(R.id.content_view);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.ll_search).setOnClickListener(this);
//        findViewById(R.id.rl_top).setOnClickListener(this);
        /*myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetAllMatch(AllMatchActivity.this).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetAllMatch(AllMatchActivity.this).execute(DataLoadDirection.LoadMore, PAGE_INDEX);
            }
        });*/

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetAllMatch(AllMatchActivity.this).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetAllMatch(AllMatchActivity.this).execute(DataLoadDirection.LoadMore, PAGE_INDEX);
            }
        });

//        adapter = new RecommendMatchAdapter(this);
        adapter = new AllMatchAdapter(this);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MatchBean matchbean = (MatchBean) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("matchbean", matchbean);
                if ("1".equals(matchbean.getIs_match_player()))//已参赛
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDESC, bundle);
                else
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDETAIL, bundle);
            }
        });

        if(isFirstIn)
            showDialog(this,R.string.loading);
        new GetAllMatch(this).execute(DataLoadDirection.Refresh, PAGE_INDEX);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiver == null){
            receiver = new ApplySuccessReceiver();
            UiHelper.RegistBroadCast(this, receiver, Constants.APPLY_SUCCESS);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHelper.UnRegistBroadCast(this,receiver);
    }

    public void updateUi(String matchid) {
        for(MatchBean matchBean : matchList){
            if(matchid.equals(matchBean.getId())){
                matchBean.setIs_match_player("1");
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class GetAllMatch extends LoadingDialog<Integer, MatchBean> {

        private int operation;
        private int page;

        public GetAllMatch(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public MatchBean doInBackground(Integer... params) {
            operation = params[0];
            page = params[1];
            MatchBean ret = null;

            if (operation == DataLoadDirection.Refresh) {
                page = 1;
            } else {
                page++;
            }
            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getMathNoStopList(page, PAGE_SIZE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ret;
        }

        @Override
        public void onPostExecute(MatchBean ret) {
            super.onPostExecute(ret);
            listview.onRefreshComplete();
            if(isFirstIn){
                isFirstIn=false;DialogDismiss();
            }
        }

        @Override
        public void doStuffWithResult(MatchBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    if (result.getList() != null) {
                        if (operation == DataLoadDirection.Refresh) {
                            matchList = result.getList();
                            adapter.setList(matchList);
                        } else {
                            adapter.addList(result.getList());
                        }
                        if (page != result.getPages()) {
                            //还有数据
                            listview.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        PAGE_INDEX = page;
                    }
//                    adapter.addList(tempList);
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(AllMatchActivity.this.getString(R.string.load_fail));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_search://发现更多比赛

                break;
//            case R.id.rl_top://头部特殊比赛
//
//                break;
        }
    }
}

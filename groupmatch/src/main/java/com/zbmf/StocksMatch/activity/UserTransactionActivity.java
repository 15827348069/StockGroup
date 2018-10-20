package com.zbmf.StocksMatch.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MatchAdapter1;
import com.zbmf.StocksMatch.adapter.UserTransAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.StockholdsBean;
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


public class UserTransactionActivity extends ExActivity{
    private PullToRefreshListView content_view;
    private TextView tv_title;
    private Get2Api server = null;
    private static final int PAGE_SIZE = 12;//每页显示数量
    private static int PAGE_INDEX = 1;//当前页码
    private UserTransAdapter adapter;
    private List<StockholdsBean> list = new ArrayList<StockholdsBean>();
    private MatchBean matchBean;
    private MatchAdapter1 adapter1 = new MatchAdapter1(this);
    private List<MatchBean> mlist ;

    private Group group;
    private String user_id = UiCommon.INSTANCE.getiUser().getUser_id();
    private ApplySuccessReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_transaction);

        getData();
        setupView();
    }

    private void getData() {
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
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(R.string.jyjl);
        content_view = (PullToRefreshListView) findViewById(R.id.content_view);
        content_view.setMode(PullToRefreshBase.Mode.BOTH);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new UserTransAdapter(this);
        adapter.setList(list);
        content_view.setAdapter(adapter);
        content_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockholdsBean sb = (StockholdsBean) parent.getItemAtPosition(position);
                if ("0".equals(sb.getIs_show())) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("match_id", matchBean.getId());
                    bundle.putSerializable("sb", sb);
                    bundle.putSerializable("group", group);
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_PAYTIP, bundle);
                } else if (sb.getType().equals("2")) {
                    showDialog(sb, UserTransactionActivity.this);
                } else {

                }
            }
        });
        content_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetOrderList(UserTransactionActivity.this).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetOrderList(UserTransactionActivity.this).execute(DataLoadDirection.LoadMore, PAGE_INDEX);
            }
        });
        adapter.setMatch_id(matchBean.getId());
        if(isFirstIn)
            showDialog(this,R.string.trans_getting);
        new GetOrderList(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, DataLoadDirection.Refresh, 1);
        new GetMyMatch(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiver == null)
            receiver = new ApplySuccessReceiver();
        UiHelper.RegistBroadCast(this,receiver,Constants.PAY_SUCCESS);
    }

    private void showDialog(final StockholdsBean stockholdsBean,Context cxt){
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View view = View.inflate(cxt, R.layout.dialog_select_match, null);
        dialog.setContentView(view);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (UiCommon.widthPixels * 0.8);
        lp.height = (int)(UiCommon.higthPixels*0.65);
        win.setAttributes(lp);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        adapter1.setList(mlist);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MatchBean mm = (MatchBean) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("matchbean", mm);
                bundle.putSerializable("stockholdbean", stockholdsBean);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_BUY, bundle);

                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tvConfirm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

        dialog.show();
    }



    private boolean isFirstIn = true;

    public void updateUi(String symbol) {
        for(StockholdsBean stockholdsBean : list){
            if(symbol.equals(stockholdsBean.getSymbol())){
                stockholdsBean.setType("2");
                stockholdsBean.setIs_show("1");
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class GetOrderList extends LoadingDialog<Integer, StockholdsBean> {

        private int operation;
        private int page;

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
                ret = server.getDeallogList(matchBean.getId(),user_id, page, PAGE_SIZE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void onPostExecute(StockholdsBean ret) {
            super.onPostExecute(ret);
           content_view.onRefreshComplete();
            isFirstIn = false;
            DialogDismiss();
        }

        @Override
        public void doStuffWithResult(StockholdsBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    if(result.getInfolist()!=null){
                        if(result.getInfolist().size()>0){
                            if (operation == DataLoadDirection.Refresh)
                                list.clear();
                            adapter.addList(result.getInfolist());
                        }else{
//                            UiCommon.INSTANCE.showTip(getString(R.string.trans_tip));
                        }

                        /*if (page != result.getPages()) {
                            //还有数据
                            content_view.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            content_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }*/
                        if(page>result.getPages())
                            UiCommon.INSTANCE.showTip(getString(R.string.load_comlete));
                        PAGE_INDEX = page;
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    private class GetMyMatch extends LoadingDialog<Integer, MatchBean> {
        public GetMyMatch(Context activity) {
            super(activity, false, true);
        }

        @Override
        public MatchBean doInBackground(Integer... params) {
            MatchBean ret = null;
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                ret = server.getRunMatches();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void doStuffWithResult(MatchBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(result.getList().size()>0){
                        mlist = result.getList();
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHelper.UnRegistBroadCast(this,receiver);
    }
}

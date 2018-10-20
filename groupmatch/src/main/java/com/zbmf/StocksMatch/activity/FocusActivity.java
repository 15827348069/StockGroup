package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.FocusAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.receiver.AccountReceiver;
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

public class FocusActivity extends ExActivity{
    private PullToRefreshListView content_view;
    private TextView tv_title;
    private FocusAdapter adapter;
    private List<Group> list = new ArrayList<Group>();
    private Get2Api server = null;
    private static final int PAGE_SIZE = 15;//每页显示数量
    private static int PAGE_INDEX = 1;//当前页码
    private static int PAGGS=1;//总页数
    private AccountReceiver receiver;
    private View footView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        footView = View.inflate(this,R.layout.pull_to_refresh_foot,null);
        setupView();

    }

    private void setupView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(R.string.myfocus);
        content_view = (PullToRefreshListView) findViewById(R.id.content_view);
        content_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new FocusAdapter(this);
        adapter.setList(list);

        content_view.getRefreshableView();
        content_view.setAdapter(adapter);
        content_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = (Group) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", group);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_USER, bundle);
            }
        });

        /*content_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetUserGroups(FocusActivity.this).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetUserGroups(FocusActivity.this).execute(DataLoadDirection.LoadMore, PAGE_INDEX);
            }
        });*/

        content_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetUserGroups(FocusActivity.this).execute(DataLoadDirection.Refresh, 1);
            }
        });

        content_view.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(PAGE_INDEX < PAGGS){
                    addFootView(content_view,footView);
                    new GetUserGroups(FocusActivity.this).execute(DataLoadDirection.LoadMore, PAGE_INDEX);
                }
            }
        });

        new GetUserGroups(this).execute(DataLoadDirection.Refresh, 1);

    }

    private void addFootView(PullToRefreshListView plv,View v){
        ListView listView = plv.getRefreshableView();
        if(listView.getFooterViewsCount() == 1)
            listView.addFooterView(v);
    }

    private void removeFootView(PullToRefreshListView plv,View v){
        ListView listView = plv.getRefreshableView();
        if(listView.getFooterViewsCount() > 1)
             listView.removeFooterView(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFirstIn){
            showDialog(this,R.string.loading);
        }
        if(receiver == null){
            receiver = new AccountReceiver();
            UiHelper.RegistBroadCast(this, receiver, Constants.ACCOUNT_DEL);
        }
    }

    private boolean isFirstIn = true;

    public void updateUi(String gid) {
        for(int i=0;i<list.size();i++){
            if(list.get(i).getGid().equals(gid)){
                list.remove(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHelper.UnRegistBroadCast(this,receiver);
    }

    private class GetUserGroups extends LoadingDialog<Integer, Group> {

        private int operation;
        private int page;

        public GetUserGroups(Context activity){
            super(activity, R.string.loading, R.string.load_fail1,false);
        }


        @Override
        public Group doInBackground(Integer... params) {
            operation = params[0];
            page = params[1];
            Group ret = null;

            if (operation == DataLoadDirection.Refresh) {
                page = 1;
            } else {
                page++;
            }
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getUserGroups(page, PAGE_SIZE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ret;
        }

        @Override
        public void onPostExecute(Group ret) {
            super.onPostExecute(ret);
            content_view.onRefreshComplete();
            isFirstIn = false;
            DialogDismiss();
        }

        @Override
        public void doStuffWithResult(Group result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(result.getList()!=null){
                        if (operation == DataLoadDirection.Refresh)
                            list.clear();
                        adapter.addList(result.getList());
                        /*if (page != result.getPages()) {
                            content_view.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            content_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }*/
                        PAGE_INDEX = page;
                    }
                    PAGGS = result.getPages();

//                    if(page>PAGGS)
//                        UiCommon.INSTANCE.showTip(getString(R.string.load_comlete));

                    if(page == PAGGS){
                        removeFootView(content_view,footView);
                        UiCommon.INSTANCE.showTip(getString(R.string.load_comlete));
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }
}

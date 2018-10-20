package com.zbmf.StockGroup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.FoucusAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.db.DBManager;
import com.zbmf.StockGroup.db.Database;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.R.id.listview;

//关注老师  //个人中心关注
public class CareTeacherActivity extends BaseActivity implements FoucusAdapter.OnCareClink {

    private PullToRefreshListView content_view;
    private FoucusAdapter mFoucusAdapter;
    private List<Group> mGroups = new ArrayList<>();
    private int PAGE_INDEX, PAGGS;
    public static final int Refresh = 1;
    public static final int LoadMore = 2;
    private int type = FoucusAdapter.FOCUS_LIST;
    private LinearLayout ll_none;
    private TextView no_message_text,right_button,left_button;
    private Database db;
    private DBManager dbManager;
    private boolean isFirst=true;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_care_teacher;
    }

    @Override
    public void initView() {
        initTitle("关注老师");
        ll_none = (LinearLayout) findViewById(R.id.ll_none);
        content_view = (PullToRefreshListView) findViewById(listview);
        content_view.setMode(PullToRefreshBase.Mode.BOTH);

        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        right_button=getView(R.id.tv_right_button);
        left_button=getView(R.id.tv_left_button);
        no_message_text.setText(getString(R.string.no_msg));
        right_button.setVisibility(View.VISIBLE);
        right_button.setText(getString(R.string.only_teacher));
        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt(IntentKey.FLAG,2);
                ShowActivity.showActivityForResult(CareTeacherActivity.this,bundle,
                        FindTeacherActivity.class, RequestCode.STUDY);
            }
        });
        left_button.setVisibility(View.VISIBLE);
        left_button.setText(getString(R.string.find_teacher));
        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivityForResult(CareTeacherActivity.this,
                        null, FindTeacherActivity.class, RequestCode.STUDY);
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = bundle.getInt(IntentKey.FLAG);
        }
        dbManager=new DBManager(this);
        db = new Database(this);
        mFoucusAdapter = new FoucusAdapter(this, mGroups, type);
        content_view.setAdapter(mFoucusAdapter);
        unReadNumReceiver = new UnReadNumReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.NEW_LIVE_MSG);
        intentFilter.addAction(Constants.UNREADNUM);
        registerReceiver(unReadNumReceiver,intentFilter);

    }

    @Override
    public void addListener() {
        content_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                userGroups(Refresh);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                userGroups(LoadMore);
            }
        });
        content_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getGroupInfo((Group) parent.getItemAtPosition(position));
            }
        });
        mFoucusAdapter.setOnCareClink(this);

    }


    private UnReadNumReceiver unReadNumReceiver;

    @Override
    public void onCareClink(int position) {
        onCheckedChanged(position);
    }

    private class UnReadNumReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.NEW_LIVE_MSG:
                    for(Group group:mGroups){
                        group.setUnredcount(dbManager.getUnredCount(group.getId()));
                    }
                    break;
                case Constants.UNREADNUM:
                    for(Group group:mGroups){
                        group.setChat(db.getChatUnReadNum(group.getId()));
                    }
                    break;
            }
            mFoucusAdapter.notifyDataSetChanged();
        }
    }

    private void getGroupInfo(Group group) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.GROUP, group);
        ShowActivity.showActivity(CareTeacherActivity.this, bundle, Chat1Activity.class.getName());
    }

    private void userGroups(final int direction) {
        if (direction == Refresh)
            PAGE_INDEX = 1;
        else
            PAGE_INDEX++;

        WebBase.userGroups(PAGE_INDEX, new JSONHandler(isFirst, this, "正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                content_view.onRefreshComplete();
                Group group = JSONParse.userGroups(obj);
                if (group != null) {
                    PAGGS = group.getPages();
                    if (PAGE_INDEX == PAGGS) {
                        content_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        content_view.setMode(PullToRefreshBase.Mode.BOTH);
                    }

                    if (group.getList() != null && group.getList().size() > 0) {
                        List<Group>infolist=group.getList();
                        for(Group group1: infolist){
                            int chatNo = db.getChatUnReadNum(group1.getId());
                            int liveNo = dbManager.getUnredCount(group1.getId());
                            group1.setChat(chatNo);
                            group1.setUnredcount(liveNo);
                        }
                        if (direction == Refresh) {
                            mGroups.clear();
                        }
                        mGroups.addAll(infolist);
                        mFoucusAdapter.notifyDataSetChanged();
                        ll_none.setVisibility(View.GONE);
                    } else
                        ll_none.setVisibility(View.VISIBLE);
                }
                if(isFirst){
                    isFirst=false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if(isFirst){
                    isFirst=false;
                }
            }
        });
    }

    public void onCheckedChanged(int position) {
        final String id = mGroups.get(position).getId();
            WebBase.unfollow(id, new JSONHandler(true, CareTeacherActivity.this, "正在取消关注圈主...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    Toast.makeText(getBaseContext(), "取消关注成功", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < mGroups.size(); i++) {
                        if (mGroups.get(i).getId().equals(id)) {
                            mGroups.remove(i);
                            break;
                        }
                    }
                    mFoucusAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String err_msg) {
                    Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                }
            });
    }
    @Override
    protected void onResume() {
        super.onResume();
        isFirst=true;
        userGroups(Refresh);
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unReadNumReceiver!=null){
            unregisterReceiver(unReadNumReceiver);
        }
    }
}

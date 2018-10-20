package com.zbmf.groupro.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.FoucusAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.db.Database;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.groupro.R.id.listview;

//关注老师  //个人中心关注
public class CareTeacherActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private PullToRefreshListView content_view;
    private FoucusAdapter mFoucusAdapter;
    private List<Group> mGroups = new ArrayList<>();
    private int PAGE_INDEX, PAGGS;
    public static final int Refresh = 1;
    public static final int LoadMore = 2;
    private int type = FoucusAdapter.FOCUS_LIST;
    private LinearLayout ll_none;
    private Database db;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_teacher);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = bundle.getInt("type");
        }

        dbManager=new DBManager(this);
        db = new Database(this);
        ll_none = (LinearLayout) findViewById(R.id.ll_none);
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("关注老师");
        tv_title.setVisibility(View.VISIBLE);
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        content_view = (PullToRefreshListView) findViewById(listview);
        content_view.setMode(PullToRefreshBase.Mode.BOTH);
        mFoucusAdapter = new FoucusAdapter(this, mGroups, type);
        content_view.setAdapter(mFoucusAdapter);
        findViewById(R.id.btn_focus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivity(CareTeacherActivity.this, RecommendActivity.class);
            }
        });
        content_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                userGroups(Refresh, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                userGroups(LoadMore, false);
            }
        });
        content_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getGroupInfo((Group) parent.getItemAtPosition(position));
            }
        });
        mFoucusAdapter.setListener(this);

        unReadNumReceiver = new UnReadNumReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.zbmf.StockGroup.UNREADNUM");
        registerReceiver(unReadNumReceiver,intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userGroups(Refresh, true);
    }

    private UnReadNumReceiver unReadNumReceiver;
    private class UnReadNumReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            for(int i=0;i<mGroups.size();i++){
                Group group1 = mGroups.get(i);
                int chatNo = db.getChatUnReadNum(group1.getId());
                group1.setChat(chatNo);
            }
            mFoucusAdapter.notifyDataSetChanged();
        }
    }

    private void getGroupInfo(Group group) {
        final int role = group.getRoles();
        WebBase.getGroupInfo(group.getId(), new JSONHandler(true, CareTeacherActivity.this, "正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject group = obj.optJSONObject("group");
                Group groupbean = new Group();
                groupbean.setId(group.optString("id"));
                groupbean.setName(group.optString("name"));
                groupbean.setNick_name(group.optString("nickname"));
                groupbean.setAvatar(group.optString("avatar"));
                groupbean.setIs_close(group.optInt("is_close"));
                groupbean.setIs_private(group.optInt("is_private"));
//                groupbean.setRoles(group.optInt("roles"));
                groupbean.setRoles(role);
                groupbean.setFans_level(group.optInt("fans_level"));
                groupbean.setNotice(group.optString("notice"));
                Bundle bundle = new Bundle();
                bundle.putSerializable("GROUP", groupbean);
                ShowActivity.showActivity(CareTeacherActivity.this, bundle, Chat1Activity.class.getName());
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(CareTeacherActivity.this, err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userGroups(final int direction, boolean show) {
        if (direction == Refresh)
            PAGE_INDEX = 1;
        else
            PAGE_INDEX++;

        WebBase.userGroups(PAGE_INDEX, new JSONHandler(show, this, "正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                content_view.onRefreshComplete();
                Group group = JSONParse.userGroups(obj);
                if (group != null) {
                    PAGGS = group.getPages();

                    if (PAGE_INDEX == PAGGS) {
                        content_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                        Toast.makeText(CareTeacherActivity.this, "已加载全部数据", Toast.LENGTH_SHORT).show();
                    } else {
                        content_view.setMode(PullToRefreshBase.Mode.BOTH);
                    }


                    if (group.getList() != null && group.getList().size() > 0) {
                        for(int i=0;i<group.getList() .size();i++){
                            Group group1 = group.getList().get(i);
                            int chatNo = db.getChatUnReadNum(group1.getId());
                            int liveNo = dbManager.getUnredCount(group1.getId());
                            group1.setChat(chatNo);
                            group1.setUnredcount(liveNo);
                        }
                        if (direction == Refresh) {
                            mGroups.clear();
                        }

                        mGroups.addAll(group.getList());
                        mFoucusAdapter.notifyDataSetChanged();
                        ll_none.setVisibility(View.GONE);
                    } else
                        ll_none.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = (int) buttonView.getTag();
        final String id = mGroups.get(pos).getId();

        if (isChecked)
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
}

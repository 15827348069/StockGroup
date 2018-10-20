package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.UserMatchAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CircleImageView;
import com.zbmf.StocksMatch.widget.CustomListView;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends ExActivity {

    private TextView tv_title, tv_name, tv_num;
    private Button btn_focus;
    private CircleImageView civ;
    private Group group;
    private CustomListView content_view;
    private Get2Api server;
    private boolean focus = true;
    private UserMatchAdapter adapter;
    private List<MatchBean> list;
    private ScrollView mylistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getData();
        setupView();
    }


    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            group = (Group) bundle.getSerializable("group");
        }
    }

    private void setupView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_num = (TextView)findViewById(R.id.tv_num);
        civ = (CircleImageView)findViewById(R.id.civ);
        btn_focus = (Button)findViewById(R.id.btn_focus);
        content_view = (CustomListView) findViewById(R.id.content_view);
        mylistview = (ScrollView) findViewById(R.id.mylistview);
        content_view.setFocusable(false);
        mylistview.smoothScrollTo(0, 20);

        if(group!=null){
            tv_title.setText(group.getName());
            tv_title.setVisibility(View.INVISIBLE);
            tv_num.setText(group.getCount_fans());
            imageLoader.displayImage(group.getAvatar(), civ, options);
            tv_name.setText(group.getName());
//            new GetMyMatch(this,R.string.loading,R.string.load_fail,true).execute(group.getGid());
            new UserMoreTask(this,R.string.loading,R.string.load_fail,true).execute(group.getGid());
        }
        content_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MatchBean matchBean = (MatchBean) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("matchbean",matchBean);
                bundle.putSerializable("group",group);
                bundle.putSerializable("from","user");
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_OPERAT,bundle);
            }
        });
        btn_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("====",group.getGroup());
                if(focus)
                    new QuitTask(UserActivity.this,R.string.loading,R.string.load_fail,true).execute(group.getGroup(), "quit");
                else{
                    new QuitTask(UserActivity.this,R.string.loading,R.string.load_fail,true).execute(group.getGroup(),"join");
                }
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendClose();

                finish();
            }
        });

        list = new ArrayList<MatchBean>();
        adapter = new UserMatchAdapter(this);
        adapter.setList(list);
        content_view.setAdapter(adapter);

    }

    private void sendClose() {
        if(!focus){
            Intent intent = new Intent();
            intent.setAction(Constants.ACCOUNT_DEL);
            intent.putExtra("gid",group.getGid());
            sendBroadcast(intent);
        }
    }

    private class GetMyMatch extends LoadingDialog<String, MatchBean> {

        public GetMyMatch(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public MatchBean doInBackground(String... params) {
            MatchBean ret = null;

            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getRunMatches(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ret;
        }

        @Override
        public void doStuffWithResult(MatchBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    list.clear();
                    adapter.addList(result.getList());
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    private class UserMoreTask extends LoadingDialog<String,User> {



        public UserMoreTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public User doInBackground(String... params) {
            User user = null;
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                user = server.UserInfo(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        public void doStuffWithResult(User user) {
            if (user != null && user.code != -1) {
                if (user.getStatus() == 1) {
                    tv_num.setText(user.getCount_fens());
                    tv_name.setText(user.getNickname());
                    imageLoader.displayImage(user.getAvatar(),civ,options);
                    group.setGroup(user.getGroup_id());
                    group.setAvatar(user.getAvatar());
                    group.setName(user.getNickname());

                    if("0".equals(user.getIs_focus())){
                        focus = false;
                        btn_focus.setText(getString(R.string.focus));
                    }else{
                        focus = true;
                        btn_focus.setText(getString(R.string.unfocus));
                    }


                    list.clear();
                    adapter.addList(user.getMatches());

                } else {
                    UiCommon.INSTANCE.showTip(user.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    private class QuitTask extends LoadingDialog<String,General>{


        public QuitTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public General doInBackground(String... params) {

            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                return server.groups_quit(params[0],params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(General result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(focus){
                        focus = false;
                        btn_focus.setText(getString(R.string.focus));
                        UiCommon.INSTANCE.showTip(getString(R.string.unfocusok));
                    }else{
                        focus = true;
                        btn_focus.setText(getString(R.string.unfocus));
                        UiCommon.INSTANCE.showTip(getString(R.string.focusok));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sendClose();
        }
        return super.onKeyDown(keyCode, event);
    }
}

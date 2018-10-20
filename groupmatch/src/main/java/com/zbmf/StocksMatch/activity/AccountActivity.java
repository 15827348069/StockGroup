package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.AccountAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.Database;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.receiver.AccountReceiver;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.utils.UiHelper;
import com.zbmf.StocksMatch.widget.CustomListView;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;



public class AccountActivity extends ExActivity implements View.OnClickListener {

    private CustomListView listview;
    private TextView tv_title,tv_right;
    private AccountAdapter adapter;
    private List<User> list = new ArrayList<User>();
    private Database db;
    private boolean isEdit = false;
    private SharedPreferencesUtils sp;
    private AccountReceiver receiver;
    private Get2Api server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setupView();
    }

    private void setupView() {
        listview = (CustomListView)findViewById(R.id.listview);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_title.setText(R.string.account_m);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(getString(R.string.edit));
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_right.setOnClickListener(this);
        adapter = new AccountAdapter(this);
        sp = new SharedPreferencesUtils(this);
        adapter.setList(list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getItemAtPosition(position);
                if (!isEdit && !sp.getAccount().equals(((User) parent.getItemAtPosition(position)).getAuth_token())) {
                    new GetUserinfo2Task(AccountActivity.this,R.string.chaing,R.string.chaee,true).execute(user.getAuth_token());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAccountListTask(this).execute();
        if(receiver == null){
            receiver = new AccountReceiver();
            UiHelper.RegistBroadCast(this, receiver, Constants.ACCOUNT_DEL);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_add:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_LOGIN,null);
                break;
            case R.id.tv_right:
                if(!isEdit){
                    tv_right.setText("完成");
                    isEdit = true;
                }else{
                    tv_right.setText(getString(R.string.edit));
                    isEdit = false;
                }

                adapter.setEdit(isEdit);
                adapter.notifyDataSetChanged();
                break;

        }
    }

    public void updateUi(String user_id) {
        for (int i=0;i<list.size();i++){
            if(user_id.equals(list.get(i).getUser_id())){
                list.remove(i);
                break;
            }
        }

        adapter.notifyDataSetChanged();
    }

    private class GetAccountListTask extends LoadingDialog<Void,User>{

        public GetAccountListTask(Context activity) {
            super(activity, false, true);
        }

        @Override
        public User doInBackground(Void... params) {
            if(db == null){
                db = new DatabaseImpl(AccountActivity.this);
            }

            return db.getUser();
        }

        @Override
        public void doStuffWithResult(User user) {
            if(user!=null && user.getList().size()>0){
                list.clear();
                adapter.addList(user.getList());
            }else{
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    private class GetUserinfo2Task extends LoadingDialog<String,User>{

        public GetUserinfo2Task(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public User doInBackground(String... params) {
            User user = null;
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                user= server.getuserinfo2(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        public void doStuffWithResult(User user) {
            if (user != null && user.code != -1) {
                if (user.getStatus() == 1) {
                    sp.setAccount(user.getAuth_token());
                    UiCommon.INSTANCE.setiUser(user);
                    new DatabaseImpl(AccountActivity.this).addUser(user);
                    for (int i=0;i<list.size();i++){
                        if(sp.getAccount().equals(list.get(i).getAuth_token()))
                            list.get(i).setAccount(true);
                        else
                            list.get(i).setAccount(false);

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    UiCommon.INSTANCE.showTip(user.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(AccountActivity.this.getString(R.string.load_fail));
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHelper.UnRegistBroadCast(this, receiver);
    }
}

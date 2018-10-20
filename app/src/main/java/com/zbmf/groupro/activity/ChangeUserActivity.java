package com.zbmf.groupro.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.ChangeUserAdapter;
import com.zbmf.groupro.beans.User;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;


public class ChangeUserActivity extends AppCompatActivity {
    private TextView group_title_name;
    private ChangeUserAdapter adapter;
    private ListView change_user_list_view;
    private List<User>infolist;
    private DBManager dbManager;
    private ImageButton group_title_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        init();
    }

    private void init() {
        dbManager=new DBManager(ChangeUserActivity.this);
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        group_title_name.setText("切换用户");
        change_user_list_view= (ListView) findViewById(R.id.change_user_list_view);
        group_title_return= (ImageButton) findViewById(R.id.group_title_return);

        infolist=new ArrayList<>();
        adapter=new ChangeUserAdapter(ChangeUserActivity.this,infolist);
        change_user_list_view.setAdapter(adapter);

        group_title_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUserMessage();
        change_user_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dbManager.addUser(infolist.get(i));
                adapter.notifyDataSetChanged();
                GroupActivity activity=(GroupActivity) ActivityUtil.getInstance().getActivity(ActivityUtil.GROUPACTIVITY);
                activity.bind_client_id();
            }
        });
    }
    public void getUserMessage(){
        infolist.addAll(dbManager.queryUser());
        adapter.notifyDataSetChanged();
    }
}

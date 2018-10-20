package com.zbmf.groupro.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.HistoryDateAdapter;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.DateUtil;
import com.zbmf.groupro.utils.ShowActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuhao on 2017/2/13.
 */

public class HistoryDateActivity extends Activity {
    private HistoryDateAdapter adapter;
    private ListView history_date_list;
    private Group group_bean;
    private List<Date>infolist;
    private TextView group_title_name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_days_layout);
        group_bean= (Group) getIntent().getSerializableExtra("GROUP");
        init();
    }

    private void init() {
        history_date_list= (ListView) findViewById(R.id.history_date_list);
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        group_title_name.setText("直播历史纪录");
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        infolist=getDate();
        adapter=new HistoryDateAdapter(this,infolist,group_bean.getNick_name());
        history_date_list.setAdapter(adapter);
        history_date_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                group_bean.setLive_history_date(infolist.get(i));
                ShowActivity.showLiveHistoryActivity(HistoryDateActivity.this,group_bean);
            }
        });
        getDate();
    }
    public List<Date> getDate(){
        infolist=new ArrayList<>();
        for(int i = -1;i>=Constants.HISTORY_DAYS;i--){
            infolist.add(DateUtil.afterNDay(i));
        }
        return  infolist;
    }
}

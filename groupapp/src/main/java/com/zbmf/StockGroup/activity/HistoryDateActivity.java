package com.zbmf.StockGroup.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.HistoryDateAdapter;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.ShowActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuhao on 2017/2/13.
 */

public class HistoryDateActivity extends BaseActivity {
    private HistoryDateAdapter adapter;
    private ListView history_date_list;
    private Group group_bean;
    private List<Date>infolist;
    private TextView group_title_name;
    @Override
    public int getLayoutResId() {
        return R.layout.history_days_layout;
    }

    @Override
    public void initView() {
        initTitle("直播历史纪录");
        history_date_list= (ListView) findViewById(R.id.history_date_list);
    }

    @Override
    public void initData() {
        group_bean= (Group) getIntent().getSerializableExtra(IntentKey.GROUP);
        infolist=getDate();
        adapter=new HistoryDateAdapter(this,infolist,group_bean.getNick_name());
        history_date_list.setAdapter(adapter);
        getDate();
    }

    @Override
    public void addListener() {
        history_date_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                group_bean.setLive_history_date(infolist.get(i));
                ShowActivity.showLiveHistoryActivity(HistoryDateActivity.this,group_bean);
            }
        });
    }

    public List<Date> getDate(){
        infolist=new ArrayList<>();
        for(int i = -1;i>=Constants.HISTORY_DAYS;i--){
            infolist.add(DateUtil.afterNDay(i));
        }
        return  infolist;
    }
}

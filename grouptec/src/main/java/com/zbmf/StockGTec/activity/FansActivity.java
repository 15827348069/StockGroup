package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.BoxItemAdapter;
import com.zbmf.StockGTec.beans.BoxBean;
import com.zbmf.StockGTec.utils.DateUtil;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/1/3.
 */

public class FansActivity extends ExActivity implements View.OnClickListener {
    private ListViewForScrollView box_list;
    private List<BoxBean> infolist;
    private BoxItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity_layout);
        init();
    }

    private void init() {
        box_list = (ListViewForScrollView) findViewById(R.id.box_item_message);
        infolist = new ArrayList<>();
        adapter = new BoxItemAdapter(getBaseContext(), infolist);
        box_list.setAdapter(adapter);
        box_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(), "点击宝盒" + i, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.add_fans_button).setOnClickListener(this);
        getBox_Message();
    }

    public void getBox_Message() {
        for (int i = 0; i < 10; i++) {
            BoxBean bb = new BoxBean();
            bb.setBox_id(i + "");
            bb.setSubject("短线T策略" + i);
            bb.setBox_updated(DateUtil.getTime("2017-01-03 12:3" + i));
            List<BoxBean.Tags> info = new ArrayList<>();
            if (i % 2 == 0) {
                bb.setBox_level("year");
                info.add(bb.getTag("投资策略", 1));
                info.add(bb.getTag("短线", 2));
                info.add(bb.getTag("高风险", 3));
            } else if (i % 3 == 0) {
                bb.setBox_level("tie");
                info.add(bb.getTag("投资策略", 1));
                info.add(bb.getTag("高风险", 3));
            } else {
                bb.setBox_level("owner");
                info.add(bb.getTag("短线", 2));
            }

            bb.setTags(info);
            bb.setDescription("铁粉服务介绍铁粉服务介绍铁粉服务介绍铁粉服务介绍铁粉服务介绍铁粉服务介绍铁粉服务介绍铁粉服务介绍铁粉服务介绍铁粉服务介绍");
            infolist.add(bb);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fans_button:
                ShowActivity.showActivity(this, AddFansActivity.class);
                break;
        }
    }
}

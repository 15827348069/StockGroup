package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.PrivateAdapter;
import com.zbmf.StockGTec.beans.Fans;

import java.util.ArrayList;

public class PrivateActivity extends ExActivity {

    private ListView listview;
    private PrivateAdapter mPrivateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_list);

        TextView tv_title = ((TextView) findViewById(R.id.group_head_name));
        tv_title.setText("私聊");
        tv_title.setVisibility(View.VISIBLE);

        listview = (ListView) findViewById(R.id.listview);
        findViewById(R.id.group_head_avatar).setVisibility(View.GONE);
        mPrivateAdapter = new PrivateAdapter(this,new ArrayList<Fans>());
        listview.setAdapter(mPrivateAdapter);

        findViewById(R.id.group_head_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.zbmf.StockGTec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.BanAdapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BanInfo;
import com.zbmf.StockGTec.utils.JSONparse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 禁言名单列表
 */
public class BanListActivity extends ExActivity {

    private ListView listview;
    private List<BanInfo> mList = new ArrayList<>();
    private String group_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_list);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            group_id = bundle.getString("group_id");
        }

        TextView tv_title = (TextView) findViewById(R.id.group_title_name);
        tv_title.setText("禁言黑名单");
        tv_title.setVisibility(View.VISIBLE);

        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview = (ListView) findViewById(R.id.listview);
        final BanAdapter adapter = new BanAdapter(this,mList);
        adapter.setGroupId(group_id);
        listview.setAdapter(adapter);

        if(!TextUtils.isEmpty(group_id))
            WebBase.banList(group_id, new JSONHandler(true,BanListActivity.this,"获取禁言列表") {
                @Override
                public void onSuccess(JSONObject obj) {
                    BanInfo banInfo = JSONparse.getBanList(obj);
                    if(banInfo!=null){
                        mList.addAll(banInfo.getInfos());
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(BanListActivity.this,"没有人被禁言.",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String err_msg) {

                }
            });
    }
}

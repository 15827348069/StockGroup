package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.ActivesAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Actives;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ActivesActivity extends ExActivity implements View.OnClickListener {

    private ListView listView;
    private TextView tv_title;
    private Get2Api server;
    private ActivesAdapter adapter;
    private List<Actives> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actives);

        setupView();
    }

    private void setupView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.activity));
        findViewById(R.id.iv_back).setOnClickListener(this);
        listView = (ListView)findViewById(R.id.listview);
        list = new ArrayList<Actives>();
        adapter = new ActivesAdapter(this);
        adapter.setList(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Actives actives = (Actives) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
//                bundle.putString("title",actives.getTitle());
                bundle.putString("web_url",actives.getUrl());
                bundle.putInt("soure_act", 2);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT_Web,bundle);
            }
        });
        new GetActivesTask(this).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private class GetActivesTask extends LoadingDialog<String, Actives> {

        public GetActivesTask(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public Actives doInBackground(String... params) {
            Actives actives = null;
            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                actives = server.getActives();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return actives;
        }


        @Override
        public void doStuffWithResult(Actives result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    if(result.getList()!=null && result.getList().size()>0){
                        adapter.addList(result.getList());
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(ActivesActivity.this.getString(R.string.load_fail));
            }
        }
    }
}

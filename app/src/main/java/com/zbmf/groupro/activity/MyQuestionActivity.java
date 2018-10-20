package com.zbmf.groupro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.AskAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.Ask;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的提问
 */
public class MyQuestionActivity extends AppCompatActivity {

    private PullToRefreshListView mListView;
    private AskAdapter mAdapter;
    private List<Ask> asks=null;
    private int PAGE_INDEX, PAGGS;
    public static final int Refresh = 1;
    public static final int LoadMore = 2;
    private LinearLayout ll_none;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);
        setupView();
    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("我的提问");
        tv_title.setVisibility(View.VISIBLE);
        ll_none = (LinearLayout) findViewById(R.id.ll_none);
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        asks = new ArrayList<>();
        mAdapter = new AskAdapter(this,asks);
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_focus).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tv_tip)).setText("还没有任何提问哦");
        mListView.setAdapter(mAdapter);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                userAsks(Refresh,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                userAsks(LoadMore,false);
            }
        });

        userAsks(Refresh,true);

        Intent answer_intent=new Intent(Constants.USER_RED_NEW_MESSAGE);
        answer_intent.putExtra("type", MessageType.ANSWER);
        this.sendBroadcast(answer_intent);
    }

    private void userAsks(final int direction,boolean show) {

        if (direction == Refresh)
            PAGE_INDEX = 1;
        else
            PAGE_INDEX++;

        WebBase.userAsks(PAGE_INDEX,SettingDefaultsManager.getInstance().authToken(), new JSONHandler(show,this,"正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                mListView.onRefreshComplete();
                Ask ask = JSONParse.getAsks(obj);
                if(ask != null){
                    PAGGS = ask.getPages();
                    if (PAGE_INDEX == PAGGS){
                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }else{
                        mListView.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    if(ask.getAsks()!= null && ask.getAsks().size()>0){
                        if (direction == Refresh) {
                            asks.clear();
                        }
                        asks.addAll(ask.getAsks());
                        mAdapter.notifyDataSetChanged();
                    }else{
                        ll_none.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                }else{
                    mListView.setVisibility(View.GONE);
                    ll_none.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });

    }


}

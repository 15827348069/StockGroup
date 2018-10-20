package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.RecordAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.MatchInfo;
import com.zbmf.StocksMatch.beans.Record;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.GetTime;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CustomListView;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshScrollView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 获奖记录
 */
public class RecordActivity extends ExActivity{

    private TextView tv_title,tv_tip,all_money, match_name, all_shouyi, arrow, match_time, match_type, match_number;
    private CustomListView listView;
    private PullToRefreshScrollView myscrllview;
    private boolean isFirstIn = true;
    private static final int PAGE_SIZE = 15;
    private static int PAGE_INDEX = 1;
    private Get2Api server = null;
    private RecordAdapter adapter;
    private List<Record> list = new ArrayList<Record>();
    private MatchBean mb;
    private Group group;//user_id 默认自己，有group为group.id
    private String user_id = UiCommon.INSTANCE.getiUser().getUser_id();
    private DecimalFormat df = new DecimalFormat("######0.00");
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mb = (MatchBean) bundle.getSerializable("matchbean");
            group = (Group) bundle.getSerializable("group");
            if (group != null)
                user_id = group.getId();
        }
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_title.setText(R.string.record);
        listView = (CustomListView) findViewById(R.id.content_view);
        myscrllview = (PullToRefreshScrollView) findViewById(R.id.myscrllview);
        all_money = (TextView) findViewById(R.id.all_money);
        match_name = (TextView) findViewById(R.id.match_name);
        all_shouyi = (TextView) findViewById(R.id.all_shouyi);
        arrow = (TextView) findViewById(R.id.arrow_textview);
        match_time = (TextView) findViewById(R.id.start_end_time);
        match_type = (TextView) findViewById(R.id.match_type);
        match_number = (TextView) findViewById(R.id.match_account);

        if(user_id.equals(UiCommon.INSTANCE.getiUser().getUser_id()))
            tv_tip.setText(R.string.record_tip);
        listView.setFocusable(false);
        myscrllview.smoothScrollTo(0, 20);

        adapter = new RecordAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetWinRecords(RecordActivity.this).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetWinRecords(RecordActivity.this).execute(DataLoadDirection.LoadMore, PAGE_INDEX);

            }
        });

        if (mb != null) {
            setData();
            showDialog(this,R.string.record_getting);
            new GetWinRecords(this).execute(DataLoadDirection.Refresh, PAGE_INDEX);
        }


        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setData() {
        all_money.setText(currencyFormat.format(mb.getUnfrozen_money()));
        match_name.setText(mb.getTitle());
        if (mb.getYield() >= 0) {
            all_shouyi.setTextColor(getResources().getColor(R.color.match_all_money));
            all_shouyi.setText("+" + df.format(mb.getYield() * 100) + "%");
        } else {
            all_shouyi.setTextColor(getResources().getColor(R.color.match_all_money_green));
            all_shouyi.setText(df.format(mb.getYield() * 100) + "%");
        }
        arrow.setText(mb.getTotal_rank());
        match_time.setText(mb.getStart_at() + getString(R.string.sperator) + mb.getEnd_at());
        if (GetTime.getTimeIsTrue(mb.getEnd_at())) {
            match_type.setText("进行中");
            match_type.setTextColor(getResources().getColor(R.color.match_all_money));
        } else {
            match_type.setText("已结束");
            match_type.setTextColor(getResources().getColor(R.color.black));
        }
        match_number.setText(getResources().getString(R.string.record_count, mb.getRecords()));
    }

    private class GetWinRecords extends LoadingDialog<Integer, Record> {

        private int operation;
        private int page;

        public GetWinRecords(Context activity) {
            super(activity, false, true);
        }


        @Override
        public Record doInBackground(Integer... params) {
            operation = params[0];
            page = params[1];
            Record ret = null;

            if (operation == DataLoadDirection.Refresh) {
                page = 1;
            } else {
                page++;
            }
            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getWinRecords(user_id, mb.getId(), page, PAGE_SIZE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void onPostExecute(Record ret) {
            super.onPostExecute(ret);
            myscrllview.onRefreshComplete();
            DialogDismiss();
        }

        @Override
        public void doStuffWithResult(Record result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    if(result.getRecords()!=null){
                        if (operation == DataLoadDirection.Refresh && result.getRecords().size() > 0) {
                            MatchBean mb = result.getRecords().get(0).getMatch();
                            list = result.getRecords();
                            adapter.setList(list);tv_tip.setVisibility(View.GONE);
                        } else if (result.getRecords().size() == 0) {
                            tv_tip.setVisibility(View.VISIBLE);
                        } else {
                            tv_tip.setVisibility(View.GONE);
                            adapter.addList(result.getRecords());
                        }

                        if (page != result.getPages()) {
                            myscrllview.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            myscrllview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        PAGE_INDEX = page;
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(RecordActivity.this.getString(R.string.load_fail));
            }
        }
    }
}

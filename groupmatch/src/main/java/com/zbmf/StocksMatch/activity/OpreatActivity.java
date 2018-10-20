package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.MatchInfo;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CircleImageView;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class OpreatActivity extends ExActivity implements View.OnClickListener {
    private TextView tv_name,tv_title,tv_username,have_money, total_rank, week_rank, day_rank, can_use_money, shouyi_lv, my_award, my_holds, week_lv, day_lv;;
    private MatchBean matchbean;
    private MatchInfo info;
    private Group group;
    private CircleImageView civ;
    private DecimalFormat df = new DecimalFormat("######0.00");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private String from="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opreat);
        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchbean = (MatchBean) bundle.getSerializable("matchbean");
            group = (Group)bundle.getSerializable("group");
            from = (String) bundle.getSerializable("from");
        }
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.opreat);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_name = (TextView) findViewById(R.id.tv_name);
        civ = (CircleImageView)findViewById(R.id.civ);
        shouyi_lv = (TextView) findViewById(R.id.shouyi_lv);
        total_rank = (TextView) findViewById(R.id.total_rank);
        week_lv = (TextView) findViewById(R.id.week_lv);
        week_rank = (TextView) findViewById(R.id.week_rank);
        day_lv = (TextView) findViewById(R.id.day_lv);
        day_rank = (TextView) findViewById(R.id.day_rank);
        have_money = (TextView) findViewById(R.id.moneyunfrozen);
        can_use_money = (TextView) findViewById(R.id.can_use_money);
        my_award = (TextView) findViewById(R.id.my_award);
        my_holds = (TextView) findViewById(R.id.my_holds);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.ll_match).setOnClickListener(this);
        findViewById(R.id.ll_award).setOnClickListener(this);
        findViewById(R.id.ll_holder).setOnClickListener(this);
        findViewById(R.id.ll_jyjl).setOnClickListener(this);
        findViewById(R.id.ll_user).setOnClickListener(this);

        if(group!=null){
            tv_username.setText(group.getName());
            imageLoader.displayImage(group.getAvatar(),civ,options);
        }
        if(matchbean !=null){
            tv_name.setText(matchbean.getTitle());
            day_rank.setText(matchbean.getDay_rank());
            week_rank.setText(matchbean.getWeek_rank());
            total_rank.setText(matchbean.getTotal_rank());

        }
        new GetMatchMessageTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, matchbean.getId(),group.getId());
    }

    private void initData() {
        total_rank.setText(info.getTotal_rank());
        week_rank.setText(info.getWeek_rank());
        day_rank.setText(info.getDay_rank());
        matchbean.setTotal_rank(info.getTotal_rank());
        my_holds.setText(info.getHolds());

        if(!TextUtils.isEmpty(info.getRecords()))
            my_award.setText(info.getRecords());
        matchbean.setRecords(info.getRecords());

        have_money.setText(currencyFormat.format(info.getTotal()));
        matchbean.setUnfrozen_money(info.getTotal());
        matchbean.setYield(info.getYield());
        if(info.getYield()>=0){
            shouyi_lv.setText("+"+df.format(info.getYield()*100)+"%");
        }else{
            shouyi_lv.setText(df.format(info.getYield()*100)+"%");
        }
        if(info.getWeek_yield()>=0){
            week_lv.setText("+"+df.format(info.getWeek_yield()*100)+"%");
        }else{
            week_lv.setText(df.format(info.getWeek_yield()*100)+"%");
        }
        if(info.getDay_yield()>=0){
            day_lv.setText("+"+df.format(info.getDay_yield()*100)+"%");
        }else{
            day_lv.setText(df.format(info.getDay_yield()*100)+"%");
        }
//            mb.setUnfrozen_money(mb_up.getUnfrozen_money());
        can_use_money.setText(currencyFormat.format(info.getMoneyunfrozen()));
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = null;
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_user://跳至用户主页
                if(!TextUtils.isEmpty(from)){
                    finish();
                }else{//榜单进入 可能需要传值
                    bundle = new Bundle();
                    bundle.putSerializable("group",group);
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_USER,bundle);
                }
                break;
            case R.id.ll_match:
                bundle = new Bundle();
                bundle.putSerializable("matchbean",matchbean);
//                bundle.putSerializable("group",group);
                if ("1".equals(matchbean.getIs_match_player()))//已参赛
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDESC, bundle);
                else
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDETAIL, bundle);
                break;
            case R.id.ll_award:
                bundle = new Bundle();

                bundle.putSerializable("matchbean",matchbean);
                bundle.putSerializable("group",group);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_RECORD,bundle);
                break;
            case R.id.ll_holder:
                bundle = new Bundle();
                bundle.putSerializable("group",group);
                bundle.putSerializable("matchbean",matchbean);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_USERHOLDER,bundle);
                break;
            case R.id.ll_jyjl:
                bundle = new Bundle();
                bundle.putSerializable("group",group);
                bundle.putSerializable("matchbean",matchbean);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_USERTRANSACTION,bundle);
                break;
        }
    }


    private Get2Api server=null;
    private class GetMatchMessageTask extends LoadingDialog<String,MatchInfo> {
        public GetMatchMessageTask(Context activity) {
            super(activity, false, true);
        }

        @Override
        public MatchInfo doInBackground(String... params) {

            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                info = server.getMatchMessage(params[0],params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return info;
        }

        @Override
        public void doStuffWithResult(MatchInfo result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(info!=null){
                        initData();
                    }
                } else if(result.msg!=null){
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }
}

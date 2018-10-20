package com.zbmf.StocksMatch.activity;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.MatchInfo;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshScrollView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;

/**
 * 比赛主页
 * @author Administrator
 */
public class MatchDescActivity extends ExActivity implements View.OnClickListener {
    private PullToRefreshScrollView myscrllview;
    private TextView tv_title,zhouzhuanlv, tv_time, tv_num, have_money, total_rank, week_rank, day_rank, can_use_money, shouyi_lv, my_weituo, my_holds, week_lv, day_lv;
    private DecimalFormat df = new DecimalFormat("######0.00");
    DecimalFormat df1=new DecimalFormat("###,###.00");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private MatchBean matchbean;
    private Get2Api server = null;
    private ImageView iv_right;
    private Group group;
    private String user_id = UiCommon.INSTANCE.getiUser().getUser_id();
    private boolean isFirst=true; private LinearLayout ll_main;
    private WindowManager.LayoutParams lp;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_desc);
        lp = getWindow().getAttributes();
        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchbean = (MatchBean) bundle.getSerializable("matchbean");
            group = (Group) bundle.getSerializable("group");

            if(group!=null)
                user_id = group.getId();
        }
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_num = (TextView) findViewById(R.id.tv_num);
        iv_right = (ImageView)findViewById(R.id.iv_right);
        iv_right.setImageResource(R.drawable.fx);
        iv_right.setVisibility(View.VISIBLE);
        ll_main = (LinearLayout)findViewById(R.id.main);
        myscrllview = (PullToRefreshScrollView) findViewById(R.id.myscrllview);
        myscrllview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        view = findViewById(R.id.view);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_intro).setOnClickListener(this);
        findViewById(R.id.btn_board).setOnClickListener(this);
        findViewById(R.id.btn_bangdan).setOnClickListener(this);
        findViewById(R.id.my_tratus_layout).setOnClickListener(this);
        findViewById(R.id.my_hold_layout).setOnClickListener(this);
        findViewById(R.id.my_transtation_layout).setOnClickListener(this);
        findViewById(R.id.my_history_layout).setOnClickListener(this);
        findViewById(R.id.btn_sale).setOnClickListener(this);
        findViewById(R.id.btn_buy).setOnClickListener(this);
        findViewById(R.id.my_history_layout).setOnClickListener(this);
        iv_right.setOnClickListener(this);
        have_money = (TextView) findViewById(R.id.moneyunfrozen);
        shouyi_lv = (TextView) findViewById(R.id.shouyi_lv);
        can_use_money = (TextView) findViewById(R.id.can_use_money);
        total_rank = (TextView) findViewById(R.id.total_rank);
        week_rank = (TextView) findViewById(R.id.week_rank);
        day_rank = (TextView) findViewById(R.id.day_rank);
        my_weituo = (TextView) findViewById(R.id.my_weituo);
        my_holds = (TextView) findViewById(R.id.my_holds);
        week_lv = (TextView) findViewById(R.id.week_lv);
        day_lv = (TextView) findViewById(R.id.day_lv);
        zhouzhuanlv = (TextView) findViewById(R.id.zhouzhuanlv);

        if(matchbean!=null){
            tv_title.setText(UiCommon.INSTANCE.subTitle(matchbean.getTitle()));
            tv_num.setText(matchbean.getPlayers());
            day_rank.setText(matchbean.getDay_rank());
            week_rank.setText(matchbean.getWeek_rank());
            total_rank.setText(matchbean.getTotal_rank());
        }
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                update();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
        lp.alpha=1.0f;
        getWindow().setAttributes(lp);
    }

    private Timer timer=null;
    private void update(){
        new GetMatchMessageTask(MatchDescActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, matchbean.getId());

        /*timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
//                Log.e("tag","--"+GetTime.getData());
                    if (GetTime.getData()) {
                        new GetMatchMessageTask(MatchDescActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, matchbean.getId());
                    } else {
                        new GetMatchMessageTask(MatchDescActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, matchbean.getId());
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                            return;
                        }
                    }
                }
        }, 0, 60000);*/
    }

    private void initData(MatchInfo temp) {
        if("1".equals(temp.getNew_announcement())){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.INVISIBLE);
        }

        total_rank.setText(temp.getTotal_rank());

        week_rank.setText(temp.getWeek_rank());

        day_rank.setText(temp.getDay_rank());
        if(matchbean.getStart_at()!=null&&!TextUtils.isEmpty(matchbean.getStart_at())){
            tv_time.setText(matchbean.getStart_at()+getString(R.string.sperator)+matchbean.getEnd_at());
        }


        tv_num.setText(temp.getCount_players());

        my_holds.setText(temp.getHolds());

        my_weituo.setText(temp.getTrusts());

        if(temp.getWeek_velocity() == 0){
            zhouzhuanlv.setText("0.00%");
        }else{
            zhouzhuanlv.setText("+"+df1.format(temp.getWeek_velocity()*100)+"%");
        }


        have_money.setText(currencyFormat.format(temp.getTotal()));

        if(temp.getYield()>=0){
            shouyi_lv.setText("+"+df.format(temp.getYield()*100)+"%");
        }else{
            shouyi_lv.setText(df.format(temp.getYield()*100)+"%");
        }
        if(temp.getWeek_yield()>=0){
            week_lv.setText("+"+df.format(temp.getWeek_yield()*100)+"%");
        }else{
            week_lv.setText(df.format(temp.getWeek_yield()*100)+"%");
        }
        if(temp.getDay_yield()>=0){
            day_lv.setText("+"+df.format(temp.getDay_yield()*100)+"%");
        }else{
            day_lv.setText(df.format(temp.getDay_yield()*100)+"%");
        }

        matchbean.setTotal_rank(temp.getTotal_rank());
        matchbean.setYield(temp.getYield());
        matchbean.setRecords(temp.getRecords());
        can_use_money.setText(currencyFormat.format(temp.getMoneyunfrozen()));
        matchbean.setUnfrozen_money(temp.getMoneyunfrozen());
        matchbean.setMoney(temp.getTotal());

    }

    private class GetMatchMessageTask extends LoadingDialog<String,MatchInfo>{
        private MatchInfo temp = null;
        public GetMatchMessageTask(Context activity) {
            super(activity, false, true);
        }

        @Override
        public MatchInfo doInBackground(String... params) {

            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                temp = server.getMatchMessage(params[0],user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return temp;
        }
        @Override
        public void onPostExecute(MatchInfo ret) {
            super.onPostExecute(ret);
            myscrllview.onRefreshComplete();
            DialogDismiss();
            isFirst = false;
        }
        @Override
        public void doStuffWithResult(MatchInfo result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(temp!=null){
                        initData(temp);
                    }
                } else if(result.msg!=null){
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(MatchDescActivity.this.getString(R.string.load_fail));
            }
        }
    }

    private Dialog dialog;

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        bundle.putSerializable("matchbean", matchbean);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_intro:
                Bundle bundle1 = new Bundle();
                matchbean.setIs_match_player("1");
                bundle1.putSerializable("matchbean", matchbean);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDETAIL, bundle1);
                break;
            case R.id.btn_board:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ANNOUNCEMENT, bundle);
                break;
            case R.id.btn_bangdan:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_RANK, bundle);
                break;
            case R.id.iv_right://分享

                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_SHARE,bundle);
                lp.alpha=0.3f;
                getWindow().setAttributes(lp);
                break;
            case R.id.my_tratus_layout://委托
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_TRUST, bundle);
                break;
            case R.id.my_hold_layout://持仓
            case R.id.btn_sale://卖
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_HOLD, bundle);
                break;
            case R.id.my_transtation_layout://我的交易记录
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_TRANSACTION, bundle);
                break;
            case R.id.my_history_layout://获奖记录
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_RECORD, bundle);
                break;
            case R.id.btn_buy:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_BUY, bundle);
                break;
        }
    }

    protected void showSharePop(Application application) {
        final View layout = View.inflate(getApplicationContext(),R.layout.share_layout, null);
        final PopupWindow pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
        pw.setAnimationStyle(R.style.dialoganimstyle);

        final WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().setAttributes(lp);

        pw.showAtLocation(MatchDescActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha=1.0f;
                getWindow().setAttributes(lp);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null) {
            timer.cancel();
            timer=null;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (timer!=null) {
            timer.cancel();
            timer=null;
        }
    }

}

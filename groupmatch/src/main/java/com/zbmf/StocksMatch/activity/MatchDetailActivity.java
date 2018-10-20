package com.zbmf.StocksMatch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.receiver.ApplySuccessReceiver;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.GetTime;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.utils.UiHelper;

/**
 * 参加的比赛简介
 * @author Administrator
 */
public class MatchDetailActivity extends ExActivity implements View.OnClickListener {

    private TextView tv_title;
    private Button btn_join;
    private MatchBean matchbean;
    private TextView tv_num,jiangping_msg,match_desc,match_jiangpin,apply_time,at_time,init_money;
    private ApplySuccessReceiver receiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchbean = (MatchBean) bundle.getSerializable("matchbean");
        }
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_num=(TextView) findViewById(R.id.tv_num);
        jiangping_msg=(TextView) findViewById(R.id.jiangpin_msg);
        match_desc=(TextView) findViewById(R.id.match_desc);
        match_jiangpin=(TextView) findViewById(R.id.match_jiangpin_msg);
        apply_time=(TextView) findViewById(R.id.apply_time);
        at_time=(TextView) findViewById(R.id.at_time);
        init_money=(TextView) findViewById(R.id.init_money);
        btn_join = (Button) findViewById(R.id.btn_join);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_join).setOnClickListener(this);

        if(matchbean != null){
            tv_title.setText(UiCommon.INSTANCE.subTitle(matchbean.getTitle()));
            jiangping_msg.setText(R.string.match_tip3);
            tv_num.setText(matchbean.getPlayers());
            match_desc.setText(matchbean.getDesc());
            if(matchbean.getAward()!=null&&!matchbean.getAward().trim().equals("")){
                match_jiangpin.setText(matchbean.getAward().replace("  ", "\n"));
            }else{
                match_jiangpin.setText(R.string.match_tip4);
            }

            apply_time.setText(getString(R.string.apply_time)+matchbean.getStart_apply()+getString(R.string.sperator)+matchbean.getEnd_apply());
            at_time.setText(getString(R.string.start_time)+matchbean.getStart_at()+getString(R.string.sperator)+matchbean.getEnd_at());
            Log.e("tag",matchbean.getInit_money());
            init_money.setText(getString(R.string.init_many)+matchbean.getInit_money());
            if(!GetTime.getTimeIsTrue(matchbean.getEnd_at())){//已结束
                btn_join.setText(getString(R.string.isover));
                btn_join.setEnabled(false);
            }else if(!GetTime.getTimeIsTrue(matchbean.getEnd_apply())){//未结束->报名结束
                btn_join.setText(getString(R.string.applyover));
                btn_join.setEnabled(false);
            }else if(matchbean.getIs_match_player().equals("1")){//可以报名
                btn_join.setText(getString(R.string.isplayer));
                btn_join.setEnabled(false);
            }else{
                btn_join.setText(getString(R.string.join_game));
                btn_join.setEnabled(true);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiver == null){
            receiver = new ApplySuccessReceiver();
            UiHelper.RegistBroadCast(this, receiver, Constants.APPLY_SUCCESS);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_join:
                Bundle bundle = new Bundle();
                bundle.putSerializable("matchbean", matchbean);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_APPLYMATCH,bundle);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHelper.UnRegistBroadCast(this, receiver);
    }

    /**
     * 直接通知关闭
     * @param matchBean
     */
    public void updateUi(MatchBean matchBean) {
//        btn_join.setText(getString(R.string.isplayer));
//        btn_join.setEnabled(false);
        finish();
    }
}

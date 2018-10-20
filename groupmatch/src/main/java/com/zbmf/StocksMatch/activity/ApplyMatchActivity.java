package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.listener.MyTextWatcher;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

/**
 * 参加，报名比赛
 *
 * @author Administrator 15/12/30
 */
public class ApplyMatchActivity extends ExActivity implements View.OnClickListener {
    private TextView tv_title, tv_name, tv_question;
    private EditText ed_truename, ed_answer, ed_phone, user_code, ed_code;
    private LinearLayout ll_yzm, ll_question, ll_phone, ll_code;
    private Button btn_code;
    private MatchBean matchbean;
    private int match_status = -1;
    private TimeCount timecount;
    private Get2Api server = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_match);

        getData();
        setupView();
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_question = (TextView) findViewById(R.id.tv_question);
        ed_truename = (EditText) findViewById(R.id.ed_truename);
        ed_answer = (EditText) findViewById(R.id.ed_answer);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        user_code = (EditText) findViewById(R.id.user_code);
        ed_code = (EditText) findViewById(R.id.ed_code);
        ll_yzm = (LinearLayout) findViewById(R.id.ll_yzm);
        ll_question = (LinearLayout) findViewById(R.id.ll_question);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        ll_code = (LinearLayout) findViewById(R.id.ll_code);
        btn_code = (Button) findViewById(R.id.btn_code);
        btn_code.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_apply).setOnClickListener(this);

        tv_name.setText(UiCommon.INSTANCE.getiUser().getUsername());
        ed_phone.setText(UiCommon.INSTANCE.getiUser().getPhone());

        MyTextWatcher watcher = new MyTextWatcher(ed_phone, 11);
        ed_phone.addTextChangedListener(watcher);
        watcher.setListener(new MyTextWatcher.WatchListener() {
            @Override
            public void afterChanged(Editable e) {
                //需要验证码 并且手机号和本地不一致
                if(match_status == 1 && !e.toString().trim().equals(UiCommon.INSTANCE.getiUser().getPhone())){
                    ll_yzm.setVisibility(View.VISIBLE);
                    isyzm = true;
                }else{
                    ll_yzm.setVisibility(View.GONE);
                    isyzm = false;
                }
            }

            @Override
            public void onChanged() {

            }
        });

        if (matchbean != null) {
            tv_title.setText(UiCommon.INSTANCE.subTitle(matchbean.getTitle()));

            //是邀请赛邀请赛类型 0问题, 1邀请码, 2两者结合
            if(matchbean.getInvite_type()!=-1) {
                switch (matchbean.getInvite_type()) {
                    case 0:
                        invitetype = 0;
                        ll_question.setVisibility(View.VISIBLE);
                        tv_question.setText(matchbean.getInvite_method1().getQeustion());
                        break;
                    case 1:
                        invitetype = 1;
                        ll_code.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        invitetype = 2;
                        ll_question.setVisibility(View.VISIBLE);
                        ll_code.setVisibility(View.VISIBLE);
                        tv_question.setText(matchbean.getInvite_method1().getQeustion());
                        break;
                    default:

                        break;
                }
            }

            //是否需要手机号码和验证 1都要  2不要验证码 3都不要
            switch (matchbean.getMatch_status()) {
                case 1:
                    ll_phone.setVisibility(View.VISIBLE);
                    if(UiCommon.INSTANCE.getiUser().getPhone()!=null){
                        ll_yzm.setVisibility(View.GONE);
                    }else{
                        ll_yzm.setVisibility(View.VISIBLE);
                    }
                    match_status = 1;
                    break;
                case 2:
                    ll_phone.setVisibility(View.VISIBLE);
                    match_status = 2;
                    break;
                case 3:
                    match_status = 3;
                    break;
                default:
                    break;
            }
        }
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchbean = (MatchBean) bundle.getSerializable("matchbean");
        }
    }

    private int invitetype=-1;
    private String phone="";
    private String truename="";
    private String invite_code="";
    private String answer="";
    private String code = "";//验证码
    private boolean isyzm = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_apply:
                invite_code = ed_code.getText().toString();
                if (!checkTruename()) return;

                //是邀请赛 0问题, 1邀请码, 2两者结合 -- 比赛类型逐一check
                if(matchbean.getInvite_type()==0){
                    if (match_status==1){
                        if (!checkPhone()) return;
                        if(isyzm)
                            if (!checkYZM()) return;
                    }else if(match_status==2){
                        if (!checkPhone()) return;
                    }
                    if(!checkAnswer()) return;
                    new ApplyMatchTask(ApplyMatchActivity.this,R.string.apply_tip,R.string.load_fail,true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else if(matchbean.getInvite_type()==1){
                    if (match_status==1){
                        if (!checkPhone()) return;
                        if(isyzm)
                            if (!checkYZM()) return;
                    }else if(match_status==2){
                        if (!checkPhone()) return;
                    }
                    if (!checkInviteCode()) return;
                    new CheckInviteCodeTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,invite_code);
                }else if(matchbean.getInvite_type()==2){
                    if (match_status==1){
                        if (!checkPhone()) return;
                        if(isyzm)
                             if (!checkYZM()) return;
                    }else if(match_status==2){
                        if (!checkPhone()) return;
                    }
                    if(!checkAnswer()) return;
                    if(!checkInviteCode()) return;

                    new CheckInviteCodeTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,invite_code);
                }else{//非邀请赛
                    if (match_status==1){//1.手机号为空 2需要验证码并且输入的手机和本地不同
                        if (!checkPhone()) return;
                        if(isyzm)
                            if (!checkYZM()) return;
                    }else if(match_status==2){
                        if (!checkPhone()) return;
                    }
                    new ApplyMatchTask(ApplyMatchActivity.this,R.string.apply_tip,R.string.load_fail,true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btn_code://验证码是可见的，所以match_status=1
                    if (!checkTruename()) return;
                    if (!checkPhone()) return;
                    new GetVerifyCode(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, matchbean.getId(), truename, phone);
                break;
            default:
                break;
        }
    }

    private boolean checkYZM() {
        code = user_code.getText().toString();
        if(TextUtils.isEmpty(code)){
            UiCommon.INSTANCE.showTip(getString(R.string.input_yzm));
            return false;
        }
        return true;
    }

    //真实姓名
    private boolean checkTruename() {
        truename = ed_truename.getText().toString().trim();
        if(TextUtils.isEmpty(truename)){
            UiCommon.INSTANCE.showTip(getString(R.string.input_truename));
            return false;
        }
        return true;
    }

    //手机号
    private boolean checkPhone() {
        phone = ed_phone.getText().toString().trim();
        if(phone.length() == 0){
            UiCommon.INSTANCE.showTip(getString(R.string.phone_tip1));
            return false;
        }
        if(phone.length() != 11){
            UiCommon.INSTANCE.showTip(getString(R.string.phone_tip2));
            return false;
        }
        return true;
    }

    //邀请码OK
    private boolean checkInviteCode() {
        if(TextUtils.isEmpty(invite_code)){
            UiCommon.INSTANCE.showTip(getString(R.string.input_code));
            return false;
        }
        return true;
    }

    //问题OK
    private boolean checkAnswer() {
        answer = ed_answer.getText().toString();
        if(TextUtils.isEmpty(answer)){
            UiCommon.INSTANCE.showTip(getString(R.string.input_answer));
            return false;
        }
        if(!matchbean.getInvite_method1().getAnswer().equals(answer)){
            UiCommon.INSTANCE.showTip(getString(R.string.err_answer));
            return false;
        }

        return true;
    }


    private class GetVerifyCode extends LoadingDialog<String, General> {
        private String matchid;
        private String truename;
        private String mobile;

        public GetVerifyCode(Context activity) {
            super(activity, false, true);
        }

        @Override
        public General doInBackground(String... params) {
            matchid = params[0];
            truename = params[1];
            mobile = params[2];
            General ret = null;

            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getVerifyCode(matchid, truename, mobile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void doStuffWithResult(General ret) {
            if (ret != null && ret.code != -1) {
                if (ret.getStatus() == 1) {
                    if (timecount == null) {
                        timecount = new TimeCount(60000, 1000);
                    }
                    timecount.start();
                    UiCommon.INSTANCE.showTip(getString(R.string.yzm_ok));
                }else
                    UiCommon.INSTANCE.showTip(ret.msg);
            } else {
                UiCommon.INSTANCE.showTip(ApplyMatchActivity.this.getString(R.string.load_fail));
            }
        }
    }

    private class CheckInviteCodeTask extends LoadingDialog<String,General>{

        public CheckInviteCodeTask(Context activity) {
            super(activity, false, true);
        }

        @Override
        public General doInBackground(String... params) {
            String invite_code = params[0];
            General ret = null;
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                ret = server.checkInviteCode(matchbean.getId(),invite_code);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;

        }

        @Override
        public void doStuffWithResult(General ret) {
            if (ret != null && ret.code != -1) {
                if (ret.getStatus() == 1) {//邀请码验证成功-->申请
                    new ApplyMatchTask(ApplyMatchActivity.this,R.string.apply_tip,R.string.load_fail,true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else
                    UiCommon.INSTANCE.showTip(ret.msg);
            } else {
                UiCommon.INSTANCE.showTip(ApplyMatchActivity.this.getString(R.string.load_fail));
            }
        }
    }

    private class ApplyMatchTask extends LoadingDialog<String,MatchBean>{

        public ApplyMatchTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public MatchBean doInBackground(String... params) {
            MatchBean ret = null;
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                ret =  server.applyMatch(matchbean.getId(),truename,phone,code);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void doStuffWithResult(MatchBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    UiCommon.INSTANCE.showTip("报名成功");
                    Bundle bundle = new Bundle();
                    matchbean.setMoney(result.getMoney());
                    matchbean.setUnfrozen_money(result.getUnfrozen_money());
                    bundle.putSerializable("matchbean", matchbean);

                    Intent intent = new Intent();
                    intent.setAction(Constants.APPLY_SUCCESS);
                    intent.putExtra("match",matchbean);
                    sendBroadcast(intent);

                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDESC, bundle);
                    finish();
                } else if(result.msg!=null){
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(ApplyMatchActivity.this.getString(R.string.load_fail));
            }
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btn_code.setText(getString(R.string.reverfiy));
            btn_code.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btn_code.setEnabled(false);
            btn_code.setText(millisUntilFinished / 1000 + getString(R.string.miao));
        }
    }


}


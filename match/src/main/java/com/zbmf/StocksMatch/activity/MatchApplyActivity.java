package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IApplyMatchView;
import com.zbmf.StocksMatch.presenter.ApplyMatchPresenter;
import com.zbmf.StocksMatch.util.EditUtil;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.util.PhoneFormatCheckUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/12/12.
 * 申请参赛
 */

public class MatchApplyActivity extends BaseActivity<ApplyMatchPresenter> implements IApplyMatchView {
    @BindView(R.id.et_user_name)
    TextInputEditText etUserName;
    @BindView(R.id.ti_user_name)
    TextInputLayout tiUserName;
    @BindView(R.id.et_true_name)
    TextInputEditText etTrueName;
    @BindView(R.id.it_true_name)
    TextInputLayout itTrueName;
    @BindView(R.id.et_code)
    TextInputEditText etCode;
    @BindView(R.id.it_code)
    TextInputLayout itCode;
    @BindView(R.id.ll_get_code)
    LinearLayout llGetCode;
    @BindView(R.id.et_user_phone)
    TextInputEditText etUserPhone;
    @BindView(R.id.it_user_phone)
    TextInputLayout itUserPhone;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.et_user_answer)
    TextInputEditText etUserAnswer;
    @BindView(R.id.ti_user_answer)
    TextInputLayout tiUserAnswer;
    @BindView(R.id.ll_question)
    LinearLayout llQuestion;
    @BindView(R.id.invite_code)
    TextInputLayout inviteCode;
    @BindView(R.id.et_invite_code)
    TextInputEditText etInviteCode;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.btn_apply)
    Button btnApply;
    @BindView(R.id.it_emil)
    TextInputLayout itEmail;
    @BindView(R.id.et_emil)
    TextInputEditText etEmail;
    @BindView(R.id.it_school)
    TextInputLayout itSchool;
    @BindView(R.id.et_school)
    TextInputEditText etSchool;
    @BindView(R.id.it_student_number)
    TextInputLayout itStudentNumber;
    @BindView(R.id.et_student_number)
    TextInputEditText etStudentNumber;
    private MatchDescBean.Result matchBean;
    private ApplyMatchPresenter mApplyMatchPresenter;
    private String mPhone;
    private TimeCount timecount;

    @Override
    protected int getLayout() {
        return R.layout.activity_apply_match;
    }

    @Override
    protected String initTitle() {
        return matchBean != null ? matchBean.getMatch_name() : getString(R.string.match_join);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            matchBean = (MatchDescBean.Result) bundle.getSerializable(IntentKey.MATCHBEAN);
            //是否需要手机号码和验证 1都要  2不要验证码 3都不要
            if (matchBean != null) {
//            int match_type = matchBean.getMatch_type();//比赛类型
                switch (matchBean.getMatch_type()) {
                    case 0://公开赛
                        inviteCode.setVisibility(View.GONE);//邀请码
                        llQuestion.setVisibility(View.GONE);//问题答案
                        tiUserName.setVisibility(View.VISIBLE);//手机号、用户名
                        etTrueName.setVisibility(View.VISIBLE);//真实姓名
                        itUserPhone.setVisibility(View.VISIBLE);//手机号
                        llGetCode.setVisibility(View.VISIBLE);//验证码
                        break;
                    case 1://邀请赛
                        inviteCode.setVisibility(View.VISIBLE);//邀请码
                        llQuestion.setVisibility(View.VISIBLE);//问题答案
                        tiUserName.setVisibility(View.VISIBLE);//手机号、用户名
                        etTrueName.setVisibility(View.VISIBLE);//真实姓名
                        itUserPhone.setVisibility(View.VISIBLE);//手机号
                        llGetCode.setVisibility(View.VISIBLE);//验证码
                        setInviteCodeQuest();
                        break;
                    case 2://私密赛
                        itUserPhone.setVisibility(View.VISIBLE);
                        inviteCode.setVisibility(View.GONE);//邀请码
                        llQuestion.setVisibility(View.GONE);//问题答案
                        llGetCode.setVisibility(View.GONE);//验证码
                        break;
                }
                MatchDescBean.Result.RequiredField required_field = matchBean.getRequired_field();
                if (required_field!=null){
                    if (required_field.getSchool()==0){
                        itSchool.setVisibility(View.GONE);
                    }
                    if (required_field.getStudent_id()==0){
                        itStudentNumber.setVisibility(View.GONE);
                    }
                    if (required_field.getEmail()==0){
                        itEmail.setVisibility(View.GONE);
                    }
                    if (required_field.getUsername()==0){
                        itTrueName.setVisibility(View.GONE);
                    }
                }
            }
        }
        etUserName.setText(MatchSharedUtil.NickName());
        etUserPhone.setText(MatchSharedUtil.UserPhone());
    }

    private void setInviteCodeQuest() {
        //是否需要问题和邀请码
        if (matchBean.getInvite_type() != -1) {
            switch (matchBean.getInvite_type()) {
                case 0://只有问题
                    llQuestion.setVisibility(View.VISIBLE);
                    inviteCode.setVisibility(View.GONE);
                    MatchDescBean.Result.InviteMethod1 inviteMethod = matchBean.getInvite_method1();
                    if (inviteMethod != null) {
                        String question = inviteMethod.getQuestion();
                        if (!TextUtils.isEmpty(question)) {
                            tvQuestion.setText(question);
                        }
                    }
                    break;
                case 1://只有邀请码
                    llQuestion.setVisibility(View.GONE);
                    inviteCode.setVisibility(View.VISIBLE);
                    MatchDescBean.Result.InviteMethod1 invite_method1 = matchBean.getInvite_method1();
//                    if (invite_method1 != null) {
//                        String invite_code = invite_method1.getInvite_code();
//                        if (!TextUtils.isEmpty(invite_code)) {
//                            etInviteCode.setText(invite_code);
//                        }
//                    }
                    break;
                case 2://问题和邀请码
                    llQuestion.setVisibility(View.VISIBLE);
                    inviteCode.setVisibility(View.VISIBLE);
                    MatchDescBean.Result.InviteMethod1 invite_method2 = matchBean.getInvite_method1();
                    if (invite_method2 != null) {
                        String question2 = invite_method2.getQuestion();
                        String invite_code = invite_method2.getInvite_code();
                        if (!TextUtils.isEmpty(question2)) {
                            tvQuestion.setText(question2);
                        }
                        if (!TextUtils.isEmpty(invite_code)) {
                            etInviteCode.setText(invite_code);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected ApplyMatchPresenter initPresent() {
        mApplyMatchPresenter = new ApplyMatchPresenter(String.valueOf(matchBean != null ? matchBean.getMatch_id() : 0));
        return mApplyMatchPresenter;
    }

    private Handler mHandler = new Handler();

    @Override
    public void onSucceed() {
        ShowOrHideProgressDialog.disMissProgressDialog();
        showToast(getString(R.string.match_join_success));
        matchBean.setIs_player(1);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //返回报名成功的信息
//                Intent intent = new Intent();
//                intent.putExtra(IntentKey.APPLY_STATUS, IntentKey.APPLY_STATUS_SUCCESS);
//                setResult(Constans.APPLY_RESPONSE_CODE, intent);
                //报名成功跳转参赛的详情页
                ShowActivity.showMatch4(MatchApplyActivity.this,matchBean);
                MatchApplyActivity.this.finish();
            }
        }, 150);
    }

    @Override
    public void onFail(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    //获取申请参赛的验证码成功
    @Override
    public void successCode(BaseBean baseBean) {
        if (baseBean.getStatus()) {
            if (timecount == null) {
                timecount = new TimeCount(60000, 1000);
            }
            timecount.start();
            showTip(getString(R.string.success_get_code));
        } else {
            showTip(MatchApplyActivity.this.getString(R.string.load_fail));
        }
    }

    @Override
    public void sendCodeErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @OnClick({R.id.btn_code, R.id.btn_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                if (matchBean == null) return;
                if (!checkTrueName()) return;
                if (!checkPhone()) return;
                mApplyMatchPresenter.sendCode(matchBean.getMatch_id(), mPhone);
                break;
            case R.id.btn_apply:
                Map<String, String> params = new HashMap<>();
                if (itUserPhone.getVisibility() == View.VISIBLE) {
                    String userPhone = etUserPhone.getText().toString();
                    if (EditUtil.isEmpty(itUserPhone, userPhone, getString(R.string.err_input_phone))) {
                        return;
                    }
                    params.put(ParamsKey.MOBILE, userPhone);
                }
                if (itTrueName.getVisibility() == View.VISIBLE) {
                    String trueName = etTrueName.getText().toString();
                    if (EditUtil.isEmpty(itTrueName, trueName, getString(R.string.err_input_true_name))) {
                        return;
                    }
                    params.put(ParamsKey.TRUENAME, trueName);
                }
                if (llGetCode.getVisibility() == View.VISIBLE) {
                    String code = etCode.getText().toString();
                    if (EditUtil.isEmpty(itCode, code, getString(R.string.err_input_true_name))) {
                        return;
                    }
                    params.put(ParamsKey.CODE, code);
                }
                if (itStudentNumber.getVisibility()==View.VISIBLE){
                    String studentNumber = etStudentNumber.getText().toString();
                    if (EditUtil.isEmpty(itStudentNumber, studentNumber, getString(R.string.err_input_student_number))) {
                        params.put(ParamsKey.STUDENT_ID, studentNumber);
                    }
                }
                if (itSchool.getVisibility()==View.VISIBLE){
                    String studentNumber = etSchool.getText().toString();
                    if (EditUtil.isEmpty(itSchool, studentNumber, getString(R.string.err_input_school))) {
                        params.put(ParamsKey.SCHOOL, studentNumber);
                    }
                }
                if (itEmail.getVisibility()==View.VISIBLE){
                    String studentNumber = etEmail.getText().toString();
                    if (EditUtil.isEmpty(itEmail, studentNumber, getString(R.string.err_input_email))) {
                       return;
                    }
                    params.put(ParamsKey.EMAIL, studentNumber);
                }
                presenter.AppleMatchMode(params);
                ShowOrHideProgressDialog.showProgressDialog(this,this,getString(R.string.applying));
                break;
        }
    }

    //真实姓名
    private boolean checkTrueName() {
        String realName = etTrueName.getText().toString().trim();
        if (TextUtils.isEmpty(realName)) {
            showTip(getString(R.string.err_input_true_name));
            return false;
        }
        return true;
    }

    //手机号
    private boolean checkPhone() {
        mPhone = etUserPhone.getText().toString().trim();
        if (mPhone.length() == 0) {
            showTip(getString(R.string.err_input_phone));
            return false;
        }
        if (mPhone.length() != 11) {
            showTip(getString(R.string.err_input_phone1));
            return false;
        }
        if (!PhoneFormatCheckUtils.isPhoneLegal(mPhone)) {
            showTip(getString(R.string.err_input_phone1));
            return false;
        }
        return true;
    }

    class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            if (btnCode != null) {
                btnCode.setText(getString(R.string.reverfiy));
                btnCode.setEnabled(true);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            if (btnCode != null) {
                btnCode.setEnabled(false);
                btnCode.setText(millisUntilFinished / 1000 + getString(R.string.miao));
            }
        }
    }
}

package com.zbmf.StocksMatch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.HomeMatchList;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.MatchList3Bean;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.StocksMatch.bean.SearchMatchBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IMatchDescView;
import com.zbmf.StocksMatch.presenter.MatchDescPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.worklibrary.util.GetTime;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/12/12.
 * 比赛介绍页面
 */
public class MatchDescActivity extends BaseActivity<MatchDescPresenter> implements IMatchDescView {
    @BindView(R.id.tv_player_num)
    TextView tvPlayerNum;
    @BindView(R.id.tv_join_match)
    TextView tvJoinMatch;
    @BindView(R.id.match_desc)
    TextView matchDesc;
    @BindView(R.id.tv_match_jiangpin_msg)
    TextView tvMatchJiangpinMsg;//奖品信息描述
    @BindView(R.id.tv_apply_time)
    TextView tvApplyTime;
    @BindView(R.id.tv_match_time)
    TextView tvMatchTime;
    @BindView(R.id.init_money)
    TextView initMoney;
    @BindView(R.id.prizeAnnouncement)
    TextView prizeAnnouncement;

    private HomeMatchList.Result.Recommend recmMatch;
    private HomeMatchList.Result.Hot hotMatch;
    private MatchDescBean.Result mResult;
    private MatchNewAllBean.Result.Matches match;
    private MatchList3Bean.Result.Matches match3;
    private SearchMatchBean.Result.Matches searchMatch;
    private MatchNewAllBean.Result.Matches mUserMatch;

    @Override
    protected int getLayout() {
        return R.layout.activity_match_desc_layout;
    }

    @Override
    protected String initTitle() {
        if (recmMatch != null) {
            return recmMatch.getMatch_name();
        } else if (match != null) {
            return match.getMatch_name();
        } else if (hotMatch != null) {
            return hotMatch.getMatch_name();
        } else if (match3 != null) {
            return match3.getMatch_name();
        } else if (searchMatch != null) {
            return searchMatch.getMatch_name();
        } else if (mUserMatch != null) {
            return mUserMatch.getMatch_name();
        } else if (mResult != null) {
            return mResult.getMatch_name();
        } else {
            return "比赛简介";
        }
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            match3 = (MatchList3Bean.Result.Matches) bundle.getSerializable(IntentKey.MATCH3);
            match = (MatchNewAllBean.Result.Matches) bundle.getSerializable(IntentKey.MATCH);
            recmMatch = (HomeMatchList.Result.Recommend) bundle.getSerializable(IntentKey.RECM_MATCH);
            hotMatch = (HomeMatchList.Result.Hot) bundle.getSerializable(IntentKey.HOT_MATCH);
            searchMatch = (SearchMatchBean.Result.Matches) bundle.getSerializable(IntentKey.SEARCH_MATCH);
            if (bundle.getSerializable(IntentKey.USER_MATCH) instanceof MatchNewAllBean.Result.Matches) {
                mUserMatch = (MatchNewAllBean.Result.Matches) bundle.getSerializable(IntentKey.USER_MATCH);
            } else if (bundle.getSerializable(IntentKey.MATCH4) instanceof MatchDescBean.Result) {
                mResult = (MatchDescBean.Result) bundle.getSerializable(IntentKey.MATCH4);
            }
        }
    }

    @Override
    protected MatchDescPresenter initPresent() {
        if (match != null) {
            return new MatchDescPresenter(match.getMatch_id());
        } else if (hotMatch != null) {
            return new MatchDescPresenter(hotMatch.getMatch_id());
        } else if (recmMatch != null) {
            return new MatchDescPresenter(recmMatch.getMatch_id());
        } else if (match3 != null) {
            return new MatchDescPresenter(match3.getMatch_id());
        } else if (searchMatch != null) {
            return new MatchDescPresenter(searchMatch.getMatch_id());
        } else if (mUserMatch != null) {
            return new MatchDescPresenter(mUserMatch.getMatch_id());
        } else if (mResult != null) {
            return new MatchDescPresenter(mResult.getMatch_id());
        } else {
            return new MatchDescPresenter(-1);
        }
//        return new MatchDescPresenter(match == null ? hotMatch.getMatch_id() : match.getMatch_id());
    }

    //申请参加比赛--->跳转报名页面
    @OnClick(R.id.tv_join_match)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.MATCHBEAN, mResult);
        if (ShowActivity.isLogin(this, ParamsKey.MATCH_ORG_OTHER)) {
            ShowActivity.showActivityForResult(this, bundle, MatchApplyActivity.class,
                    Constans.APPLY_REQUEST_CODE);
        }
    }

    //刷新详情数据
    @Override
    public void refreshMatchDesc(MatchDescBean.Result result) {
        if (result != null) {
            this.mResult = result;
            setViewFromData(result);
        }
    }

    private boolean isPopWindow = false;

    //弹窗
    @Override
    public void popWindow(PopWindowBean popWindowBean, int gainStatus, int matchId) {
        if (gainStatus == Constans.GAIN_DATA_SUCCESS && !isPopWindow) {
            //获取弹窗的数据
            ShowActivity.skipPopWindowAct(popWindowBean, this, String.valueOf(matchId));
            isPopWindow = true;
        }
    }

    private void setViewFromData(MatchDescBean.Result result) {
        if (result != null) {
            tvPlayerNum.setText(String.valueOf(result.getPlayers()));
            matchDesc.setText(result.getDesc());
            prizeAnnouncement.setText(TextUtils.isEmpty(result.getAward_remark())
                    ? getString(R.string.no_declarant) : result.getAward_remark());
            tvMatchJiangpinMsg.setText(!TextUtils.isEmpty(result.getAward_detail()) ? result.
                    getAward_detail().replace("  ", "\n") : getString(R.string.match_desc_award));
            tvApplyTime.setText(String.format(getString(R.string.match_apply_date), result.getStart_apply(), result.getEnd_apply()));
            tvMatchTime.setText(String.format(getString(R.string.match_start_date), result.getStart_at(), result.getEnd_at()));


            int init_money = result.getInit_money();
            Log.i("--TAG", "--- initMoney" + init_money);
            initMoney.setText(/*String.format(getString(R.string.match_money), */String.valueOf(result.getInit_money())/*)*/);
            if (!GetTime.getTimeIsTrue(result.getEnd_at())) {//已结束
                tvJoinMatch.setText(getString(R.string.match_is_over));
                tvJoinMatch.setEnabled(false);
            } else if (!GetTime.getTimeIsTrue(result.getEnd_apply())) {
                tvJoinMatch.setText(getString(R.string.match_apply_over));
                tvJoinMatch.setEnabled(false);
            } else if (result.getIs_player()) {
                tvJoinMatch.setText(getString(R.string.match_is_player));
                tvJoinMatch.setEnabled(false);
            } else {
                tvJoinMatch.setText(getString(R.string.match_join));
                tvJoinMatch.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constans.APPLY_REQUEST_CODE && resultCode ==
                Constans.APPLY_RESPONSE_CODE && data != null) {
            int applyStatus = data.getIntExtra(IntentKey.APPLY_STATUS, -1);
            if (applyStatus == IntentKey.APPLY_STATUS_SUCCESS) {//报名成功
                tvJoinMatch.setText(getString(R.string.appled_match));
                tvJoinMatch.setEnabled(false);
            } else if (applyStatus == IntentKey.APPLY_STATUS_FAIL) {//报名失败
                tvJoinMatch.setText(getString(R.string.no_appled_match));
                tvJoinMatch.setEnabled(true);
            }
        }
    }
}

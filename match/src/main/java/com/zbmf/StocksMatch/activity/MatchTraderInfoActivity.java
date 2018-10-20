package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.TraderDealsRecordAdapter;
import com.zbmf.StocksMatch.adapter.TraderHolderAdapter;
import com.zbmf.StocksMatch.adapter.TraderRecordLessAdapter;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.Tracker;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.StocksMatch.bean.TraderInfo;
import com.zbmf.StocksMatch.bean.TraderYield;
import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.listener.IMatchTraderInfo;
import com.zbmf.StocksMatch.presenter.MatchTraderInfoPresenter;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.BottomDialog;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.util.DoubleFromat;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/12/19.
 * 操盘高手
 */

public class MatchTraderInfoActivity extends BaseActivity<MatchTraderInfoPresenter> implements IMatchTraderInfo {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.imv_avatar)
    ImageView imvAvatar;
    @BindView(R.id.tv_deal_days)
    TextView tvDealDays;
    @BindView(R.id.tv_deal_total)
    TextView tvDealTotal;
    @BindView(R.id.tv_deal_success)
    TextView tvDealSuccess;
    @BindView(R.id.tv_total_yield)
    TextView tvTotalYield;
    @BindView(R.id.tv_win_index)
    TextView tvWinIndex;
    @BindView(R.id.tv_index_yield)
    TextView tvIndexYield;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_hold_num)
    TextView tvHoldNum;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    //    @BindView(R.id.tv_commit_look)
//    TextView tvCommitLook;    //订阅后查看持仓--状态是：没有订阅
    @BindView(R.id.trader_deal_list)
    ListViewForScrollView traderDealList;
    @BindView(R.id.tv_commit_look_hold)
    TextView tvCommitLookHold;    //查看完整的持仓记录: 已经订阅
    @BindView(R.id.layout_commit_look_hold)
    LinearLayout layoutCommitLookHold;
    @BindView(R.id.tv_no_hold)
    TextView tvNoHold;
    @BindView(R.id.hold_layout)
    LinearLayout holdLayout;
    @BindView(R.id.trader_deal_history_list)
    ListViewForScrollView traderDealHistoryList;
    @BindView(R.id.tv_commit_look_history)
    TextView tvCommitLookHistory;
    @BindView(R.id.layout_commit_look_history)
    LinearLayout layoutCommitLookHistory;
    @BindView(R.id.trader_scrollview)
    PullToRefreshScrollView traderScrollview;
    @BindView(R.id.trader_bottom_line)
    View traderBottomLine;
    @BindView(R.id.tv_commit_content)
    TextView tvCommitContent;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.trader_bottom_layout)
    LinearLayout traderBottomLayout;
    private Traders traders;
    private boolean is_track;
    private TraderRecordLessAdapter mTraderRecordLessAdapter;
    private TraderDealsRecordAdapter mTraderDealsRecordAdapter;
    private BottomDialog mDialog;
    private TraderHolderAdapter mTraderHolderAdapter;
    private MatchInfo matchInfo;
    private MatchTraderInfoPresenter mMatchTraderInfoPresenter;

    public MatchTraderInfoActivity() {
    }

    private void setUserMessage() {
        if (traders != null) {
            tvName.setText(traders.getNickname());
            tvDesc.setText(traders.getProfile() != null ? traders.getProfile() : "");//描述,简介
            tvDate.setText(traders.getJoin_at() != null ? String.format(getString(R.string.trader_join_date), traders.getJoin_at()) : "");
            Glide.with(this).load(traders.getAvatar()).apply(GlideOptionsManager.getInstance()
                    .getRequestOptions()).into(imvAvatar);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setTraderYield(TraderYield traderYield) {
        if (traderYield != null) {
            tvDealDays.setText(traderYield.getDeal_days() + "");
            tvDealTotal.setText(traderYield.getDeal_total() + "");
            tvDealSuccess.setText(setTvString(traderYield.getDeal_success()));
            String total_yield = traderYield.getTotal_yield();
            tvTotalYield.setText(setTvString(total_yield));
            tvWinIndex.setText(setTvString(traderYield.getWin_index()));
            tvIndexYield.setText(setTvString(traderYield.getIndex_yield()));
            tvTotalMoney.setText(DoubleFromat.getStockDouble(Double.parseDouble(traderYield.getTotal_money()), 2));
            tvHoldNum.setText(traderYield.getHold_num() + "");
            tvPosition.setText(setTvString(traderYield.getPosition()));
            tvTotalYield.setTextColor(traderYield.getTotal_yield() == null ? this.getResources().getColor(R.color.black_66) :
                    (Double.parseDouble(traderYield.getTotal_yield()) > 0 ? getResources().
                            getColor(R.color.red) : getResources().getColor(R.color.green)));
            tvWinIndex.setTextColor(traderYield.getWin_index() == null ? this.getResources().getColor(R.color.black_66) :
                    (Double.parseDouble(traderYield.getWin_index()) > 0 ? getResources().
                            getColor(R.color.red) : getResources().getColor(R.color.green)));
            tvDealSuccess.setTextColor(traderYield.getDeal_success() == null ? this.getResources().getColor(R.color.black_66) :
                    (Double.parseDouble(traderYield.getDeal_success()) > 0 ? getResources().
                            getColor(R.color.red) : getResources().getColor(R.color.green)));
            tvIndexYield.setTextColor(traderYield.getIndex_yield() == null ? this.getResources().getColor(R.color.black_66) :
                    (Double.parseDouble(traderYield.getIndex_yield()) > 0 ? getResources().
                            getColor(R.color.red) : getResources().getColor(R.color.green)));
        }
    }

    private int setTvRateColor(String rate) {
        return rate == null ? this.getResources().getColor(R.color.black_66) : (Double.parseDouble(rate) >= 0 ? this.getResources()
                .getColor(R.color.red) : this.getResources().getColor(R.color.green));
    }

    private String setTvString(String rate) {
        return rate == null ? "" : ((Double.parseDouble(rate) > 0 ? "+" : "") +
                DoubleFromat.getStockDouble(Double.parseDouble(rate) * 100, 2) + "%");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_trader_detail_layout;
    }

    @Override
    protected String initTitle() {
        return  getString(R.string.match_trader_info);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            traders = (Traders) bundle.getSerializable(IntentKey.TRADER);
            setUserMessage();
        }
//        if (mDialog == null) {
//            mDialog = resetAccDialog();
//        }
//        mDialog.show();
        //高手持仓的不完整股票数据
        mTraderHolderAdapter = new TraderHolderAdapter(this);
        traderDealList.setAdapter(mTraderHolderAdapter);
        //高手成交的历史记录
        //不完整记录
        mTraderRecordLessAdapter = new TraderRecordLessAdapter(this);
        //完整的记录
        mTraderDealsRecordAdapter = new TraderDealsRecordAdapter(this);
        traderScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getPresenter().getDatas();
            }
        });
    }

    @Override
    protected MatchTraderInfoPresenter initPresent() {
        mMatchTraderInfoPresenter = new MatchTraderInfoPresenter(String.valueOf(traders.getUser_id()));
        return mMatchTraderInfoPresenter;
    }

    @Override
    public void rushMatchBean(MatchInfo matchInfo) {
        if (matchInfo != null) {
            this.matchInfo = matchInfo;
//            double paynum = matchInfo.getPaynum();
        }
    }

    @Override
    public void rushMatchBeanErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @Override
    public void traderBuyState(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
            if (msg.equals(getString(R.string.pay_success))) {
                tvCommitLookHold.setVisibility(View.GONE);
                tvCommitLookHistory.setVisibility(View.GONE);
                tvCommitLookHold.setText(getString(R.string.check_full_holder));
                tvCommitLookHistory.setText(getString(R.string.check_full_record));
                mMatchTraderInfoPresenter.getTraderInfo(String.valueOf(traders.getUser_id()));
                mMatchTraderInfoPresenter.getMatchDetail(String.valueOf(matchInfo.getResult().getMatch_id()), MatchSharedUtil.UserId());
                mMatchTraderInfoPresenter.traderHolderPositionRecord(String.valueOf(traders.getUser_id()));
            }
        }
        mDialog.dissMissI();
    }

    @Override
    public void onRushTraderInfo(TraderInfo traderInfo) {
        if (traderScrollview != null && traderScrollview.isRefreshing()) {
            traderScrollview.onRefreshComplete();
        }
        if (traderInfo.getTrader() != null) {
            traders = traderInfo.getTrader();
            setUserMessage();
        }
        if (traderInfo.getTracker() != null) {
            Tracker tracker = traderInfo.getTracker();
            is_track = tracker.getIs_track() == 1;  // 1表示已经订阅   否则表示没有订阅
//            is_track = true;//测试，如果已经订阅了  true 表示订阅 false 表示没有订阅
//            tvCommitLook.setVisibility(is_track?View.GONE:View.VISIBLE); //如果没有订阅 则显示订阅后显示持仓按钮
//            tvCommitLookHold.setVisibility(is_track?View.VISIBLE:View.GONE);//如果订阅 则显示查看完整持仓按钮
            tvCommitLookHold.setVisibility(View.VISIBLE);
            tvNoHold.setText(is_track ? R.string.check_full : R.string.no_holder_position);
            layoutCommitLookHistory.setVisibility(View.VISIBLE);//操盘高手交易记录
//            holdLayout.setVisibility(is_track ? View.VISIBLE : View.GONE);//持仓布局
            tvCommitLookHistory.setVisibility(View.VISIBLE);
            tvCommit.setText(is_track ? "续订" : "订阅");
            tvCommitContent.setText(is_track ? "有效期至" + tracker.getExpired_at() : "订阅后可查看操盘记录");
//            getTraderHolderPositionRecord();//获取操盘高手的持仓数据
        }
        if (is_track){
            tvCommitLookHold.setText(getString(R.string.check_full_holder));
            tvCommitLookHistory.setText(getString(R.string.check_full_record));
        }
        if (traderInfo.getYield() != null) {
            setTraderYield(traderInfo.getYield());
        }
        //往交易记录的Adapter中添加不完整的交易记录
        if (traderInfo.getDeals() != null) {
            List<TraderInfo.Deals> deals = traderInfo.getDeals();
            mTraderRecordLessAdapter.setList(deals);
            traderDealHistoryList.setAdapter(mTraderRecordLessAdapter);
        }
    }

    private List<TraderHolderPosition.Holds> limitList = new ArrayList<>();

    //高手持仓数据
    @Override
    public void onRefreshHolderRecord(List<TraderHolderPosition.Holds> holds) {
        tvNoHold.setVisibility(holds.size() > 0 ? View.GONE : View.VISIBLE);
        limitList.clear();
        for (int i = 0; i < holds.size(); i++) {
            if (i < 2) {
                limitList.add(holds.get(i));
            }
        }
        if (mTraderDealsRecordAdapter.getList() == null) {
            mTraderHolderAdapter.setList(limitList);
        }else {
            mTraderHolderAdapter.clearList();
            mTraderHolderAdapter.addList(limitList);
        }
    }

    //高手交易记录数据
    @Override
    public void onRefreshDealRecord(TraderDeals.Result result) {
//        int page1 = result.getPage();
        int total = result.getTotal();
        List<TraderDeals.Result.Deals> deals = result.getDeals();
        if (mTraderDealsRecordAdapter.getList() == null) {
            if (deals != null) {
                mTraderDealsRecordAdapter.setList(deals);
            }
        } else {
            if (mTraderDealsRecordAdapter.getList().size() < total) {
                mTraderDealsRecordAdapter.addList(deals);
            }
        }
        if (traderDealHistoryList.getAdapter() != mTraderDealsRecordAdapter) {
            traderDealHistoryList.setAdapter(mTraderDealsRecordAdapter);
        }
    }

    @OnClick({R.id.tv_commit_look_hold, R.id.tv_commit_look_history,/*R.id.tv_commit_look,*/ R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_commit_look_hold:
                if (is_track) {//订阅-->查看完整持仓列表
                    ShowActivity.showMatchHoldActivity(this, matchInfo,String.valueOf(matchInfo.getResult().getMatch_id()) ,
                            String.valueOf(traders.getUser_id()),IntentKey.TRADER_FLAG_INT);
                } else {//没有订阅-->从窗口底部弹出充值对话框
                    showDialog(getString(R.string.pay_dialog_tip_holder));
                }
                break;
            case R.id.tv_commit_look_history:
                if (is_track) {//订阅
                    //点击跳转 交易记录页面
                    ShowActivity.showDealActivity(this, String.valueOf(traders.getUser_id()),IntentKey.TRADER_FLAG,"","");
                } else {//没有订阅 从窗口底部弹出充值对话框
                    showDialog(getString(R.string.pay_dialog_tip));
                }
                break;
           /* case R.id.tv_commit_look:
                //从窗口底部弹出订阅对话框---充值
                showToast("订阅后方可查看持仓列表");
                break;*/
            case R.id.tv_commit:
                showDialog(getString(R.string.pay_dialog_tip));
                break;
        }
    }

    private UserWallet userWallet;

    @Override
    public void userWallet(UserWallet userWallet) {
        if (userWallet != null) {
            this.userWallet = userWallet;
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_PAY, userWallet.getPay().getUnfrozen());
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_POINT, userWallet.getPoint().getUnfrozen());
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_COUPON, userWallet.getCoupon().getUnfrozen());
        }
    }

    @Override
    public void userWalletErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
        }
    }

    private void showDialog(String dealOrHolder) {//魔方宝充值提示的dialog
//        String sum = SharedpreferencesUtil.getInstance().getString(SharedKey.MFB_PAY, "0.00");
        String mfb = userWallet.getPay().getUnfrozen();
        final Double my_mfb = Double.valueOf(mfb);
        if (mDialog == null) {
            mDialog = new BottomDialog(this, R.style.Theme_Light_Dialog);
        }
        mDialog.showDialog().setText1(dealOrHolder).setText4(String.valueOf(traders.getPrice_month()) + getString(R.string.mfb)).setText3(mfb)
                .showI().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_mfb < traders.getPrice_month()) {
                    mDialog.setText2(getString(R.string.trder_pay_tip))
                            .setText2Color(MatchTraderInfoActivity.this.getResources()
                                    .getColor(R.color.err_red))
                            .setBtnTv(getString(R.string.go_to_pay))
                            .setBtnVisibility(View.GONE)
                            .setBtn1Visibility(View.VISIBLE);
                } else {
                    mMatchTraderInfoPresenter.traderBuy(String.valueOf(traders.getUser_id()));
                }
            }
        });
        mDialog.setOnBtn1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.goToAppGroup(MatchTraderInfoActivity.this);
                mDialog.dissMissI();
            }
        });
        mDialog.closeDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dissMissI();
            }
        });
    }
}

package com.zbmf.StocksMatch.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.activity.StockCommitActivity;
import com.zbmf.StocksMatch.adapter.MatchDetailStockHoldAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.listener.BuyClick;
import com.zbmf.StocksMatch.listener.IDrillFragment;
import com.zbmf.StocksMatch.listener.SellClick;
import com.zbmf.StocksMatch.presenter.DrillPresenter;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.util.StockSellEvent;
import com.zbmf.StocksMatch.util.ToastUtils;
import com.zbmf.StocksMatch.view.BuyDialog;
import com.zbmf.StocksMatch.view.CustomMarqueeTextView;
import com.zbmf.StocksMatch.view.CustomMyCProgress;
import com.zbmf.StocksMatch.view.CustomMyProgress;
import com.zbmf.StocksMatch.view.MyIncreaseView;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.util.DoubleFromat;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 训练  ---  同炒股圈子
 * Created by xuhao on 2017/11/27.
 */

public class DrillFragment extends BaseFragment<DrillPresenter> implements IDrillFragment
        , MatchDetailStockHoldAdapter.OnCommit, SellClick, BuyClick, AdapterView.OnItemClickListener {
    @BindView(R.id.tv_all_num)
    TextView tvAllNum;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_all_asset)
    TextView tvAllAsset;
    @BindView(R.id.tv_can_use)
    TextView tvCanUse;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_day_yield)
    TextView tvDayYield;
    @BindView(R.id.custom_day_progress)
    CustomMyProgress customDayProgress;
    @BindView(R.id.tv_week_yield)
    TextView tvWeekYield;
    @BindView(R.id.custom_week_progress)
    CustomMyProgress customWeekProgress;
    @BindView(R.id.btn_reset)
    TextView btnReset;
    @BindView(R.id.simulate_hold_list)
    ListViewForScrollView simulateHoldList;
    @BindView(R.id.tv_day_reached)
    MyIncreaseView tvDayReached;
    @BindView(R.id.tv_week_reached)
    MyIncreaseView tvWeekReached;
    @BindView(R.id.btn_all_hold)
    TextView btnAllHold;
    @BindView(R.id.notice_text)
    CustomMarqueeTextView noticeTv;
    @BindView(R.id.no_holder)
    TextView noHolder;
    @BindView(R.id.plv)
    PullToRefreshScrollView plv;
    //    @BindView(R.id.ll_none)
//    LinearLayout llNone;
//    @BindView(R.id.no_message_text)
//    TextView noMessage;
    private MatchDetailStockHoldAdapter adapter;
    private double paynum;
    private MatchInfo matchInfo;
    private DrillPresenter mDrillPresenter;
    private RelativeLayout mRl_reset_state;
    private CustomMyCProgress mMCustomProgress;
    private Button mBtnStartReset;
    private TextView mTv_finish_tip;
    private BuyDialog mBuyDialog;
    private NoticeBean.Result mNotice;
    private Handler mHandler = new Handler();

    public static DrillFragment newInstance() {
        return new DrillFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_drill_layout;
    }

    @Override
    protected DrillPresenter initPresent() {
        mDrillPresenter = new DrillPresenter(Constans.MATCH_ID);
        return mDrillPresenter;
    }

    @Override
    protected void initView() {
        setTitleMessage(getString(R.string.drill));
    }

    @Override
    protected void initData() {
        // 注册订阅者
        EventBus.getDefault().register(this);
        plv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//只能刷新
        ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
        adapter = new MatchDetailStockHoldAdapter(getActivity());
        simulateHoldList.setAdapter(adapter);
        adapter.setOnClickListener(this);
        simulateHoldList.setOnItemClickListener(this);
        if (mBuyDialog == null) {
            mBuyDialog = new BuyDialog(getActivity(), R.style.Buy_Dialog).createDialog();
        }
        plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
                getPresenter().setFirst(true);
                getPresenter().getDatas();
            }
        });
    }

    @OnClick({R.id.tv_hold, R.id.tv_buy, R.id.tv_sell_id, R.id.tv_match_trusts, R.id.tv_log_list,
            R.id.tv_record, R.id.btn_reset, R.id.btn_all_hold, R.id.ll_stock_chat, R.id.notice_layout,
            R.id.notice_right_arrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_hold:
            case R.id.tv_sell_id:
            case R.id.btn_all_hold:
                ShowActivity.showMatchHoldActivity(getActivity(), matchInfo, Constans.MATCH_ID,
                        MatchSharedUtil.UserId(), IntentKey.DEFAULT_FLAG_INT);
                break;
            case R.id.tv_buy:
                ShowActivity.showStockBuyActivity(getActivity(), matchInfo, -1,
                        null, Constans.BUY_FLAG, Constans.MATCH_ID);
                break;
            case R.id.tv_match_trusts://撤单
                ShowActivity.showMyTrustActivity(getActivity(), matchInfo, Constans.MATCH_ID);
                break;
            case R.id.tv_log_list://查询
                ShowActivity.showDealsListActivity(getActivity(), matchInfo, Constans.MATCH_ID);
                break;
            case R.id.tv_record:
                ShowActivity.showUserPrizeActivity(getActivity(), matchInfo, Constans.MATCH_ID);
                break;
            case R.id.btn_reset://重置
                getWolle();
                break;
            case R.id.ll_stock_chat://跳转排行页面
                ShowActivity.showMatchRankActivity(getActivity(), Constans.MATCH_ID, Constans.NEW_DEAL_FLAG, false);
                break;
            case R.id.notice_layout:
            case R.id.notice_right_arrow:
                ShowActivity.showNoticeActivity(getActivity(), mNotice);
                break;
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void rushMatchBean(MatchInfo matchInfo) {
        if (plv.isRefreshing()) {
            plv.onRefreshComplete();
        }
        ShowOrHideProgressDialog.disMissProgressDialog();
        MatchInfo.Result result = null;
        if (matchInfo != null) {
            this.matchInfo = matchInfo;
            result=matchInfo.getResult();
        }
        if (result==null){
            return;
        }
        tvAllNum.setText(String.valueOf(result.getPlayers()));
        if (result.getTotal_yield() >= 0) {
            tvProfit.setText("+" + DoubleFromat.getStockDouble(result.getTotal_yield() * 100, 2) + "%");
            btnReset.setVisibility(View.GONE);
//            btnReset.setVisibility(View.VISIBLE);
        } else {
            btnReset.setVisibility(View.VISIBLE);
            tvProfit.setText(DoubleFromat.getStockDouble(result.getTotal_yield() * 100, 2) + "%");
        }
        paynum = result.getPaynum();
        tvAllAsset.setText(String.valueOf(result.getTotal()));
        tvCanUse.setText(DoubleFromat.getStockDouble(result.getUnfrozen(), 2));
        tvProfit.setTextColor(result.getTotal_yield() >= 0 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.green));
        tvWeekYield.setTextColor(result.getWeek_yield() >= 0 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.green));
        tvDayYield.setTextColor(result.getDay_yield() >= 0 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.green));
        tvWeekYield.setText(String.format("%+.2f%%", result.getWeek_yield() * 100));
        tvDayYield.setText(String.format("%+.2f%%", result.getDay_yield() * 100));
        int maxDayNum = result.getPlayers(),
                maxWeekNum = result.getPlayers();
        int currDayReachedNum = result.getDay_rank() > 0 ? maxDayNum - result.getDay_rank() : 0;
        int currWeekReachedNum = result.getWeek_rank() > 0 ? maxDayNum - result.getWeek_rank() : 0;
        tvDayReached.setMax(currDayReachedNum);
        tvWeekReached.setMax(currWeekReachedNum);
        tvDayReached.increaseBarBrother();
        tvWeekReached.increaseBarBrother();
        customDayProgress.setMax(maxDayNum);
        customDayProgress.setInitProgress(currDayReachedNum);
        customDayProgress.displayNiuAnim();
        customWeekProgress.setMax(maxWeekNum);
        customWeekProgress.setInitProgress(currWeekReachedNum);
        customWeekProgress.displayNiuAnim();
    }

//    @Override
//    public void rushHold(StockHoldList stockHoldList) {
//
//    }

    @Override
    public void rushHoldErr(String msg) {
        if (plv.isRefreshing()) {
            plv.onRefreshComplete();
        }
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            if (ShowActivity.checkLoginStatus(getActivity(), msg)) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putInt(ParamsKey.FG_FLAG, ParamsKey.FG_DILL_V);
                        ShowActivity.showActivity(getActivity(), bundle, LoginActivity.class);
//                        showToast(getString(R.string.need_login));
                        ToastUtils.rectangleSingleToast(getString(R.string.need_login));
                        getActivity().finish();
                    }
                }, Constans.DELAY_TIME);
            }
        }
    }

    @Override
    public void userWallet(UserWallet userWallet) {
        if (userWallet != null) {
//            UserWallet userWallet1 = userWallet;
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_PAY, userWallet.getPay().getUnfrozen());
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_POINT, userWallet.getPoint().getUnfrozen());
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_COUPON, userWallet.getCoupon().getUnfrozen());
            Dialog dialog = resetAccDialog();
            dialog.show();
        }
    }

    @Override
    public void userWalletErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Log.e("--TAG", "-----------用户余额 " + msg);
        }
    }

    private int page=ParamsKey.D_PAGE;
    @Override
    public void resetOnSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
            //更新用户参赛数据   账户回位,清空持仓数据
            mDrillPresenter.getMatchDetail(Constans.MATCH_ID, MatchSharedUtil.UserId());
            mDrillPresenter.getMatchHolder(Constans.MATCH_ID,page, MatchSharedUtil.UserId());
            showCircleProgress();
        }
    }

    @Override
    public void resetOnFail(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @Override
    public void notice(NoticeBean.Result notice) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (notice != null) {
            this.mNotice = notice;
            String subject = notice.getAnnouncements().get(0).getSubject();
            noticeTv.setText(subject);
        }
    }

    @Override
    public void noticeErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }
    private List<HolderPositionBean.Result.Stocks> mStocksList = new ArrayList<>();
    private List<HolderPositionBean.Result.Stocks> mStocksList1 = new ArrayList<>();
    @Override
    public void RushHoldList(HolderPositionBean.Result result) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (result != null) {
            List<HolderPositionBean.Result.Stocks> stocks1 = result.getStocks();
//            int total = result.getTotal();
//            if (stockHoldsBeans.size() > 0){
//                noMessage.setText(getString(R.string.no_holder_record));
//                llNone.setVisibility(View.VISIBLE);
//            }else {
//                llNone.setVisibility(View.GONE);
//            }
            btnAllHold.setVisibility(stocks1.size() > 0 ? View.VISIBLE : View.GONE);
            noHolder.setVisibility(stocks1.size() > 0 ? View.GONE : View.VISIBLE);
            int size = stocks1.size();
            if (stocks1.size() > 3) {
                size = 3;
            }
            mStocksList.clear();
            for (int i = 0; i < size; i++) {
//                HolderPositionBean.Result.Stocks stocks = new HolderPositionBean.Result.Stocks();
//                stocks.setPrice_buy(stockHoldsBeans.get(i).getPrice_buy());
//                stocks.setClose(stockHoldsBeans.get(i).getClose());
//                stocks.setCreated_at(stockHoldsBeans.get(i).getCreated_at());
//                stocks.setCurrent(stockHoldsBeans.get(i).getCurrent());
//                stocks.setId(stockHoldsBeans.get(i).getId());
//                stocks.setName(stockHoldsBeans.get(i).getName());
//                stocks.setYield_float(stockHoldsBeans.get(i).getYield_float());
//                stocks.setPrice2(stockHoldsBeans.get(i).getPrice2());
//                stocks.setPrice_sell(stockHoldsBeans.get(i).getPrice_sell());
//                stocks.setProfit(stockHoldsBeans.get(i).getProfit());
//                stocks.setSymbol(stockHoldsBeans.get(i).getSymbol());
//                stocks.setVolumn_total(Integer.parseInt(stockHoldsBeans.get(i).getVolumn_total()));
//                stocks.setVolumn_unfrozen(Integer.parseInt(stockHoldsBeans.get(i).getVolumn_unfrozen()));
//                stocks.setPrice_float(stockHoldsBeans.get(i).getPrice_float());
//                stocks.setCommnet_count(stockHoldsBeans.get(i).getComment_count());
                HolderPositionBean.Result.Stocks stocks = stocks1.get(i);
                mStocksList.add(stocks);
            }
            if (adapter.getList() == null) {
                mStocksList1.addAll(mStocksList);
                adapter.setList(mStocksList1);
            } else {
                adapter.clearList();
                adapter.addList(mStocksList);
            }
        }
    }

    @Override
    public void holderListErr(String msg) {

    }

    private void showCircleProgress() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mMCustomProgress, "progress", 0f, 100f);
        anim.setDuration(2000);
        anim.start();
        mMCustomProgress.setStateListener(new CustomMyCProgress.StateListener() {
            @Override
            public void loadFinished() {
                mTv_finish_tip.setVisibility(View.VISIBLE);
                mBtnStartReset.setEnabled(false);
            }
        });
    }

    private void getWolle() {
        mDrillPresenter.getUserWallet();
    }

    @SuppressLint("SetTextI18n")
    private Dialog resetAccDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        final LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.reset_account, null);
        final Button btn_confirm_reset = (Button) layout.findViewById(R.id.btn_confirm_reset);
        TextView tv_account_remain = (TextView) layout.findViewById(R.id.tv_account_remain);
        mTv_finish_tip = (TextView) layout.findViewById(R.id.tv_finish_tip);
        TextView tv_tip1 = (TextView) layout.findViewById(R.id.tv_tip1);
        TextView tv_paynum = (TextView) layout.findViewById(R.id.tv_paynum);
        mRl_reset_state = (RelativeLayout) layout.findViewById(R.id.rl_reset_state);
        mBtnStartReset = (Button) layout.findViewById(R.id.btn_start_op);
        mMCustomProgress = (CustomMyCProgress) layout.findViewById(R.id.cc_progress);
        mMCustomProgress.setdefaultTextStr(getString(R.string.reseting));
        tv_paynum.setText(paynum==0?99 + getString(R.string.mfb)
                :DoubleFromat.getStockDouble(paynum, 0) + getString(R.string.mfb));
//        String unfrozen = mUserWallet1.getPay().getUnfrozen();
        String mfb = SharedpreferencesUtil.getInstance().getString(SharedKey.MFB_PAY, "0.00");
        tv_account_remain.setText(getString(R.string.account_sum) + mfb + getString(R.string.mfb));
        final Double my_mfb = Double.valueOf(mfb);
        if (my_mfb < paynum) {
            btn_confirm_reset.setText(getString(R.string.go_to_pay));
            tv_tip1.setText(getString(R.string.need_pay_tip));
        } else {
            btn_confirm_reset.setText(getString(R.string.comfirm_reset));
        }
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(false);
        layout.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_confirm_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_mfb < paynum) {//魔方宝小于设置额度,跳转充值
                    showToast(getString(R.string.pay_tip));
                    ShowActivity.goToAppGroup(getActivity());
                    SharedpreferencesUtil.getInstance().removeKey(SharedKey.MFB_PAY);
//                    ShowActivity.showActivity(getActivity(), PayDetailActivity.class);//跳转充值页面
                    dialog.dismiss();
                } else {//重置
                    mRl_reset_state.setVisibility(View.VISIBLE);
                    mBtnStartReset.setEnabled(true);
                    mDrillPresenter.resetMatch(Constans.MATCH_ID);
                }
            }
        });
        mBtnStartReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onCommit(HolderPositionBean.Result.Stocks stockholdsBean) {
        if (stockholdsBean != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.STOCK_HOLDER, new StockMode(stockholdsBean.getName(), stockholdsBean.getSymbol()));
            ShowActivity.showActivityForResult(getActivity(), bundle, StockCommitActivity.class, Constans.ADD_COMMENT_REQUEST);
        }
    }

    //买入股票
    @Override
    public void buyClick(HolderPositionBean.Result.Stocks stocks) {
        if (stocks != null) {
            ShowActivity.showStockBuyActivity(getActivity(), matchInfo, stocks.getVolumn_unfrozen(),
                    new StockMode(stocks.getName(), stocks.getSymbol()), Constans.BUY_FLAG, Constans.MATCH_ID);
            mBuyDialog.dissMissI();
        }
    }

    //卖出股票
    @Override
    public void sellClick(HolderPositionBean.Result.Stocks stocks) {
        if (stocks != null) {
            ShowActivity.showStockBuyActivity(getActivity(), matchInfo,
                    stocks.getVolumn_unfrozen(), new StockMode(stocks.getName(), stocks.getSymbol()),
                    Constans.SELL_FLAG, Constans.MATCH_ID);
            mBuyDialog.dissMissI();
        }
    }

    //点击持仓  卖出或买入
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HolderPositionBean.Result.Stocks item = adapter.getItem(position);
        mBuyDialog.setStocks(item).showI().setSellClick(this).setBuyClick(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constans.ADD_COMMENT_REQUEST && resultCode == Constans.ADD_COMMENT_RESPONSE && data != null) {
            int intExtra = data.getIntExtra(IntentKey.COMMENT_COUNT, -1);
            adapter.setCommentCount(intExtra);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StockSellEvent event) {
        Log.i("--TAG", "message is " + event.getMessage());
        // 更新持股信息
        mDrillPresenter.getMatchHolder(Constans.MATCH_ID,page, MatchSharedUtil.UserId());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}

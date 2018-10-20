package com.zbmf.StockGroup.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ChatStockHoldAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchAnnouncements;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.DisplayUtil;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.CustomMyCProgress;
import com.zbmf.StockGroup.view.CustomMyProgress;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.MyIncreaseView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SimulateStockAccActivity extends BaseActivity implements View.OnClickListener {

    //    private AnimationDrawable mAnim;
    private TextView tv_buy, tv_all_num, tv_all_asset, tv_can_use, tv_profit, tv_week_yield, tv_day_yield;
    private MyIncreaseView tv_day_reached, tv_week_reached;
    private CustomMyProgress mCustomDayProgress, mCustomWeekProgress;
    private MatchInfo matchInfo;
    private ListViewForScrollView simulate_hold_list;
    private List<StockholdsBean> infolist;
    private ChatStockHoldAdapter adapter;
    private double paynum;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private TextView tv_match_message;
    private LinearLayout rl_announcements;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_simulate_stock_acc;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.deal));
//        ImageView iv_niu =getView(R.id.iv_niuc);
//        mAnim = (AnimationDrawable) iv_niu.getBackground();
        tv_day_reached = getView(R.id.tv_day_reached);
        tv_week_reached = getView(R.id.tv_week_reached);
        tv_buy = getView(R.id.tv_buy);
        mCustomDayProgress = getView(R.id.custom_day_progress);
        mCustomWeekProgress = getView(R.id.custom_week_progress);
        tv_all_num = getView(R.id.tv_all_num);
        tv_all_asset = getView(R.id.tv_all_asset);
        tv_can_use = getView(R.id.tv_can_use);
        tv_profit = getView(R.id.tv_profit);
        tv_week_yield = getView(R.id.tv_week_yield);
        tv_day_yield = getView(R.id.tv_day_yield);
        simulate_hold_list = getView(R.id.simulate_hold_list);
        mPullToRefreshScrollView = getView(R.id.mPullToRefreshScrollView);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        rl_announcements = getView(R.id.notice_layout);
        tv_match_message = getView(R.id.notice_text);
        getView(R.id.icon_notice_close_img).setOnClickListener(this);
        rl_announcements.setOnClickListener(this);
    }

    @Override
    public void initData() {
        infolist = new ArrayList<>();
        adapter = new ChatStockHoldAdapter(this);
        adapter.setList(infolist);
        simulate_hold_list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RushData();
    }


    private void RushData() {
//        WebBase.getPlayer(new JSONHandler() {
        WebBase.getMatchPlayer(new JSONHandler() {
            @SuppressLint({"SetTextI18n", "DefaultLocale", "AnimatorKeep"})
            @Override
            public void onSuccess(JSONObject obj) {
                mPullToRefreshScrollView.onRefreshComplete();
                matchInfo = JSONParse.getMatchMessage1(obj);
//                matchInfo = JSONParse.getMatchMessage(obj);
                if (matchInfo != null) {
                    paynum = matchInfo.getPaynum();
                    tv_all_num.setText(matchInfo.getPlayers());
                    tv_all_asset.setText(DoubleFromat.getStockDouble(matchInfo.getTotal(), 2));
                    Double unfrozen = matchInfo.getUnfrozen();
                    tv_can_use.setText(DoubleFromat.getStockDouble(unfrozen, 2));
                    LogUtil.e(DoubleFromat.getStockDouble(matchInfo.getYield() * 100, 2));
                    if (matchInfo.getTotal_yield() >= 0) {
                        tv_profit.setText("+" + DoubleFromat.getStockDouble(matchInfo.getTotal_yield() * 100, 2) + "%");
                        getView(R.id.btn_reset).setVisibility(View.GONE);
//                    getView(R.id.btn_reset).setVisibility(View.VISIBLE);
                    } else {
                        getView(R.id.btn_reset).setVisibility(View.VISIBLE);
                        tv_profit.setText(DoubleFromat.getStockDouble(matchInfo.getTotal_yield() * 100, 2) + "%");
                    }
                    tv_profit.setTextColor(matchInfo.getTotal_yield() >= 0 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.green));
                    tv_week_yield.setTextColor(matchInfo.getWeek_yield() >= 0 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.green));
                    tv_day_yield.setTextColor(matchInfo.getDay_yield() >= 0 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.green));
                    tv_week_yield.setText(String.format("%+.2f%%", matchInfo.getWeek_yield() * 100));
                    tv_day_yield.setText(String.format("%+.2f%%", matchInfo.getDay_yield() * 100));
                    int maxDayNum = Integer.parseInt(matchInfo.getPlayers()), maxWeekNum = Integer.parseInt(matchInfo.getPlayers());
                    int currDayReachedNum = matchInfo.getDay_rank() > 0 ? maxDayNum - matchInfo.getDay_rank() : 0;
                    int currWeekReachedNum = matchInfo.getWeek_rank() > 0 ? maxDayNum - matchInfo.getWeek_rank() : 0;
                    tv_day_reached.setMax(currDayReachedNum);
                    tv_week_reached.setMax(currWeekReachedNum);
                    tv_day_reached.increaseBarBrother();
                    tv_week_reached.increaseBarBrother();
                    mCustomDayProgress.setMax(maxDayNum);
                    mCustomDayProgress.setInitProgress(currDayReachedNum);
                    mCustomDayProgress.displayNiuAnim();
                    mCustomWeekProgress.setMax(maxWeekNum);
                    mCustomWeekProgress.setInitProgress(currWeekReachedNum);
                    mCustomWeekProgress.displayNiuAnim();
                }
            }


            @Override
            public void onFailure(String err_msg) {
                mPullToRefreshScrollView.onRefreshComplete();
                showToast(err_msg);
            }
        });
        WebBase.getAnnouncements(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                LogUtil.e(obj.toString());
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    if (result.has("announcements")) {
                        JSONArray jsonArray = result.optJSONArray("announcements");
                        if (jsonArray.length() > 0) {
                            rl_announcements.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = jsonArray.optJSONObject(0);
                            MatchAnnouncements matchAnnouncements = new MatchAnnouncements();
                            matchAnnouncements.setGroup_id(jsonObject.optString("group_id"));
                            matchAnnouncements.setContent(jsonObject.optString("content"));
                            matchAnnouncements.setPosted_at(jsonObject.optString("posted_at"));
                            matchAnnouncements.setSubject(jsonObject.optString("subject"));
                            matchAnnouncements.setTopic_id(jsonObject.optString("topic_id"));
                            tv_match_message.setText(matchAnnouncements.getSubject());
                            rl_announcements.setTag(matchAnnouncements);
                        }
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });

        WebBase.getHoldlist2(1, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    infolist.clear();
                    if (result.has("stocks")) {
                        infolist.addAll(JSONParse.getHolder(result.optJSONArray("stocks")));
                    }
                    if (infolist.size() > 0) {
                        getView(R.id.btn_all_hold).setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    } else {
                        getView(R.id.btn_all_hold).setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void addListener() {
        findViewById(R.id.btn_reset).setOnClickListener(this);
        findViewById(R.id.ll_stock_chat).setOnClickListener(this);
        getView(R.id.btn_all_hold).setOnClickListener(this);
        getView(R.id.tv_match_trusts).setOnClickListener(this);
        getView(R.id.tv_log_list).setOnClickListener(this);
        getView(R.id.tv_record).setOnClickListener(this);
        getView(R.id.tv_sell_id).setOnClickListener(this);
        getView(R.id.tv_hold).setOnClickListener(this);
        tv_buy.setOnClickListener(this);
        simulate_hold_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOpreateDialog(infolist.get(position));
            }
        });
        adapter.setOnClickListener(new ChatStockHoldAdapter.OnCommit() {
            @Override
            public void onCommit(StockholdsBean stockholdsBean) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.STOCKHOLDER, stockholdsBean);
                ShowActivity.showActivity(SimulateStockAccActivity.this, bundle, SimulateOneStockCommitActivity.class);
            }
        });
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                RushData();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        mAnim.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mAnim.stop();
        mCustomDayProgress.cancelTimer();
        mCustomWeekProgress.cancelTimer();
    }

    @Override
    public void onClick(View v) {
        if (matchInfo == null) {
            showToast("数据错误，请刷新后重试！");
            return;
        }
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.notice_layout://跳转公告页面
                bundle = new Bundle();
                ShowActivity.showActivity(this, bundle, MatchNoticeActivity.class);
                break;
            case R.id.icon_notice_close_img:
                rl_announcements.setVisibility(View.GONE);
                break;
            case R.id.tv_record://获奖记录替换完成
                bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                ShowActivity.showActivity(this, bundle, RecordActivity.class);
                break;
            case R.id.tv_buy://买入 接口替换完成
                bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                ShowActivity.showActivity(this, bundle, StockBuyActivity.class);
                break;
            case R.id.tv_match_trusts://委托页面接口替换完成
                bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                ShowActivity.showActivity(this, bundle, MatchTrustsActivity.class);
                break;
            case R.id.tv_log_list://交易记录接口替换完成
                bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                ShowActivity.showActivity(this, bundle, MatchLogListActivity.class);
                break;
            case R.id.btn_reset://重置比赛接口替换完成
                getWolle();
                break;
            case R.id.ll_stock_chat://完成排行榜接口的替换
                bundle = new Bundle();
                bundle.putInt(IntentKey.SELECT, 1);
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                ShowActivity.showActivity(this, bundle, SimulateStockChatActivity.class);
                break;
            case R.id.tv_hold:
            case R.id.tv_sell_id:
            case R.id.btn_all_hold://持仓的接口替换完成
                bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                ShowActivity.showActivity(this, bundle, MatchHoldActivity.class);
                break;
        }
    }

    private void getWolle() {
        WebBase.getWalle(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays = obj.optJSONObject("pay");
                JSONObject point = obj.optJSONObject("point");
                JSONObject coupon = obj.optJSONObject("coupon");
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
                SettingDefaultsManager.getInstance().setCoupon(coupon.optString("unfrozen"));
                if (dialog == null) {
                    dialog = resetAccDialog();
                }
                dialog.show();
            }

            @Override
            public void onFailure(String err_msg) {
                if (dialog == null) {
                    dialog = resetAccDialog();
                }
                dialog.show();
            }
        });
    }

    private Dialog dialog;

    @SuppressLint("SetTextI18n")
    private Dialog resetAccDialog() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        final LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.reset_account, null);
        final Button btn_confirm_reset = (Button) layout.findViewById(R.id.btn_confirm_reset);
        TextView tv_account_remain = (TextView) layout.findViewById(R.id.tv_account_remain);
        final TextView tv_finish_tip = (TextView) layout.findViewById(R.id.tv_finish_tip);
        TextView tv_paynum = (TextView) layout.findViewById(R.id.tv_paynum);
        final RelativeLayout rl_reset_state = (RelativeLayout) layout.findViewById(R.id.rl_reset_state);
        final Button btn_start_op = (Button) layout.findViewById(R.id.btn_start_op);
        final CustomMyCProgress mCustomProgress = (CustomMyCProgress) layout.findViewById(R.id.cc_progress);
        mCustomProgress.setdefaultTextStr("重置中");
        tv_paynum.setText(paynum == 0 ? 99 + "魔方宝" : DoubleFromat.getStockDouble(paynum, 0) + "魔方宝");
        tv_account_remain.setText("账户余额" + SettingDefaultsManager.getInstance().getPays() + "魔方宝");
        final Double my_mfb = Double.valueOf(SettingDefaultsManager.getInstance().getPays());
        if (my_mfb < paynum) {
            btn_confirm_reset.setText("去充值");
        }
        layout.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //点击重置
        btn_confirm_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_mfb < paynum) {
                    ShowActivity.showPayDetailActivity(SimulateStockAccActivity.this);
                    dialog.dismiss();
                } else {
                    WebBase.resetMatch(new JSONHandler() {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            rl_reset_state.setVisibility(View.VISIBLE);
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mCustomProgress, "progress", 0f, 100f);
                            anim.setDuration(2000);
                            anim.start();
                            RushData();
                            mCustomProgress.setStateListener(new CustomMyCProgress.StateListener() {
                                @Override
                                public void loadFinished() {
                                    tv_finish_tip.setVisibility(View.VISIBLE);
                                    btn_start_op.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            showToast(err_msg);
                        }
                    });
                }
            }
        });

        btn_start_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(false);
        return dialog;
    }

    public void showOpreateDialog(final StockholdsBean stockholdsBean) {
        final Dialog dialog1 = new Dialog(this, R.style.myDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_tip2, null);
        TextView tvTip = (TextView) layout.findViewById(R.id.tvTip);
        tvTip.setText(stockholdsBean.getName());
        TextView tvCancl = (TextView) layout.findViewById(R.id.tvCancl);
        TextView tvConfirm = (TextView) layout.findViewById(R.id.tvConfirm);
        TextView tvConfirm1 = (TextView) layout.findViewById(R.id.tvConfirm1);
        tvCancl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog1.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.STOCKHOLDER, stockholdsBean);
                ShowActivity.showActivity(SimulateStockAccActivity.this, bundle, StockSellActivity.class);
                dialog1.dismiss();
            }
        });

        tvConfirm1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                bundle.putSerializable(IntentKey.STOCKHOLDER, stockholdsBean);
                ShowActivity.showActivity(SimulateStockAccActivity.this, bundle, StockBuyActivity.class);
                dialog1.dismiss();
            }
        });
        dialog1.setContentView(layout);

        // 设置对话框的出现位置，借助于window对象
        Window win = dialog1.getWindow();
        win.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (DisplayUtil.getScreenWidthPixels(this) * 0.65);
        win.setAttributes(lp);
        dialog1.setCancelable(false);
        dialog1.show();
    }
}

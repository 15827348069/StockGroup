package com.zbmf.StocksMatch.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.MainActivity;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.adapter.HomeMatchAdapter;
import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.bean.User;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.HtmlUrl;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.listener.IMineView;
import com.zbmf.StocksMatch.presenter.MinePresenter;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.util.ToastUtils;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
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
 * on 2017/11/27.
 * 我的
 */

public class MineFragment extends BaseFragment<MinePresenter> implements IMineView {

    @BindView(R.id.imv_user_avatar)
    ImageView imvUserAvatar;
    @BindView(R.id.list_my_match)
    ListViewForScrollView listMyMatch;
    @BindView(R.id.mine_pull_to_refresh)
    PullToRefreshScrollView minePullToRefresh;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_mine_pay)
    TextView tvMinePay;
    @BindView(R.id.kf_layout_id)
    LinearLayout kfLayoutId;
    private User user;
    private List<MatchNewAllBean.Result.Matches> matchList = new ArrayList<>();

    private HomeMatchAdapter homeMatchAdapter;
    private int mPage;
    private int mTotal;
    private MinePresenter mMinePresenter;
    private Dialog mDialog;
    private Dialog mDialog1;
    private Handler mHandler = new Handler();

    // TODO: 2018/4/25 我的参赛列表  第一页请求数据设置的一页获取 50 条数据 ，下次更改

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initView() {
        minePullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                setLoadingVis(View.VISIBLE);
                getPresenter().getDatas();
            }
        });
        kfLayoutId.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
        Glide.with(getActivity())
                .load(SharedpreferencesUtil.getInstance().getString(SharedKey.AVATAR, ""))
                .apply(GlideOptionsManager.getInstance().getRequestOptions())
                .into(imvUserAvatar);
        tvUserName.setText(SharedpreferencesUtil.getInstance().getString(SharedKey.NICK_NAME, ""));
        tvMinePay.setText(getString(R.string.mfb) + DoubleFromat.getMoneyDouble(Double.
                valueOf(SharedpreferencesUtil.getInstance().
                        getString(SharedKey.MPAY, "0.00")), 2));
        homeMatchAdapter = new HomeMatchAdapter(getActivity());
        listMyMatch.setAdapter(homeMatchAdapter);
        listMyMatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MatchNewAllBean.Result.Matches matches = homeMatchAdapter.getItem(i);
                ShowActivity.showMatchDetail(getActivity(), matches);
            }
        });
        minePullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (homeMatchAdapter.getList() != null) {
                    homeMatchAdapter.clearList();
                }
                mPage = ParamsKey.D_PAGE;
                setLoadingVis(View.VISIBLE);
                getPresenter().getDatas();
                ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTotal > homeMatchAdapter.getList().size()) {
                    mPage += 1;
                    mMinePresenter.getUserMatch(String.valueOf(mPage),MatchSharedUtil.UserId());
                } else {
                    showToast(getString(R.string.nomore_loading));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            minePullToRefresh.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    protected MinePresenter initPresent() {
        mMinePresenter = new MinePresenter();
        return mMinePresenter;
    }

    @Override
    public void RushMatchList(MatchNewAllBean.Result result) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (minePullToRefresh.isRefreshing()) {
            minePullToRefresh.onRefreshComplete();
        }
        if (result != null) {
            mPage = result.getPage();
            mTotal = result.getTotal();
            List<MatchNewAllBean.Result.Matches> matches = result.getMatches();
            if (homeMatchAdapter.getList() == null) {
                homeMatchAdapter.setList(matches);
            } else {
                if (mPage == ParamsKey.D_PAGE) {
                    matchList.addAll(matches);
                    homeMatchAdapter.clearList();
                    homeMatchAdapter.addList(matchList);
                    matchList.clear();
                } else {
                    homeMatchAdapter.addList(matches);
                }
            }
        }
        setLoadingVis(View.GONE);
        listMyMatch.setVisibility(View.VISIBLE);
        kfLayoutId.setVisibility(View.VISIBLE);
    }

    @Override
    public void RushMatchListErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
//        if (!TextUtils.isEmpty(msg)) {
//            showToast(msg);
//        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void RushMineMessage(User user) {
        if (user != null) {
            this.user = user;
        }
        Glide.with(getActivity())
                .load(user.getAvatar())
                .apply(GlideOptionsManager.getInstance().getRequestOptions())
                .into(imvUserAvatar);
        tvUserName.setText(user.getNickname());
        tvMinePay.setText(getString(R.string.mfb) + DoubleFromat.getMoneyDouble(user.getMpay(), 2));
        if (minePullToRefresh.isRefreshing()) {
            minePullToRefresh.onRefreshComplete();
        }
    }

    @Override
    public void onError(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (minePullToRefresh.isRefreshing()) {
            minePullToRefresh.onRefreshComplete();
        }
            if (!TextUtils.isEmpty(msg)) {
                if (ShowActivity.checkLoginStatus(getActivity(), msg)) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putInt(ParamsKey.FG_FLAG, ParamsKey.FG_MINE_V);
                            ShowActivity.showActivity(getActivity(), bundle, LoginActivity.class);
//                            showToast(getString(R.string.need_login));
                            ToastUtils.rectangleSingleToast(getString(R.string.need_login));
                            getActivity().finish();
                        }
                    }, Constans.DELAY_TIME);
                }
        }
        listMyMatch.setVisibility(View.VISIBLE);
        kfLayoutId.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.imv_user_avatar, R.id.ll_stockgroup, R.id.ll_open, R.id.ll_cl, R.id.ll_stock_trader,
            R.id.kf_layout_id, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imv_user_avatar:
                ShowActivity.showUserActivity(getActivity(), user);
                break;
            case R.id.ll_stockgroup:
                PackageManager packageManager = getActivity().getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(Method.PACKAGE_NAME);
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(intent);
                } else {
                    skipGroup();
                }
                break;
            case R.id.ll_open:
                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.OPEN, getString(R.string.open_zbmf));
                break;
            case R.id.ll_cl:
                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.RUN_TRADER, "");
                break;
            case R.id.ll_stock_trader:
                ShowActivity.showMatchRankActivity(getActivity(), Constans.MATCH_ID, Constans.TRADER_RANK_FLAG,false);
                break;
            case R.id.kf_layout_id:
                if (mDialog == null) {
                    mDialog = dialog1();
                }
                mDialog.show();
                break;
            case R.id.tv_logout:
                if (mDialog1 == null) {
                    mDialog1 = dialog2();
                }
                mDialog1.show();
                break;
        }
    }

    //跳转应用市场
    private void skipGroup() {
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                .setMessage(R.string.load_group_app)
                .setNegativeButton(R.string.wait, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.load_app, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowActivity.goToAppGroup(getActivity());
                    }
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.
                getColor(getActivity(), R.color.black));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.
                getColor(getActivity(), R.color.apply_textcolor));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(getResources().
                getDimension(R.dimen.tv_size_14));
    }

    private Dialog dialog1() {
        Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.kefu_layout, null);
        TextView qq_button = (TextView) layout.findViewById(R.id.qq_kefu_button);
        TextView phone_button = (TextView) layout.findViewById(R.id.phone_id_button);
        phone_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000607878"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mDialog.dismiss(); //关闭dialog
            }
        });
        qq_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=2852273339&version=1";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                mDialog.dismiss(); //关闭dialog
            }
        });
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.bottomDialogStyle);
        dialog.setCancelable(true);
        return dialog;
    }

    //只退出账号，跳转到首页
    private Dialog dialog2() {
        final Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.out_dialog, null);
        TextView cancel_tv = (TextView) layout.findViewById(R.id.cancel_tv);
        TextView confirm_tv = (TextView) layout.findViewById(R.id.confirm_tv);
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog1.dismiss(); //
            }
        });
        confirm_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MatchSharedUtil.clearUser();
                dialog.dismiss();
                mDialog1.dismiss(); //关闭dialog
                MainActivity activity = (MainActivity) getActivity();
                activity.setIsExit(true);
                activity.setSelectHome();
//                MyActivityManager.getMyActivityManager().removeAllAct();//退出应用的方法
            }
        });
        dialog.setContentView(layout);
        dialog.setCancelable(false);//设置dialog不可取消
        dialog.setCanceledOnTouchOutside(false);//设置dialog点击dialog以外的区域不消失
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager.LayoutParams lp = win.getAttributes();
        win.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        lp.width = (int) (metrics.widthPixels * 0.85);//获取屏幕的像素宽度
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.bottomDialogStyle);
        dialog.setCancelable(true);
        return dialog;
    }
}

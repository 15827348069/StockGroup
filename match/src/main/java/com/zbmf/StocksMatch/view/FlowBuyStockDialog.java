package com.zbmf.StocksMatch.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.FlowDialogAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.listener.FlowDialogItemListener;
import com.zbmf.StocksMatch.listener.FlowItemClickToBuy;
import com.zbmf.StocksMatch.model.JoinMatchMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pq
 * on 2018/4/27.
 */

public class FlowBuyStockDialog extends Dialog implements FlowDialogItemListener {
    private Context mContext;
    private Activity activity;
    private PullToRefreshScrollView mFlowList;
    private TextView mCancelBuy;
    private LinearLayout mLlNone;
    private int mPage1;
    private int mTotal;
    private FlowDialogAdapter mFlowDialogAdapter;
    private List<MatchNewAllBean.Result.Matches> matchesList = new ArrayList<>();
    private Handler mHandler = new Handler();
    private FlowItemClickToBuy mFlowItemClickToBuy;

    public FlowBuyStockDialog setFlowItemClickToBuy(FlowItemClickToBuy flowItemClickToBuy){
        this.mFlowItemClickToBuy=flowItemClickToBuy;
        return this;
    }

    public FlowBuyStockDialog(@NonNull Context context, Activity activity) {
        super(context);
        this.mContext = context;
        this.activity = activity;
    }

    public FlowBuyStockDialog(@NonNull Context context, int themeResId, Activity activity) {
        super(context, themeResId);
        this.mContext = context;
        this.activity = activity;
    }

    protected FlowBuyStockDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public FlowBuyStockDialog showFlowDialog(MatchNewAllBean.Result userMatch) {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.fllow_dialog, null);
        //获得dialog的window窗口
        Window window = getWindow();
        //设置dialog在屏幕底部
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.bottomDialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        this.setContentView(dialogView);
        mFlowList = dialogView.findViewById(R.id.flow_list);
        ListViewForScrollView myMatchList = dialogView.findViewById(R.id.myMatchList);
        mCancelBuy = dialogView.findViewById(R.id.cancelBuy);
        TextView noMessageText = dialogView.findViewById(R.id.no_message_text);
        noMessageText.setText(mContext.getString(R.string.no_data));
        mLlNone = dialogView.findViewById(R.id.ll_none);
//        mLlNone.setVisibility(View.VISIBLE);
        mFlowDialogAdapter = new FlowDialogAdapter(activity);
        mFlowDialogAdapter.setList(userMatch.getMatches());
        myMatchList.setAdapter(mFlowDialogAdapter);
        mFlowDialogAdapter.setFlowDialogItemListener(this);
        initMatchData(String.valueOf(ParamsKey.D_PAGE), MatchSharedUtil.UserId());//初始化数据
        refreshInitData();
        return this;
    }

    public FlowBuyStockDialog showFlow() {
        show();
        return this;
    }

    public void dismissFlow() {
        if (isShowing()) {
            dismiss();
        }
    }

    public void clickCancelFlow() {
        mCancelBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
    }

    public void setCancel(boolean b) {
        setCancelable(b);
        setCanceledOnTouchOutside(b);
    }

    private void refreshInitData() {
        mFlowList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mFlowDialogAdapter.getList() != null) {
                    if (mFlowDialogAdapter.getList().size() > 0) {
                        mFlowDialogAdapter.getList().clear();
                        mPage1 = 1;
                        initMatchData(String.valueOf(mPage1),MatchSharedUtil.UserId());
                        ShowOrHideProgressDialog.showProgressDialog(activity, mContext, mContext.getString(R.string.hard_loading));
                    }
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mFlowList.onRefreshComplete();
                        }
                    }, 1000);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTotal > mFlowDialogAdapter.getList().size()) {
                    mPage1 += 1;
                    initMatchData(String.valueOf(mPage1),MatchSharedUtil.UserId());
                }else {
                    mFlowList.setPullLabel("没有更多", PullToRefreshBase.Mode.PULL_FROM_END);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mFlowList.onRefreshComplete();
                        }
                    },1000);
                }
            }
        });
    }

    private void initMatchData(String page,String userId) {
        ShowOrHideProgressDialog.showProgressDialog(activity, mContext, mContext.getString(R.string.hard_loading));
        new JoinMatchMode().getUserMatch(page, userId,new CallBack<MatchNewAllBean.Result>() {

            @Override
            public void onSuccess(MatchNewAllBean.Result result) {
                if (mFlowList.isRefreshing()){
                    mFlowList.onRefreshComplete();
                }
                ShowOrHideProgressDialog.disMissProgressDialog();
                if (result!=null) {
                    mPage1 = result.getPage();
                    mTotal = result.getTotal();
                    List<MatchNewAllBean.Result.Matches> matches = result.getMatches();
                    if (matches.size()>0&&mFlowDialogAdapter.getList() == null) {
                        mFlowDialogAdapter.setList(matches);
                    } else {
                        if (mPage1 == 1) {
                            matchesList.addAll(matches);
                            mFlowDialogAdapter.clearList();
                            mFlowDialogAdapter.addList(matchesList);
                            matchesList.clear();
                        } else {
                            mFlowDialogAdapter.addList(matches);
                        }
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                ShowOrHideProgressDialog.disMissProgressDialog();
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void flowItemClick(MatchNewAllBean.Result.Matches matches) {
              if (mFlowItemClickToBuy!=null){
                  mFlowItemClickToBuy.flowItemClickToBuy(matches);
              }
    }
}

package com.zbmf.StocksMatch.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MatchDetailStockHoldAdapter;
import com.zbmf.StocksMatch.adapter.SponsorAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.HomeMatchList;
import com.zbmf.StocksMatch.bean.JoinMatchBean;
import com.zbmf.StocksMatch.bean.MatchBottomAdsBean;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.MatchList3Bean;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.bean.MatchRank;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.StocksMatch.bean.SearchMatchBean;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.listener.BuyClick;
import com.zbmf.StocksMatch.listener.IJoinMatchView;
import com.zbmf.StocksMatch.listener.SellClick;
import com.zbmf.StocksMatch.listener.SponsorAdsClick;
import com.zbmf.StocksMatch.presenter.JoinMatchPresenter;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ScreenShot;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.util.SkipMatchDetailEvent;
import com.zbmf.StocksMatch.util.StockSellEvent;
import com.zbmf.StocksMatch.view.BuyDialog;
import com.zbmf.StocksMatch.view.CustomCircleProgress;
import com.zbmf.StocksMatch.view.CustomMarqueeTextView;
import com.zbmf.StocksMatch.view.CustomMyCProgress;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.StocksMatch.view.ShareDialog;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.StocksMatch.view.ViewFactory;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.util.DoubleFromat;
import com.zbmf.worklibrary.util.Logx;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;
import com.zbmf.worklibrary.view.CycleViewPager;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/11/29.
 * 参赛页面
 */

public class MatchDetailActivity extends BaseActivity<JoinMatchPresenter> implements IJoinMatchView,
        AdapterView.OnItemClickListener, SellClick, BuyClick, MatchDetailStockHoldAdapter.OnCommit,
        View.OnTouchListener, CycleViewPager.ImageCycleViewListener<Adverts>, SponsorAdsClick {
    @BindView(R.id.tv_match_name)
    TextView tvMatchName;
    @BindView(R.id.tv_match_player)
    TextView tvMatchPlayer;
    @BindView(R.id.tv_match_date)
    TextView tvMatchDate;
    @BindView(R.id.notice_text)
    CustomMarqueeTextView notice_text;
    @BindView(R.id.notice_right_arrow)
    ImageButton notice_right_arrow;
    @BindView(R.id.cp_yield)
    CustomCircleProgress cpYield;
    @BindView(R.id.tv_win_player)
    TextView tvWinPlayer;
    @BindView(R.id.tv_week_rank)
    TextView tvWeekRank;
    @BindView(R.id.tv_week_yield)
    TextView tvWeekYield;
    @BindView(R.id.tv_day_rank)
    TextView tvDayRank;
    @BindView(R.id.tv_day_yield)
    TextView tvDayYield;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_stock_money)
    TextView tvStockMoney;
    @BindView(R.id.tv_can_use_money)
    TextView tvCanUseMoney;
    @BindView(R.id.tv_close_money)
    TextView tvCloseMoney;
    @BindView(R.id.tv_stock_num)
    TextView tvStockNum;
    @BindView(R.id.tv_money_yield)
    TextView tvMoneyYield;
    @BindView(R.id.cp_money)
    CustomCircleProgress cpMoney;
    @BindView(R.id.cp_stock)
    CustomCircleProgress cpStock;
    @BindView(R.id.cp_close_money)
    CustomCircleProgress cpCloseMoney;
    @BindView(R.id.cp_can_use_money)
    CustomCircleProgress cpCanUseMoney;
    @BindView(R.id.simulate_hold_list)
    ListViewForScrollView simulateHoldList;
    @BindView(R.id.pull_scrollview)
    PullToRefreshScrollView pullScrollview;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.btn_all_hold)
    TextView btnAllHold;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.no_holder)
    TextView noHolder;
    @BindView(R.id.tv_reload)
    TextView tvReload;
    @BindView(R.id.title_layout_id)
    LinearLayout title_layout_id;
    @BindView(R.id.holder_title)
    LinearLayout holderTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.other)
    LinearLayout other;
    @BindView(R.id.otherUserName)
    TextView otherUserName;
    @BindView(R.id.go_to_match)
    ImageView go_to_match;
    @BindView(R.id.topTab)
    LinearLayout topTab;
    @BindView(R.id.notice_layout)
    LinearLayout noticeLayout;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.lGotoDeals)
    LinearLayout lGotoDeals;
    @BindView(R.id.lGotoWinRecord)
    LinearLayout lGotoWinRecord;
    @BindView(R.id.ivGotoWinRecord)
    ImageView ivGotoWinRecord;
    @BindView(R.id.ivGoToDeals)
    ImageView ivGoToDeals;
    @BindView(R.id.bottomItemRecord)
    LinearLayout bottomItemRecord;
    @BindView(R.id.matchDecTopBg)
    ImageView matchDecTopBg;
    @BindView(R.id.tv_match_rank)
    TextView tvMatchRank;
    @BindView(R.id.imgRecyclerView)
    RecyclerView imgRecyclerView;
    @BindView(R.id.sponsorView)
    LinearLayout sponsorView;

    private HomeMatchList.Result.Recommend recmMatch;
    private HomeMatchList.Result.Hot hotMatch;
    private MatchNewAllBean.Result.Matches match, mUserMatch;
    private MatchList3Bean.Result.Matches match3;
    private SearchMatchBean.Result.Matches searchMatches;
    private JoinMatchPresenter mJoinMatchPresenter;
    private Dialog mDialog;
    private CustomMyCProgress mMCustomProgress;
    private TextView mTv_finish_tip;
    private RelativeLayout mRl_reset_state;
    private Button mBtnStartReset;
    private double mPaynum;
    private MatchDetailStockHoldAdapter mStockHoldAdapter;
    private BuyDialog mBuyDialog;
    private MatchDescBean.Result mResult;
    private int mOffsetY;
    private Bitmap mShotBitmap, mSrcBitmap, mBitmapTop;
    private int mMatchId;
    private MatchInfo matchInfo;
    private int mMatchEnd;
    private String mMatchName;
    private String mMyFlag;
    private String mUserID;
    private CycleViewPager mCycleViewPager;
    private SponsorAdapter mSponsorAdapter;
    private List<ImageView> views;
    private List<Adverts> mAdverts;
    private Bitmap mPullBit;

    @Override
    protected int getLayout() {
        return R.layout.activity_match_detail;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.match);
    }

    @Override
    protected void initData(Bundle bundle) {
        // 注册订阅者
        EventBus.getDefault().register(this);
        MyActivityManager.getMyActivityManager().pushAct(this);
        ShowOrHideProgressDialog.setDialogCancel(false);
        ShowOrHideProgressDialog.showProgressDialog(MatchDetailActivity.this,
                MatchDetailActivity.this, getString(R.string.hard_loading));
        tvTitleRight.setText(getString(R.string.intro));
        tvTitleRight.setVisibility(View.VISIBLE);
        bottomItemRecord.setVisibility(View.GONE);
        if (bundle != null) {
            mMatchEnd = bundle.getInt(IntentKey.MATCH_END);
            match3 = (MatchList3Bean.Result.Matches) bundle.getSerializable(IntentKey.MATCH3);
            match = (MatchNewAllBean.Result.Matches) bundle.getSerializable(IntentKey.MATCH);
            recmMatch = (HomeMatchList.Result.Recommend) bundle.getSerializable(IntentKey.RECM_MATCH);
            hotMatch = (HomeMatchList.Result.Hot) bundle.getSerializable(IntentKey.HOT_MATCH);
            searchMatches = (SearchMatchBean.Result.Matches) bundle.getSerializable(IntentKey.SEARCH_MATCH);
            if (bundle.getSerializable(IntentKey.USER_MATCH) instanceof MatchNewAllBean.Result.Matches) {
                mUserMatch = (MatchNewAllBean.Result.Matches) bundle.getSerializable(IntentKey.USER_MATCH);
            } else if (bundle.getSerializable(IntentKey.MATCH4) instanceof MatchDescBean.Result) {
                mResult = (MatchDescBean.Result) bundle.getSerializable(IntentKey.MATCH4);
            }
        }
        mStockHoldAdapter = new MatchDetailStockHoldAdapter(this);
        simulateHoldList.setAdapter(mStockHoldAdapter);
        mStockHoldAdapter.setOnClickListener(this);
        //底部赞助商
        imgRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mSponsorAdapter = new SponsorAdapter(this);
        imgRecyclerView.setAdapter(mSponsorAdapter);
        mSponsorAdapter.setSponsorAdsClick(this);
        if (mBuyDialog == null) {
            mBuyDialog = new BuyDialog(this, R.style.Buy_Dialog).createDialog();
        }
        simulateHoldList.setOnItemClickListener(this);

        tvShare.setOnTouchListener(this);
        ScrollView scrollView = pullScrollview.getRefreshableView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android 6.0 版本
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {

                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mOffsetY = scrollY;
                }
            });
        }
        if (mCycleViewPager == null && getSupportFragmentManager() != null) {
            mCycleViewPager = (CycleViewPager) getSupportFragmentManager().findFragmentById(R.id.home_cycleViewPage);
        }

        pullScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getPresenter().setFirst(true);
                getPresenter().getDatas();
                ShowOrHideProgressDialog.setDialogCancel(false);
                ShowOrHideProgressDialog.showProgressDialog(MatchDetailActivity.this,
                        MatchDetailActivity.this, getString(R.string.hard_loading));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //解决模型分享QQ之后，界面会死掉的问题
        if (isNeedRestart()) {
            Intent intent = new Intent(this, this.getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //清除栈顶的activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//不显示多余的动画，假装没有重新启动 //记得带需要的参数
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mShotBitmap = shotBitmap();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    //滑动截图存在一个问题，当滑动距离超过一个屏幕距离时，截图就会出现黑屏
    private Bitmap shotBitmap() {
        int width = pullScrollview.getMeasuredWidth();
        DisplayMetrics dm = new DisplayMetrics();
        //获取当前屏幕窗口的高度
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int h = dm.heightPixels;
//        int i = DisplayUtil.dip2px(this, 110);
        int height = mCycleViewPager.getView().getLayoutParams().height;
        int offsetH = /*i*/height + tvShare.getHeight() + holderTitle.getHeight() /*+ 20*/;
        int i1 = h + mOffsetY - offsetH;
        // 创建对应大小的bitmap
        mSrcBitmap = Bitmap.createBitmap(width, h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(mSrcBitmap);
        pullScrollview.draw(canvas);
        Bitmap bitmap = Bitmap.createBitmap(width, i1, Bitmap.Config.RGB_565);
        Canvas iCanvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int layer = iCanvas.saveLayer(0, 0, width, i1, null, Canvas.ALL_SAVE_FLAG);
        iCanvas.drawBitmap(mBitmapTop, 0, 0, null);//顶部的图片
        Log.i("--TAG", "----   滑动的高度  -----" + mOffsetY + "---- i1    " + i1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
//        int i2 = mOffsetY % h;
//        if (mOffsetY>h){//滑动超过一屏高度
//            iCanvas.drawBitmap(mSrcBitmap, 0, h, paint);//下方的图片
//        }else {
            iCanvas.drawBitmap(mSrcBitmap, 0, mOffsetY, paint);//下方的图片
//        }
        paint.setXfermode(null);
        iCanvas.restoreToCount(layer);
        iCanvas.drawBitmap(bitmap, 0, 0, null);
        return bitmap;
    }

    @SuppressLint("SetTextI18n")
    private void setDataFromMatch(JoinMatchBean.Result result) {
        if (match != null) {
            tvMatchName.setText(match.getMatch_name());
        } else if (recmMatch != null) {
            tvMatchName.setText(recmMatch.getMatch_name());
        } else if (hotMatch != null) {
            tvMatchName.setText(hotMatch.getMatch_name());
        } else if (match3 != null) {
            tvMatchName.setText(match3.getMatch_name());
        } else if (searchMatches != null) {
            tvMatchName.setText(searchMatches.getMatch_name());
        } else if (mUserMatch != null) {
            tvMatchName.setText(mUserMatch.getMatch_name());
        } else if (mResult != null) {
            tvMatchName.setText(mResult.getMatch_name());
        }
        if (tvMatchName.getText() != null) {
            mMatchName = tvMatchName.getText().toString();
        }
        tvMatchPlayer.setText(String.format(getString(R.string.match_detail_players), result.getPlayers()));
        //开始和结束时间
        tvMatchDate.setText(String.format(getString(R.string.match_detail_date), result.getStart_at(), result.getEnd_at()));
        notice_text.setText(getString(R.string.no_notice));
        pullScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                presenter.getDatas();
            }
        });
    }

    @Override
    protected JoinMatchPresenter initPresent() {
        if (match != null) {
            mMatchId = match.getMatch_id();
            mJoinMatchPresenter = new JoinMatchPresenter(mMatchId, MatchSharedUtil.UserId());
            return mJoinMatchPresenter;
        } else if (recmMatch != null) {
            mMatchId = recmMatch.getMatch_id();
            mJoinMatchPresenter = new JoinMatchPresenter(mMatchId, MatchSharedUtil.UserId());
            return mJoinMatchPresenter;
        } else if (hotMatch != null) {
            mMatchId = hotMatch.getMatch_id();
            mJoinMatchPresenter = new JoinMatchPresenter(mMatchId, MatchSharedUtil.UserId());
            return mJoinMatchPresenter;
        } else if (match3 != null) {
            mMatchId = match3.getMatch_id();
            mJoinMatchPresenter = new JoinMatchPresenter(mMatchId, MatchSharedUtil.UserId());
            return mJoinMatchPresenter;
        } else if (searchMatches != null) {
            mMatchId = searchMatches.getMatch_id();
            mJoinMatchPresenter = new JoinMatchPresenter(mMatchId, MatchSharedUtil.UserId());
            return mJoinMatchPresenter;
        } else if (mUserMatch != null) {
            mMatchId = mUserMatch.getMatch_id();
            mJoinMatchPresenter = new JoinMatchPresenter(mMatchId, MatchSharedUtil.UserId());
            return mJoinMatchPresenter;
        } else if (mResult != null) {
            mMatchId = mResult.getMatch_id();
            mJoinMatchPresenter = new JoinMatchPresenter(mMatchId, MatchSharedUtil.UserId());
            return mJoinMatchPresenter;
        } else {
            return new JoinMatchPresenter(-1, MatchSharedUtil.UserId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick({R.id.tv_hold, R.id.tv_buy,
            R.id.tv_sell, R.id.tv_cause,
            R.id.tv_qure, R.id.tv_cup, R.id.tv_share,
            R.id.tv_reload, R.id.btn_all_hold, R.id.tv_match_rank, R.id.notice_layout,
            R.id.notice_right_arrow, R.id.tv_title_right, R.id.go_to_match, R.id.other, R.id.avatar,
            R.id.lGotoDeals, R.id.lGotoWinRecord, R.id.ivGotoWinRecord, R.id.ivGoToDeals})
    public void onViewClicked(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.tv_hold:
            case R.id.tv_sell:
                if (mMatchEnd == 1) {//表示比赛已经结束
                    showToast(getString(R.string.match_end));
                    return;
                }
                ShowActivity.showMatchHoldActivity(this, matchInfo, String.valueOf(mMatchId),
                        MatchSharedUtil.UserId(), IntentKey.DEFAULT_FLAG_INT);
                break;
            case R.id.tv_buy:
                if (mMatchEnd == 1) {
                    showToast(getString(R.string.match_end));
                    return;
                }
                ShowActivity.showStockBuyActivity(this, matchInfo, -1,
                        null, Constans.BUY_FLAG, String.valueOf(mMatchId));
                break;
            case R.id.tv_cause:
                if (mMatchEnd == 1) {
                    showToast(getString(R.string.match_end));
                    return;
                }
                ShowActivity.showMyTrustActivity(this, matchInfo, String.valueOf(mMatchId));
                break;
            case R.id.tv_qure:
                ShowActivity.showDealsListActivity(this, matchInfo, String.valueOf(mMatchId));
                break;
            case R.id.tv_cup:
                ShowActivity.showUserPrizeActivity(this, matchInfo, String.valueOf(mMatchId));
                break;
            case R.id.tv_share:
                //如果数据没有加载完成，不可分享
                if (mBitmapTop == null) {
                    showToast(getString(R.string.wait_load_data));
                    return;
                }
                if (mShotBitmap == null) {
                    showToast(getString(R.string.no_img));
                    return;
                }
                String path = ScreenShot.savePic(mShotBitmap);
                share();
                mShareDialog.createWXApi();
                mShareDialog.setFlag(Constans.WX_SHARE_IMG);
                mShareDialog.setPath(path);
                shareImg(mShotBitmap);
                break;
            case R.id.tv_reload://重置
                if (!TextUtils.isEmpty(mMyFlag) && mMyFlag.equals(IntentKey.MY_FLAG)) {
                    return;
                }
                getWolle();
                break;
            case R.id.btn_all_hold:
                ShowActivity.showMatchHoldActivity(this, matchInfo, String.valueOf(mMatchId),
                        MatchSharedUtil.UserId(), IntentKey.DEFAULT_FLAG_INT);
                break;
            case R.id.tv_match_rank://排行榜
                bundle = new Bundle();
                bundle.putBoolean(IntentKey.TRADER_HIDE, true);
                bundle.putString(IntentKey.MY_FLAG, IntentKey.MY_FLAG);
                bundle.putString(IntentKey.MATCH_NAME, mMatchName);
                if (match != null) {
                    bundle.putString(IntentKey.MATCH_ID, String.valueOf(match.getMatch_id()));
                } else if (recmMatch != null) {
                    bundle.putString(IntentKey.MATCH_ID, String.valueOf(recmMatch.getMatch_id()));
                } else if (hotMatch != null) {
                    bundle.putString(IntentKey.MATCH_ID, String.valueOf(hotMatch.getMatch_id()));
                } else if (match3 != null) {
                    bundle.putString(IntentKey.MATCH_ID, String.valueOf(match3.getMatch_id()));
                } else if (searchMatches != null) {
                    bundle.putString(IntentKey.MATCH_ID, String.valueOf(searchMatches.getMatch_id()));
                } else if (mUserMatch != null) {
                    bundle.putString(IntentKey.MATCH_ID, String.valueOf(mUserMatch.getMatch_id()));
                } else if (mResult != null) {
                    bundle.putString(IntentKey.MATCH_ID, String.valueOf(mResult.getMatch_id()));
                }
                ShowActivity.showActivity(this, bundle, MatchRankActivity.class);
                break;
            case R.id.notice_layout:
            case R.id.notice_right_arrow:
                ShowActivity.showNoticeActivity(this, mNotice);
                break;
            case R.id.tv_title_right:
                if (match != null) {
                    ShowActivity.showUserMathchDesc(this, match);
                } else if (recmMatch != null) {
                    ShowActivity.showRecoMathchDesc(this, recmMatch);
                } else if (hotMatch != null) {
                    ShowActivity.showHotMathchDesc(this, hotMatch);
                } else if (match3 != null) {
                    ShowActivity.showMathch3Desc(this, match3);
                } else if (searchMatches != null) {
                    ShowActivity.showSearchMatchDesc(this, searchMatches);
                } else if (mUserMatch != null) {
                    ShowActivity.showUserMathchDesc(this, mUserMatch);
                } else if (mResult != null) {
                    ShowActivity.showMathch4Desc(this, mResult);
                }
                break;
            case R.id.other:
            case R.id.avatar:
            case R.id.go_to_match:
                Bundle bundle1 = new Bundle();
                bundle1.putString(IntentKey.USER_ID, mUserID);
                ShowActivity.showActivity(this, bundle1, UserJoinMatchActivity.class);
                break;
            case R.id.lGotoDeals:
            case R.id.ivGoToDeals:
                ShowActivity.showDealsListActivity(this, matchInfo, String.valueOf(mMatchId));
                break;
            case R.id.ivGotoWinRecord:
            case R.id.lGotoWinRecord:
                ShowActivity.showUserPrizeActivity(this, matchInfo, String.valueOf(mMatchId));
                break;
        }
    }

    @Override
    public void refreshMatchJoin(JoinMatchBean.Result result) {
        if (result != null) {
            setDataFromMatch(result);
            refreshMatchDetail(result);
        }
    }

    @Override
    public void refreshMatchJoinErr(String msg) {
        Logx.e(msg);
    }

    //持仓
    @Override
    public void refreshMatchHolder(HolderPositionBean.Result result) {
    }

    private List<HolderPositionBean.Result.Stocks> mStocksList = new ArrayList<>();
    private List<HolderPositionBean.Result.Stocks> mStocksList1 = new ArrayList<>();

//    @Override
//    public void rushHold(StockHoldList stockHoldList) {
//        ShowOrHideProgressDialog.disMissProgressDialog();
//        if (result != null) {
//            stockHolderListSet(result);
//        }
//    }
//    @Override
//    public void rushHolderErr(String msg) {
//        ShowOrHideProgressDialog.disMissProgressDialog();
//        if (!TextUtils.isEmpty(msg)) {
//            showToast(msg);
//        }
//    }
    @Override
    public void RushHoldList(HolderPositionBean.Result result) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (result != null) {
            stockHolderListSet(result);
        }
    }

    @Override
    public void holderListErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }
    private void stockHolderListSet(HolderPositionBean.Result stockHoldList) {
//        int total = stockHoldList.getTotal();
//        int page = stockHoldList.getPage();
        List<HolderPositionBean.Result.Stocks> stocks1 = stockHoldList.getStocks();
        btnAllHold.setVisibility(stocks1.size() > 0 ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(mMyFlag) && mMyFlag.equals(IntentKey.MY_FLAG)) {
            btnAllHold.setVisibility(View.GONE);
        }
        noHolder.setVisibility(stocks1.size() > 0 ? View.GONE : View.VISIBLE);
        int size = stocks1.size();
        if (stocks1.size() > 3) {
            size = 3;
        }
        mStocksList.clear();
        for (int i = 0; i < size; i++) {
//            HolderPositionBean.Result.Stocks stocks = new HolderPositionBean.Result.Stocks();
//            stocks.setPrice_buy(stockHoldsBeans.get(i).getPrice_buy());
//            stocks.setClose(stockHoldsBeans.get(i).getClose());
//            stocks.setCreated_at(stockHoldsBeans.get(i).getCreated_at());
//            stocks.setCurrent(stockHoldsBeans.get(i).getCurrent());
//            stocks.setId(stockHoldsBeans.get(i).getId());
//            stocks.setName(stockHoldsBeans.get(i).getName());
//            stocks.setYield_float(stockHoldsBeans.get(i).getYield_float());
//            stocks.setPrice2(stockHoldsBeans.get(i).getPrice2());
//            stocks.setPrice_sell(stockHoldsBeans.get(i).getPrice_sell());
//            stocks.setProfit(stockHoldsBeans.get(i).getProfit());
//            stocks.setSymbol(stockHoldsBeans.get(i).getSymbol());
//            stocks.setVolumn_total(Integer.parseInt(stockHoldsBeans.get(i).getVolumn_total()));
//            if (stockHoldsBeans.get(i).getVolumn_unfrozen().contains(".")) {//
//                int i1 = stockHoldsBeans.get(i).getVolumn_unfrozen().indexOf(".");
//                String Volumn_unfrozen = stockHoldsBeans.get(i).getVolumn_unfrozen().substring(0, i1);
//                stocks.setVolumn_unfrozen(Integer.parseInt(Volumn_unfrozen));
//            } else {
//                stocks.setVolumn_unfrozen(Integer.parseInt(stockHoldsBeans.get(i).getVolumn_unfrozen()));
//            }
//            stocks.setPrice_float(stockHoldsBeans.get(i).getPrice_float());
//            stocks.setCommnet_count(stockHoldsBeans.get(i).getComment_count());
            HolderPositionBean.Result.Stocks stocks = stocks1.get(i);
            mStocksList.add(stocks);
        }
        if (mStockHoldAdapter.getList() == null) {
            mStocksList1.addAll(mStocksList);
            mStockHoldAdapter.setList(mStocksList1);
        } else {
            mStockHoldAdapter.clearList();
            mStockHoldAdapter.addList(mStocksList);
        }
    }

    @Override
    public void matchInfo(MatchInfo matchInfo) {
        this.matchInfo = matchInfo;
        getPay(matchInfo);
    }

    private void getPay(MatchInfo matchInfo) {
        mPaynum = matchInfo.getResult().getPaynum();
    }

    @Override
    public void userWallet(UserWallet userWallet) {
        if (userWallet != null) {
//            UserWallet userWallet1 = userWallet;
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_PAY, userWallet.getPay().getUnfrozen());
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_POINT, userWallet.getPoint().getUnfrozen());
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_COUPON, userWallet.getCoupon().getUnfrozen());
        }
        if (mDialog == null) {
            mDialog = resetAccDialog();
        }
        mDialog.show();
    }

    @Override
    public void userWalletErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        if (mDialog == null) {
            mDialog = resetAccDialog();
        }
        mDialog.show();
    }

    @Override
    public void resetOnSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
            showCircleProgress();
            mJoinMatchPresenter.resetMatch(String.valueOf(mMatchId));
            mJoinMatchPresenter.holderPosition(mMatchId, ParamsKey.D_PAGE, MatchSharedUtil.UserId());
        }
    }

    @Override
    public void resetOnFail(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    private NoticeBean.Result mNotice;

    @Override
    public void notice(NoticeBean.Result notice) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (notice != null && notice.getAnnouncements().size() > 0) {
            this.mNotice = notice;
            setNotice(notice);
        } else {
            noticeLayout.setClickable(false);
            notice_right_arrow.setVisibility(View.GONE);
        }
    }

    private void setNotice(NoticeBean.Result notice) {
        String subject = notice.getAnnouncements().get(0).getSubject();
        notice_text.setText(subject);
    }

    @Override
    public void noticeErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
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

    //获取到的比赛详情
    @Override
    public void refreshMatchDesc(MatchDescBean.Result result, int gainStatus) {
        if (gainStatus == Constans.GAIN_DATA_SUCCESS) {
            String template = result.getTemplate();
            if (TextUtils.isEmpty(template)) {
                return;
            }
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int widthPixels = metrics.widthPixels;
//            int heightPixels = metrics.heightPixels;
//            Log.i("--TAG", "---------- w:" + widthPixels + "  ----  h:" + heightPixels);
            int heightPixels = widthPixels * 2 / 3;
            ViewGroup.LayoutParams lp = matchDecTopBg.getLayoutParams();
            lp.width = widthPixels;
            lp.height = heightPixels;
            matchDecTopBg.setLayoutParams(lp);
            Glide.with(this).load(template).apply(GlideOptionsManager
                    .getInstance().getBitOptionsNoPlaceholder(0)).into(matchDecTopBg);
            //适配文字
            if (widthPixels >= Constans.WIDTH_PIX_800 && widthPixels <= Constans.WIDTH_PIX_960) {
                tvMatchName.setTextSize(getResources().getDimension(R.dimen.tv_size_9));
                tvMatchPlayer.setTextSize(getResources().getDimension(R.dimen.tv_size_5));
                tvMatchDate.setTextSize(getResources().getDimension(R.dimen.tv_size_5));
                tvMatchRank.setTextSize(getResources().getDimension(R.dimen.tv_size_5));
            } else if (widthPixels > Constans.WIDTH_PIX_960 && widthPixels <= Constans.WIDTH_PIX_1280) {
                tvMatchName.setTextSize(getResources().getDimension(R.dimen.tv_size_9));
                tvMatchPlayer.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
                tvMatchDate.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
                tvMatchRank.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
            } else if (widthPixels > Constans.WIDTH_PIX_1280 && widthPixels <= Constans.WIDTH_PIX_1920) {
                tvMatchName.setTextSize(getResources().getDimension(R.dimen.tv_size_8_6));
                tvMatchPlayer.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
                tvMatchDate.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
                tvMatchRank.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
            } else if (widthPixels > Constans.WIDTH_PIX_1920 && widthPixels <= Constans.WIDTH_PIX_2560) {
                tvMatchName.setTextSize(getResources().getDimension(R.dimen.tv_size_6));
                tvMatchPlayer.setTextSize(getResources().getDimension(R.dimen.tv_size_3));
                tvMatchDate.setTextSize(getResources().getDimension(R.dimen.tv_size_3));
                tvMatchRank.setTextSize(getResources().getDimension(R.dimen.tv_size_3));
            } else {
                tvMatchName.setTextSize(getResources().getDimension(R.dimen.tv_size_8_3));
                tvMatchPlayer.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
                tvMatchDate.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
                tvMatchRank.setTextSize(getResources().getDimension(R.dimen.tv_size_4_5));
            }
            ViewGroup.LayoutParams tvLp = tvMatchRank.getLayoutParams();
            tvLp.width = widthPixels / 4;
            tvLp.height = widthPixels / 13;
            tvMatchRank.setLayoutParams(tvLp);
            matchDecTopBg.setVisibility(View.VISIBLE);
        }
    }

    //底部广告位
    @Override
    public void bottomAds(MatchBottomAdsBean adverts) {
        //邀请朋友轮播图
        List<Adverts> match_invite_advert = adverts.getMatch_invite_advert();
        mAdverts = match_invite_advert;
        if (views == null) {
            views = new ArrayList<>();
        } else {
            views.clear();
        }
        if (mCycleViewPager != null) {
            if (match_invite_advert.size() > 1) {
                mCycleViewPager.setCycle(true);
                mCycleViewPager.setWheel(true);
                mCycleViewPager.setTime(2000);
                mCycleViewPager.setIndicatorLayout(View.GONE);
                for (int i = 1; i < match_invite_advert.size(); i++) {//第一张图为默认分享的图片
                    views.add(ViewFactory.getImageView(this, match_invite_advert.get(i).getImg_url()));
                }
            } else {
                mCycleViewPager.setIndicatorLayout(View.GONE);
                mCycleViewPager.setWheel(false);
                for (int i = 0; i < match_invite_advert.size(); i++) {//第一张图为默认分享的图片
                    views.add(ViewFactory.getImageView(this, match_invite_advert.get(i).getImg_url()));
                }
            }
        }
        mCycleViewPager.setData(views, match_invite_advert, this);
        //赞助商跳转链接
        List<Adverts> match_buttom_advert = adverts.getMatch_buttom_advert();
        //获取赞助商广告
        if (match_buttom_advert == null && match_buttom_advert.size() == 0) {
            sponsorView.setVisibility(View.GONE);
            return;
        } else {
            sponsorView.setVisibility(View.VISIBLE);
        }
        if (mSponsorAdapter.getSponsorList() != null && match_buttom_advert.size() > 0) {
            mSponsorAdapter.clearList();
            mSponsorAdapter.addList(match_buttom_advert);
        }
    }

    @SuppressLint("DefaultLocale")
    private void refreshMatchDetail(JoinMatchBean.Result result) {
        if (result.getDeal() == 0) {
            tvWinPlayer.setText(String.format(getString(R.string.match_win_player), result.getPlayers(), 0));
        } else {
            tvWinPlayer.setText(String.format(getString(R.string.match_win_player), result.getPlayers(),
                    result.getPlayers() - result.getTotal_rank()));
        }
        if (result.getCanusecard() == 1 && result.getTotal_yield() < 0) {
            if (!TextUtils.isEmpty(mMyFlag) && mMyFlag.equals(IntentKey.MY_FLAG)) {
                tvReload.setVisibility(View.GONE);
            } else {
                tvReload.setVisibility(View.VISIBLE);
            }
        } else {
            tvReload.setVisibility(View.GONE);
        }
        tvWeekYield.setTextColor(result.getWeek_yield() >= 0 ? this.getResources().getColor(R.color.red)
                : this.getResources().getColor(R.color.green));
        tvWeekYield.setText(String.format("%+.2f%%", result.getWeek_yield() * 100));
        tvWeekRank.setText(String.format(getString(R.string.match_detail_rank), result.getWeek_rank()));
        tvDayYield.setTextColor(result.getDay_yield() >= 0 ? this.getResources().getColor(R.color.red)
                : this.getResources().getColor(R.color.green));
        tvDayYield.setText(String.format("%+.2f%%", result.getDay_yield() * 100));
        tvDayRank.setText(String.format(getString(R.string.match_detail_rank), result.getDay_rank()));
//        double v = result.getFrozen() + result.getUnfrozen() + result.getStocks_value();
//        String total = DoubleFromat.getMoneyDouble(v, 2);
//        tvAllMoney.setText(total);//总资产
        tvAllMoney.setText(DoubleFromat.getMoneyDouble(result.getTotal(), 2));//总资产
        tvStockMoney.setText(DoubleFromat.getStockDouble(result.getStocks_value(), 2));//股票总市值
        cpYield.setContentText(String.format(getString(R.string.match_detail_rank), result.getTotal_rank()));
        double total_yield = result.getTotal_yield();
        cpYield.runAnimat((float) total_yield * 100);//总收益

        tvCanUseMoney.setText(DoubleFromat.getStockDouble(result.getUnfrozen(), 2));//可用金额
        cpCanUseMoney.runAnimat((float) (result.getUnfrozen() / result.getTotal()) * 100);
        tvCloseMoney.setText(DoubleFromat.getStockDouble(result.getFrozen(), 2));
        cpCloseMoney.runAnimat((float) (result.getFrozen() / result.getTotal()) * 100);
        tvStockNum.setText(String.format("%.2f%%", result.getPosition() * 100));
        cpStock.runAnimat((float) result.getPosition() * 100);
        double deal = result.getDeal();
        tvMoneyYield.setText(String.format("%+.2f%%", (float) deal / 10000));
        cpMoney.runAnimat((float) result.getWeek_yield() * 100);
        if (pullScrollview.isRefreshing()) {
            pullScrollview.onRefreshComplete();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //初始截图
                mBitmapTop = initScreenShot();
            }
        }, 800);
    }

    private Bitmap initScreenShot() {
     /*   int h = 0;
        for (int i = 0; i < pullScrollview.getChildCount(); i++) {
            h += pullScrollview.getChildAt(i).getMeasuredHeight();
            pullScrollview.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }    // 获取状态栏高度
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;*/
        Bitmap bitmap = null;
        if (pullScrollview != null) {
            int width = pullScrollview.getMeasuredWidth();
            DisplayMetrics dm = new DisplayMetrics();
            //获取当前屏幕窗口的高度
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int windowHeight = dm.heightPixels;
            // 创建对应大小的bitmap
            bitmap = Bitmap.createBitmap(width, /*h-*/windowHeight,
                    Bitmap.Config.RGB_565);
            final Canvas canvas = new Canvas(bitmap);
            pullScrollview.draw(canvas);
        }
        return bitmap;
    }

    private void getWolle() {
        mJoinMatchPresenter.getUserWallet();
    }

    @SuppressLint("SetTextI18n")
    private Dialog resetAccDialog() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
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
        tv_paynum.setText(DoubleFromat.getStockDouble(mPaynum, 0) + getString(R.string.mfb));
        String s = SharedpreferencesUtil.getInstance().getString(SharedKey.MFB_PAY, "");
        tv_account_remain.setText(getString(R.string.account_sum) + s + getString(R.string.mfb));
        final Double my_mfb = Double.valueOf(s);
        if (my_mfb < mPaynum) {
            btn_confirm_reset.setText(getString(R.string.go_to_pay));
            tv_tip1.setText(getString(R.string.need_pay_tip));
        } else {
            btn_confirm_reset.setText(getString(R.string.comfirm_reset));
            mBtnStartReset.setEnabled(true);
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
                if (my_mfb < mPaynum) {//魔方宝小于设置额度,跳转充值
                    showToast(getString(R.string.pay_tip));
                    ShowActivity.goToAppGroup(MatchDetailActivity.this);
//                    ShowActivity.showActivity(getActivity(), PayDetailActivity.class);//跳转充值页面
                    dialog.dismiss();
                } else {//重置
                    mRl_reset_state.setVisibility(View.VISIBLE);
                }
            }
        });
        mBtnStartReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJoinMatchPresenter.resetMatch(String.valueOf(mMatchId));
            }
        });
        return dialog;
    }

    private void showCircleProgress() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mMCustomProgress, "progress", 0f, 100f);
        anim.setDuration(2000);
        anim.start();
        mMCustomProgress.setStateListener(new CustomMyCProgress.StateListener() {
            @Override
            public void loadFinished() {
                mTv_finish_tip.setVisibility(View.VISIBLE);
                mBtnStartReset.setEnabled(true);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mShareDialog != null) {
            mShareDialog.onNewIntent(intent);
        }
    }

    private ShareDialog mShareDialog;

    private void share() {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog(this, R.style.myDialogTheme);
        }
    }

    private void shareWeb(String webShareUrl) {
        if (mShareDialog != null) {
            mShareDialog.createDialog(this, String.valueOf(matchInfo.getResult().getMatch_id())).setShareWebUrl(webShareUrl).showI();
        }
    }

    private void shareImg(Bitmap imgBitmap) {
        if (mShareDialog != null) {
            mShareDialog.createDialog(this, String.valueOf(matchInfo.getResult().getMatch_id())).setShareImg(imgBitmap).showI();
        }
    }

    private void shareText(String text, Bitmap bitmap) {
        if (mShareDialog != null) {
            mShareDialog.createDialog(this, String.valueOf(matchInfo.getResult().getMatch_id())).setShareText(text, bitmap).showI();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HolderPositionBean.Result.Stocks item = mStockHoldAdapter.getItem(position);
        if (!TextUtils.isEmpty(mMyFlag) && mMyFlag.equals(IntentKey.MY_FLAG)) {
            ShowActivity.showStockDetail2(this, new StockMode(item.getName(), item.getSymbol()), String.valueOf(mMatchId));
            return;
        }
        mBuyDialog.setStocks(item).showI().setSellClick(this).setBuyClick(this);
    }

    @Override
    public void buyClick(HolderPositionBean.Result.Stocks stocks) {
        if (stocks != null) {
            ShowActivity.showStockBuyActivity(this, matchInfo, stocks.getVolumn_unfrozen(),
                    new StockMode(stocks.getName(), stocks.getSymbol()), Constans.BUY_FLAG, String.valueOf(mMatchId));
            mBuyDialog.dissMissI();
        }
    }

    @Override
    public void sellClick(HolderPositionBean.Result.Stocks stocks) {
        if (stocks != null) {
            ShowActivity.showStockBuyActivity(this, matchInfo, stocks.getVolumn_unfrozen(),
                    new StockMode(stocks.getName(), stocks.getSymbol()), Constans.SELL_FLAG, String.valueOf(mMatchId));
            mBuyDialog.dissMissI();
        }
    }

    @Override
    public void onCommit(HolderPositionBean.Result.Stocks stockholdsBean) {
        if (stockholdsBean != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.STOCK_HOLDER, new StockMode(stockholdsBean.getName(), stockholdsBean.getSymbol()));
            ShowActivity.showActivityForResult(this, bundle, StockCommitActivity.class, Constans.ADD_COMMENT_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constans.ADD_COMMENT_REQUEST && resultCode == Constans.ADD_COMMENT_RESPONSE && data != null) {
            int intExtra = data.getIntExtra(IntentKey.COMMENT_COUNT, -1);
            mStockHoldAdapter.setCommentCount(intExtra);
        }
    }

    private boolean isNeedRestart() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            String s = topActivity.toString();
//            ActivityManager.RunningTaskInfo taskInfo = tasks.get(0);
            if (topActivity.getPackageName().equals(this.getPackageName())) {
                // 若当前栈顶界面是AssistActivity，则需要手动关闭
                if (topActivity.getClassName().equals("com.tencent.connect.common.AssistActivity"))
                    return true;
            }
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StockSellEvent event) {
        // 更新持股信息
        mJoinMatchPresenter.holderPosition(mMatchId,ParamsKey.D_PAGE, MatchSharedUtil.UserId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSkipMatchDetailEvent(SkipMatchDetailEvent event) {
        mMatchId = Integer.parseInt(event.getMatchID());
        mMyFlag = event.getMyFlag();
        MatchRank.Result.Yields last_deal = event.getLast_deal();
        mUserID = String.valueOf(last_deal.getUser_id());
        mJoinMatchPresenter.joinMatch(mMatchId, mUserID);
        mJoinMatchPresenter.holderPosition(mMatchId,ParamsKey.D_PAGE, mUserID);
        mJoinMatchPresenter.getMatchInfo(String.valueOf(mMatchId), mUserID);
        btnAllHold.setVisibility(View.GONE);
        Glide.with(this)
                .load(last_deal.getAvatar())
                .apply(GlideOptionsManager.getInstance().getRequestOptions())
                .into(avatar);
        otherUserName.setText(last_deal.getNickname());
        if (!TextUtils.isEmpty(mMyFlag) && mMyFlag.equals(IntentKey.MY_FLAG)) {
            pullScrollview.setMode(PullToRefreshBase.Mode.DISABLED);
            noticeLayout.setVisibility(View.GONE);
            topTab.setVisibility(View.GONE);
//            tvReload.setVisibility(View.GONE);
            other.setVisibility(View.VISIBLE);
            bottomItemRecord.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShareDialog != null) {
            mShareDialog.recyclerBitmap();
            if (mShareDialog.isShowing()) {
                mShareDialog.dismiss();
            }
        }
        if (mBitmapTop != null && !mBitmapTop.isRecycled()) {
            mBitmapTop.recycle();
        }
        if (mSrcBitmap != null && !mSrcBitmap.isRecycled()) {
            mSrcBitmap.recycle();
        }
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onImageClick(Adverts info, int position, View imageView) {
        if (mAdverts.size() > 1) {
            //轮播图点击回调
            ShowActivity.circleImageClick(info, this);
        } else if (mAdverts.size() == 1) {
            share();
            mShareDialog.setFlag(Constans.WX_SHARE_WEB);
            shareWeb(Constans.SHARE_URL);
        }
    }

    @Override
    public void sponsor(int position, Adverts ad) {
        //跳转赞转上链接
        if (ad != null) {
            if (!TextUtils.isEmpty(ad.getJump_url())) {
                ShowActivity.showWebViewActivity(this, ad.getJump_url(), "");
            }
        }
    }
}

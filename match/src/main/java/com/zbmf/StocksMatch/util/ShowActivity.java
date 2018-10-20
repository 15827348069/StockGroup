package com.zbmf.StocksMatch.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.DealActivity;
import com.zbmf.StocksMatch.activity.FindPassWordActivity;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.activity.MatchDescActivity;
import com.zbmf.StocksMatch.activity.MatchDetailActivity;
import com.zbmf.StocksMatch.activity.MatchHoldActivity;
import com.zbmf.StocksMatch.activity.MatchRankActivity;
import com.zbmf.StocksMatch.activity.MatchTraderInfoActivity;
import com.zbmf.StocksMatch.activity.MatchTrustsActivity;
import com.zbmf.StocksMatch.activity.NoticeActivity;
import com.zbmf.StocksMatch.activity.PopDialogActivity;
import com.zbmf.StocksMatch.activity.QueryActivity;
import com.zbmf.StocksMatch.activity.RemarkListActivity;
import com.zbmf.StocksMatch.activity.StockBuyActivity;
import com.zbmf.StocksMatch.activity.StockCommitActivity;
import com.zbmf.StocksMatch.activity.StockDetailActivity;
import com.zbmf.StocksMatch.activity.UserActivity;
import com.zbmf.StocksMatch.activity.WebViewActivity;
import com.zbmf.StocksMatch.activity.WinAPrizeActivity;
import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.HomeMatchList;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.MatchList3Bean;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.bean.MatchRank;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.StocksMatch.bean.SearchMatchBean;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.bean.StockholdsBean;
import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.StocksMatch.bean.User;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.presenter.HomePresenter;
import com.zbmf.StocksMatch.presenter.LaunchPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuhao
 * on 2017/11/29.
 */

public class ShowActivity {
    public static Intent intent = null;
    private static final String init_time="1520452057";

    @SuppressLint("ShowToast")
    public static boolean checkLoginStatus(Context context, String msg) {
        if (msg.equals(Constans.INVALID_ERR_CODE) || msg.equals(Constans.INVALID_ERR_MSG) || TextUtils.isEmpty(MatchSharedUtil.AuthToken())) {
            Toast.makeText(context, context.getString(R.string.user_invalid_login), Toast.LENGTH_SHORT);
            return true;
        } else if (msg.equals(Constans.INVALID_LOGIN_CODE) || msg.equals(Constans.INVALID_LOGIN_MSG) || TextUtils.isEmpty(MatchSharedUtil.AuthToken())) {
            Toast.makeText(context, context.getString(R.string.invalid_login_err), Toast.LENGTH_SHORT);
            return true;
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            return false;
        }
    }

    public static void StartActivity(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (intent != null) {
            intent = null;
        }
    }

    public static void StartActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        if (intent != null) {
            intent = null;
        }
    }

    public static void StartActivityForResult(Activity activity, Intent intent, int code) {
        activity.startActivityForResult(intent, code);
        if (intent != null) {
            intent = null;
        }
    }

    public static void showActivity(Context context, Class<?> c) {
        intent = new Intent(context, c);
        StartActivity(context, intent);
    }

    public static void showActivity(Activity activity, Bundle aBundle, Class<?> c) {
        Intent intent = new Intent(activity, c);
        if (null != aBundle) {
            intent.putExtras(aBundle);
        }
        StartActivity(activity, intent);
    }

    public static void showActivityForResult(Activity activity, Bundle aBundle, Class<?> c, int code) {
        Intent intent = new Intent(activity, c);
        if (null != aBundle) {
            intent.putExtras(aBundle);
        }
        StartActivityForResult(activity, intent, code);
    }

    /**
     * 是否登录，未登录打开登录页面
     *
     * @param activity
     * @return
     */
    @SuppressLint("ShowToast")
    public static boolean isLogin(Activity activity, int flag) {
        if (TextUtils.isEmpty(MatchSharedUtil.AuthToken())) {
            Toast.makeText(activity, activity.getString(R.string.login_tip), Toast.LENGTH_SHORT);
            intent = new Intent(activity, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(ParamsKey.FG_FLAG, flag);
            intent.putExtras(bundle);
            StartActivity(activity, intent);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否已经登录
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(MatchSharedUtil.AuthToken());
    }

    /**
     * 跳转找回密码页面
     *
     * @param activity
     * @param phone
     */
    public static void showFindPasswordActivity(Activity activity, String phone) {
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.USER, phone);
        showActivity(activity, bundle, FindPassWordActivity.class);
    }

    /**
     * 加载h5
     */
    public static void showWebViewActivity(Activity activity, String url, String webTitle) {
        intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(IntentKey.WEB_URL, url);
        intent.putExtra(IntentKey.WEB_TITLE, webTitle);
        StartActivity(activity, intent);
    }

    /**
     * 判断条件跳转H5
     *
     * @param activity
     * @param adverts
     */
    public static void showWebViewActivityJudge(Activity activity,Adverts adverts){
        if (adverts != null) {
            if (adverts.getJump_url().contains(Constans.MATCH_AD_TYPE)) {
                //如果是比赛类型的广告，则请求接口，判断是否已经参赛
                new LaunchPresenter().getMatchDesc();
            } else {
                ShowActivity.showWebViewActivity(activity, adverts.getJump_url(), "");
            }
        }
    }

    /**
     * MatchInfo 股票买入
     *
     * @param activity
     */
    public static void showStockBuyActivity(Activity activity, MatchInfo matchInfo,
                                            int volumnUnfrozen, StockMode stockMode, int flag, String matchId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.MATCH_INFO, matchInfo);
        bundle.putSerializable(IntentKey.STOCK_HOLDER, stockMode);
        bundle.putInt(IntentKey.FLAG, flag);
        bundle.putInt(IntentKey.VUF, volumnUnfrozen);
        bundle.putString(IntentKey.MATCH_ID, matchId);
        showActivity(activity, bundle, StockBuyActivity.class);
    }

    /**
     * 跳转通告页面
     *
     * @param activity
     * @param notice
     */
    public static void showNoticeActivity(Activity activity, NoticeBean.Result notice) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.NOTICE, notice);
        showActivity(activity, bundle, NoticeActivity.class);
    }

    /**
     * 跳转用户个人资料
     *
     * @param activity
     * @param user
     */
    public static void showUserActivity(Activity activity, User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.USER, user);
        showActivity(activity, bundle, UserActivity.class);
    }

    /**
     * 跳转交易记录页面
     *
     * @param activity
     * @param matchInfo
     */
    public static void showDealsListActivity(Activity activity, MatchInfo matchInfo, String matchId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.MATCH_INFO, matchInfo);
        bundle.putString(IntentKey.MATCH_ID, matchId);
        showActivity(activity, bundle, QueryActivity.class);
    }

    /**
     * 跳转用火获奖页面
     *
     * @param activity
     * @param matchInfo
     */
    public static void showUserPrizeActivity(Activity activity, MatchInfo matchInfo, String matchId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.MATCH_INFO, matchInfo);
        bundle.putString(IntentKey.MATCH_ID, matchId);
        showActivity(activity, bundle, WinAPrizeActivity.class);
    }

    /**
     * 跳转持仓页面
     *
     * @param activity
     * @param matchId
     * @param userId
     */
    public static void showMatchHoldActivity(Activity activity, MatchInfo matchInfo, String matchId, String userId
            , int flag/* TraderHolderPosition.Holds holds*/) {
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.USER, userId);
        bundle.putString(IntentKey.MATCH_ID, matchId);
        bundle.putSerializable(IntentKey.MATCH_INFO, matchInfo);
        bundle.putInt(IntentKey.FLAG, flag);
        showActivity(activity, bundle, MatchHoldActivity.class);
    }

    /**
     * 跳转股票评论页面
     *
     * @param activity
     * @param stockMode
     */
    public static void showStockCommentActivity(Activity activity, StockMode stockMode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCK_HOLDER, stockMode);
//        bundle.putInt(IntentKey.FLAG,intentCode);
        showActivity(activity, bundle, StockCommitActivity.class);
    }

    /**
     * 跳转添加自选股页面
     *
     * @param activity
     */
    public static void addStockActivity(Activity activity, Intent intent, int code) {
//        showActivity(activity, AddStockActivity.class);
        StartActivityForResult(activity, intent, code);

    }

    public static void showDealActivity(Activity activity, String userId, String flag, String matchID, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.USER, userId);
        bundle.putString(IntentKey.FLAG, flag);
        bundle.putString(IntentKey.MATCH_ID, matchID);
        bundle.putString(IntentKey.ID, id);
        showActivity(activity, bundle, DealActivity.class);
    }

    /**
     * 如果参赛，跳转比赛详情页面
     * 如果没有参赛,打开推荐比赛简介
     *
     * @param activity
     * @param matchBean
     */
    public static void showRecoMatchDetail(Activity activity, HomeMatchList.Result.Recommend matchBean) {
        if (matchBean.getIs_player()) {//1表示参赛 0表示没有参赛
            Bundle bundle = new Bundle();//跳转已报名的详情页面
            bundle.putSerializable(IntentKey.RECM_MATCH, matchBean);
            bundle.putInt(IntentKey.MATCH_END, matchBean.getIs_end1());
            ShowActivity.showActivity(activity, bundle, MatchDetailActivity.class);
        } else {//跳转比赛详情页面
            showRecoMathchDesc(activity, matchBean);
        }
    }

    /**
     * 跳转我的委托列表页面
     *
     * @param activity
     * @param matchInfo
     */
    public static void showMyTrustActivity(Activity activity, MatchInfo matchInfo, String matchId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.MATCH_INFO, matchInfo);
        bundle.putString(IntentKey.MATCH_ID, matchId);
        showActivity(activity, bundle, MatchTrustsActivity.class);
    }

    /**
     * 跳转用户参赛详情页面
     *
     * @param activity
     * @param matches
     */
    public static void showUserMatchDetail(Activity activity, MatchNewAllBean.Result.Matches matches) {
        if (matches.getIs_player()) {//1表示参赛 0表示没有参赛
            Bundle bundle = new Bundle();//跳转已报名的详情页面
            bundle.putSerializable(IntentKey.USER_MATCH, matches);
            bundle.putInt(IntentKey.MATCH_END, matches.getIs_end1());
            ShowActivity.showActivity(activity, bundle, MatchDetailActivity.class);
        } else {//跳转比赛详情页面
            showUserMathchDesc(activity, matches);
        }
    }

    /**
     * 如果参赛，跳转比赛详情页面
     * 如果没有参赛打开热门比赛简介
     *
     * @param activity
     * @param matchBean
     */
    public static void showHotMatchDetail(Activity activity, HomeMatchList.Result.Hot matchBean) {
        if (matchBean.getIs_player()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.HOT_MATCH, matchBean);
            bundle.putInt(IntentKey.MATCH_END, matchBean.getIs_end1());
            ShowActivity.showActivity(activity, bundle, MatchDetailActivity.class);
        } else {
            showHotMathchDesc(activity, matchBean);
        }
    }

    /**
     * 如果参赛跳转比赛的详情页面
     *
     * @param activity
     * @param matches
     */
    public static void showMatchDetail(Activity activity, MatchNewAllBean.Result.Matches matches) {
        if (matches.getIs_player()) {//1表示参赛 0表示没有参赛
            Bundle bundle = new Bundle();//跳转已报名的详情页面
            bundle.putSerializable(IntentKey.MATCH, matches);
            bundle.putInt(IntentKey.MATCH_END, matches.getIs_end1());
            ShowActivity.showActivity(activity, bundle, MatchDetailActivity.class);
        } else {//跳转比赛介绍页面
            showMathchDesc(activity, matches);
        }
    }

    /**
     * 带标记跳转比赛详情页面，查看他人的参赛信息，不可操作
     *
     * @param activity
     */
    public static void showMatchDetail(Activity activity/*,String matchId,String myFlag,String matchName*/) {
        Bundle bundle = new Bundle();//跳转已报名的详情页面
//        bundle.putString(IntentKey.MATCH_ID,matchId);
//        bundle.putString(IntentKey.MY_FLAG,myFlag);
//        bundle.putString(IntentKey.MATCH_NAME,matchName);
        ShowActivity.showActivity(activity, bundle, MatchDetailActivity.class);
    }

    /**
     * 跳转操盘高手页面
     *
     * @param activity
     * @param traders
     */
    public static void showMatchTraderInfoActivity(Activity activity, Traders traders) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.TRADER, traders);
        ShowActivity.showActivity(activity, bundle, MatchTraderInfoActivity.class);
    }

    /**
     * 跳转高校券商
     *
     * @param activity
     * @param matches
     */
    public static void showMatch3(Activity activity, MatchList3Bean.Result.Matches matches) {
        if (matches.getIs_player()) {//1表示参赛 0表示没有参赛
            Bundle bundle = new Bundle();//跳转已报名的详情页面
            bundle.putSerializable(IntentKey.MATCH3, matches);
            ShowActivity.showActivity(activity, bundle, MatchDetailActivity.class);
        } else {//跳转比赛详情页面
            showMathch3Desc(activity, matches);
        }
    }

    /**
     * @param activity
     * @param result
     */
    public static void showMatch4(Activity activity, MatchDescBean.Result result) {
        if (result.getIs_player()) {//1表示参赛 0表示没有参赛
            Bundle bundle = new Bundle();//跳转已报名的详情页面
            bundle.putSerializable(IntentKey.MATCH4, result);
            ShowActivity.showActivity(activity, bundle, MatchDetailActivity.class);
        } else {//跳转比赛介绍页面
            showMathch4Desc(activity, result);
        }
    }

    /**
     * @param activity
     * @param matches
     */
    public static void showSearchMatch(Activity activity, SearchMatchBean.Result.Matches matches) {
        if (matches.getIs_player()) {//1表示参赛 0表示没有参赛
            Bundle bundle = new Bundle();//跳转已报名的详情页面
            bundle.putSerializable(IntentKey.SEARCH_MATCH, matches);
            bundle.putInt(IntentKey.MATCH_END, matches.getIs_end1());
            ShowActivity.showActivity(activity, bundle, MatchDetailActivity.class);
        } else {//跳转比赛详情页面
            showSearchMatchDesc(activity, matches);
        }
    }

    /**
     * 推荐比赛简介页面
     *
     * @param activity
     * @param matchBean
     */
    public static void showRecoMathchDesc(Activity activity, HomeMatchList.Result.Recommend matchBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.RECM_MATCH, matchBean);
        ShowActivity.showActivity(activity, bundle, MatchDescActivity.class);
    }

    /**
     * 用户参赛详情
     *
     * @param activity
     * @param matches
     */
    public static void showUserMathchDesc(Activity activity, MatchNewAllBean.Result.Matches matches) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.USER_MATCH, matches);
        ShowActivity.showActivity(activity, bundle, MatchDescActivity.class);
    }

    /**
     * 热门比赛简介页面
     *
     * @param activity
     * @param matchBean
     */
    public static void showHotMathchDesc(Activity activity, HomeMatchList.Result.Hot matchBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.HOT_MATCH, matchBean);
        ShowActivity.showActivity(activity, bundle, MatchDescActivity.class);
    }

    /**
     * 跳转备注列表页面
     *
     * @param activity
     * @param stockholdsBean
     */
    public static void showRemarkListActivity(Activity activity, StockholdsBean stockholdsBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.REMARK_BEAN_LIST, stockholdsBean);
        showActivity(activity, bundle, RemarkListActivity.class);
    }

    /**
     * 跳转比赛拍名页面
     *
     * @param activity
     * @param matchID
     * @param flag     0x17/操盘高手标记
     */
    public static void showMatchRankActivity(Activity activity, String matchID, int flag, boolean hideFlag) {
        Bundle mBundle = new Bundle();
        mBundle.putString(IntentKey.MATCH_ID, matchID);
        mBundle.putInt(IntentKey.FLAG, flag);
        mBundle.putBoolean(IntentKey.TRADER_HIDE, hideFlag);
        ShowActivity.showActivity(activity, mBundle, MatchRankActivity.class);
    }

    /**
     * 跳转到股票详情页面
     *
     * @param activity
     * @param lastDeal
     */
    public static void showStockDetail(Activity activity, MatchRank.Result.Yields.Last_deal lastDeal/*,
                                       MatchInfo mMatchInfo*/, String matchID) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCK_HOLDER, lastDeal);
        bundle.putString(IntentKey.MATCH_ID, matchID);
        /*bundle.putSerializable(IntentKey.MATCH_INFO,mMatchInfo);*/
        showActivity(activity, bundle, StockDetailActivity.class);
    }

    public static void showStockDetail2(Activity activity, StockMode stockMode, String matchId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCK_HOLDER, stockMode);
        bundle.putString(IntentKey.MATCH_ID, matchId);
        showActivity(activity, bundle, StockDetailActivity.class);
    }

    /**
     * 跳转比赛的推荐页面
     *
     * @param activity
     * @param matchBean
     */
    public static void showMathchDesc(Activity activity, MatchNewAllBean.Result.Matches matchBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.MATCH, matchBean);
        ShowActivity.showActivity(activity, bundle, MatchDescActivity.class);
    }

    public static void showMathch3Desc(Activity activity, MatchList3Bean.Result.Matches matches) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.MATCH3, matches);
        ShowActivity.showActivity(activity, bundle, MatchDescActivity.class);
    }

    public static void showMathch4Desc(Activity activity, MatchDescBean.Result result) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.MATCH4, result);
        ShowActivity.showActivity(activity, bundle, MatchDescActivity.class);
    }

    public static void showSearchMatchDesc(Activity activity, SearchMatchBean.Result.Matches matches) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.SEARCH_MATCH, matches);
        ShowActivity.showActivity(activity, bundle, MatchDescActivity.class);
    }


    //跳转充值页面
    public static void showPayActivity(Activity activity) {

    }

    //跳转应用市场
    public static void goToMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + Method.PACKAGE_NAME);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    //跳转到圈子app
    public static void goToAppGroup(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(Method.PACKAGE_NAME);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            goToMarket(context);
        }
    }

    //跳转到PopWindowActivity
    @SuppressLint("SimpleDateFormat")
    public static void skipPopWindowAct(PopWindowBean popWindowBean, Context context, String matchId) {
        //获取弹窗的数据
        if (popWindowBean == null) {
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        String currentDay = format.format(date);
        String saveFileName = MatchSharedUtil.UserId() + "_save_pop";
        PopWindowSharePreferenceUtils.getInstance().initShareUtils(context, saveFileName);
        // 在此处清除PopWindowShareference   当前时间减去存储的时间大于18时，即可清除数据
//        PopWindowSharePreferenceUtils.getInstance().clearPopData();     //若测试,请打开这句注销的代码,清空存储
        String initTime = PopWindowSharePreferenceUtils.getInstance().getInitTime(init_time);
        if (initTime.equals(init_time)){
            PopWindowSharePreferenceUtils.getInstance().putInitTime(String.valueOf(l));//保存初始化时间
        }else {
            long initTimeL = Long.parseLong(initTime);
            String s = DateUtil.timeStampToFormat(initTimeL);
            if (Long.parseLong(s) >= Constans.CLEAR_TIME_MIN) {//当前时间与存储的时间差值大于18时，清空popWindow信息的存储
                PopWindowSharePreferenceUtils.getInstance().clearPopData();
            }
        }
        String userId1 = MatchSharedUtil.UserId();
        String popId1 = String.valueOf(popWindowBean.getId());
        String userId = PopWindowSharePreferenceUtils.getInstance().getUserId("-100");
        if (TextUtils.isEmpty(matchId)) {//全站的弹窗判断
            String popTime = PopWindowSharePreferenceUtils.getInstance().getPopTime(init_time);
            long popL = Long.parseLong(popTime);//存储的时间戳
            Date popDate = new Date(popL);
            String popOldDay = format.format(popDate);//当天日期
            String popId = PopWindowSharePreferenceUtils.getInstance().getPopId("-1");
            //如果 当前用户ID 弹窗ID 存储的日期于当天日期都相等的话，表示这个用户今天已经弹出过这个弹窗 在首页
            if (userId.equals(userId1) && popId.equals(popId1) && popOldDay.equals(currentDay)) {
                return;
            }
            showPopWindowAds(popWindowBean,context,l,matchId);
        } else {//单个比赛的弹窗是否显示判断
            String matchTime = PopWindowSharePreferenceUtils.getInstance()
                    .getMatchTime(String.valueOf(popWindowBean.getId()) + "_time", init_time);
            long matchL = Long.parseLong(matchTime);//存储的时间戳
            Date matchDate = new Date(matchL);
            String matchOldDay = format.format(matchDate);//当天日期
            String popMatchID = PopWindowSharePreferenceUtils.getInstance().getPopMatchID(matchId, "-122");
            String popWindowId = PopWindowSharePreferenceUtils.getInstance().getPopWindowId(popId1, "-2");
//            Log.i("--TAG","------- 11   -------"+matchId);
            //如果 当前用户ID 弹窗ID 存储的日期于当天日期 比赛ID都相等的话，表示今天这个用户的这个比赛已经弹出过这个弹窗
            if (userId.equals(userId1) && popWindowId.equals(popId1) && matchOldDay.equals(currentDay) && popMatchID.equals(matchId)) {
//                Log.i("--TAG","------- 22   -------"+matchId);
                return;
            }
//            Log.i("--TAG","------- 33   -------"+matchId);
            showPopWindowAds(popWindowBean,context,l,matchId);
        }
    }

    private static void showPopWindowAds(PopWindowBean popWindowBean,Context context,long l,String matchId){
        if (!TextUtils.isEmpty(popWindowBean.getUrl())) {
            //如果弹窗跳转的url不为空 并且该用户第一次进入时该弹窗内容第一次显示
            Intent intent = new Intent(context, PopDialogActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.POP_MSG, popWindowBean);
            intent.putExtras(bundle);
            context.startActivity(intent);
            //跳出弹窗之后存储弹窗以及用户ID
            //存储的文件名字：当前用户ID_当天时间
            PopWindowSharePreferenceUtils.getInstance().putPopWindowMsg(MatchSharedUtil.UserId());
            if (!TextUtils.isEmpty(matchId)) {
                //存储单个比赛弹窗的PopID matchID
                PopWindowSharePreferenceUtils.getInstance().putPopMatchID(matchId, matchId);
                PopWindowSharePreferenceUtils.getInstance().
                        putMatchTime(String.valueOf(popWindowBean.getId())+"_time",String.valueOf(l));
                PopWindowSharePreferenceUtils.getInstance().
                        putPopWindowId(String.valueOf(popWindowBean.getId()),String.valueOf(popWindowBean.getId()));
            }else {
                //存储全站比赛的弹窗PopID
                PopWindowSharePreferenceUtils.getInstance().putPopId(String.valueOf(popWindowBean.getId()));
                PopWindowSharePreferenceUtils.getInstance().putPopTime(String.valueOf(l));
            }
        }
    }

    //轮播图点击跳转
    public static void circleImageClick(Adverts phoneAd, Activity activity) {
        if (phoneAd != null) {
            if (phoneAd.getJump_url().contains(Constans.MATCH_AD_TYPE)) {
                String jump_url = phoneAd.getJump_url();//app://match/2121/
                int h = jump_url.lastIndexOf("h") + 2;
                String adMatchId = "";
                if (jump_url.endsWith("/")) {
                    adMatchId = jump_url.substring(h, jump_url.length() + 1);
                } else {
                    adMatchId = jump_url.substring(h, jump_url.length());
                }
                //如果是比赛类型的广告，则请求接口，判断是否已经参赛
                HomePresenter.getInstance().getMatchDesc(Integer.parseInt(adMatchId));
            } else {
                ShowActivity.showWebViewActivity(activity, phoneAd.getJump_url(), "");
            }
        }
    }
}

package com.zbmf.StocksMatch.api;

/**
 * Created by xuhao
 * on 2017/11/22.
 */

public class Method {
    public static final String LOGIN="zbmf.auth.login";//登录
    public static final String VERS="zbmf.version.vers";//更新版本
    public static final String RECOMMEND=
//            "zbmf.advert.getAdverts";
            "mcc.match.recommend";//获取推荐的图片
//    public static final String GET_MATCH_NOSTOPLIST="mcc.match.getMathNoStopList";//全部比赛
//    public static final String GETPLAYER="mcc.match.getPlayer";//比赛信息
//    public static final String GET_MATCH_YIELD_LIST="mcc.match.getMatchYieldList";//排行榜
//    public static final String GET_HOLD_LIST="mcc.match.getHoldlist";//获取持仓列表----圈子接口
//    public static final String GET_FOCUS_LIST = "mcc.stocks.getFocusList";//获取自选股
    public static final String GET_TRADER_INFO="mcc.traders.traderInfo";//获取操盘高手详情
    public static final String VERIFY_CODE="mcc.match.verifyCode";//验证手机报名比赛
    public static final String SUPREME_MATCH="mcc.match.supremeMatch";//推荐比赛
    public static final String TRADER_RANKS="mcc.traders.traderRanks";//操盘高手
    public static final String GET_RUNMATCHS="mcc.match.getRunMatchs";//用户参加的比赛
    public static final String GET_USERINFO="mcc.users.getuserinfo";//获取用户信息
    public static final String DEALSYS="mcc.match.dealSys";//最新交易

    public static final String STOCK_REAL_TIME_INFO="mcc.match.getStockRealtimeInfo";
    public static final String STOCK_TIME_INFO="mcc.match.getRealtimeInfo";
    public static final String TRADER_BUY="mcc.traders.buy";//高手续订


    public static final String NEW_BUY_STOCK="zbmf.match.buy";//买入股票
    public static final String NEW_SELL_STOCK="zbmf.match.sell";//卖出股票
    public static final String GET_STOCK="search/stockquery?";
    public static final String PACKAGE_NAME="com.zbmf.StockGroup";
    public static final String REGISTER_CODE="zbmf.auth.code";//发送注册验证码
    public static final String SEND_FORGET_PASSWORD_CODE="zbmf.auth.codeForget";//发送忘记密码验证码
    public static final String BIND_BIND_CODE="zbmf.auth.codeBind";//发送绑定手机验证码
    public static final String REGISTER="zbmf.auth.register";//注册
    public static final String REGISTER_PASSWORD="zbmf.auth.resetForget";//重置密码
    public static final String FORGET_PASSWORD="zbmf.auth.pwdForget";//设置忘记密码
    public static final String VER_CODE="zbmf.auth.verifyForget";//校验验证码
    public static final String POP_WINDOW="zbmf.window.load";//弹窗
    //首页
    public static final String HOME_MATCH_SCHOOL="zbmf.match.school";//参赛学校
    public static final String HOME_STOCK_INDEX="zbmf.match.index";//获取证券指数
    public static final String HOME_MATCH_RECOMMEND="zbmf.match.recommend";//推荐比赛
    public static final String HOME_MATCH_CITY="zbmf.match.city";//推荐比赛城市
    public static final String HOME_TOP_AD="zbmf.advert.getAdverts";//首页top轮播广告
    public static final String TRADER_DEAL_RECORD="zbmf.traders.traderDeals";//操盘高手的交易记录
    public static final String TRADER_HOLDER_POSITION_RECORD="mcc.traders.traderHolds";//高手持仓记录
    public static final String MATCH_DESC="zbmf.match.info";//比赛详情
    public static final String APPLY_MATCH="zbmf.match.apply";//报名比赛
    public static final String MATCH_JOIN="zbmf.match.player";//参加比赛
    public static final String MATCH_NOTICE="zbmf.match.announcementList";//比赛公告列表
    public static final String HOLDER_POSITION="zbmf.match.holdList";//持仓数据
    public static final String SEARCH_MATCH="zbmf.match.searchMatch";//搜索比赛
    public static final String MATCH_LIST_3="zbmf.match.orgList";//比赛列表-城市/搞笑/全上
    public static final String MATCH_NEW="zbmf.match.newList";//比赛列表-最新
    public static final String MATCH_ALL="zbmf.match.allList";//比赛列表-全部
    public static final String SEND_CODE="zbmf.match.sendCode";//申请参赛-发送验证码
    public static final String USER_MATCH="zbmf.match.userMatch";//用户参赛/
    public static final String MY_ORDER_LIST="zbmf.match.orderList";//比赛-委托列表
    public static final String REVOKE="zbmf.match.withdraw";//比赛-委托撤单
    public static final String RECORD_LIST="zbmf.match.dealList";//交易记录
    public static final String PRIZE_LIST="zbmf.match.recordList";//用户获奖记录
    public static final String GET_WALLET="zbmf.wallet.getWalletInfo";//获取用户资金
    public static final String RESET_MATCH="zbmf.match.reset";//重置比赛
    public static final String MATCH_BOTTOM_ADS="zbmf.advert.getMatchAdverts";//比赛-单个比赛页面获取中下部广告位

    /*---------------排行----------------*/
    public static final String RECENT_DEALS = "zbmf.match.recentDeal";//最新交易
    public static final String MATCH_RANK = "zbmf.match.yieldList";//排行榜--榜单
    public static final String SEARCH_USER_BLOG_INFO="zbmf.blog.searchUserBlogInfo";//搜索博文详情
    public static final String LOAD_VIDEO="zbmf.video.AndroidLoadVideo";//加载视频

    /*-----------------自选股----------------*/
    public static final String FOCUS_STOCK_LIST="zbmf.stock.focusList";//自选股列表
    public static final String ADD_STOCK="zbmf.stock.addFocus";//新增自选股
    public static final String STOCK_REMARK_LIST="zbmf.stock.remarkList";//自选股备注列表
    public static final String ADD_STOCK_REMARK="zbmf.stock.addRemark";//添加自选股备注
    public static final String DEL_STOCK_REMARK="zbmf.stock.delRemark";//删除自选股备注
    public static final String DEL_STOCK="zbmf.stock.delFocus";//删除自选股

    public static final String STOCK_ASK_LIST="zbmf.ask.list";//互动列表
    public static final String COMMENTS="mcc.match.getsComment";//获取评论消息
    public static final String ADD_COMMENTS="mcc.match.addComment";//添加股票评论
    public static final String UP_AVATAR="zbmf.users.uploadAvatar";//上传图像
    public static final String UP_NICK="zbmf.users.updateUser";// 更新用户昵称

    /*--------------------充值----------------------*/
    public static final String PAY_LIST="zbmf.pay.products";//获取充值列表
    public static final String WEIXIN_PAY="zbmf.pay.weixin";//微信支付
    public static final String LOGIN_WE_CHAT="zbmf.auth.weChat";    //微信登陆
    public static final String LOGIN_Q_SINA="zbmf.auth.getAccessTokenByOpenapi";//登录QQ 新浪

    public static final String NEW_LIVE_MSG_READ="com.zbmf.StockGroup.new_live_msg_read";
    public static final String UPDATE_VIDEO_LIST="com.zbmf.StockGroup.videoList";
    public static final String UP_DATA_MESSAGE="com.zbmf.StockGroup.Updata_Message";
}

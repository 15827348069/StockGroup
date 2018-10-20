package com.zbmf.StockGroup.api;

/**
 * Created by xuhao on 2016/12/12.
 */

public class AppUrl {
    //普通版圈子pro  VqbBtMjZwLK1r2RJ57UQj4E2mZEgYZc   W8gN3fQi
    //普通版圈子 tlHmM7iap00solm1q9WsBUGTAfMm4Qm   secret:BrZ2g1TB
    public static final String API_KEY = "tlHmM7iap00solm1q9WsBUGTAfMm4Qm";
    public static final String API_SECRET = "BrZ2g1TB";
    public static final String DEVICE_TYPE="1";//设备标识
    public static final String REDIRECT_URL = "http://www.sina.com";

    public static final String SCOPE ="email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog,"
                    + "invitation_write";
    // FIXME: 连接正式服务器
    public static  String ALL_URL_PREFIX = "https://passport.zbmf.com/rest/json/";
    public static  String ALL_URL="https://group.zbmf.com/rest/json/";
    public static  String ZBMF_URL="http://center.zbmf.com/rest/json/";
    public static  String Walle_URL="https://center.zbmf.com/rest/json/";
    public static  String MATCH_URL="http://rest.zbmf.com/newapi/json/";
    public static  String GUO_PIAO_URL="https://gupiao.zbmf.com/rest/json/";
    public static  final String SINA_MESSAGE="https://api.weibo.com/2/users/show.json";
    public static  final String QQ_MESSAGE="https://graph.qq.com/user/get_user_info";
    public static  String STOCK_URL="https://stock.zbmf.com/rest/json/";
    //client链接地址
    public static final String URL_CHAT_LOCATION="ws://120.27.163.136:7878";
    //版本更新
    public static final String VERS="zbmf.version.vers";
    /**
     * 用户相关
     */
    //用户绑定client_id
    public static final String BIND="zbmf.im.bind";
    // 用户登录
    public static final String URL_LOGINAUTH = "zbmf.auth.login";
    //微信登陆
    public static final String weChat="zbmf.auth.weChat";
    //qq登陆，微博登陆
    public static final String getAccessTokenByOpenapi="zbmf.auth.getAccessTokenByOpenapi";

    public static final String LOGIN_OUT="zbmf.users.logout";//退出登陆
    //发送验证码
    public static final String POST_CODE="zbmf.auth.code";
    //注册账户
    public static final String REGISTER="zbmf.auth.register";
    //发送重置验证码
    public static final String CODE_FORGET="zbmf.auth.codeForget";

    public static final String uploadAvatar="zbmf.users.uploadAvatar";

    //默认头像列表
    public static final String defaultAvatar="zbmf.users.defaultAvatar";

    //更新头像
    public static final String updateUser="zbmf.users.updateUser";

    //校验充值验证码
    public static final String VERIFYFORGET="zbmf.auth.verifyForget";
    //重置忘记密码
    public static final String RESTFORGET="zbmf.auth.resetForget";
    //手机忘记密码
    public static final String PWD_FORGET="zbmf.auth.pwdForget";
    //获取用户资金
    public static final String GET_WALLE="zbmf.wallet.getWalletInfo";
    //绑定手机
    public static final String bindPhone="zbmf.auth.bindPhone";
    public static final String bindName="zbmf.auth.bindName";

    /**
     * 我订阅的宝盒
     */
    public static final String getUserBoxs="zbmf.boxs.getUserBoxs";
    /**
     * 我的宝盒详情
     */
    public static final String getUserBoxInfo="zbmf.boxs.getUserBoxInfo";
    /**
     * 圈子宝盒列表
     */
    public static final String getGroupBoxs="zbmf.boxs.getGroupBoxs";
    /**
     * 圈子宝盒详情
     */
    public static final String getGroupBoxInfo="zbmf.boxs.getGroupBoxInfo";

    /**
     * 宝盒标签
     */
    public static final String getManageTags="zbmf.boxs.getManageTags";
    /**
     * 礼物列表
     */
    public static final String getGiftList="zbmf.gifts.getGifts";
    /**
     * 赠送礼物
     */
    public static final String SEND_GIFT="zbmf.gifts.sendGift";
    /**
     * 离开圈子
     */
    public static final String LEAVEGROUP="zbmf.im.leaveGroup";
    /**
     * 进入圈子
     */
    public static final String ENTERGROUP="zbmf.im.enterGroup";
    /**
     * 获取直播室消息记录
     */
    public static final String GETLIVEMSG="zbmf.im.getLiveMsg";
    /**
     * 发送聊天小组消息
     */
    public static final String sendToRoom="zbmf.im.sendToRoom";
    /**
     * 发送到铁粉小组
     */
    public static final String sendToFans="zbmf.im.sendToFans";
    /**
     * 获取聊天消息记录
     */
    public static final String getRoomMsg="zbmf.im.getRoomMsg";
    public static final String getFansMsg="zbmf.im.getFansMsg";
    /**
     * 抢红包
     */
    public static final String getRedPackged="zbmf.packet.receive";
    /**
     * 开红包
     */
    public static final String openRedPackged="zbmf.packet.open";
    /**
     * 获取红包详情
     */
    public static final String getRedMessage="zbmf.packet.list";
    /**
     * 发送红包
     */
    public static final String sendRed="zbmf.packet.create";

    //举报消息
    public static final String complaint="zbmf.im.complaint";

    //获取优惠券列表
    public static final String getGroupCoupons="zbmf.coupons.getGroupCoupons";

    //领取优惠券
    public static final String takeCoupon="zbmf.coupons.takeCoupon";
    public static final String takeAllCoupon="zbmf.coupons.takeAllCoupon";
    //我的已使用优惠券列表
    public static final String getHistCoupons="zbmf.coupons.getHistCoupons";
    //我的过期优惠券列表
    public static final String getExpireCoupons ="zbmf.coupons.getExpireCoupons";
    //用户可用优惠券
    public static final String getUserCoupons="zbmf.coupons.getUserCoupons";

    //关注
    public static final String follow="zbmf.groups.follow";

    //取消关注
    public static final String unfollow="zbmf.groups.unfollow";

    //圈子数据
    public static final String getGroupInfo="zbmf.groups.liveInfo";

    public static final String groupInfo="zbmf.groups.groupInfo";

    //铁粉专区
    public static final String fansInfo="zbmf.groups.fansInfo";

    //铁粉专区价格
    public static final String fansProduct="zbmf.groups.fansProduct";

    //铁粉优惠
    public static final String ruleCouponFans="zbmf.coupons.ruleFansCoupon";
    //按月加入铁粉
    public static final String subFans="zbmf.groups.subFans";

    //按天加入铁粉
    public static final String subGuest="zbmf.groups.subGuest";

    //直播历史纪录
    public static final String GET_HISTORY_MSG="zbmf.im.getHistoryMsg";

    //获取推荐圈子
    public static final String recommend="zbmf.groups.recommend";
    //人气圈子
    public static final String hot="zbmf.groups.hot";
    //贡献排行
    public static final String userVote="zbmf.groups.userVote";
    //投票排行
    public static final String vote="zbmf.groups.vote";
    //正在直播
    public static final String live="zbmf.groups.live";
    //独家圈子
    public static final String exclusive="zbmf.groups.exclusive";

    //系统博文列表/魔方头条
    public static final String searchUserBlogs="zbmf.blog.searchUserBlogs";

    //注释呢？
    public static final String userAsks="zbmf.asks.userAsks";
    //用户提问
    public static final String groupAnsweredAsks="zbmf.asks.groupAnsweredAsks";

    //用户博文列表
    public static final String getUserBlogs="zbmf.blog.getUserBlogs";

    //用户博文列表
    public static final String getRudimentsBlogs="zbmf.blog.getRudimentsBlogs";
    //用户博文评论
    public static final String getUserBlogPosts="zbmf.blog.getUserBlogPosts";

    //评论用户博文
    public static final String createUserBlogPost="zbmf.blog.createUserBlogPost";

    //首页广告
    public static final String getAdverts="zbmf.advert.getAdverts";
    /**
     * 我关注的圈子
     */
    public static final String userGroups="zbmf.groups.userGroups";
    /**
     * 积分明细
     */
    public static final String pointLogs="zbmf.wallet.pointLogs";
    /**
     * 魔方明细
     */
    public static final String payLogs="zbmf.wallet.payLogs";
    /**
     * 充值金额列表
     */
    public static final String products="zbmf.pay.products";
    /**
     * 微信支付
     */
    public static final String weixin_pay="zbmf.pay.weixin";
    //博文动态
    public static final String blog="zbmf.newsfeed.blog";
    /**
     * 提问
     */
    public static final String ask="zbmf.asks.ask";
    /**
     * 收藏博文
     */
    public static final String createBlogCollect="zbmf.blog.createCollectBlog";
    /**
     * 取消收藏博文
     */
    public static final String removeBlogCollect="zbmf.blog.removeCollectBlog";
    /**
     * 收藏博文列表
     */
    public static final String getUserCollects="zbmf.collect.getUserCollects";
    /**
     * 用户博文详情
     */
    public static final String getUserBlogInfo="zbmf.blog.getUserBlogInfo";
    /**
     * 搜索博文详情
     */
    public static final String searchUserBlogInfo="zbmf.blog.searchUserBlogInfo";
    /**
     * 直播状态
     */
    public static final String isOnline="zbmf.im.isOnline";
    /**
     * 签到
     */
    public static final String signIn="zbmf.sign.signIn";
    /**
     * 签到状态
     */
    public static final String userSigns="zbmf.sign.userSigns";
    //铁粉动态
    public static final String box="zbmf.newsfeed.box";
    //是否绑定手机
    public static final String isBind="zbmf.auth.isBind";
    //修改密码
    public static final String changePwd="zbmf.users.changePwd";
    //圈子数据统计
    public static final String GroupStat="zbmf.groups.GroupStat";
    //codeBind
    public static final String codeBind="zbmf.auth.codeBind";
    /**
     * 宝盒详情
     */
    public static final String getGroupBoxItems="zbmf.boxs.getGroupBoxItems";

    //获取用户详情
    public static final String userInfo="zbmf.auth.userInfo";

    /**视频相关**/
    public static final String GetsVideos="zbmf.video.GetsVideos";
    public static final String LoadVideo="zbmf.video.AndroidLoadVideo";
    public static final String Series="zbmf.video.Series";
    public static final String GetSeries="zbmf.video.GetSeries";
    public static final String GetVideoNews="zbmf.video.GetVideoNews";
    public static final String Recommend="zbmf.video.Recommend";
    public static final String GetFilter="zbmf.video.GetFilter";
    public static final String PayVideoNews="zbmf.video.PayVideoNews";
    public static final String PayVideo="zbmf.video.PayVideo";
    public static final String GetPayVideoLog="zbmf.video.GetPayVideoLog";
    public static final String GetTeachers="zbmf.video.GetTeachers";
    /**
     * 视频群聊
     */
    public static final String sendToVideo="zbmf.im.sendToVideo";
    public static final String getVideoMsg="zbmf.im.getVideoMsg";
    //邀请列表
    public static final String INVITELIST="zbmf.invite.list";
    public static final String SEARCH="zbmf.groups.search";

    //圈子首页活动视频推荐
    public static final String INDEX="zbmf.groups.index";
    //优惠铁粉
    public static final String COUPON="zbmf.groups.coupon";

    public static final String HOT_STOCKS_TAG="zbmf.asks.hotStocks";
    public static final String SEND_ASK_STOCK="zbmf.asks.askPro";
    public static final String ASK_LIST="zbmf.asks.askList";

    /**
     * 找股票相关
     */
    public static final String getScreenProducts="zbmf.screen.getScreenProducts";
    public static final String loadScreenProduct="zbmf.screen.loadScreenProduct";
    public static final String getNotices="zbmf.screen.getNotices";
    public static final String PayScreen="zbmf.screen.PayScreen";

    public static final String getAnnouncements="mcc.match.getAnnouncements";
    public static final String getPlayer="mcc.match.getPlayer";
    public static final String getHoldlist="mcc.match.getHoldlist";
    public static final String resetMatch="mcc.match.resetMatch";
    public static final String getWinRecords="mcc.match.getWinRecords";//获奖记录
    public static final String buy="mcc.match.buy";//买入
    public static final String getOrderList="mcc.match.getOrderList";//委托列表
    public static final String withdraw="mcc.match.withdraw";//委托撤单
    public static final String getDeallogList="mcc.match.getDeallogList";//交易记录
    public static final String dealSys="mcc.match.dealSys";//最新交易
    public static final String getYieldList="mcc.match.getMatchYieldList";//排行榜
    //替换比赛用户接口
    public static final String MATCH_NOTICE="zbmf.match.announcementList";//比赛公告列表
    public static final String MATCH_PLAYER="zbmf.match.player";
    public static final String HOLDER_LIST="zbmf.match.holdList";//持仓数据
    public static final String RESET_MATCH="zbmf.match.reset";//重置比赛
    public static final String RECORD="zbmf.match.recordList";//获奖记录
    public static final String STOCK_BUY="zbmf.match.buy";//买入股票
    public static final String TRUST_LIST="zbmf.match.orderList";//委托列表
    public static final String WITH_DRAW="zbmf.match.withdraw";//委托列表
    public static final String DEALS_RECORD="zbmf.match.dealList";//交易记录
    public static final String LASTLY_DEALS="zbmf.match.recentDeal";//最新交易
    public static final String RANK_LIST="zbmf.match.yieldList";//排行榜
    //VIP会员相关接口
    public static final String SUBSCRIBE_VIP="zbmf.vip.join";//开通vip会员
    public static final String VIP_PRICE="zbmf.vip.price";//开通vip的价格
    public static final String XF_VIP="zbmf.vip.renew";//续费VIP
    //话题相关
    public static final String TOPIC_LISTS="zbmf.topic.types";//获取话题列表
    public static final String GZ_TOPICS_LIST="zbmf.topic.my";//获取关注话题列表
    public static final String FL_TOPICS_LIST="zbmf.topic.list";//获取分类话题列表
    public static final String TOPIC_DETAIL="zbmf.topic.detail";//获取话题详情
    public static final String TOPIC_GD_LIST="zbmf.viewpoint.list";//获取话题观点列表
    public static final String GET_GD_DETAIL="zbmf.viewpoint.detail";//获取话题观点详情
    public static final String COMMENT_LIST="zbmf.viewpoint.commentlist";//评论列表接口
    public static final String FB_POINT="zbmf.viewpoint.create";//发表观点
    public static final String GZ_TOPIC="zbmf.topic.useradd";//关注话题
    public static final String GD_DZ="zbmf.viewpoint.hits";//观点点赞
    public static final String FB_COMMENT="zbmf.viewpoint.commentadd";//发表评论
    public static final String NO_READ_MSG="zbmf.topicmsg.list";//未读消息接口
    public static final String NO_READ_MSG_COUNT="zbmf.topicmsg.count";//未读消息数量
    public static final String UP_IMG="zbmf.upload.img";//上传图片
    public static final String DZ_USER_LIST="zbmf.viewpoint.hitusers";//点赞用户

    public static final String getsComment="mcc.match.getsComment";
    public static final String addComment="mcc.match.addComment";
    public static final String getStockRealtimeInfo="mcc.match.getStockRealtimeInfo";
    public static final String getRealtimeInfo="mcc.match.getRealtimeInfo";
    public static final String sell="mcc.match.sell";
    //选股大赛相关
    public static final String SELECT_STOCK_YIELD_LIST="zbmf.xgds.rounds";//选股大赛周期列表
    public static final String STOCK_WEEK_RANK_LIST="zbmf.xgds.ranks";//选股周排行榜
    public static final String USER_SELECT_STOCK_LIST="zbmf.xgds.user";//用户选股记录
    public static final String NEXT_SELECT_STOCK_DETAIL="zbmf.xgds.nextRound";//下次选股详情
    public static final String SUBMIT_STOCK="zbmf.xgds.select";//提交选股
    //董秘信息
    public static final String DONG_MI_INFO="zbmf.company.detail";

    //操盘高手
    public static final String traderRanks="mcc.traders.traderRanks";
    public static final String traderInfo=" mcc.traders.traderInfo";
    public static final String traders_buy=" mcc.traders.buy";
    public static final String TrackList=" mcc.traders.TrackList";
    public static final String traderHolds=" mcc.traders.traderHolds";
    public static final String traderDeals="zbmf.traders.traderDeals";
    //金股金句
    public static final String getDictumNums="zbmf.dictum.getDictumNums";
    public static final String getDictumByIds="zbmf.dictum.getDictumByIds";
    //模型选股
    public static final String modelProduct="zbmf.screen.modelProduct";
    public static final String modelList="zbmf.screen.modelList";
    //模型选股-股票列表（新增2天数据+自定义排序）
    public static final String newModelList="zbmf.screen.modelStock";

    public static final String STOCK_ASK_LIST="zbmf.ask.list";
    public static final String STOCK_ASK="zbmf.ask.ask";
    public static final String USER_LIST="zbmf.ask.userList";
    public static final String REMIND="zbmf.ask.remind";
    public static final String STOCK_LIST="zbmf.ask.stockList";
    public static final String tagList="zbmf.ask.tagList";
    public static final String addStock="zbmf.ask.addStock";
    public static final String addTag="zbmf.ask.addTag";
    public static final String delStock="zbmf.ask.delStock";
    public static final String delTag="zbmf.ask.delTag";
    public static final String noticeList="zbmf.ask.noticeList";
}

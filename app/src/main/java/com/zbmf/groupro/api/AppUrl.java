package com.zbmf.groupro.api;

/**
 * Created by xuhao on 2016/12/12.
 */

public class AppUrl {
    //普通版圈子pro  VqbBtMjZwLK1r2RJ57UQj4E2mZEgYZc   W8gN3fQi
    //普通版圈子 tlHmM7iap00solm1q9WsBUGTAfMm4Qm   secret:BrZ2g1TB
    public static final String API_KEY = "VqbBtMjZwLK1r2RJ57UQj4E2mZEgYZc";
    public static final String API_SECRET = "W8gN3fQi";
    public static final String DEVICE_TYPE="1";//设备标识
    public static final String REDIRECT_URL = "http://www.sina.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE ="email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog,"
                    + "invitation_write";
    // FIXME: 连接正式服务器
    public static final String ALL_URL_PREFIX = "https://passport.zbmf.com/rest/json/";
    public static final String ALL_URL="https://group.zbmf.com/rest/json/";
    public static final String Walle_URL="https://center.zbmf.com/rest/json/";

    public static final String SINA_MESSAGE="https://api.weibo.com/2/users/show.json";
    public static final String QQ_MESSAGE="https://graph.qq.com/user/get_user_info";
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
     * 获取聊天消息记录
     */
    public static final String getRoomMsg="zbmf.im.getRoomMsg";
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

    //我的过期优惠券列表
    public static final String getHistCoupons="zbmf.coupons.getHistCoupons";

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

    //按月加入铁粉
    public static final String subFans="zbmf.groups.subFans";

    //按天加入铁粉
    public static final String subGuest="zbmf.groups.subGuest";

    //直播历史纪录
    public static final String GET_HISTORY_MSG="zbmf.im.getHistoryMsg";

    //获取推荐圈子
    public static final String recommend="zbmf.groups.recommend";

    //系统博文列表/魔方头条
    public static final String searchUserBlogs="zbmf.blog.searchUserBlogs";

    //注释呢？
    public static final String userAsks="zbmf.asks.userAsks";
    //用户提问
    public static final String groupAnsweredAsks="zbmf.asks.groupAnsweredAsks";

    //用户博文列表
    public static final String getUserBlogs="zbmf.blog.getUserBlogs";

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
     * 股市直播
     */
    public static final String STOCK_LIVE="http://m.zbmf.com/apps/kzb/";
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
}

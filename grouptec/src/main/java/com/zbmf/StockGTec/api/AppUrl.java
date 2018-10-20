package com.zbmf.StockGTec.api;

/**
 * Created by xuhao on 2016/12/12.
 */

public class AppUrl {
    //普通版圈子newiph7bafaidke9557430523e00802a   inewph
    //新版本圈子NrrnMaIMYjKZosRNqMePRakcl3jj   Z7L646lj
//    public static final String API_KEY = "tlHmM7iap00solm1q9WsBUGTAfMm4Qm";
//    public static final String API_SECRET = "BrZ2g1TB";
    public static final String DEVICE_TYPE="1";//设备标识
    //圈主版本
    public static final String API_KEY = "dV55dVOKOhfdCoABNGzXaVUyAM7P9KF";
    public static final String API_SECRET = "gswxSbDG";

    // FIXME: 连接正式服务器
    public static final String ALL_URL_PREFIX = "https://passport.zbmf.com/rest/json/";
    public static final String ALL_URL="https://group.zbmf.com/rest/json/";
    public static final String Walle_URL="https://center.zbmf.com/rest/json/";
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
    public static final String URL_LOGINAUTH = "zbmf.group.login";
    //退出登陆
    public static final String LOGIN_OUT="zbmf.users.logout";
    //发送验证码
    public static final String POST_CODE="zbmf.auth.code";
    //注册账户
    public static final String REGISTER="zbmf.auth.register";
    //发送重置验证码
    public static final String CODE_FORGET="zbmf.auth.codeForget";
    //校验充值验证码
    public static final String VERIFYFORGET="zbmf.auth.verifyForget";
    //重置忘记密码
    public static final String RESTFORGET="zbmf.auth.resetForget";
    //手机忘记密码
    public static final String PWD_FORGET="zbmf.auth.pwdForget";
    //获取用户资金
    public static final String GET_WALLE="zbmf.wallet.getWalletInfo";
    //qq登陆，微博登陆
    public static final String getAccessTokenByOpenapi="zbmf.auth.getAccessTokenByOpenapi";


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
    public static final String getGroupBoxs="zbmf.boxs.getManageBoxs";
    /**
     * 圈子宝盒详情
     */
    public static final String getGroupBoxInfo="zbmf.boxs.getGroupBoxInfo";
    public static final String getManageBoxInfo="zbmf.boxs.getManageBoxInfo";
    public static final String getGroupBoxItems="zbmf.boxs.getGroupBoxItems";
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

    /**
     * 发送直播
     */
    public static final String sendToLive="zbmf.im.sendToLive";

    /**
     * 发送直播
     */
    public static final String uploadImg="zbmf.im.uploadImg";

    //举报消息
    public static final String complaint="zbmf.im.complaint";
    //禁言
    public static final String ban="zbmf.im.ban";
    //解除禁言
    public static final String unBan="zbmf.im.unBan";
    //删除消息
    public static final String deleteMsg="zbmf.im.deleteMsg";
    //获取公告
    public static final String notice="zbmf.groups.notice";
    //设置公告
    public static final String setNotice="zbmf.groups.setNotice";
    //获取圈子禁言列表
    public static final String banList="zbmf.im.banList";
    //获取即将到期铁粉
    public static final String expireFans="zbmf.groups.expireFans";
    //全部铁粉
    public static final String groupFans="zbmf.groups.groupFans";
    //宝盒下架
    public static final String stopManageBox="zbmf.boxs.stopManageBox";
    //宝盒置顶
    public static final String stickManageBox="zbmf.boxs.stickManageBox";
    //宝盒杀出
    public static final String removeManageBox="zbmf.boxs.removeManageBox";
    //宝盒刷新
    public static final String updatedManageBox="zbmf.boxs.updatedManageBox";
    //圈子未回答的提问
    public static final String groupAsks="zbmf.asks.groupAsks";
    //老师版提问列表
    public static final String groupAllAsks="zbmf.asks.groupAllAsks";
    //删除问题
    public static final String deleteAsks="zbmf.asks.deleteAsks";
    //回答
    public static final String post="zbmf.asks.post";
    //宝盒内容添加
    public static final String createManageBoxItem="zbmf.boxs.createManageBoxItem";
    //魔方宝明细
    public static final String payLogs="zbmf.wallet.payLogs";
    //用户博文评论
    public static final String getUserBlogPosts="zbmf.blog.getUserBlogPosts";

    /**
     * 搜索博文详情
     */
    public static final String searchUserBlogInfo="zbmf.blog.searchUserBlogInfo";

    public static final String getUserBlogs="zbmf.blog.getUserBlogs";

//    public static final String GetsVideos="zbmf.video.GetUserVideosceshi";
    public static final String GetsVideos="zbmf.video.GetsUserVideos";
    //邀请列表
    public static final String INVITELIST="zbmf.invite.list";

    public static final String getVideoMsg="zbmf.im.getVideoMsg";

    public static final String groupInfo="zbmf.groups.groupInfo";


}

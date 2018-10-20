package com.zbmf.StocksMatch.utils;

/**
 * Created by Administrator on 2015/12/31.
 */
public class Constants {
    //Action
    public static final String APPLY_SUCCESS= "apply_success";
    public static final String ACCOUNT_DEL= "account_del";
    public static final String FINISH= "finish";
    public static final String PAY_SUCCESS= "pay_success";

    //requestcode
    public static final int TAKEPHOTO = 0X001;
    public static final int PHOTO_REQUEST_CUT = 0x002;
    public static final int PhotoGallery = 0X003;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0X004;

    //px
    public static final int PIC_WIDTH = 800;// 图片宽度大于此参数，表示需要进行缩小
    public static final int PIC_SCALE = 640;// 图片缩小对比值

    public static final int ICO_WIDTH = 400;// 头像宽度大于此参数，表示需要进行缩小
    public static final int ICO_SCALE = 300;// 头像缩小对比值

    //From
    public static final String FROM_QUOTATION = "quotation";
    public static final String FROM_MATCH = "match";//

    public static final String WEI_APK_KEY="wx1569265af11bd90c";//webchat
    public static final String TencentSDKAppKey = "100886835";//tencent qq

    public static final String WBSDKAppKey = "2967409088";//sina appkey

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     *
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://www.7878.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String SINA_AT = "【资本魔方炒股大赛】这有属于我们自己的模拟比赛，快来参加吧！@资本魔方7878";
}

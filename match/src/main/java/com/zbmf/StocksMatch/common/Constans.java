package com.zbmf.StocksMatch.common;

/**
 * Created by xuhao
 * on 2017/11/28.
 */

public class Constans {
    public static final int PERPAGE=20;
    public static final String MATCH_ID="267";
    public static final int CITY=0x1;//城市
    public static final int SCHOOL=0x2;//高校
    public static final int BUSINESS=0x3;//券商
    public static final int NEW_MATCH=0x4;//最新比赛
    public static final int All_MATCH =0x5;    //全部比赛

    public static final int DAY_RANK=0x6;//日榜
    public static final int WEEK_RANK=0x7;//周榜
    public static final int MOUNTH_RANK=0x8;//月榜
    public static final int ALL_RANK=0x9;//总榜

    public static final int LASTLY_DEALS=0x10;//最新交易
    public static final int RANK_LIST=0x11;//排行榜
    public static final int HOLDER_SUPERIOR=0x12;//操盘高手

    public static final int NEW_DEAL_FLAG=0x13;//最新交易标记
    public static final int DAY_RANK_FLAG=0x14;//日榜标记
    public static final int WEEK_RANK_FLAG=0x15;//周榜标记
    public static final int MONTH_RANK_FLAG=0x16;//月榜标记
    public static final int ALL_RANK_FLAG=0x17;//月榜标记
    public static final int TRADER_RANK_FLAG=0x18;//操盘高手标记

    public static final int BUY_FLAG=0x19;
    public static final int SELL_FLAG=0x20;
    /*----------------微信分享标识---------------*/
    public static final int WX_SHARE_WEB=0X50;
    public static final int WX_SHARE_TEXT=0X51;
    public static final int WX_SHARE_IMG=0X52;
    public static final int WX_SHARE_MUSIC=0X53;
    public static final int WX_SHARE_VIDEO=0X54;
    /*------------------订阅标识--------------------*/
    public static final int TRADER_FLAG1=0x61;
    public static final int TRADER_FLAG2=0x62;
    public static final int TRADER_FLAG3=0x63;

    /*--------------屏幕分辨率-------------*/
    public static final int WIDTH_PIX_960=540;
    public static final int WIDTH_PIX_1280=720;
    public static final int WIDTH_PIX_1920=1080;
    public static final int WIDTH_PIX_2560=1440;
    public static final int WIDTH_PIX_854=600;
    public static final int WIDTH_PIX_800=480;

    /*----------延时跳转----------*/
    public static final int DELAY_TIME=30;

    //清除popWindow最小的时间间隔
    public static final int CLEAR_TIME_MIN=18;
    /*------------是否显示K线图------------*/
    public static final int NOT_SHOW_K_LINE_CHART=0;
    public static final int IS_SHOW_K_LINE_CHART=1;

    /*------------------category-------------------*/
    public static final String HOME_TOP_BANNER="20";
    public static final String LUNCH_ADS="21";
    public static final String HOME_SPONSOR_ADS="22";

    /*--------------------接口数据获取成功或失败的标数------------------*/
    public static final int GAIN_DATA_SUCCESS=0x11;
    public static final int GAIN_DATA_FAIL=0x12;

    /*-------------------存储popWindow弹窗的信息标记---------------*/
    public static final String USER_ID="user_id";
    public static final String POP_ID="pop_id";//全站的弹窗Id
    public static final String TIME_STAMP="time";//全站的弹窗时间
    public static final String INIT_TIME_STAMP="init_time";//个赛的弹窗时间

    public static final String CLIENT_ID="abxd1234";//默认的个推ID

    public static final String MATCH_AD_TYPE="app://match";//表示比赛类型的广告
    public static final String REDIRECT_URL = "http://www.7878.com";
    public static final String SHARE_URL = "http://m1.zbmf.com/match/2052/invite/?user_id=1788263";
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    public static final String SINA_AT = "【资本魔方炒股大赛】这有属于我们自己的模拟比赛，快来参加吧！@资本魔方7878";

    public static final String LIVE_IMG_350="-350_auto";
    /*---------------微信----------------*/

    public static final String WEI_APK_KEY="wx1569265af11bd90c";//webchat
    public static final String TencentSDKAppKey = "100886835";//tencent qq
    public static final String WBSDKAppKey = "2967409088";//sina appkey

//    public static final String WEI_APK_KEY = "wxf076e40b3d21cda2";
//    public static final String TencentSDKAppKey = "205998";//tencent qq
//    public static final String  WBSDKAppKey = "679452946";//sina appkey
    public static final String NEED_LOGIN = "用户登录失败或已过期";//tencent qq
    public static final String ACCOUNT_EXIT="2103";

    /*--------- CODE --------报名----------------*/
    public static final int APPLY_REQUEST_CODE=0x50;
    public static final int APPLY_RESPONSE_CODE=0x51;

     /*----------------添加股票评论--------------*/
     public static final int ADD_COMMENT_REQUEST=0x60;
    public static final int ADD_COMMENT_RESPONSE=0x61;
    public static final String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CAMERA",
            "android.permission.ACCESS_WIFI_STATE"};

    /*-------------------- Err ------------------*/
    public static final String INVALID_ERR_MSG="Login failed / Invalid auth token";
    public static final String INVALID_LOGIN_MSG="User not logged in / Insufficient permissions";
    public static final String INVALID_ERR_CODE ="1004";
    public static final String INVALID_LOGIN_CODE ="1005";
}

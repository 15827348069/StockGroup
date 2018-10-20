package com.zbmf.StockGTec.utils;


import com.zbmf.StockGTec.R;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Constants {
    public static final int TAKEPHOTO = 0X001;
    public static final int PHOTO_REQUEST_CUT = 0x002;
    public static final int PhotoGallery = 0X003;

    public static final int PIC_WIDTH = 1280;// 图片宽度大于此参数，表示需要进行缩小
    public static final int PIC_SCALE = 960;// 图片缩小对比值

    public static final String NET_ENVIR = "internet";

    public static final int PER_PAGE = 10;

    public static final int LIVE_MESSAGE_TYPE = 1;//直播消息
    public static final int CHAT_MESSAGE_TYPE = 2;//群聊消息

    public static final String LIVE_IMG_500 = "-500_auto";

    public static final String LIVE_IMG_350 = "-350_auto";

    public static final String LIVE_IMG_200 = "-200_auto";

    //	public static final String TencentSDKAppKey = "1105709876";//tencent qq
    public static final String TencentSDKAppKey = "1105747518";//tencent qq

    public static final String CC_USERID="B9641D861A8BF45E";
    public static final String CC_KEY="iVGLTXNTOciHKaXElrbdwOBSLjHNmP4T";
    public static final int CC_TIME=3*60*1000;

    public static final String WBSDKAppKey = "3522752864";//sina appkey

    public static String WEI_APK_KEY = "wxf076e40b3d21cda2";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://www.7878.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String SINA_AT = "在资本魔方分享了一只好股票，点击即可查看！@资本魔方";

    /**
     * 记录圈子唯一url
     */
    public static final String LOGIN_INFO = "login_info";

    private static HashMap<String, Integer> mEmojiIcon;

    public static HashMap<String, Integer> getEmojiIconMaps() {
        if (mEmojiIcon == null) {
            mEmojiIcon = new LinkedHashMap<String, Integer>();
            mEmojiIcon.put("[微笑]", R.drawable.face_001);
            mEmojiIcon.put("[撇嘴]", R.drawable.face_002);
            mEmojiIcon.put("[色]", R.drawable.face_003);
            mEmojiIcon.put("[发呆]", R.drawable.face_004);
            mEmojiIcon.put("[得意]", R.drawable.face_005);
            mEmojiIcon.put("[流泪]", R.drawable.face_006);
            mEmojiIcon.put("[害羞]", R.drawable.face_007);
            mEmojiIcon.put("[闭嘴]", R.drawable.face_008);
            mEmojiIcon.put("[睡]", R.drawable.face_009);
            mEmojiIcon.put("[大哭]", R.drawable.face_010);
            mEmojiIcon.put("[尴尬]", R.drawable.face_011);
            mEmojiIcon.put("[发怒]", R.drawable.face_012);
            mEmojiIcon.put("[调皮]", R.drawable.face_013);
            mEmojiIcon.put("[呲牙]", R.drawable.face_014);
            mEmojiIcon.put("[惊讶]", R.drawable.face_015);
            mEmojiIcon.put("[难过]", R.drawable.face_016);
            mEmojiIcon.put("[酷]", R.drawable.face_017);
            mEmojiIcon.put("[冷汗]", R.drawable.face_018);
            mEmojiIcon.put("[抓狂]", R.drawable.face_019);
            mEmojiIcon.put("[吐]", R.drawable.face_020);
            mEmojiIcon.put("[偷笑]", R.drawable.face_021);
            mEmojiIcon.put("[可爱]", R.drawable.face_022);
            mEmojiIcon.put("[白眼]", R.drawable.face_023);
            mEmojiIcon.put("[傲慢]", R.drawable.face_024);
            mEmojiIcon.put("[饥饿]", R.drawable.face_025);
            mEmojiIcon.put("[困]", R.drawable.face_026);
            mEmojiIcon.put("[惊恐]", R.drawable.face_027);
            mEmojiIcon.put("[流汗]", R.drawable.face_028);
            mEmojiIcon.put("[憨笑]", R.drawable.face_029);
            mEmojiIcon.put("[大兵]", R.drawable.face_030);
            mEmojiIcon.put("[奋斗]", R.drawable.face_031);
            mEmojiIcon.put("[咒骂]", R.drawable.face_032);
            mEmojiIcon.put("[疑问]", R.drawable.face_033);
            mEmojiIcon.put("[嘘]", R.drawable.face_034);
            mEmojiIcon.put("[晕]", R.drawable.face_035);
            mEmojiIcon.put("[折磨]", R.drawable.face_036);
            mEmojiIcon.put("[衰]", R.drawable.face_037);
            mEmojiIcon.put("[骷髅]", R.drawable.face_038);
            mEmojiIcon.put("[敲打]", R.drawable.face_039);
            mEmojiIcon.put("[再见]", R.drawable.face_040);
            mEmojiIcon.put("[擦汗]", R.drawable.face_041);
            mEmojiIcon.put("[抠鼻]", R.drawable.face_042);
            mEmojiIcon.put("[鼓掌]", R.drawable.face_043);
            mEmojiIcon.put("[糗大了]", R.drawable.face_044);
            mEmojiIcon.put("[坏笑]", R.drawable.face_045);
            mEmojiIcon.put("[左哼哼]", R.drawable.face_046);
            mEmojiIcon.put("[右哼哼]", R.drawable.face_047);
            mEmojiIcon.put("[哈欠]", R.drawable.face_048);
            mEmojiIcon.put("[鄙视]", R.drawable.face_049);
            mEmojiIcon.put("[委屈]", R.drawable.face_050);
            mEmojiIcon.put("[快哭了]", R.drawable.face_051);
            mEmojiIcon.put("[阴险]", R.drawable.face_052);
            mEmojiIcon.put("[亲亲]", R.drawable.face_053);
            mEmojiIcon.put("[吓]", R.drawable.face_054);
            mEmojiIcon.put("[可怜]", R.drawable.face_055);
            mEmojiIcon.put("[菜刀]", R.drawable.face_056);
            mEmojiIcon.put("[西瓜]", R.drawable.face_057);
            mEmojiIcon.put("[啤酒]", R.drawable.face_058);
            mEmojiIcon.put("[篮球]", R.drawable.face_059);
            mEmojiIcon.put("[乒乓]", R.drawable.face_060);
            mEmojiIcon.put("[咖啡]", R.drawable.face_061);
            mEmojiIcon.put("[饭]", R.drawable.face_062);
            mEmojiIcon.put("[猪头]", R.drawable.face_063);
            mEmojiIcon.put("[玫瑰]", R.drawable.face_064);
            mEmojiIcon.put("[凋谢]", R.drawable.face_065);
            mEmojiIcon.put("[示爱]", R.drawable.face_066);
            mEmojiIcon.put("[爱心]", R.drawable.face_067);
            mEmojiIcon.put("[心碎]", R.drawable.face_068);
            mEmojiIcon.put("[蛋糕]", R.drawable.face_069);
            mEmojiIcon.put("[闪电]", R.drawable.face_070);
            mEmojiIcon.put("[炸弹]", R.drawable.face_071);
            mEmojiIcon.put("[刀]", R.drawable.face_072);
            mEmojiIcon.put("[足球]", R.drawable.face_073);
            mEmojiIcon.put("[瓢虫]", R.drawable.face_074);
            mEmojiIcon.put("[便便]", R.drawable.face_075);
            mEmojiIcon.put("[月亮]", R.drawable.face_076);
            mEmojiIcon.put("[太阳]", R.drawable.face_077);
            mEmojiIcon.put("[礼物]", R.drawable.face_078);
            mEmojiIcon.put("[拥抱]", R.drawable.face_079);
            mEmojiIcon.put("[强]", R.drawable.face_080);
            mEmojiIcon.put("[弱]", R.drawable.face_081);
            mEmojiIcon.put("[握手]", R.drawable.face_082);
            mEmojiIcon.put("[胜利]", R.drawable.face_083);
            mEmojiIcon.put("[抱拳]", R.drawable.face_084);
            mEmojiIcon.put("[勾引]", R.drawable.face_085);
            mEmojiIcon.put("[拳头]", R.drawable.face_086);
            mEmojiIcon.put("[差劲]", R.drawable.face_087);
            mEmojiIcon.put("[爱你]", R.drawable.face_088);
            mEmojiIcon.put("[NO]", R.drawable.face_089);
            mEmojiIcon.put("[OK]", R.drawable.face_090);
            mEmojiIcon.put("[爱情]", R.drawable.face_091);
            mEmojiIcon.put("[飞吻]", R.drawable.face_092);
            mEmojiIcon.put("[跳跳]", R.drawable.face_093);
            mEmojiIcon.put("[发抖]", R.drawable.face_094);
            mEmojiIcon.put("[怄火]", R.drawable.face_095);
            mEmojiIcon.put("[转圈]", R.drawable.face_096);
            mEmojiIcon.put("[磕头]", R.drawable.face_097);
            mEmojiIcon.put("[回头]", R.drawable.face_098);
            mEmojiIcon.put("[跳绳]", R.drawable.face_099);
            mEmojiIcon.put("[挥手]", R.drawable.face_100);
            mEmojiIcon.put("[激动]", R.drawable.face_101);
            mEmojiIcon.put("[街舞]", R.drawable.face_102);
            mEmojiIcon.put("[献吻]", R.drawable.face_103);
            mEmojiIcon.put("[左太极]", R.drawable.face_104);
            mEmojiIcon.put("[右太极]", R.drawable.face_105);
            mEmojiIcon.put("[双喜]", R.drawable.face_106);
            mEmojiIcon.put("[鞭炮]", R.drawable.face_107);
            mEmojiIcon.put("[灯笼]", R.drawable.face_108);
            mEmojiIcon.put("[发财]", R.drawable.face_109);
            mEmojiIcon.put("[K歌]", R.drawable.face_110);
            mEmojiIcon.put("[购物]", R.drawable.face_111);
            mEmojiIcon.put("[邮件]", R.drawable.face_112);
            mEmojiIcon.put("[帅]", R.drawable.face_113);
            mEmojiIcon.put("[喝彩]", R.drawable.face_114);
            mEmojiIcon.put("[祈祷]", R.drawable.face_115);
            mEmojiIcon.put("[爆筋]", R.drawable.face_116);
            mEmojiIcon.put("[棒棒糖]", R.drawable.face_117);
            mEmojiIcon.put("[喝奶]", R.drawable.face_118);
            mEmojiIcon.put("[下面]", R.drawable.face_119);
            mEmojiIcon.put("[香蕉]", R.drawable.face_120);
            mEmojiIcon.put("[飞机]", R.drawable.face_121);
            mEmojiIcon.put("[开车]", R.drawable.face_122);
            mEmojiIcon.put("[高铁左车头]", R.drawable.face_123);
            mEmojiIcon.put("[车厢]", R.drawable.face_124);
            mEmojiIcon.put("[高铁右车头]", R.drawable.face_125);
            mEmojiIcon.put("[多云]", R.drawable.face_126);
            mEmojiIcon.put("[下雨]", R.drawable.face_127);
            mEmojiIcon.put("[钞票]", R.drawable.face_128);
            mEmojiIcon.put("[熊猫]", R.drawable.face_129);
            mEmojiIcon.put("[灯泡]", R.drawable.face_130);
            mEmojiIcon.put("[风车]", R.drawable.face_131);
            mEmojiIcon.put("[闹钟]", R.drawable.face_132);
            mEmojiIcon.put("[打伞]", R.drawable.face_133);
            mEmojiIcon.put("[彩球]", R.drawable.face_134);
            mEmojiIcon.put("[钻戒]", R.drawable.face_135);
            mEmojiIcon.put("[沙发]", R.drawable.face_136);
            mEmojiIcon.put("[纸巾]", R.drawable.face_137);
            mEmojiIcon.put("[药]", R.drawable.face_138);
            mEmojiIcon.put("[手枪]", R.drawable.face_139);
            mEmojiIcon.put("[青蛙]", R.drawable.face_140);
            mEmojiIcon.put("[招财猫]", R.drawable.face_141);
        }
        return mEmojiIcon;
    }
}

package com.zbmf.StockGTec.utils;

/**
 * Created by iMac on 2016/12/27.
 */

public class MessageType {
    public static final String TXT = "txt";
    public static final String IMG = "img";
    public static final String AUDIO = "audio";
    public static final String CHAT="chat";
    public static final String GIFT="gift";
    public static final String FANS="fans";
    public static final String SYSTEM="system";
    public static final String BOX="box";
    public static final String RED_PACKET="packet";
    public static final String BLOG="blog";
    public static final String MEMBER="member";//加入铁粉提醒
//    public static final int MESSAGE=0;
//    public static final int FANS_MESSAGE=2;
//    public static final int SYSTEM_MESSAGE=3;
//    public static final int BOX_MESSAGE=4;
    /**
     * 圈子角色
     */
//    -1	未登录或未关注圈子的用户
//    0	普通关注用户
//    5	试用
//    10	铁粉
//    20	年粉
//    50	管理员
//    100 圈主
    public static final int ROLE_1 = -1;
    public static final int ROLE_0 = 0;
    public static final int ROLE_5 = 5;
    public static final int ROLE_10 = 10;
    public static final int ROLE_20 = 20;
    public static final int ROLE_50 = 50;
    public static final int ROLE_100 = 100;

    public static final String CHAT_GROUP = "group";
    public static final String CHAT_USER = "user";

    public static final int UPLOADING = 0;//正在上传
    public static final int UPLOAD_SUCCESS = 1;//上传成功
    public static final int UPLOAD_FAIL = 2;//上传失败
    public static final int DOWN_SUCCESS = 3;//下载成功
    public static final int DOWN_FAIL = 4;//下载失败

    public static final String ROOM_GROUP = "2";//小组群聊
    public static final String ROOM_CHAT = "1";//聊天室
    public static final String ROOM_FANS = "3";//铁粉小组

}

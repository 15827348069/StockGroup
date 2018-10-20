package com.zbmf.StockGTec.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.zbmf.StockGTec.R;

/**
 * Created by xuhao on 2016/12/14.
 */

public class SettingDefaultsManager {
    private static final String TAG = "UserDefaultsManager";
    private static SettingDefaultsManager defaultInstance = null;
    private static SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;

    public static final String CLIENT_ID = "client_id";
    // 用户authtoken
    public static final String USER_AUTHTOKEN = "User_AuthToken";
    //用户名
    public static final String USER_NICK_NAME = "User_Nick_name";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_IMG = "group_img";
    //用户头像
    public static final String USER_AVATAR = "User_Avatar";
    //用户ID
    public static final String USER_ID = "User_Id";
    public static final String GROUP_ID = "group_id";
    //用户设置字体大小
    public static final String TEXT_SIZE = "Text_Size";
    //设置用户声音是否提示
    public static final String MESSAGE_VEDIO = "message_vedio_";
    //设置用户群聊声音是否提示
    public static final String CHAT_MESSAGE_VEDIO = "chat_message_vedio_";
    //设置用户可用魔方宝
    public static final String PAYS = "pays";
    //设置用户可用积分
    public static final String POINT = "point";

    public static final String LIVE_IMG = "live_img";
    //用户手机
    public static final String USER_PHONE = "user_phone";
    //以圈主名义
    public static final String IS_GROUP_MANAGER = "is_groups";
    public static final String IS_GROUP_CHAT_MANAGER = "is_group_chat_manager";

    // 设置当前用户的USER_PHONE
    public void setUserPhone(String USER_PHONE) {
        editor.putString(USER_PHONE, USER_PHONE);
        editor.commit();
    }

    // 获得当前用户的phone
    public String getUserPhone() {
        return sharedPreferences.getString(USER_PHONE, "");
    }

    public static SettingDefaultsManager getInstance() {
        if (defaultInstance == null) {
            defaultInstance = new SettingDefaultsManager();
        }
        return defaultInstance;
    }

    //设置直播室图片宽度
    public void setLiveImg(String liveImg) {
        editor.putString(LIVE_IMG, liveImg);
        editor.commit();
    }

    public String getLiveImg() {
        return sharedPreferences.getString(LIVE_IMG, Constants.LIVE_IMG_350);
    }

    //存储为Group_UserInformation的文件中
    public void setSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName() + "Group_SettingDefaultsManager", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // 设置当前用户的authtoken
    public void setAuthtoken(String authToken) {
        editor.putString(USER_AUTHTOKEN, authToken);
        editor.commit();
    }

    // 获得当前用户的authtoken
    public String authToken() {
        //return "d02f7b60b65a27f4224197e56d8bdb75";
        return sharedPreferences.getString(USER_AUTHTOKEN, null);
    }

    //设置用户名
    public void setNickName(String userName) {
        editor.putString(USER_NICK_NAME, userName);
        editor.commit();
    }

    public String NickName() {
        return sharedPreferences.getString(USER_NICK_NAME, null);
    }

    //圈子名
    public void setGroupName(String groupName) {
        editor.putString(GROUP_NAME, groupName);
        editor.commit();
    }

    public String getGroupName() {
        return sharedPreferences.getString(GROUP_NAME, "");
    }

    //圈子名
    public void setGroupImg(String groupImg) {
        editor.putString(GROUP_IMG, groupImg);
        editor.commit();
    }

    public String getGroupImg() {
        return sharedPreferences.getString(GROUP_IMG, "");
    }

    //设置用户头像
    public void setUserAvatar(String useravatar) {
        editor.putString(USER_AVATAR, useravatar);
        editor.commit();
    }

    public String UserAvatar() {
        return sharedPreferences.getString(USER_AVATAR, null);
    }

    //设置用户ID
    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public String UserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    //设置当前圈子
    public void setGroupId(String groupId) {
        editor.putString(GROUP_ID, groupId);
        editor.commit();
    }

    public String getGroupId() {
        return sharedPreferences.getString(GROUP_ID, "");
    }


    //设置直播字体大小
    public void setTextSize(int textSize) {
        editor.putInt(TEXT_SIZE, textSize);
        editor.commit();
    }

    //用户设置字体大小
    public int getTextSize() {
        return sharedPreferences.getInt(TEXT_SIZE, R.dimen.live_text_size_small);
    }

    //设置client_id
    public void setClientId(String clientId) {
        Log.e("TAG", "设置clientid" + clientId);
        editor.putString(CLIENT_ID, clientId);
        editor.commit();
    }

    //获取client_id
    public String getClientId() {
        return sharedPreferences.getString(CLIENT_ID, null);
    }

    /**
     * 设置消息是否提示声音
     *
     * @param group_id
     * @param vedio
     */
    public void setNewMessageVedio(String group_id, boolean vedio) {
        editor.putBoolean(MESSAGE_VEDIO + group_id, vedio);
        editor.commit();
    }

    /**
     * 获取圈子是否提示声音
     *
     * @param group_id
     * @return
     */
    public boolean getNewMessageVedio(String group_id) {
        return sharedPreferences.getBoolean(MESSAGE_VEDIO + group_id, false);
    }

    /**
     * 设置消息是否提示声音
     *
     * @param group_id
     * @param vedio
     */
    public void setNewChatMessageVedio(String group_id, boolean vedio) {
        editor.putBoolean(CHAT_MESSAGE_VEDIO + group_id, vedio);
        editor.commit();
    }

    /**
     * 获取圈子是否提示声音
     *
     * @param group_id
     * @return
     */
    public boolean getNewChatMessageVedio(String group_id) {
        return sharedPreferences.getBoolean(CHAT_MESSAGE_VEDIO + group_id, false);
    }

    /**
     * 设置可用魔方宝
     *
     * @param pays
     */
    public void setPays(String pays) {
        editor.putString(PAYS + UserId(), pays);
        editor.commit();
    }

    /**
     * 获取可用魔方宝
     *
     * @return
     */
    public String getPays() {
        return sharedPreferences.getString(PAYS + UserId(), "0.00");
    }

    /**
     * 设置可用积分
     *
     * @param pays
     */
    public void setPoint(long pays) {
        editor.putLong(POINT + UserId(), pays);
        editor.commit();
    }

    /**
     * 获取可用积分
     *
     * @return
     */
    public long getPoint() {
        return sharedPreferences.getLong(POINT + UserId(), 0);
    }

    public static final String PUSH_CILENT_ID = "push_client_id";

    // 获得当前用户的authtoken
    public String PUSH_CILENT_ID() {
        //return "d02f7b60b65a27f4224197e56d8bdb75";
        return sharedPreferences.getString(PUSH_CILENT_ID, "");
    }

    // 设置当前用户的authtoken
    public void setPushCilentId(String authToken) {
        editor.putString(PUSH_CILENT_ID, authToken);
        editor.commit();
    }


    //用户设置字体大小
    public static final String BLOG_TEXT_SIZE = "Blog_Text_Size";

    //设置博文字体大小
    public void setBlogTextSize(int textsize) {
        editor.putInt(BLOG_TEXT_SIZE, textsize);
        editor.commit();
    }

    //用户设置博文字体大小
    public int getBlogTextSize() {
        return sharedPreferences.getInt(BLOG_TEXT_SIZE, 2);
    }

    //当前聊天
    public static final String CURRENT_CHAT = "current_chat";

    public void setCurrentChat(String currentChat) {
        editor.putString(CURRENT_CHAT, currentChat);
        editor.commit();
    }

    public String getCurrentChat() {
        return sharedPreferences.getString(CURRENT_CHAT, "");
    }

    public static final String GROUP_CONTENT = "group_content";

    public void setGroupContent(String group_content) {
        editor.putString(GROUP_CONTENT, group_content);
        editor.commit();
    }

    public String getGroupContent() {
        return sharedPreferences.getString(GROUP_CONTENT, "");
    }

    public void setIsGroupManager(String groupId, boolean isGroupManager) {
        editor.putBoolean(IS_GROUP_MANAGER + groupId, isGroupManager).commit();
    }

    public boolean isGroupManager(String groupId) {
        return sharedPreferences.getBoolean(IS_GROUP_MANAGER + groupId, false);
    }

    public void setIsChatManager(boolean isChatManager) {
        editor.putBoolean(IS_GROUP_CHAT_MANAGER, isChatManager).commit();
    }

    public boolean isGroupChatManager() {
        return sharedPreferences.getBoolean(IS_GROUP_CHAT_MANAGER, false);
    }

    public void setGroupManager(boolean isGroup) {
        editor.putBoolean("groupManager", isGroup).commit();
    }

    public boolean isGroupManager() {
        return sharedPreferences.getBoolean("groupManager", false);
    }

    public void setManager(boolean isManager) {
        editor.putBoolean("managerGroup", isManager);
    }

    public boolean isManager() {
        return sharedPreferences.getBoolean("managerGroup", false);
    }

    public void setSayFans(boolean sayFans) {
        editor.putBoolean("sayFans", sayFans);
    }

    public boolean isSayFans() {
        return sharedPreferences.getBoolean("sayFans", false);
    }

}

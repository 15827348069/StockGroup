package com.zbmf.StockGroup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.TagBean;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2016/12/14.
 */

public class SettingDefaultsManager {
    private static final String TAG="UserDefaultsManager";
    private static SettingDefaultsManager defaultInstance=null;
    private static SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor=null;

    public static final String CLIENT_ID="client_id";
    // 用户authtoken
    public static final String USER_AUTHTOKEN = "User_AuthToken";
    //用户名
    public static final String USER_NICK_NAME="User_Nick_name";
    //vip
    public static final String VIP="vip";
    //superVip
    public static final String SUPER_VIP="super_vip";
    //vip到期时间
    public static final String VIP_AT_END="vip_at_end";
    //真实名字
    public static final String TRUE_NAME="true_name";
    //idcard
    public static final String IDCARD="idcard";
    //用户头像
    public static final String USER_AVATAR="User_Avatar";
    //用户ID
    public static final String USER_ID="User_Id";
    //用户手机
    public static final String USER_PHONE="user_phone";
    //用户设置字体大小
    public static final String TEXT_SIZE="Text_Size";
    //用户设置字体大小
    public static final String  BLOG_TEXT_SIZE="Blog_Text_Size";
    //设置用户声音是否提示
    public static final String MESSAGE_VEDIO="message_vedio_";
    //设置用户群聊声音是否提示
    public static final String CHAT_MESSAGE_VEDIO="chat_message_vedio_";
    //设置用户可用魔方宝
    public static final String PAYS="pays";
    //设置用户可用积分
    public static final String POINT="point";
    //设置用户可用优惠券
    public static final String COUPON="coupon";

    public static final String LIVE_IMG ="live_img";

    //是否显示铁粉
    public static final String SHOW_FANS="show_fans";

    //设置用户声音是否提示
    public static final String MESSAGE_All="message_all";

    //是否显示K线图
    public static final String IS_SHOW_KLINE_CHART="show_k_line_chart";

    public static  final String PUSH_CILENT_ID="push_client_id";
    public static  final String CURRENT_CHAT="current_chat";
    public static final String SEARCH_HISTORY="search_history;";
    public static final String UPDATA_LIVE="updata_live";

    public static final String STOCK_MODE_DESC="stock_mode_desc";
    public void clearUserInfo(){
        editor.remove(USER_AUTHTOKEN);
        editor.remove(USER_ID);
        editor.remove(USER_PHONE);
        editor.remove(USER_AVATAR);
        editor.remove(USER_NICK_NAME);
        editor.remove(TRUE_NAME);
        editor.remove(IDCARD);
        editor.remove(POINT);
        editor.remove(PAYS);
        editor.remove(COUPON);
        editor.commit();
    }

    public static SettingDefaultsManager getInstance()
    {
        if (defaultInstance == null)
        {
            defaultInstance = new SettingDefaultsManager();
        }
        return defaultInstance;
    }
    //存储是否显示当前铁粉
    public void setIsShowFans(int isShowFans){
        editor.putInt(SHOW_FANS,isShowFans);
        editor.commit();
    }
    public boolean getIsShowFans(){
        return sharedPreferences.getInt(SHOW_FANS,1)==1;// 0非紧急 显示,1紧急 隐藏
    }
    //存储为Group_UserInformation的文件中
    public void setSharedPreferences(Context context)
    {
        sharedPreferences = context.getSharedPreferences(context.getPackageName()+"Group_SettingDefaultsManager", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    //设置直播室图片宽度
    public void setLiveImg(String liveImg){
        editor.putString(LIVE_IMG, liveImg);
        editor.commit();
    }
    public String getLiveImg(){
        return  sharedPreferences.getString(LIVE_IMG, Constants.LIVE_IMG_350);
    }
    // 设置当前用户的USER_PHONE
    public void setUserPhone(String userPhone)
    {
        editor.putString(USER_PHONE, userPhone);
        editor.commit();
    }

    // 获得当前用户的phone
    public String getUserPhone()
    {
        return sharedPreferences.getString(USER_PHONE,"");
    }
    // 设置当前用户的PUSH_CILENT_ID
    public void setPushCilentId(String pushClientID)
    {
        editor.putString(PUSH_CILENT_ID, pushClientID);
        editor.commit();
    }
    public String getCoupon()
    {
        return sharedPreferences.getString(COUPON,"0");
    }
    public void setCoupon(String coupon)
    {
        editor.putString(COUPON, coupon);
        editor.commit();
    }
    public void setStockModeDesc(boolean isFirst){
        editor.putBoolean(STOCK_MODE_DESC,isFirst);
        editor.commit();
    }
    public boolean getStockModeDesc(){
        return sharedPreferences.getBoolean(STOCK_MODE_DESC,false);
    }
    public String PUSH_CILENT_ID()
    {
        return sharedPreferences.getString(PUSH_CILENT_ID,"");
    }
    public void setAuthtoken(String authToken)
    {
        editor.putString(USER_AUTHTOKEN, authToken);
        editor.commit();
    }

    // 获得当前用户的authtoken
    public String authToken()
    {
        return sharedPreferences.getString(USER_AUTHTOKEN,"");
    }
    //设置用户名
    public void setNickName(String userName){
        editor.putString(USER_NICK_NAME, userName);
        editor.commit();
    }
    public void setIsVip(int vip){
        editor.putInt(VIP,vip);
        editor.commit();
    }
    public void setSuperVip(int superVip){
        editor.putInt(SUPER_VIP,superVip);
        editor.commit();
    }
    public void setVipAtEnd(String vipAtEnd){
        editor.putString(VIP_AT_END,vipAtEnd);
        editor.commit();
    }
    public void setTrueName(String trueName){
        editor.putString(TRUE_NAME, trueName);
        editor.commit();
    }
    public void setIsShowKlineChart(int kChart){
        editor.putInt(IS_SHOW_KLINE_CHART,kChart);
        editor.commit();
    }
    public void setIdcard(String idcard){
        editor.putString(IDCARD, idcard);
        editor.commit();
    }
    public int getIsShowKLineChart(){return sharedPreferences.getInt(IS_SHOW_KLINE_CHART,Constants.IS_SHOW_K_LINE_CHART);}
    public int getIsVip(){return sharedPreferences.getInt(VIP,-1);}
    public int getIsSuperVip(){return sharedPreferences.getInt(SUPER_VIP,-1);}
    public String getVipAtEnd(){return sharedPreferences.getString(VIP_AT_END,"");}
    public String getIdcard(){return sharedPreferences.getString(IDCARD,null);}
    public String getTrueName(){
        return sharedPreferences.getString(TRUE_NAME,null);
    }
    public String NickName(){
        return  sharedPreferences.getString(USER_NICK_NAME,null);
    }
    //设置用户头像
    public void setUserAvatar(String useravatar){
        editor.putString(USER_AVATAR, useravatar);
        editor.commit();
    }
    public String UserAvatar(){
        return  sharedPreferences.getString(USER_AVATAR,null);
    }
    //设置用户ID
    public void setUserId(String userId){
        editor.putString(USER_ID, userId);
        editor.commit();
    }
    public String UserId(){
        return  sharedPreferences.getString(USER_ID,"");
    }
    //设置博文字体大小
    public void setBlogTextSize(int textsize)
    {
        editor.putInt(BLOG_TEXT_SIZE, textsize);
        editor.commit();
    }
    //用户设置博文字体大小
    public int getBlogTextSize(){
        return sharedPreferences.getInt(BLOG_TEXT_SIZE,2);
    }
     //设置直播字体大小
    public void setTextSize(int textSize)
    {
        editor.putInt(TEXT_SIZE, textSize);
        editor.commit();
    }
    //用户设置字体大小
    public int getTextSize(){
        return sharedPreferences.getInt(TEXT_SIZE, R.dimen.live_text_size_small);
    }
    //设置client_id
    public void setClientId(String clientId)
    {
        editor.putString(CLIENT_ID, clientId);
        editor.commit();
    }
    //获取client_id
    public String getClientId(){
        return sharedPreferences.getString(CLIENT_ID,null);
    }

    /**
     * 设置消息是否提示声音
     * @param group_id
     * @param vedio
     */
    public void setNewMessageVedio(String group_id,boolean vedio){
        editor.putBoolean(MESSAGE_VEDIO+group_id,vedio);
        editor.commit();
    }

    /**
     * 获取圈子是否提示声音
     * @param group_id
     * @return
     */
    public boolean getNewMessageVedio(String group_id){
        return sharedPreferences.getBoolean(MESSAGE_VEDIO+group_id,false);
    }

    //设置全局消息
    public void setMessageAll(boolean vedio){
        editor.putBoolean(MESSAGE_All,vedio);
        editor.commit();
    }

    //全局消息获取
    public boolean getMessageAll(){
        return sharedPreferences.getBoolean(MESSAGE_All,false);
    }
    /**
     * 设置消息是否提示声音
     * @param group_id
     * @param vedio
     */
    public void setNewChatMessageVedio(String group_id,boolean vedio){
        editor.putBoolean(CHAT_MESSAGE_VEDIO+group_id,vedio);
        editor.commit();
    }

    /**
     * 获取圈子是否提示声音
     * @param group_id
     * @return
     */
    public boolean getNewChatMessageVedio(String group_id){
        return sharedPreferences.getBoolean(CHAT_MESSAGE_VEDIO+group_id,false);
    }

    /**
     * 设置可用魔方宝
     * @param pays
     */
    public void setPays(String pays){
        editor.putString(PAYS+UserId(),pays);
        editor.commit();
    }

    /**
     * 获取可用魔方宝
     * @return
     */
    public String getPays(){
        return sharedPreferences.getString((PAYS+UserId()),"0.00");
    }
    /**
     * 设置可用积分
     * @param pays
     */
    public void setPoint(long pays){
        editor.putLong(POINT+UserId(),pays);
        editor.commit();
    }

    /**
     * 获取可用积分
     * @return
     */
    public long getPoint(){
        return sharedPreferences.getLong(POINT+UserId(),0);
    }


    public void setCurrentChat(String currentChat) {
        editor.putString(CURRENT_CHAT, currentChat);
        editor.commit();
    }

    public String getCurrentChat() {
        return sharedPreferences.getString(CURRENT_CHAT,"");
    }
    public void setUpdataLive(long currentChat,String groupId) {
        editor.putLong(UPDATA_LIVE+UserId()+groupId, currentChat);
        editor.commit();
    }

    public long getUpdataLive(String groupId) {
        return sharedPreferences.getLong(UPDATA_LIVE+UserId()+groupId,0);
    }
    public void getSearchHistory(ResultCallback callback){
        String result = sharedPreferences.getString(SEARCH_HISTORY,"");
        List<TagBean.ChildrenTag> tags=new ArrayList<>();
        try {
            JSONArray array ;
            if(!result.equals("")){
                array = new JSONArray(result);
            }else{
                array=new JSONArray();
            }
            for(int i=array.length()-1;i>=0;i--){
                JSONObject object = array.getJSONObject(i);
                tags.add(new TagBean.ChildrenTag(object.optString("name"),object.optString("id"),object.optInt("hot")));
            }
            if(tags.size()>0){
                callback.onSuccess(tags);
            }else{
                LogUtil.e("搜索历史为空");
                callback.onFail("搜索历史为空");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e("取出异常"+e.getMessage());
            editor.putString(SEARCH_HISTORY,"");
            editor.commit();
            callback.onFail(e.getMessage());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setSearchHistory(TagBean.ChildrenTag tag){
        String result = sharedPreferences.getString(SEARCH_HISTORY,"");
        if(!result.contains(tag.getName())){
            try {
                JSONObject object=new JSONObject();
                object.put("name",tag.getName());
                object.put("id",tag.getId());
                object.put("hot",tag.getIs_hot());
                JSONArray jsonArray=null;
                if(!result.equals("")){
                    jsonArray =new JSONArray(result);
                    if(jsonArray.length()==5){
                        jsonArray.remove(0);
                    }
                }else{
                    jsonArray=new JSONArray();
                }
                jsonArray.put(object);
                editor.putString(SEARCH_HISTORY,jsonArray.toString());
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void clearSearchHistory(){
        editor.remove(SEARCH_HISTORY);
        editor.commit();
    }
}

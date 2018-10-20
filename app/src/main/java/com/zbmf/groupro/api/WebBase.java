package com.zbmf.groupro.api;


import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.SettingDefaultsManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WebBase {
    private static final String BASE_URL = AppUrl.ALL_URL_PREFIX;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static AsyncHttpClient center_client = new AsyncHttpClient();
    private static AsyncHttpClient passport_client = new AsyncHttpClient();

    static {
        client.setTimeout(8000);
        client.setMaxConnections(20);
        center_client.setTimeout(8000);
        center_client.setMaxConnections(20);
        passport_client.setTimeout(8000);
        passport_client.setMaxConnections(20);
    }

    /****
     * MD5加密
     *
     * @param string
     * @return
     */
    public static String getMD5String(String string) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(string.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e) {

            System.out.println("NoSuchAlgorithmException caught!");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 最后组装数据
     *
     * @param
     * @return
     */
    public static RequestParams getRequest(String method, Map<String, String> map) {
        String api_sig = AppUrl.API_SECRET;
        if (map == null) {
            map = new HashMap<>();
        }
        if (method != null) {
            map.put("method", method);
        }
        map.put("api_key", AppUrl.API_KEY);
        map.put("device_type", AppUrl.DEVICE_TYPE);
        map = sortMapByKey(map);
        RequestParams params = new RequestParams();
        for (String key : map.keySet()) {
            api_sig += key + map.get(key);
        }
        map.put("api_sig", getMD5String(api_sig).toLowerCase());
        for (String key : map.keySet()) {
            params.put(key, map.get(key));
        }
        return params;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 登陆域名：https://passport.zbmf.com/rest/json/
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void login_get(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        passport_client.get(BASE_URL, getRequest(url, params), responseHandler);
    }

    public static void login_post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        passport_client.post(BASE_URL, getRequest(url, params), responseHandler);
    }

    /**
     * 普通域名：https://group.zbmf.com/rest/json/
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        Log.e("group_get>>>", AppUrl.ALL_URL + "?" + getRequest(url, params));
        client.get(AppUrl.ALL_URL, getRequest(url, params), responseHandler);
    }

    public static void post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        Log.e("group_post>>>", AppUrl.ALL_URL + "?" + getRequest(url, params));
        client.post(AppUrl.ALL_URL, getRequest(url, params), responseHandler);
    }

    /**
     * 资金域名：https://center.zbmf.com/rest/json/
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void walle_get(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        Log.e("center_get>>>", AppUrl.Walle_URL + "?" + getRequest(url, params));
        center_client.get(AppUrl.Walle_URL, getRequest(url, params), responseHandler);
    }

    public static void walle_post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        Log.e("center_post>>>", AppUrl.Walle_URL + "?" + getRequest(url, params));
        center_client.post(AppUrl.Walle_URL, getRequest(url, params), responseHandler);
    }

    /**
     * 获取当前登陆用户资金
     *
     * @param jsonHandler
     */
    public static void getWalle(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.GET_WALLE, param, jsonHandler);
    }

    /**
     * 绑定用户client_Id
     *
     * @param jsonHandler
     */
    public static void Bind(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("socket_id", SettingDefaultsManager.getInstance().getClientId());
        post(AppUrl.BIND, param, jsonHandler);
    }

    /**
     * 版本更新
     *
     * @param jsonHandler
     */
    public static void vers(JSONHandler jsonHandler) {
        walle_get(AppUrl.VERS, null, jsonHandler);
    }

    /**
     * 登陆炒股圈子
     *
     * @param user_name       用户名
     * @param pass_word       用户密码
     * @param responseHandler
     */
    public static void login(String user_name, String pass_word, String client_id, AsyncHttpResponseHandler responseHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("username", user_name);
        param.put("password", pass_word);
        param.put("client_id", client_id);
        login_post(AppUrl.URL_LOGINAUTH, param, responseHandler);
    }

    /**
     * 退出登陆
     *
     * @param jsonHandler
     */
    public static void logout(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        login_post(AppUrl.LOGIN_OUT, param, jsonHandler);
    }

    /**
     * 发送验证码
     *
     * @param phone       手机号
     * @param client_id   个推ID
     * @param jsonHandler
     */
    public static void send_code(String phone, String client_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("phone", phone);
        param.put("client_id", client_id);
        login_post(AppUrl.POST_CODE, param, jsonHandler);
    }

    /**
     * 注册账户
     *
     * @param client_id   个推ID
     * @param code        验证码
     * @param phone       手机号
     * @param password    密码
     * @param jsonHandler
     */
    public static void register(String client_id, String code, String phone, String password, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("client_id", client_id);
        param.put("code", code);
        param.put("phone", phone);
        param.put("password", password);
        login_post(AppUrl.REGISTER, param, jsonHandler);
    }

    /**
     * 上传图片
     *
     * @param
     * @param jsonHandler
     */
    public static void uploadAvatar(String path, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        RequestParams params = getRequest(AppUrl.uploadAvatar, param, path);
        client.setTimeout(7000);
        client.post(BASE_URL, params, jsonHandler);
    }

    public static RequestParams getRequest(String method, Map<String, String> map, String path) {
        String api_sig = AppUrl.API_SECRET;
        if (map == null) {
            map = new HashMap<>();
        }
        if (method != null) {
            map.put("method", method);
        }
        map.put("api_key", AppUrl.API_KEY);
        map.put("device_type", AppUrl.DEVICE_TYPE);
        RequestParams params = new RequestParams();
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            map = sortMapByKey(map);
            for (String key : map.keySet()) {
                api_sig += key + map.get(key);
            }
            map.put("api_sig", getMD5String(api_sig).toLowerCase());
            for (String key : map.keySet()) {
                params.put(key, map.get(key));
            }
            try {
                params.put("avatar", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return params;
    }


    /**
     * 发送重置验证码
     *
     * @param phone
     * @param client_id
     * @param jsonHandler
     */
    public static void code_forget(String phone, String client_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("phone", phone);
        param.put("client_id", client_id);
        login_post(AppUrl.CODE_FORGET, param, jsonHandler);
    }

    //默认头像列表
    public static void defaultAvatar(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        login_post(AppUrl.defaultAvatar, param, jsonHandler);
    }

    //更新头像 !!!垃圾文档啊!!
    public static void updateUser(String icon_key, String nickname, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("nickname", nickname);
        if (icon_key != null) {
            param.put("icon_key", icon_key);
        }
        login_post(AppUrl.updateUser, param, jsonHandler);
    }

    public static void uploadAvatar(String path, String nickname, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("nickname", nickname);
        if (path != null && !TextUtils.isEmpty(path)) {
            RequestParams params = getRequest(AppUrl.updateUser, param, path);
            client.setTimeout(7000);
            client.post(BASE_URL, params, jsonHandler);
        } else {
            login_post(AppUrl.updateUser, param, jsonHandler);
        }

    }

    /**
     * 校验重置验证码
     *
     * @param client_id
     * @param code
     * @param phone
     * @param jsonHandler return
     */
    public static void verifyForget(String client_id, String code, String phone, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("client_id", client_id);
        param.put("code", code);
        param.put("phone", phone);
        login_post(AppUrl.VERIFYFORGET, param, jsonHandler);
    }

    /**
     * 忘记密码
     *
     * @param client_id
     * @param phone
     * @param jsonHandler
     */
    public static void pwdForget(String client_id, String phone, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("client_id", client_id);
        param.put("phone", phone);
        login_post(AppUrl.VERIFYFORGET, param, jsonHandler);
    }

    /**
     * 重置忘记密码
     *
     * @param client_id   个推ID
     * @param log_id      验证码ID
     * @param phone       手机号码
     * @param password    新密码
     * @param jsonHandler
     */
    public static void resetForget(String client_id, String log_id, String phone, String password, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("client_id", client_id);
        param.put("log_id", log_id);
        param.put("phone", phone);
        param.put("password", password);
        login_post(AppUrl.RESTFORGET, param, jsonHandler);
    }

    /**
     * 获取用户详情
     * @param auth_token
     * @param jsonHandler
     */
    public static void userInfo(String auth_token, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", auth_token);
        login_post(AppUrl.userInfo, param, jsonHandler);
    }

    /**
     * 我订阅的宝盒
     *
     * @param is_stop     是否下架 0或1
     * @param is_stick    是否推荐 0或1
     * @param page
     * @param jsonHandler
     */
    public static void getUserBoxs(String is_stop, String is_stick, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("is_stop", is_stop);
        param.put("is_stick", is_stick);
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        post(AppUrl.getUserBoxs, param, jsonHandler);
    }

    /**
     * 圈子宝盒详情
     *
     * @param id          圈主ID
     * @param box_id      宝盒ID
     * @param page
     * @param jsonHandler
     */
    public static void getBoxInfo(String id, String box_id, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("box_id",box_id);
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        get(AppUrl.getGroupBoxInfo, param, jsonHandler);
    }


    /**
     * 圈子宝盒列表
     *
     * @param is_stick    是否推荐 0或1
     * @param page
     * @param jsonHandler
     */
    public static void getGroupBoxs(String id, String is_stick, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("is_stick", is_stick);
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        get(AppUrl.getGroupBoxs, param, jsonHandler);
    }

    /**
     * 宝盒标签
     *
     * @param auth_token
     * @param jsonHandler
     */
    public static void getManageTags(String auth_token, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", auth_token);
        post(AppUrl.getManageTags, param, jsonHandler);
    }

    /**
     * 礼物列表
     *
     * @param jsonHandler
     */
    public static void getGiftList(JSONHandler jsonHandler) {
        get(AppUrl.getGiftList, null, jsonHandler);
    }

    /**
     * 赠送礼物
     *
     * @param jsonHandler
     */
    public static void sendGift(String id, String gift_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("gift_id", gift_id);
        param.put("amount", "1");
        get(AppUrl.SEND_GIFT, param, jsonHandler);
    }

    /**
     * 离开圈子
     *
     * @param jsonHandler
     */
    public static void leaveGroup(String group_id,JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("socket_id", SettingDefaultsManager.getInstance().getClientId());
        param.put("group_id", group_id);
        post(AppUrl.LEAVEGROUP, param, jsonHandler);
    }

    /**
     * 加入圈子
     *
     * @param jsonHandler
     */
    public static void enterGroup(String group_id,JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("socket_id", SettingDefaultsManager.getInstance().getClientId());
        param.put("group_id", group_id);
        post(AppUrl.ENTERGROUP, param, jsonHandler);
    }

    /**
     * 发送聊天小组消息
     *
     * @param group_id
     * @param type        消息类型(详见消息结构)
     * @param content
     * @param url
     * @param jsonHandler
     */
    public static void sendToRoom(String group_id, String type, String content, String url, String client_msg_id, ChatHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("type", type);
        param.put("content", content);
        param.put("client_msg_id", client_msg_id);
        param.put("socket_id", SettingDefaultsManager.getInstance().getClientId());

        if (!"".equals(url))
            param.put("url", url);

        post(AppUrl.sendToRoom, param, jsonHandler);
    }

    /**
     * 获取直播室消息记录
     *
     * @param time
     * @param jsonHandler
     */
    public static void getLiveMsg(String group_id, long time, boolean just_look_tf, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("time", time + "");
        if (just_look_tf) {
            param.put("msg_type", MessageType.FANS);
        }
        post(AppUrl.GETLIVEMSG, param, jsonHandler);
    }

    /**
     * 获取聊天小组消息记录
     *
     * @param time        时间戳 （毫秒），获取该时间之前的消息
     * @param jsonHandler
     */
    public static void getRoomMsg(String group_id, String time, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("time", time);
        post(AppUrl.getRoomMsg, param, jsonHandler);
    }

    /**
     * 开红包
     *
     * @param red_id
     * @param openRedPackHandler
     */
    public static void openRedPackged(String red_id, OpenRedPackHandler openRedPackHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("packet_id", red_id);
        post(AppUrl.openRedPackged, param, openRedPackHandler);
    }

    /**
     * 抢红包
     *
     * @param red_id
     * @param redPackHandler
     */
    public static void getRedPackged(String red_id, GetRedPackHandler redPackHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("packet_id", red_id);
        post(AppUrl.getRedPackged, param, redPackHandler);
    }

    /**
     * 红包详情
     *
     * @param red_id
     * @param redPackgedDetailHandler
     */
    public static void getRedPackgedDetail(String red_id, int page, RedPackgedDetailHandler redPackgedDetailHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("packet_id", red_id);
        param.put("page", page + "");
        param.put("per_page", 10 + "");
        post(AppUrl.getRedMessage, param, redPackgedDetailHandler);
    }

    /**
     * 发送红包
     *
     * @param
     * @param jsonHandler
     */
    public static void sendRedPackged(String title, String pays, int amount, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("title", title);
        param.put("pays", pays);
        param.put("amount", amount + "");
        post(AppUrl.sendRed, param, jsonHandler);
    }

    /**
     * 举报消息
     *
     * @param jsonHandler
     */
    public static void complaint(String msg_id, String room, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("msg_id", msg_id);
        param.put("room", room);
        post(AppUrl.complaint, param, jsonHandler);
    }

    /**
     * 圈子优惠券列表
     *
     * @param id
     * @param jsonHandler
     */
    public static void getGroupCoupons(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        post(AppUrl.getGroupCoupons, param, jsonHandler);
    }

    /**
     * 领取圈子优惠券
     *
     * @param coupon_id
     * @param jsonHandler
     */
    public static void takeCoupon(String coupon_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("coupon_id", coupon_id);
        post(AppUrl.takeCoupon, param, jsonHandler);
    }

    /**
     * 获取用户优惠券列表
     *
     * @param id
     * @param jsonHandler
     */
    public static void getUserCoupons(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        if (id != null) {
            param.put("id", id);
        }
        post(AppUrl.getUserCoupons, param, jsonHandler);
    }

    /**
     * 获取用户不可用优惠券
     *
     * @param jsonHandler
     */
    public static void getHistCoupons(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        get(AppUrl.getHistCoupons, param, jsonHandler);
    }

    /**
     * 关注圈子
     *
     * @param id
     * @param jsonHandler
     */
    public static void follow(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        post(AppUrl.follow, param, jsonHandler);
    }

    /**
     * 取消关注圈子
     *
     * @param id
     * @param jsonHandler
     */
    public static void unfollow(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        post(AppUrl.unfollow, param, jsonHandler);
    }

    /**
     * 获取圈子数据
     *
     * @param id
     * @param jsonHandler
     */
    public static void getGroupInfo(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        post(AppUrl.getGroupInfo, param, jsonHandler);
    }

    /**
     * 获取圈子数据
     *
     * @param id
     * @param jsonHandler
     */
    public static void groupInfo(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        post(AppUrl.groupInfo, param, jsonHandler);
    }

    /**
     * 圈子是否在直播
     *
     * @param id
     * @param jsonHandler
     */
    public static void isOnline(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("group_id", id);
        get(AppUrl.isOnline, param, jsonHandler);
    }

    /**
     * 圈子数据统计
     *
     * @param id
     * @param jsonHandler
     */
    public static void GroupStat(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        get(AppUrl.GroupStat, param, jsonHandler);
    }

    /**
     * 获取铁粉专区数据
     *
     * @param id
     * @param jsonHandler
     */
    public static void fansInfo(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        post(AppUrl.fansInfo, param, jsonHandler);
    }

    /**
     * 获取铁粉订阅选项
     *
     * @param id
     * @param jsonHandler
     */
    public static void fansProduct(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        post(AppUrl.fansProduct, param, jsonHandler);
    }

    /**
     * 按月加入铁粉
     *
     * @param id
     * @param jsonHandler
     */
    public static void subFans(String id, int monthz, String take_id,boolean jf_to_mfb, long point, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("monthz", monthz + "");
        if (take_id != null) {
            param.put("take_id", take_id);
        }
        if(jf_to_mfb){
            param.put("point", String.format("%d", point));
        }else{
            param.put("point", String.format("%d", 0));
        }
        post(AppUrl.subFans, param, jsonHandler);
    }

    /**
     * 按天加入铁粉
     *
     * @param id
     * @param jsonHandler
     */
    public static void subGuest(String id, int days,boolean jf_to_mfb, String take_id, long point, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("days", days + "");
        if (take_id != null) {
            param.put("take_id", take_id);
        }
        if(jf_to_mfb){
            param.put("point", String.format("%d", point));
        }else{
            param.put("point", String.format("%d", 0));
        }

        post(AppUrl.subGuest, param, jsonHandler);
    }

    /**
     * 微信登陆
     *
     * @param
     * @param jsonHandler
     */
    public static void weChat(String code, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("code", code);
        param.put("client_id", SettingDefaultsManager.getInstance().PUSH_CILENT_ID());
        login_post(AppUrl.weChat, param, jsonHandler);
    }

    /**
     * QQ登陆
     *
     * @param
     * @param jsonHandler
     */
    public static void qq_sina_login(String openid, String toekn, String api_type, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("openid", openid);
        param.put("token", toekn);
        param.put("api_type", api_type);
        param.put("client_id", SettingDefaultsManager.getInstance().PUSH_CILENT_ID());
        login_post(AppUrl.getAccessTokenByOpenapi, param, jsonHandler);
    }

    /**
     * 绑定手机号
     *
     * @param phone
     * @param code
     * @param password
     * @param jsonHandler
     */
    public static void bindPhone(String phone, String code, String password, String truename,String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("phone", phone);
        param.put("client_id", id);
        param.put("code", code);
        if (!TextUtils.isEmpty(password))
            param.put("password", password);


        login_post(AppUrl.bindPhone, param, jsonHandler);
    }

    public static void bindName(String phone, String code, String truename,String idcard,String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("truename", truename);
        param.put("client_id", id);
        param.put("idcard", idcard);
        if (!TextUtils.isEmpty(code)){
            param.put("code", code);
            param.put("phone", phone);
        }

        login_post(AppUrl.bindName, param, jsonHandler);
    }

    /**
     * 获取QQ用户信息
     *
     * @param oauth_consumer_key
     * @param access_token
     * @param openid
     * @param jsonHandler
     */
    public static void getQQMessage(String oauth_consumer_key, String access_token, String openid, AsyncHttpResponseHandler jsonHandler) {
        RequestParams params = new RequestParams();
        params.put("oauth_consumer_key", oauth_consumer_key);
        params.put("access_token", access_token);
        params.put("openid", openid);
        client.get(AppUrl.QQ_MESSAGE, params, jsonHandler);
    }

    /**
     * 获取直播历史纪录
     *
     * @param group_id
     * @param date
     * @param page
     * @param jsonHandler
     */
    public static void getHistoryMsg(String group_id, String date, String page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("group_id", group_id);
        param.put("date", date);
        param.put("page", page);
        get(AppUrl.GET_HISTORY_MSG, param, jsonHandler);
    }

    /**
     * 获取首页推荐
     *
     * @param jsonHandler
     */
    public static void recommend(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        get(AppUrl.recommend, param, jsonHandler);
    }

    /**
     * 获取更多推荐
     *
     * @param jsonHandler
     */
    public static void recommend(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        get(AppUrl.recommend, param, jsonHandler);
    }

    /**
     * 魔方头条
     *
     * @param page
     * @param jsonHandler
     */
    public static void searchUserBlogs(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        walle_get(AppUrl.searchUserBlogs, param, jsonHandler);
    }

    /**
     * 注释没了呢？
     *
     * @param auth_token
     * @param jsonHandler
     */
    public static void userAsks(int page, String auth_token, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", auth_token);
        param.put("page", page + "");
        param.put("per_page", 5 + "");
        get(AppUrl.userAsks, param, jsonHandler);
    }

    /**
     * 用户提问？
     *
     * @param auth_token
     * @param jsonHandler
     */
    public static void groupAnsweredAsks(String id, int page, String auth_token, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", auth_token);
        param.put("id", id);
        param.put("page", page + "");
        param.put("per_page", 5 + "");
        get(AppUrl.groupAnsweredAsks, param, jsonHandler);
    }

    /**
     * 获取用户博文列表
     *
     * @param id
     * @param page
     * @param jsonHandler
     */
    public static void getUserBlogs(String id, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        walle_get(AppUrl.getUserBlogs, param, jsonHandler);
    }

    /**
     * 用户博文评论
     *
     * @param blog_id
     * @param page
     * @param jsonHandler
     */
    public static void getUserBlogPosts(String blog_id, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("blog_id", blog_id);
        param.put("id", SettingDefaultsManager.getInstance().UserId());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        walle_get(AppUrl.getUserBlogPosts, param, jsonHandler);
    }

    /**
     * 用户博文评论
     *
     * @param blog_id
     * @param
     * @param jsonHandler
     */
    public static void createUserBlogPost(String blog_id, String content, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("blog_id", blog_id);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("content", content);
        walle_post(AppUrl.createUserBlogPost, param, jsonHandler);
    }

    /**
     * 首页广告
     *
     * @param
     * @param
     * @param jsonHandler
     */
    public static void getAdverts(JSONHandler jsonHandler) {
        walle_get(AppUrl.getAdverts, null, jsonHandler);
    }

    /**
     * 我关注的圈子
     *
     * @param page
     * @param jsonHandler
     */
    public static void userGroups(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        get(AppUrl.userGroups, param, jsonHandler);
    }

    /**
     * 积分明细
     *
     * @param page
     * @param jsonHandler
     */
    public static void pointLogs(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        walle_get(AppUrl.pointLogs, param, jsonHandler);
    }

    /**
     * 魔方明细
     *
     * @param page
     * @param jsonHandler
     */
    public static void payLogs(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        walle_get(AppUrl.payLogs, param, jsonHandler);
    }

    /**
     * 充值列表
     *
     * @param
     * @param
     * @param jsonHandler
     */
    public static void products(JSONHandler jsonHandler) {
        walle_get(AppUrl.products, null, jsonHandler);
    }

    /**
     * 微信支付
     *
     * @param
     * @param jsonHandler
     */
    public static void wx_pay(String id, String pro_num, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        if (id.equals("7")) {
            param.put("pro_num", pro_num);
        }
        walle_post(AppUrl.weixin_pay, param, jsonHandler);
    }

    public static void ask(String id, String content,int flag, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("content", content);
        param.put("is_private",flag+"");
        post(AppUrl.ask, param, jsonHandler);
    }

    //博文动态
    public static void blog(int feed_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("feed_id", feed_id + "");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.blog, param, jsonHandler);
    }

    /**
     * 收藏博文
     *
     * @param blog_id
     * @param jsonHandler
     */
    public static void createBlogCollect(String blog_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("blog_id", blog_id);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.createBlogCollect, param, jsonHandler);
    }

    /**
     * 取消收藏
     *
     * @param blog_id
     * @param jsonHandler
     */
    public static void removeBlogCollect(String blog_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("blog_id", blog_id);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.removeBlogCollect, param, jsonHandler);
    }

    /**
     * 取消收藏
     *
     * @param page
     * @param jsonHandler
     */
    public static void getUserCollects(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("collect_type", Constants.BLOG_COLLECT_TYPE);
        walle_get(AppUrl.getUserCollects, param, jsonHandler);
    }

    /**
     * 用户博文是否收藏
     *
     * @param blog_id
     * @param jsonHandler
     */
    public static void getUserBlogInfo(String blog_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("blog_id", blog_id);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.getUserBlogInfo, param, jsonHandler);
    }

    /**
     * 搜索博文详情
     *
     * @param blog_id
     * @param jsonHandler
     */
    public static void searchUserBlogInfo(String blog_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("blog_id", blog_id);
        walle_get(AppUrl.searchUserBlogInfo, param, jsonHandler);

    }

    /**
     * 铁粉
     *
     * @param is_more
     * @param jsonHandler
     */
    public static void box(String is_more, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("is_more", is_more);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.box, param, jsonHandler);
    }

    //是否绑定手机
    public static void isBind(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        login_post(AppUrl.isBind, param, jsonHandler);
    }

    /**
     * 每日签到
     *
     * @param jsonHandler
     */
    public static void signIn(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.signIn, param, jsonHandler);
    }

    /**
     * 签到状态
     *
     * @param jsonHandler
     */
    public static void userSigns(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.userSigns, param, jsonHandler);
    }

    //修改密码
    public static void changePwd(String oldpassword, String newpwd, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("oldpassword", oldpassword);
        param.put("password", newpwd);
        login_post(AppUrl.changePwd, param, jsonHandler);
    }

    //发送绑定手机验证码
    public static void codeBind(String phone, String client_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("phone", phone);
        param.put("client_id", client_id);
        login_post(AppUrl.codeBind, param, jsonHandler);
    }
    //管理宝盒详情
    public static void getGroupBoxItems(String group_id,String box_id,int page,int per_page,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", group_id);
        param.put("box_id", box_id);
        param.put("page", page+"");
        param.put("per_page", per_page+"");
        get(AppUrl.getGroupBoxItems, param,  jsonHandler);
    }

}
package com.zbmf.StockGTec.api;


import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zbmf.StockGTec.fragment.ChatFragment;
import com.zbmf.StockGTec.utils.BitmapUtil;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WebBase {
    private static final String BASE_URL = AppUrl.ALL_URL_PREFIX;
    private static AsyncHttpClient client = new AsyncHttpClient();

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

        Log.e("TAG", params.toString());
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
        client.setTimeout(7000);
        client.get(BASE_URL, getRequest(url, params), responseHandler);
    }

    public static void login_post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(7000);
        RequestParams param = getRequest(url, params);
        Log.e("方法" + url, BASE_URL + param.toString());
        client.post(BASE_URL, param, responseHandler);
    }

    /**
     * 普通域名：https://group.zbmf.com/rest/json/
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(7000);
        client.get(AppUrl.ALL_URL, getRequest(url, params), responseHandler);
    }

    public static void post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(7000);
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
        client.setTimeout(7000);
        client.get(AppUrl.Walle_URL, getRequest(url, params), responseHandler);
    }

    public static void walle_post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(7000);
        client.post(AppUrl.Walle_URL, getRequest(url, params), responseHandler);
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
        Map<String, String> param = new HashMap<>();
        param.put("device_type", AppUrl.DEVICE_TYPE);
        walle_get(AppUrl.VERS, param, jsonHandler);
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
        login_post(AppUrl.VERIFYFORGET, param, jsonHandler);
    }

    /**
     * 我订阅的宝盒
     *
     * @param auth_token
     * @param is_stop     是否下架 0或1
     * @param is_stick    是否推荐 0或1
     * @param page
     * @param per_page
     * @param jsonHandler
     */
    public static void getUserBoxs(String auth_token, String is_stop, String is_stick, int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", auth_token);
        param.put("is_stop", is_stop);
        param.put("is_stick", is_stick);
        param.put("page", page + "");
        param.put("per_page", per_page + "");
        post(AppUrl.getUserBoxs, param, jsonHandler);
    }

    /**
     * 我的宝盒详情/圈子宝盒详情
     *
     * @param auth_token
     * @param id          圈主ID
     * @param box_id      宝盒ID
     * @param page
     * @param per_page
     * @param url         请求宝盒类型
     * @param jsonHandler
     */
    public static void getBoxInfo(String auth_token, String id, String box_id, int page, int per_page, String url, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", auth_token);
        param.put("id", id);
        param.put("box_id", box_id);
        param.put("page", page + "");
        param.put("per_page", per_page + "");
        post(url, param, jsonHandler);
    }


    /**
     * 圈子宝盒列表
     *
     * @param auth_token
     * @param page
     * @param per_page
     * @param jsonHandler
     */
    public static void getGroupBoxs(String auth_token, int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", auth_token);
        param.put("page", page + "");
        param.put("per_page", per_page + "");
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
        post(AppUrl.SEND_GIFT, param, jsonHandler);
    }

    /**
     * 离开圈子
     *
     * @param jsonHandler
     */
    public static void leaveGroup(String group_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("socket_id", SettingDefaultsManager.getInstance().getClientId());
        param.put("group_id", group_id);
        post(AppUrl.LEAVEGROUP, param, jsonHandler);
    }

    /**
     * 进入圈子
     *
     * @param jsonHandler
     */
    public static void enterGroup(String group_id, JSONHandler jsonHandler) {
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
    public static void sendToRoom(int sendType, String group_id, String type, String content, String url, String client_msg_id, String creator, ChatHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("type", type);
        param.put("content", content);
        String method = "zbmf.im.sendToFans";
        if (sendType == ChatFragment.GROUP_CHAT) {

            method = "zbmf.im.sendToRoom";
            param.put("creator", creator);
        }
        param.put("client_msg_id", client_msg_id);
        param.put("socket_id", SettingDefaultsManager.getInstance().getClientId());

        if (!"".equals(url))
            param.put("url", url);

        post(method, param, jsonHandler);
    }

    /**
     * 获取直播室消息记录
     *
     * @param time
     * @param jsonHandler
     */
    public static void getLiveMsg(String group_id, long time, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("time", time + "");
        post(AppUrl.GETLIVEMSG, param, jsonHandler);
    }


    /**
     * 获取聊天小组消息记录
     *
     * @param time        时间戳 （毫秒），获取该时间之前的消息
     * @param jsonHandler
     */
    public static void getRoomMsg(int chatType, String group_id, String time, JSONHandler jsonHandler) {
        String method = AppUrl.getRoomMsg;
        if (chatType == ChatFragment.FANS_CHAT)
            method = AppUrl.getFansMsg;
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("time", time);
        post(method, param, jsonHandler);
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
     * 红包详情
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
     * 发送直播室消息
     * type	 消息类型(详见消息结构)	txt
     * content  消息内容	hello world
     * url 	图片或语音（type 为 img 或 audio 时必填）	``
     * fans  是否铁粉悄悄话，1：是; 0：否	0
     * client_msg_id  客户端消息ID	123
     * socket_id	 如传值，则不推送自己发的消息
     */
    public static void sendToLive(String group_id, String type, String content, String url, int width, int height, int fans, int important,int creator, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("client_msg_id", Utils.getMD5String(new Date().getTime() + ""));
        param.put("group_id", group_id);
        param.put("type", type);
        param.put("content", content);
        param.put("url", url);
        param.put("width", width + "");
        param.put("height", height + "");
        param.put("fans", fans + "");
        param.put("socket_id", "");
        param.put("important", important + "");
        param.put("creator",creator+"");
        post(AppUrl.sendToLive, param, jsonHandler);
    }

    /**
     * 上传图片
     *
     * @param group_id
     * @param file
     * @param jsonHandler
     */
    public static void uploadImg(String group_id, String file, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("client_msg_id", Utils.getMD5String(new Date().getTime() + ""));
        param.put("group_id", group_id);
        RequestParams params = getRequest(AppUrl.uploadImg, param, file);
        client.setTimeout(7000);
        client.post(AppUrl.ALL_URL, params, jsonHandler);
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
        File file = BitmapUtil.scal(path);
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
                params.put("image", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return params;
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
     * 禁言
     *
     * @param group_id
     * @param user_id
     * @param jsonHandler
     */
    public static void ban(String group_id, String user_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("user_id", user_id);
        post(AppUrl.ban, param, jsonHandler);
    }

    /**
     * 解除禁言
     *
     * @param group_id
     * @param user_id
     * @param jsonHandler
     */
    public static void unBan(String group_id, String user_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("user_id", user_id);
        post(AppUrl.unBan, param, jsonHandler);
    }

    /**
     * 删除消息
     *
     * @param group_id
     * @param msg_id
     * @param room
     * @param jsonHandler
     */
    public static void deleteMsg(String group_id, String msg_id, String room, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("msg_id", msg_id);
        param.put("room", room);
        post(AppUrl.deleteMsg, param, jsonHandler);
    }

    //设置公告
    public static void setNotice(String group_id, String content, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", group_id);
        param.put("content", content);
        post(AppUrl.setNotice, param, jsonHandler);
    }

    //获取圈子禁言列表
    public static void banList(String group_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        post(AppUrl.banList, param, jsonHandler);
    }

    //获取圈子公告
    public static void notice(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("id", SettingDefaultsManager.getInstance().UserId());
        post(AppUrl.notice, param, jsonHandler);
    }

    //全部铁粉 he  即将到期的铁粉
    public static void Fans(String method, String group_id, int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", group_id);
        param.put("page", page + "");
        param.put("per_page", per_page + "");
        get(method, param, jsonHandler);
    }

    //管理宝盒详情
    public static void getGroupBoxInfo(String box_id, int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", SettingDefaultsManager.getInstance().UserId());
        param.put("box_id", box_id);
        param.put("page", page + "");
        param.put("per_page", per_page + "");
        get(AppUrl.getManageBoxInfo, param, jsonHandler);
    }

    //管理宝盒详情
    public static void getGroupBoxItems(String box_id, int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", SettingDefaultsManager.getInstance().UserId());
        param.put("box_id", box_id);
        param.put("page", page + "");
        param.put("per_page", per_page + "");
        get(AppUrl.getGroupBoxItems, param, jsonHandler);
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

    //宝盒置顶
    public static void stickManageBox(String box_id, String is_stick, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("box_id", box_id);
        param.put("is_stick", is_stick);
        post(AppUrl.stickManageBox, param, jsonHandler);
    }

    //宝盒下架
    public static void stopManageBox(String box_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("box_id", box_id);
        post(AppUrl.stopManageBox, param, jsonHandler);
    }

    //宝盒删除
    public static void removeManageBox(String box_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("box_id", box_id);
        post(AppUrl.removeManageBox, param, jsonHandler);
    }

    //宝盒刷新
    public static void updatedManageBox(String box_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("box_id", box_id);
        post(AppUrl.updatedManageBox, param, jsonHandler);
    }

    //圈子未回答的提问
    public static void groupAsks(int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", per_page + "");
        get(AppUrl.groupAsks, param, jsonHandler);
    }

    //圈子未回答的提问
    public static void groupAllAsks(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        get(AppUrl.groupAllAsks, param, jsonHandler);
    }

    //删除问题
    public static void deleteAsks(String ask_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("ask_id", ask_id);
        post(AppUrl.deleteAsks, param, jsonHandler);
    }

    //回答
    public static void post(String ask_id, String content, String id, String is_private, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("ask_id", ask_id);
        param.put("id", id);
        param.put("content", content);
        param.put("is_private", is_private);
        post(AppUrl.post, param, jsonHandler);
    }

    //宝盒内容添加
    public static void createManageBoxItem(String box_id, String content, String img, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("box_id", box_id);
        param.put("content", content);
        if (!TextUtils.isEmpty(img))
            param.put("img", img);
        post(AppUrl.createManageBoxItem, param, jsonHandler);
    }

    //魔方宝详情
    public static void payLogs(String month, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        if (month != null) {
            param.put("log_month", month + "");
        }
        param.put("page", page + "");
        param.put("per_page", 20 + "");
        walle_get(AppUrl.payLogs, param, jsonHandler);
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
     * @param page
     * @param is_live     视频(0)or直播(1)
     * @param jsonHandler
     */
    public static void GetsVideos(int page, int per_page, int is_live, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("is_live", is_live + "");
        param.put("per_page", per_page + "");
        post(AppUrl.GetsVideos, param, jsonHandler);
    }

    //邀请列表
    public static void getInviteList(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        walle_get(AppUrl.INVITELIST, param, jsonHandler);
    }

    public static void getVideoMsg(int video_id, String time,String after, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("video_id", video_id + "");
        param.put("time", time);
        param.put("after", after);
        post(AppUrl.getVideoMsg, param, jsonHandler);
    }

    /**
     * 获取圈子数据
     *
     * @param id
     * @param jsonHandler
     */
    public static void groupInfo( JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id",SettingDefaultsManager.getInstance().getGroupId());
        post(AppUrl.groupInfo, param, jsonHandler);
    }

    public static void getWelcomeAdverts(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("category",12+"");
        param.put("limit",1+"");
        walle_get("zbmf.advert.getAdverts", param, jsonHandler);
    }

    public static void groupData( JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id",SettingDefaultsManager.getInstance().getGroupId());
        post("zbmf.groups.data", param, jsonHandler);
    }

}
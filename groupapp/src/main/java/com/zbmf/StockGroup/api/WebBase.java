package com.zbmf.StockGroup.api;


import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zbmf.StockGroup.callback.LoadFileResult;
import com.zbmf.StockGroup.callback.UpFileResult;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.MessageType;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

import org.apache.http.Header;

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
    private static AsyncHttpClient match_client = new AsyncHttpClient();
    private static AsyncHttpClient stock_client = new AsyncHttpClient();

    static {
        client.setMaxConnections(20);
        center_client.setMaxConnections(20);
        passport_client.setMaxConnections(20);
        match_client.setMaxConnections(20);
        stock_client.setMaxConnections(20);
        client.setMaxRetriesAndTimeout(2, 3000);
        center_client.setMaxRetriesAndTimeout(2, 3000);
        passport_client.setMaxRetriesAndTimeout(2, 3000);
        match_client.setMaxRetriesAndTimeout(2, 3000);
        stock_client.setMaxRetriesAndTimeout(2, 3000);
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
        LogUtil.e(BASE_URL + "?" + getRequest(url, params));
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
        LogUtil.e(AppUrl.ALL_URL + "?" + getRequest(url, params));
        client.get(AppUrl.ALL_URL, getRequest(url, params), responseHandler);
    }

    public static void post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e(AppUrl.ALL_URL + "?" + getRequest(url, params));
        client.post(AppUrl.ALL_URL, getRequest(url, params), responseHandler);
    }

    public static void stockGet(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e(AppUrl.STOCK_URL + "?" + getRequest(url, params));
        client.get(AppUrl.STOCK_URL, getRequest(url, params), responseHandler);
    }

    public static void stockPost(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e(AppUrl.STOCK_URL + "?" + getRequest(url, params));
        client.post(AppUrl.STOCK_URL, getRequest(url, params), responseHandler);
    }

    /**
     * 资金域名：https://center.zbmf.com/rest/json/
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void walle_get(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e(AppUrl.Walle_URL + "?" + getRequest(url, params));
        center_client.get(AppUrl.Walle_URL, getRequest(url, params), responseHandler);
    }

    public static void walle_post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e("center_post>>>" + AppUrl.Walle_URL + "?" + getRequest(url, params));
        center_client.post(AppUrl.Walle_URL, getRequest(url, params), responseHandler);
    }

    public static void match_get(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e(AppUrl.Walle_URL + "?" + getRequest(url, params));
        center_client.get(AppUrl.MATCH_URL, getRequest(url, params), responseHandler);
    }
    public static void zbmf_post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e(AppUrl.Walle_URL + "?" + getRequest(url, params));
        center_client.get(AppUrl.ZBMF_URL, getRequest(url, params), responseHandler);
    }
    public static void match_post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e("center_post>>>" + AppUrl.MATCH_URL + "?" + getRequest(url, params));
        center_client.post(AppUrl.MATCH_URL, getRequest(url, params), responseHandler);
    }

    public static void match_post1(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e("center_post>>>" + AppUrl.GUO_PIAO_URL + "?" + getRequest(url, params));
        center_client.post(AppUrl.GUO_PIAO_URL, getRequest(url, params), responseHandler);
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


    //测试
    private static final String BASE =
            "http://www.printgo.cn/rest/json/";
    public static final String VERIFY_CODE="yun.auth.code";
    public static final String REGISTER="yun.auth.register";

    static String API_SECRET="ae6709a";//BrZ2g1TB
    static String API_KEY="fa6ea67bafae6709557430523e00802a";
    static String API_SIG="93857c76876c1c4178dfaa30d89a0f8a";//不涉及到token的不用传递api_sign
    public static RequestParams getRequest1(String method, Map<String, String> map) {
        String api_sig = API_SECRET;
        if (map == null) {
            map = new HashMap<>();
        }
        if (method != null) {
            map.put("method", method);
        }
        map.put("api_key", API_KEY);
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
    public static void basePost(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        LogUtil.e(BASE + "?" + getRequest(url, params));
        passport_client.post(BASE, getRequest1(url, params), responseHandler);
    }
    public static void getCode(String phone,JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("phone", phone);
        basePost(VERIFY_CODE, param, jsonHandler);
    }
    public static void getLogin( String client_id,String code, String user_name,String pass_word, AsyncHttpResponseHandler responseHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("phone", user_name);
        param.put("code", code);
        param.put("password", pass_word);
//        param.put("client_id", client_id);
        basePost(REGISTER, param, responseHandler);
    }

    /**
     * 上传图片
     *
     * @param
     * @param jsonHandler
     */
    public static void uploadAvatar1(String path,String url,String fileKey, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        RequestParams params = getRequest(url, param, path,fileKey);
        Log.i("--TAG","--- method :"+url);
        client.setTimeout(7000);
        client.post(BASE_URL, params, jsonHandler);
    }

    public static RequestParams getRequest(String method, Map<String, String> map, String path,String fileKey) {
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
                params.put(fileKey, file);
            } catch (FileNotFoundException e) {
                LogUtil.e(e.getMessage());
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
        LogUtil.e(path + "");
        LogUtil.e(TextUtils.isEmpty(path) + "");
        if (path != null && !TextUtils.isEmpty(path)) {
            RequestParams params = getRequest(AppUrl.updateUser, param, path,"avatar");
            client.setTimeout(7000);
            client.post(BASE_URL, params, jsonHandler);
            LogUtil.e("上传头像" + BASE_URL + params.toString());
        } else {
            login_post(AppUrl.updateUser, param, jsonHandler);
        }
    }
    //上传图片文件
    public static void upImgFile(String path,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        LogUtil.e(path + "");
        LogUtil.e(TextUtils.isEmpty(path) + "");
        if (path != null && !TextUtils.isEmpty(path)) {
            RequestParams params = getRequest(AppUrl.UP_IMG, param, path,"image");
            client.setTimeout(7000);
            client.post(AppUrl.ZBMF_URL, params, jsonHandler);
            LogUtil.e("上传图片" + AppUrl.ZBMF_URL + params.toString());
        } else {
            login_post(AppUrl.UP_IMG, param, jsonHandler);
        }
    }

    /**
     * @param path
     *            要上传的文件路径
     * @param url
     *            服务端接收URL
     * @throws Exception
     */
    public static void uploadFile(String path, String url, final UpFileResult upFileResult) {
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("method",AppUrl.UP_IMG);
            try {
                params.put("image", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            params.put("auth_token", SettingDefaultsManager.getInstance().authToken());
            // 上传文件
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    // 上传成功后要做的工作
                    upFileResult.success(statusCode,headers,responseBody);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    // 上传失败后要做到工作
                    upFileResult.failure(statusCode,headers,responseBody,error);
                }

                @Override
                public void onProgress(int bytesWritten, int totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                    // 上传进度显示
                   upFileResult.onProgress(count);
                }

                @Override
                public void onRetry(int retryNo) {
                    super.onRetry(retryNo);
                    // 返回重试次数
                    upFileResult.onRetry(retryNo);
                }
            });
        } else {
            upFileResult.failNoExist("文件不存在");
        }
    }
    /**
     * @param url
     *            要下载的文件URL
     * @throws Exception
     */
    public static void downloadFile(String url,final LoadFileResult loadFileResult) throws Exception {

        AsyncHttpClient client = new AsyncHttpClient();
        // 指定文件类型
        String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };
        // 获取二进制数据如图片和其他文件
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] binaryData) {
                loadFileResult.onSuccess(statusCode,headers,binaryData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                loadFileResult.onFailure(statusCode,headers,binaryData,error);
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                // 下载进度显示
                loadFileResult.onProgress(count);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                // 返回重试次数
                loadFileResult.onRetry(retryNo);
            }
        });
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
     *
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
        param.put("box_id", box_id);
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
    public static void sendGift(String id, String gift_id, int amount, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("gift_id", gift_id);
        param.put("amount", String.valueOf(amount));
        get(AppUrl.SEND_GIFT, param, jsonHandler);
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
     * 加入圈子
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

    public static void sendToFans(String group_id, String type, String content, String url, String client_msg_id, ChatHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("type", type);
        param.put("content", content);
        param.put("client_msg_id", client_msg_id);
        param.put("socket_id", SettingDefaultsManager.getInstance().getClientId());

        if (!"".equals(url))
            param.put("url", url);

        post(AppUrl.sendToFans, param, jsonHandler);
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
     * 获取铁粉小组消息记录
     *
     * @param time        时间戳 （毫秒），获取该时间之前的消息
     * @param jsonHandler
     */
    public static void getFansMsg(String group_id, String time, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("group_id", group_id);
        param.put("time", time);
        post(AppUrl.getFansMsg, param, jsonHandler);
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
     * 一键领取圈子优惠券
     *
     * @param id
     * @param jsonHandler
     */
    public static void takeAllCoupon(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        post(AppUrl.takeAllCoupon, param, jsonHandler);
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
    public static void getCoupons(String method, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        get(method, param, jsonHandler);
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
    public static void subFans(String id, int monthz, String take_id, boolean jf_to_mfb, long point, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("monthz", monthz + "");
        if (take_id != null) {
            param.put("take_id", take_id);
        }
        if (jf_to_mfb) {
            param.put("point", String.format("%d", point));
        } else {
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
    public static void subGuest(String id, int days, boolean jf_to_mfb, String take_id, long point, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("days", days + "");
        if (take_id != null) {
            param.put("take_id", take_id);
        }
        if (jf_to_mfb) {
            param.put("point", String.format("%d", point));
        } else {
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
    public static void bindPhone(String phone, String code, String password, String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("phone", phone);
        param.put("client_id", id);
        param.put("code", code);
        if (!TextUtils.isEmpty(password))
            param.put("password", password);


        login_post(AppUrl.bindPhone, param, jsonHandler);
    }

    public static void bindName(String phone, String code, String truename, String idcard, String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("truename", truename);
        param.put("client_id", id);
        param.put("idcard", idcard);
        if (!TextUtils.isEmpty(code)) {
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
    public static void recommendList(int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", 1 + "");
        param.put("per_page", per_page + "");
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

    public static void getTeaCher(int flag, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        String method = "";
        switch (flag) {
            case Constants.PEOPLE_ARROW:
                method = AppUrl.hot;
                break;
            case Constants.NOW_LIVE:
                method = AppUrl.live;
                break;
            case Constants.EXCLUSIVE:
                method = AppUrl.exclusive;
                break;
            case Constants.PEOPLE_RECOMED:
                method = AppUrl.recommend;
                break;
        }
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        if (!method.isEmpty()) {
            get(method, param, jsonHandler);
        }
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
     * 魔方头条
     *
     * @param page
     * @param jsonHandler
     */
    public static void searchUserBlogs(int page, String q, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        param.put("q", q);
        walle_get(AppUrl.searchUserBlogs, param, jsonHandler);
    }

    /**
     * 魔方头条
     *
     * @param jsonHandler
     */
    public static void searchUserBlogs(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("page", 1 + "");
        param.put("per_page", 3 + "");
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
     * 获取用户博文列表
     *
     * @param id
     * @param page
     * @param jsonHandler
     */
    public static void getUserBlogs(String id, int page, String q, String choiceness, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        param.put("q", q);
        param.put("choiceness", "1");
        walle_get(AppUrl.getUserBlogs, param, jsonHandler);
    }

    public static void getRudimentsBlogs(String id, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        param.put("rand", "1");
        walle_get(AppUrl.getRudimentsBlogs, param, jsonHandler);
    }

    public static void getRudimentsBlogs(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("per_page", "4");
        param.put("rand", "1");
        walle_get(AppUrl.getRudimentsBlogs, param, jsonHandler);
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
    //获取关注列表
    public static void getGzTopicList(JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.GZ_TOPICS_LIST,param,jsonHandler);
    }
    //获取话题分类列表
    public static void getTopicsList(String type_id,String page,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("type_id",type_id);
        param.put("page",page);
        param.put("per_page","10");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.FL_TOPICS_LIST,param,jsonHandler);
    }
    //获取话题详情
    public static void getHtDetail(String topic_id,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("topic_id",topic_id);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.TOPIC_DETAIL,param,jsonHandler);
    }
    //发布观点
    public static void publishPoint(String content,String topic_id,String img_keys,String height,String width,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("content",content);
        param.put("topic_id",topic_id);
        param.put("img_keys",img_keys);
        param.put("height",height);
        param.put("width",width);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.FB_POINT,param,jsonHandler);
    }
    //获取话题观点列表
    public static void getHtGdIdeaList(String topic_id,String page,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("topic_id",topic_id);
        param.put("page",page);
        param.put("per_page","10");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.TOPIC_GD_LIST,param,jsonHandler);
    }
    //关注话题
    public static void gzTopic(String topic_id,String status,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("topic_id",topic_id);
        param.put("status",status);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.GZ_TOPIC,param,jsonHandler);
    }
    //观点点赞
    public static void pointDz(String viewpoint_id,String status,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("viewpoint_id",viewpoint_id);
        param.put("status",status);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.GD_DZ,param,jsonHandler);
    }
    //发布评论
    public static void fbComment(String viewpoint_id,String content,String to_user,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("viewpoint_id",viewpoint_id);
        param.put("content",content);
        param.put("to_user","");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.FB_COMMENT,param,jsonHandler);
    }
    //获取观点详情
    public static void getGDDetail(String viewpoint_id,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("viewpoint_id",viewpoint_id);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.GET_GD_DETAIL,param,jsonHandler);
    }
    //获取未读消息数量
    public static void getNoReadMsgCount(JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.NO_READ_MSG_COUNT,param,jsonHandler);
    }
    //未读消息列表的数据
    public static void getNoReadMsgList(String page,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("page",page);
        param.put("per_page","10");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.NO_READ_MSG,param,jsonHandler);
    }
    //获取点赞用户
    public static void getDzUserList(String viewpoint_id,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("viewpoint_id",viewpoint_id);
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.DZ_USER_LIST,param,jsonHandler);
    }
    //获取评论列表
    public static void commentList(String viewpoint_id,String page,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
        param.put("viewpoint_id",viewpoint_id);
        param.put("page_id",page);
        param.put("per_page","10");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.COMMENT_LIST,param,jsonHandler);
    }
    //获取开通VIP会员的价格
    public static void getSubscribeVipPrice(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        walle_post(AppUrl.VIP_PRICE, param, jsonHandler);
    }
    //获取话题管理列表
    public static void getTopicList(JSONHandler jsonHandler){
        walle_post(AppUrl.TOPIC_LISTS,null,jsonHandler);
    }
    //开通VIP会员
    public static void subscribeVIP(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.SUBSCRIBE_VIP, param, jsonHandler);
    }

    //续费会员
    public static void xfVIP(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_post(AppUrl.XF_VIP, param, jsonHandler);
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

    public static void getWelcomeAdverts(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("category", 11 + "");
        param.put("limit", 1 + "");
        walle_get(AppUrl.getAdverts, param, jsonHandler);
    }

    public static void getHomeActivity(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("category", "13");
        param.put("limit", "2");
        walle_get(AppUrl.getAdverts, param, jsonHandler);
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

    public static void userGroups(int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", per_page + "");
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

    public static void ask(String id, String content, int flag, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("content", content);
        param.put("is_private", flag + "");
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
    public static void getGroupBoxItems(String group_id, String box_id, int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", group_id);
        param.put("box_id", box_id);
        param.put("page", page + "");
        param.put("per_page", per_page + "");
        get(AppUrl.getGroupBoxItems, param, jsonHandler);
    }

    /**
     * 获取数据列表
     *
     * @param jsonHandler
     */
    public static void GetsVideos(Map<String, String> param, JSONHandler jsonHandler) {
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        post(AppUrl.GetsVideos, param, jsonHandler);
    }

    /**
     * 获取专辑列表
     *
     * @param page
     * @param jsonHandler
     */
    public static void GetSeries(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        post(AppUrl.GetSeries, param, jsonHandler);
    }

    /**
     * 获取老师数量
     *
     * @param jsonHandler
     */
    public static void GetVideoNews(JSONHandler jsonHandler) {
        post(AppUrl.GetVideoNews, null, jsonHandler);
    }

    /**
     * 获取老师列表
     *
     * @param jsonHandler
     */
    public static void GetTeachers(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        post(AppUrl.GetTeachers, param, jsonHandler);
    }

    /**
     * 获取我订阅的
     *
     * @param jsonHandler
     */
    public static void GetPayVideoLog(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        post(AppUrl.GetPayVideoLog, param, jsonHandler);
    }

    /**
     * 获取视频详情
     *
     * @param video_id
     * @param jsonHandler
     */
    public static void GetVideoDetail(String video_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("video_id", video_id);
        post(AppUrl.LoadVideo, param, jsonHandler);
    }

    /**
     * 获取视频专辑
     *
     * @param jsonHandler
     */
    public static void Series(String series_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("series_id", series_id);
        post(AppUrl.Series, param, jsonHandler);
    }

    /**
     * 获取视频专辑
     *
     * @param jsonHandler
     */
    public static void Series(String series_id, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("series_id", series_id);
        param.put("page", page + "");
        post(AppUrl.Series, param, jsonHandler);
    }

    /**
     * 获取推荐视频
     *
     * @param jsonHandler
     */
    public static void Recommend(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        post(AppUrl.Recommend, param, jsonHandler);
    }

    /**
     * 获取推荐视频
     *
     * @param jsonHandler
     */
    public static void StudyRecommend(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("per_page", "4");
        param.put("choiceness", "1");
        post(AppUrl.Recommend, param, jsonHandler);
    }

    /**
     * 获取视频标签
     *
     * @param jsonHandler
     */
    public static void GetFilter(JSONHandler jsonHandler) {
        post(AppUrl.GetFilter, null, jsonHandler);
    }

    /**
     * 发送消息到视频聊天
     *
     * @param video_id
     * @param content
     */
    public static void SendVideoMessage(String video_id, String content, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("video_id", video_id);
        param.put("content", content);
        post(AppUrl.sendToVideo, param, jsonHandler);
    }

    public static void getVideoMsg(String video_id, String time, int after, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("video_id", video_id);
        param.put("time", time);
        param.put("after", after + "");
        post(AppUrl.getVideoMsg, param, jsonHandler);
    }

    /**
     * 订阅视频
     *
     * @param is_series
     * @param id
     * @param jsonHandler
     */
    public static void PayVideo(int is_series, String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("is_series", is_series + "");
        param.put("id", id);
        post(AppUrl.PayVideo, param, jsonHandler);
    }

    /**
     * 获取购买信息
     *
     * @param id
     * @param jsonHandler
     */
    public static void PayVideoNews(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("video_id", id);
        post(AppUrl.PayVideoNews, param, jsonHandler);
    }

    //邀请列表
    public static void getInviteList(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        walle_get(AppUrl.INVITELIST, param, jsonHandler);
    }

    /**
     * 铁粉优惠规则
     *
     * @param id
     * @param take_id
     * @param product_id
     * @param point
     * @param jsonHandler
     */
    public static void ruleCouponFans(String id, int take_id, int product_id, int point, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("id", id);
        param.put("take_id", String.valueOf(take_id));
        param.put("product_id", String.valueOf(product_id));
        param.put("point", String.valueOf(point));
        post(AppUrl.ruleCouponFans, param, jsonHandler);
    }

    /**
     * 搜索圈子
     *
     * @param key
     * @param jsonHandler
     */
    public static void searchGroup(String key, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("keyword", key);
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        get(AppUrl.SEARCH, param, jsonHandler);
    }

    /**
     * 首页活动
     *
     * @param jsonHandler
     */
    public static void index(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        get(AppUrl.INDEX, param, jsonHandler);
    }

    public static void coupon(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        get(AppUrl.COUPON, param, jsonHandler);
    }

    /**
     * 获取热门股票标签
     *
     * @param jsonHandler
     */
    public static void getStockTag(JSONHandler jsonHandler) {
        get(AppUrl.HOT_STOCKS_TAG, null, jsonHandler);
    }

    /**
     * 获取热门股票标签
     *
     * @param jsonHandler
     */
    public static void sendAskStock(String stock, String content, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("symbol", stock);
        param.put("content", content);
        post(AppUrl.SEND_ASK_STOCK, param, jsonHandler);
    }

    /**
     * 获取问股列表
     *
     * @param stock
     * @param page
     * @param jsonHandler
     */
    public static void askList(String stock, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("symbol", stock);
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        get(AppUrl.ASK_LIST, param, jsonHandler);
    }

    /**
     * 获取股票产品列表
     *
     * @param jsonHandler
     */
    public static void getScreenProducts(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        walle_get(AppUrl.getScreenProducts, param, jsonHandler);
    }

    /**
     * 购买股票产品
     *
     * @param jsonHandler
     */
    public static void PayScreen(String screen_id, String price_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("screen_id", screen_id);
        param.put("price_id", price_id);
        walle_get(AppUrl.PayScreen, param, jsonHandler);
    }

    /**
     * 股票产品
     *
     * @param jsonHandler
     */
    public static void loadScreenProduct(String screen_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
//        if(!TextUtils.isEmpty( SettingDefaultsManager.getInstance().authToken())){
//        }
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("screen_id", screen_id);
        walle_get(AppUrl.loadScreenProduct, param, jsonHandler);
    }

    /**
     * 获取股票产品消息
     *
     * @param jsonHandler
     */
    public static void getNotices(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", page + "");
        param.put("per_page", Constants.PER_PAGE + "");
        walle_get(AppUrl.getNotices, param, jsonHandler);
    }

    /**
     * 获取比赛消息
     *
     * @param jsonHandler
     */
    public static void getPlayer(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("match_id", Constants.MATCH_ID);
        param.put("user_id", SettingDefaultsManager.getInstance().UserId());
        match_post(AppUrl.getPlayer, param, jsonHandler);
    }

    public static void getMatchPlayer(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("match_id", Constants.MATCH_ID);
        param.put("user_id", SettingDefaultsManager.getInstance().UserId());
        match_post1(AppUrl.MATCH_PLAYER, param, jsonHandler);
    }

    /**
     * 获取比赛公告
     *
     * @param jsonHandler
     */
    public static void getAnnouncements(JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("match_id", Constants.MATCH_ID);
        param.put("page", "1");
        param.put("per_page", String.valueOf(1));
        match_post1(AppUrl.MATCH_NOTICE/*getAnnouncements*/, param, jsonHandler);
    }

    /**
     * 获取比赛公告
     *
     * @param jsonHandler
     */
    public static void getAnnouncements(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("match_id", Constants.MATCH_ID);
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(Constants.PER_PAGE));
        match_post(AppUrl./*MATCH_NOTICE*/getAnnouncements, param, jsonHandler);
    }

    //选股大赛周期列表
    public static void getSelectStockYieldList(JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        walle_post(AppUrl.SELECT_STOCK_YIELD_LIST, mParams, jsonHandler);
    }
    //下次选股详情
    public static void nextSelectStockDetail(JSONHandler jsonHandler){
        Map<String,String> map=new HashMap<>();
        walle_post(AppUrl.NEXT_SELECT_STOCK_DETAIL,map,jsonHandler);
    }
    //提交选股
    public static void submitStock(String symbol,String stockReason,JSONHandler jsonHandler){
        Map<String,String> map=new HashMap<>();
        map.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        map.put("symbol",symbol);
        map.put("reason",stockReason);
        walle_post(AppUrl.SUBMIT_STOCK,map,jsonHandler);
    }
    //获取选股的周排行榜
    public static void getWeekRankList(String round_id, JSONHandler jsonHandler) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(round_id)) {
            map.put("round_id", round_id);
        }
        walle_post(AppUrl.STOCK_WEEK_RANK_LIST, map, jsonHandler);
    }

    //获取用户选股的记录
    public static void getUserSelectStockRecord(String userID,String page, JSONHandler jsonHandler) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userID);
        map.put("page",page);
//        walle_post(AppUrl.USER_SELECT_STOCK_LIST,map,jsonHandler);
        zbmf_post(AppUrl.USER_SELECT_STOCK_LIST,map,jsonHandler);
    }

    /**
     * 获取持仓数据
     *
     * @param page
     * @param jsonHandler
     */
    public static void getsComment(int page, String contract_id, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        if (contract_id != null) {
            mParams.put("contract_id", contract_id);
        }
        mParams.put("page", String.valueOf(page));
        mParams.put("per_page", String.valueOf(Constants.PER_PAGE));
        match_post(AppUrl.getsComment, mParams, jsonHandler);
    }

    /**
     * 高手持仓
     *
     * @param jsonHandler
     */
    public static void traderHolds(String id, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("user_id", id);
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        match_post(AppUrl.traderHolds, mParams, jsonHandler);
    }


    /**
     * 获取持仓数据
     *
     * @param jsonHandler
     */
    public static void getHoldlist(JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("user_id", SettingDefaultsManager.getInstance().UserId());
        mParams.put("page", String.valueOf(1));
        mParams.put("per_page", String.valueOf(10));
        mParams.put("hide", "1");
        match_post(AppUrl.getHoldlist, mParams, jsonHandler);
    }

    /**
     * 获取持仓数据
     *
     * @param page
     * @param jsonHandler
     */
    public static void getHoldlist(int page, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("user_id", SettingDefaultsManager.getInstance().UserId());
        mParams.put("page", String.valueOf(page));
        mParams.put("per_page", String.valueOf(Constants.PER_PAGE));
        mParams.put("hide", "1");
        match_post(AppUrl.getHoldlist, mParams, jsonHandler);
    }

    public static void getHoldlist1(JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("user_id", SettingDefaultsManager.getInstance().UserId());
        mParams.put("page", String.valueOf(1));
        mParams.put("per_page", String.valueOf(10));
        mParams.put("hide", "1");
        match_post1(AppUrl.HOLDER_LIST/*getHoldlist*/, mParams, jsonHandler);
    }

    public static void getHoldlist2(int page, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("user_id", SettingDefaultsManager.getInstance().UserId());
        mParams.put("page", String.valueOf(page));
        mParams.put("per_page", String.valueOf(10));
        mParams.put("hide", "1");
        match_post1(AppUrl.HOLDER_LIST/*getHoldlist*/, mParams, jsonHandler);
    }

    public static void addComment(String contract_id, String message, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("desc", message);
        mParams.put("contract_id", contract_id);
        match_post(AppUrl.addComment, mParams, jsonHandler);
    }

    public static void dealSys(String match_id, int page, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("match_id", match_id);
        mParams.put("page", String.valueOf(page));
        mParams.put("per_page", String.valueOf(Constants.PER_PAGE));
        match_post(AppUrl./*LASTLY_DEALS*/dealSys, mParams, jsonHandler);
    }

    public static void getYieldList(String order, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("page", String.valueOf(1));
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("order", order);
        mParams.put("per_page", "10");//每页加载10条数据
        match_post1(AppUrl.RANK_LIST/*getYieldList*/, mParams, jsonHandler);
    }

    public static void getYieldList1(String page, String order, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("page", page);
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("order", order);
        mParams.put("per_page", "10");//每页加载10条数据
        match_post1(AppUrl.RANK_LIST/*getYieldList*/, mParams, jsonHandler);
    }

    public static void traderRanks(JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        match_post(AppUrl.traderRanks, mParams, jsonHandler);
    }

    public static void traderInfo(String id, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("user_id", id);
        match_post(AppUrl.traderInfo, mParams, jsonHandler);
    }

    public static void traders_buy(String id, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("user_id", id);
        match_post(AppUrl.traders_buy, mParams, jsonHandler);
    }

    public static void traderDeals(String id, int page, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("user_id", id);
        mParams.put("page", String.valueOf(page));
        mParams.put("per_page", String.valueOf(Constants.PER_PAGE));
        match_post(AppUrl.traderDeals, mParams, jsonHandler);
    }

    public static void TrackList(JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        match_post(AppUrl.TrackList, mParams, jsonHandler);
    }

    //重置比赛
    public static void resetMatch(JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("match_id", Constants.MATCH_ID);
        match_post1(AppUrl.RESET_MATCH/*resetMatch*/, mParams, jsonHandler);
    }

    public static void getStockRealtimeInfo(String symbols, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("symbols", symbols);
        mParams.put("full", "1");
        match_post(AppUrl.getStockRealtimeInfo, mParams, jsonHandler);
    }

    public static void getRealtimeInfo(String symbols, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("symbols", symbols);
        mParams.put("full", "1");
        mParams.put("id", Constants.MATCH_ID);
        match_post(AppUrl.getRealtimeInfo, mParams, jsonHandler);
    }

    public static void buyMatchStock(String symbol, String price, String volumn, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("symbol", symbol);
        mParams.put("price", price);
        mParams.put("volumn", volumn);
        mParams.put("match_id", Constants.MATCH_ID);
        match_post1(AppUrl.STOCK_BUY/*buy*/, mParams, jsonHandler);
    }

    public static void sellMatchStock(String symbol, String price, String volumn, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("symbol", symbol);
        mParams.put("price", price);
        mParams.put("volumn", volumn);
        mParams.put("match_id", Constants.MATCH_ID);
        match_post(AppUrl.sell, mParams, jsonHandler);
    }

    public static void getOrderList(int page, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("page", String.valueOf(page));
        mParams.put("per_page", String.valueOf(Constants.PER_PAGE));
        match_post1(AppUrl.TRUST_LIST/*getOrderList*/, mParams, jsonHandler);
    }

    public static void getDeallogList(int page, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("page", String.valueOf(page));
        mParams.put("per_page", String.valueOf(Constants.PER_PAGE));
        mParams.put("user_id", SettingDefaultsManager.getInstance().UserId());
//        mParams.put("hide","1");
        match_post1(AppUrl.DEALS_RECORD/*getDeallogList*/, mParams, jsonHandler);
    }

    public static void withdraw(String id, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("id", id);
        mParams.put("match_id", Constants.MATCH_ID);
        match_post1(AppUrl.WITH_DRAW/*withdraw*/, mParams, jsonHandler);
    }

    public static void getWinRecords(int page, JSONHandler jsonHandler) {
        Map<String, String> mParams = new HashMap<>();
        mParams.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        mParams.put("match_id", Constants.MATCH_ID);
        mParams.put("user_id", SettingDefaultsManager.getInstance().UserId());
        mParams.put("page", String.valueOf(page));
        mParams.put("per_page", String.valueOf(Constants.PER_PAGE));
        match_post1(AppUrl.RECORD/*getWinRecords*/, mParams, jsonHandler);
    }

    public static void vote(String month, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("month", month);
        get(AppUrl.vote, param, jsonHandler);
    }

    public static void userVote(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        get(AppUrl.userVote, param, jsonHandler);
    }

    /**
     * 获取金股金句
     *
     * @param jsonHandler
     */
    public static void getDictumNums(JSONHandler jsonHandler) {
        get(AppUrl.getDictumNums, null, jsonHandler);
    }

    public static void getDictumByIds(String id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("dictum_ids", id);
        get(AppUrl.getDictumByIds, param, jsonHandler);
    }

    /**
     * 获取模型选股产品列表
     */
    public static void modelProduct(JSONHandler jsonHandler) {
        walle_get(AppUrl.modelProduct, null, jsonHandler);
    }

    public static void modelList(String product_id, String date, String time, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("product_id", product_id);
        param.put("date", date);
        if (time != null) {
            param.put("time", time);
        }
        walle_get(AppUrl.modelList, param, jsonHandler);
    }

    /**
     * 模型选股-股票列表（新增2天数据+自定义排序）
     */
    public static void getNewModelList(String product_id, String sort_key, String sort_order, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("product_id", product_id);
        param.put("sort_key", sort_key);
        param.put("sort_order", sort_order);
        walle_get(AppUrl.newModelList, param, jsonHandler);
    }


    /**
     * 互动列表
     *
     * @param symbol
     * @param page
     * @param jsonHandler
     */
    public static void stockAskList(String symbol, int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        if (!TextUtils.isEmpty(symbol)) {
            param.put("symbol", symbol);
        }
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(Constants.PER_PAGE));
        stockGet(AppUrl.STOCK_ASK_LIST, param, jsonHandler);
    }

    /**
     * 获取相应的董秘信息
     * @param symbol 股票代码
     * @param jsonHandler
     */
    public static void getCompanySecretaryInfo(String symbol,JSONHandler jsonHandler){
        Map<String, String> param = new HashMap<>();
//        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("symbol",symbol);
        stockPost(AppUrl.DONG_MI_INFO,param,jsonHandler);
    }

    /**
     * 互动列表
     *
     * @param page
     * @param jsonHandler
     */
    public static void userList(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(Constants.PER_PAGE));
        stockGet(AppUrl.USER_LIST, param, jsonHandler);
    }

    /**
     * 向董秘 提问
     *
     * @param symbol
     * @param content
     * @param jsonHandler
     */
    public static void stockAsk(String symbol, String content, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("symbol", symbol);
        param.put("content", content);
        stockGet(AppUrl.STOCK_ASK, param, jsonHandler);
    }

    public static void reMind(String position, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        if (position != null) {
            param.put("position", position);
        }
        stockGet(AppUrl.REMIND, param, jsonHandler);
    }

    public static void stockList(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(Constants.PER_PAGE));
        stockGet(AppUrl.STOCK_LIST, param, jsonHandler);
    }

    public static void tagList(int page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(Constants.PER_PAGE));
        stockGet(AppUrl.tagList, param, jsonHandler);
    }

    public static void stockList(int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(per_page));
        stockGet(AppUrl.STOCK_LIST, param, jsonHandler);
    }

    public static void tagList(int page, int per_page, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(per_page));
        stockGet(AppUrl.tagList, param, jsonHandler);
    }

    public static void noticeList(int page, int tag_type, String symbol, String tag_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        if (tag_type == 1) {
            param.put("tag_type", String.valueOf(tag_type));
            if (symbol != null) {
                param.put("symbol", symbol);
            }
        } else if (tag_type == 2) {
            param.put("tag_type", String.valueOf(tag_type));
            if (tag_id != null) {
                param.put("tag_id", tag_id);
            }
        }
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(Constants.PER_PAGE));
        stockGet(AppUrl.noticeList, param, jsonHandler);
    }

    /**
     * 添加个股
     *
     * @param symbol
     * @param jsonHandler
     */
    public static void addStock(String symbol, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("symbol", symbol);
        stockGet(AppUrl.addStock, param, jsonHandler);
    }

    /**
     * 添加关键词
     *
     * @param tag_name
     * @param jsonHandler
     */
    public static void addTag(String tag_name, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("tag_name", tag_name);
        stockGet(AppUrl.addTag, param, jsonHandler);
    }

    /**
     * 删除关键词
     *
     * @param tag_id
     * @param jsonHandler
     */
    public static void delTag(String tag_id, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("tag_id", tag_id);
        stockGet(AppUrl.delTag, param, jsonHandler);
    }

    /**
     * 删除个股
     *
     * @param symbol
     * @param jsonHandler
     */
    public static void delStock(String symbol, JSONHandler jsonHandler) {
        Map<String, String> param = new HashMap<>();
        param.put("auth_token", SettingDefaultsManager.getInstance().authToken());
        param.put("symbol", symbol);
        stockGet(AppUrl.delStock, param, jsonHandler);
    }
}
package com.zbmf.StocksMatch.api;

import com.loopj.android.http.RequestParams;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.worklibrary.util.Logx;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xuhao
 * on 2017/11/22.
 */
class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}

public class SendParam {
    private static Map<String, String> getTokenMap(String token) {
        Map<String, String> map = new HashMap<>();
        map.put(ParamsKey.TOKEN, token);
        return map;
    }

    private static Map<String, String> getAuthTokenMap() {
        Map<String, String> map = new HashMap<>();
        map.put(ParamsKey.AUTH_TOKEN, SharedpreferencesUtil.getInstance().getString(SharedKey.AUTH_TOKEN, ""));
        return map;
    }

    public static Map<String, String> getRequest(String method, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (method != null) {
            map.put(ParamsKey.METHOD, method);
        }
        return getRequest(map);
    }

    public static Map<String, String> getRequest(Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(ParamsKey.API_KEY, HostUrl.API_KEY);
        map.put(ParamsKey.DEVICE_TYPE, HostUrl.DEVICE_TYPE);
        map = sortMapByKey(map);
        String api_sig = HostUrl.API_SECRET;
        for (String key : map.keySet()) {
            api_sig += key + map.get(key);
        }
        String lowerCase = getMD5String(api_sig).toLowerCase();
        map.put(ParamsKey.API_SIG, lowerCase);
        return map;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    private static String getMD5String(String string) {
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

    public static RequestParams getRequest(String method, Map<String, String> map, String path) {
        String api_sig = HostUrl.API_SECRET;
        if (map == null) {
            map = new HashMap<>();
        }
        if (method != null) {
            map.put(ParamsKey.METHOD, method);
        }
        map.put(ParamsKey.API_KEY, HostUrl.API_KEY);
        map.put(ParamsKey.DEVICE_TYPE, HostUrl.DEVICE_TYPE);
        RequestParams params = new RequestParams();
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            map = sortMapByKey(map);
            for (String key : map.keySet()) {
                api_sig += key + map.get(key);
            }
            map.put(ParamsKey.API_SIG, getMD5String(api_sig).toLowerCase());
            for (String key : map.keySet()) {
                params.put(key, map.get(key));
            }
            try {
                params.put(ParamsKey.AVATAR, file);
            } catch (FileNotFoundException e) {
                Logx.e(e.getMessage());
                e.printStackTrace();
            }
        }
        return params;
    }

    /**
     * 登录
     *
     * @param username
     * @param pass
     * @return
     */
    public static Map<String, String> getLoginMap(String username, String pass) {
        Map<String, String> map = new HashMap<>();
        map.put(ParamsKey.USERNAME, username);
        map.put(ParamsKey.PASSWORD, pass);
        map.put(ParamsKey.CLIENT_ID,Constans.CLIENT_ID);
        return getRequest(Method.LOGIN, map);
    }

    /*-------------------------------首页参数-------------------------------*/

    /**
     * 获取证券指数
     */
    public static Map<String, String> getStockIndex() {
        Map<String, String> params = getAuthTokenMap();
        return getRequest(Method.HOME_STOCK_INDEX, params);
    }

    /**
     * 弹窗
     * @param match_id
     * @return
     */
    public static Map<String,String> getPopWindow(String match_id){
        Map<String, String> map = getAuthTokenMap();
//        map.put(ParamsKey.DEVICE_TYPE,"2");
        map.put(ParamsKey.MATCH_ID,match_id);
        return getRequest(Method.POP_WINDOW,map);
    }

    /**
     * 获取学校
     */
    public static Map<String, String> getSchool() {
        Map<String, String> param = getAuthTokenMap();
        return getRequest(Method.HOME_MATCH_SCHOOL, param);
    }

    /**
     * 获取推荐比赛
     */
    public static Map<String, String> getSupremeMatch() {
        Map<String, String> param = getAuthTokenMap();
        return getRequest(Method.HOME_MATCH_RECOMMEND, param);
    }

    /**
     * 搜索博文信息
     * @param blogId
     * @return
     */
    public static Map<String,String> getUserBlogInfo(String blogId){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.BLOG_ID, blogId);
     return getRequest(Method.SEARCH_USER_BLOG_INFO,params);
    }

    /**
     * 获取视频信息
     * @param videoID
     * @return
     */
    public static Map<String,String> getVideoInfo(String videoID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.VIDEO_ID, videoID);
        return getRequest(Method.LOAD_VIDEO,params);
    }
    /**
     * 获取比赛城市
     */
    public static Map<String,String> getMatchCity(){
        Map<String, String> param = getAuthTokenMap();
        return getRequest(Method.HOME_MATCH_CITY,param);
    }
    /**
     * 获取高手的交易记录
     */
    public static Map<String,String> getTraderDealRecord(String user_id,int page,int perPage){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.USER_ID,user_id);
        params.put(ParamsKey.PAGE,String.valueOf(page));
        params.put(ParamsKey.PER_PAGE,String.valueOf(perPage));
        return getRequest(Method.TRADER_DEAL_RECORD,params);
    }
    /**
     * 获取高手的仓盘列表
     */
     public static Map<String,String> getTraderHolderPotionRecord(String user_id){
         Map<String, String> params = getAuthTokenMap();
         params.put(ParamsKey.USER_ID,String.valueOf(user_id));
         return getRequest(Method.TRADER_HOLDER_POSITION_RECORD,params);
     }
    /**
     * 首页广告轮播
     *
     * @return
     */
    public static Map<String, String> getReconpic(String category) {
        Map<String, String> params = getAuthTokenMap();
//        params.put(ParamsKey.DEVICE_TYPE,HostUrl.DEVICE_TYPE);
        params.put(ParamsKey.CATEGORY,category);
        params.put("limit","5");
        return getRequest(Method.HOME_TOP_AD, params);
    }

    public static Map<String, String> getReconpic1() {
        Map<String, String> params = getAuthTokenMap();
//        params.put(ParamsKey.DEVICE_TYPE,HostUrl.DEVICE_TYPE);
        params.put(ParamsKey.CATEGORY,"20");
        params.put("limit","5");
        return getRequest(Method.HOME_TOP_AD, params);
    }

    /**
     * 获取操盘高手
     *
     * @return
     */
    public static Map<String, String> getTraders() {
        Map<String, String> map = getTokenMap(SharedpreferencesUtil.getInstance().getString(SharedKey.AUTH_TOKEN, ""));
        return getRequest(map);
    }

    /**
     * 获取操盘高手排行
     * @return
     */
    public static Map<String,String> getTraderRank(){
        Map<String, String> params = getAuthTokenMap();
        return getRequest(Method.TRADER_RANKS,params);
    }
    /************************************-----比赛-----******************************************/
    /**
     * 搜索比赛
     * @param keyWord
     * @return
     */
    public static Map<String,String> searchMatchFromName(String keyWord,int page,int per_page){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.KEY_WORD,keyWord);
        params.put(ParamsKey.PAGE,String.valueOf(page));
        params.put(ParamsKey.PER_PAGE,String.valueOf(per_page));
        return getRequest(Method.SEARCH_MATCH,params);
    }

    /**
     * 订阅高手续订
     * @param user_id
     * @return
     */
    public static Map<String,String> traderBuy(String user_id){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.USER_ID,user_id);
        return getRequest(Method.TRADER_BUY,params);
    }

    /**
     * 校验验证码
     * @param phone
     * @param code
     * @return
     */
    public static Map<String,String> verifyCode(String phone,String code){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.PHONE,phone);
        params.put(ParamsKey.CLIENT_ID,Constans.CLIENT_ID);
        params.put(ParamsKey.CODE,code);
        return getRequest(Method.VER_CODE,params);
    }

    /**
     * 通知个推后台忘记密码
     * @param phone
     * @return
     */
    public static Map<String,String> forgetPassword(String phone){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.PHONE,phone);
        params.put(ParamsKey.CLIENT_ID,Constans.CLIENT_ID);
        return getRequest(Method.FORGET_PASSWORD,params);
    }

    /**
     * 重置密码
     * @param log_id
     * @param phone
     * @param password
     * @return
     */
    public static Map<String,String> registerPassword(String log_id,String phone,String password){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.CLIENT_ID,Constans.CLIENT_ID);
        params.put(ParamsKey.LOG_ID,log_id);
        params.put(ParamsKey.PHONE,phone);
        params.put(ParamsKey.PASSWORD,password);
        return getRequest(Method.REGISTER_PASSWORD,params);
    }

    /**
     * 注册
     * @param phone
     * @param password
     * @param code
     * @return
     */
    public static Map<String,String> register(String phone,String password,String code){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.CLIENT_ID,Constans.CLIENT_ID);
        params.put(ParamsKey.PHONE,phone);
        params.put(ParamsKey.CODE,code);
        params.put(ParamsKey.PASSWORD,password);
        return getRequest(Method.REGISTER,params);
    }
    /**
     * 发送注册验证码
     * @param phone
     * @return
     */
    public static Map<String,String> sendRegisterCode(String phone){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.PHONE,phone);
        params.put(ParamsKey.CLIENT_ID,Constans.CLIENT_ID);
        return getRequest(Method.REGISTER_CODE,params);
    }

    /**
     * 发送忘记密码验证码
     * @param phone
     * @return
     */
    public static Map<String,String> sendForgetPasswordCode(String phone){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.PHONE,phone);
        params.put(ParamsKey.CLIENT_ID,Constans.CLIENT_ID);
        return getRequest(Method.SEND_FORGET_PASSWORD_CODE,params);
    }
    /**
     * 更新用户昵称
     * @param nickname
     * @return
     */
    public static Map<String,String> upUserNick(String nickname){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.NICK_NAME,nickname);
        return getRequest(Method.UP_NICK,params);
    }
    /**
     * 上传用户图像
     * @param img
     * @return
     */
    public static Map<String,String> upAvatar(String img){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.AVATAR,img);
        return getRequest(Method.UP_AVATAR,params);
    }
    public static RequestParams/*Map<String,String>*/ upAvatar1(String img){
        Map<String, String> params = getAuthTokenMap();
//        params.put(ParamsKey.AVATAR,img);
        return getRequest(Method.UP_AVATAR, params, img);
    }

    /**
     * 获取比赛公告列表
     * @param matchID
     * @param page
     * @return
     */
    public static Map<String,String> getMatchNotice(String matchID,String page){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.MATCH_ID,matchID);
        params.put(ParamsKey.PER_PAGE,String.valueOf(ParamsKey.D_PERPAGE));
        return getRequest(Method.MATCH_NOTICE,params);
    }

    /**
     * 登录微信
     * @param code
     * @return
     */
    public static Map<String,String> loginWeChat(String code){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.CODE,code);
        params.put(ParamsKey.CLIENT_ID,Constans.CLIENT_ID);
        params.put(ParamsKey.IS_MATCH,"1");
        return getRequest(Method.LOGIN_WE_CHAT,params);
    }

    /**
     * 第三方 QQ Sina微博登录
     * @param open_id
     * @param token
     * @param api_type
     * @return
     */
    public static Map<String,String> loginForQQ_Sina(String open_id, String token, String api_type){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.OPEN_ID, open_id);
        params.put(ParamsKey.TOKEN, token);
        params.put(ParamsKey.API_TYPE, api_type);
        params.put(ParamsKey.CLIENT_ID, Constans.CLIENT_ID);
        return getRequest(Method.LOGIN_Q_SINA,params);
    }
    /**
     * 获取股票充值列表
     */
    public static Map<String,String> getPayList(){
        Map<String, String> params = getAuthTokenMap();
        return getRequest(Method.PAY_LIST,params);
    }

    /**
     * 微信支付
     * @param id
     * @param proNum
     * @return
     */
    public static Map<String,String> wxPay(String id,String proNum){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.ID,id);
        if (id.equals("7")){
            params.put(ParamsKey.PRO_NUM,proNum);
        }
        return getRequest(Method.WEIXIN_PAY,params);
    }
    /**
     * 重置比赛
     */
    public static Map<String,String> resetMatch(String matchID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,matchID);
        return getRequest(Method.RESET_MATCH,params);
    }
    /**
     * 获取用户账户余额资金
     */
    public static Map<String,String> getUserWallet(){
        Map<String, String> params = getAuthTokenMap();
        return getRequest(Method.GET_WALLET,params);
    }
    /**
     * 获取交易记录
     */
    public static Map<String,String> getRecordList(String match_id,String page,String userID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,match_id);
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.USER_ID,userID);
        params.put(ParamsKey.PER_PAGE,String.valueOf(ParamsKey.D_PERPAGE));
        return getRequest(Method.RECORD_LIST,params);
    }

    /**
     * 获取用户获奖列表
     * @param matchID
     * @param userID
     * @param page
     * @return
     */
    public static Map<String,String> getUserPrize(String matchID,String userID,String page){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,matchID);
        params.put(ParamsKey.USER_ID,userID);
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.PER_PAGE,String.valueOf(ParamsKey.D_PERPAGE));
        return getRequest(Method.PRIZE_LIST,params);
    }
    /**
     *  撤单
     * @param id
     * @param matchID
     * @return
     */
    public static Map<String,String> revoke(String id,String matchID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,matchID);
        params.put(ParamsKey.ID,id);
        return getRequest(Method.REVOKE,params);
    }
    /**
     * 获取委托列表
     * @param matchID
     * @param page
     * @return
     */
    public static Map<String,String> getOrderList(String matchID,String page){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,matchID);
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.PER_PAGE,String.valueOf(ParamsKey.D_PERPAGE));
        return getRequest(Method.MY_ORDER_LIST,params);
    }

    /**
     * 获取用户参赛
     * @param page
     * @return
     */
    public static Map<String,String> getUserMatch(String page,String userID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.PER_PAGE,"50");
        params.put(ParamsKey.USER_ID, userID);
        return getRequest(Method.USER_MATCH,params);
    }
    /**
     *
     * @param symbols
     * @return
     */
    public static Map<String,String> getStockRealInfo(String symbols){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOLS, symbols);
        params.put("full", "1");
        return getRequest(Method.STOCK_REAL_TIME_INFO,params);
    }
    /**
     * 获取排行榜单
     * @param matchID
     * @param page
     * @param perPage
     * @param order
     * @return
     */
    public static Map<String,String> getMatchRank(String matchID,String page,String perPage,String order){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,matchID);
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.PER_PAGE,perPage);
        params.put(ParamsKey.ORDER,order);
        return getRequest(Method.MATCH_RANK,params);
    }
    /**
     * 最后组装数据
     *
     * @param
     * @return
     */
    public static /*Map<String,String>*/RequestParams getStock(String searchKey) {
        Map<String,String>  map = new HashMap<>();
        map.put("from","mobile");
        map.put("os_ver","1");
        map.put("cuid","xxx");
        map.put("vv","3.2");
        map.put("format","json");
        map.put("query_content",searchKey);
        map.put("asset","0%2C4%2C14");
        long time=new Date().getTime();
        map.put("timestamp",String.valueOf(time));
        map = sortMapByKey(map);
        RequestParams params = new RequestParams();
        for (String key : map.keySet()) {
            params.put(key, map.get(key));
        }
        return params;
    }
    /**
     * 获取比赛列表-城市、高校、券商
     * @param match_org
     * @param page
     * @return
     */
    public static Map<String,String> getMatchList3(int match_org,int page){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ORG,String.valueOf(match_org));
        params.put(ParamsKey.PAGE,String.valueOf(page));
        params.put(ParamsKey.PER_PAGE,String.valueOf(ParamsKey.D_PERPAGE));
        return getRequest(Method.MATCH_LIST_3,params);
    }

    /**
     * 获取最新 或 全部数据
     * @param page
     * @param perPage
     * @param method
     * @return
     */
    public static Map<String,String> getMatchNewAll(int page,int perPage,String method){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.PAGE,String.valueOf(page));
        params.put(ParamsKey.PER_PAGE,String.valueOf(perPage));
        return getRequest(method,params);
    }
    /**
     * 获取比赛详情
     * @param matchID
     * @return
     */
    public static Map<String,String> getMatchDesc(int matchID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,String.valueOf(matchID));
        return getRequest(Method.MATCH_DESC,params);
    }

    /**
     * 申请参赛,获取验证码
     * @param match_id
     * @param mobile
     * @return
     */
    public static Map<String,String> getMatchCode(int match_id,String mobile){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,String.valueOf(match_id));
        params.put(ParamsKey.MOBILE,mobile);
        return getRequest(Method.SEND_CODE,params);
    }

    /**
     * 获取参加比赛的数据
     * @param match_id
     * @return
     */
    public static Map<String,String> getMatchJoin(int match_id,String userID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,String.valueOf(match_id));
        params.put(ParamsKey.USER_ID, userID);
        return getRequest(Method.MATCH_JOIN,params);
    }
    /*------------------------------------------训练---------------------------------------*/
     public static Map<String,String> getStockInfo(String symbols){
         Map<String, String> params = getAuthTokenMap();
         params.put("symbols", symbols);
         params.put("full", "1");
         params.put("id", Constans.MATCH_ID);
         return getRequest(Method.STOCK_TIME_INFO,params);
     }
    /**
     * 买入股票
     * @param symbol
     * @param price
     * @param volumn
     * @return
     */
    public static Map<String,String> buyStock(String symbol, String price, String volumn,String matchId){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOL, symbol);
        params.put(ParamsKey.PRICE, price);
        params.put(ParamsKey.VOLUMN, volumn);
        params.put(ParamsKey.MATCH_ID,matchId);
        return getRequest(Method.NEW_BUY_STOCK,params);
    }

    /**
     * 卖出股票
     * @param symbol
     * @param price
     * @param volumn
     * @return
     */
    public static Map<String,String> sellStock(String symbol, String price, String volumn,String matchID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOL, symbol);
        params.put(ParamsKey.PRICE, price);
        params.put(ParamsKey.VOLUMN, volumn);
        params.put(ParamsKey.MATCH_ID,matchID);
        return getRequest(Method.NEW_SELL_STOCK,params);
    }

    /**
     * 最新交易
     * @param page
     * @param perPage
     * @param matchID
     * @return
     */
     public static Map<String,String> recentDeals(int page,int perPage,String matchID){
         Map<String, String> params = getAuthTokenMap();
         params.put(ParamsKey.PAGE,String.valueOf(page));
         params.put(ParamsKey.PER_PAGE,String.valueOf(perPage));
//         params.put(ParamsKey.MATCH_ID,matchID);
         return getRequest(Method.DEALSYS,params);
     }
    /**
     * 实时更新股票的信息
     * @param symbols
     * @return
     */
    public static Map<String,String> stockRealTimeInfo(String symbols){
        Map<String, String> params = getAuthTokenMap();
        params.put("symbols", symbols);
        params.put("full", "1");
        return getRequest(Method.STOCK_REAL_TIME_INFO,params);
    }
    /**
     * 获取持仓数据
     * @param match_id
     * @return
     */
    public static Map<String,String> getHolderPosition(String page,int match_id,String userId){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.MATCH_ID,String.valueOf(match_id));
        params.put(ParamsKey.USER_ID, userId);
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.PER_PAGE,String.valueOf(ParamsKey.D_PERPAGE));
        //默认分页，从1开始，每页10条数据
        params.put(ParamsKey.HIDE,"1");
        return getRequest(Method.HOLDER_POSITION,params);
    }

    /**
     * 获取比赛列表
     *
     * @return
     */
    public static Map<String, String> getMatchList(int page) {
        Map<String, String> map = getTokenMap(SharedpreferencesUtil.getInstance().getString(SharedKey.AUTH_TOKEN, ""));
        map.put(ParamsKey.PAGE, String.valueOf(page));
        map.put(ParamsKey.PER_PAGE, String.valueOf(Constans.PERPAGE));
        return getRequest(map);
    }

    /**
     * 获取比赛信息
     *
     * @return
     */
    public static Map<String, String> getMatchDetail(String match_id,String userID) {
        Map<String, String> param = getAuthTokenMap();
        param.put(ParamsKey.MATCH_ID, match_id);
        param.put(ParamsKey.USER_ID, userID);
        return getRequest(Method.MATCH_JOIN/*GETPLAYER*/, param);
    }

    /**
     * 操盘高手详情
     *
     * @param traderId
     * @return
     */
    public static Map<String, String> getTraderInfo(String traderId) {
        Map<String, String> param = getAuthTokenMap();
        param.put(ParamsKey.USER_ID, traderId);
        return getRequest(Method.GET_TRADER_INFO, param);
    }

    /**
     * 获取持仓列表
     *
     * @param match_id
     * @return
     */
    public static Map<String, String> getHoldList(String match_id, String user_id) {
        Map<String, String> param = getAuthTokenMap();
        param.put(ParamsKey.MATCH_ID, match_id);
        param.put(ParamsKey.USER_ID, user_id);
        param.put(ParamsKey.PAGE, String.valueOf(1));
        param.put(ParamsKey.PER_PAGE, String.valueOf(10));
        param.put(ParamsKey.HIDE, "1");
        return getRequest(Method.HOLDER_POSITION/*GET_HOLD_LIST*/, param);
    }

    /**
     * 获取用户参加的比赛
     *
     * @return
     */
    public static Map<String, String> getRunMatchList() {
        Map<String, String> param = getAuthTokenMap();
        return getRequest(Method.GET_RUNMATCHS, param);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static Map<String, String> getUserInfo() {
        Map<String, String> param = getAuthTokenMap();
        return getRequest(Method.GET_USERINFO, param);
    }

    /**
     * 获取最新交易
     *
     * @param page
     * @return
     */
    public static Map<String, String> dealSys(int page,String matchID) {
        Map<String, String> param = getAuthTokenMap();
        param.put(ParamsKey.MATCH_ID,matchID);
        param.put(ParamsKey.PAGE, String.valueOf(page));
        param.put(ParamsKey.PER_PAGE, String.valueOf(Constans.PERPAGE));
        return getRequest(Method.RECENT_DEALS/*DEALSYS*/, param);
    }

    /**
     * 报名比赛
     *
     * @param map
     * @return
     */
    public static Map<String, String> getApplyMatch(Map<String, String> map) {
        map.put(ParamsKey.AUTH_TOKEN, SharedpreferencesUtil.getInstance().getString(SharedKey.AUTH_TOKEN, ""));
        return getRequest(Method.VERIFY_CODE, map);
    }
    /*-----------------------------自选股--------------------------------*/
    /**
     * 获取自选股
     *
     * @param page
     * @return
     */
    public static Map<String, String> getFocusList(int page) {
        Map<String, String> param = getAuthTokenMap();
        param.put(ParamsKey.PAGE, String.valueOf(page));
        param.put(ParamsKey.PER_PAGE, String.valueOf(Constans.PERPAGE));
        return getRequest(Method.FOCUS_STOCK_LIST/*GET_FOCUS_LIST*/, param);
    }

    /**
     * 新增自选股
     * @param symbol
     * @param remark
     * @return
     */
    public static Map<String,String> addNewStock(String symbol,String remark){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOL,symbol);
        params.put(ParamsKey.REMARK,remark);
        return getRequest(Method.ADD_STOCK, params);
    }

    /**
     * 获取自选股列表
     * @param symbol
     * @param page
     * @return
     */
    public static Map<String,String> getStockRemarkList(String symbol,String page){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOL,symbol);
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.PER_PAGE,String.valueOf(Constans.PERPAGE));
        return getRequest(Method.STOCK_REMARK_LIST,params);
    }

    /**
     * 删除自选股票
     * @param symbol
     * @return
     */
    public static Map<String,String> delStockItem(String symbol){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOL,symbol);
        return getRequest(Method.DEL_STOCK,params);
    }
    /**
     * 添加自选股备注列表
     * @param symbol
     * @param remark
     * @return
     */
    public static Map<String,String> addStockRemark(String symbol,String remark){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOL,symbol);
        params.put(ParamsKey.REMARK,remark);
        return getRequest(Method.ADD_STOCK_REMARK,params);
    }

    /**
     * 删除自选股备注
     * @param symbol
     * @param remarkID
     * @return
     */
    public static Map<String,String> delStockRemark(String symbol,String remarkID){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOL,symbol);
        params.put(ParamsKey.REMARK_ID,remarkID);
        return getRequest(Method.DEL_STOCK_REMARK,params);
    }

    /**
     * 获取互动列表
     * @param symbol
     * @param page
     * @return
     */
    public static Map<String,String> getAskStockList(String symbol,String page){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.SYMBOL,symbol);
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.PER_PAGE,String.valueOf(ParamsKey.D_PERPAGE));
        return getRequest(Method.STOCK_ASK_LIST,params);
    }

    /**
     * 获取股票的品论信息
     * @param contract_id
     * @param page
     * @return
     */
    public static Map<String,String> getStockComments(String contract_id,String page){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.CONTRACT_ID,contract_id);
        params.put(ParamsKey.PAGE,page);
        params.put(ParamsKey.PER_PAGE,String.valueOf(ParamsKey.D_PERPAGE));
        return getRequest(Method.COMMENTS,params);
    }

    /**
     * 添加股票评论
     * @param symbol
     * @param desc
     * @return
     */
    public static Map<String,String> addStockComments(String symbol,String desc){
        Map<String, String> params = getAuthTokenMap();
        params.put(ParamsKey.CONTRACT_ID,symbol);
        params.put(ParamsKey.DESC,desc);
        return getRequest(Method.ADD_COMMENTS,params);
    }

    /**
     * 比赛详情页底部的广告位
     * @param matchId
     * @return
     */
    public static Map<String,String> getMatchBottomAds(String matchId){
        Map<String, String> map = getAuthTokenMap();
        map.put(ParamsKey.ID,matchId);
        return getRequest(Method.MATCH_BOTTOM_ADS,map);
    }
}


package com.zbmf.StocksMatch.api;

import android.text.TextUtils;
import android.util.Log;

import com.zbmf.StocksMatch.beans.Actives;
import com.zbmf.StocksMatch.beans.Announcement;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.MatchInfo;
import com.zbmf.StocksMatch.beans.PayInfo;
import com.zbmf.StocksMatch.beans.Quotation;
import com.zbmf.StocksMatch.beans.RecommendPic;
import com.zbmf.StocksMatch.beans.Record;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.beans.VersionInfo;
import com.zbmf.StocksMatch.beans.Yield;
import com.zbmf.StocksMatch.utils.GetTime;
import com.zbmf.StocksMatch.utils.JSONFunctions;
import com.zbmf.StocksMatch.utils.UiCommon;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Get2ApiImpl implements Get2Api {

    // 请求服务器的系统参数
    private String v = "1.1";
    private String method = "";
    //	private String timestamp = "";
    private String format = "json";

    private String sign_method = "md5";
    private String access_token = "";
    private String sign = "";

    private static final String API_KEY = "newiph7bafaidke9557430523e00802a";

//	/**
//	 * 替换特殊字符
//	 * @param str
//	 * @return String
//	 */
//	private String strReplace(String str) {
//		return str.replace("'", "\\'").replace("&", "＆").replace("?", "？")
//				.replace("+", "");
//	}


    /**
     * 根据输入参数生成签名，然后组合成访问服务器的地址
     *
     * @param keyList 存储入参的关键字列表
     * @param mParams 存储所有入参数据
     * @param keyName 去掉某个入参关键字，不让其加入条件（输入#表示全部加入）
     * @return StringBuffer
     * @author atan
     */
    private StringBuffer doUrl(List<String> keyList,
                               Map<String, String> mParams, String keyName) {
        String api_sig = "inewph";
        StringBuffer strbuf = new StringBuffer();
        try {
            Iterator<String> it = mParams.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                keyList.add(key);

                api_sig += key + mParams.get(key);
            }

            mParams.put("api_sig", UiCommon.INSTANCE.getMD5String(api_sig));
            Log.e("atan", "-2----" + UiCommon.INSTANCE.getMD5String(api_sig));
            // keyList升序
            Collections.sort(keyList, new Comparator<String>() {
                public int compare(String str1, String str2) {
                    if (str1.compareTo(str2) > 0) {
                        return 1;
                    }
                    if (str1.compareTo(str2) < 0) {
                        return -1;
                    }
                    return 0;
                }
            });

            for (String key : keyList) {
                if (!key.equals(keyName)) {
                    strbuf.append(key);
                    strbuf.append("=");
                    strbuf.append(mParams.get(key));
                    strbuf.append("&");
                }
            }

            strbuf.deleteCharAt(strbuf.length() - 1);
            Log.d("atan", "-3----" + strbuf.toString());
            return strbuf;
        } catch (Exception e) {
            e.printStackTrace();
            return strbuf;
        }
    }


    /**
     * 根据输入参数生成签名，然后组合成访问服务器的地址
     *
     * @param keyList 存储入参的关键字列表
     * @param mParams 存储所有入参数据
     * @param keyName 去掉某个入参关键字，不让其加入条件（输入#表示全部加入）
     * @return StringBuffer
     * @author atan
     */
    private synchronized List<NameValuePair> doUrlValuePair(List<String> keyList,
                                               Map<String, String> mParams, String keyName) {
        String api_sig = "inewph";
        try {
            Iterator<String> it = mParams.keySet().iterator();
            while(it.hasNext()){
                String key = it.next();
                keyList.add(key);
            }

            Collections.sort(keyList, new Comparator<String>() {
                public int compare(String str1, String str2) {
                    if (str1.compareTo(str2) > 0) {
                        return 1;
                    }
                    if (str1.compareTo(str2) < 0) {
                        return -1;
                    }
                    return 0;
                }
            });

            for (String key : keyList) {
                api_sig += key + mParams.get(key);

            }
            mParams.put("api_sig", UiCommon.INSTANCE.getMD5String(api_sig));//服务器端特殊加密参数
//            StringBuffer suf = new StringBuffer();
            List<NameValuePair> mops = new ArrayList<NameValuePair>();
            for(String key : mParams.keySet()){
                mops.add(new BasicNameValuePair(key, mParams.get(key)));
//                suf.append(key+"="+mParams.get(key)).append(",");
//                Log.e("tag","========"+suf.toString());
            }
            return mops;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 把服务器需要的固定参数整合
     *
     * @author kubo
     */
    private Map<String, String> doParams() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("method", method);
        mParams.put("api_key", API_KEY);
        if(UiCommon.INSTANCE.getiUser()!=null)
            if (UiCommon.INSTANCE.getiUser().getAuth_token() == null ||
                    UiCommon.INSTANCE.getiUser().getAuth_token().length() == 0)
                mParams.put("auth_token", access_token);
            else
                mParams.put("auth_token", UiCommon.INSTANCE.getiUser().getAuth_token());
        else
            mParams.put("auth_token", access_token);
        return mParams;
    }


    @Override
    public User Login(String user_name, String pass_wrod) throws JSONException {
        String url = GenApiHashUrl.loginUrl+"/account/auth2/";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
        mParams.put("username", user_name);
        mParams.put("password", pass_wrod);
        String api_sig = "inewph";
        Iterator<String> it = mParams.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            keyList.add(key);
        }
        Collections.sort(keyList, new Comparator<String>() {
            public int compare(String str1, String str2) {
                if (str1.compareTo(str2) > 0) {
                    return 1;
                }
                if (str1.compareTo(str2) < 0) {
                    return -1;
                }
                return 0;
            }
        });
        for (String key : keyList) {
            api_sig += key + mParams.get(key);
        }
        mParams.put("api_sig", UiCommon.INSTANCE.getMD5String(api_sig));
        List<NameValuePair> mops = new ArrayList<NameValuePair>();
        for(String key : mParams.keySet()){
            mops.add(new BasicNameValuePair(key, mParams.get(key)));
        }
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(url, mops);
        if (jsonStr == null) return null;
        return JSONFunctions.Login(jsonStr, method);

    }

    @Override
    public General getPhonecode(String mobile) throws JSONException{
        method = "mcc.users.phonecode";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
        mParams.put("mobile",mobile);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;

        JSONObject json = new JSONObject(jsonStr);

        return JSONFunctions.getJson(json, method);

    }

    @Override
    public User signphone(String mobile, String password, String code) throws JSONException {
        method = "mcc.users.signphone";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
        mParams.put("mobile",mobile);
        mParams.put("password",password);
        mParams.put("code",code);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;

        return JSONFunctions.Login(jsonStr, method);
    }

    @Override
    public User getuserinfo2(String auth_token) throws JSONException {
        method = "mcc.users.getuserinfo2";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
        mParams.put("auth_token",auth_token);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;

        return JSONFunctions.Login(jsonStr, method);
    }

    /**
     * 会员OAUTH登出
     *
     * @return General
     * @author atan
     */
    @Override
    public General Logont() throws JSONException, WSError {
        method = "linkea.user.oauth.logout";
//		List<String> keyList = new ArrayList<String>();
//		Map<String, String> mParams = doParams();
//
//		StringBuffer jsonBuf = doUrl(keyList, mParams, "");
//		String jsonStr = GenApiHashUrl.getInstance().Http_Get(
//				jsonBuf.toString());
//		if(jsonStr==null) return null;
//		JSONObject json = new JSONObject(jsonStr);
//		return JSONFunctions.jsonGet(json,method);
        return null;
    }

    @Override
    public MatchBean getRecommendMatch() throws JSONException {
        method = "mcc.match.supremeMatch";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("method", "mcc.match.supremeMatch");
//        mParams.put("api_key", API_KEY);
//        mParams.put("auth_token", UiCommon.INSTANCE.getiUser().getAuth_token());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;

        JSONObject json = new JSONObject(jsonStr);

        return JSONFunctions.getRecommendMatch(json, method);
    }

    @Override
    public RecommendPic getRecommendPic() throws JSONException {
        method = "mcc.match.recommend";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("method", "mcc.match.recommend");
//        mParams.put("api_key", API_KEY);
//        mParams.put("auth_token", UiCommon.INSTANCE.getiUser().getAuth_token());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;

        JSONObject json = new JSONObject(jsonStr);

        return JSONFunctions.getRecommendPic(json, method);
    }

    @Override
    public MatchBean getRunMatches() throws JSONException {

        method = "mcc.match.getRunMatchs";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();

//        mParams.put("auth_token", UiCommon.INSTANCE.getiUser().getAuth_token());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;

        JSONObject json = new JSONObject(jsonStr);

        return JSONFunctions.getRunMatches(json, method);
    }

    @Override
    public MatchBean getRunMatches(String user_id) throws JSONException {
        method = "mcc.match.getRunMatchs";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();

        mParams.put("user_id", user_id);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;

        JSONObject json = new JSONObject(jsonStr);

        return JSONFunctions.getRunMatches(json, method);
    }

    @Override
    public MatchInfo getMatchMessage(String match_id,String user_id) throws JSONException {
        method = "mcc.match.getPlayer";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();

        mParams.put("match_id", match_id);
        mParams.put("user_id",  user_id);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getMatchMessage(json, method);
    }

    @Override
    public MatchBean getMathNoStopList(int page,int per_page) throws JSONException {
        method = "mcc.match.getMathNoStopList";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("page",String.valueOf(page));
        mParams.put("per_page",String.valueOf(per_page));

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getMatchNoStopList(json, method);
    }

    @Override
    public General getVerifyCode(String match_id, String truename, String mobile) throws JSONException {
        method = "mcc.match.sendCode";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",match_id);
        mParams.put("truename",truename);
        mParams.put("mobile",mobile);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getVerifyCode(json, method);
    }

    @Override
    public General checkInviteCode(String match_id, String invite_code) throws JSONException {
        method = "mcc.match.CheckInvites";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",match_id);
        mParams.put("invite_code",invite_code);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }

    @Override
    public MatchBean applyMatch(String match_id, String truename, String mobile, String code) throws JSONException {
        method = "mcc.match.verifyCode";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",match_id);
        mParams.put("truename",truename);
        mParams.put("code",code);
        mParams.put("mobile",mobile);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.applyMatch(json, method);
    }

    @Override
    public Quotation getFocusList() throws JSONException {
        method = "mcc.stocks.getFocusList";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return  JSONFunctions.getFocusList(json, method);
    }

    @Override
    public Quotation getStockRealtimeInfo2() throws JSONException {
        method = "mcc.stocks.getRealtimeInfo2";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getStockRealtimeInfo2(json, method);
    }

    @Override
    public Stock getStockRealtimeInfo(String symbols) throws JSONException {
        method = "mcc.match.getStockRealtimeInfo";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("symbols", symbols);
        mParams.put("full", "1");

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getStockRealtimeInfo(json, method);
    }

    @Override
    public Stock getRealtimeInfo(String id, String symbols) throws JSONException {
        method = "mcc.match.getRealtimeInfo";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("symbols", symbols);
        mParams.put("full", "1");
        if(!TextUtils.isEmpty(id))
             mParams.put("id", id);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getStockRealtimeInfo(json, method);
    }


    @Override
    public General focus2(String symbol) throws JSONException {
        method = "mcc.stocks.focus2";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("symbol", symbol);


        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }

    @Override
    public General defocus(String symbol) throws JSONException {
        method = "mcc.stocks.defocus";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("id",  UiCommon.INSTANCE.getiUser().getUser_id()+"-"+symbol);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }

    @Override
    public User searchUsers(String keyword) throws JSONException {
        method = "mcc.match.searchUsers";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("keyword", keyword);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.searchUsers(json, method);
    }

    @Override
    public MatchBean searchMatch(String keyword) throws JSONException {
        method = "mcc.match.searchMatch";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("keyword", keyword);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getRunMatches(json, method);
    }

    @Override
    public Actives getActives() throws JSONException {
        method = "mcc.match.actives";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.actives(json, method);
    }

    @Override
    public Group getUserGroups(int page, int per_page) throws JSONException {
        method = "mcc.groups.getUserGroups";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("page",String.valueOf(page));
        mParams.put("per_page",String.valueOf(per_page));

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getUserGroups(json, method);
    }



    @Override
    public General groups_quit(String group_id,String method) throws JSONException {
        this.method = "mcc.groups."+method;
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("id",  group_id);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }

    @Override
    public User UserMore() throws JSONException {
        method = "mcc.match.UserMore";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.UserMore(json, method);
    }

    @Override
    public General suggests(String content) throws JSONException {
        method = "mcc.suggests.post";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("content",  content);
        if(!TextUtils.isEmpty(UiCommon.INSTANCE.getiUser().getPhone()))
            mParams.put("phone",  UiCommon.INSTANCE.getiUser().getPhone());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }

    @Override
    public Record getWinRecords(String user_id, String match_id, int page, int per_page) throws JSONException {
        method = "mcc.match.getWinRecords";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("user_id",user_id);
        mParams.put("match_id",match_id);
        mParams.put("page",String.valueOf(page));
        mParams.put("per_page",String.valueOf(per_page));

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getWinRecords(json, method);
    }

    @Override
    public Announcement getAnnouncements(String match_id, int page, int per_page) throws JSONException {
        method = "mcc.match.getAnnouncements";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",match_id);
        mParams.put("page",String.valueOf(page));
        mParams.put("per_page",String.valueOf(per_page));

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getAnnouncements(json, method);
    }

    @Override
    public Yield getYieldList(String match_id,String order, int page, int per_page) throws JSONException {
        method = "mcc.match.getYieldList";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",match_id);
        mParams.put("page",String.valueOf(page));
        mParams.put("per_page",String.valueOf(per_page));
        mParams.put("order",order);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");

        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getYieldList(json, method);
    }

    @Override
    public StockholdsBean getHoldlist(String match_id, String user_id, int page, int per_page) throws JSONException {
        method = "mcc.match.getHoldlist";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",match_id);
        mParams.put("user_id",user_id);
        mParams.put("page",String.valueOf(page));
        mParams.put("per_page",String.valueOf(per_page));
        mParams.put("hide","1");

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");

        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getHoldlist(json, method);
    }

    @Override
    public StockholdsBean getOrderList(String match_id, int page, int per_page) throws JSONException {
        method = "mcc.match.getOrderList";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",match_id);
        mParams.put("page",String.valueOf(page));
        mParams.put("per_page",String.valueOf(per_page));
        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");

        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getOrderList(json, method);//2
    }


    @Override
    public StockholdsBean getDeallogList(String match_id, String user_id,int page, int per_page) throws JSONException {
        method = "mcc.match.getDeallogList";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",match_id);
        mParams.put("page",String.valueOf(page));
        mParams.put("per_page",String.valueOf(per_page));
        mParams.put("user_id",user_id);
        mParams.put("hide","1");
        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");

        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getOrderList(json, method);//1
    }

    @Override
    public User UserInfo(String user_id) throws JSONException {
        method = "mcc.match.UserInfo";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("user_id",String.valueOf(user_id));

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.UserInfo(json, method);
    }

    @Override
    public General buy(String symbol, String price, String volumn, String match_id) throws JSONException {
        method = "mcc.match.buy";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("symbol",  symbol);
        mParams.put("price",  price);
        mParams.put("volumn",  volumn);
        mParams.put("match_id",  match_id);


        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }

    @Override
    public General sell(String symbol, String price, String volumn, String match_id) throws JSONException {
        method = "mcc.match.sell";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("symbol",  symbol);
        mParams.put("price",  price);
        mParams.put("volumn",  volumn);
        mParams.put("match_id",  match_id);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }

    @Override
    public User iconupload(String nickname, String avatar) throws JSONException{
        method = "mcc.users.iconupload";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("nickname",  nickname);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.iconupload(json, method);
    }

    @Override
    public PayInfo PayStockTem() throws JSONException {
        method = "mcc.match.PayStockTem";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.PayStockTem(json, method);
    }

    @Override
    public General PayStock(String user_id,String match_id,String symbol) throws JSONException {
        method = "mcc.match.PayStock";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("symbol",  symbol);
        mParams.put("user_id",  user_id);
        mParams.put("type",  "1");
        mParams.put("match_id",  match_id);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }

    @Override
    public String getAccessTokenByOpenapi(String token, String openid, String api_type) throws JSONException {
        method = "mcc.users.getAccessTokenByOpenapi";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
        mParams.put("token",  token);
        mParams.put("openid",  openid);
        mParams.put("api_type",  api_type);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return jsonStr;
    }

    @Override
    public String matchWechat(String code) throws JSONException {
        method = "mcc.users.matchWechat";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
        mParams.put("code",  code);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return jsonStr;
    }

    @Override
    public General withdraw(String match_id, String id) throws JSONException {
        method = "mcc.match.withdraw";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("match_id",  match_id);
        mParams.put("id",  id);

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.getJson(json, method);
    }


    @Override
    public VersionInfo version() throws JSONException {
        String url="https://center.zbmf.com/rest/json/";
        method = "zbmf.version.vers";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
        mParams.put("category","android_match");
        mParams.put("device_type","1");
//        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(url,jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.version(json, method);
    }

    @Override
    public Stock updateDB() throws JSONException {
        method = "mcc.stocks.version";
        List<String> keyList = new ArrayList<String>();
        Map<String, String> mParams = doParams();
        mParams.put("auth_token",  UiCommon.INSTANCE.getiUser().getAuth_token());
        mParams.put("updated", GetTime.format.format(new Date()));

        List<NameValuePair> jsonBuf = doUrlValuePair(keyList, mParams, "");
        String jsonStr = GenApiHashUrl.getInstance().Http_Post(jsonBuf);
        if (jsonStr == null) return null;
        JSONObject json = new JSONObject(jsonStr);
        return JSONFunctions.updateDB(json, method);
    }
}
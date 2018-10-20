package com.zbmf.StocksMatch.utils;


import android.content.Context;

import com.zbmf.StocksMatch.AppContext;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.Actives;
import com.zbmf.StocksMatch.beans.Announcement;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.MatchInfo;
import com.zbmf.StocksMatch.beans.PayInfo;
import com.zbmf.StocksMatch.beans.Question;
import com.zbmf.StocksMatch.beans.Quotation;
import com.zbmf.StocksMatch.beans.RecommendPic;
import com.zbmf.StocksMatch.beans.Record;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.beans.VersionInfo;
import com.zbmf.StocksMatch.beans.Yield;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONFunctions {
    private static JSONObject doJson(JSONObject json, String method)
            throws JSONException {
        String name = method.replaceAll("\\.", "_") + "_response";
        if (json.has(name))
            return json.getJSONObject(name);
        return null;
    }

    public static General jsonGet(JSONObject json, String method) {
        try {
            General obj = new General();
            JSONObject o = doJson(json, method);
            if (o != null) {
                if (o.getBoolean("success")) {
                    obj.setCode(0);
                } else {
                    obj.setCode(o.getInt("result_code"));
                    obj.setMsg(o.getString("result_msg"));
                }
            } else
                obj.setCode(-1);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static General jsonGet2(JSONObject json, String method) {
        try {
            General obj = new General();
            JSONObject o = doJson(json, method);
            if (o != null) {
                if (o.getBoolean("success")) {
                    obj.setCode(o.getInt("result_code"));
                } else {
                    obj.setCode(o.getInt("result_code"));
                    obj.setMsg(o.getString("result_code_msg"));
                }
            } else
                obj.setCode(-1);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static User Login(String str, String method) throws JSONException {
        try {
            User user = new User();
            JSONObject json = new JSONObject(str);
            if (json.optString("status").equals("ok")) {
                user.setStatus(1);
                JSONObject obj = json.optJSONObject("userinfo");
                user.setUser_id(obj.optString("user_id"));
                user.setRole(obj.optString("role"));
                user.setGid(obj.optString("gid"));
                user.setGroup_id(obj.optString("group_id"));
                user.setEnable_fans(obj.optString("enable_fans"));
                user.setFans(obj.optString("fans"));
                user.setUsername(obj.optString("username"));
                user.setNickname(obj.optString("nickname"));
                user.setAvatar(obj.optString("avatar"));
                user.setPhone(obj.optString("phone"));
                user.setMpay(obj.optString("mpay"));
                user.setSms(obj.optString("sms"));
                user.setAuth_token(obj.optString("auth_token"));
            } else {//错误信息
                user.setStatus(-1);
                user.setCode(json.optJSONObject("err").optInt("code"));
                user.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static MatchBean getRecommendMatch(JSONObject obj, String method) {

        try {
            MatchBean mb = new MatchBean();
            if (obj.optString("status").equals("ok")) {
                mb.setStatus(1);
                List<MatchBean> infoList = new ArrayList<MatchBean>();
                if (obj.has("match")) {
                    JSONArray js = obj.getJSONArray("match");
                    for (int i = 0; i < js.length(); i++) {
                        JSONObject oj = js.getJSONObject(i);
//                        Log.e("match", oj.toString());
                        MatchBean mbs = new MatchBean();
                        mbs.setId(oj.optString("id"));
                        mbs.setTitle(oj.optString("title"));
                        mbs.setInit_money(oj.optString("init_money"));
                        mbs.setMpay(oj.optString("mpay"));
                        mbs.setObject_type(oj.optString("object_type"));
                        mbs.setDesc(oj.optString("desc"));
                        mbs.setAward_remark(oj.optString("award_remark"));
                        mbs.setSponsor_logo(oj.optString("sponsor_logo"));
                        mbs.setStart_apply(oj.optString("start_apply"));
                        mbs.setStart_at(oj.optString("start_at"));
                        mbs.setEnd_at(oj.optString("end_at"));
                        mbs.setEnd_apply(oj.optString("end_apply"));
                        mbs.setApply_require_field(oj.optString("apply_require_field"));
                        mbs.setIs_match_player(oj.optString("is_match_player"));
                        mbs.setPlayers(oj.optString("players"));
                        mbs.setAward(oj.optString("award"));
                        if (oj.has("match_type")) {
                            mbs.setMatch_type(oj.getInt("match_type"));//比赛类型 0公开赛, 1邀请赛
                        }
                        if (oj.has("invite_type")) {
                            mbs.setInvite_type(oj.getInt("invite_type"));//邀请赛类型 0问题, 1邀请码, 2两者结合
                        }
                        mbs.setMatch_status(oj.optInt("match_status"));//是否需要手机号码和验证 1都要  2不要验证码 3都不要
                        if (oj.has("invite_method1")) {
                            Question q = new Question();
                            JSONObject o = oj.getJSONObject("invite_method1");
                            if (o.has("question"))
                                q.setQeustion(o.getString("question"));
                            if (o.has("answer"))
                                q.setAnswer(o.getString("answer"));
                            if (o.has("invite_code"))
                                q.setInvite_code(o.getString("invite_code"));

                            mbs.setInvite_method1(q);
                        }
                        infoList.add(mbs);
                    }
                }
                mb.setList(infoList);
            } else {
                mb.setStatus(-1);
                mb.setCode(obj.optJSONObject("err").optInt("code"));
                mb.setMsg(obj.optJSONObject("err").optString("msg"));
            }
            return mb;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RecommendPic getRecommendPic(JSONObject obj, String method) throws JSONException {
        RecommendPic rpc = new RecommendPic();
        List<RecommendPic> list = null;
        if (obj.optString("status").equalsIgnoreCase("ok")) {
            rpc.setStatus(1);
            list = new ArrayList<RecommendPic>();
            rpc.setMatch_count(obj.optString("match_count"));
            if (obj.has("phone_ads")) {
                JSONArray arr = obj.getJSONArray("phone_ads");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj1 = arr.getJSONObject(i);
                    RecommendPic rp = new RecommendPic();
                    if (obj1.has("link_url")) {
                        rp.setLink_url(obj1.optString("link_url"));
                    }

                    rp.setPic_url(obj1.optString("pic_url"));
                    rp.setType(obj1.optString("type"));

                    if (obj1.has("title")) {
                        rp.setTitle(obj1.getString("title"));
                    }
                    if (obj1.has("user_id")) {
                        rp.setUser_id(obj1.getString("user_id"));
                    }
                    if (obj1.has("match")) {
                        JSONObject o = obj1.optJSONObject("match");

                        rp.setMatch(jsonObj(o));
                    }
                    list.add(rp);
                }
                rpc.setList(list);
            }
        } else {
            rpc.setStatus(-1);
            rpc.setMsg(obj.optJSONObject("err").optString("msg"));
        }
        return rpc;
    }

    public static MatchBean getRunMatches(JSONObject obj, String method) throws JSONException {
        MatchBean mb = new MatchBean();
        if (obj.optString("status").equals("ok")) {
            mb.setStatus(1);
            List<MatchBean> infoList = new ArrayList<MatchBean>();
            JSONArray js = obj.optJSONArray("matches");
            for (int i = 0; i < js.length(); i++) {
                JSONObject oj = js.getJSONObject(i);
                infoList.add(jsonObj(oj));
            }
            mb.setList(infoList);
        } else {
            mb.setCode(obj.optJSONObject("err").optInt("code"));
            mb.setMsg(obj.optJSONObject("err").optString("msg"));
        }
        return mb;
    }

    private static MatchBean jsonObj(JSONObject oj) throws JSONException{
        MatchBean mbs = new MatchBean();
        mbs.setId(oj.optString("id"));
        mbs.setTitle(oj.optString("title"));
        mbs.setInit_money(oj.optString("init_money"));
        mbs.setMpay(oj.optString("mpay"));
        mbs.setObject_type(oj.optString("object_type"));
        mbs.setDesc(oj.optString("desc"));
        mbs.setAward_remark(oj.optString("award_remark"));
        mbs.setSponsor_logo(oj.optString("sponsor_logo"));
        mbs.setStart_apply(oj.optString("start_apply"));
        mbs.setStart_at(oj.optString("start_at"));
        mbs.setEnd_at(oj.optString("end_at"));
        mbs.setEnd_apply(oj.optString("end_apply"));
        mbs.setApply_require_field(oj.optString("apply_require_field"));
        mbs.setIs_match_player(oj.optString("is_match_player"));
        mbs.setPlayers(oj.optString("players"));
        mbs.setYield(oj.optDouble("yield"));
        mbs.setWeek_rank(oj.optString("week_rank"));
        mbs.setDay_rank(oj.optString("day_rank"));
        mbs.setTotal_rank(oj.optString("total_rank"));
        mbs.setMoney(oj.optDouble("money"));
        mbs.setUnfrozen_money(oj.optDouble("unfrozen_money"));

        mbs.setStock_holds(oj.optString("stock_holds"));
        mbs.setStock_order(oj.optString("stock_order"));
        mbs.setAward(oj.optString("award"));
//			mbs.setDay_yield(oj.optDouble("day_yield"));
//			mbs.setWeek_yield(oj.optDouble("week_yield"));

        if (oj.has("match_type")) {
            mbs.setMatch_type(oj.getInt("match_type"));//比赛类型 0公开赛, 1邀请赛
        }
        if (oj.has("invite_type")) {
            mbs.setInvite_type(oj.getInt("invite_type"));//邀请赛类型 0问题, 1邀请码, 2两者结合
        }

        mbs.setMatch_status(oj.optInt("match_status"));//是否需要手机号码和验证 1都要  2不要验证码 3都不要
        if (oj.has("invite_method1")) {
            Question q = new Question();
            JSONObject o = oj.getJSONObject("invite_method1");
            if (o.has("question"))
                q.setQeustion(o.getString("question"));
            if (o.has("answer"))
                q.setAnswer(o.getString("answer"));
            if (o.has("invite_code"))
                q.setInvite_code(o.getString("invite_code"));

            mbs.setInvite_method1(q);
        }

        return mbs;

    }

    public static MatchInfo getMatchMessage(JSONObject oj, String method) {
        MatchInfo mbs = new MatchInfo();

        if (oj.optString("status").equals("ok")) {
            mbs.setStatus(1);
            mbs.setId(oj.optString("id"));
            mbs.setNickname(oj.optString("nickname"));
            mbs.setAvatar(oj.optString("avatar"));
            mbs.setRecords(oj.optString("role"));
            mbs.setTruename(oj.optString("truename"));
            mbs.setMatch_username(oj.optString("match_username"));
            mbs.setMobile(oj.optString("mobile"));
            mbs.setUpdated_at(oj.optString("updated_at"));
            mbs.setTotal(oj.optDouble("total"));
            mbs.setInit(oj.optString("init"));
            mbs.setPosition(oj.optString("position"));
            mbs.setMoneyunfrozen(oj.optDouble("moneyunfrozen"));
            mbs.setFrozen(oj.optDouble("frozen"));
            mbs.setDeal(oj.optString("deal"));
            mbs.setStocks_value(oj.optString("stocks_value"));

            mbs.setYield(oj.optDouble("yield"));
            mbs.setDay_yield(oj.optDouble("day_yield"));
            mbs.setWeek_yield(oj.optDouble("week_yield"));
            mbs.setMonth_yield(oj.optDouble("month_yield"));
            mbs.setWeek_velocity(oj.optDouble("week_velocity"));

            mbs.setDay_rank(oj.optString("day_rank"));
            mbs.setWeek_rank(oj.optString("week_rank"));
            mbs.setMonth_rank(oj.optString("month_rank"));
            mbs.setTotal_rank(oj.optString("total_rank"));

            mbs.setHolds(oj.optString("holds"));
            mbs.setRecords(oj.optString("records"));
            mbs.setTrusts(oj.optString("trusts"));
            mbs.setHalf(oj.optString("half"));
            mbs.setAvg_week_yield(oj.optString("avg_week_yield"));
            mbs.setAvg_month_yield(oj.optString("avg_month_yield"));
            mbs.setCount_players(oj.optString("count_players"));
            mbs.setNew_announcement(oj.optString("new_announcement"));
        } else {
            mbs.setCode(oj.optJSONObject("err").optInt("code"));
            mbs.setStatus(-1);
            mbs.setMsg(oj.optJSONObject("err").optString("msg"));
        }
        return mbs;
    }

    public static MatchBean getMatchNoStopList(JSONObject obj, String method) {

        try {
            MatchBean mb = new MatchBean();
            if (obj.optString("status").equals("ok")) {
                mb.setStatus(1);
                String result = obj.optString("result");
                JSONObject obj1 = new JSONObject(result);
                mb.setPage(obj1.optInt("page"));
                mb.setPerpage(obj1.optInt("perpage"));
                mb.setPages(obj1.optInt("pages"));
                List<MatchBean> infoList = new ArrayList<MatchBean>();
                JSONArray js = obj1.optJSONArray("matches");
                for (int i = 0; i < js.length(); i++) {

                    JSONObject oj = js.getJSONObject(i);

                    infoList.add(jsonObj(oj));
                }
                mb.setList(infoList);
            } else {
                mb.setCode(obj.optJSONObject("err").optInt("code"));
                mb.setStatus(-1);
                mb.setMsg(obj.optJSONObject("err").optString("msg"));
            }
            return mb;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static General getVerifyCode(JSONObject json, String method) {
        General general = new General();
        if (json.optString("status").equals("ok")) {
            general.setStatus(1);
        } else {
            general.setMsg(json.optJSONObject("err").optString("msg"));
        }
        return general;
    }

    public static MatchBean applyMatch(JSONObject json, String method) {
        try {
            MatchBean mb = new MatchBean();
            if (json.optString("status").equals("ok")) {
                mb.setStatus(1);
                JSONObject obj = new JSONObject(json.optString("match"));
                mb.setMoney(obj.optDouble("money"));
                mb.setUnfrozen_money(obj.optDouble("unfrozen_money"));
                mb.setYield(obj.optDouble("yield"));
                mb.setDay_rank(obj.optString("day_rank"));
                mb.setWeek_rank(obj.optString("week_rank"));
                mb.setTotal_rank(obj.optString("total_rank"));
            } else {
                mb.setCode(json.optJSONObject("err").optInt("code"));
                mb.setStatus(-1);
                mb.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return mb;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static General getJson(JSONObject json, String method) {
        General general = new General();
        if (json.optString("status").equals("ok")) {
            general.setStatus(1);
        } else {
            general.setCode(json.optJSONObject("err").optInt("code"));
            general.setMsg(json.optJSONObject("err").optString("msg"));
        }
        return general;
    }

    public static Quotation getFocusList(JSONObject json, String method) {
        try {
            Quotation quotation = new Quotation();
            if (json.optString("status").equals("ok")) {
                quotation.setStatus(1);
                List<Quotation> list = new ArrayList<Quotation>();
                JSONArray js = json.optJSONArray("stocks");
                for (int i = 0; i < js.length(); i++) {
                    JSONObject obj = js.getJSONObject(i);
                    Quotation q = new Quotation();
                    q.setId(obj.optString("id"));
                    q.setSymbol(obj.optString("symbol"));
                    q.setName(obj.optString("name"));
                    q.setClose(obj.optString("close"));
                    q.setCurrent(obj.optString("current"));
                    list.add(q);
                }
                quotation.setList(list);
            } else {
                quotation.setCode(json.optJSONObject("err").optInt("code"));
                quotation.setStatus(-1);
                quotation.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return quotation;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Quotation getStockRealtimeInfo2(JSONObject json, String method) {
        Context cxt = AppContext.getInstance();
        HashMap<String, String> asiaMap = new HashMap<String, String>();
        try {
            Quotation quotation = new Quotation();
            List<List<Quotation>> lists = new ArrayList<List<Quotation>>();
            if (json.optString("status").equals("ok")) {
                quotation.setStatus(1);
                List<Quotation> list = new ArrayList<Quotation>();
                String result = json.getString("result");
                JSONObject obj = new JSONObject(result);
                if (obj.has("user")) {
                    JSONArray js = obj.optJSONArray("user");
                    add(lists, asiaMap, new String[]{"0"}, new String[]{"0"}, js);
                }
                if (obj.has("Asia")) {

                    String[] karr = cxt.getResources().getStringArray(R.array.kAsia);
                    String[] varr = cxt.getResources().getStringArray(R.array.vAsia);

                    JSONArray js = obj.optJSONArray("Asia");

                    add(lists, asiaMap, karr, varr, js);

                }
                if (obj.has("Europe")) {
                    String[] karr = cxt.getResources().getStringArray(R.array.kEurope);
                    String[] varr = cxt.getResources().getStringArray(R.array.vEurope);
                    JSONArray js = obj.optJSONArray("Europe");
                    add(lists, asiaMap, karr, varr, js);
                }
                if (obj.has("Future")) {
                    String[] karr = cxt.getResources().getStringArray(R.array.kFuture);
                    String[] varr = cxt.getResources().getStringArray(R.array.vFuture);
                    JSONArray js = obj.optJSONArray("Future");
                    add(lists, asiaMap, karr, varr, js);
                }
                if (obj.has("Us")) {
                    String[] karr = cxt.getResources().getStringArray(R.array.kUs);
                    String[] varr = cxt.getResources().getStringArray(R.array.vUs);
                    JSONArray js = obj.optJSONArray("Us");
                    add(lists, asiaMap, karr, varr, js);
                }
                if (obj.has("RMB")) {
                    String[] karr = cxt.getResources().getStringArray(R.array.kRMB);
                    String[] varr = cxt.getResources().getStringArray(R.array.vRMB);
                    JSONArray js = obj.optJSONArray("RMB");
                    add(lists, asiaMap, karr, varr, js);
                }

                quotation.setLists(lists);
            } else {
                quotation.setCode(json.optJSONObject("err").optInt("code"));
                quotation.setStatus(-1);
                quotation.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return quotation;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void add(List<List<Quotation>> lists, HashMap<String, String> asiaMap, String[] karr, String[] varr, JSONArray js) throws JSONException {
        for (int i = 0; i < karr.length; i++) {
            asiaMap.put(karr[i], varr[i]);
        }

        List<Quotation> qs = new ArrayList<Quotation>();
        for (int i = 0; i < js.length(); i++) {
            JSONObject ob = js.getJSONObject(i);
            Quotation q = new Quotation();
            if (asiaMap.containsKey(ob.optString("name"))) {
                q.setName(asiaMap.get(ob.optString("name")));
            } else {
                q.setName(ob.optString("name"));
            }

            q.setClose(ob.optString("close"));
            q.setCurrent(ob.optString("current"));
            if (ob.has("symbol")) {
                q.setSymbol(ob.getString("symbol"));
            }
            if (ob.has("id")) {
                q.setId(ob.getString("id"));
            }
            if (ob.has("buy")) {
                q.setBuy(ob.getString("buy"));
            }
            if (ob.has("sell")) {
                q.setSell(ob.getString("sell"));
            }
            if (ob.has("midprice")) {
                q.setMidprice(ob.getString("midprice"));
            }
            qs.add(q);
        }
        lists.add(qs);
    }

    public static Stock getStockRealtimeInfo(JSONObject json, String method) {
        try {
            Stock s = new Stock();
            if (json.optString("status").equals("ok")) {
                s.setStatus(1);
                String result = json.optString("result");
                JSONObject obj = new JSONObject(result);
                s.setDt(obj.optString("dt"));
                s.setSymbol(obj.optString("symbol"));
                s.setName(obj.optString("name"));
                s.setCurrent(obj.optDouble("current"));
                s.setTotal_amount(obj.optDouble("total_amount"));
                s.setTotal_volumn(obj.optDouble("total_volumn"));
                s.setClose(obj.optDouble("close"));
                s.setOpen(obj.optDouble("open"));
                s.setHigh(obj.optDouble("high"));
                s.setLow(obj.optDouble("low"));
                s.setVolumnunfrozen(obj.optDouble("volumnunfrozen"));

                List<Stock> list = new ArrayList<Stock>();

                for (int index = 1; index < 6; index++) {
                    Stock s1 = new Stock();
                    s1.setBuy(obj.optDouble("buy" + index));
                    s1.setSell(obj.optDouble("sell" + index));
                    s1.setVolumn_buy(obj.optString("volumn_buy" + index));
                    s1.setVolumn_sell(obj.optString("volumn_sell" + index));
                    list.add(s1);
                }

                s.setList(list);
                s.setOptional_stock_user(obj.optBoolean("optional_stock_user"));
            } else {
                s.setCode(json.optJSONObject("err").optInt("code"));
                s.setStatus(-1);
                s.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return s;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static User searchUsers(JSONObject json, String method) {
        try {
            User user = new User();
            if (json.optString("status").equals("ok")) {
                List<User> list = new ArrayList<User>();
                user.setStatus(1);
                String str = json.optString("user");
                JSONArray js = new JSONArray(str);
                for (int i = 0; i < js.length(); i++) {
                    JSONObject obj = js.getJSONObject(i);
                    User u = new User();
                    u.setNickname(obj.optString("nickname"));
                    u.setUser_id(obj.optString("user_id"));
                    list.add(u);
                }
                user.setList(list);
            } else {
                user.setCode(json.optJSONObject("err").optInt("code"));
                user.setStatus(-1);
                user.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Actives actives(JSONObject json, String method) {
        try {
            Actives actives = new Actives();
            if (json.optString("status").equals("ok")) {
                List<Actives> list = new ArrayList<Actives>();
                actives.setStatus(1);
                String str = json.optString("actives");
                JSONArray js = new JSONArray(str);
                for (int i = 0; i < js.length(); i++) {
                    JSONObject obj = js.getJSONObject(i);
                    Actives a = new Actives();
                    a.setTitle(obj.optString("title"));
                    a.setUrl(obj.optString("url"));
                    a.setDesc(obj.optString("desc"));
                    a.setStart_time(obj.optString("start_time"));
                    a.setEnd_time(obj.optString("end_time"));
                    a.setPic_url(obj.optString("pic_url"));
                    a.setCreated_at(obj.optString("created_at"));
                    a.setType(obj.optString("type"));
                    list.add(a);
                }
                actives.setList(list);
            } else {
                actives.setCode(json.optJSONObject("err").optInt("code"));
                actives.setStatus(-1);
                actives.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return actives;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Group getUserGroups(JSONObject json, String method) {
        try {
            Group g = new Group();
            if (json.optString("status").equals("ok")) {
                g.setStatus(1);
                String result = json.optString("result");
                JSONObject obj1 = new JSONObject(result);
                g.setPage(obj1.optInt("page"));
                g.setPerpage(obj1.optInt("perpage"));
                g.setPages(obj1.optInt("pages"));
                List<Group> infoList = new ArrayList<Group>();
                JSONArray js = obj1.optJSONArray("groups");
                for (int i = 0; i < js.length(); i++) {
                    JSONObject obj = js.getJSONObject(i);
                    Group group = new Group();
                    group.setId(obj.optString("id"));
                    group.setName(obj.optString("name"));
                    group.setAvatar(obj.optString("avatar"));
                    group.setRole(obj.optString("role"));
                    group.setGid(obj.optString("gid"));
                    group.setGroup(obj.optString("group"));
                    group.setCount_fans(obj.optString("count_fans"));
                    group.setPrivat(obj.optString("private"));
                    infoList.add(group);
                }
                g.setList(infoList);
            } else {
                g.setCode(json.optJSONObject("err").optInt("code"));
                g.setStatus(-1);
                g.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return g;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User UserMore(JSONObject json, String method) {

        try {
            User user = new User();
            if (json.optString("status").equals("ok")) {
                user.setStatus(1);
                if (json.has("avatar")) {
                    user.setAvatar(json.getString("avatar"));
                }
                if (json.has("nickname")) {
                    user.setNickname(json.getString("nickname"));
                }
                if (json.has("mpay")) {
                    user.setMpay(json.getString("mpay"));
                }

                if (json.has("count_users")) {
                    user.setCount_fens(json.getString("count_users"));
                }

            } else {
                user.setCode(json.optJSONObject("err").optInt("code"));
                user.setStatus(-1);
                user.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Record getWinRecords(JSONObject json, String method) {
        try {
            Record record = new Record();
            if (json.optString("status").equals("ok")) {
                record.setStatus(1);
                if (json.has("result")) {
                    JSONObject obj = json.getJSONObject("result");
                    record.setPage(obj.optInt("page"));
                    record.setPerpage(obj.optInt("perpage"));
                    record.setPages(obj.optInt("pages"));
                    if (obj.has("records")) {
                        List<Record> records = new ArrayList<Record>();
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject oj1 = jsonArray.getJSONObject(i);
                            Record r = new Record();

                            r.setWin_at(oj1.optString("win_at"));
                            r.setCategory(oj1.optString("category"));
                            r.setRank(oj1.optString("rank"));
                            r.setAward(oj1.optString("award"));
                            if (oj1.has("match")) {
                                JSONObject oj = oj1.getJSONObject("match");

                                r.setMatch(jsonObj(oj));
                            }

                            records.add(r);

                        }
                        record.setRecords(records);
                    }
                }
            } else {
                record.setCode(json.optJSONObject("err").optInt("code"));
                record.setStatus(-1);
                record.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return record;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Announcement getAnnouncements(JSONObject json, String method) {
        try {
            Announcement announcement = new Announcement();
            if (json.optString("status").equals("ok")) {
                announcement.setStatus(1);
                String result = json.optString("result");
                JSONObject obj1 = new JSONObject(result);
                announcement.setPage(obj1.optInt("page"));
                announcement.setPerpage(obj1.optInt("perpage"));
                announcement.setPages(obj1.optInt("pages"));
                List<Announcement> infoList = new ArrayList<Announcement>();
                JSONArray js = obj1.optJSONArray("announcements");
                for (int i = 0; i < js.length(); i++) {
                    JSONObject obj = js.getJSONObject(i);
                    Announcement a = new Announcement();
                    a.setGroup_id(obj.optString("group_ip"));
                    a.setTopic_id(obj.optString("topic_id"));
                    a.setSubject(obj.optString("subject"));
                    a.setContent(obj.optString("content"));
                    a.setPosted_at(obj.optString("posted_at"));
                    infoList.add(a);
                }
                announcement.setList(infoList);
            } else {
                announcement.setCode(json.optJSONObject("err").optInt("code"));
                announcement.setStatus(-1);
                announcement.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return announcement;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Yield getYieldList(JSONObject json, String method) {
        try {
            Yield yield = new Yield();
            if (json.optString("status").equals("ok")) {
                yield.setStatus(1);
                if (json.has("result")) {
                    JSONObject obj = json.getJSONObject("result");
                    yield.setPage(obj.optInt("page"));
                    yield.setPerpage(obj.optInt("perpage"));
                    yield.setPages(obj.optInt("pages"));
                    if (obj.has("yields")) {
                        List<Yield> yields = new ArrayList<Yield>();
                        JSONArray jsonArray = obj.getJSONArray("yields");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject oj1 = jsonArray.getJSONObject(i);
                            Yield y = new Yield();
                            y.setUser(oj1.optString("user"));
                            y.setNickname(oj1.optString("nickname"));
                            y.setAvatar(oj1.optString("avatar"));
                            y.setRole(oj1.optString("role"));
                            y.setTotal_yield(oj1.optDouble("total_yield"));
                            y.setDay_yield(oj1.optDouble("day_yield"));
                            y.setWeek_yield(oj1.optDouble("week_yield"));
                            y.setMonth_yield(oj1.optDouble("month_yield"));
                            y.setIs_track(oj1.optString("is_track"));
                            y.setYield(oj1.optDouble("yield"));
                            y.setTotal(oj1.optInt("total"));
                            yields.add(y);
                        }
                        yield.setYields(yields);
                    }
                }
            } else {
                yield.setCode(json.optJSONObject("err").optInt("code"));
                yield.setStatus(-1);
                yield.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return yield;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static StockholdsBean getHoldlist(JSONObject json, String method) {
        try {
            StockholdsBean stockholdsBean = new StockholdsBean();
            if (json.optString("status").equals("ok")) {
                stockholdsBean.setStatus(1);
                if (json.has("result")) {
                    JSONObject obj = json.getJSONObject("result");
                    stockholdsBean.setPage(obj.optInt("page"));
                    stockholdsBean.setPerpage(obj.optInt("perpage"));
                    stockholdsBean.setPages(obj.optInt("pages"));
                    if (obj.has("stocks")) {
                        List<StockholdsBean> list = new ArrayList<StockholdsBean>();
                        JSONArray jsonArray = obj.getJSONArray("stocks");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject oj = jsonArray.getJSONObject(i);
                            StockholdsBean sbl = new StockholdsBean();
                            sbl.setId(oj.optString("id"));
                            sbl.setSymbol(oj.optString("symbol"));
                            sbl.setName(oj.optString("name"));
                            sbl.setCuurent(oj.optDouble("current", 0));
                            sbl.setColose(oj.optDouble("close", 0));
                            sbl.setPrice_buy(oj.optDouble("price_buy", 0));
                            sbl.setPrice2(oj.optDouble("price2", 0));
                            sbl.setPrice_float(oj.optDouble("price_float", 0));
                            sbl.setPrice_sell(oj.optDouble("price_sell", 0));
                            sbl.setProfit(oj.optDouble("profit", 0));
                            sbl.setYield_float(oj.optDouble("yield_float", 0));
                            sbl.setVolumn_total(oj.optString("volumn_total"));
                            sbl.setVolumn_infrozen(oj.optString("volumn_unfrozen"));
                            sbl.setCreate_at(oj.optString("created_at"));
                            sbl.setIs_buy(oj.optString("is_buy"));
                            sbl.setIs_show(oj.optString("is_show"));
                            list.add(sbl);
                        }
                        stockholdsBean.setInfolist(list);
                    }
                }
            } else {
                stockholdsBean.setCode(json.optJSONObject("err").optInt("code"));
                stockholdsBean.setStatus(-1);
                stockholdsBean.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return stockholdsBean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static StockholdsBean getOrderList(JSONObject json, String method) {
        try {
            StockholdsBean stockholdsBean = new StockholdsBean();
            if (json.optString("status").equals("ok")) {
                stockholdsBean.setStatus(1);
                if (json.has("result")) {
                    JSONObject obj = json.getJSONObject("result");
                    stockholdsBean.setPage(obj.optInt("page"));
                    stockholdsBean.setPerpage(obj.optInt("perpage"));
                    stockholdsBean.setPages(obj.optInt("pages"));
                    if (obj.has("stocks")) {
                        List<StockholdsBean> list = new ArrayList<StockholdsBean>();
                        JSONArray jsonArray = obj.getJSONArray("stocks");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject oj = jsonArray.getJSONObject(i);
                            StockholdsBean sbl = new StockholdsBean();
                            sbl.setId(oj.optString("id"));
                            sbl.setSymbol(oj.optString("symbol"));
                            sbl.setName(oj.optString("name"));
//                            sbl.setPrice_buy(oj.optDouble("price", 0));

                            sbl.setPrice2(oj.optDouble("price",0));
                            sbl.setPrice_buy(oj.optDouble("price_buy",0));
                            sbl.setPrice_sell(oj.optDouble("price_sell",0));



                            sbl.setFrozen(oj.optString("frozen"));
                            sbl.setType(oj.optString("type"));
                            sbl.setVolumn(oj.optString("volumn"));
                            sbl.setPosted_at(oj.optString("posted_at"));
                            sbl.setProfit(oj.optDouble("profit", 0));
                            sbl.setIs_vip(oj.optBoolean("is_vip"));
                            sbl.setIs_show(oj.optString("is_show"));
                            list.add(sbl);
                        }
                        stockholdsBean.setInfolist(list);
                    }
                }
            } else {
                stockholdsBean.setCode(json.optJSONObject("err").optInt("code"));
                stockholdsBean.setStatus(-1);
                stockholdsBean.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return stockholdsBean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User UserInfo(JSONObject json, String method) {
        try {
            User user = new User();
            if (json.optString("status").equals("ok")) {
                user.setStatus(1);
                JSONObject obj = json.optJSONObject("user");
                user.setUser_id(obj.optString("user_id"));
                user.setGroup_id(obj.optString("group_id"));
                user.setNickname(obj.optString("nickname"));
                user.setAvatar(obj.optString("avatar"));
                user.setCount_fens(obj.optString("count_fens"));
                user.setIs_focus(obj.optString("is_focus"));
                if (obj.has("matches")) {
                    JSONArray jsonArray = obj.getJSONArray("matches");
                    List<MatchBean> matchs = new ArrayList<MatchBean>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject oj = jsonArray.getJSONObject(i);

                        matchs.add(jsonObj(oj));

                    }

                    user.setMatches(matchs);
                }
            } else {//错误信息
                user.setCode(json.optJSONObject("err").optInt("code"));
                user.setStatus(-1);
                user.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User iconupload(JSONObject json, String method) {
        User general = new User();
        if (json.optString("status").equals("ok")) {
            general.setStatus(1);
            general.setAvatar(json.optString("icon_url"));
            general.setNickname(json.optString("nickname"));
        } else {
            general.setCode(json.optJSONObject("err").optInt("code"));
            general.setMsg(json.optJSONObject("err").optString("msg"));
        }
        return general;
    }

    public static PayInfo PayStockTem(JSONObject json, String method) {
        try {
            PayInfo payInfo = new PayInfo();
            if (json.optString("status").equals("ok")) {
                payInfo.setStatus(1);
                if (json.has("mpay")) {
                    payInfo.setMpay(json.getString("mpay"));
                }
                if (json.has("discount")) {
                    payInfo.setDiscount(json.optDouble("discount"));
                }
            } else {
                payInfo.setCode(json.optJSONObject("err").optInt("code"));
                payInfo.setStatus(-1);
                payInfo.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return payInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static VersionInfo version(JSONObject json, String method) {
            VersionInfo info = new VersionInfo();
            if (json.optString("status").equals("ok")) {
                info.setStatus(1);
                info.setForce(json.optString("subject"));
                info.setUrl(json.optString("download"));
                info.setVersion(json.optString("version"));
                if(json.has("address")){
                    JSONObject address=json.optJSONObject("address");
                    if(address.has("match")){
                        JSONObject match=address.optJSONObject("match");
                        info.setAppUrl(match.optString("host")+match.optString("api"));
                        info.setAppHost(match.optString("host"));
                    }
                }
            } else {
                info.setStatus(-1);
                info.setCode(json.optJSONObject("err").optInt("code"));
                info.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return info;
    }


public static Stock updateDB(JSONObject json, String method) {
        try {
            Stock stock = new Stock();
            if (json.optString("status").equals("ok")) {
                stock.setStatus(1);
                if(json.has("stocks")){
                    JSONArray obj = json.getJSONArray("stocks");
                    ArrayList<Stock> stocks = new ArrayList<>();
                    for (int i=0;i<obj.length();i++){
                        JSONObject object = obj.getJSONObject(i);
                        Stock s = new Stock();
                        s.setSymbol(object.optString("symbol"));
                        s.setName(object.optString("name"));
                        s.setMarket(object.optString("short"));
                        stocks.add(s);
                    }
                    stock.setList(stocks);
                }
            } else {
                stock.setCode(json.optJSONObject("err").optInt("code"));
                stock.setStatus(-1);
                stock.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return stock;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

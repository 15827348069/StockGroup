package com.zbmf.groupro.utils;

import com.zbmf.groupro.beans.Ask;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.ChatMessage;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.beans.NewsFeed;
import com.zbmf.groupro.beans.Offine;
import com.zbmf.groupro.beans.User;
import com.zbmf.groupro.beans.Vers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by iMac on 2016/12/28.
 */

public class JSONParse {


    public static ChatMessage getRoomMsg(JSONObject json) {
        ChatMessage message = new ChatMessage();
        try {
            JSONArray jsonArray = json.getJSONArray("msgs");
            List<ChatMessage> messages = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                messages.add(getChatMsgObj(obj));
            }
            message.setMessages(messages);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return message;
    }

    public static ChatMessage getChatMsgObj(JSONObject obj) {
        try {
            ChatMessage msg = new ChatMessage();
            msg.setMsg_id(obj.optString("msg_id"));
            msg.setClient_msg_id(obj.optString("client_msg_id"));
            msg.setMsg_type(obj.optString("msg_type"));
            msg.setChat_type(obj.optString("chat_type"));
            msg.setRoom(obj.optInt("room"));
            msg.setFrom(obj.optString("from"));
            msg.setNickname(obj.optString("nickname"));
            msg.setAvatar(obj.optString("avatar"));
            msg.setRole(obj.optInt("role"));
            msg.setTo(obj.optInt("to"));
            msg.setTime(obj.optString("time"));
//            Log.e("TAG","role:"+obj.optInt("role"));
            msg.setAction(obj.optString("action"));
            if (obj.has("msg")) {
                JSONObject object = obj.getJSONObject("msg");
                msg.setType(object.optString("type"));
                msg.setContent(object.optString("content"));
            }

            if (obj.has("object")) {
                JSONObject object1 = obj.getJSONObject("object");
                msg.setGroup_id(object1.optString("group_id"));
                msg.setNickname(object1.optString("nickname"));//new nickname 4 action
                msg.setUser_id(object1.optString("user_id"));
                msg.setMsg_id(object1.optString("msg_id"));//new msg_id
            }
            return msg;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Ask getAsks(JSONObject jsonObject) {
        Ask ask = new Ask();
        try {
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            ask.setPages(jsonObject1.getInt("pages"));
            ask.setPage(jsonObject1.optInt("page"));
            JSONArray jsonArray = jsonObject1.getJSONArray("asks");
            List<Ask> asks = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Ask a = new Ask();
                JSONObject object = jsonArray.getJSONObject(i);
                a.setAsk_id(object.optString("ask_id"));
                a.setAsk_status(object.optString("ask_status"));
                a.setUser_id(object.optString("user_id"));
                a.setNickname(object.optString("nickname"));
                a.setAsk_content(object.optString("ask_content"));
                a.setTarget_id(object.optString("target_id"));
                a.setTarget_nickname(object.optString("target_nickname"));
                a.setPosted_at(object.optString("posted_at"));
                if(object.has("post")){
                    Ask.Post post = new Ask.Post();
                    JSONObject object1 = object.getJSONObject("post");
                    post.setPost_id(object1.optString("post_id"));
                    post.setUser_id(object1.optString("user_id"));
                    post.setNickname(object1.optString("nickname"));
                    post.setPost_content(object1.optString("post_content"));
                    post.setAsk_id(object1.optString("ask_id"));
                    post.setPosted_at(object1.optString("posted_at"));
                    post.setIs_private(object.getInt("is_private"));
                    a.setPost(post);
                    if(a.getPost().getIs_private()
                            &&a.getAsk_id().equals(SettingDefaultsManager.getInstance().UserId())){
                        asks.add(a);
                    }else{
                        asks.add(a);
                    }
                }
            }
            ask.setAsks(asks);
            return ask;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Group userGroups(JSONObject obj) {
        Group group = new Group();
        try {
            JSONObject jsonObject = obj.getJSONObject("result");
            group.setPages(jsonObject.getInt("pages"));
            JSONArray jsonArray = jsonObject.getJSONArray("groups");
            List<Group> groups = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Group g = new Group();
                g.setId(object.optString("id"));
                g.setName(object.optString("name"));
                g.setNick_name(object.optString("nickname"));
                g.setAvatar(object.optString("avatar"));
                g.setIs_close(object.optInt("is_close"));
                g.setIs_private(object.optInt("is_private"));
                g.setRoles(object.optInt("role"));
                g.setIs_recommend(true);
                groups.add(g);
            }
            group.setList(groups);
            return group;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NewsFeed blog(JSONObject obj) {
        NewsFeed newsFeed = new NewsFeed();
        try {
            JSONArray jsonArray = obj.getJSONArray("newsfeeds");
            List<NewsFeed> ns = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                NewsFeed n = new NewsFeed();
                n.setBlog_id(object.optString("blog_id"));
                n.setCover(object.optString("cover"));
                n.setSubject(object.optString("subject"));

                if (object.has("user")) {
                    JSONObject o1 = object.getJSONObject("user");
                    NewsFeed.User stat = new NewsFeed.User();
                    stat.setId(o1.optString("id"));
                    stat.setNickname(o1.optString("nickname"));
                    stat.setAvatar(o1.optString("avatar"));
                    n.setUser(stat);
                }

                if (object.has("link")) {
                    JSONObject o1 = object.getJSONObject("link");
                    NewsFeed.Link stat = new NewsFeed.Link();
                    stat.setApp(o1.optString("app"));
                    stat.setWap(o1.optString("wap"));
                    n.setLink(stat);
                }
                if (object.has("stat")) {
                    JSONObject o1 = object.getJSONObject("stat");
                    NewsFeed.Stat stat = new NewsFeed.Stat();
                    stat.setReplys(o1.optString("replys"));
                    stat.setViews(o1.optString("views"));
                    n.setStat(stat);
                }
                ns.add(n);
            }
            newsFeed.setList(ns);
            return newsFeed;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Vers vers(JSONObject obj) {
        Vers vers = new Vers();
        try {
            vers.setVersion(obj.getString("version"));
            vers.setSubject(obj.getString("subject"));
            vers.setIntro(obj.getString("intro"));
            vers.setUrl(obj.getString("download"));
            vers.setUpdated_at(obj.getString("updated_at"));
            if (obj.has("logics")) {
                JSONObject object = obj.getJSONArray("logics").getJSONObject(0);
                Vers.Logics logics = new Vers.Logics();
                logics.state = object.optString("state");
                logics.intro = object.optString("intro");
                vers.setLogics(logics);
            }
            return vers;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BoxBean box(JSONObject obj) {
        BoxBean newsBox = new BoxBean();
        try {
            JSONArray jsonArray = obj.getJSONArray("newsfeeds");
            List<BoxBean> ns = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                BoxBean n = new BoxBean();
                JSONObject o = jsonArray.getJSONObject(i);
                n.setBox_id(o.optString("box_id"));
                n.setSubject(o.optString("subject"));
                n.setDescription(o.optString("description"));
                n.setIs_stick(o.optString("is_stick"));
                n.setIs_stop(o.optString("is_stop"));
                n.setBox_level(o.optString("box_level"));
                n.setBox_updated(o.optString("box_updated"));
                n.setFans_level(o.optString("fans_level"));
                if (o.has("user")) {
                    JSONObject o1 = o.getJSONObject("user");
                    NewsFeed.User stat = new NewsFeed.User();
                    stat.setId(o1.optString("id"));
                    stat.setNickname(o1.optString("nickname"));
                    n.setUser(stat);
                    n.setId(stat.getId());
                }
                ns.add(n);
            }
            newsBox.setList(ns);
            return newsBox;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static User isBind(JSONObject obj) {
        User user = new User();
        try {
            user.setUser_id(obj.getString("user_id"));
            user.setUsername(obj.getString("username"));
            user.setPhone(obj.getString("phone"));
            user.setIs_bind(obj.getString("is_bind"));
            user.setHas_password(obj.getString("has_password"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Group fansInfo(JSONObject obj) {
        Group group = new Group();
        try {
            JSONObject object = obj.getJSONObject("group");
            group.setId(object.optString("id"));
            group.setName(object.optString("name"));
            group.setNick_name(object.optString("nickname"));
            group.setAvatar(object.optString("avatar"));
            group.setIs_close(object.optInt("is_close"));
            group.setIs_private(object.optInt("is_private"));
            group.setRoles(object.optInt("roles"));
            group.setFans_level(object.optInt("fans_level"));
            group.setDay_mapy(object.optLong("day_mpay"));
            group.setMonth_mapy(object.optLong("month_mpay"));
            group.setEnable_day(object.optInt("enable_day"));
            group.setEnable_point(object.optInt("enable_point"));
            group.setMax_point(object.optInt("max_point"));
            group.setDescription(object.optString("fans_profile"));
            group.setFans_activity(object.optString("fans_activity"));
            group.setFans_countent(object.optString("fans_content"));
            group.setPoint_desc(object.optString("point_desc"));
            group.setMax_mpay(object.optLong("max_mpay"));
            return group;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //        11	技术教程
    //        12	操盘日志
    //        13	指标研究
    //        21	短线
    //        22	中线
    //        23	长线
    //        31	低风险
    //        32	高风险
    public static BoxBean getGroupBoxs(JSONObject obj) {
        int[] caArr = {11, 12, 13}, rArr = {21, 22, 23}, cyArr = {31, 32};

        int category, risk, cycle;
        String name = "";
        BoxBean newsBox = new BoxBean();
        try {
            List<BoxBean> ns = new ArrayList<>();
            JSONObject obj1 = obj.optJSONObject("result");
            newsBox.setPage(obj1.optInt("page"));
            newsBox.setPages(obj1.optInt("pages"));
            JSONArray boxs = obj1.optJSONArray("boxs");
            for (int i = 0; i < boxs.length(); i++) {
                JSONObject box = boxs.optJSONObject(i);
                BoxBean bb = new BoxBean();
                bb.setId(box.optString("id"));
                bb.setBox_id(box.optString("box_id"));
                bb.setSubject(box.optString("subject"));
                bb.setDescription(box.optString("description"));
                bb.setIs_stick(box.optString("is_stick"));
                bb.setBox_level(box.optString("box_level"));
                bb.setBox_updated(box.optString("box_updated"));
                bb.setItems(box.optInt("items"));
                List<BoxBean.Tags> info = new ArrayList<>();
                category = box.getInt("category");
                if (Arrays.asList(caArr).contains(category)) {
                    if (category == 11)
                        name = "技术教程";
                    else if (category == 12)
                        name = "操盘日志";
                    else if (category == 13)
                        name = "指标研究";
                    info.add(bb.getTag(name, 1));
                }

                risk = box.getInt("risk");
                if (Arrays.asList(rArr).contains(risk)) {
                    if (risk == 21)
                        name = "短线";
                    else if (risk == 21)
                        name = "中线";
                    else if (risk == 21)
                        name = "长线";
                    info.add(bb.getTag(name, 2));
                }

                cycle = box.getInt("cycle");
                if (Arrays.asList(cyArr).contains(cycle)) {
                    if (risk == 21)
                        name = "低风险";
                    else if (risk == 21)
                        name = "高风险";
                    info.add(bb.getTag(name, 3));
                }

                bb.setTags(info);
                ns.add(bb);
            }
            newsBox.setList(ns);
            return newsBox;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Offine Bind(JSONObject obj) {
        Offine offine = new Offine();
        try {
            JSONArray jsonArray = obj.getJSONArray("offline");
            List<Offine> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Offine o = new Offine();
                o.setGroup_id(object.optInt("group_id"));
                o.setLive(object.optInt("live"));
                o.setRoom(object.optInt("room"));
                o.setUser(object.optInt("user"));
                list.add(o);
            }
            offine.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return offine;
    }
}

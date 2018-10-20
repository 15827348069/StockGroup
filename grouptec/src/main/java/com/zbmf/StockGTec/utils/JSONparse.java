package com.zbmf.StockGTec.utils;

import com.zbmf.StockGTec.adapter.QuestionAdapter;
import com.zbmf.StockGTec.beans.Ask;
import com.zbmf.StockGTec.beans.BanInfo;
import com.zbmf.StockGTec.beans.BlogBean;
import com.zbmf.StockGTec.beans.BoxBean;
import com.zbmf.StockGTec.beans.ChatMessage;
import com.zbmf.StockGTec.beans.Fans;
import com.zbmf.StockGTec.beans.Invite;
import com.zbmf.StockGTec.beans.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by iMac on 2016/12/28.
 */

public class JSONparse {


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
            JSONObject object = obj.getJSONObject("msg");
            msg.setType(object.optString("type"));
            msg.setContent(object.optString("content"));
            return msg;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BanInfo getBanList(JSONObject object) {
        try {
            BanInfo banInfo = new BanInfo();
            JSONArray jsonArray = object.getJSONArray("ban_users");
            ArrayList<BanInfo> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                BanInfo info = new BanInfo();
                info.setUser_id(o.optString("user_id"));
                info.setNickname(o.optString("nickname"));
                info.setTime(o.optString("time"));
                list.add(info);
            }
            banInfo.setInfos(list);
            return banInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Fans Fans(JSONObject obj) {
        try {
            Fans fans = new Fans();
            JSONObject object = obj.getJSONObject("result");
            fans.pages = object.getInt("pages");
            JSONArray jsonArray = object.getJSONArray("fans");
            List<Fans> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                Fans f = new Fans();
                f.setUser_id(o.optString("user_id"));
                f.setNickname(o.optString("nickname"));
                f.setAvatar(o.optString("avatar"));
                f.setFans_level(o.optInt("fans_level"));
                f.setJoin_at(o.optString("join_at"));
                f.setExpire_at(o.optString("expire_at"));
                list.add(f);
            }
            fans.setList(list);
            return fans;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Ask groupAsks(JSONObject o) {
        Ask ask = new Ask();
        try {
//            JSONObject o = obj.getJSONObject("result");
//            ask.pages = o.optInt("pages");
            if (o.has("unanswered_asks")) {
                List<Ask> unanswered_asks = new ArrayList<Ask>();
                JSONArray jsonArray = o.getJSONArray("unanswered_asks");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Ask a = new Ask();
                    if (i == 0)
                        a.setFirst(true);
                    a.setAnswerType(QuestionAdapter.TYPE_UNANSWERED);
                    JSONObject oo = jsonArray.getJSONObject(i);
                    a.setAsk_id(oo.optString("ask_id"));
                    a.setUser_id(oo.optString("user_id"));
                    a.setAsk_content(oo.optString("ask_content"));
                    a.setPosted_at(oo.optString("posted_at"));
                    a.setNickname(oo.optString("nickname"));
                    a.setAsk_status(oo.optInt("ask_status"));
                    a.setIs_private(oo.optInt("is_private"));
                    a.setFans_level(oo.optInt("fans_level"));
                    unanswered_asks.add(a);
                }
                ask.setUnanswered_asks(unanswered_asks);
            }

            if (o.has("answered_asks")) {
                List<Ask> answered_asks = new ArrayList<Ask>();
                JSONArray jsonArray = o.getJSONArray("answered_asks");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Ask a = new Ask();
                    if (i == 0)
                        a.setFirst(true);
                    a.setAnswerType(QuestionAdapter.TYPE_ANSWERED);
                    JSONObject oo = jsonArray.getJSONObject(i);
                    a.setAsk_id(oo.optString("ask_id"));
                    a.setUser_id(oo.optString("user_id"));
                    a.setAsk_content(oo.optString("ask_content"));
                    a.setPosted_at(oo.optString("posted_at"));
                    a.setNickname(oo.optString("nickname"));
                    a.setAsk_status(oo.optInt("ask_status"));
                    a.setIs_private(oo.optInt("is_private"));
                    a.setFans_level(oo.optInt("fans_level"));
                    if (oo.has("post")) {
                        JSONObject pobj = oo.getJSONObject("post");
                        Ask.PostBean postBean = new Ask.PostBean();
                        postBean.setPost_id(pobj.optInt("post_id"));
                        postBean.setUser_id(pobj.optString("user_id"));
                        postBean.setPost_content(pobj.optString("post_content"));
                        postBean.setPosted_at(pobj.optString("posted_at"));
                        postBean.setNickname(pobj.optString("nickname"));
                        postBean.setIs_private(pobj.optInt("is_private"));
                        postBean.setAsk_id(oo.optInt("ask_id"));
                        a.setPost(postBean);
                    }
                    answered_asks.add(a);
                }
                ask.setAnswered_asks(answered_asks);
            }

            return ask;
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
        Integer[] caArr = {11, 12, 13}, cyArr = {21, 22, 23}, rArr = {31, 32};

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
                bb.setBox_id(box.optString("box_id"));
                bb.setSubject(box.optString("subject"));
                bb.setDescription(box.optString("description"));
                bb.setIs_stick(box.optString("is_stick"));
                bb.setBox_level(box.optString("box_level"));
                bb.setBox_updated(box.optString("box_updated"));
                bb.setItems(box.optInt("items"));
//                JSONArray tags = box.optJSONArray("tags");
//                int tag_size = tags.length();
//                List<BoxBean.Tags> info = new ArrayList<>();
//                for (int k = 0; k < tag_size; k++) {
//                    JSONObject tag = tags.optJSONObject(k);
//                    if (tag != null) {
//                        info.add(bb.getTag(tag.optString("name"), k + 1));
//                    }
//                }


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


                cycle = box.getInt("cycle");
                if (Arrays.asList(cyArr).contains(cycle)) {
                    if (cycle == 21)
                        name = "短线";
                    else if (cycle == 22)
                        name = "中线";
                    else if (cycle == 23)
                        name = "长线";
                    info.add(bb.getTag(name, 2));
                }

                risk = box.getInt("risk");
                if (Arrays.asList(rArr).contains(risk)) {
                    if (risk == 31)
                        name = "低风险";
                    else if (risk == 32)
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

    public static BlogBean getUserBlogs(JSONObject obj) {
        BlogBean bb = new BlogBean();
        JSONObject result = obj.optJSONObject("result");
        bb.page = result.optInt("page");
        bb.pages = result.optInt("pages");
        JSONArray blogs = result.optJSONArray("blogs");
        int size = blogs.length();
        List<BlogBean> infolist = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            JSONObject blog = blogs.optJSONObject(i);
            BlogBean blogBean = new BlogBean();
            blogBean.setImg(blog.optString("cover"));
            blogBean.setTitle(blog.optString("subject"));
            blogBean.setDate(blog.optString("posted_at"));
            blogBean.setBlog_id(blog.optString("blog_id"));
            blogBean.setLook_number(blog.optJSONObject("stat").optString("views"));
            blogBean.setPinglun(blog.optJSONObject("stat").optString("replys"));
            blogBean.setApp_link(blog.optJSONObject("link").optString("app"));
            blogBean.setWap_link(blog.optJSONObject("link").optString("wap"));
            blogBean.setIs_myself(true);
            infolist.add(blogBean);
        }
        bb.setList(infolist);
        return bb;
    }

    public static Videos GetsVideos(JSONObject obj) {
        try {
            Videos videos = new Videos();
            JSONObject object = obj.getJSONObject("result");
            videos.pages = object.getInt("pages");
            JSONArray jsonArray = object.getJSONArray("videos");
            List<Videos> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                Videos v = new Videos();
                v.setVideo_id(o.optInt("video_id"));
                v.setSeries_id(o.optInt("series_id"));
                v.setUser_id(o.optInt("user_id"));
                v.setGroup_id(o.optInt("group_id"));
                v.setIs_live(o.optInt("is_live"));
                v.setTeacher(o.optString("teacher"));
                v.setTitle(o.optString("title"));
                v.setSeries_name(o.optString("series_name"));
                v.setPrice(o.optInt("price"));
                v.setIs_fans(o.optInt("is_fans"));
                v.setStart_at(o.optInt("start_at"));
                v.setPic_play(o.optString("pic_play"));
                v.setPic_thumb(o.optString("pic_thumb"));
                v.setTag(o.optString("tag"));
                v.setIs_hot(o.optInt("is_live"));
                v.setIs_stick(o.optInt("is_stick"));
                v.setDiscount(o.optInt("discount"));
                v.setSubscribe(o.optInt("subscribe"));
                v.setBokecc_id(o.optString("bokecc_id"));
                v.setShare_img(o.optString("share_img"));
                v.setShare_url(o.optString("share_url"));
                v.setOrder(o.optInt("order"));
                JSONObject jsonObject = o.getJSONObject("content");
                JSONArray jsonArray1 = jsonObject.names();
                List<Videos.ContentBean> content = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    Videos.ContentBean bean = new Videos.ContentBean();
                    String title = jsonArray1.getString(j).trim();
                    bean.setTitle(title);
                    bean.setDesc(jsonObject.getString(title).trim());
                    content.add(bean);
                }
                v.setContent(content);
                list.add(v);
            }
            videos.setList(list);
            return videos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Invite getInviteList(JSONObject obj) {
        try {
            Invite invite = new Invite();
            JSONObject object = obj.getJSONObject("result");
            invite.pages = object.getInt("pages");
            JSONArray jsonArray = object.getJSONArray("invite_lists");
            List<Invite> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                Invite invite1 = new Invite();
                invite1.setNickname(o.optString("nickname"));
                invite1.setInvited_at(o.optString("invited_at"));
                invite1.setMpay(o.optDouble("mpay"));
                list.add(invite1);
            }
            JSONObject oo = obj.getJSONObject("stat");
            Invite.StatBean stat = new Invite.StatBean();
            stat.setMpay(oo.optDouble("mpay"));
            stat.setTotal(oo.optDouble("total"));
            stat.setValid(oo.optDouble("valid"));
            invite.setStat(stat);
            invite.setInvite_lists(list);
            return invite;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

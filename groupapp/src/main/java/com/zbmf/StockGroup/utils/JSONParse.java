package com.zbmf.StockGroup.utils;

import android.util.Log;

import com.zbmf.StockGroup.beans.Ask;
import com.zbmf.StockGroup.beans.AskBean;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.beans.ChatMessage;
import com.zbmf.StockGroup.beans.CommentBean;
import com.zbmf.StockGroup.beans.CouPonsRules;
import com.zbmf.StockGroup.beans.CouponsBean;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.DealsRecord;
import com.zbmf.StockGroup.beans.Dictum;
import com.zbmf.StockGroup.beans.DongmiBean;
import com.zbmf.StockGroup.beans.DzUser;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.HomeImage;
import com.zbmf.StockGroup.beans.Invite;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.MyTopicData;
import com.zbmf.StockGroup.beans.NewsFeed;
import com.zbmf.StockGroup.beans.NoReadMsgBean;
import com.zbmf.StockGroup.beans.Offine;
import com.zbmf.StockGroup.beans.PointsBean;
import com.zbmf.StockGroup.beans.Record;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.beans.ScreenMessage;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.StockBean;
import com.zbmf.StockGroup.beans.StockComments;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.beans.StockModeMenu;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.beans.TagBean;
import com.zbmf.StockGroup.beans.TraderYield;
import com.zbmf.StockGroup.beans.Traders;
import com.zbmf.StockGroup.beans.User;
import com.zbmf.StockGroup.beans.Vers;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.beans.VideoPrice;
import com.zbmf.StockGroup.beans.VideoTeacher;
import com.zbmf.StockGroup.beans.Yield;
import com.zbmf.StockGroup.constans.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
                JSONObject object = jsonArray.getJSONObject(i);
                Ask a = new Ask();
                a.setAsk_id(object.optString("ask_id"));
                a.setAsk_status(object.optString("ask_status"));
                a.setUser_id(object.optString("user_id"));
                a.setNickname(object.optString("nickname"));
                a.setAsk_content(object.optString("ask_content"));
                a.setTarget_id(object.optString("target_id"));
                a.setTarget_nickname(object.optString("target_nickname"));
                a.setPosted_at(object.optString("posted_at"));
                if (object.has("post")) {
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
                    if (a.getPost().getIs_private()
                            && a.getAsk_id().equals(SettingDefaultsManager.getInstance().UserId())) {
                        asks.add(a);
                    } else {
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
                g.setIs_fans(object.optInt("is_fans"));
                g.setIs_recommend(1);
                groups.add(g);
            }
            group.setList(groups);
            return group;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BlogBean getBlogDetail(JSONObject blog) {
        BlogBean blogBean = new BlogBean();
        blogBean.setImg(blog.optString("cover"));
        blogBean.setTitle(blog.optString("subject"));
        blogBean.setDate(blog.optString("posted_at"));
        if (blog.has("stat")) {
            JSONObject stat = blog.optJSONObject("stat");
            blogBean.setLook_number(stat.optString("views"));
            blogBean.setPinglun(stat.optString("replys"));
        }
        if (blog.has("link")) {
            JSONObject link = blog.optJSONObject("link");
            blogBean.setApp_link(link.optString("app"));
            blogBean.setWap_link(link.optString("wap"));
        }
        blogBean.setBlog_id(blog.optString("blog_id"));
        if (blogBean.getBlog_id().contains(SettingDefaultsManager.getInstance().UserId())) {
            blogBean.setIs_myself(true);
        } else {
            blogBean.setIs_myself(false);
        }
        return blogBean;
    }

    public static List<BlogBean> getBlogBean(JSONArray jsonArray) {
        List<BlogBean> infolist = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            infolist.add(getBlogDetail(jsonArray.optJSONObject(i)));
        }
        return infolist;
    }

    public static NewsFeed blog(JSONObject obj) {
        NewsFeed newsFeed = new NewsFeed();
        try {
            JSONArray jsonArray = null;
            if (!obj.isNull("newsfeeds")) {
                jsonArray = obj.getJSONArray("newsfeeds");
            }
            if (!obj.isNull("blogs")) {
                jsonArray = obj.getJSONArray("blogs");
            }
            List<NewsFeed> ns = new ArrayList<>();
            if (jsonArray != null) {
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
            int kchart = obj.optInt("kchart");
            int group_kchart = obj.optInt("group_kchart");
            int emergency = obj.optInt("emergency");
            vers.setKchart(kchart);
            SettingDefaultsManager.getInstance().setIsShowKlineChart(group_kchart);//圈子K线图
            SettingDefaultsManager.getInstance().setIsShowFans(emergency);
            vers.setGroup_kchart(group_kchart);
            vers.setEmergency(emergency);
            if (obj.has("logics")) {
                JSONObject object = obj.getJSONArray("logics").getJSONObject(0);
                Vers.Logics logics = new Vers.Logics();
                logics.state = object.optString("state");
                logics.intro = object.optString("intro");
                vers.setLogics(logics);
            }
            if (obj.has("address")) {
                JSONObject address = obj.optJSONObject("address");
                if (address.has("match")) {
                    JSONObject matchoj = address.optJSONObject("match");
                    Vers.address match = new Vers.address();
                    match.setApi(matchoj.optString("api"));
                    match.setHost(matchoj.optString("host"));
                    vers.setMatch(match);
                }
                if (address.has("group")) {
                    JSONObject groupoj = address.optJSONObject("group");
                    Vers.address group = new Vers.address();
                    group.setApi(groupoj.optString("api"));
                    group.setHost(groupoj.optString("host"));
                    vers.setGroup(group);
                }
                if (address.has("www")) {
                    JSONObject wwwoj = address.optJSONObject("www");
                    Vers.address www = new Vers.address();
                    www.setApi(wwwoj.optString("api"));
                    www.setHost(wwwoj.optString("host"));
                    vers.setWww(www);
                }
                if (address.has("passport")) {
                    JSONObject passportoj = address.optJSONObject("passport");
                    Vers.address passport = new Vers.address();
                    passport.setApi(passportoj.optString("api"));
                    passport.setHost(passportoj.optString("host"));
                    vers.setPassport(passport);
                }
                if (address.has("stock")) {
                    JSONObject stockoj = address.optJSONObject("stock");
                    Vers.address stock = new Vers.address();
                    stock.setApi(stockoj.optString("api"));
                    stock.setHost(stockoj.optString("host"));
                    vers.setStock(stock);
                }
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
                    stat.setAvatar(o1.optString("avatar"));
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

    public static List<Group> careGroups(JSONArray obj) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < obj.length(); i++) {
            JSONObject object = obj.optJSONObject(i);
            Group g = new Group();
            g.setId(object.optString("id"));
            g.setName(object.optString("name"));
            g.setNick_name(object.optString("nickname"));
            g.setAvatar(object.optString("avatar"));
            g.setIs_close(object.optInt("is_close"));
            g.setIs_private(object.optInt("is_private"));
            g.setRoles(object.optInt("role"));
            g.setIs_fans(object.optInt("is_fans"));
            g.setIs_recommend(1);
            groups.add(g);
        }
        return groups;
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

    /**
     * 获取圈子列表
     *
     * @param jsonArray
     * @return
     */
    public static List<TagBean.ChildrenTag> getGroupTags(JSONArray jsonArray) {
        int size = jsonArray.length();
        List<TagBean.ChildrenTag> groupTags = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            JSONObject object = jsonArray.optJSONObject(i);
            groupTags.add(new TagBean.ChildrenTag(object.optString("nickname"), object.optString("id"), 0));
        }
        return groupTags;
    }

    /**
     * 获取圈子列表
     *
     * @param jsonArray
     * @return
     */
    public static List<Group> getGroupList(JSONArray jsonArray) {
        int size = jsonArray.length();
        List<Group> groupList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            JSONObject object = jsonArray.optJSONObject(i);
            groupList.add(getGroup(object));
        }
        return groupList;
    }

    /**
     * 获取视频老师列表
     *
     * @param jsonArray
     * @return
     */
    public static List<VideoTeacher> getVideoTeacherList(JSONArray jsonArray) {
        List<VideoTeacher> videoTeachers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                videoTeachers.add(getVideoTeacher(jsonArray.optJSONObject(i)));
            }
        }
        return videoTeachers;
    }

    /**
     * "id": 1745093,
     * "name": "萝卜寻金",
     * "is_hot": 0,
     * "teacher_head": "http:\/\/oss2.zbmf.com\/avatar\/1745093-298e9a5776ff7s.jpg",
     * "attention": 0,
     * "is_group": 1,
     * "count_video": 3
     *
     * @param jsonObject
     * @return
     */
    public static VideoTeacher getVideoTeacher(JSONObject jsonObject) {
        return new VideoTeacher(
                jsonObject.optString("id"),
                jsonObject.optString("name"),
                jsonObject.optInt("is_hot"),
                jsonObject.optString("teacher_head"),
                jsonObject.optInt("attention"),
                jsonObject.optInt("is_group"),
                jsonObject.optInt("count_video"),
                jsonObject.optString("description")
        );
    }

    /**
     * 获取圈子
     *
     * @param object
     * @return
     */
    public static Group getGroup(JSONObject object) {
        Group group = new Group();
        group.setNotice(object.optString("notice"));
        group.setId(object.optString("id"));
        group.setName(object.optString("name"));
        group.setNick_name(object.optString("nickname"));
        group.setAvatar(object.optString("avatar"));
        group.setIs_close(object.optInt("is_close"));
        group.setIs_private(object.optInt("is_private"));
        group.setDescription(object.optString("description"));
        group.setRoles(object.isNull("role") ? object.optInt("roles") : object.optInt("role"));
        group.setFans_level(object.optInt("fans_level"));
        group.setDay_mapy(object.optLong("day_mpay"));
        group.setMonth_mapy(object.optLong("month_mpay"));
        group.setEnable_day(object.optInt("enable_day"));
        group.setEnable_point(object.optInt("enable_point"));
        group.setMax_point(object.optInt("max_point"));
        group.setFans_activity(object.optString("fans_activity"));
        group.setFans_countent(object.optString("fans_content"));
        group.setPoint_desc(object.optString("point_desc"));
        group.setMax_mpay(object.optLong("max_mpay"));
        group.setIs_recommend(object.optInt("is_followed"));
        group.setIs_online(object.optInt("is_online"));
        group.setCertificate(object.optString("certificate"));
        group.setContent(object.optString("content"));
        group.setStyle(object.optString("style"));
        group.setTags(object.optString("tags"));
        group.setFollow_num(object.optString("follow_num"));
        group.setIs_fans(object.optInt("is_fans"));
        group.setCoupon(object.optString("coupon"));
        group.setFans_date(object.optString("fans_expire_at"));
        group.setVotes(object.optDouble("votes"));
        group.setRank(object.optDouble("rank"));
        group.setToday_sign(object.optInt("today_sign"));
        return group;
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

    public static Video getVideo(JSONObject v) {
        Video video = new Video();
        if (v != null) {
            video.setVideoId(v.optString("video_id"));
            video.setSeries_id(v.optInt("series_id"));
            video.setVideoImg(v.optString("pic_play"));
            video.setUser_id(v.optString("user_id"));
            video.setGroup_id(v.optString("group_id"));
            video.setAvatar(v.optString("avatar"));
            video.setVideoGroupname(v.optString("teacher"));
            video.setVideoName(v.optString("title"));
            video.setVideoParticipation(v.optString("subscribe"));
            video.setIs_live(v.optInt("is_live"));
            video.setStart_time(v.optLong("start_at"));
            if (video.getIs_live()) {
                Date date = new Date();
                video.setVideoDate(DateUtil.getLiveTime(v.optLong("start_at") * 1000));
                long nowtime = date.getTime() / 1000;
                int seconds = (int) (video.getStart_time() - nowtime);
                if (seconds <= 0) {
                    video.setVideoPlayType("正在直播...");
                } else {
                    video.setVideoPlayType("即将开始");
                }
            } else {
                video.setVideoDate(DateUtil.getVedioTime(v.optLong("start_at") * 1000));
            }
            video.setVideoPrice(v.optDouble("price"));
            video.setVideoPriceType(v.optDouble("discount"));

            if (v.optDouble("discount") != 100) {
                double price = v.optDouble("price") * v.optDouble("discount") / 100;
                video.setVideonewPrice(price);
            }
            video.setIs_fans(v.optInt("is_fans"));
            video.setIs_group(v.optInt("is_group"));

            video.setBokecc_id(v.optString("bokecc_id"));
            video.setOrder(v.optInt("order"));
            video.setFollow(v.optInt("follow"));

            video.setShare_url(v.optString("share_url"));
            video.setShare_img(v.optString("pic_thumb"));
            video.setContent(v.optString("content"));
            video.setVideoType(v.optInt("is_hot"));
            video.setVideo_tag(v.optString("tag"));
        }
        return video;
    }

    public static CouponsBean getCouponsBean(JSONObject coupon) {
        CouponsBean cb = new CouponsBean();
        cb.setCoupon_id(coupon.optInt("coupon_id"));
        cb.setId(coupon.optString("id"));
        cb.setAvatar(coupon.optString("avatar"));
        cb.setNickName(coupon.optString("nickname"));
        cb.setCouponsName(coupon.optString("name"));
        cb.setSubject(coupon.optString("subject"));
        cb.setSumary(coupon.optString("summary"));
        cb.setKind(coupon.optInt("kind"));
        cb.setTake_id(coupon.optInt("take_id"));
        cb.setType("coupons");
        cb.setIs_take(coupon.optInt("is_taken") == 1);
        cb.setStart_at(coupon.optString("start_at"));
        cb.setEnd_at(coupon.optString("end_at"));
        cb.setMinimum(coupon.optDouble("minimum"));
        cb.setMaximum(coupon.optDouble("maximum"));
        cb.setCateogry(coupon.optInt("category"));
        cb.setStatus(coupon.optInt("status"));
        cb.setCan_use(true);
        if (!coupon.isNull("rules")) {
            JSONArray rules = coupon.optJSONArray("rules");
            List<CouPonsRules> riles = new ArrayList<CouPonsRules>();
            int rules_size = rules.length();
            for (int k = 0; k < rules_size; k++) {
                CouPonsRules cr = new CouPonsRules();
                JSONObject rule = rules.optJSONObject(k);
                if (!rule.isNull("maximum")) {
                    cr.setAmount(rule.optDouble("amount"));
                    cr.setCateogry(rule.optInt("category"));
                    cr.setIs_ratio(rule.optInt("is_ratio"));
                    cr.setIs_incr(rule.optInt("is_incr"));
                    cr.setMaximum(rule.optDouble("maximum"));
                    riles.add(cr);
                }
            }
            cb.setRiles(riles);
            if (rules_size > 0) {
                cb.setRuls(riles.get(0));
            }
        }
        if (!coupon.isNull("rule")) {
            JSONObject rule = coupon.optJSONObject("rule");
            CouPonsRules cr = new CouPonsRules();
            cr.setAmount(rule.optDouble("amount"));
            cr.setCateogry(rule.optInt("category"));
            cr.setIs_ratio(rule.optInt("is_ratio"));
            cr.setIs_incr(rule.optInt("is_incr"));
            cr.setMaximum(rule.optDouble("maximum"));
            cb.setRuls(cr);
        }
        return cb;
    }

    public static List<CouponsBean> getCouponsList(JSONArray coupons) {
        int size = coupons.length();
        List<CouponsBean> couponslist = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            couponslist.add(getCouponsBean(coupons.optJSONObject(i)));
        }
        return couponslist;
    }

    /**
     * 获取视频列表
     *
     * @param array
     * @return
     */
    public static List<Video> getVideoList(JSONArray array) {
        List<Video> infolist = new ArrayList<>();
        if (array != null) {
            int size = array.length();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = array.optJSONObject(i);
                Video video = getVideo(jsonObject);
                infolist.add(video);
            }
        }
        return infolist;
    }

    public static List<HomeImage> getHomeImageList(JSONArray jsonArray) {
        List<HomeImage> homeImages = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                homeImages.add(getHomeImage(jsonArray.optJSONObject(i)));
            }
        }
        return homeImages;
    }

    public static HomeImage getHomeImage(JSONObject jsonObject) {
        HomeImage h = new HomeImage();
        h.setType("link");
        h.setId(jsonObject.optString("advert_id"));
        h.setTitle(jsonObject.optString("subject"));
        h.setLink_url(jsonObject.optString("jump_url"));
        h.setImg_url(jsonObject.optString("img_url"));
        return h;
    }

    /**
     * 获取我订阅的
     */
    public static List<Video> getPayVideoList(JSONObject result) {
        List<Video> infolist = new ArrayList<>();
        if (!result.isNull("videos")) {
            infolist.addAll(getVideoList(result.optJSONArray("videos")));
        }
        if (!result.isNull("series")) {
            infolist.addAll(getSeriesList(result.optJSONArray("series")));
        }
        return infolist;
    }

    /**
     * 获取专辑列表
     *
     * @param array
     * @return
     */
    public static List<Video> getSeriesList(JSONArray array) {
        List<Video> infolist = new ArrayList<>();
        int size = array.length();
        for (int i = 0; i < size; i++) {
            JSONObject jsonObject = array.optJSONObject(i);
            Video video = new Video();
            video.setIs_series(1);
            video.setSeriesVideo(getSeries(jsonObject));
            infolist.add(video);
        }
        return infolist;
    }

    /**
     * 获取专辑信息
     *
     * @param object
     * @return
     */
    public static Video.SeriesVideo getSeries(JSONObject object) {
        return new Video.SeriesVideo(
                object.optString("phase"),
                object.optString("new_phase"),
                object.optString("name"),
                object.isNull("teacher_name") ? object.optString("teachername") : object.optString("teacher_name"),
                object.optInt("status"),
                object.optString("share_url"),
                object.optString("series_id"),
                object.optString("pic_play"),
                object.optString("pic_thumb"),
                DateUtil.getLiveTime(object.optLong("created_at") * 1000),
                object.optString("teacher_head"),
                object.optInt("series_num"),
                object.optInt("commit"),
                object.optInt("is_play"),
                object.optDouble("price"),
                object.optDouble("price") * object.optInt("discount") / 100,
                object.optInt("discount"),
                getVideo(object.optJSONObject("video"))
        );
    }

    /**
     * 获取标签列表
     *
     * @param array
     * @return
     */
    public static List<TagBean> getTagBeanlist(JSONArray array) {
        List<TagBean> tagBeenlist = new ArrayList<>();
        int size = array.length();
        for (int i = 0; i < size; i++) {
            tagBeenlist.add(getTagBean(array.optJSONObject(i)));
        }
        return tagBeenlist;
    }

    /**
     * 视频购买信息
     *
     * @param object
     * @return
     */
    public static VideoPrice getVideoPrice(JSONObject object) {
        return new VideoPrice(
                object.optDouble("video_price"),
                object.optInt("video_discount"),
                object.optString("video_start_at"),
                object.optDouble("series_price"),
                object.optInt("series_discount"),
                object.optString("series_name"),
                object.optInt("series_phase"),
                object.optInt("series_status"),
                object.optDouble("group_price"),
                object.optString("group_name")
        );
    }

    public static Screen getScreen(JSONObject obj) {
        JSONArray prices = obj.optJSONArray("price");
        List<Screen.Prce> pricelist = new ArrayList<>();
        for (int i = prices.length() - 1; i >= 0; i--) {
            JSONObject price = prices.optJSONObject(i);
            pricelist.add(new Screen.Prce(price.optString("price_id")
                    , price.optDouble("price")
                    , price.optInt("days")
                    , price.optDouble("prime_price")
                    , price.optString("unit")
                    , price.optInt("is_discount")
                    , price.optLong("expire_at")));
        }
        Log.i("--TAG", "---name :" + obj.optString("name"));
        return new Screen(obj.optDouble("day_yield")
                , obj.optString("descripition")
                , obj.optLong("end_at")
                , obj.optDouble("month_yield")
                , obj.optString("name")
                , obj.optString("screen_id")
                , obj.optDouble("sh_index")
                , obj.optDouble("sh_yield")
                , obj.optString("total_stock")
                , obj.optDouble("total_yield")
                , obj.optDouble("win_rate")
                , obj.optInt("win_stock")
                , obj.optInt("order_status")
                , obj.optDouble("m_prime_price")
                , obj.optDouble("m_price")
                , obj.optInt("is_discount")
                , obj.optInt("is_expire")
                , obj.optLong("expire_at")
                , obj.optLong("join_at")
                , obj.optInt("is_buy")
                , pricelist
        );
    }

    public static List<Screen> getScreenList(JSONArray jsonArray) {
        List<Screen> info = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            info.add(getScreen(jsonArray.optJSONObject(i)));
        }
        return info;
    }

    public static List<Screen> getScreenList(JSONArray jsonArray, int maxLength) {
        List<Screen> info = new ArrayList<>();
        int size = jsonArray.length();
        if (size > maxLength) {
            size = maxLength;
        }
        for (int i = 0; i < size; i++) {

            info.add(getScreen(jsonArray.optJSONObject(i)));
        }
        return info;
    }

    /**
     * 筛选标签
     *
     * @param object
     * @return
     */
    public static TagBean getTagBean(JSONObject object) {
        JSONArray array = object.optJSONArray("tag");
        List<TagBean.ChildrenTag> childrenTags = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject tag = array.optJSONObject(i);
            TagBean.ChildrenTag childrenTag = new TagBean.ChildrenTag(tag.optString("name"), tag.optString("id"), tag.optInt("is_hot", 0));
            childrenTags.add(childrenTag);
        }
        return new TagBean(object.optString("value"), object.optString("name"), childrenTags);
    }

    public static Invite getInviteList(JSONObject obj) {
        try {
            Invite invite = new Invite();
            JSONObject object = obj.getJSONObject("result");
            invite.setPages(object.getInt("pages"));
            invite.setPage(object.getInt("page"));
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
            LogUtil.e(e.getMessage());
            return null;
        }
    }

    public static List<TagBean.ChildrenTag> getStockTag(JSONArray jsonArray) {
        List<TagBean.ChildrenTag> tags = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.optJSONObject(i);
            tags.add(new TagBean.ChildrenTag(o.optString("name"), o.optString("symbol"), 0));
        }
        return tags;
    }

    public static List<Ask> getAskList(JSONArray jsonArray) {
        List<Ask> askList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.optJSONObject(i);
            Ask ask = new Ask();
            ask.setAsk_content(o.optString("ask_content"));
            ask.setNickname(o.optString("ask_nickname"));
            ask.setPosted_at(o.optString("ask_time"));
            Ask.Post post = new Ask.Post();
            post.setNickname(o.optString("post_nickname"));
            post.setPost_content(o.optString("post_content"));
            post.setPosted_at(o.optString("post_time"));
            post.setPost_id(o.optString("post_user_id"));
            ask.setPost(post);
            askList.add(ask);
        }
        return askList;
    }

    public static List<ScreenMessage> getScreenMessageList(JSONArray jsonArray) {
        List<ScreenMessage> infolist = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            infolist.add(getScreenMessage(jsonArray.optJSONObject(i)));
        }
        return infolist;
    }

    /**
     * 获取股票产品信息
     *
     * @param jsonObject
     * @return
     */
    public static ScreenMessage getScreenMessage(JSONObject jsonObject) {
        return new ScreenMessage(jsonObject.optString("content")
                , jsonObject.optString("msg_id")
                , jsonObject.optString("screen_id")
                , jsonObject.optString("subject")
                , jsonObject.optString("url")
                , jsonObject.optLong("created_at")
                , jsonObject.optString("screen_name")
        );
    }

    /**
     * 获取比赛信息
     *
     * @param oj
     * @return
     */
    public static MatchInfo getMatchMessage(JSONObject oj) {
        if (oj.optString("status").equals("ok")) {
            MatchInfo mbs = new MatchInfo();
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

            mbs.setDay_rank(oj.optInt("day_rank"));
            mbs.setWeek_rank(oj.optInt("week_rank"));
            mbs.setMonth_rank(oj.optString("month_rank"));
            mbs.setTotal_rank(oj.optString("total_rank"));

            mbs.setHolds(oj.optString("holds"));
            mbs.setRecords(oj.optString("records"));
            mbs.setTrusts(oj.optString("trusts"));
            mbs.setHalf(oj.optString("half"));
            mbs.setAvg_week_yield(oj.optString("avg_week_yield"));
            mbs.setAvg_month_yield(oj.optString("avg_month_yield"));
            mbs.setCount_players(oj.optInt("count_players"));
            mbs.setNew_announcement(oj.optString("new_announcement"));
            mbs.setPaynum(oj.optDouble("paynum"));
            return mbs;
        } else {
            return null;
        }
    }

    //获取比赛信息，接口替换后的解析
    public static MatchInfo getMatchMessage1(JSONObject oj) {
        if (oj.optString("status").equals("ok")) {
            MatchInfo mi = new MatchInfo();
            JSONObject result = oj.optJSONObject("result");
            if (result != null) {
                mi.setUser_id(result.optString("user_id"));
                mi.setMatch_id(result.optString("match_id"));
                mi.setMatch_name(result.optString("match_name"));
                mi.setStart_at(result.optString("start_at"));
                mi.setEnd_at(result.optString("end_at"));
                mi.setNickname(result.optString("nickname"));
                mi.setAvatar(result.optString("avatar"));
                mi.setTruename(result.optString("truename"));
                mi.setMobile(result.optString("mobile"));
                mi.setTotal(result.optDouble("total"));
                mi.setInit(result.optString("init"));
                mi.setPosition(result.optString("position"));
                mi.setUnfrozen(result.optDouble("unfrozen"));
                mi.setFrozen(result.optDouble("frozen"));
                mi.setDeal(result.optString("deal"));
                mi.setStocks_value(result.optString("stocks_value"));
                mi.setTotal_yield(result.optDouble("total_yield"));
                mi.setDay_yield(result.optDouble("day_yield"));
                mi.setWeek_yield(result.optDouble("week_yield"));
                mi.setMonth_yield(result.optDouble("month_yield"));
                mi.setDay_rank(result.optInt("day_rank"));
                mi.setWeek_rank(result.optInt("week_rank"));
                mi.setMonth_rank(result.optString("month_rank"));
                mi.setTotal_rank(result.optString("total_rank"));
                mi.setHolds(result.optString("holds"));
                mi.setRecords(result.optString("records"));
                mi.setPlayers(result.optString("players"));
                mi.setCanusecard(result.optString("canusecard"));
                mi.setTemplate(result.optString("template"));
                return mi;
            }
        }
        return null;
    }

    /**
     * 获取股票信息
     *
     * @param oj
     * @return
     */
    public static StockholdsBean getStockHoldes(JSONObject oj) {
        StockholdsBean sbl = new StockholdsBean();
        sbl.setId(oj.optString("id"));
        sbl.setSymbol(oj.optString("symbol"));
        sbl.setName(oj.optString("name"));
        sbl.setCuurent(oj.optDouble("current", 0));
        sbl.setColose(oj.optDouble("close", 0));
        sbl.setPrice_buy(oj.has("price") ? oj.optDouble("price", 0) : oj.optDouble("price_buy", 0));
        sbl.setPrice2(oj.optDouble("price2", 0));
        sbl.setPrice_float(oj.optDouble("price_float", 0));
        sbl.setPrice_sell(oj.has("price") ? oj.optDouble("price", 0) : oj.optDouble("price_sell", 0));
        sbl.setProfit(oj.has("gain") ? oj.optDouble("gain", 0) : oj.optDouble("profit", 0));
        sbl.setYield_float(oj.has("gain_yield") ? oj.optDouble("gain_yield", 0) : oj.optDouble("yield_float", 0));
        sbl.setVolumn_total(oj.optString("volumn_total"));
        sbl.setVolumn_infrozen(oj.optString("volumn_unfrozen"));
        sbl.setCreate_at(oj.optString("created_at"));
        sbl.setIs_buy(oj.optString("is_buy"));
        sbl.setIs_show(oj.optString("is_show"));
        sbl.setComment_count(oj.optInt("comment_count"));
        sbl.setVolumn(oj.optString("volumn"));
        sbl.setPosted_at(oj.optString("created_at"));
        if (oj.optString("action").equals("买入")) {
            sbl.setType("2");
        } else {
            sbl.setType("3");
        }
        return sbl;
    }

    /**
     * 获取持仓数据
     *
     * @param jsonArray
     * @return
     */
    public static List<StockholdsBean> getHolder(JSONArray jsonArray) {
        List<StockholdsBean> infolist = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                infolist.add(getStockHoldes(jsonArray.optJSONObject(i)));
            }
        }
        return infolist;
    }

    public static StockComments getComments(JSONObject oj) {
        StockholdsBean stockholdsBean = new StockholdsBean();
        stockholdsBean.setName(oj.optString("name"));
        stockholdsBean.setSymbol(oj.optString("symbol"));
        stockholdsBean.setComment_count(oj.optInt("count_comments"));
        return new StockComments(
                oj.optString("id")
                , oj.optString("user_id")
                , oj.optString("nickname")
                , oj.optString("user_img")
                , oj.optString("desc")
                , oj.optLong("create_at") * 1000,
                stockholdsBean);
    }

    public static List<StockComments> getCommentsList(JSONArray jsonArray) {
        List<StockComments> getCommentsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            getCommentsList.add(getComments(jsonArray.optJSONObject(i)));
        }
        return getCommentsList;
    }

    /**
     * @param jsonObject
     * @return
     */
    public static DealSys getDealSys(JSONObject jsonObject) {
        int type = 0;
        if (jsonObject.optString("action").equals("买入")) {
            type = 2;
        } else if (jsonObject.optString("action").equals("卖出")) {
            type = 3;
        } else {
            type = jsonObject.optInt("action");
        }
        return new DealSys(
                jsonObject.optString("user_id")
                , type
                , jsonObject.optString("nickname")
                , jsonObject.optString("user_img")
                , jsonObject.has("posted_at") ? jsonObject.optString("posted_at") : jsonObject.optString("date")
                , jsonObject.has("stock_name") ? jsonObject.optString("stock_name") : jsonObject.optString("name")
                , jsonObject.optString("symbol")
                , jsonObject.optString("count_comments")
                , jsonObject.optDouble("price")
                , jsonObject.optDouble("gain_yield")
                , jsonObject.optInt("volumn_total")
        );
    }

    public static List<DealSys> getDealSysList(JSONArray jsonArray) {
        List<DealSys> getDealSysList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            getDealSysList.add(getDealSys(jsonArray.optJSONObject(i)));
        }
        return getDealSysList;
    }

    public static List<Yield> getYieldList(JSONArray jsonArray) {
        List<Yield> yields = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            yields.add(getYield(jsonArray.optJSONObject(i)));
        }
        return yields;
    }

    public static Yield getYield(JSONObject oj1) {
        Yield y = new Yield();
        y.setUser(oj1.optString("user_id"));
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
        if (oj1.has("last_deal")) {
            JSONObject deals = oj1.optJSONObject("last_deal");
            if (deals != null) {
                y.setDealSys(new DealSys(
//                        oj1.optString("user")
                        deals.optInt("type")
//                        ,oj1.optString("nickname")
//                        ,oj1.optString("avatar")
                        , DateUtil.getTime(deals.optLong("created_at") * 1000, Constants.MM_dd_HH_mm)
                        , deals.optString("name")
                        , deals.optString("symbol")
//                        ,oj1.optString("comment_count")
//                        ,oj1.optDouble("price")
//                        ,oj1.optDouble("gain_yield")
//                        ,oj1.optInt("volumn_total")
                ));
            }
        }
        return y;
    }

    public static Stock getStockRealtimeInfo(JSONObject json) {
        Stock s = new Stock();
        if (json.optString("status").equals("ok")) {
            JSONObject obj = json.optJSONObject("result");
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
            s.setMsg(json.optJSONObject("err").optString("msg"));
        }
        return s;
    }

    public static StockholdsBean getOrderList(JSONObject json) {
        try {
            StockholdsBean stockholdsBean = new StockholdsBean();
            if (json.optString("status").equals("ok")) {
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
                            sbl.setPrice2(oj.optDouble("price", 0));
                            sbl.setPrice_buy(oj.optDouble("price_buy", 0));
                            sbl.setPrice_sell(oj.optDouble("price_sell", 0));
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
                stockholdsBean.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return stockholdsBean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<DealsRecord> getWinRecords1(JSONArray array) {
        if (array != null) {
            List<DealsRecord> deals = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                DealsRecord dealsRecord = new DealsRecord();
                JSONObject jsonObject = array.optJSONObject(i);
                dealsRecord.setId(jsonObject.optString("id"));
                dealsRecord.setSymbol(jsonObject.optString("symbol"));
                dealsRecord.setName(jsonObject.optString("name"));
                dealsRecord.setVolumn(jsonObject.optString("volumn"));
                dealsRecord.setPrice_buy(jsonObject.optString("price"));
                dealsRecord.setPrice_buy(jsonObject.optString("price_buy"));
                dealsRecord.setPrice_sell(jsonObject.optString("price_sell"));
                dealsRecord.setContract_id(jsonObject.optInt("contract_id"));
                dealsRecord.setType(jsonObject.optInt("type"));
                dealsRecord.setProfit(jsonObject.optDouble("profit"));
                dealsRecord.setPosted_at(jsonObject.optString("posted_at"));
                deals.add(dealsRecord);
            }
            return deals;
        } else {
            return null;
        }
    }

    public static Record getWinRecords(JSONObject json) {
        try {
            Record record = new Record();
            if (json.optString("status").equals("ok")) {
                if (json.has("result")) {
                    JSONObject obj = json.optJSONObject("result");
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
                                r.setMatch(getMatchMessage(oj));
                            }
                            records.add(r);
                        }
                        record.setRecords(records);
                    }
                }
            } else {
                record.setCode(json.optJSONObject("err").optInt("code"));
                record.setMsg(json.optJSONObject("err").optString("msg"));
            }
            return record;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dictum getDictum(JSONObject object) {
        return new Dictum(object.optString("dictum_id")
                , object.optString("nickname")
                , object.optString("avatar")
                , object.optString("tags")
                , object.optInt("online_status")
                , object.optInt("dictum_num")
                , object.optLong("showtime") * 1000
                , object.optString("user_advice")
                , object.optString("zbmf_advice")
                , object.optLong("created_at") * 1000
                , object.optInt("dictum_total")
                , object.optString("group_id")
        );
    }

    /**
     * 获取操盘高手
     *
     * @param jsonArray
     * @return
     */
    public static List<Traders> getTradersList(JSONArray jsonArray) {
        List<Traders> infolist = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Traders traders = getTraders(jsonArray.optJSONObject(i));
            if (traders != null) {
                infolist.add(traders);
            }
        }
        return infolist;
    }

    public static List<Traders> getTradersList(JSONArray jsonArray, int maxLength) {
        List<Traders> infolist = new ArrayList<>();
        int size = jsonArray.length();
        if (size > maxLength) {
            size = maxLength;
        }
        for (int i = 0; i < size; i++) {
            Traders traders = getTraders(jsonArray.optJSONObject(i));
            if (traders != null) {
                infolist.add(traders);
            }
        }
        return infolist;
    }

    public static Traders getTraders(JSONObject object) {
        if (object == null) {
            return null;
        }
        return new Traders(object.optInt("rank")
                , object.optString("user_id")
                , object.optString("nickname")
                , object.optString("avatar")
                , object.optDouble("total_yield")
                , object.optDouble("deal_success")
                , object.optString("deal_total")
                , object.optString("join_at")
                , object.optDouble("price_month")
                , object.optString("profile")
                , object.optString("expired_at")
        );
    }

    public static TraderYield getTraderYield(JSONObject object) {
        return new TraderYield(
                object.optInt("deal_days")
                , object.optInt("deal_total")
                , object.optDouble("deal_success")
                , object.optDouble("total_yield")
                , object.optDouble("win_index")
                , object.optDouble("index_yield")
                , object.optDouble("total_money")
                , object.optInt("hold_num")
                , object.optDouble("position"));
    }

    public static List<StockModeMenu> getModeProductList(JSONArray jsonArray) {
        List<StockModeMenu> modeProductList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            StockModeMenu stockModeMenu = new StockModeMenu();
            stockModeMenu.setName(jsonObject.optString("name"));
            stockModeMenu.setProduct_id(jsonObject.optInt("product_id"));
            JSONArray time = jsonObject.optJSONArray("time");
            List<StockModeMenu.Time> times = new ArrayList<>();
            for (int j = 0; j < time.length(); j++) {
                StockModeMenu.Time time1 = new StockModeMenu.Time();
                time1.setTime(time.optString(j));
                times.add(time1);
            }
            stockModeMenu.setTimes(times);
            modeProductList.add(stockModeMenu);
        }
        return modeProductList;
    }

    public static List<StockMode> getStockModeList(JSONArray jsonArray) {
        List<StockMode> stockModes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            stockModes.add(new StockMode(
                    jsonObject.optString("name"),
                    jsonObject.optString("symbol"),
                    jsonObject.optDouble("current"),
                    jsonObject.optDouble("yield"),
                    jsonObject.optDouble("vrsi"),
                    jsonObject.optDouble("ywpi"),
                    jsonObject.optInt("repeat")
            ));
        }
        return stockModes;
    }

    //解析MyTopicData
    public static ArrayList<MyTopicData> getMyTopicData(JSONArray array) {

        return null;
    }

    //解析MyTopicData
    public static List<PointsBean> getPointList(JSONArray array) {
        List<PointsBean> list = new ArrayList<>();
//        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.optJSONObject(i);
          /*  JSONArray img_keys1 = jsonObject.optJSONArray("img_keys");
            if (img_keys1 != null) {
                for (int i1 = 0; i1 < img_keys1.length(); i1++) {
                    try {
                        String string = img_keys1.getString(i);
                        imgs.add(string);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }*/
            list.add(new PointsBean(jsonObject.optInt("viewpoint_id"), jsonObject.optInt("topic_id"),
                    jsonObject.optInt("uid"), jsonObject.optInt("is_hit"), jsonObject.optInt("comment_count"),
                    jsonObject.optInt("hits"), jsonObject.optString("content"), jsonObject.optString("nickname"),
                    jsonObject.optString("avatar"), jsonObject.optString("img_keys"), jsonObject.optString("created_at"),
                    jsonObject.optString("title"),jsonObject.optInt("width"),jsonObject.optInt("height"),
                    jsonObject.optString("company"),jsonObject.optString("position")));
        }
        return list;
    }

    //解析话题列表
    public static List<MyTopicData> getMyTopicList(JSONArray topic_list){
        List<MyTopicData> topicList=new ArrayList<>();
        for (int i = 0; i < topic_list.length(); i++) {
            JSONObject jsonObject = topic_list.optJSONObject(i);
            int topic_id = jsonObject.optInt("topic_id");
            int type_id = jsonObject.optInt("type_id");
            int vp_number = jsonObject.optInt("vp_number");
            int users = jsonObject.optInt("users");
            int status = jsonObject.optInt("status");
            String img = jsonObject.optString("img");
            String name = jsonObject.optString("name");
            String title = jsonObject.optString("title");
            String body = jsonObject.optString("body");
            String created_at = jsonObject.optString("created_at");
            topicList.add(new MyTopicData(topic_id, type_id, vp_number, users,
                    status, img, name, title, body, created_at));
        }
        return topicList;
    }

    //解析获取评论列表
    public static List<CommentBean> getCommentList(JSONArray array){
        List<CommentBean> commentBeanList=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.optJSONObject(i);
            commentBeanList.add(new CommentBean(jsonObject.optInt("id"),jsonObject.optInt("viewpoint_id"),
                    jsonObject.optString("content"),jsonObject.optLong("uid"),jsonObject.optString("nickname"),
                    jsonObject.optString("avatar"),jsonObject.optInt("to_user"),jsonObject.optString("created_at"),
                    jsonObject.optString("company"),jsonObject.optString("position")));
        }
        return commentBeanList;
    }
    //解析未读消息的列表数据
    public static List<NoReadMsgBean> getNoMsgList(JSONArray jsonArray){
        List<NoReadMsgBean> list=new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            list.add(new NoReadMsgBean(jsonObject.optInt("msg_id"),jsonObject.optInt("user_id"),
                    jsonObject.optString("content"),jsonObject.optInt("is_read"),jsonObject.optString("created_at"),
                    jsonObject.optInt("type"),jsonObject.optString("nick_name"),jsonObject.optInt("viewpoint_id"),
                    jsonObject.optInt("topic_id")));
        }
        return list;
    }

    public static List<AskBean> getAskBeans(JSONArray jsonArray) {
        List<AskBean> askBeanList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            askBeanList.add(getAskBean(jsonArray.optJSONObject(i)));
        }
        return askBeanList;
    }

    public static AskBean getAskBean(JSONObject jsonObject) {
        AskBean askBean = new AskBean();
        askBean.setAsk_at(jsonObject.optString("ask_at"));
        askBean.setAsk_content(jsonObject.optString("ask_content"));
        askBean.setAsk_name(jsonObject.optString("ask_name"));
        askBean.setReply_at(jsonObject.optString("reply_at"));
        askBean.setReply_content(jsonObject.optString("reply_content"));
        askBean.setReply_name(jsonObject.optString("reply_name"));
        askBean.setSymbol(jsonObject.optString("symbol"));
        askBean.setStock_name(jsonObject.optString("stock_name"));
        return askBean;
    }

    public static List<StockBean> getStockBeanList(JSONArray jsonArray) {
        List<StockBean> stockBeanList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            stockBeanList.add(getStockBean(jsonArray.optJSONObject(i)));
        }
        return stockBeanList;
    }

    public static StockBean getStockBean(JSONObject jsonObject) {
        return new StockBean(jsonObject.optString("stock_name"),
                jsonObject.optString("symbol"));
    }

    public static List<StockBean> getTagList(JSONArray jsonArray) {
        List<StockBean> stockBeanList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            stockBeanList.add(getTag(jsonArray.optJSONObject(i)));
        }
        return stockBeanList;
    }

    public static StockBean getTag(JSONObject jsonObject) {
        return new StockBean(jsonObject.optString("tag_name"),
                jsonObject.optString("tag_id"));
    }

    public static List<DzUser> getDzUser(JSONArray jsonArray){
        List<DzUser> dzUsers=new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            dzUsers.add(new DzUser(jsonObject.optInt("id"), jsonObject.optString("nickname"),
                    jsonObject.optString("avatar"), jsonObject.optString("created_at")));
        }
        return dzUsers;
    }
//解析董秘信息
    public static DongmiBean getDongmiInfo(JSONObject jsonObject){
        return new DongmiBean(jsonObject.optInt("company_id"),jsonObject.optString("symbol"),
                jsonObject.optString("company_name"),jsonObject.optString("secretary"),jsonObject.optString("address"),
                jsonObject.optString("email"),jsonObject.optString("phone"),jsonObject.optString("industry"),
                jsonObject.optString("area"),jsonObject.optString("avatar"),jsonObject.optInt("reply"),
                jsonObject.optInt("reply_rank"),jsonObject.optInt("updated_at"));
    }


}

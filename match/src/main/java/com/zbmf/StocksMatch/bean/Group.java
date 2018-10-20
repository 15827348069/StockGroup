package com.zbmf.StocksMatch.bean;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xuhao
 * on 2017/1/14.
 */

public class Group implements Serializable{
    private String id;//圈子ID
    private String name;//圈子名称
    private String avatar;//圈子头像
    private String nick_name;//昵称
    private double day_mapy;//一天的铁粉价格
    private double month_mapy;//一月的铁粉价格
    private int enable_day;//是否开启按天付费
    private int is_close;//圈子是否已关闭
    private int  is_private;//是否是私密圈子
    private int enable_point;//是否开启积分抵扣
    private int max_point;//最大抵扣积分
    private int exclusives;//是否是独家圈主
    private String style;//风格
    private String certificate;//证券从业资格证书
    private String description;//圈子简介
    private String notice;//圈子公告
    private String fans_activity;
    private String fans_countent;
    private String point_desc;
    private double max_mpay;
    private int roles;//圈子角色
    private int fans_level;
    private Date live_history_date;
    private List<Group> list;
    private String index;
    private String pingyin;
    private int is_recommend;
    private int pages;
    private int unredcount;
    private String fans_date;
    private int blogs;
    private int fans;
    private int follows;
    private int chat;
    private int is_fans;
    private int is_online;
    private String subject;
    private String msg_id;
    private String content;
    private String follow_num;
    private String tags;
    private String coupon;
    private double votes;
    private double rank;
    private int today_sign;

    public boolean getToday_sign() {
        return today_sign==1;
    }

    public void setToday_sign(int today_sign) {
        this.today_sign = today_sign;
    }

    public double getVotes() {
        return votes;
    }

    public void setVotes(double votes) {
        this.votes = votes;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(String follow_num) {
        this.follow_num = follow_num;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean getIs_online() {
        return is_online==1;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getFans_date() {
        return fans_date;
    }

    public void setFans_date(String fans_date) {
        this.fans_date = fans_date;
    }

    public void setChat(int chat) {
        this.chat = chat;
    }

    public int getChat() {
        return chat;
    }

    public int getBlogs() {
        return blogs;
    }

    public void setBlogs(int blogs) {
        this.blogs = blogs;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getUnredcount() {
        return unredcount;
    }

    public void setUnredcount(int unredcount) {
        this.unredcount = unredcount;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }

    public Group(){

    }

    public boolean getIs_fans() {
        return is_fans==1;
    }

    public void setIs_fans(int is_fans) {
        this.is_fans = is_fans;
    }

    /**
     *
     * @param id
     * @param name
     * @param nick_name
     * @param avatar
     * @param exclusives
     */
    public Group(String id, String name, String nick_name, String avatar, int exclusives) {
        this.id = id;
        this.name = name;
        this.nick_name=nick_name;
        this.avatar = avatar;
        this.exclusives = exclusives;
    }

    public boolean is_recommend() {
        return is_recommend==1;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getPingyin() {
        return pingyin;
    }

    public void setPingyin(String pingyin) {
        this.pingyin = pingyin;
    }

    public int getExclusives() {
        return exclusives;
    }

    public void setExclusives(int exclusives) {
        this.exclusives = exclusives;
    }

    public void setList(List<Group> list) {
        this.list = list;
    }

    public List<Group> getList() {
        return list;
    }

    public Date getLive_history_date() {
        return live_history_date;
    }

    public void setLive_history_date(Date live_history_date) {
        this.live_history_date = live_history_date;
    }

    public String getPoint_desc() {
        return point_desc;
    }

    public void setPoint_desc(String point_desc) {
        this.point_desc = point_desc;
    }

    public double getMax_mpay() {
        return max_mpay;
    }

    public void setMax_mpay(double max_mpay) {
        this.max_mpay = max_mpay;
    }

    public String getFans_activity() {
        return fans_activity;
    }

    public void setFans_activity(String fans_activity) {
        this.fans_activity = fans_activity;
    }

    public String getFans_countent() {
        return fans_countent;
    }

    public void setFans_countent(String fans_countent) {
        this.fans_countent = fans_countent;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getFans_level() {
        return fans_level;
    }

    public void setFans_level(int fans_level) {
        this.fans_level = fans_level;
    }

    public int getRoles() {
        return roles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDay_mapy() {
        return day_mapy;
    }

    public void setDay_mapy(double day_mapy) {
        this.day_mapy = day_mapy;
    }

    public double getMonth_mapy() {
        return month_mapy;
    }

    public void setMonth_mapy(double month_mapy) {
        this.month_mapy = month_mapy;
    }

    public int getEnable_day() {
        return enable_day;
    }

    public void setEnable_day(int enable_day) {
        this.enable_day = enable_day;
    }

    public int getIs_close() {
        return is_close;
    }

    public void setIs_close(int ic_close) {
        this.is_close = ic_close;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public int getEnable_point() {
        return enable_point;
    }

    public void setEnable_point(int enable_point) {
        this.enable_point = enable_point;
    }

    public int getMax_point() {
        return max_point;
    }

    public void setMax_point(int max_point) {
        this.max_point = max_point;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", day_mapy=" + day_mapy +
                ", month_mapy=" + month_mapy +
                ", enable_day=" + enable_day +
                ", is_close=" + is_close +
                ", is_private=" + is_private +
                ", enable_point=" + enable_point +
                ", max_point=" + max_point +
                ", exclusives=" + exclusives +
                ", style='" + style + '\'' +
                ", certificate='" + certificate + '\'' +
                ", description='" + description + '\'' +
                ", notice='" + notice + '\'' +
                ", fans_activity='" + fans_activity + '\'' +
                ", fans_countent='" + fans_countent + '\'' +
                ", point_desc='" + point_desc + '\'' +
                ", max_mpay=" + max_mpay +
                ", roles=" + roles +
                ", fans_level=" + fans_level +
                ", live_history_date=" + live_history_date +
                ", list=" + list +
                ", index='" + index + '\'' +
                ", pingyin='" + pingyin + '\'' +
                ", is_recommend=" + is_recommend +
                ", pages=" + pages +
                ", unredcount=" + unredcount +
                ", fans_date='" + fans_date + '\'' +
                ", blogs=" + blogs +
                ", fans=" + fans +
                ", follows=" + follows +
                ", chat=" + chat +
                ", is_fans=" + is_fans +
                ", is_online=" + is_online +
                ", subject='" + subject + '\'' +
                ", msg_id='" + msg_id + '\'' +
                ", content='" + content + '\'' +
                ", follow_num='" + follow_num + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}

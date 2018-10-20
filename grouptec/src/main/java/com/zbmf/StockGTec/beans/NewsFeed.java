package com.zbmf.StockGTec.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iMac on 2017/2/23.
 */

public class NewsFeed extends General{


    private String blog_id;
    private String cover;//博文封面
    private String subject;//博文标题
    private String posted_at;//创建时间
    private String changed_at;//修改时间
    private String feed_id;
    private List<NewsFeed> list;

    public void setChanged_at(String changed_at) {
        this.changed_at = changed_at;
    }

    public String getChanged_at() {
        return changed_at;
    }

    public void setList(List<NewsFeed> list) {
        this.list = list;
    }

    public List<NewsFeed> getList() {
        return list;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    private Stat stat;

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public Stat getStat() {
        return stat;
    }

    public static class Stat {

        private String views;
        private String replys;

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getReplys() {
            return replys;
        }

        public void setReplys(String replys) {
            this.replys = replys;
        }
    }

    private Link link;

    public void setLink(Link link) {
        this.link = link;
    }

    public Link getLink() {
        return link;
    }

    public static class Link{

        private String app;//应用内访问地址
        private String wap;//wap手机访问地址

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getWap() {
            return wap;
        }

        public void setWap(String wap) {
            this.wap = wap;
        }
    }

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public static class User implements Serializable{

        private String id;
        private String nickname;
        private String avatar;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}

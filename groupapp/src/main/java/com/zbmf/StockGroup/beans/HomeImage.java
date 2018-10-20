package com.zbmf.StockGroup.beans;

import java.util.List;

/**
 * Created by xuhao on 2017/2/17.
 */

public class HomeImage {
    private String img_url;
    private String description;
    private String id;
    private int code;
    private String title;
    private String avatar_url;
    private String nickname;
    private String group_id;
    private String gid;
    private String pic_url;
    private String group_user_id;
    private String type;
    private String blog_user_id;
    private String blog_id;
    private String link_url;
    private String user_id;
    private int is_login;

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    private List<HomeImage> infolist;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getGroup_user_id() {
        return group_user_id;
    }

    public void setGroup_user_id(String group_user_id) {
        this.group_user_id = group_user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBlog_user_id() {
        return blog_user_id;
    }

    public void setBlog_user_id(String blog_user_id) {
        this.blog_user_id = blog_user_id;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<HomeImage> getInfolist() {
        return infolist;
    }

    public void setInfolist(List<HomeImage> infolist) {
        this.infolist = infolist;
    }
}

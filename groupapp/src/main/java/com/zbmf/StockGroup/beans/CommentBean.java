package com.zbmf.StockGroup.beans;

/**
 * Created by pq
 * on 2018/7/3.
 */

public class CommentBean {
    private int id;
    private int viewpoint_id;
    private String content;
    private long uid;
    private String nickname;
    private String avatar;
    private int to_user;
    private String created_at;
    private String company;
    private String position;

    public CommentBean(int id, int viewpoint_id, String content, long uid, String nickname,
                       String avatar, int to_user, String created_at,String company,String position) {
        this.id = id;
        this.viewpoint_id = viewpoint_id;
        this.content = content;
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.to_user = to_user;
        this.created_at = created_at;
        this.company = company;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViewpoint_id() {
        return viewpoint_id;
    }

    public void setViewpoint_id(int viewpoint_id) {
        this.viewpoint_id = viewpoint_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public int getTo_user() {
        return to_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

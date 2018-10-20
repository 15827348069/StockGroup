package com.zbmf.StockGroup.beans;

/**
 * Created by pq
 * on 2018/7/3.
 */

public class PointDetailBean {
    private int viewpoint_id;
    private String content;
    private int topic_id;
    private String title;
    private long uid;
    private String nickname;
    private String avatar;
    private int comment_count;
    private String img_keys;
    private int hits;
    private String created_at;
    private String company;
    private String position;

    public PointDetailBean(int viewpoint_id, String content, int topic_id, String title, long uid,
                           String nickname, String avatar, int comment_count,String img_keys, int hits,
                           String created_at,String company,String position) {
        this.viewpoint_id = viewpoint_id;
        this.content = content;
        this.topic_id = topic_id;
        this.title = title;
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.comment_count = comment_count;
        this.img_keys=img_keys;
        this.hits = hits;
        this.created_at = created_at;
        this.company=company;
        this.position=position;
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

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImg_keys() {
        return img_keys;
    }

    public void setImg_keys(String img_keys) {
        this.img_keys = img_keys;
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

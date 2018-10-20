package com.zbmf.StockGroup.beans;

/**
 * Created by pq
 * on 2018/7/2.
 */

public class PointsBean {
    private int viewpoint_id;
    private int topic_id;
    private int uid;
    private int is_hit;
    private int comment_count;
    private int hits;
    private String content;
    private String nickname;
    private String avatar;
    private String img_keys;
    private String created_at;
    private String title;
    private int width;
    private int height;
    private String company;
    private String position;


    public PointsBean(int viewpoint_id, int topic_id, int uid, int is_hit, int comment_count,
                      int hits, String content, String nickname, String avatar, String img_keys,
                      String created_at) {
        this.viewpoint_id = viewpoint_id;
        this.topic_id = topic_id;
        this.uid = uid;
        this.is_hit = is_hit;
        this.comment_count = comment_count;
        this.hits = hits;
        this.content = content;
        this.nickname = nickname;
        this.avatar = avatar;
        this.img_keys = img_keys;
        this.created_at = created_at;
    }

    public PointsBean(int viewpoint_id, int topic_id, int uid, int is_hit, int comment_count,
                      int hits, String content, String nickname, String avatar, String img_keys,
                      String created_at, String title, int width, int height,String company,String position) {
        this.viewpoint_id = viewpoint_id;
        this.topic_id = topic_id;
        this.uid = uid;
        this.is_hit = is_hit;
        this.comment_count = comment_count;
        this.hits = hits;
        this.content = content;
        this.nickname = nickname;
        this.avatar = avatar;
        this.img_keys = img_keys;
        this.created_at = created_at;
        this.title = title;
        this.width = width;
        this.height = height;
        this.company = company;
        this.position = position;
    }

    public int getViewpoint_id() {
        return viewpoint_id;
    }

    public void setViewpoint_id(int viewpoint_id) {
        this.viewpoint_id = viewpoint_id;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getIs_hit() {
        return is_hit;
    }

    public void setIs_hit(int is_hit) {
        this.is_hit = is_hit;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getImg_keys() {
        return img_keys;
    }

    public void setImg_keys(String img_keys) {
        this.img_keys = img_keys;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

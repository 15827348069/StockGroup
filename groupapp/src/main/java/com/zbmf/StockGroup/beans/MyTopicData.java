package com.zbmf.StockGroup.beans;

/**
 * Created by pq
 * on 2018/7/2.
 */

public class MyTopicData {
    private int topic_id;
    private int type_id;
    private int vp_number;
    private int is_hot;
    private int users;
    private int status;
    private String img;
    private String name;
    private String title;
    private String body;
    private String created_at;

    public MyTopicData(int topic_id, int type_id, int vp_number, int users, int status,
                       String img, String name, String title, String body, String created_at) {
        this.topic_id = topic_id;
        this.type_id = type_id;
        this.vp_number = vp_number;
        this.users = users;
        this.status = status;
        this.img = img;
        this.name = name;
        this.title = title;
        this.body = body;
        this.created_at = created_at;
    }
    public MyTopicData(int topic_id, int type_id, int vp_number, int is_hot, int users, int status,
                       String img, String name, String title, String body, String created_at) {
        this.topic_id = topic_id;
        this.type_id = type_id;
        this.vp_number = vp_number;
        this.is_hot = is_hot;
        this.users = users;
        this.status = status;
        this.img = img;
        this.name = name;
        this.title = title;
        this.body = body;
        this.created_at = created_at;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getVp_number() {
        return vp_number;
    }

    public void setVp_number(int vp_number) {
        this.vp_number = vp_number;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

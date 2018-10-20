package com.zbmf.StockGroup.beans;

/**
 * Created by pq
 * on 2018/7/5.
 */

public class NoReadMsgBean {
    private int msg_id;
    private int user_id;
    private String content;
    private int is_read;
    private String created_at;
    private int type;
    private String nick_name;
    private int viewpoint_id;
    private int topic_id;

    public NoReadMsgBean(int msg_id,int user_id, String content, int is_read, String created_at,
                         int type, String nick_name, int viewpoint_id, int topic_id) {
        this.msg_id=msg_id;
        this.user_id = user_id;
        this.content = content;
        this.is_read = is_read;
        this.created_at = created_at;
        this.type = type;
        this.nick_name = nick_name;
        this.viewpoint_id = viewpoint_id;
        this.topic_id = topic_id;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
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
}

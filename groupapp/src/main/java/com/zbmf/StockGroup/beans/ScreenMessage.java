package com.zbmf.StockGroup.beans;

/**
 * Created by xuhao on 2017/10/18.
 */

public class ScreenMessage {
    private String content;
    private String msg_id;
    private String screen_id;
    private String subject;
    private String url;
    private long created_at;
    private String screen_name;

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getScreen_id() {
        return screen_id;
    }

    public void setScreen_id(String screen_id) {
        this.screen_id = screen_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ScreenMessage() {
    }

    public ScreenMessage(String content, String msg_id, String screen_id, String subject, String url, long created_at, String screen_name) {
        this.content = content;
        this.msg_id = msg_id;
        this.screen_id = screen_id;
        this.subject = subject;
        this.url = url;
        this.created_at = created_at;
        this.screen_name = screen_name;
    }
}

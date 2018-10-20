package com.zbmf.groupro.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2017/4/25.
 */

public class TopticBean implements Serializable{
    private String content;
    private String link;
    private String subject;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

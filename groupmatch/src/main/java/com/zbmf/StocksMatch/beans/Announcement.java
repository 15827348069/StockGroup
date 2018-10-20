package com.zbmf.StocksMatch.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/1/13.
 */
public class Announcement extends General{
    private int page;
    private int perpage;
    private int pages;
    private List<Announcement> list;
    private String group_id;
    private String topic_id;
    private String subject;
    private String content;
    private String posted_at;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<Announcement> getList() {
        return list;
    }

    public void setList(List<Announcement> list) {
        this.list = list;
    }
}

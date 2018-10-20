package com.zbmf.StocksMatch.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class Actives extends General{

    private String title;
    private String url;
    private String desc;
    private String start_time;
    private String end_time;
    private String pic_url;
    private String created_at;
    private String type;
    private List<Actives> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Actives> getList() {
        return list;
    }

    public void setList(List<Actives> list) {
        this.list = list;
    }
}

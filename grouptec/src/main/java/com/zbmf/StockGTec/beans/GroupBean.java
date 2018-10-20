package com.zbmf.StockGTec.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuhao on 2016/12/14.
 */

public class GroupBean implements Serializable{
    private String group_id;
    private String name;
    private String avatar;
    private String desc;
    private int page;
    private int pages;
    private List<GroupBean>infolist;

    public List<GroupBean> getInfolist() {
        return infolist;
    }

    public void setInfolist(List<GroupBean> infolist) {
        this.infolist = infolist;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

package com.zbmf.StockGroup.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 宝盒实体类
 * Created by xuhao on 2016/12/24.
 */

public class BoxBean implements Serializable{
    private int page; //页码
    private int pages; //分页数量
    private int perpage;//分页数量
    private int total;//总数
    private String id;//圈主ID
    private String box_id;//宝盒ID
    private String subject;//宝盒标题
    private String description;//宝盒简介
    private String is_stick;//是否推荐
    private String is_stop;//是否下架
    private String box_level;//宝盒等级
    private String box_updated;//宝盒更新时间
    private String fans_level;//圈子等级
    private String fans_created;//宝盒订阅时间
    private String category;//宝盒订阅时间
    private List<Tags> tags;//标签列表
    private int items;
    private List<BoxBean> list;

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setList(List<BoxBean> list) {
        this.list = list;
    }

    public List<BoxBean> getList() {
        return list;
    }

    private NewsFeed.User user;

    public void setUser(NewsFeed.User user) {
        this.user = user;
    }

    public NewsFeed.User getUser() {
        return user;
    }

    private Tags tag;

    public Tags getTag(String name,int type) {
        this.tag = new Tags(name,type);
        return tag;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBox_id() {
        return box_id;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_stick() {
        return is_stick;
    }

    public void setIs_stick(String is_stick) {
        this.is_stick = is_stick;
    }

    public String getIs_stop() {
        return is_stop;
    }

    public void setIs_stop(String is_stop) {
        this.is_stop = is_stop;
    }

    public String getBox_level() {
        return box_level;
    }

    public void setBox_level(String box_level) {
        this.box_level = box_level;
    }

    public String getBox_updated() {
        return box_updated;
    }

    public void setBox_updated(String box_updated) {
        this.box_updated = box_updated;
    }

    public String getFans_level() {
        return fans_level;
    }

    public void setFans_level(String fans_level) {
        this.fans_level = fans_level;
    }

    public String getFans_created() {
        return fans_created;
    }

    public void setFans_created(String fans_created) {
        this.fans_created = fans_created;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    /**
     * 宝盒标签
     */
    public class Tags extends BoxBean{
        private String name;
        private int tag_type;
        public Tags(String name,int tag_type){
            this.name=name;
            this.tag_type=tag_type;
        }
        public int getTag_type() {
            return tag_type;
        }

        public void setTag_type(int tag_type) {
            this.tag_type = tag_type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}

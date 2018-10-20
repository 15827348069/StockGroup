package com.zbmf.StockGTec.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lulu on 2017/7/10.
 */

public class Videos extends General{

    /**
     * video_id : 4
     * series_id : 1
     * user_id : 7878
     * group_id : 7878
     * is_live : 1
     * teacher : 薇薇
     * title : 测试4
     * price : 11
     * start_at : 1498795200
     * pic_play : http://oss2.zbmf.com/video_play/4-29a85b1bb8f07.png
     * pic_thumb : http://oss2.zbmf.com/video_thumb/4-29a85b1bb9434.png
     * tag : 热门
     * is_hot : 1
     * is_stick : 0
     * discount : 100
     * subscribe : 0
     * bokecc_id : 1837A7D2131E996A9C33DC5901307461
     * order : 0
     */
    public int pages;
    private int video_id;
    private int series_id;
    private int user_id;
    private int group_id;
    private int is_live;
    private int is_fans;
    private String teacher;
    private String title;
    private String series_name;
    private int price;
    private int start_at;
    private String pic_play;
    private String pic_thumb;
    private String tag;
    private int is_hot;
    private int is_stick;
    private int discount;
    private int subscribe;
    private String bokecc_id;
    private String share_img;
    private String share_url;
    private int order;
    private List<Videos> list;
    private List<ContentBean> content;

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public void setIs_fans(int is_fans) {
        this.is_fans = is_fans;
    }

    public int getIs_fans() {
        return is_fans;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setList(List<Videos> list) {
        this.list = list;
    }

    public List<Videos> getList() {
        return list;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getSeries_id() {
        return series_id;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getIs_live() {
        return is_live;
    }

    public void setIs_live(int is_live) {
        this.is_live = is_live;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStart_at() {
        return start_at;
    }

    public void setStart_at(int start_at) {
        this.start_at = start_at;
    }

    public String getPic_play() {
        return pic_play;
    }

    public void setPic_play(String pic_play) {
        this.pic_play = pic_play;
    }

    public String getPic_thumb() {
        return pic_thumb;
    }

    public void setPic_thumb(String pic_thumb) {
        this.pic_thumb = pic_thumb;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getIs_stick() {
        return is_stick;
    }

    public void setIs_stick(int is_stick) {
        this.is_stick = is_stick;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public String getBokecc_id() {
        return bokecc_id;
    }

    public void setBokecc_id(String bokecc_id) {
        this.bokecc_id = bokecc_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public static class ContentBean implements Serializable{
        private String title;
        private String desc;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

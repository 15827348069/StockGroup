package com.zbmf.StockGroup.beans;

import java.util.List;

/**
 * Created by xuhao on 2016/12/24.
 */

public class BoxDetailBean extends BoxBean{
    private int page; //页码
    private int pages; //分页数量
    private int perpage;//分页数量
    private int total;//总数

    private List<Items>infolist;//宝盒条目
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

    public List<Items> getInfolist() {
        return infolist;
    }

    public void setInfolist(List<Items> infolist) {
        this.infolist = infolist;
    }
    public Items getItems(String item_id,String content,String is_plth,String created_at){
        return new Items(item_id,content,is_plth,created_at);
    }
    public class Items{
        private String item_id;//内容ID
        private String content;//内容介绍
        private String is_plth;//是否精华
        private String created_at;//录入时间
        public Items(String item_id,String content,String is_plth,String created_at){
            this.item_id=item_id;
            this.content=content;
            this.is_plth=is_plth;
            this.created_at=created_at;
        }
        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIs_plth() {
            return is_plth;
        }

        public void setIs_plth(String is_plth) {
            this.is_plth = is_plth;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}

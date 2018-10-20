package com.zbmf.StockGroup.beans;

/**
 * Created by xuhao on 2017/8/31.
 */

public class ShareBean {
    private String title;
    private String img;
    private String shareUrl;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ShareBean(String title, String img, String shareUrl) {
        this.title = title;
        this.img = img;
        this.shareUrl = shareUrl;
    }

    public ShareBean(String title, String img, String shareUrl, String desc) {
        this.title = title;
        this.img = img;
        this.shareUrl = shareUrl;
        this.desc = desc;
    }
}

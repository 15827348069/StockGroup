package com.zbmf.StockGTec.beans;


public class Advert extends General {

    /**
     * advert_id : 81
     * subject : 测试3
     * img_url : http://oss2.zbmf.com/banner/81-29b33bc4ed973.jpg
     * jump_url : http://m.zbmf.com/xgds_new/
     * is_blank : 0
     * end_time : 1502520000
     */

    private int advert_id;
    private String subject;
    private String img_url;
    private String jump_url;
    private int is_blank;
    private long end_time;

    public int getAdvert_id() {
        return advert_id;
    }

    public void setAdvert_id(int advert_id) {
        this.advert_id = advert_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public int getIs_blank() {
        return is_blank;
    }

    public void setIs_blank(int is_blank) {
        this.is_blank = is_blank;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
}

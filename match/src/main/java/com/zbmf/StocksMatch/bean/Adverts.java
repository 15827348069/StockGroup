package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/3/16.
 */

public class Adverts implements Serializable{
    private String advert_id;
    private String subject;
    private String img_url;
    private String jump_url;
    private int is_blank;
    private int is_login;
    private String end_time;

    public String getAdvert_id() {
        return advert_id;
    }

    public void setAdvert_id(String advert_id) {
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

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
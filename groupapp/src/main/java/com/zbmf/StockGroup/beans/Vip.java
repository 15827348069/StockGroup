package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/6/5.
 */

public class Vip implements Serializable {
    String user_id;
    String vip_start_at;
    String vip_end_at;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVip_start_at() {
        return vip_start_at;
    }

    public void setVip_start_at(String vip_start_at) {
        this.vip_start_at = vip_start_at;
    }

    public String getVip_end_at() {
        return vip_end_at;
    }

    public void setVip_end_at(String vip_end_at) {
        this.vip_end_at = vip_end_at;
    }
}

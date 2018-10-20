package com.zbmf.StockGroup.beans;

import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.DateUtil;

/**
 * Created by xuhao on 2017/10/25.
 */

public class StockComments {
    private String id;
    private String user_id;
    private String nickname;
    private String user_img;
    private String desc;
    private long create_at;
    private String symbol;
    private StockholdsBean dealSys;

    public StockholdsBean getDealSys() {
        return dealSys;
    }

    public void setDealSys(StockholdsBean dealSys) {
        this.dealSys = dealSys;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreate_at() {
        return DateUtil.getTime(this.create_at, Constants.MM_dd_HH_mm);
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public StockComments(String id, String user_id, String nickname, String user_img,
                         String desc, long create_at,StockholdsBean dealSys) {
        this.id = id;
        this.user_id = user_id;
        this.nickname = nickname;
        this.user_img = user_img;
        this.desc = desc;
        this.create_at = create_at;
        this.dealSys=dealSys;
    }
}

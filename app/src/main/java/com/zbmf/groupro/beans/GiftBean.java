package com.zbmf.groupro.beans;

import java.io.Serializable;

/**
 * 礼物实体类
 * Created by xuhao on 2016/12/20.
 */

public class GiftBean implements Serializable {
    private String gift_name;//礼物名字
    private String gift_icon;//礼物的图标
    private String gift_id;//礼物的ID
    private String gift_price; //礼物的价格
    private String gift_points;//礼物的兑换积分
    private String send_from_user_name;//礼物来源的对象
    private String send_from_user_avatar;//礼物来源的头像
    private String send_to_user_name;//赠送对象的名字
    private int send_gift_number; //赠送的礼物数量
    private String send_from_user_id;//赠送者ID

    public String getSend_from_user_id() {
        return send_from_user_id;
    }

    public void setSend_from_user_id(String send_from_user_id) {
        this.send_from_user_id = send_from_user_id;
    }

    public String getGift_points() {
        return gift_points;
    }

    public void setGift_points(String gift_points) {
        this.gift_points = gift_points;
    }

    public String getSend_from_user_avatar() {
        return send_from_user_avatar;
    }

    public void setSend_from_user_avatar(String send_from_user_avatar) {
        this.send_from_user_avatar = send_from_user_avatar;
    }

    public int getSend_gift_number() {
        return send_gift_number;
    }

    public void setSend_gift_number(int send_gift_number) {
        this.send_gift_number = send_gift_number;
    }

    public String getSend_from_user_name() {
        return send_from_user_name;
    }

    public void setSend_from_user_name(String send_from_user_name) {
        this.send_from_user_name = send_from_user_name;
    }

    public String getSend_to_user_name() {
        return send_to_user_name;
    }

    public void setSend_to_user_name(String send_to_user_name) {
        this.send_to_user_name = send_to_user_name;
    }

    public String getGift_price() {
        return gift_price;
    }

    public void setGift_price(String gift_price) {
        this.gift_price = gift_price;
    }

    private boolean is_checked;

    public boolean is_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_icon() {
        return gift_icon;
    }

    public void setGift_icon(String gift_icon) {
        this.gift_icon = gift_icon;
    }
}

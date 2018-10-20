package com.zbmf.StockGroup.beans;

import java.util.List;

/**
 * Created by iMac on 2016/12/26.
 */

public class Gift extends General {

    /**
     * gift_id : 2
     * name : 1111 礼物名称
     * icon : https://oss.zbmf.com/xxxx/1.png
     * mpays : 199 魔方宝价格
     * points : 1999 积分价格
     */

    private String gift_id;
    private String name;
    private String icon;
    private String category;
    private int mpays;
    private int points;
    private boolean checked;
    private List<Gift> list;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setList(List<Gift> list) {
        this.list = list;
    }

    public List<Gift> getList() {
        return list;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMpays() {
        return mpays;
    }

    public void setMpays(int mpays) {
        this.mpays = mpays;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

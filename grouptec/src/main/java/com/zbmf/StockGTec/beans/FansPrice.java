package com.zbmf.StockGTec.beans;

/**
 * Created by xuhao on 2017/1/4.
 */

public class FansPrice {
    private String title;//宝盒头部
    private String price;//宝盒价格
    private boolean is_checked;//是否选中

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean is_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }
}

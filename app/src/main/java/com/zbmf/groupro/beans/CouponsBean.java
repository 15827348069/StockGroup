package com.zbmf.groupro.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuhao on 2017/1/11.
 */

public class CouponsBean implements Serializable {
    private String coupon_id;//优惠券ID
    private String subject;//	优惠券标题
    private String sumary;//	优惠券介绍
    private int kind;//	优惠类型
    private String start_at;//优惠券开始时间
    private String end_at;//优惠券结束时间
    private String type;
    private String couponsName;
    private boolean can_use;//是否可用

    private boolean amin_show;//是否播放动画
    private boolean is_take;//是否已领取

    private double minimum;//优惠券使用条件 0为无条件
    private int cateogry;//使用条件
    private String id;//使用的圈子ID
    private String take_id;
    private List<CouPonsRules> riles;//优惠规则
    private List<CouponsBean>infolist;
    private double maximum;

    public String getTake_id() {
        return take_id;
    }

    public void setTake_id(String take_id) {
        this.take_id = take_id;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public List<CouponsBean> getInfolist() {
        return infolist;
    }

    public String getCouponsName() {
        return couponsName;
    }

    public void setCouponsName(String couponsName) {
        this.couponsName = couponsName;
    }

    public void setInfolist(List<CouponsBean> infolist) {
        this.infolist = infolist;
    }

    public int getCateogry() {
        return cateogry;
    }

    public void setCateogry(int cateogry) {
        this.cateogry = cateogry;
    }

    public boolean isCan_use() {
        return can_use;
    }

    public void setCan_use(boolean can_use) {
        this.can_use = can_use;
    }

    public List<CouPonsRules> getRiles() {
        return riles;
    }

    public void setRiles(List<CouPonsRules> riles) {
        this.riles = riles;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public boolean isAmin_show() {
        return amin_show;
    }

    public void setAmin_show(boolean amin_show) {
        this.amin_show = amin_show;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public boolean is_take() {
        return is_take;
    }

    public void setIs_take(boolean is_take) {
        this.is_take = is_take;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSumary() {
        return sumary;
    }

    public void setSumary(String sumary) {
        this.sumary = sumary;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

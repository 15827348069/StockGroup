package com.zbmf.StockGroup.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/6/7.
 */

public class CouponsOrSystem implements Serializable {
    private int take_id;
    private int coupon_id;
    private int id;
    private boolean isCanUse;
    private boolean isCheck;
    private String subject;
    private String summary;
    private int kind;
    private String start_at;
    private String end_at;
    private int is_taken;
    private int rule_valid;
    private int minimum;
    private int maximum;
    private int category;
    private int total;
    private int havings;
    private int days;
    private int is_ratio;
    private int is_hide;
    private int is_delete;
    private int user_id;
    private String nickname;
    private String avatar;
    private String couponsName;
    private List<Rules> rules;
    private boolean amin_show;//是否播放动画

    public CouponsOrSystem(int take_id,int coupon_id,int id,String subject,String summary,int kind,
                           int category,int minimum,int is_ratio,String start_at,String end_at,List<Rules> rules){
        this.take_id=take_id;
        this.coupon_id=coupon_id;
        this.id=id;
        this.subject=subject;
        this.summary=summary;
        this.kind=kind;
        this.category=category;
        this.minimum=minimum;
        this.is_ratio=is_ratio;
        this.start_at=start_at;
        this.end_at=end_at;
        this.rules=rules;
    }
    public CouponsOrSystem(int take_id,int coupon_id, String subject, String summary, int kind, String start_at, String end_at) {
        this.take_id=take_id;
        this.coupon_id = coupon_id;
        this.subject = subject;
        this.summary = summary;
        this.kind = kind;
        this.start_at = start_at;
        this.end_at = end_at;
    }

    public CouponsOrSystem(int take_id,int coupon_id, int id, String subject, String summary, int kind,
                           String start_at, String end_at, int is_taken, int rule_valid,
                           int minimum, int maximum, int category, int total, int havings,
                           int days, int is_ratio, int is_hide, int is_delete, int user_id,
                           String nickname, String avatar) {
        this.take_id=take_id;
        this.coupon_id = coupon_id;
        this.id = id;
        this.subject = subject;
        this.summary = summary;
        this.kind = kind;
        this.start_at = start_at;
        this.end_at = end_at;
        this.is_taken = is_taken;
        this.rule_valid = rule_valid;
        this.minimum = minimum;
        this.maximum = maximum;
        this.category = category;
        this.total = total;
        this.havings = havings;
        this.days = days;
        this.is_ratio = is_ratio;
        this.is_hide = is_hide;
        this.is_delete = is_delete;
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
    }
    public CouponsOrSystem(int take_id,int coupon_id, int id, String subject, String summary, int kind,
                           String start_at, String end_at, int is_taken, int rule_valid,
                           int minimum, int maximum, int category, int total, int havings,
                           int days, int is_ratio, int is_hide, int is_delete, int user_id,
                           String nickname, String avatar,List<Rules> rules) {
        this.take_id=take_id;
        this.coupon_id = coupon_id;
        this.id = id;
        this.subject = subject;
        this.summary = summary;
        this.kind = kind;
        this.start_at = start_at;
        this.end_at = end_at;
        this.is_taken = is_taken;
        this.rule_valid = rule_valid;
        this.minimum = minimum;
        this.maximum = maximum;
        this.category = category;
        this.total = total;
        this.havings = havings;
        this.days = days;
        this.is_ratio = is_ratio;
        this.is_hide = is_hide;
        this.is_delete = is_delete;
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.rules=rules;
    }

    public boolean isAmin_show() {
        return amin_show;
    }

    public void setAmin_show(boolean amin_show) {
        this.amin_show = amin_show;
    }

    public String getCouponsName() {
        return couponsName;
    }

    public void setCouponsName(String couponsName) {
        this.couponsName = couponsName;
    }
    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_taken() {
        return is_taken;
    }

    public void setIs_taken(int is_taken) {
        this.is_taken = is_taken;
    }

    public int getRule_valid() {
        return rule_valid;
    }

    public void setRule_valid(int rule_valid) {
        this.rule_valid = rule_valid;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getHavings() {
        return havings;
    }

    public void setHavings(int havings) {
        this.havings = havings;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getIs_ratio() {
        return is_ratio;
    }

    public void setIs_ratio(int is_ratio) {
        this.is_ratio = is_ratio;
    }

    public int getIs_hide() {
        return is_hide;
    }

    public void setIs_hide(int is_hide) {
        this.is_hide = is_hide;
    }

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Rules> getRules() {
        return rules;
    }

    public void setRules(List<Rules> rules) {
        this.rules = rules;
    }

    public boolean isCanUse() {
        return isCanUse;
    }

    public void setCanUse(boolean canUse) {
        isCanUse = canUse;
    }

    public int getTake_id() {
        return take_id;
    }

    public void setTake_id(int take_id) {
        this.take_id = take_id;
    }
    public String getCouponsType(){
        String msg="";
        switch (kind){
            case 0:
                //默认  满送积分
                msg="满送";
                break;
            case 1:
                //满加券
                msg="满加";
                break;
            case 2:
                //满减券
                msg="满减";
                break;
            case 3:
                //折扣券
                msg="折扣";
                break;
            case 10:
                //粉丝券
                msg="铁粉";
                break;
            case 11:
                //年粉券
                msg="年粉";
                break;
            case 12:
                //体验券
                msg="体验";
                break;
        }
        return msg;
    }
}

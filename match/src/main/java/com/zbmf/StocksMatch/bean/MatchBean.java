package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by xuhao
 * on 2017/11/23.
 */

public class MatchBean implements Serializable{
    private int id;
    private String title;
    private String init_money;
    private double mpay;
    private int object_type;
    private String desc;
    private String award_remark;
    private String award;
    private String sponsor_logo;
    private String start_apply;
    private String end_apply;
    private String start_at;
    private String end_at;
    private String apply_require_field;
    private int is_match_player;
    private int mach_type;
    private int players;
    private int match_status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInit_money() {
        return init_money;
    }

    public void setInit_money(String init_money) {
        this.init_money = init_money;
    }

    public double getMpay() {
        return mpay;
    }

    public void setMpay(double mpay) {
        this.mpay = mpay;
    }

    public int getObject_type() {
        return object_type;
    }

    public void setObject_type(int object_type) {
        this.object_type = object_type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAward_remark() {
        return award_remark;
    }

    public void setAward_remark(String award_remark) {
        this.award_remark = award_remark;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getSponsor_logo() {
        return sponsor_logo;
    }

    public void setSponsor_logo(String sponsor_logo) {
        this.sponsor_logo = sponsor_logo;
    }

    public String getStart_apply() {
        return start_apply;
    }

    public void setStart_apply(String start_apply) {
        this.start_apply = start_apply;
    }

    public String getEnd_apply() {
        return end_apply;
    }

    public void setEnd_apply(String end_apply) {
        this.end_apply = end_apply;
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

    public String getApply_require_field() {
        return apply_require_field;
    }

    public void setApply_require_field(String apply_require_field) {
        this.apply_require_field = apply_require_field;
    }

    public boolean getIs_match_player() {
        return is_match_player==1;
    }

    public void setIs_match_player(int is_match_player) {
        this.is_match_player = is_match_player;
    }

    public int getMach_type() {
        return mach_type;
    }

    public void setMach_type(int mach_type) {
        this.mach_type = mach_type;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getMatch_status() {
        return match_status;
    }

    public void setMatch_status(int match_status) {
        this.match_status = match_status;
    }
}

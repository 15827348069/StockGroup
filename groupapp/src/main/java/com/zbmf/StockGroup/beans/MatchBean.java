package com.zbmf.StockGroup.beans;


import java.io.Serializable;
import java.util.List;

/**
 * 比赛bean
 * Created by kubo on 2015/12/28.
 */
public class MatchBean extends General implements Serializable {

    private int page;
    private int perpage;
    private int pages;

    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String mpay;
    private String object_type;
    private String desc;
    private String award_remark;
    private String award;
    private String sponsor_logo;
    private String start_apply;
    private String end_apply;
    private String start_at;
    private String end_at;
    private String apply_require_field;
    private String is_match_player;
    private String players;
    private String init_money;

    private String err_msg;
    private String records;

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "MatchBean{" +
                "unfrozen_money=" + unfrozen_money +
                '}';
    }

    private int match_type=-1;// 比赛类型   0公开赛, 1邀请赛
    private int invite_type=-1;//邀请赛类型 0问题, 1邀请码, 2两者结合
    private int match_status=-1; //是否需要手机号码和验证 1都要  2不要验证码 3都不要
    private List<MatchBean> list;
    private String week_rank;
    private String day_rank;
    private String total_rank;
    private String stock_order;
    private String stock_holds;

    private double week_yield;
    private double day_yield;
    private double yield;
    private double money;
    private double unfrozen_money;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getWeek_rank() {
        return week_rank;
    }

    public void setWeek_rank(String week_rank) {
        this.week_rank = week_rank;
    }

    public String getDay_rank() {
        return day_rank;
    }

    public void setDay_rank(String day_rank) {
        this.day_rank = day_rank;
    }

    public String getTotal_rank() {
        return total_rank;
    }

    public void setTotal_rank(String total_rank) {
        this.total_rank = total_rank;
    }

    public String getStock_order() {
        return stock_order;
    }

    public void setStock_order(String stock_order) {
        this.stock_order = stock_order;
    }

    public String getStock_holds() {
        return stock_holds;
    }

    public void setStock_holds(String stock_holds) {
        this.stock_holds = stock_holds;
    }

    public double getWeek_yield() {
        return week_yield;
    }

    public void setWeek_yield(double week_yield) {
        this.week_yield = week_yield;
    }

    public double getDay_yield() {
        return day_yield;
    }

    public void setDay_yield(double day_yield) {
        this.day_yield = day_yield;
    }

    public int getMatch_status() {
        return match_status;
    }

    public void setMatch_status(int match_status) {
        this.match_status = match_status;
    }

    public double getYield() {
        return yield;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getUnfrozen_money() {
        return unfrozen_money;
    }

    public void setUnfrozen_money(double unfrozen_money) {
        this.unfrozen_money = unfrozen_money;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<MatchBean> getList() {
        return list;
    }

    public void setList(List<MatchBean> list) {
        this.list = list;
    }

    public int getMatch_type() {
        return match_type;
    }

    public void setMatch_type(int match_type) {
        this.match_type = match_type;
    }

    public int getInvite_type() {
        return invite_type;
    }

    public void setInvite_type(int invite_type) {
        this.invite_type = invite_type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMpay() {
        return mpay;
    }

    public void setMpay(String mpay) {
        this.mpay = mpay;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
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

    public String getIs_match_player() {
        return is_match_player;
    }

    public void setIs_match_player(String is_match_player) {
        this.is_match_player = is_match_player;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getInit_money() {
        return init_money;
    }

    public void setInit_money(String init_money) {
        this.init_money = init_money;
    }


    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }


}

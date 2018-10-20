package com.zbmf.StockGTec.beans;

import java.util.List;

/**
 * Created by lulu on 2017/7/20.
 */

public class Invite {


    public int pages;
    private String nickname;
    private String invited_at;
    private double mpay;

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }

    private StatBean stat;
    private List<Invite> invite_lists;

    public void setInvite_lists(List<Invite> invite_lists) {
        this.invite_lists = invite_lists;
    }

    public List<Invite> getInvite_lists() {
        return invite_lists;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInvited_at() {
        return invited_at;
    }

    public void setInvited_at(String invited_at) {
        this.invited_at = invited_at;
    }

    public double getMpay() {
        return mpay;
    }

    public void setMpay(double mpay) {
        this.mpay = mpay;
    }

    public StatBean getStat() {
        return stat;
    }

    public void setStat(StatBean stat) {
        this.stat = stat;
    }

    public static class StatBean {

        private double total;
        private double valid;
        private double mpay;

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getValid() {
            return valid;
        }

        public void setValid(double valid) {
            this.valid = valid;
        }

        public double getMpay() {
            return mpay;
        }

        public void setMpay(double mpay) {
            this.mpay = mpay;
        }
    }
}

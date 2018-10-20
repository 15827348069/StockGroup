package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public class TraderInfo extends Erro implements Serializable{
    private String status;
    private Traders trader;
    private Tracker tracker;
    private TraderYield yield;
    private List<Holds> holds;
    private List<Deals> deals;

    public void setDeals(List<Deals> deals) {
        this.deals = deals;
    }

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Holds> getHolds() {
        return holds;
    }

    public void setHolds(List<Holds> holds) {
        this.holds = holds;
    }

    public List<Deals> getDeals() {
        return deals;
    }

    public Traders getTrader() {
        return trader;
    }

    public void setTrader(Traders trader) {
        this.trader = trader;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public TraderYield getYield() {
        return yield;
    }

    public void setYield(TraderYield yield) {
        this.yield = yield;
    }

    public class Deals implements Serializable{
        private String created_at;
        private String action;
        private String name;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public class Holds implements Serializable{}

}

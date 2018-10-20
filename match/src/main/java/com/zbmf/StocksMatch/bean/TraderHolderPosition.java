package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class TraderHolderPosition extends Erro implements Serializable {
    private String status;
    private List<Holds> holds;

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

    public static class Holds implements Serializable {
        private String name;
        private int volumn_total;
        private int volumn_unfrozen;
        private String price;
        private String current;
        private String day_yield;
        private String gain;
        private String gain_yield;
        private String stock_value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getVolumn_total() {
            return volumn_total;
        }

        public void setVolumn_total(int volumn_total) {
            this.volumn_total = volumn_total;
        }

        public int getVolumn_unfrozen() {
            return volumn_unfrozen;
        }

        public void setVolumn_unfrozen(int volumn_unfrozen) {
            this.volumn_unfrozen = volumn_unfrozen;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public String getDay_yield() {
            return day_yield;
        }

        public void setDay_yield(String day_yield) {
            this.day_yield = day_yield;
        }

        public String getGain() {
            return gain;
        }

        public void setGain(String gain) {
            this.gain = gain;
        }

        public String getGain_yield() {
            return gain_yield;
        }

        public void setGain_yield(String gain_yield) {
            this.gain_yield = gain_yield;
        }

        public String getStock_value() {
            return stock_value;
        }

        public void setStock_value(String stock_value) {
            this.stock_value = stock_value;
        }
    }
}

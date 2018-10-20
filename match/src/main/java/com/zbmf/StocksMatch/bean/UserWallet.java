package com.zbmf.StocksMatch.bean;


import java.io.Serializable;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class UserWallet extends Erro implements Serializable {
    private String status;
    private Pay pay;
    private Point point;
    private Coupon coupon;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pay getPay() {
        return pay;
    }

    public void setPay(Pay pay) {
        this.pay = pay;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public class Pay implements Serializable{
        private String total;
        private String unfrozen;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUnfrozen() {
            return unfrozen;
        }

        public void setUnfrozen(String unfrozen) {
            this.unfrozen = unfrozen;
        }
    }
    public class Point implements Serializable{
        private String total;
        private String unfrozen;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUnfrozen() {
            return unfrozen;
        }

        public void setUnfrozen(String unfrozen) {
            this.unfrozen = unfrozen;
        }
    }
    public class Coupon implements Serializable{
        private String total;
        private String unfrozen;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUnfrozen() {
            return unfrozen;
        }

        public void setUnfrozen(String unfrozen) {
            this.unfrozen = unfrozen;
        }
    }
}

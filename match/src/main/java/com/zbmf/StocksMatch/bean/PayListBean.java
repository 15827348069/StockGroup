package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/4/9.
 */

public class PayListBean extends Erro implements Serializable{
    private String status;
    private List<Products> products;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
    public class Products implements Serializable{
        private int id;
        private String name;
        private double amount;
        private double mpay;
        private double gift;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getMpay() {
            return mpay;
        }

        public void setMpay(double mpay) {
            this.mpay = mpay;
        }

        public double getGift() {
            return gift;
        }

        public void setGift(double gift) {
            this.gift = gift;
        }
    }
}

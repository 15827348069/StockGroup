package com.zbmf.StockGroup.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 找股票
 * Created by xuhao on 2017/10/17.
 */

public class Screen implements Serializable {
    private double day_yield;
    private String descripition;
    private long end_at;
    private double month_yield;
    private String name;
    private String screen_id;
    private double sh_index;
    private double sh_yield;
    private String total_stock;
    private double total_yield;
    private double win_rate;
    private int win_stock;
    private int order_status;
    private double m_prime_price;
    private double m_price;
    private int is_discount;
    private long expire_at;
    private int is_expire;
    private long join_at;
    private int is_buy;
    private boolean is_more;

    public boolean getIs_buy() {
        return is_buy==1;
    }

    public void setIs_buy(int is_buy) {
        this.is_buy = is_buy;
    }

    public boolean is_more() {
        return is_more;
    }

    public void setIs_more(boolean is_more) {
        this.is_more = is_more;
    }

    public double getM_prime_price() {
        return m_prime_price;
    }

    public void setM_prime_price(double m_prime_price) {
        this.m_prime_price = m_prime_price;
    }

    public long getExpire_at() {
        return expire_at;
    }

    public void setExpire_at(long expire_at) {
        this.expire_at = expire_at;
    }

    public int getIs_expire() {
        return is_expire;
    }

    public void setIs_expire(int is_expire) {
        this.is_expire = is_expire;
    }

    public long getJoin_at() {
        return join_at;
    }

    public void setJoin_at(long join_at) {
        this.join_at = join_at;
    }

    public double getM_price() {
        return m_price;
    }

    public void setM_price(double m_price) {
        this.m_price = m_price;
    }

    public int getIs_discount() {
        return is_discount;
    }

    public void setIs_discount(int is_discount) {
        this.is_discount = is_discount;
    }

    private List<Prce>prceList;

    public List<Prce> getPrceList() {
        return prceList;
    }

    public void setPrceList(List<Prce> prceList) {
        this.prceList = prceList;
    }

    public boolean getOrder_status() {
        return order_status==1;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public double getDay_yield() {
        return day_yield;
    }

    public void setDay_yield(double day_yield) {
        this.day_yield = day_yield;
    }

    public String getDescripition() {
        return descripition;
    }

    public void setDescripition(String descripition) {
        this.descripition = descripition;
    }

    public long getEnd_at() {
        return end_at;
    }

    public void setEnd_at(long end_at) {
        this.end_at = end_at;
    }

    public double getMonth_yield() {
        return month_yield;
    }

    public void setMonth_yield(double month_yield) {
        this.month_yield = month_yield;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen_id() {
        return screen_id;
    }

    public void setScreen_id(String screen_id) {
        this.screen_id = screen_id;
    }

    public double getSh_index() {
        return sh_index;
    }

    public void setSh_index(double sh_index) {
        this.sh_index = sh_index;
    }

    public double getSh_yield() {
        return sh_yield;
    }

    public void setSh_yield(double sh_yield) {
        this.sh_yield = sh_yield;
    }

    public String getTotal_stock() {
        return total_stock;
    }

    public void setTotal_stock(String total_stock) {
        this.total_stock = total_stock;
    }

    public double getTotal_yield() {
        return total_yield;
    }

    public void setTotal_yield(double total_yield) {
        this.total_yield = total_yield;
    }

    public double getWin_rate() {
        return win_rate;
    }

    public void setWin_rate(double win_rate) {
        this.win_rate = win_rate;
    }

    public int getWin_stock() {
        return win_stock;
    }

    public void setWin_stock(int win_stock) {
        this.win_stock = win_stock;
    }

    public Screen() {

    }

    public Screen(boolean is_more) {
        this.is_more = is_more;
    }

    /**
     *
     * @param day_yield
     * @param descripition
     * @param end_at
     * @param month_yield
     * @param name
     * @param screen_id
     * @param sh_index
     * @param sh_yield
     * @param total_stock
     * @param total_yield
     * @param win_rate
     * @param win_stock
     * @param order_status
     */
    public Screen(double day_yield, String descripition, long end_at, double month_yield,
                  String name, String screen_id, double sh_index, double sh_yield,
                  String total_stock, double total_yield, double win_rate,
                  int win_stock,int order_status,double m_prime_price,double m_price,int is_discount,
                  int is_expire,long expire_at,long join_at,int is_buy,
                  List<Prce> pricelist) {
        this.day_yield = day_yield;
        this.descripition = descripition;
        this.end_at = end_at;
        this.month_yield = month_yield;
        this.name = name;
        this.screen_id = screen_id;
        this.sh_index = sh_index;
        this.sh_yield = sh_yield;
        this.total_stock = total_stock;
        this.total_yield = total_yield;
        this.win_rate = win_rate;
        this.win_stock = win_stock;
        this.order_status=order_status;
        this.m_prime_price=m_prime_price;
        this.m_price=m_price;
        this.is_discount=is_discount;
        this.is_expire=is_expire;
        this.expire_at=expire_at;
        this.join_at=join_at;
        this.is_buy=is_buy;
        this.prceList=pricelist;
    }



    public static class Prce implements Serializable{
        private String price_id;
        private double price;
        private int days;
        private double prime_price;
        private String unit;
        private int is_discount;
        private boolean is_check;
        private long expire_at;

        public long getExpire_at() {
            return expire_at;
        }

        public void setExpire_at(long expire_at) {
            this.expire_at = expire_at;
        }

        public boolean getIs_check() {
            return is_check;
        }

        public void setIs_check(boolean is_check) {
            this.is_check = is_check;
        }

        public Prce(String price_id, double price, int days, double prime_price, String unit, int is_discount,long expire_at) {
            this.price_id = price_id;
            this.price = price;
            this.days = days;
            this.prime_price = prime_price;
            this.unit = unit;
            this.is_discount = is_discount;
            this.expire_at=expire_at;
        }

        public String getPrice_id() {
            return price_id;
        }

        public void setPrice_id(String price_id) {
            this.price_id = price_id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public double getPrime_price() {
            return prime_price;
        }

        public void setPrime_price(double prime_price) {
            this.prime_price = prime_price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public boolean getIs_discount() {
            return is_discount==1;
        }

        public void setIs_discount(int is_discount) {
            this.is_discount = is_discount;
        }
    }
}

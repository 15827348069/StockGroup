package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/4/9.
 */

public class WxOrder extends Erro implements Serializable{
    private String status;
    private Order order;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public class Order implements Serializable{
//        private String appid;
//        private String noncestr;
//        private String Sign;
//        private String partnerid;
//        private String prepayid;
//        private String timestamp;
//        private String sign;
//
//        public String getAppid() {
//            return appid;
//        }
//
//        public void setAppid(String appid) {
//            this.appid = appid;
//        }
//
//        public String getNoncestr() {
//            return noncestr;
//        }
//
//        public void setNoncestr(String noncestr) {
//            this.noncestr = noncestr;
//        }
//
//        public String getSign() {
//            return Sign;
//        }
//
//        public void setSign(String sign) {
//            Sign = sign;
//        }
//
//        public String getPartnerid() {
//            return partnerid;
//        }
//
//        public void setPartnerid(String partnerid) {
//            this.partnerid = partnerid;
//        }
//
//        public String getPrepayid() {
//            return prepayid;
//        }
//
//        public void setPrepayid(String prepayid) {
//            this.prepayid = prepayid;
//        }
//
//        public String getTimestamp() {
//            return timestamp;
//        }
//
//        public void setTimestamp(String timestamp) {
//            this.timestamp = timestamp;
//        }
    }
}

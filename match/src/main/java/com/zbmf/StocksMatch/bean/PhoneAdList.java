package com.zbmf.StocksMatch.bean;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/11/30.
 */

public class PhoneAdList extends Erro {
    private String status;
    private List<Adverts >adverts;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Adverts> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<Adverts> adverts) {
        this.adverts = adverts;
    }

//    public Result getResult() {
//        return result;
//    }
//
//    public void setResult(Result result) {
//        this.result = result;
//    }

//    public class Result{
//        private List<Adverts> adverts;
//
//        public List<Adverts> getAdverts() {
//            return adverts;
//        }
//
//        public void setAdverts(List<Adverts> adverts) {
//            this.adverts = adverts;
//        }
//    }
//    private int match_count;
//    private List<PhoneAd>phone_ads;
//
//    public int getMatch_count() {
//        return match_count;
//    }
//
//    public void setMatch_count(int match_count) {
//        this.match_count = match_count;
//    }
//
//    public List<PhoneAd> getPhone_ads() {
//        return phone_ads;
//    }
//
//    public void setPhone_ads(List<PhoneAd> phone_ads) {
//        this.phone_ads = phone_ads;
//    }
}

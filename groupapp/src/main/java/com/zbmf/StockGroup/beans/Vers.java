package com.zbmf.StockGroup.beans;

/**
 * Created by lulu
 * on 16/4/27.
 */
public class Vers extends General {
    private String updated_at;//更新时间
    private String version;//版本号
    private String subject;//更新标题
    private String intro;//更新内容
    private Logics logics;//升级逻辑(为空代表不强制升级)
    private String url;
    private int group_kchart;
    private int kchart;
    private int emergency;

    private address match;
    private address group;
    private address www;
    private address passport;
    private address stock;

    public address getMatch() {
        return match;
    }

    public void setMatch(address match) {
        this.match = match;
    }

    public address getGroup() {
        return group;
    }

    public void setGroup(address group) {
        this.group = group;
    }

    public address getWww() {
        return www;
    }

    public void setWww(address www) {
        this.www = www;
    }

    public address getPassport() {
        return passport;
    }

    public void setPassport(address passport) {
        this.passport = passport;
    }

    public address getStock() {
        return stock;
    }

    public void setStock(address stock) {
        this.stock = stock;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Logics getLogics() {
        return logics;
    }

    public void setLogics(Logics logics) {
        this.logics = logics;
    }

    public int getGroup_kchart() {
        return group_kchart;
    }

    public void setGroup_kchart(int group_kchart) {
        this.group_kchart = group_kchart;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public int getKchart() {
        return kchart;
    }

    public void setKchart(int kchart) {
        this.kchart = kchart;
    }


    /**
     * 升级逻辑(为空代表不强制升级)
     */
    public static class Logics {
        public String state;//强制升级
        public String intro;//逻辑详情
    }


    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public static class address{
        private String host;
        private String api;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }
    }
}

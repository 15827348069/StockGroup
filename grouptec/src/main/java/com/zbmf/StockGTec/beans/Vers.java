package com.zbmf.StockGTec.beans;

/**
 * Created by lulu on 16/4/27.
 */
public class Vers extends General {
    private String updated_at;//更新时间
    private String version;//版本号
    private String subject;//更新标题
    private String intro;//更新内容
    private Logics logics;//升级逻辑(为空代表不强制升级)
    private String url;
    private String download;

    public void setDownload(String download) {
        this.download = download;
    }

    public String getDownload() {
        return download;
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

}

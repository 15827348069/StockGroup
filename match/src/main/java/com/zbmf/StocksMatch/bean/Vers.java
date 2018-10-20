package com.zbmf.StocksMatch.bean;

/**
 * Created by xuhao
 * on 2017/11/23.
 */

public class Vers extends BaseBean {
    private String version;
    private String subject;
    private String intro;
    private String updated_at;
    private String download;
    private int kchart;
    private Address address;

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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getKchart() {
        return kchart;
    }

    public void setKchart(int kchart) {
        this.kchart = kchart;
    }

    public static class logics{
        private int state;
        private String intro;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }
    }

    @Override
    public String toString() {
        return "Vers{" +
                "version='" + version + '\'' +
                ", subject='" + subject + '\'' +
                ", intro='" + intro + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", download='" + download + '\'' +
                ", address=" + address +
                '}';
    }
}

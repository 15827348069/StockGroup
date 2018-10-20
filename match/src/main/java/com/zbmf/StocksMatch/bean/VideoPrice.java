package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by xuhao
 * on 2017/7/7.
 */

public class VideoPrice implements Serializable{
    private double video_price;//视频价格
    private int video_discount;//	视频折扣
    private String video_start_at;//直播开始时间(订阅)or视频上传时间
    private double seris_price;//专辑价格
    private int series_discount;//专辑折扣
    private String series_name;//	专辑名称
    private int series_phase;//	期数
    private int series_status;//	待续(0) 已完结(1)
    private double group_price;//	月铁粉价格
    private String group_name;

//    public VideoPrice() {
//    }

    public VideoPrice(double video_price, int video_discount, String video_start_at, double seris_price, int series_discount, String series_name, int series_phase, int series_status, double group_price, String group_name) {
        this.video_price = video_price;
        this.video_discount = video_discount;
        this.video_start_at = video_start_at;
        this.seris_price = seris_price;
        this.series_discount = series_discount;
        this.series_name = series_name;
        this.series_phase = series_phase;
        this.series_status = series_status;
        this.group_price = group_price;
        this.group_name = group_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public double getVideo_price() {
        return video_price;
    }

    public void setVideo_price(double video_price) {
        this.video_price = video_price;
    }

    public int getVideo_discount() {
        return video_discount;
    }

    public void setVideo_discount(int video_discount) {
        this.video_discount = video_discount;
    }

    public String getVideo_start_at() {
        return video_start_at;
    }

    public void setVideo_start_at(String video_start_at) {
        this.video_start_at = video_start_at;
    }

    public double getSeris_price() {
        return seris_price;
    }

    public void setSeris_price(double seris_price) {
        this.seris_price = seris_price;
    }

    public int getSeries_discount() {
        return series_discount;
    }

    public void setSeries_discount(int series_discount) {
        this.series_discount = series_discount;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public int getSeries_phase() {
        return series_phase;
    }

    public void setSeries_phase(int series_phase) {
        this.series_phase = series_phase;
    }

    public int getSeries_status() {
        return series_status;
    }

    public void setSeries_status(int series_status) {
        this.series_status = series_status;
    }

    public double getGroup_price() {
        return group_price;
    }

    public void setGroup_price(double group_price) {
        this.group_price = group_price;
    }

    @Override
    public String toString() {
        return "VideoPrice{" +
                "video_price=" + video_price +
                ", video_discount=" + video_discount +
                ", video_start_at=" + video_start_at +
                ", seris_price=" + seris_price +
                ", series_discount=" + series_discount +
                ", series_name='" + series_name + '\'' +
                ", series_phase=" + series_phase +
                ", series_status=" + series_status +
                ", group_price=" + group_price +
                '}';
    }
}

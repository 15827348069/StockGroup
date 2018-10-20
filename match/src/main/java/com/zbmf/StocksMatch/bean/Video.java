package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by xuhao
 * on 2017/6/22.
 */

public class Video implements Serializable{
    private String videoName;
    private String videoId;

    private String videoDate;
    private String videoImg;
    private String video_tag;

    private String user_id;
    private String videoGroupname;
    private String avatar;
    private String group_id;

    private double videoPrice;
    private double videonewPrice;
    private String videoParticipation;
    private int videoType;
    private double videoPriceType;
    private String videoPlayType;
    private int is_live;
    private int order;
    private int status;
    private int is_group;
    private int is_fans;
    private int series_id;
    private SeriesVideo seriesVideo;
    private VideoPrice videoPriceBean;
    private int videoViewType;
    private int is_series;
    private String share_url;

    private String share_img;

    private String bokecc_id;

    private String content;

    private int follow;
    private long start_time;

    public boolean getIs_series() {
        return is_series==1;
    }

    public int getVideoViewType() {
        return videoViewType;
    }

    public void setVideoViewType(int videoViewType) {
        this.videoViewType = videoViewType;
    }

    public void setIs_series(int is_series) {
        this.is_series = is_series;
    }

    public SeriesVideo getSeriesVideo() {
        return seriesVideo;
    }

    public void setSeriesVideo(SeriesVideo seriesVideo) {
        this.seriesVideo = seriesVideo;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public int getSeries_id() {
        return series_id;
    }

    public VideoPrice getVideoPriceBean() {
        return videoPriceBean;
    }

    public void setVideoPriceBean(VideoPrice videoPriceBean) {
        this.videoPriceBean = videoPriceBean;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getIs_group() {
        return is_group==1;
    }

    public void setIs_group(int is_group) {
        this.is_group = is_group;
    }

    public boolean getIs_fans() {
        return is_fans==1;
    }

    public void setIs_fans(int is_fans) {
        this.is_fans = is_fans;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getFollow() {
        return follow==1;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public boolean getOrder() {
        return order==1;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getBokecc_id() {
        return bokecc_id;
    }

    public void setBokecc_id(String bokecc_id) {
        this.bokecc_id = bokecc_id;
    }

    public String getShare_url() {
        return share_url;
    }

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public boolean getIs_live() {
        return is_live==1;
    }

    public void setIs_live(int is_live) {
        this.is_live = is_live;
    }

    public String getVideoPlayType() {
        return videoPlayType;
    }

    public void setVideoPlayType(String videoPlayType) {
        this.videoPlayType = videoPlayType;
    }

    public String getVideoParticipation() {
        return videoParticipation;
    }

    public void setVideoParticipation(String videoParticipation) {
        this.videoParticipation = videoParticipation;
    }

    public double getVideonewPrice() {
        return videonewPrice;
    }

    public void setVideonewPrice(double videonewPrice) {
        this.videonewPrice = videonewPrice;
    }

    public String getVideo_tag() {
        return video_tag;
    }

    public void setVideo_tag(String video_tag) {
        this.video_tag = video_tag;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoGroupname() {
        return videoGroupname;
    }

    public void setVideoGroupname(String videoGroupname) {
        this.videoGroupname = videoGroupname;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public double getVideoPrice() {
        return videoPrice;
    }

    public void setVideoPrice(double videoPrice) {
        this.videoPrice = videoPrice;
    }


    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public double getVideoPriceType() {
        return videoPriceType;
    }

    public void setVideoPriceType(double videoPriceType) {
        this.videoPriceType = videoPriceType;
    }

    public static class SeriesVideo implements Serializable{
        private String phase;//专辑期数
        private String new_phase;//总期数
        private String name; //专辑名
        private String teacher_name;//老师名字
        private int status;//1完结   0未完结
        private String share_url;
        private String series_id;
        private String pic_play;
        private String pic_thumb;
        private String created_at;
        private String teacher_avatar;
        private int series_num;
        private int commit;
        private int is_play;
        private double series_price;
        private double new_series_price;
        private int discount;
        private Video video;

        public Video getVideo() {
            return video;
        }

        public void setVideo(Video video) {
            this.video = video;
        }

        public String getPic_thumb() {
            return pic_thumb;
        }

        public void setPic_thumb(String pic_thumb) {
            this.pic_thumb = pic_thumb;
        }

        public String getNew_phase() {
            return new_phase;
        }

        public void setNew_phase(String new_phase) {
            this.new_phase = new_phase;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public String getTeacher_avatar() {
            return teacher_avatar;
        }

        public void setTeacher_avatar(String teacher_avatar) {
            this.teacher_avatar = teacher_avatar;
        }

        public int getSeries_num() {
            return series_num;
        }

        public void setSeries_num(int series_num) {
            this.series_num = series_num;
        }

        public double getSeries_price() {
            return series_price;
        }

        public void setSeries_price(double series_price) {
            this.series_price = series_price;
        }

        public double getNew_series_price() {
            return new_series_price;
        }

        public void setNew_series_price(double new_series_price) {
            this.new_series_price = new_series_price;
        }

        public boolean getIs_play() {
            return is_play==1;
        }

        public void setIs_play(int is_play) {
            this.is_play = is_play;
        }

        public boolean getCommit() {
            return commit==0;
        }

        public void setCommit(int commit) {
            this.commit = commit;
        }

        public String getSeries_id() {
            return series_id;
        }

        public void setSeries_id(String series_id) {
            this.series_id = series_id;
        }

        public String getPic_play() {
            return pic_play;
        }

        public void setPic_play(String pic_play) {
            this.pic_play = pic_play;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public SeriesVideo(){

        }

        public SeriesVideo(String phase,String new_phase, String name, String teacher_name, int status, String share_url,
                           String series_id, String pic_play,String pic_thumb, String created_at, String teacher_avatar,
                           int series_num, int commit, int is_play, double series_price, double new_series_price,int discount,Video video) {
            this.phase = phase;
            this.new_phase = new_phase;
            this.name = name;
            this.teacher_name = teacher_name;
            this.status = status;
            this.share_url = share_url;
            this.series_id = series_id;
            this.pic_play = pic_play;
            this.pic_thumb=pic_thumb;
            this.created_at = created_at;
            this.teacher_avatar = teacher_avatar;
            this.series_num = series_num;
            this.commit = commit;
            this.is_play = is_play;
            this.series_price = series_price;
            this.new_series_price = new_series_price;
            this.discount=discount;
            this.video=video;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String treacher_name) {
            this.teacher_name = treacher_name;
        }

        public boolean getStatus() {
            return status==1;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        @Override
        public String toString() {
            return "SeriesVideo{" +
                    "phase='" + phase + '\'' +
                    ", name='" + name + '\'' +
                    ", teacher_name='" + teacher_name + '\'' +
                    ", status=" + status +
                    ", share_url='" + share_url + '\'' +
                    ", series_id='" + series_id + '\'' +
                    ", pic_play='" + pic_play + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", teacher_avatar='" + teacher_avatar + '\'' +
                    ", series_num=" + series_num +
                    ", commit=" + commit +
                    ", is_play=" + is_play +
                    ", series_price=" + series_price +
                    ", new_series_price=" + new_series_price +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoName='" + videoName + '\'' +
                ", videoId='" + videoId + '\'' +
                ", videoDate='" + videoDate + '\'' +
                ", videoImg='" + videoImg + '\'' +
                ", video_tag='" + video_tag + '\'' +
                ", user_id='" + user_id + '\'' +
                ", videoGroupname='" + videoGroupname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", videoPrice=" + videoPrice +
                ", videoOldPrice=" + videonewPrice +
                ", videoParticipation='" + videoParticipation + '\'' +
                ", videoType=" + videoType +
                ", videoPriceType=" + videoPriceType +
                ", videoPlayType='" + videoPlayType + '\'' +
                ", is_live=" + is_live +
                ", order=" + order +
                ", status=" + status +
                ", is_group=" + is_group +
                ", is_fans=" + is_fans +
                ", share_url='" + share_url + '\'' +
                ", share_img='" + share_img + '\'' +
                ", bokecc_id='" + bokecc_id + '\'' +
                ", content='" + content + '\'' +
                ", follow=" + follow +
                '}';
    }
}

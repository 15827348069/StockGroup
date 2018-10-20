package com.zbmf.StockGTec.beans;

import java.util.List;

/**
 * Created by iMac on 2017/3/7.
 */

public class Ask extends General{
    public int pages;
    /**
     * ask_id	number	问题ID
     user_id	number	提问人ID
     nickname	string	提问人昵称
     ask_content	string	问题内容
     posted_at	string	提问时间
     */

    private String ask_id;
    private String user_id;
    private String nickname;
    private String ask_content;
    private String posted_at;
    private int answerType;
    private boolean first;

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public int getAnswerType() {
        return answerType;
    }

    /**
     * ask_status : 0
     * fans_level : 0
     * is_private : 0
     */

    private int ask_status;
    private int fans_level;
    private int is_private;
    /**
     * ask_id : 43
     * user_id : 98
     * post : {"post_id":22,"user_id":88,"nickname":"牛云","post_content":"123","ask_id":43,"is_private":0,"posted_at":"03-22 17:28:58"}
     */

    private PostBean post;

//    private List<Ask> list;
    private List<Ask> answered_asks;
    private List<Ask> unanswered_asks;

    public List<Ask> getAnswered_asks() {
        return answered_asks;
    }

    public void setAnswered_asks(List<Ask> answered_asks) {
        this.answered_asks = answered_asks;
    }

    public List<Ask> getUnanswered_asks() {
        return unanswered_asks;
    }

    public void setUnanswered_asks(List<Ask> unanswered_asks) {
        this.unanswered_asks = unanswered_asks;
    }
    //
//    public void setList(List<Ask> list) {
//        this.list = list;
//    }
//
//    public List<Ask> getList() {
//        return list;
//    }

    public String getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(String ask_id) {
        this.ask_id = ask_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAsk_content() {
        return ask_content;
    }

    public void setAsk_content(String ask_content) {
        this.ask_content = ask_content;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public int getAsk_status() {
        return ask_status;
    }

    public void setAsk_status(int ask_status) {
        this.ask_status = ask_status;
    }

    public int getFans_level() {
        return fans_level;
    }

    public void setFans_level(int fans_level) {
        this.fans_level = fans_level;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public PostBean getPost() {
        return post;
    }

    public void setPost(PostBean post) {
        this.post = post;
    }

    public static class PostBean {
        private int post_id;
        private String user_id;
        private String nickname;
        private String post_content;
        private int ask_id;
        private int is_private;
        private String posted_at;

        public int getPost_id() {
            return post_id;
        }

        public void setPost_id(int post_id) {
            this.post_id = post_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPost_content() {
            return post_content;
        }

        public void setPost_content(String post_content) {
            this.post_content = post_content;
        }

        public int getAsk_id() {
            return ask_id;
        }

        public void setAsk_id(int ask_id) {
            this.ask_id = ask_id;
        }

        public int getIs_private() {
            return is_private;
        }

        public void setIs_private(int is_private) {
            this.is_private = is_private;
        }

        public String getPosted_at() {
            return posted_at;
        }

        public void setPosted_at(String posted_at) {
            this.posted_at = posted_at;
        }
    }

}

package com.zbmf.groupro.beans;

import java.util.List;

/**
 * Created by iMac on 2017/2/20.
 */

public class Ask {
    private String ask_id;
    private String ask_status;
    private String user_id;
    private String nickname;
    private String ask_content;
    private String target_id;
    private String target_nickname;
    private String posted_at;
    private Post post;
    private List<Ask> asks;
    private int pages;
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }

    public void setAsks(List<Ask> asks) {
        this.asks = asks;
    }

    public List<Ask> getAsks() {
        return asks;
    }

    public String getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(String ask_id) {
        this.ask_id = ask_id;
    }

    public String getAsk_status() {
        return ask_status;
    }

    public void setAsk_status(String ask_status) {
        this.ask_status = ask_status;
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

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getTarget_nickname() {
        return target_nickname;
    }

    public void setTarget_nickname(String target_nickname) {
        this.target_nickname = target_nickname;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public static class Post {

        private String post_id;
        private String user_id;
        private String nickname;
        private String post_content;
        private String ask_id;
        private String posted_at;
        private int is_private;


        public boolean getIs_private() {
            return is_private==1;
        }

        public void setIs_private(int is_private) {
            this.is_private = is_private;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
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

        public String getAsk_id() {
            return ask_id;
        }

        public void setAsk_id(String ask_id) {
            this.ask_id = ask_id;
        }

        public String getPosted_at() {
            return posted_at;
        }

        public void setPosted_at(String posted_at) {
            this.posted_at = posted_at;
        }
    }
}

package com.zbmf.StockGTec.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lulu on 2017/6/6.
 */

public class LoginInfo extends General {

    private static final long serialVersionUID = 9171937430237892703L;
    private User user;

    private List<Group> groups;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public static class Group implements Serializable{

        private static final long serialVersionUID = -9053219602968460133L;
        /**
         * id : 65
         * name : 炒股普及教育
         * nickname : 牛云
         * avatar : https://oss.zbmf.com/avatar/65-299ba4aedfdd3.jpg
         * is_close : 0
         * is_private : 0
         * content : 炒股高手  新浪名博   三次获得炒股大赛一等奖炒股高手  新浪名博   三次获得炒股大赛一等奖炒股高手  新浪名博   三次获得炒股大赛一等奖炒股高手  新浪名博   三次获得炒股大赛一等奖
         */

        private int id;
        private String name;
        private String nickname;
        private String avatar;
        private int is_close;//圈子是否关闭
        private int is_private;//圈子是否私有
        private String content;
        private Auth auth;
        private boolean isCheck;

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public Auth getAuth() {
            return auth;
        }


        public void setAuth(Auth auth) {
            this.auth = auth;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getIs_close() {
            return is_close;
        }

        public void setIs_close(int is_close) {
            this.is_close = is_close;
        }

        public int getIs_private() {
            return is_private;
        }

        public void setIs_private(int is_private) {
            this.is_private = is_private;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * 0：没有权限，1：有权限)
     */
    public static class Auth implements Serializable{


        private static final long serialVersionUID = -2905812248641836473L;
        private int live;//直播室管理（编辑公告、禁言、拉黑、删消息）
        private int fans;//查看铁粉和圈子数据
        private int data;//发布铁粉悄悄话
        private int creator;//以圈主名义发言

        public int getLive() {
            return live;
        }

        public void setLive(int live) {
            this.live = live;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public int getCreator() {
            return creator;
        }

        public void setCreator(int creator) {
            this.creator = creator;
        }
    }
}

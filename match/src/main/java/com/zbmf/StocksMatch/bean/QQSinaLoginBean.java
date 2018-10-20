package com.zbmf.StocksMatch.bean;


import java.io.Serializable;

/**
 * Created by pq
 * on 2018/4/26.
 */

public class QQSinaLoginBean extends Erro implements Serializable {
    private String status;
    private String user_id;
    private String auth_token;
    private String openapi_token;
    private String bind_phone;
    private UserInfo user_info;
    private String new_user;
    private User user;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getOpenapi_token() {
        return openapi_token;
    }

    public void setOpenapi_token(String openapi_token) {
        this.openapi_token = openapi_token;
    }

    public String getBind_phone() {
        return bind_phone;
    }

    public void setBind_phone(String bind_phone) {
        this.bind_phone = bind_phone;
    }

    public String getNew_user() {
        return new_user;
    }

    public void setNew_user(String new_user) {
        this.new_user = new_user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }

//    public class User implements Serializable {
//        private String user_id;
//        private String username;
//        private String nickname;
//        private String truename;
//        private String avatar;
//        private String role;
//        private String phone;
//        private String idcard;
//
//        public String getUser_id() {
//            return user_id;
//        }
//
//        public void setUser_id(String user_id) {
//            this.user_id = user_id;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        public void setNickname(String nickname) {
//            this.nickname = nickname;
//        }
//
//        public String getTruename() {
//            return truename;
//        }
//
//        public void setTruename(String truename) {
//            this.truename = truename;
//        }
//
//        public String getAvatar() {
//            return avatar;
//        }
//
//        public void setAvatar(String avatar) {
//            this.avatar = avatar;
//        }
//
//        public String getRole() {
//            return role;
//        }
//
//        public void setRole(String role) {
//            this.role = role;
//        }
//
//        public String getPhone() {
//            return phone;
//        }
//
//        public void setPhone(String phone) {
//            this.phone = phone;
//        }
//
//        public String getIdcard() {
//            return idcard;
//        }
//
//        public void setIdcard(String idcard) {
//            this.idcard = idcard;
//        }
//    }
}

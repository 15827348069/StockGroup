package com.zbmf.StocksMatch.bean;


import java.io.Serializable;

/**
 * Created by xuhao on 2017/11/21.
 */

public class LoginUser extends BaseBean implements Serializable{
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

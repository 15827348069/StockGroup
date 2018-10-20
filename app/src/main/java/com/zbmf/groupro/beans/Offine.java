package com.zbmf.groupro.beans;

import java.util.List;

/**
 * Created by iMac on 2017/3/1.
 */

public class Offine {

    /**
     * group_id	number	圈子ID
       live	number	直播
       room	number	群聊
       user	number	私聊
     */
    private int group_id;
    private int live;
    private int room;
    private int user;
    private List<Offine> list;

    public void setList(List<Offine> list) {
        this.list = list;
    }

    public List<Offine> getList() {
        return list;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}

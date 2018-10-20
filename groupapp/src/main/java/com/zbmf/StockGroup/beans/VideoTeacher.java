package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2017/8/22.
 */

public class VideoTeacher implements Serializable{
    private String id;
    private String name;
    private int is_hot;
    private String teacher_head;
    private int attention;
    private int is_group;
    private int count_video;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public String getTeacher_head() {
        return teacher_head;
    }

    public void setTeacher_head(String teacher_head) {
        this.teacher_head = teacher_head;
    }

    public boolean getAttention() {
        return attention==1;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public boolean getIs_group() {
        return is_group==1;
    }

    public void setIs_group(int is_group) {
        this.is_group = is_group;
    }

    public int getCount_video() {
        return count_video;
    }

    public void setCount_video(int count_video) {
        this.count_video = count_video;
    }

    public VideoTeacher(String id, String name, int is_hot, String teacher_head, int attention, int is_group,int count_video,String description) {
        this.id = id;
        this.name = name;
        this.is_hot = is_hot;
        this.teacher_head = teacher_head;
        this.attention = attention;
        this.is_group = is_group;
        this.count_video = count_video;
        this.description=description;
    }

    @Override
    public String toString() {
        return "VideoTeacher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", is_hot=" + is_hot +
                ", teacher_head='" + teacher_head + '\'' +
                ", attention=" + attention +
                ", is_group=" + is_group +
                ", count_video=" + count_video +
                ", description='" + description + '\'' +
                '}';
    }
}

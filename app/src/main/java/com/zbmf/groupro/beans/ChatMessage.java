package com.zbmf.groupro.beans;

import java.io.Serializable;
import java.util.List;


public class ChatMessage extends General implements Serializable {


    private String msg_id;//消息ID
    private String msg_type;//消息类型，（系统/铁粉悄悄话/红包。。。）
    private String chat_type; //用来判断单聊还是群聊。user: 单聊；group: 群聊
    private int room; //区分聊天室和小组，1：聊天室，2：小组，0：单聊
    private String from;//发送人ID
    private int role;//发送人等级权限
    private String nickname;//发送人昵称
    private String avatar; //发送人头像
    private int to; //接收人的id或者接收group的ID
    private String time;
    private String client_msg_id;
    private int state = 1;// 状态：1上传成功，2上传失败，3下载成功，4下载失败,0正在上传
    private int isRead = 1;// 是否已经阅读过 0未读，1已读
    private int progress = -1;
    private String group_id;
    private String action;
    private String user_id;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setClient_msg_id(String client_msg_id) {
        this.client_msg_id = client_msg_id;
    }

    public String getClient_msg_id() {
        return client_msg_id;
    }

    private List<ChatMessage> mMessages;

    public void setMessages(List<ChatMessage> messages) {
        mMessages = messages;
    }

    public List<ChatMessage> getMessages() {
        return mMessages;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    private String type; //消息类型。txt: 文本消息；img: 图片；audio: 语音
    private String content;//消息内容
    private String url;//图片语音地址
    private String thumb;//图片缩略图
    private int length; //语音时间（单位：秒）

    public String getType() {
        return type;
    }

    /**
     * 消息类型 txt: 文本消息；img: 图片；audio: 语音
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}

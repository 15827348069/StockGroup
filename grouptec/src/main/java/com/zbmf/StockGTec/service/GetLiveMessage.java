package com.zbmf.StockGTec.service;


import com.zbmf.StockGTec.beans.GiftBean;
import com.zbmf.StockGTec.beans.LiveMessage;
import com.zbmf.StockGTec.utils.MessageType;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;

import org.json.JSONObject;

/**
 * Created by xuhao on 2017/1/14.
 */

public class GetLiveMessage {
    public static LiveMessage getMessage(JSONObject obj){
        LiveMessage lm=new LiveMessage();
        lm.setMessage_time(obj.optLong("time"));
        lm.setGroup_id(obj.optString("to"));
        lm.setMsg_id(obj.optString("msg_id"));
        JSONObject msg=obj.optJSONObject("msg");
        lm.setImportent(msg.optInt("important"));
        lm.setMsg(msg.toString());
        lm.setMessage_type(obj.optString("msg_type"));
        lm.setMessage_or_img(msg.optString("type"));
        switch (obj.optString("msg_type")) {
            case MessageType.CHAT:
                lm.setMessage_name(obj.optString("nickname"));
                break;
            case MessageType.FANS:
                lm.setMessage_name(obj.optString("nickname"));
                break;
            case MessageType.SYSTEM:
                lm.setMessage_name("系统消息");
                break;
            case MessageType.MEMBER:
                lm.setMessage_name("系统消息");
                break;
            case MessageType.BOX:
                lm.setMessage_name("系统消息");
                lm.setBox_name(msg.optString("subject"));
                lm.setBox_user_name(msg.optString("nickname"));
                lm.setAction(msg.optInt("action"));
                lm.setBox_id(msg.optString("box_id"));
                break;
            case MessageType.BLOG:
                lm.setMessage_name("系统消息");
                lm.setBlog_name(msg.optString("subject"));
                lm.setUser_id(msg.optString("user_id"));
                lm.setBlog_id(msg.optString("blog_id"));
                break;
            case MessageType.RED_PACKET:
                lm.setMessage_name(obj.optString("nickname"));
                lm.setRed_id(msg.optString("packet_id"));
                lm.setRed_message(msg.optString("content"));
                break;
            case MessageType.GIFT:
                //礼物
                lm.setMessage_name("系统消息");
                GiftBean gf=new GiftBean();
                gf.setGift_name(msg.optString("gift_name"));
                gf.setSend_from_user_name(msg.optString("send_name"));
                gf.setSend_gift_number(msg.optInt("amount"));
                gf.setGift_id(msg.optString("gift_id"));
                gf.setSend_from_user_id(msg.optString("send_id"));
                gf.setGift_icon(msg.optString("icon"));
                lm.setMessage_or_img(msg.optString("type"));
                lm.setMessage_countent(msg.optString("content"));
                lm.setGiftbean(gf);
                break;
        }
        switch (msg.optString("type")){
            case MessageType.TXT:
                lm.setMessage_or_img(MessageType.TXT);
                lm.setMessage_countent(msg.optString("content"));
                break;
            case MessageType.IMG:
                lm.setMessage_or_img(MessageType.IMG);
                lm.setMessage_countent(msg.optString("content"));
                lm.setThumb(msg.optString("url")+ SettingDefaultsManager.getInstance().getLiveImg());
                lm.setImg_url(msg.optString("url"));
                break;
        }
        return lm;
    }
}

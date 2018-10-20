package com.zbmf.StockGroup.service;

import android.database.Cursor;

import com.zbmf.StockGroup.beans.GiftBean;
import com.zbmf.StockGroup.beans.LiveMessage;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.MessageType;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xuhao on 2017/1/14.
 */

public class GetLiveMessage {
    public static LiveMessage getMessage(JSONObject obj,boolean is_red){
        LiveMessage lm=new LiveMessage();
        lm.setMessage_time(obj.optLong("time"));
        lm.setGroup_id(obj.optString("to"));
        lm.setMsg_id(obj.optString("msg_id"));
        JSONObject msg=obj.optJSONObject("msg");
        lm.setMsg(msg.toString());
        lm.setMessage_type(obj.optString("msg_type"));
        lm.setMessage_or_img(msg.optString("type"));
        lm.setImportent(msg.optInt("important"));
        lm.setUnablered(is_red);
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
                lm.setBox_leaver(msg.optInt("level"));
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
            case MessageType.ANSWER:
                JSONObject answer=msg.optJSONObject("answer");
                lm.setQuestion_name(answer.optString("ask_nickname"));
                lm.setQuestion_time(answer.optString("ask_time"));
                lm.setQuestion_content(answer.optString("ask_content"));
                lm.setQuestion_id(answer.optString("ask_user_id"));
                lm.setAnswer_name(answer.optString("post_nickname"));
                lm.setAnswer_time(answer.optString("post_time"));
                lm.setAnswer_content(answer.optString("post_content"));
                lm.setAnswer_id(answer.optString("post_id"));
                lm.setIs_private(answer.optInt("is_private"));
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
    public static LiveMessage getLiveMessage(Cursor c) {
        LiveMessage message = new LiveMessage();
        message.setMessage_type(c.getString(c.getColumnIndex("msg_type")));
        message.setGroup_id(c.getString(c.getColumnIndex("_to")));
        message.setMessage_time(c.getLong(c.getColumnIndex("time")));
        message.setMessage_name(c.getString(c.getColumnIndex("nickname")));
        message.setMsg_id(c.getString(c.getColumnIndex("msg_id")));
        String json_msg = c.getString(c.getColumnIndex("msg"));
        message.setMsg(json_msg);
        if (json_msg != null) {
            try {
                JSONObject msg = new JSONObject(json_msg);
                switch (message.getMessage_type()) {
                    case MessageType.CHAT:
                        break;
                    case MessageType.FANS:
                        break;
                    case MessageType.SYSTEM:
                        message.setMessage_name("系统消息");
                        break;
                    case MessageType.MEMBER:
                        message.setMessage_name("系统消息");
                        break;
                    case MessageType.GIFT:
                        //礼物
                        message.setMessage_name("系统消息");
                        GiftBean gf=new GiftBean();
                        gf.setGift_name(msg.optString("gift_name"));
                        gf.setSend_from_user_name(msg.optString("send_name"));
                        gf.setSend_gift_number(msg.optInt("amount"));
                        gf.setGift_id(msg.optString("gift_id"));
                        gf.setSend_from_user_id(msg.optString("send_id"));
                        gf.setGift_icon(msg.optString("icon"));
                        message.setMessage_or_img(msg.optString("type"));
                        message.setMessage_countent(msg.optString("content"));
                        message.setGiftbean(gf);
                        break;
                    case MessageType.BOX:
                        message.setBox_name(msg.optString("subject"));
                        message.setBox_user_name(msg.optString("nickname"));
                        message.setAction(msg.optInt("action"));
                        message.setBox_id(msg.optString("box_id"));
                        message.setBox_leaver(msg.optInt("level"));
                        break;
                    case MessageType.BLOG:
                        message.setMessage_name("系统消息");
                        message.setBlog_name(msg.optString("subject"));
                        message.setUser_id(msg.optString("user_id"));
                        message.setBlog_id(msg.optString("blog_id"));
                        break;
                    case MessageType.RED_PACKET:
                        message.setRed_id(msg.optString("packet_id"));
                        message.setRed_message(msg.optString("content"));
                        break;
                    case MessageType.ANSWER:
                        JSONObject answer=msg.optJSONObject("answer");
                        message.setQuestion_name(answer.optString("ask_nickname"));
                        message.setQuestion_time(answer.optString("ask_time"));
                        message.setQuestion_content(answer.optString("ask_content"));
                        message.setAnswer_name(answer.optString("post_nickname"));
                        message.setAnswer_time(answer.optString("post_time"));
                        message.setAnswer_content(answer.optString("post_content"));
                        break;
                }

                switch (msg.optString("type")) {
                    case MessageType.TXT:
                        message.setMessage_or_img(MessageType.TXT);
                        message.setMessage_countent(msg.optString("content"));
                        break;
                    case MessageType.IMG:
                        message.setMessage_or_img(MessageType.IMG);
                        message.setMessage_countent(msg.optString("content"));
                        message.setThumb(msg.optString("url") + SettingDefaultsManager.getInstance().getLiveImg());
                        message.setImg_url(msg.optString("url"));
                        break;
                }
                message.setImportent(msg.optInt("important"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return message;
    }
}

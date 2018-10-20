package com.zbmf.StockGroup.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.zbmf.StockGroup.beans.ChatMessage;


public class ChatMsgBuilder {
	public ChatMessage build(Cursor cursor) {
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setMsg_id(cursor.getString(cursor.getColumnIndex("msg_id")));
		chatMessage.setMsg_type(cursor.getString(cursor.getColumnIndex("msg_type")));
		chatMessage.setChat_type(cursor.getString(cursor.getColumnIndex("chat_type")));
		chatMessage.setRoom(cursor.getInt(cursor.getColumnIndex("room")));
		chatMessage.setFrom(cursor.getString(cursor.getColumnIndex("_from")));
		chatMessage.setRole(cursor.getInt(cursor.getColumnIndex("role")));
//                cursor.getString(cursor.getColumnIndex("isRead")));
		chatMessage.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
		chatMessage.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
		chatMessage.setTo(cursor.getInt(cursor.getColumnIndex("_to")));
		chatMessage.setTime(cursor.getString(cursor.getColumnIndex("time")));
//                cursor.getString(cursor.getColumnIndex("duration")));
		chatMessage.setLength(cursor.getInt(cursor.getColumnIndex("length")));
		chatMessage.setType(cursor.getString(cursor.getColumnIndex("type")));
		chatMessage.setContent(cursor.getString(cursor.getColumnIndex("content")));
//                cursor.getString(cursor.getColumnIndex("localUrl")));
//                cursor.getString(cursor.getColumnIndex("progress")));
		chatMessage.setUrl(cursor.getString(cursor.getColumnIndex("url")));
		chatMessage.setThumb(cursor.getString(cursor.getColumnIndex("thumb")));
		chatMessage.setAction(cursor.getString(cursor.getColumnIndex("action")));
		chatMessage.setClient_msg_id(cursor.getString(cursor.getColumnIndex("client_msg_id")));
		return chatMessage;
	}

	public ContentValues deconstruct(ChatMessage obj) {
		ContentValues values = new ContentValues();
		values.put("msg_id", obj.getMsg_id());
		values.put("msg_type", obj.getMsg_type());
		values.put("chat_type", obj.getChat_type());
		values.put("room", obj.getRoom());
		values.put("_from", obj.getFrom());
		values.put("role", obj.getRole());
		values.put("state", obj.getState());
		values.put("isRead", obj.getIsRead());
		values.put("nickname", obj.getNickname());
		values.put("avatar", obj.getAvatar());
		values.put("_to", obj.getTo());
		values.put("time", obj.getTime());
		values.put("type", obj.getType());
		values.put("content", obj.getContent());
		values.put("url", obj.getUrl());
		values.put("thumb", obj.getThumb());
		values.put("length", obj.getLength());
		values.put("action", obj.getAction());
		values.put("client_msg_id", obj.getClient_msg_id());
		return values;
	}

}

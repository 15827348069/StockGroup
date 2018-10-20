package com.zbmf.StocksMatch.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.zbmf.StocksMatch.beans.User;


public class UserDatabaseBuilder extends DatabaseBuilder<User> {
    @Override
    public User build(Cursor query) {
        int columnUser_id = query.getColumnIndex("user_id");
        int columnRole = query.getColumnIndex("role");
        int columnGid = query.getColumnIndex("gid");
        int columnGroup_id = query.getColumnIndex("group_id");
        int columnEnable_fans = query.getColumnIndex("enable_fans");
        int columnFans = query.getColumnIndex("fans");
        int columnUsername = query.getColumnIndex("username");
        int columnNickname = query.getColumnIndex("nickname");
        int columnAvatar = query.getColumnIndex("avatar");
        int columnPhone = query.getColumnIndex("phone");
        int columnMpay = query.getColumnIndex("mpay");
        int columnSms = query.getColumnIndex("sms");
        int columnAuth_token = query.getColumnIndex("auth_token");

        User user = new User();
        user.setUser_id(query.getString(columnUser_id));
        user.setRole(query.getString(columnRole));
        user.setGid(query.getString(columnGid));
        user.setGroup_id(query.getString(columnGroup_id));
        user.setEnable_fans(query.getString(columnEnable_fans));
        user.setFans(query.getString(columnFans));
        user.setUsername(query.getString(columnUsername));
        user.setNickname(query.getString(columnNickname));
        user.setAvatar(query.getString(columnAvatar));
        user.setPhone(query.getString(columnPhone));
        user.setMpay(query.getString(columnMpay));
        user.setAuth_token(query.getString(columnAuth_token));
        user.setSms(query.getString(columnSms));
        return user;
    }

    @Override
    public ContentValues deconstruct(User user) {
        ContentValues values = new ContentValues();
        values.put("user_id", user.getUser_id());
        values.put("role", user.getRole());
        values.put("gid", user.getGid());
        values.put("group_id", user.getGroup_id());
        values.put("enable_fans", user.getEnable_fans());
        values.put("fans", user.getFans());
        values.put("username", user.getUsername());
        values.put("nickname", user.getNickname());
        values.put("avatar", user.getAvatar());
        values.put("phone", user.getPhone());
        values.put("mpay", user.getMpay());
        values.put("sms", user.getSms());
        values.put("auth_token", user.getAuth_token());

        return values;
    }
}

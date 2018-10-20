package com.zbmf.StockGroup.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.LiveMessage;
import com.zbmf.StockGroup.beans.Offine;
import com.zbmf.StockGroup.beans.User;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.service.GetLiveMessage;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.MessageType;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2016/12/31.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;


    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * query all persons, return list
     * +" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
     * +"msg_id VARCHAR,"
     * +"msg_type VARCHAR,"
     * +"chat_type VARCHAR,"
     * +"room INTEGER,"
     * +"_from VARCHAR,"
     * +"role INTEGER,"
     * +"nickname VARCHAR,"
     * +"avatar VARCHAR,"
     * +"_to INTEGER,"
     * +"time LONG,"
     * +"msg VARCHAR"
     * +"isRead INTEGER"
     *
     * @return List<Person>
     */
    public void query(String group_id, long time, boolean just_look_fans, ResultCallback<List<LiveMessage>> callback) {
        ArrayList<LiveMessage> infolist = new ArrayList<LiveMessage>();
        Cursor c = null;
        try {
            if (just_look_fans) {
                c = db.rawQuery("SELECT * FROM " + DBHelper.LIVE_TAB_NAME + " where _to = " + group_id + " and msg_type = 'fans' and time < " + time + " order by time desc limit 100", null);
            } else {
                c = db.rawQuery("SELECT * FROM " + DBHelper.LIVE_TAB_NAME + " where ( _to = " + group_id + " or msg_type = 'system')" + " and time < " + time + " order by time desc limit 100", null);
            }
            db.beginTransaction();  //开始事务
            if (c.moveToFirst()) {
                do {
                    LiveMessage message = GetLiveMessage.getLiveMessage(c);
                    if (message.is_private()) {
                        if (message.getQuestion_id().equals(SettingDefaultsManager.getInstance().UserId())) {
                            infolist.add(0, message);
                        }
                    } else {
                        if(message.getMessage_type().equals(MessageType.SYSTEM)){
                            if(message.getGroup_id().equals(group_id)||message.getGroup_id().equals("0")){
                                infolist.add(0, message);
                            }
                        }else{
                            infolist.add(0, message);
                        }

                    }
                } while (c.moveToNext());
            }
            if (infolist.size() != 0) {
                callback.onCallback(infolist);
            } else {
                callback.onFail("消息记录为空");
            }
            db.setTransactionSuccessful();
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();    //结束事务
            }
        }
        if (c != null) {
            c.close();
        } else {
            callback.onFail("消息记录为空");
        }
    }

    /**
     * add messages
     * 批量添加数据到数据库
     *
     * @param
     */
    public void addAll(List<LiveMessage> messages, ResultCallback<Boolean> callback) {
        db.beginTransaction();  //开始事务
        try {
            for (LiveMessage message : messages) {
                add(message);
            }
            callback.onCallback(true);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * 添加单个消息到数据库
     *
     * @param message
     */
    public void add(LiveMessage message) {
        if (!msg_id_already_have(message.getMsg_id())) {
            ContentValues values = getContentValuse(message);
            db.insert(DBHelper.LIVE_TAB_NAME, null, values);
        }
    }

    /**
     * @param
     */
    public void deleteOldPerson(LiveMessage message) {
        db.delete(DBHelper.LIVE_TAB_NAME, "msg_id = ?", new String[]{message.getMsg_id()});
    }

    /**
     * message
     *
     * @return values
     */
    public ContentValues getContentValuse(LiveMessage message) {
        ContentValues values = new ContentValues();
        values.put("msg_id", message.getMsg_id());
        values.put("msg_type", message.getMessage_type());
        values.put("chat_type", message.getLive_message_type());
        if (message.isUnablered()) {
            values.put("_read", 1);
        } else {
            values.put("_read", 0);
        }
        values.put("nickname", message.getMessage_name());
        values.put("_to", message.getGroup_id());
        values.put("time", message.getMessage_time());
        values.put("msg", message.getMsg());
        return values;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor(String group_id, long time) {
        try {
            return db.rawQuery("SELECT * FROM " + DBHelper.LIVE_TAB_NAME + " where ( _to = " + group_id + " or msg_type = 'system')" + " and time < " + time + " order by time desc limit 100", null);
        } finally {
            return null;
        }
    }

    public Cursor queryFansCursor(String group_id, long time) {
        try {
            return db.rawQuery("SELECT * FROM " + DBHelper.LIVE_TAB_NAME + " where ( _to = " + group_id + " or msg_type = 'system')" + " and msg_type = 'fans' and time < " + time + " order by time desc limit 100", null);
        } finally {
            return null;
        }
    }

    public int getUnredCount(String group_id) {
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.LIVE_TAB_NAME + " where  _to = " + group_id + " and msg_type != 'system'" + " and _read = 0 ", null);
        int count = c.getCount();
        c.close();
        return count;
    }

    /**
     * 聊天室离线未读数量
     *
     * @param group_id
     * @return
     */
    public int getOfflineMsgConunt(String type, String group_id) {
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.OFFLINE_MSG + " where group_id = " + group_id, null);
        while (c.moveToNext()) {
            int count = c.getInt(c.getColumnIndex(type));
            return count;
        }
        c.close();
        return 0;
    }

    public int getOfflineMsgConunt(String type) {
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.OFFLINE_MSG, null);
        while (c.moveToNext()) {
            int count = c.getInt(c.getColumnIndex(type));
            return count;
        }
        c.close();
        return 0;
    }

    public void setOfflineMsgCount(Offine offine) {
        Cursor c = Already_have_Group(offine.getGroup_id() + "");
        ContentValues values = new ContentValues();
        values.put(Constants.LIVE, offine.getLive());
        values.put(Constants.ROOM, offine.getRoom());
        values.put(Constants.USER, offine.getUser());
        values.put("group_id", offine.getGroup_id());
        if (c != null && c.getCount() > 0) {
            //已经有记录就更新记录
            db.update(DBHelper.OFFLINE_MSG, values, "group_id=?", new String[]{offine.getGroup_id() + ""});
        } else {
            //没有就插入数据
            db.insert(DBHelper.OFFLINE_MSG, null, values);
        }
    }

    /**
     * 获取关注列表的未读消息
     *
     * @return
     */
    public int getAllUnredCount() {
        if (db != null && db.isOpen()) {
            Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.LIVE_TAB_NAME + " where _read = 0 and msg_type != 'system'", null);
            int count = c.getCount() + getOfflineMsgConunt(Constants.LIVE);
            return count;
        }
        return 0;
    }

    public boolean msg_id_already_have(String message_id) {
        Cursor cursor = db.rawQuery("select * from   " + DBHelper.LIVE_TAB_NAME + " where   msg_id=?", new String[]{message_id});
        while (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;

    }

    /**
     * +"avatar VARCHAR,"
     * +"username VARCHAR,"
     * +"nickname VARCHAR,"
     * +"role INTEGER,"
     * +"phone INTEGER,"
     * +"auth_token VARCHAR,"
     * +"user_id VARCHAR,"
     * +"truename VARCHAR "
     *
     * @param user
     */
    public void addUser(User user) {
//        db = helper.getWritableDatabase();
//        Cursor c = Already_have_User(user.getUser_id());
//        ContentValues values = new ContentValues();
//        values.put("avatar", user.getAvatar());
//        values.put("username", user.getUsername());
//        values.put("nickname", user.getNickname());
//        values.put("role", user.getRole());
//        values.put("phone", user.getPhone());
//        values.put("auth_token", user.getAuth_token());
//        values.put("user_id", user.getUser_id());
//        values.put("truename", user.getTruename());
//        if (c != null && c.getCount() > 0) {
//            //已经有记录就更新记录
//            db.update(DBHelper.USER_TABLE, values, "user_id=?", new String[]{user.getUser_id()});
//        } else {
//            //没有就插入数据
//            db.insert(DBHelper.USER_TABLE, null, values);
//        }
        SettingDefaultsManager.getInstance().setAuthtoken(user.getAuth_token());
        SettingDefaultsManager.getInstance().setUserId(user.getUser_id());
        SettingDefaultsManager.getInstance().setUserAvatar(user.getAvatar());
        SettingDefaultsManager.getInstance().setUserPhone(user.getPhone());
        SettingDefaultsManager.getInstance().setNickName(user.getNickname());
        SettingDefaultsManager.getInstance().setTrueName(user.getTruename());
        SettingDefaultsManager.getInstance().setIdcard(user.getIdcard());
        SettingDefaultsManager.getInstance().setIdcard(user.getIdcard());
        SettingDefaultsManager.getInstance().setIsVip(user.getIs_vip());
        SettingDefaultsManager.getInstance().setSuperVip(user.getIs_super());
        SettingDefaultsManager.getInstance().setVipAtEnd(user.getVip_end_at());
    }

    public List<User> queryUser() {
        ArrayList<User> infolist = new ArrayList<User>();
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.USER_TABLE, null);
        while (c.moveToNext()) {
            User user = new User();
            user.setAvatar(c.getString(c.getColumnIndex("avatar")));
            user.setUsername(c.getString(c.getColumnIndex("username")));
            user.setNickname(c.getString(c.getColumnIndex("nickname")));
            user.setRole(c.getString(c.getColumnIndex("role")));
            user.setPhone(c.getString(c.getColumnIndex("phone")));
            user.setAuth_token(c.getString(c.getColumnIndex("auth_token")));
            user.setUser_id(c.getString(c.getColumnIndex("user_id")));
            user.setTruename(c.getString(c.getColumnIndex("truename")));
            infolist.add(user);
        }
        c.close();
        return infolist;
    }

    public void addGroups(List<Group> groups) {
        db.beginTransaction();  //开始事务
        try {
            for (Group group : groups) {
                addGroup(group);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addGroup(Group group) {
        ContentValues values = getGroupContentValuse(group);
        if (AlreadyGroup(group.getId())) {
            db.update(DBHelper.GROUP_TABLE, values, "group_id=?", new String[]{group.getId()});
        } else {
            db.insert(DBHelper.GROUP_TABLE, null, values);
        }
    }

    public boolean isGroupOnline(String id) {
        Cursor c = db.rawQuery("select * from   " + DBHelper.GROUP_TABLE + " where   group_id=? ", new String[]{id});
        if (c != null && c.getCount() > 0) {
            int value = 0;
            if (c.moveToFirst()) {
                value = c.getInt(c.getColumnIndex("is_online"));
                LogUtil.e(c.getString(c.getColumnIndex("group_id")) + "数据库" + value);
            }
            return value == 1;
        } else {
            return false;
        }
    }

    public void updataGroup(String id, int is_only) {
        ContentValues values = new ContentValues();
        values.put("is_online", is_only);
        LogUtil.e(id + "更新圈子未读状态" + is_only);
        if (id == null) {
            db.update(DBHelper.GROUP_TABLE, values, null, null);
        } else {
            db.update(DBHelper.GROUP_TABLE, values, " group_id=? ", new String[]{id});
            isGroupOnline(id);
        }
    }

    public boolean AlreadyGroup(String user_id) {
        try {
            Cursor c = db.query(DBHelper.GROUP_TABLE, new String[]{"group_id"}, "group_id = ?", new String[]{user_id}, null, null, null);
            if (c != null && c.getCount() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param group
     * @return
     */
    public ContentValues getGroupContentValuse(Group group) {
        ContentValues values = new ContentValues();
        values.put("avatar", group.getAvatar());
        values.put("username", group.getName());
        values.put("nickname", group.getNick_name());
        values.put("group_id", group.getId());
        values.put("is_online", group.getIs_online() ? 1 : 0);
        return values;
    }

    public List<Group> queryGroup() {
        ArrayList<Group> infolist = new ArrayList<Group>();
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.GROUP_TABLE, null);
        while (c.moveToNext()) {
            Group group = new Group();
            group.setAvatar(c.getString(c.getColumnIndex("avatar")));
            group.setName(c.getString(c.getColumnIndex("username")));
            group.setNick_name(c.getString(c.getColumnIndex("nickname")));
            group.setId(c.getString((c.getColumnIndex("group_id"))));
            group.setIs_online(c.getInt(c.getColumnIndex("is_online")));
            infolist.add(group);
        }
        c.close();
        return infolist;
    }

    /**
     * 删除用户
     *
     * @param user
     */
    public void deleteUser(User user) {
        db.delete(DBHelper.USER_TABLE, "user_id=?", new String[]{user.getUser_id()});
        db.close();
    }

    /**
     * 设置消息状态为已读
     *
     * @param group_id
     */
    public void setUnablemessage(String group_id) {
        ContentValues values = new ContentValues();
        values.put("_read", 1);
        if (group_id == null) {
            db.update(DBHelper.LIVE_TAB_NAME, values, null, null);
        } else {
            db.update(DBHelper.LIVE_TAB_NAME, values, "_to=? or msg_type=?", new String[]{group_id, "system"});
        }
        ContentValues value = new ContentValues();
        value.put(Constants.LIVE, 0);
        if (group_id == null) {
            db.update(DBHelper.OFFLINE_MSG, value, null, null);
        } else {
            db.update(DBHelper.OFFLINE_MSG, value, "group_id=?", new String[]{group_id});
        }
    }

    public void setMessageUnable(String msg_id) {
        ContentValues values = new ContentValues();
        values.put("_read", 1);
        int flag = db.update(DBHelper.LIVE_TAB_NAME, values, "msg_id=?", new String[]{msg_id});

    }

    /**
     * 判断是否已经有该用户
     *
     * @param user_id
     * @return
     */
    public Cursor Already_have_User(String user_id) {
        try {
            Cursor c = db.query(DBHelper.USER_TABLE, new String[]{"user_id"}, "user_id = ?", new String[]{user_id}, null, null, null);
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param group_id
     * @return
     */
    public Cursor Already_have_Group(String group_id) {
        try {
            Cursor c = db.query(DBHelper.OFFLINE_MSG, new String[]{"group_id"}, "group_id = ?", new String[]{group_id}, null, null, null);
            return c;
        } catch (Exception e) {
            return null;
        }
    }


}

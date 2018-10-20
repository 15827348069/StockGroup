package com.zbmf.StockGTec.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.zbmf.StockGTec.beans.LiveMessage;
import com.zbmf.StockGTec.beans.User;
import com.zbmf.StockGTec.utils.MessageType;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;

import org.json.JSONException;
import org.json.JSONObject;

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
     *   +" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
     +"msg_id VARCHAR,"
     +"msg_type VARCHAR,"
     +"chat_type VARCHAR,"
     +"room INTEGER,"
     +"_from VARCHAR,"
     +"role INTEGER,"
     +"nickname VARCHAR,"
     +"avatar VARCHAR,"
     +"_to INTEGER,"
     +"time LONG,"
     +"msg VARCHAR"
     +"isRead INTEGER"
     * @return List<Person>
     */
    public List<LiveMessage> query(String group_id, long time) {
        ArrayList<LiveMessage> infolist = new ArrayList<LiveMessage>();
        Cursor c = queryTheCursor(group_id,time);
        while (c.moveToNext()) {
            LiveMessage message = new LiveMessage();
            message.setMessage_type(c.getString(c.getColumnIndex("msg_type")));
            message.setGroup_id(c.getString(c.getColumnIndex("_to")));
            message.setMessage_time(c.getLong(c.getColumnIndex("time")));
            message.setMessage_name(c.getString(c.getColumnIndex("nickname")));
            message.setMsg_id(c.getString(c.getColumnIndex("msg_id")));
            String json_msg=c.getString(c.getColumnIndex("msg"));
            if(json_msg!=null) {
                try {
                    JSONObject msg = new JSONObject(json_msg);
                    switch (message.getMessage_type()) {
                        case MessageType.CHAT:
//                            message.setLive_message_type(MessageType.MESSAGE);
                            break;
                        case MessageType.FANS:
//                            message.setLive_message_type(MessageType.FANS_MESSAGE);
                            break;
                        case MessageType.SYSTEM:
//                            message.setLive_message_type(MessageType.SYSTEM_MESSAGE);
                            break;
                        case MessageType.BOX:
//                            message.setLive_message_type(MessageType.BOX_MESSAGE);
//                            message.setMessage_type(msg.optString("type"));
                            message.setBox_name(msg.optString("subject"));
                            message.setBox_user_name(msg.optString("nickname"));
                            message.setAction(msg.optInt("action"));
                            message.setBox_id(msg.optString("box_id"));
                            break;
                        case MessageType.RED_PACKET:
                            message.setRed_id(msg.optString("packet_id"));
                            message.setRed_message(msg.optString("content"));
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
                            message.setThumb(msg.optString("url")+ SettingDefaultsManager.getInstance().getLiveImg());
                            message.setImg_url(msg.optString("url"));
                            break;
                    }
                    message.setImportent(msg.optInt("important"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            infolist.add(0,message);
        }
        c.close();
        return infolist;
    }
    /**
     * add messages
     * 批量添加数据到数据库
     * @param
     */
    public void addAll(List<LiveMessage> messages) {
        db.beginTransaction();  //开始事务
        try {
            for (LiveMessage message : messages) {
                add(message);
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * 添加单个消息到数据库
     * @param message
     */
    public void add(LiveMessage message){
        Cursor c=msg_id_already_have(message.getMsg_id());
        if(c!=null&&c.getCount()>0){
            return;
        }
        ContentValues values = getContentValuse(message);
        db.insert(DBHelper.LIVE_TAB_NAME ,null, values);
    }
    /**
     * @param
     */
    public void deleteOldPerson(LiveMessage message) {
        db.delete(DBHelper.LIVE_TAB_NAME, "msg_id = ?", new String[]{message.getMsg_id()});
    }
    /**
     * message
     * @return values
     */
    public ContentValues getContentValuse(LiveMessage message){
        ContentValues values =new ContentValues();
        values.put("msg_id",message.getMsg_id());
        values.put("msg_type",message.getMessage_type());
        values.put("chat_type",message.getLive_message_type());
        values.put("nickname",message.getMessage_name());
        values.put("_to",message.getGroup_id());
        values.put("time",message.getMessage_time());
        values.put("msg",message.getMsg());
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
     * @return  Cursor
     */
    public Cursor queryTheCursor(String group_id,long time) {
        //+" where _to = "+group_id+" and time < "+time +" order by time desc limit 20"
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.LIVE_TAB_NAME+" where _to = "+group_id+" and time < "+time +" order by time desc limit 20", null);
        return c;
    }
    public Cursor msg_id_already_have(String message_id) {
//       return db.rawQuery("SELECT * FROM "+DBHelper.LIVE_TAB_NAME+" where msg_id = "+message_id, null);
        try {
            Cursor c= db.query(DBHelper.LIVE_TAB_NAME, new String[]{"msg_id"}, "msg_id = ?", new String[]{message_id},null,null,null);
            return c;
        }catch (Exception e){
            return null;
        }
    }
    /**
     +"avatar VARCHAR,"
     +"username VARCHAR,"
     +"nickname VARCHAR,"
     +"role INTEGER,"
     +"phone INTEGER,"
     +"auth_token VARCHAR,"
     +"user_id VARCHAR,"
     +"truename VARCHAR "
     * @param user
     */
    public void addUser(User user){
        Cursor c=Already_have_User(user.getUser_id());
        ContentValues values =new ContentValues();
        values.put("avatar",user.getAvatar());
        values.put("username",user.getUsername());
        values.put("nickname",user.getNickname());
        values.put("role",user.getRole());
        values.put("phone",user.getPhone());
        values.put("auth_token",user.getAuth_token());
        values.put("user_id",user.getUser_id());
        values.put("truename",user.getTruename());
        if(c!=null&&c.getCount()>0){
            //已经有记录就更新记录
            Log.e("更新数据","user"+user.getUsername());
            db.update(DBHelper.USER_TABLE ,values,"user_id=?",new String []{user.getUser_id()});
        }else{
            //没有就插入数据
            Log.e("插入数据","user"+user.getUsername());
            db.insert(DBHelper.USER_TABLE ,null, values);
        }
        SettingDefaultsManager.getInstance().setAuthtoken(user.getAuth_token());
        SettingDefaultsManager.getInstance().setUserId(user.getUser_id());
        SettingDefaultsManager.getInstance().setUserAvatar(user.getAvatar());
//        SettingDefaultsManager.getInstance().setUserPhone(user.getPhone());
        SettingDefaultsManager.getInstance().setNickName(user.getNickname());
        c.close();
    }
    public List<User> queryUser() {
        ArrayList<User> infolist = new ArrayList<User>();
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.USER_TABLE, null);
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
            Log.e("setUsername",user.getUsername());
            infolist.add(user);
        }
        c.close();
        return infolist;
    }

    /**
     * 删除用户
     * @param user
     */
    public void deleteUser(User user){
        db.delete(DBHelper.USER_TABLE, "user_id=?", new String[]{user.getUser_id()});
        db.close();
    }

    /**
     * 判断是否已经有该用户
     * @param user_id
     * @return
     */
    public Cursor Already_have_User(String user_id) {
        try {
            Cursor c= db.query(DBHelper.USER_TABLE, new String[]{"user_id"}, "user_id = ?", new String[]{user_id},null,null,null);
            return c;
        }catch (Exception e){
            return null;
        }
    }

}

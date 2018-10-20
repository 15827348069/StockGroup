package com.zbmf.groupro.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zbmf.groupro.beans.LiveMessage;
import com.zbmf.groupro.beans.Offine;
import com.zbmf.groupro.beans.User;
import com.zbmf.groupro.service.GetLiveMessage;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.SettingDefaultsManager;

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
    public List<LiveMessage> query(String group_id,long time,boolean just_look_fans) {
        ArrayList<LiveMessage> infolist = new ArrayList<LiveMessage>();
        Cursor c=null;
        if(just_look_fans){
            c = queryFansCursor(group_id,time);
        }else{
            c = queryTheCursor(group_id,time);
        }
        while (c.moveToNext()) {
            LiveMessage message = GetLiveMessage.getLiveMessage(c);
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
            c.close();
            return;
        }
        ContentValues values = getContentValuse(message);
        db.insert(DBHelper.LIVE_TAB_NAME ,null, values);
    }
    /**
     * @param
     */
    public void deleteOldPerson(LiveMessage message) {
        db.delete(DBHelper.LIVE_TAB_NAME, "msg_id = ?",new String[]{message.getMsg_id()});
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
        if(message.isUnablered()){
            values.put("_read",1);
        }else{
            values.put("_read",0);
        }
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
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.LIVE_TAB_NAME+" where ( _to = "+group_id+" or msg_type = 'system')"+" and time < "+time +" order by time desc limit 20", null);
        return c;
    }
    public Cursor queryFansCursor(String group_id,long time) {
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.LIVE_TAB_NAME+" where ( _to = "+group_id+" or msg_type = 'system')"+" and msg_type = 'fans' and time < "+time +" order by time desc limit 20", null);
        return c;
    }
    public int getUnredCount(String group_id){
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.LIVE_TAB_NAME+" where  _to = "+group_id+" and msg_type != 'system'"+" and _read = 0 ", null);
        int count=c.getCount()+getOfflineMsgConunt(Constants.LIVE,group_id);
        c.close();
        return count;
    }

    /**
     * 聊天室离线未读数量
     * @param group_id
     * @return
     */
    public int getOfflineMsgConunt(String type,String group_id){
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.OFFLINE_MSG+" where group_id = "+group_id, null);
        while (c.moveToNext()){
            int count=c.getInt(c.getColumnIndex(type));
            return count;
        }
        c.close();
        return 0;
    }
    public int getOfflineMsgConunt(String type){
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.OFFLINE_MSG, null);
        while (c.moveToNext()){
            int count=c.getInt(c.getColumnIndex(type));
            return count;
        }
        c.close();
        return 0;
    }
    public void setOfflineMsgCount(Offine offine){
        Cursor c=Already_have_Group(offine.getGroup_id()+"");
        ContentValues values =new ContentValues();
        values.put(Constants.LIVE,offine.getLive());
        values.put(Constants.ROOM,offine.getRoom());
        values.put(Constants.USER,offine.getUser());
        values.put("group_id",offine.getGroup_id());
        if(c!=null&&c.getCount()>0){
            //已经有记录就更新记录
            db.update(DBHelper.OFFLINE_MSG ,values,"group_id=?",new String []{offine.getGroup_id()+""});
        }else{
            //没有就插入数据
            db.insert(DBHelper.OFFLINE_MSG ,null, values);
        }
    }
    /**
     * 获取关注列表的未读消息
     * @return
     */
    public int getAllUnredCount(){
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.LIVE_TAB_NAME+" where _read = 0 and msg_type != 'system'", null);
        int count=c.getCount()+getOfflineMsgConunt(Constants.LIVE);
        c.close();
        return count;
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
        db = helper.getWritableDatabase();
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
            db.update(DBHelper.USER_TABLE ,values,"user_id=?",new String []{user.getUser_id()});
        }else{
            //没有就插入数据
            db.insert(DBHelper.USER_TABLE ,null, values);
        }
        SettingDefaultsManager.getInstance().setAuthtoken(user.getAuth_token());
        SettingDefaultsManager.getInstance().setUserId(user.getUser_id());
        SettingDefaultsManager.getInstance().setUserAvatar(user.getAvatar());
        SettingDefaultsManager.getInstance().setUserPhone(user.getPhone());
        SettingDefaultsManager.getInstance().setNickName(user.getNickname());
        SettingDefaultsManager.getInstance().setTrueName(user.getTruename());
        SettingDefaultsManager.getInstance().setIdcard(user.getIdcard());
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
     * 设置消息状态为已读
     * @param group_id
     */
    public void setUnablemessage(String group_id){
        ContentValues values =new ContentValues();
        values.put("_read",1);
        if(group_id==null){
            db.update(DBHelper.LIVE_TAB_NAME ,values,null,null);
        }else{
            db.update(DBHelper.LIVE_TAB_NAME ,values,"_to=? or msg_type=?",new String []{group_id,"system"});
        }
        ContentValues value =new ContentValues();
        value.put(Constants.LIVE,0);
        if(group_id==null){
            db.update(DBHelper.OFFLINE_MSG ,value,null,null);
        }else{
            db.update(DBHelper.OFFLINE_MSG ,value,"group_id=?",new String []{group_id});
        }
    }
    public void setMessageUnable(String msg_id){
        ContentValues values =new ContentValues();
        values.put("_read",1);
        int flag=db.update(DBHelper.LIVE_TAB_NAME ,values,"msg_id=?",new String []{msg_id});

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

    /**
     *
     * @param group_id
     * @return
     */
    public Cursor Already_have_Group(String group_id) {
        try {
            Cursor c= db.query(DBHelper.OFFLINE_MSG, new String[]{"group_id"}, "group_id = ?", new String[]{group_id},null,null,null);
            return c;
        }catch (Exception e){
            return null;
        }
    }
}

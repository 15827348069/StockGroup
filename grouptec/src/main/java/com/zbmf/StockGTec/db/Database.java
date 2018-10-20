package com.zbmf.StockGTec.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zbmf.StockGTec.beans.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class Database {
    private Context mActivity;

    private static final String DB_NAME = "stock_group";
    private static final String TABLE_USER = "group_user";
    private static final String TABLE_CHAT = "group_chat";//聊天记录
    private static final String TABLE_CHAT_ML = "group_chat_ml";//聊天目录

    private static final int vserion = 1;

    public Database(Context activity) {
        this.mActivity = activity;
        create();
    }

    private void create() {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, vid VARCHAR, version VARCHAR," +
                " url VARCHAR, news VARCHAR, force VARCHAR, intro VARCHAR, subject VARCHAR );");


        db.close();
    }

    public void createChat(String chatID, SQLiteDatabase db) {
//		SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_CHAT + chatID
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "group_id VARCHAR,msg_id VARCHAR,client_msg_id varchar,msg_type VARCHAR,chat_type VARCHAR,room INTEGER,"
                + "_from VARCHAR,role INTEGER,state INTEGER,isRead INTEGER,nickname VARCHAR,"
                + "avatar VARCHAR,_to INTEGER,"
                + "time LONG,duration VARCHAR,length VARCHAR,type VARCHAR,content VARCHAR,localUrl VARCHAR,progress INTEGER,url VARCHAR,thumb VARCHAR);");
//		db.close();
    }

    /**
     * 添加聊天记录
     * @param chatID 组id
     * @param obj
     * @return
     */
    public int addChat(String chatID, ChatMessage obj) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);;
        if (db == null) return 0;

        createChat(chatID, db);

        ContentValues values = new ContentValues();
        values.put("group_id",chatID);
        values.putAll(new ChatMsgBuilder().deconstruct(obj));

        int row_count = db.update(TABLE_CHAT + chatID, values, "msg_id=?", new String[]{obj.getMsg_id()});
        if (row_count == 0) {
            db.insert(TABLE_CHAT + chatID, null, values);
        }

//        int id = 0;
//

//        Cursor cursor = db.rawQuery("select last_insert_rowid() newid;", null);
//        while (cursor.moveToNext()) {
//            id = cursor.getInt(cursor.getColumnIndex("newid"));
//        }
        db.close();
        return id;
    }

    /**
     * 获取聊天记录总数
     * @param chatID
     * @return
     */
    public int getChatNums(String chatID) {
        SQLiteDatabase db =mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);;
        int result = 0;
        if (db == null) return 0;
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select count(*) nums from " + TABLE_CHAT + chatID +";", null);
            while (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex("nums"));
            }
        }
        db.close();
        return result;
    }

    /**
     * 获取群组聊天记录表
     * @param chatID
     * @return
     */
    public List<ChatMessage> getChatMessages(String chatID,int start,int size){
        List<ChatMessage> messages = null;
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);;
        if(db == null) return null;

        createChat(chatID,db);

        if(db.isOpen()){
            messages = new ArrayList<>();
            Cursor cursor = db.rawQuery("select * from " + TABLE_CHAT + chatID +" where group_id = '"+chatID+"' order by time desc limit "+start+","+size+";",null);
            while(cursor.moveToNext()){
                ChatMessage chatMessage = new ChatMsgBuilder().build(cursor);
                messages.add(chatMessage);
            }
        }
        db.close();
        return messages;
    }

    /**
     * 获取最新的聊天消息
     * @param chatID
     * @return
     */
    public ChatMessage getLastMessage(String chatID){
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);;
        if(db == null) return null;

        createChat(chatID,db);
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from " + TABLE_CHAT + chatID +" order by time desc" ,null);
            if(cursor!=null && cursor.moveToFirst()){
                return new ChatMsgBuilder().build(cursor);
            }
        }
        db.close();
        return null;
    }

    public void deleteMsgByMsgId(String chatID,String msg_id){
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);;
        if(db == null) return;

        createChat(chatID,db);
        if(db.isOpen()){
            db.delete(TABLE_CHAT+chatID,"msg_id = ?",new String[]{msg_id});
        }
        db.close();
    }

    /**
     * 更新聊天数据 --回调成功时
     * @param chatID
     * @param obj
     */
    public void updateChat(String chatID,ChatMessage obj) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        if (db == null) return;

        createChat(chatID,db);

        ContentValues values = new ContentValues();
        values.put("state", obj.getState());

        if (obj.getUrl() != null && obj.getUrl().length() > 0) {
            values.put("url", obj.getUrl());
        }

        if (obj.getProgress() != -1) {
            values.put("progress", obj.getProgress());
        }
//		values.putAll(new ChatMsgBuilder().deconstruct(obj));

        db.update(TABLE_CHAT+chatID, values, "client_msg_id=?", new String[]{obj.getClient_msg_id()});

        db.close();
    }
}

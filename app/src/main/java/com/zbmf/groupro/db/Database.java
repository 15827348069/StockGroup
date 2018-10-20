package com.zbmf.groupro.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zbmf.groupro.beans.ChatCatalog;
import com.zbmf.groupro.beans.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class Database {
    private Context mActivity;

    private static final String DB_NAME = "group_chat_db";
    private static final String TABLE_USER = "group_user";
    private static final String TABLE_CHAT = "group_chat";
    private static final String TABLE_CHAT_ML = "group_chat_ml";

    private static final int vserion = 1;

//    public Database(Context context) {
//        super(context, DB_NAME, null, vserion);
//        mActivity = context;
//    }

    public Database(Context activity) {
        this.mActivity = activity;
        create();
    }


//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        create();
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }

//    public Database(Context activity) {
//        this.mActivity = activity;
//
//    }

    private void create() {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, vid VARCHAR, version VARCHAR," +
                " url VARCHAR, news VARCHAR, force VARCHAR, intro VARCHAR, subject VARCHAR );");


        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_CHAT_ML +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "chatid VARCHAR unique, name VARCHAR, " +
                " avatar VARCHAR," +
                " msg_id INTEGER,unreadnum INTEGER,type INTEGER,time LONG" + ");");

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
                + "time LONG,duration VARCHAR,length VARCHAR,type VARCHAR,content VARCHAR,localUrl VARCHAR,progress INTEGER,url VARCHAR,thumb VARCHAR,action VARCHAR);");
//		db.close();
    }

    /**
     * 添加聊天记录
     *
     * @param chatID 组id
     * @param obj
     * @return
     */
    public int addChat(String chatID, ChatMessage obj) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        ;
        if (db == null) return 0;

        createChat(chatID, db);

        ContentValues values = new ContentValues();
        values.put("group_id", chatID);
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
     *
     * @param chatID
     * @return
     */
    public int getChatNums(String chatID) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        ;
        int result = 0;
        if (db == null) return 0;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select count(*) nums from " + TABLE_CHAT + chatID + ";", null);
            while (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex("nums"));
            }
        }
        db.close();
        return result;
    }

    /**
     * 获取群组聊天记录表
     *
     * @param chatID
     * @return
     */
    public List<ChatMessage> getChatMessages(String chatID, int start, int size) {
        List<ChatMessage> messages = null;
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        ;
        if (db == null) return null;

        createChat(chatID, db);

        if (db.isOpen()) {
            messages = new ArrayList<>();
            Cursor cursor = db.rawQuery("select * from " + TABLE_CHAT + chatID + " where group_id = '" + chatID + "' order by time desc limit " + start + "," + size + ";", null);
            while (cursor.moveToNext()) {
                ChatMessage chatMessage = new ChatMsgBuilder().build(cursor);
                messages.add(chatMessage);
            }
        }
        return messages;
    }

    public ChatMessage getLastMessage(String chatID) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        if (db == null) return null;

        createChat(chatID, db);
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_CHAT + chatID + " order by time desc", null);
            if (cursor != null && cursor.moveToFirst()) {
                return new ChatMsgBuilder().build(cursor);
            }
        }

        return null;
    }

    public void deleteMsgByMsgId(String chatID, String msg_id) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        ;
        if (db == null) return;

        createChat(chatID, db);
        if (db.isOpen()) {
            db.delete(TABLE_CHAT + chatID, "msg_id = ?", new String[]{msg_id});
        }
        db.close();
    }

    /**
     * 更新聊天数据 --回调成功时
     *
     * @param chatID
     * @param obj
     */
    public void updateChat(String chatID, ChatMessage obj) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        if (db == null) return;

        createChat(chatID, db);

        ContentValues values = new ContentValues();
        values.put("state", obj.getState());
        values.put("msg_id", obj.getMsg_id());

        if (obj.getUrl() != null && obj.getUrl().length() > 0) {
            values.put("url", obj.getUrl());
        }

        if (obj.getProgress() != -1) {
            values.put("progress", obj.getProgress());
        }
//		values.putAll(new ChatMsgBuilder().deconstruct(obj));

        db.update(TABLE_CHAT + chatID, values, "client_msg_id=?", new String[]{obj.getClient_msg_id()});

        db.close();
    }

    public void addChatCatalog(ChatCatalog catalog) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        if (db == null) return;

        ContentValues values = new ContentValues();
        values.put("msg_id",catalog.getMsg_id());
        values.put("chatid",catalog.getGroup_id());
        values.put("unreadnum",catalog.getUnreadnum());
        values.put("type",catalog.getType());
        values.put("time",catalog.getTime());

        String[] whereArgs = {catalog.getGroup_id()};//catalog.getChatid() =  questionid
        int row_count = db.update(TABLE_CHAT_ML, values, "chatid=?", whereArgs);
        if (row_count == 0) {
            db.insert(TABLE_CHAT_ML, null, values);
        }


        db.close();
    }


    /**
     * 获取该聊天的未读数
     * @param chatID
     * @return
     */
    public int getChatUnReadNum(String chatID) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        ChatCatalog chatMsg = null;
        if (db == null) return 0;
        createChat(chatID, db);
        Cursor cursor = db.query(TABLE_CHAT_ML, null, "chatid = ?", new String[]{chatID}, null, null, null);
        while (cursor.moveToNext()) {
            chatMsg = new ChatCatalog();
            chatMsg.setMsg_id(cursor.getString(cursor.getColumnIndex("msg_id")));
            chatMsg.setUnreadnum(cursor.getInt(cursor.getColumnIndex("unreadnum")));
            chatMsg.setGroup_id(cursor.getString(cursor.getColumnIndex("chatid")));
            chatMsg.setType(cursor.getInt(cursor.getColumnIndex("type")));
        }
        db.close();
        return  chatMsg == null ? 0 : chatMsg.getUnreadnum();
    }

    public void updateUnReadNum(String chatID, int unReadnum) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        if (db == null) return;


        ContentValues values = new ContentValues();
        values.put("unreadnum", unReadnum);

        db.update(TABLE_CHAT_ML, values, "chatid = ?", new String[]{chatID});

        db.close();
    }

    public int getMessageUnReadNum() {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        int result = 0;
        if (db == null) return result;

        Cursor cursor = db.rawQuery("select sum(unreadnum) nums from " + TABLE_CHAT_ML +" ;", null);
        while (cursor.moveToNext()) {
            result = cursor.getInt(cursor.getColumnIndex("nums"));
        }
        cursor.close();
        db.close();
        return result;
    }

    public void deleteChatBy(String chatID) {
        SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        if (db == null) return;

        createChat(chatID, db);
        if (db.isOpen()) {
            db.delete(TABLE_CHAT_ML + chatID, "chatid = ?", new String[]{chatID});
        }
        db.close();
    }

}

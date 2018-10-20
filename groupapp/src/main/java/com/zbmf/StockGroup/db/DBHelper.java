package com.zbmf.StockGroup.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

/**
 * Created by xuhao on 2016/12/31.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME="stock_group";
    public static final String LIVE_TAB_NAME="live_message";
    public static final String OFFLINE_MSG="offline_msg";
    public static final String USER_TABLE="user_table";
    public static final String GROUP_TABLE="group_table";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME +SettingDefaultsManager.getInstance().UserId(), null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建消息存储表
        db.execSQL(getLiveTabSql());
        db.execSQL(getGroupTabSql());
        db.execSQL(getOfflineMsg());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(getLiveTabSql());
        db.execSQL(getGroupTabSql());
        db.execSQL(getOfflineMsg());
    }
    public String getLiveTabSql(){
        String sql="CREATE TABLE IF NOT EXISTS "
                +LIVE_TAB_NAME
                +" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"msg_id VARCHAR,"
                +"msg_type VARCHAR,"
                +"chat_type VARCHAR,"
                +"_read INTEGER,"
                +"room INTEGER,"
                +"_from VARCHAR,"
                +"role INTEGER,"
                +"nickname VARCHAR,"
                +"avatar VARCHAR,"
                +"_to INTEGER,"
                +"time LONG,"
                +"msg VARCHAR"
                +" ); ";
        return sql;
    }
    public String geUserTabSql(){
        String sql="CREATE TABLE IF NOT EXISTS "
                +USER_TABLE
                +" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"avatar VARCHAR,"
                +"username VARCHAR,"
                +"nickname VARCHAR,"
                +"role INTEGER,"
                +"phone INTEGER,"
                +"auth_token VARCHAR,"
                +"user_id VARCHAR,"
                +"truename VARCHAR "
                +" );";
        return sql;
    }
    public String getGroupTabSql(){
        String sql="CREATE TABLE IF NOT EXISTS "
                +GROUP_TABLE
                +" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"avatar VARCHAR,"
                +"username VARCHAR,"
                +"nickname VARCHAR,"
                +"group_id VARCHAR,"
                +"is_online INTEGER "
                +" );";
        return sql;
    }
    public String getOfflineMsg(){
        String sql="CREATE TABLE IF NOT EXISTS "
                +OFFLINE_MSG
                +" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Constants.LIVE+" INTEGER,"
                +Constants.ROOM+" INTEGER,"
                +Constants.USER+" INTEGER,"
                +"group_id INTEGER"
                +" );";
        return sql;
    }
}

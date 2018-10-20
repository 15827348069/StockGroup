package com.zbmf.StockGTec.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xuhao on 2016/12/31.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="stock_group";
    public static final String LIVE_TAB_NAME="live_message";
    public static final String USER_TABLE="user_table";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建消息存储表
        db.execSQL(getLiveTabSql());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("ALTER TABLE "+ LIVE_TAB_NAME+" ADD COLUMN other STRING");
    }
    public String getLiveTabSql(){
        String sql="CREATE TABLE IF NOT EXISTS "
                +LIVE_TAB_NAME
                +" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
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
                +");";
        return sql;
    }
}

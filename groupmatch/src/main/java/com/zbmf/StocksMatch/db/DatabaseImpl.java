package com.zbmf.StocksMatch.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.utils.AssetsDatabaseManager;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseImpl implements Database {
	private Context mActivity;

	private static final String DB_NAME = "zbmf_db";
	private static final String TABLE_USER = "zbmf_user";
	private static final String TABLE_STOCKHIS = "table_stockhis";
	private static final String TABLE_GONGAO = "table_gonggao";

	public DatabaseImpl(Context activity){
		this.mActivity = activity;
		create();
	}

	private void create() {
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_USER+
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                " phone VARCHAR, nickname VARCHAR, fans VARCHAR, group_id VARCHAR," +
                " enable_fans VARCHAR, auth_token VARCHAR, sms VARCHAR ,avatar VARCHAR," +
                " advanceds VARCHAR, username VARCHAR, mpay VARCHAR, gid VARCHAR," +
                " role VARCHAR, user_id VARCHAR, groups VARCHAR, profile VARCHAR);");

		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_STOCKHIS+
				"( id INTEGER PRIMARY KEY AUTOINCREMENT, "+
				" name VARCHAR, symbol VARCHAR, type INTEGER);");

		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_GONGAO+
				"( id INTEGER PRIMARY KEY AUTOINCREMENT, "+
				" mathc_id VARCHAR, type INTEGER);");
		db.close();
	}

	@Override
	public void addStockHis(String name,String symbol,int type){
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
		if(db ==null) return;
		ContentValues values = new ContentValues();
		values.put("name",name);
		values.put("symbol",symbol);
		values.put("type",type);
		String[] whereArgs = {symbol};
		int row_count = db.update(TABLE_STOCKHIS,values,"symbol=?", whereArgs);
		if(row_count == 0){
			db.insert(TABLE_STOCKHIS,null,values);
		}

		db.close();
	}

	public void addGongGao(String mathc_id,String type){
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
		if(db ==null) return;
		ContentValues values = new ContentValues();
		values.put("mathc_id",mathc_id);
		values.put("type",type);
		String[] whereArgs = {mathc_id};
		int row_count = db.update(TABLE_GONGAO,values,"mathc_id=?", whereArgs);
		if(row_count == 0){
			db.insert(TABLE_GONGAO,null,values);
		}

		db.close();
	}

	@Override
	public void delStockHis() {
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
		if(db ==null) return;
		db.execSQL("delete from " + TABLE_STOCKHIS);
		db.close();
	}

	@Override
	public List<Stock> getStockHis(int type) {
		ArrayList<Stock> list = new ArrayList<Stock>();
		SQLiteDatabase db= mActivity.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
		if(db == null){
			return list;
		}
		String[] whereArgs = {String.valueOf(type)};
		Cursor query = db.query(TABLE_STOCKHIS,null,"type = ?",whereArgs,null,null,"id desc");//"order desc"
		if(query!=null){
			while (query.moveToNext()){
				Stock stock = new Stock();
				int columnId = query.getColumnIndex("id");
				int columnName = query.getColumnIndex("name");
				int columnSymbol = query.getColumnIndex("symbol");
				int columnType = query.getColumnIndex("type");
				stock.setName(query.getString(columnName));
				stock.setSymbol(query.getString(columnSymbol));
				stock.setType(query.getInt(columnType));
				list.add(stock);
			}
		}

		query.close();
		db.close();
		return list;
	}

	@Override
	public Stock getStocks(String symbol) {
		Stock stock = new Stock();

		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		if (mg == null)
			return null;
		SQLiteDatabase db = mg.getDatabase("StockList.db");
		Cursor query = db.rawQuery("select code as _id,code,name from stocklist where code like '"+ symbol + "%' or sname like '" + symbol + "%' ",
				null);

		if(query!=null){
			ArrayList<Stock> list = new ArrayList<Stock>();
			while (query.moveToNext()){
				Stock s = new Stock();
				int columnName = query.getColumnIndex("name");
				int columnSymbol = query.getColumnIndex("code");
				s.setName(query.getString(columnName));
				s.setSymbol(query.getString(columnSymbol));
//				Log.e("tag", "Name=" + query.getString(columnName) + ",Symbol="+query.getString(columnSymbol));
				list.add(s);
			}
			stock.setList(list);
		}
		return stock;
	}

	@Override
	public Cursor getStocks1(String symbol) {
		ArrayList<Stock> list = new ArrayList<Stock>();
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		if (mg == null)
			return null;
		SQLiteDatabase db = mg.getDatabase("StockList.db");
		Cursor query = db.rawQuery("select code as _id,code,name from stocklist where code like '"+ symbol + "%' or sname like '" + symbol + "%' ",
				null);
		return query;
	}


	@Override
	public void addUser(User user) {
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		if (db == null) return;

		ContentValues values = new ContentValues();
		values.putAll(new UserDatabaseBuilder().deconstruct(user));

//		String[] whereArgs = {"1"};
//		int row_count = db.update(TABLE_USER, values, "id=?", whereArgs);
		String[] whereArgs = {user.getUser_id()};
		int row_count = db.update(TABLE_USER, values, "user_id=?", whereArgs);
		if(row_count == 0){
			db.insert(TABLE_USER, null, values);
		}
		
		db.close();
	}

	@Override
	public void delUser(String user_id) {
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		if (db == null) return;
		String[] whereArgs = {user_id};
		db.delete(TABLE_USER, "user_id=?", whereArgs);
		db.close();
	}

	@Override
	public void LogouUser() {
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		if (db == null) return;

		db.execSQL("update " + TABLE_USER + " set token = ? where id = ?", new Object[] { "", 1 });
		db.close();
	}

	@Override
	public User getUser() {
		User user = new User();
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		SharedPreferencesUtils sp = new SharedPreferencesUtils(mActivity);
		String account = sp.getAccount();
		if (db == null) return user;
		
//		String[] whereArgs = {"1"};
//		Cursor query = db.query(TABLE_USER, null, "id = ?", whereArgs, null, null, null);

		Cursor query = db.query(TABLE_USER,null,null,null,null,null,null);
		if (query != null) {
			List<User> list = new ArrayList<User>();
			while (query.moveToNext()) {
				user = new UserDatabaseBuilder().build(query);
				if(user.getAuth_token().equals(account))
					user.setAccount(true);
				list.add(user);
			}
			user.setList(list);
		}
		
		query.close();
		db.close();
		return user;
	}

    @Override
    public User getUser(String authtoken) {
		User user = new User();
		SQLiteDatabase db = mActivity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		if (db == null) return user;

		String[] whereArgs = {authtoken};
		Cursor query = db.query(TABLE_USER, null, "auth_token = ?", whereArgs, null, null, null);
		if(query!=null){
			while(query.moveToNext()){
				user = new UserDatabaseBuilder().build(query);
				break;
			}
		}

		query.close();
		db.close();

		return user;
    }


	@Override
	public void addSymbol(Stock stock) {
		SQLiteDatabase db = mActivity.openOrCreateDatabase("StockList.db", Context.MODE_PRIVATE, null);
		if (db == null) return;

		ContentValues values = new ContentValues();
		values.put("code",stock.getSymbol());
		values.put("name",stock.getName());
		values.put("sname",stock.getMarket());//暂用market表示short
		String[] whereArgs = {stock.getSymbol()};
		int row_count = db.update("stocklist", values, "code=?", whereArgs);
		if(row_count == 0){
			db.insert("stocklist", null, values);
		}

		db.close();
	}

}

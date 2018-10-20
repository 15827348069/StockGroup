package com.zbmf.StocksMatch.db;

import android.database.Cursor;

import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.User;

import java.util.List;

public interface Database {


	public void addUser(User user);

	public void delUser(String user_id);

	public void LogouUser();

	/**
	 * 获取用户列表
	 * @return
     */
	public User getUser();

	public User getUser(String authtoken);

	/**
	 * 添加股票搜索历史记录
	 * @param name
	 * @param symbol
	 * @param type
	 */
	public void addStockHis(String name,String symbol,int type);

	public void delStockHis();
	/**
	 * 读取搜索记录
	 * @param type
	 * @return
	 */
	public List<Stock> getStockHis(int type);

	/**
	 * 查询股票
	 * @param symbol
	 * @return
	 */
	public Stock getStocks(String symbol);
	public Cursor getStocks1(String symbol);
	public void addSymbol(Stock stock);

}


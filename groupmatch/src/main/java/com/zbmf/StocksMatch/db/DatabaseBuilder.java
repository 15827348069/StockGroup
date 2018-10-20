package com.zbmf.StocksMatch.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 数据库参数构建
 * @param <T>
 */
public abstract class DatabaseBuilder<T> {

	/**
	 * 取
	 * @param c
	 * @return
	 */
	public abstract T build(Cursor c);


	/**
	 * 封装
	 * @param t
	 * @return
	 */
	public abstract ContentValues deconstruct(T t);
}

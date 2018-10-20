package com.zbmf.StockGTec.beans;

import java.io.Serializable;

public class RedBagUserMessage implements Serializable{
	private String user_name;
	private String user_avatar;
	private String user_time;
	private String countent;
	private boolean isBast;
	
	public RedBagUserMessage(){
		
	}
	/***
	 * 
	 * @param name
	 * @param avatar
	 * @param time
	 * @param countent
	 * @param isbast
	 */
	public RedBagUserMessage(String name,String avatar,String time,String countent,boolean isbast){
		this.user_name=name;
		this.user_avatar=avatar;
		this.user_time=time;
		this.countent=countent;
		this.isBast=isbast;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_avatar() {
		return user_avatar;
	}
	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}
	public String getUser_time() {
		return user_time;
	}
	public void setUser_time(String user_time) {
		this.user_time = user_time;
	}
	public String getCountent() {
		return countent;
	}
	public void setCountent(String countent) {
		this.countent = countent;
	}
	public boolean isBast() {
		return isBast;
	}
	public void setBast(boolean isBast) {
		this.isBast = isBast;
	}
	
}

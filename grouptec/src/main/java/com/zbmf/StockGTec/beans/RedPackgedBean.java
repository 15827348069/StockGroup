package com.zbmf.StockGTec.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RedPackgedBean implements Serializable{
	private String user_avatar;
	private String user_name;
	private String red_message;
	private String red_bag_money;
	private String red_type;
	private int status;
	private String err_message;
	private String err_code;
	private int red_status;
	private String red_id;
	private int total_num;
	private int receive_num;
	private int duration;
	private String coupon_url;
	
	List<RedBagUserMessage>infolist=new ArrayList<RedBagUserMessage>();
	
	private int page;
	private int pages;
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getCoupon_url() {
		return coupon_url;
	}
	public void setCoupon_url(String coupon_url) {
		this.coupon_url = coupon_url;
	}
	public List<RedBagUserMessage> getInfolist() {
		return infolist;
	}
	public void setInfolist(List<RedBagUserMessage> infolist) {
		this.infolist = infolist;
	}
	public int getTotal_num() {
		return total_num;
	}
	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}
	public int getReceive_num() {
		return receive_num;
	}
	public void setReceive_num(int receive_num) {
		this.receive_num = receive_num;
	}
	public String getRed_id() {
		return red_id;
	}
	public void setRed_id(String red_id) {
		this.red_id = red_id;
	}
	public int getRed_status() {
		return red_status;
	}
	public void setRed_status(int red_status) {
		this.red_status = red_status;
	}
	public String getRed_bag_money() {
		return red_bag_money;
	}
	public void setRed_bag_money(String red_bag_money) {
		this.red_bag_money = red_bag_money;
	}
	public String getRed_type() {
		return red_type;
	}
	public void setRed_type(String red_type) {
		this.red_type = red_type;
	}
	public String getUser_avatar() {
		return user_avatar;
	}
	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getRed_message() {
		return red_message;
	}
	public void setRed_message(String red_message) {
		this.red_message = red_message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErr_message() {
		return err_message;
	}
	public void setErr_message(String err_message) {
		this.err_message = err_message;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	
}

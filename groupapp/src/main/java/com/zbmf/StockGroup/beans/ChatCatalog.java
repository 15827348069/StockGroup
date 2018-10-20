package com.zbmf.StockGroup.beans;

import java.io.Serializable;

public class ChatCatalog implements Serializable, Comparable<ChatCatalog> {

	// private String name;
	// private String headpicpath;
	private String msg_id;
	private int unreadnum;
	private long time;
	private String group_id;
	private int type;

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public int getUnreadnum() {
		return unreadnum;
	}

	public void setUnreadnum(int unreadnum) {
		this.unreadnum = unreadnum;
	}


	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public int compareTo(ChatCatalog arg0) {
		return (int) (arg0.getTime() - time);
	}

}

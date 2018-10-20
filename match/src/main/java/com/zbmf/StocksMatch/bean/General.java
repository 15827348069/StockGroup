package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

public class General implements Serializable {
	private static final long serialVersionUID = 5872106612645698155L;

	public int id;
	public int code;

	/**
	 * 提示消息
	 */
	public String msg;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

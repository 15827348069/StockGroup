package com.zbmf.StocksMatch.beans;

import java.io.Serializable;

public class General implements Serializable {
	private static final long serialVersionUID = 5872106612645698155L;

	public int code;
	public int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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

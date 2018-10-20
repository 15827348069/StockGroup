package com.zbmf.StocksMatch.api;

public class WSError extends Throwable {
	private static final long serialVersionUID = 3631692294019244014L;
	
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

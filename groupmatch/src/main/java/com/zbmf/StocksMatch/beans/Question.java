package com.zbmf.StocksMatch.beans;

import java.io.Serializable;

public class Question implements Serializable{
	private String qeustion;
	private String answer;
	private String invite_code;
	public String getQeustion() {
		return qeustion;
	}
	public void setQeustion(String qeustion) {
		this.qeustion = qeustion;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getInvite_code() {
		return invite_code;
	}

	public void setInvite_code(String invite_code) {
		this.invite_code = invite_code;
	}
}

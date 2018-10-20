package com.zbmf.StocksMatch.beans;

import java.io.Serializable;
import java.util.List;

public class RecommendPic extends  General implements Serializable{
	private String link_url;
	private String title;
	private String pic_url;
	private String type;
	private MatchBean match;
	private String user_id;
	private String match_count;
	private int status;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	private List<RecommendPic> list;
	
	
	public String getMatch_count() {
		return match_count;
	}
	public void setMatch_count(String match_count) {
		this.match_count = match_count;
	}
	public List<RecommendPic> getList() {
		return list;
	}
	public void setList(List<RecommendPic> list) {
		this.list = list;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getLink_url() {
		return link_url;
	}
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MatchBean getMatch() {
		return match;
	}
	public void setMatch(MatchBean match) {
		this.match = match;
	}
	
	
}

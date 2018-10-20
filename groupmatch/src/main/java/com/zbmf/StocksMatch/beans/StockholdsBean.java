package com.zbmf.StocksMatch.beans;

import java.io.Serializable;
import java.util.List;

public class StockholdsBean extends General implements Serializable{
	private int page;
	private int perpage;
	private int pages;
	private String id;
	private String symbol;
	private String name;
	private double cuurent;
	private double colose;
	private double price_buy;
	private double price2;
	private double price_float;
	private double price_sell;
	private double profit;
	private double yield_float;
	private String volumn_total;
	private String volumn_infrozen;
	private String create_at;
	private String posted_at;
	private String frozen;
	private String is_show;//1 可看
	private String is_buy;
	private boolean is_vip;

	public boolean is_vip() {
		return is_vip;
	}

	public void setIs_vip(boolean is_vip) {
		this.is_vip = is_vip;
	}

	public String getIs_show() {
		return is_show;
	}

	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}

	public String getIs_buy() {
		return is_buy;
	}

	public void setIs_buy(String is_buy) {
		this.is_buy = is_buy;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerpage() {
		return perpage;
	}

	public void setPerpage(int perpage) {
		this.perpage = perpage;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getFrozen() {
		return frozen;
	}

	public void setFrozen(String frozen) {
		this.frozen = frozen;
	}

	public String getPosted_at() {
		return posted_at;
	}

	public void setPosted_at(String posted_at) {
		this.posted_at = posted_at;
	}

	private String type;
	private String volumn;
	
	
	
	public String getVolumn() {
		return volumn;
	}

	public void setVolumn(String volumn) {
		this.volumn = volumn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private List<StockholdsBean>infolist;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCuurent() {
		return cuurent;
	}

	public void setCuurent(double cuurent) {
		this.cuurent = cuurent;
	}

	public double getColose() {
		return colose;
	}

	public void setColose(double colose) {
		this.colose = colose;
	}

	public double getPrice_buy() {
		return price_buy;
	}

	public void setPrice_buy(double price_buy) {
		this.price_buy = price_buy;
	}

	public double getPrice2() {
		return price2;
	}

	public void setPrice2(double price2) {
		this.price2 = price2;
	}

	public double getPrice_float() {
		return price_float;
	}

	public void setPrice_float(double price_float) {
		this.price_float = price_float;
	}

	public double getPrice_sell() {
		return price_sell;
	}

	public void setPrice_sell(double price_sell) {
		this.price_sell = price_sell;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getYield_float() {
		return yield_float;
	}

	public void setYield_float(double yield_float) {
		this.yield_float = yield_float;
	}

	public String getVolumn_total() {
		return volumn_total;
	}

	public void setVolumn_total(String volumn_total) {
		this.volumn_total = volumn_total;
	}

	public String getVolumn_infrozen() {
		return volumn_infrozen;
	}

	public void setVolumn_infrozen(String volumn_infrozen) {
		this.volumn_infrozen = volumn_infrozen;
	}

	public String getCreate_at() {
		return create_at;
	}

	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}

	public List<StockholdsBean> getInfolist() {
		return infolist;
	}

	public void setInfolist(List<StockholdsBean> infolist) {
		this.infolist = infolist;
	}
	
}

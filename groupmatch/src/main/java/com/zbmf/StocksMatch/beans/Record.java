package com.zbmf.StocksMatch.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class Record extends General{
    private int page;
    private int perpage;
    private int pages;
    private List<Record> records;
    private String win_at;
    private String category;
    private String rank;
    private String award;
    private MatchBean match;

    public MatchBean getMatch() {
        return match;
    }

    public void setMatch(MatchBean match) {
        this.match = match;
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

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getWin_at() {
        return win_at;
    }

    public void setWin_at(String win_at) {
        this.win_at = win_at;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }
}

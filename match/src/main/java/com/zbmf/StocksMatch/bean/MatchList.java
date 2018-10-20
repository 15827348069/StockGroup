package com.zbmf.StocksMatch.bean;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/11/23.
 */

public class MatchList extends BaseBean{
    private int page;
    private int pages;
    private List<MatchBean>match;
    private List<MatchBean>matches;

    public List<MatchBean> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchBean> matches) {
        this.matches = matches;
    }

    public List<MatchBean> getMatch() {
        return match;
    }

    public void setMatch(List<MatchBean> match) {
        this.match = match;
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
}

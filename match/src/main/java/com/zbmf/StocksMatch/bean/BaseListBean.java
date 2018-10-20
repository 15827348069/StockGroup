package com.zbmf.StocksMatch.bean;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public class BaseListBean extends BaseBean{
    private int page;
    private int perpags;
    private int total;
    private int pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerpags() {
        return perpags;
    }

    public void setPerpags(int perpags) {
        this.perpags = perpags;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}

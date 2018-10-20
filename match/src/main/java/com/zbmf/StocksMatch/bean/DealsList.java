package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public class DealsList extends Erro implements Serializable{
    private String status;
    private Result result;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result implements Serializable{
        private int page;
        private int perpage;
        private int pages;
        private int total;
        private List<DealSys> deals;

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

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DealSys> getDeals() {
            return deals;
        }

        public void setDeals(List<DealSys> deals) {
            this.deals = deals;
        }
    }

}

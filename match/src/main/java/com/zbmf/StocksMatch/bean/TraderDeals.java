package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/23.
 */

public class TraderDeals extends Erro implements Serializable {
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
        private List<Deals> deals;
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

        public List<Deals> getDeals() {
            return deals;
        }

        public void setDeals(List<Deals> deals) {
            this.deals = deals;
        }
        public static class Deals implements Serializable{
            private String created_at;
            private String action;
            private String name;
            private int volumn;
            private String price;
            private String gain;
            private String gain_yield;
            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getVolumn() {
                return volumn;
            }

            public void setVolumn(int volumn) {
                this.volumn = volumn;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getGain() {
                return gain;
            }

            public void setGain(String gain) {
                this.gain = gain;
            }

            public String getGain_yield() {
                return gain_yield;
            }

            public void setGain_yield(String gain_yield) {
                this.gain_yield = gain_yield;
            }
        }
    }
}

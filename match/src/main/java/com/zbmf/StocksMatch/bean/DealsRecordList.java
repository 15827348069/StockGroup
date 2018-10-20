package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/4/8.
 * 交易记录
 */

public class DealsRecordList extends Erro implements Serializable{
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

    public class Result implements Serializable{
        private int page;
        private int perpage;
        private int pages;
        private int total;
        private List<Stocks> stocks;

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

        public List<Stocks> getStocks() {
            return stocks;
        }

        public void setStocks(List<Stocks> stocks) {
            this.stocks = stocks;
        }

        public class Stocks implements Serializable{
            private String id;
            private String symbol;
            private String name;
            private int volumn;
            private double price;
            private double price_buy;
            private double price_sell;
            private String contract_id;
            private int type;
            private double profit;
            private String posted_at;

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

            public int getVolumn() {
                return volumn;
            }

            public void setVolumn(int volumn) {
                this.volumn = volumn;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getPrice_buy() {
                return price_buy;
            }

            public void setPrice_buy(double price_buy) {
                this.price_buy = price_buy;
            }

            public double getPrice_sell() {
                return price_sell;
            }

            public void setPrice_sell(double price_sell) {
                this.price_sell = price_sell;
            }

            public String getContract_id() {
                return contract_id;
            }

            public void setContract_id(String contract_id) {
                this.contract_id = contract_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public double getProfit() {
                return profit;
            }

            public void setProfit(double profit) {
                this.profit = profit;
            }

            public String getPosted_at() {
                return posted_at;
            }

            public void setPosted_at(String posted_at) {
                this.posted_at = posted_at;
            }
        }
    }
}

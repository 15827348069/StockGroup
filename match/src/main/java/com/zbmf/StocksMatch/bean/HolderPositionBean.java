package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/19.
 */

public class HolderPositionBean extends Erro implements Serializable {
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

        public static class Stocks implements Serializable{
            private String id;
            private String symbol;
            private String name;
            private double current;
            private double close;
            private double price_buy;
            private double price2;
            private double price_float;
            private double price_sell;
            private double profit;
            private double yield_float;
            private int volumn_total;
            private int volumn_unfrozen;
            private String created_at;
            private double comment_count;
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

            public double getCurrent() {
                return current;
            }

            public void setCurrent(double current) {
                this.current = current;
            }

            public double getClose() {
                return close;
            }

            public void setClose(double close) {
                this.close = close;
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

            public int getVolumn_total() {
                return volumn_total;
            }

            public void setVolumn_total(int volumn_total) {
                this.volumn_total = volumn_total;
            }

            public int getVolumn_unfrozen() {
                return volumn_unfrozen;
            }

            public void setVolumn_unfrozen(int volumn_unfrozen) {
                this.volumn_unfrozen = volumn_unfrozen;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public double getCommnet_count() {
                return comment_count;
            }

            public void setCommnet_count(double comment_count) {
                this.comment_count = comment_count;
            }
        }
    }
}

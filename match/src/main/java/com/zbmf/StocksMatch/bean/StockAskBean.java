package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/31.
 */

public class StockAskBean extends Erro implements Serializable {
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
        private List<Asks> asks;

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

        public List<Asks> getAsks() {
            return asks;
        }

        public void setAsks(List<Asks> asks) {
            this.asks = asks;
        }
        public class Asks implements Serializable{
            private String symbol;
            private String stock_name;
            private String ask_name;
            private String ask_content;
            private String ask_at;
            private String reply_name;
            private String reply_content;
            private String reply_at;
            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getStock_name() {
                return stock_name;
            }

            public void setStock_name(String stock_name) {
                this.stock_name = stock_name;
            }

            public String getAsk_name() {
                return ask_name;
            }

            public void setAsk_name(String ask_name) {
                this.ask_name = ask_name;
            }

            public String getAsk_content() {
                return ask_content;
            }

            public void setAsk_content(String ask_content) {
                this.ask_content = ask_content;
            }

            public String getAsk_at() {
                return ask_at;
            }

            public void setAsk_at(String ask_at) {
                this.ask_at = ask_at;
            }

            public String getReply_name() {
                return reply_name;
            }

            public void setReply_name(String reply_name) {
                this.reply_name = reply_name;
            }

            public String getReply_content() {
                return reply_content;
            }

            public void setReply_content(String reply_content) {
                this.reply_content = reply_content;
            }

            public String getReply_at() {
                return reply_at;
            }

            public void setReply_at(String reply_at) {
                this.reply_at = reply_at;
            }
        }
    }
}

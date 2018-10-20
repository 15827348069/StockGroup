package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/30.
 */

public class StockRemarkListBean extends Erro implements Serializable {
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
        private List<Remarks> remarks;

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

        public List<Remarks> getRemarks() {
            return remarks;
        }

        public void setRemarks(List<Remarks> remarks) {
            this.remarks = remarks;
        }

        public class Remarks implements Serializable{
            private int remark_id;
            private String symbol;
            private String subject;
            private String content;

            public int getRemark_id() {
                return remark_id;
            }

            public void setRemark_id(int remark_id) {
                this.remark_id = remark_id;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}

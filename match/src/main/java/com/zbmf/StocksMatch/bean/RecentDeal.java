package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/28.
 */

public class RecentDeal extends Erro implements Serializable {
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

        public class Deals implements Serializable{
            private int user_id;
            private String nickname;
            private String avatar;
            private int type;
            private int symbol;
            private String name;
            private String posted_at;
            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getSymbol() {
                return symbol;
            }

            public void setSymbol(int symbol) {
                this.symbol = symbol;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

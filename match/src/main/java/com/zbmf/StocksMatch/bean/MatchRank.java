package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/28.
 */

public class MatchRank extends Erro implements Serializable {
    private String status;
    private Result result;
    private String last_update;

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

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public class Result implements Serializable {
        private List<Yields> yields;
        private int page;
        private int perpage;
        private int total;
        private int pages;

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

        public List<Yields> getYields() {
            return yields;
        }

        public void setYields(List<Yields> yields) {
            this.yields = yields;
        }

        public class Yields implements Serializable {
            private int user_id;
            private String nickname;
            private String avatar;
            private double yield;
            private double total_yield;
            private double day_yield;
            private double week_yield;
            private double month_yield;
            private Last_deal last_deal;

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

            public double getYield() {
                return yield;
            }

            public void setYield(double yield) {
                this.yield = yield;
            }

            public double getTotal_yield() {
                return total_yield;
            }

            public void setTotal_yield(double total_yield) {
                this.total_yield = total_yield;
            }

            public double getDay_yield() {
                return day_yield;
            }

            public void setDay_yield(double day_yield) {
                this.day_yield = day_yield;
            }

            public double getWeek_yield() {
                return week_yield;
            }

            public void setWeek_yield(double week_yield) {
                this.week_yield = week_yield;
            }

            public double getMonth_yield() {
                return month_yield;
            }

            public void setMonth_yield(double month_yield) {
                this.month_yield = month_yield;
            }

            public Last_deal getLast_deal() {
                return last_deal;
            }

            public void setLast_deal(Last_deal last_deal) {
                this.last_deal = last_deal;
            }

            public class Last_deal implements Serializable {
                private String symbol;
                private String name;
                private int type;
                private String created_at;

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

                public String getType() {
                   return type==2?"买入":"卖出";
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }
            }
        }
    }
}

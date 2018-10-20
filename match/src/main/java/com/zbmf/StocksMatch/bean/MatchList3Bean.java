package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/21.
 * 比赛列表-高校/城市/埢商
 */

public class MatchList3Bean extends Erro implements Serializable {
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
        private List<Matches> matches;
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

        public List<Matches> getMatches() {
            return matches;
        }

        public void setMatches(List<Matches> matches) {
            this.matches = matches;
        }

        public class Matches implements Serializable{
            private int match_id;
            private String match_name;
            private double week_yield;
            private int rank;
            private int is_player;
            private List<String> top_players;

            public int getMatch_id() {
                return match_id;
            }

            public void setMatch_id(int match_id) {
                this.match_id = match_id;
            }

            public String getMatch_name() {
                return match_name;
            }

            public void setMatch_name(String match_name) {
                this.match_name = match_name;
            }

            public double getWeek_yield() {
                return week_yield;
            }

            public void setWeek_yield(double week_yield) {
                this.week_yield = week_yield;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public boolean getIs_player() {
                return is_player==1;
            }

            public void setIs_player(int is_player) {
                this.is_player = is_player;
            }

            public List<String> getTop_players() {
                return top_players;
            }

            public void setTop_players(List<String> top_players) {
                this.top_players = top_players;
            }
        }
    }
}

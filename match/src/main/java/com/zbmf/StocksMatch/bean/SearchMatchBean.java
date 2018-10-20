package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/21.
 */

public class SearchMatchBean extends Erro implements Serializable {
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
            private int match_type;
            private int is_end;
            private int is_player;
            private int players;
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

            public int getMatch_type() {
                return match_type;
            }

            public void setMatch_type(int match_type) {
                this.match_type = match_type;
            }

            public boolean getIs_end() {
                return is_end==1;
            }
            public int getIs_end1() {
                return is_end;
            }
            public void setIs_end(int is_end) {
                this.is_end = is_end;
            }

            public boolean getIs_player() {
                return is_player==1;
            }

            public void setIs_player(int is_player) {
                this.is_player = is_player;
            }

            public int getPlayers() {
                return players;
            }

            public void setPlayers(int players) {
                this.players = players;
            }
        }
    }
}

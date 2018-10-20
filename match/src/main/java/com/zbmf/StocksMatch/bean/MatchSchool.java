package com.zbmf.StocksMatch.bean;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/11/27.
 */

public class MatchSchool extends BaseBean {
    private int match_num;
    private int match_player;
    private List<School>schools;

    public int getMatch_num() {
        return match_num;
    }

    public void setMatch_num(int match_num) {
        this.match_num = match_num;
    }

    public int getMatch_player() {
        return match_player;
    }

    public void setMatch_player(int match_player) {
        this.match_player = match_player;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }
}

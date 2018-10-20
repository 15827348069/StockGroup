package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/6/6.
 */

public class Round implements Serializable {
    private int round_id;
    private String round;
    private String start_apply;
    private String end_apply;
    private String start_at;
    private String end_at;

    public int getRound_id() {
        return round_id;
    }

    public void setRound_id(int round_id) {
        this.round_id = round_id;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getStart_apply() {
        return start_apply;
    }

    public void setStart_apply(String start_apply) {
        this.start_apply = start_apply;
    }

    public String getEnd_apply() {
        return end_apply;
    }

    public void setEnd_apply(String end_apply) {
        this.end_apply = end_apply;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }
}

package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2018/1/30.
 */

public class AskBean implements Serializable{
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

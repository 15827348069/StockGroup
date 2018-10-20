package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/4/17.
 */

public class VerifyCodeBean extends Erro implements Serializable{
  private String status;
  private int log_id;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }
}

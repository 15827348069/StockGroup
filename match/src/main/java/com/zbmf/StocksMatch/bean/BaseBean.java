package com.zbmf.StocksMatch.bean;


/**
 * Created by xuhao
 * on 2017/11/22.
 */

public class BaseBean {
    private String status;
    private String auth_token;
    private int bind_phone;
    private Err err;
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Err getErr() {
        return err;
    }

    public void setErr(Err err) {
        this.err = err;
    }

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public int getBind_phone() {
        return bind_phone;
    }

    public void setBind_phone(int bind_phone) {
        this.bind_phone = bind_phone;
    }
    public static class Err{
        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "status='" + status + '\'' +
                ", auth_token='" + auth_token + '\'' +
                ", bind_phone=" + bind_phone +
                ", err=" + err +
                ", result=" + result +
                '}';
    }
}

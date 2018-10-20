package com.zbmf.StocksMatch.bean;

/**
 * Created by xuhao
 * on 2017/11/23.
 */

public class Address {
    private match match;
    private group group;
    private passport passport;
    private stock stock;
    private www www;

    public Address.match getMatch() {
        return match;
    }

    public void setMatch(Address.match match) {
        this.match = match;
    }

    public Address.group getGroup() {
        return group;
    }

    public void setGroup(Address.group group) {
        this.group = group;
    }

    public Address.passport getPassport() {
        return passport;
    }

    public void setPassport(Address.passport passport) {
        this.passport = passport;
    }

    public Address.stock getStock() {
        return stock;
    }

    public void setStock(Address.stock stock) {
        this.stock = stock;
    }

    public Address.www getWww() {
        return www;
    }

    public void setWww(Address.www www) {
        this.www = www;
    }

    public static class match{
        private String host;
        private String api;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }
    }
    public static class group{
        private String host;
        private String api;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }

        @Override
        public String toString() {
            return "group{" +
                    "host='" + host + '\'' +
                    ", api='" + api + '\'' +
                    '}';
        }
    }
    public static class passport{
        private String host;
        private String api;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }
    }
    public static class stock{
        private String host;
        private String api;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }
    }
    public static class www{
        private String host;
        private String api;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }
    }

    @Override
    public String toString() {
        return "Address{" +
                "match=" + match +
                ", group=" + group +
                ", passport=" + passport +
                ", stock=" + stock +
                ", www=" + www +
                '}';
    }
}

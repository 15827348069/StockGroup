package com.zbmf.StockGroup.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pq
 * on 2018/7/30.
 */

public class DongmiBean implements Parcelable{
    private int company_id;
    private String symbol;
    private String company_name;
    private String secretary;
    private String address;
    private String email;
    private String phone;
    private String industry;
    private String area;
    private String avatar;
    private int reply;
    private int reply_rank;
    private int updated_at;

    public DongmiBean(int company_id, String symbol, String company_name, String secretary, String address,
                      String email, String phone, String industry, String area, String avatar, int reply,
                      int reply_rank, int updated_at) {
        this.company_id = company_id;
        this.symbol = symbol;
        this.company_name = company_name;
        this.secretary = secretary;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.industry = industry;
        this.area = area;
        this.avatar = avatar;
        this.reply = reply;
        this.reply_rank = reply_rank;
        this.updated_at = updated_at;
    }

    protected DongmiBean(Parcel in) {
        company_id = in.readInt();
        symbol = in.readString();
        company_name = in.readString();
        secretary = in.readString();
        address = in.readString();
        email = in.readString();
        phone = in.readString();
        industry = in.readString();
        area = in.readString();
        avatar = in.readString();
        reply = in.readInt();
        reply_rank = in.readInt();
        updated_at = in.readInt();
    }

    public static final Creator<DongmiBean> CREATOR = new Creator<DongmiBean>() {
        @Override
        public DongmiBean createFromParcel(Parcel in) {
            return new DongmiBean(in);
        }

        @Override
        public DongmiBean[] newArray(int size) {
            return new DongmiBean[size];
        }
    };

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public int getReply_rank() {
        return reply_rank;
    }

    public void setReply_rank(int reply_rank) {
        this.reply_rank = reply_rank;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(company_id);
        dest.writeString(symbol);
        dest.writeString(company_name);
        dest.writeString(secretary);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(industry);
        dest.writeString(area);
        dest.writeString(avatar);
        dest.writeInt(reply);
        dest.writeInt(reply_rank);
        dest.writeInt(updated_at);
    }
}

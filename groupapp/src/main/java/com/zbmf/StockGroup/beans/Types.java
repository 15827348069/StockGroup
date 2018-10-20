package com.zbmf.StockGroup.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pq
 * on 2018/7/2.
 */

public class Types implements Parcelable {
    private int totals;
    private int id;
    private String name;
    private String created_at;

    public Types(int totals, int id, String name, String created_at) {
        this.totals = totals;
        this.id = id;
        this.name = name;
        this.created_at = created_at;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 序列化过程：必须按成员变量声明的顺序进行封装
        dest.writeInt(totals);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(created_at);
    }

    public static Parcelable.Creator<Types> CREATOR = new Creator<Types>() {
        @Override
        public Types createFromParcel(Parcel source) {
            return new Types(source.readInt(),source.readInt(),source.readString(),source.readString());
        }

        @Override
        public Types[] newArray(int size) {
            return new Types[size];
        }
    };
}

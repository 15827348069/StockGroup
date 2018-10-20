package com.zbmf.StocksMatch.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/1/26.
 */
public class VersionInfo extends General implements Parcelable {
    private String version;
    private String force;
    private String url;

    private String AppUrl;

    private String AppHost;

    public String getAppHost() {
        return AppHost;
    }

    public void setAppHost(String appHost) {
        AppHost = appHost;
    }

    public String getAppUrl() {
        return AppUrl;
    }

    public void setAppUrl(String appUrl) {
        AppUrl = appUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.version);
        dest.writeString(this.force);
        dest.writeString(this.url);
    }

    public VersionInfo() {
    }

    protected VersionInfo(Parcel in) {
        this.version = in.readString();
        this.force = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<VersionInfo> CREATOR = new Parcelable.Creator<VersionInfo>() {
        @Override
        public VersionInfo createFromParcel(Parcel source) {
            return new VersionInfo(source);
        }

        @Override
        public VersionInfo[] newArray(int size) {
            return new VersionInfo[size];
        }
    };
}

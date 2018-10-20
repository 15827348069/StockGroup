package com.zbmf.StockGroup.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuhao on 2018/1/5.
 */

public class MatchAnnouncements implements Parcelable {
    private String group_id;
    private String topic_id;
    private String subject;
    private String content;
    private String posted_at;
    public MatchAnnouncements(){

    }
    protected MatchAnnouncements(Parcel in) {
        group_id = in.readString();
        topic_id = in.readString();
        subject = in.readString();
        content = in.readString();
        posted_at = in.readString();
    }

    public static final Creator<MatchAnnouncements> CREATOR = new Creator<MatchAnnouncements>() {
        @Override
        public MatchAnnouncements createFromParcel(Parcel in) {
            return new MatchAnnouncements(in);
        }

        @Override
        public MatchAnnouncements[] newArray(int size) {
            return new MatchAnnouncements[size];
        }
    };

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group_id);
        dest.writeString(topic_id);
        dest.writeString(subject);
        dest.writeString(content);
        dest.writeString(posted_at);
    }
}

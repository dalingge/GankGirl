package com.dalingge.gankio.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dingboyang on 2017/4/27.
 */

public class ReadListBean implements Parcelable {

    private String title;
    private String time;
    private String source;
    private String logo;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.time);
        dest.writeString(this.source);
        dest.writeString(this.logo);
        dest.writeString(this.link);
    }

    public ReadListBean() {
    }

    protected ReadListBean(Parcel in) {
        this.title = in.readString();
        this.time = in.readString();
        this.source = in.readString();
        this.logo = in.readString();
        this.link = in.readString();
    }

    public static final Creator<ReadListBean> CREATOR = new Creator<ReadListBean>() {
        @Override
        public ReadListBean createFromParcel(Parcel source) {
            return new ReadListBean(source);
        }

        @Override
        public ReadListBean[] newArray(int size) {
            return new ReadListBean[size];
        }
    };
}

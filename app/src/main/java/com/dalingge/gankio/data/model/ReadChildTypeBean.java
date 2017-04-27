package com.dalingge.gankio.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dingboyang on 2017/4/27.
 */

public class ReadChildTypeBean implements Parcelable {

    private String title;
    private String img;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
        dest.writeString(this.title);
        dest.writeString(this.img);
        dest.writeString(this.url);
    }

    public ReadChildTypeBean() {
    }

    protected ReadChildTypeBean(Parcel in) {
        this.title = in.readString();
        this.img = in.readString();
        this.url = in.readString();
    }

    public static final Creator<ReadChildTypeBean> CREATOR = new Creator<ReadChildTypeBean>() {
        @Override
        public ReadChildTypeBean createFromParcel(Parcel source) {
            return new ReadChildTypeBean(source);
        }

        @Override
        public ReadChildTypeBean[] newArray(int size) {
            return new ReadChildTypeBean[size];
        }
    };
}

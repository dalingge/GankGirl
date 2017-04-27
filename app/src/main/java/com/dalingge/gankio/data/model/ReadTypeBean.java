package com.dalingge.gankio.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dingboyang on 2017/4/26.
 */

public class ReadTypeBean implements Parcelable {

    private String title;
    private String url;
    private List<ReadChildTypeBean> readChildTypeBeanList;
    private List<ReadListBean> ReadListBeanList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public List<ReadChildTypeBean> getReadChildTypeBeanList() {
        return readChildTypeBeanList;
    }

    public void setReadChildTypeBeanList(List<ReadChildTypeBean> readChildTypeBeanList) {
        this.readChildTypeBeanList = readChildTypeBeanList;
    }

    public List<ReadListBean> getReadListBeanList() {
        return ReadListBeanList;
    }

    public void setReadListBeanList(List<ReadListBean> readListBeanList) {
        ReadListBeanList = readListBeanList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeTypedList(this.readChildTypeBeanList);
        dest.writeTypedList(this.ReadListBeanList);
    }

    public ReadTypeBean() {
    }

    protected ReadTypeBean(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.readChildTypeBeanList = in.createTypedArrayList(ReadChildTypeBean.CREATOR);
        this.ReadListBeanList = in.createTypedArrayList(ReadListBean.CREATOR);
    }

    public static final Creator<ReadTypeBean> CREATOR = new Creator<ReadTypeBean>() {
        @Override
        public ReadTypeBean createFromParcel(Parcel source) {
            return new ReadTypeBean(source);
        }

        @Override
        public ReadTypeBean[] newArray(int size) {
            return new ReadTypeBean[size];
        }
    };
}

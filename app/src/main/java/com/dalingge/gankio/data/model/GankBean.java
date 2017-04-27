package com.dalingge.gankio.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dingboyang on 2016/11/15.
 */

public class GankBean implements Serializable {

    @SerializedName("_id")
    public String _id;
    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("desc")
    public String desc;
    @SerializedName("publishedAt")
    public String publishedAt;
    @SerializedName("source")
    public String source;
    @SerializedName("type")
    public String type;
    @SerializedName("url")
    public String url;
    @SerializedName("used")
    public boolean used;
    @SerializedName("who")
    public String who;
    @SerializedName("images")
    public ArrayList<String> images;

}

package com.dalingge.gankio.network;

import java.io.Serializable;
import java.util.Map;

/**
 * 请求参数类
 * Created by dingboyang on 2016/12/23.
 */
public class RequestContext implements Serializable {

    private static final long serialVersionUID = 1L;

    /**请求ID*/
    private int requestID;
    /**当接口里的*/
    private Map<String,Object> valueMap;

    private String type;
    private String url;
    private String desc;
    private String who;
    private int page;

    public RequestContext() {

    }

    public RequestContext(int requestID){

        this.requestID = requestID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}

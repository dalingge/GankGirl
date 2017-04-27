package com.dalingge.gankio.data.model;

import java.io.Serializable;

/**
 * FileName: ResultBean.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/11
 */
public class ResultBean<T>  implements Serializable{

    /**
     * error : false
     * msg : 老大, 所有提交数据一切正常!
     */

    private boolean error;
    private String msg;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //用来模仿Data
    private T results;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("title=" + error);
        if (null != results) {
            sb.append(" results:" + results.toString());
        }
        return sb.toString();
    }
}

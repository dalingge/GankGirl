package com.dalingge.gankio.bean;

/**
 * FileName: ResultBean.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/11
 */
public class ResultBean {

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
}

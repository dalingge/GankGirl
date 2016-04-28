package com.dalingge.gankio.http;

/**
 * FileName: ExceptionApi.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/28
 */
public class ExceptionApi extends RuntimeException{

    public ExceptionApi(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ExceptionApi(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code){
        String message = "";
        return message;
    }
}

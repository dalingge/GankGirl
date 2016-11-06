package com.dalingge.gankio.network;

import rx.Subscriber;

/**
 * FileName: HttpResultSubscriber
 * description:
 * Author: 丁博洋
 * Date: 2016/9/10
 */
public abstract class HttpResultSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        //在这里做全局的错误处理
        if(e instanceof HttpExceptionHandle.ResponeThrowable){
            onError((HttpExceptionHandle.ResponeThrowable)e);
        } else {
            onError(new HttpExceptionHandle.ResponeThrowable(e, HttpExceptionHandle.ERROR.UNKNOWN));
        }
    }

    public abstract void onError(HttpExceptionHandle.ResponeThrowable e);
}

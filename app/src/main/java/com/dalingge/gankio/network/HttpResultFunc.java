package com.dalingge.gankio.network;


import com.dalingge.gankio.bean.ResultBean;

import rx.functions.Func1;

/**
 * FileName: HttpResultFunc
 * description:  用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *                Subscriber真正需要的数据类型，也就是Data部分的数据类型
 * Author: 丁博洋
 * Date: 2016/9/1
 */
public class HttpResultFunc<T> implements Func1<ResultBean<T>, T> {
    @Override
    public T call(ResultBean<T> httpResult) {
        if (httpResult.isError()) {
            throw new RuntimeException(httpResult.getMsg());
        }
        return httpResult.getResults();
    }
}

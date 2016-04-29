package com.dalingge.gankio.main.model;


import com.dalingge.gankio.http.HttpMethods;

import rx.Subscriber;

/**
 * FileName: SubmitGankModel.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/11
 */
public class SubmitGankModel {

    public void submitGank(String strUrl, String strDesc, String strWho, String strType,final OnSubmitListListener listener) {
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                if (!isUnsubscribed()) {
                    unsubscribe();
                }
            }
            @Override
            public void onError(Throwable e) {

                listener.onFailure("提交失败！", e);
            }

            @Override
            public void onNext(String str) {

                listener.onSuccess(str);
            }
        };

        HttpMethods.getInstance().submitGank(subscriber, strUrl, strDesc,strWho,strType);
    }

    public interface OnSubmitListListener {
        void onSuccess(String msg);
        void onFailure(String msg, Throwable e);
    }

}

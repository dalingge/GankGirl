package com.dalingge.gankio.main.model;

import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * FileName:WelfareModel.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class WelfareModel {


    public void loadNews(String type,int pageIndex, final OnLoadDataListListener listener) {
        Subscriber subscriber = new Subscriber<List<GirlBean>>() {
            @Override
            public void onCompleted() {
                if (!isUnsubscribed()) {
                    unsubscribe();
                }
            }
            @Override
            public void onError(Throwable e) {

                listener.onFailure("load news list failure.", e);
            }

            @Override
            public void onNext(List<GirlBean> girlBean) {

                listener.onSuccess(girlBean);
            }
        };

        HttpMethods.getInstance().getData(subscriber, type, pageIndex);

    }

    public interface OnLoadDataListListener {
        void onSuccess(List<GirlBean> list);
        void onFailure(String msg, Throwable e);
    }
}

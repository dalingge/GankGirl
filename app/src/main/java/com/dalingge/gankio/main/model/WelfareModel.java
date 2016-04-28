package com.dalingge.gankio.main.model;

import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.http.HttpUtils;

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
        Subscriber subscriber = new Subscriber<GirlBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure("load news list failure.", e);
            }

            @Override
            public void onNext(GirlBean girlBean) {
                listener.onSuccess(girlBean.getResults());
            }
        };

        HttpUtils.getInstance().getDate(subscriber, type, pageIndex);

    }

    public interface OnLoadDataListListener {
        void onSuccess(List<GirlBean.ResultsBean> list);
        void onFailure(String msg, Throwable e);
    }
}

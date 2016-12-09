package com.dalingge.gankio.module.home.gank;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.rxjava.Function0;
import com.dalingge.gankio.network.HttpExceptionHandle;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RetryWhenNetworkException;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.BiConsumer;

/**
 * Created by dingboyang on 2016/11/12.
 */

public class GankPresenter extends BaseRxPresenter<GankFragment> {

    private static final int REQUEST_ITEMS = 1;
    private String type;
    private int page;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableFirst(REQUEST_ITEMS, new Function0<Flowable<List<GankBean>>>() {
            @Override
            public Flowable<List<GankBean>> apply(){
                return HttpRetrofit.getInstance().apiService.getData(type, page)
                        .compose(HttpRetrofit.toTransformer())
                        .retryWhen(new RetryWhenNetworkException());
            }
        }, new BiConsumer<GankFragment, List<GankBean>>() {
            @Override
            public void accept(GankFragment gankFragment, List<GankBean> gankBeanList) throws Exception {
                gankFragment.onAddData(gankBeanList);
            }
        }, new BiConsumer<GankFragment, HttpExceptionHandle.ResponeThrowable>() {
            @Override
            public void accept(GankFragment gankFragment, HttpExceptionHandle.ResponeThrowable responeThrowable) throws Exception {
                gankFragment.onNetworkError(responeThrowable);
            }
        });
    }

    void request( String type, int page) {
        this.type = type;
        this.page = page;
        start(REQUEST_ITEMS);
    }

}

package com.dalingge.gankio.module.home.gank;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.common.rxjava.Function0;
import com.dalingge.gankio.data.model.GankBean;
import com.dalingge.gankio.network.HttpExceptionHandle;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RetryWhenNetworkException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;

/**
 * Created by dingboyang on 2016/11/12.
 */

public class GankPresenter extends BaseRxPresenter<GankFragment> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableFirst(RequestCommand.REQUEST_HOME_GANK,
                new Function0<Observable<List<GankBean>>>() {
                    @Override
                    public Observable<List<GankBean>> apply() {
                        return HttpRetrofit.getInstance().apiService.getData(requestContext.getType(), requestContext.getPage())
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
}

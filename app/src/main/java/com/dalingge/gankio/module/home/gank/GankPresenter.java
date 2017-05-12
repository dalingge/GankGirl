package com.dalingge.gankio.module.home.gank;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RetryWhenNetworkException;

/**
 * Created by dingboyang on 2016/11/12.
 */

public class GankPresenter extends BaseRxPresenter<GankFragment> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableFirst(RequestCommand.REQUEST_HOME_GANK,
                () -> HttpRetrofit.getInstance().apiService.getData(requestContext.getType(), requestContext.getPage())
                        .compose(HttpRetrofit.toTransformer())
                        .retryWhen(new RetryWhenNetworkException()),
                (gankFragment, gankBeanList) -> gankFragment.onAddData(gankBeanList),
                (gankFragment, responeThrowable) -> gankFragment.onNetworkError(responeThrowable));
    }
}

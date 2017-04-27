package com.dalingge.gankio.module.girl;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RetryWhenNetworkException;

/**
 * Created by dingboyang on 2016/11/18.
 */

public class GirlPresenter extends BaseRxPresenter<GirlFragment> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableFirst(RequestCommand.REQUEST_GIRL_IMAGE,
                () -> HttpRetrofit.getInstance().apiService
                        .getData(requestContext.getType(), requestContext.getPage())
                        .compose(HttpRetrofit.toTransformer())
                        .retryWhen(new RetryWhenNetworkException()),
                (gankFragment, gankBeanList) -> gankFragment.onAddData(gankBeanList),
                (gankFragment, responeThrowable) -> gankFragment.onNetworkError(responeThrowable));
    }
}

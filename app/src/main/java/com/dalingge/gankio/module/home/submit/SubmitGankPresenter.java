package com.dalingge.gankio.module.home.submit;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RetryWhenNetworkException;

/**
 * Created by dingboyang on 2016/11/17.
 */

public class SubmitGankPresenter extends BaseRxPresenter<SubmitGankActivity> {


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(RequestCommand.RESPONSE_SUBMIT_GANK,
                () -> HttpRetrofit.getInstance().apiService
                        .submit(requestContext.getUrl(), requestContext.getDesc(), requestContext.getWho(), requestContext.getType(), true)
                        .compose(HttpRetrofit.toStringTransformer())
                        .retryWhen(new RetryWhenNetworkException()),
                SubmitGankActivity::onSuccess,
                SubmitGankActivity::onFailure);
    }


}

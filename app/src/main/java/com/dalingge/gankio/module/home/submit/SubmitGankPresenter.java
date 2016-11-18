package com.dalingge.gankio.module.home.submit;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RetryWhenNetworkException;

/**
 * Created by dingboyang on 2016/11/17.
 */

public class SubmitGankPresenter extends BaseRxPresenter<SubmitGankActivity> {

    private static final int REQUEST_ITEMS = 1;

    private String strUrl;
    private String strDesc;
    private String strWho;
    private String strType;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_ITEMS,
                () -> HttpRetrofit.getInstance().apiService.submit(strUrl, strDesc, strWho, strType, true)
                        .compose(HttpRetrofit.toStringTransformer())
                        .retryWhen(new RetryWhenNetworkException()),
                SubmitGankActivity::onSuccess,
                SubmitGankActivity::onFailure);
    }

    void request(String strUrl, String strDesc, String strWho, String strType) {
        this.strUrl = strUrl;
        this.strDesc = strDesc;
        this.strWho = strWho;
        this.strType = strType;
        start(REQUEST_ITEMS);
    }
}

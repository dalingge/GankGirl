package com.dalingge.gankio.module.home.gank;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RetryWhenNetworkException;

/**
 * Created by dingboyang on 2016/11/12.
 */

public class GankPresenter extends BaseRxPresenter<GankFragment> {

    private static final int REQUEST_ITEMS = 1;
    private String mType;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_ITEMS,
                () -> HttpRetrofit.getInstance().apiService.getData(mType, 1).compose(HttpRetrofit.toTransformer()).retryWhen(new RetryWhenNetworkException()),
                GankFragment::onAddData,
                GankFragment::onNetworkError);
    }

    void request( String type) {
        mType=type;
        start(REQUEST_ITEMS);
    }

}

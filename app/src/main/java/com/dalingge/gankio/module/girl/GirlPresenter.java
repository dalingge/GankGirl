package com.dalingge.gankio.module.girl;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RetryWhenNetworkException;

/**
 * Created by dingboyang on 2016/11/18.
 */

public class GirlPresenter extends BaseRxPresenter<GirlFragment> {

    private static final int REQUEST_ITEMS = 1;
    private String type;
    private int page;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_ITEMS,
                () -> HttpRetrofit.getInstance().apiService.getData(type, page).compose(HttpRetrofit.toTransformer()).retryWhen(new RetryWhenNetworkException()),
                GirlFragment::onAddData,
                GirlFragment::onNetworkError);
    }

    void request(String type, int page) {
        this.type = type;
        this.page = page;
        start(REQUEST_ITEMS);
    }
}

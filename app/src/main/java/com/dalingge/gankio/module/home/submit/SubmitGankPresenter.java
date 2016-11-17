package com.dalingge.gankio.module.home.submit;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;

/**
 * Created by dingboyang on 2016/11/17.
 */

public class SubmitGankPresenter extends BaseRxPresenter<SubmitGankActivity> {

    private static final int REQUEST_ITEMS = 1;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

//        restartableLatestCache(REQUEST_ITEMS,()-> HttpRetrofit.getInstance().apiService.submit("","","","",true).compose(HttpRetrofit.toStringTransformer()),
//                SubmitGankActivity::onSuccess,
//                SubmitGankActivity::onFailure);
    }

    void request(String strUrl, String strDesc, String strWho, String strType) {
        start(REQUEST_ITEMS);
    }
}

package com.dalingge.gankio.module.home;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.network.HttpExceptionHandle;
import com.dalingge.gankio.network.HttpRetrofit;

import java.util.List;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;

/**
 * Created by dingboyang on 2016/11/12.
 */

public class HomePresenter extends BaseRxPresenter<HomeFragment> {

    private static final int REQUEST_ITEMS = 1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }


    public void request(int id ,String type) {

        restartableLatestCache(id, new Func0<Observable<List<GankBean>>>() {
            @Override
            public Observable<List<GankBean>> call() {
                return HttpRetrofit.getInstance().apiService.getData(type, 1)
                        .compose(HttpRetrofit.toTransformer());
            }
        }, new Action2<HomeFragment, List<GankBean>>() {
            @Override
            public void call(HomeFragment homeFragment, List<GankBean> gankBeanList) {
                homeFragment.addData(gankBeanList);
            }
        }, new Action2<HomeFragment, HttpExceptionHandle.ResponeThrowable>() {
            @Override
            public void call(HomeFragment homeFragment, HttpExceptionHandle.ResponeThrowable responeThrowable) {
                homeFragment.showMessage(responeThrowable.message);
            }
        });

        start(id);
    }
}

package com.dalingge.gankio.module.home;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.util.log.L;

/**
 * Created by dingboyang on 2016/11/12.
 */

public class HomePresenter extends BaseRxPresenter<HomeFragment> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        L.i("onCreate");
    }
}

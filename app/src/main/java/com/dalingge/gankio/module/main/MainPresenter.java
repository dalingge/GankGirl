package com.dalingge.gankio.module.main;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.util.log.L;

/**
 * FileName:MainPresenter.java
 * Description:主页
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class MainPresenter extends BaseRxPresenter<MainActivity> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        L.i("onCreate");
    }
}

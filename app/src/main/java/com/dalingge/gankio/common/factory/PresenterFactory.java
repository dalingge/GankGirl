package com.dalingge.gankio.common.factory;

import com.dalingge.gankio.common.base.BasePresenter;

public interface PresenterFactory<P extends BasePresenter> {
    P createPresenter();
}

package com.dalingge.gankio.base;

/**
 * FileName:BasePresenter.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class BasePresenter<BV extends IBaseView> {

    protected BV mView;

    public BasePresenter(BV view) {
        mView = view;
    }
}

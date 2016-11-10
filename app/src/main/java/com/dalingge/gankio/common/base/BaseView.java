package com.dalingge.gankio.common.base;

import com.dalingge.gankio.common.base.factory.PresenterFactory;
import com.dalingge.gankio.common.base.factory.ReflectionPresenterFactory;

/**
 * Created by dingboyang on 2016/11/7.
 */

interface BaseView<P extends BasePresenter> {

    /**
     * 获取当前Presenter代理
     */
    PresenterFactory<P> getPresenterFactory();

    /**
     * 设置当前Presenter代理
     * 调用这个方法之前 onCreate/onFinishInflate 覆盖默认的 {@link ReflectionPresenterFactory} presenter 代理.
     * 使用这种方法对Presenter依赖注入。
     */
    void setPresenterFactory(PresenterFactory<P> presenterFactory);

    /**
     * 获取当前 presenter
     *
     * @return 返回当前工厂的 presenter
     */
    P getPresenter();
}

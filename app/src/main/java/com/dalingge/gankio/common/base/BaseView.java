package com.dalingge.gankio.common.base;

import com.dalingge.gankio.common.base.factory.PresenterFactory;
import com.dalingge.gankio.common.base.factory.ReflectionPresenterFactory;

/**
 * Created by dingboyang on 2016/11/7.
 */

public interface BaseView<P extends BasePresenter> {
    /**
     * Returns a current presenter factory.
     */
    PresenterFactory<P> getPresenterFactory();

    /**
     * Sets a presenter factory.
     * Call this method before onCreate/onFinishInflate to override default {@link ReflectionPresenterFactory} presenter factory.
     * Use this method for presenter dependency injection.
     */
    void setPresenterFactory(PresenterFactory<P> presenterFactory);

    /**
     * Returns a current attached presenter.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the presenter factory returns a non-null value.
     *
     * @return a currently attached presenter or null.
     */
    P getPresenter();
}

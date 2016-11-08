package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dalingge.gankio.common.base.factory.PresenterFactory;
import com.dalingge.gankio.common.base.factory.ReflectionPresenterFactory;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by dingboyang on 2016/11/7.
 */

public class BaseFragment<P extends BasePresenter> extends RxFragment implements BaseView<P>{

    private static final String PRESENTER_STATE_KEY = "fragment_presenter_state";

    // 在ViewPager中,虽然Fragment被destroy了,再是实例似乎并没有被销毁,重新重新创建的时候并不会初始化这里的参数,而是
    // 仍然保留成员变量的值
    private PresenterLifecycleDelegate<P> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    /**
     * Returns a current presenter factory.
     */
    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    /**
     * Sets a presenter factory.
     * Call this method before onCreate/onFinishInflate to override default {@link ReflectionPresenterFactory} presenter factory.
     * Use this method for presenter dependency injection.
     */
    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    /**
     * Returns a current attached presenter.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the presenter factory returns a non-null value.
     *
     * @return a currently attached presenter or null.
     */
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && getPresenter()==null)
            presenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterDelegate.onDropView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy(getActivity().isFinishing());
    }
}

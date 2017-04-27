package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.dalingge.gankio.common.factory.PresenterFactory;
import com.dalingge.gankio.common.factory.ReflectionPresenterFactory;
import com.dalingge.gankio.common.widgets.tips.DefaultTipsHelper;
import com.dalingge.gankio.common.widgets.tips.TipsHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dingboyang on 2016/11/7.
 */

public abstract class BaseActivity<P extends BasePresenter> extends RxAppCompatActivity implements BaseView<P>{

    private static final String PRESENTER_STATE_KEY = "presenter_state";

    private Unbinder unbinder;

    private TipsHelper mTipsHelper;

    protected abstract int getLayout();

    protected abstract void initView();


    private PresenterLifecycleDelegate<P> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    /**
     * 获取当前Presenter代理
     */
    @Override
    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    /**
     * 设置当前Presenter代理
     * 调用这个方法之前 onCreate/onFinishInflate 覆盖默认的 {@link ReflectionPresenterFactory} presenter 代理.
     * 使用这种方法对Presenter依赖注入。
     */
    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    /**
     * 获取当前 presenter
     *
     * @return 返回当前工厂的 presenter
     */
    @Override
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());//设置布局内容
        if (savedInstanceState != null)//
            presenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
        unbinder = ButterKnife.bind(this);  //初始化黄油刀控件绑定框架
        initView();//初始化视图
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterDelegate.onDropView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy(!isChangingConfigurations());

        if (unbinder != null)
            unbinder.unbind();
    }

    public void setTipView(View view) {
        if (mTipsHelper == null)
            mTipsHelper = new DefaultTipsHelper(this, view);
    }

    //状态帧布局，通常用于网络请求的四种状态，普通、载入、错误、空白。支持Drawable或者View来展示，也可以混搭
    public TipsHelper getTipsHelper() {
        return mTipsHelper;
    }
}

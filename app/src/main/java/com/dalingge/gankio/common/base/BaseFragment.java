package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dalingge.gankio.common.factory.PresenterFactory;
import com.dalingge.gankio.common.factory.ReflectionPresenterFactory;
import com.dalingge.gankio.common.widgets.tips.DefaultTipsHelper;
import com.dalingge.gankio.common.widgets.tips.TipsHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dingboyang on 2016/11/7.
 */

public abstract class BaseFragment<P extends BasePresenter> extends RxFragment implements BaseView<P>{

    private static final String PRESENTER_STATE_KEY = "fragment_presenter_state";

    private View rootView;
    private Unbinder unbinder;
    private TipsHelper mTipsHelper;

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
             initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    protected ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void setTipView(View view) {
        if (mTipsHelper == null)
            mTipsHelper = new DefaultTipsHelper(getContext(), view);
    }

    //状态帧布局，通常用于网络请求的四种状态，普通、载入、错误、空白。支持Drawable或者View来展示，也可以混搭
    public TipsHelper getTipsHelper() {
        return mTipsHelper;
    }

    // 在ViewPager中,虽然Fragment被destroy了,再是实例似乎并没有被销毁,重新重新创建的时候并不会初始化这里的参数,而是
    // 仍然保留成员变量的值
    private PresenterLifecycleDelegate<P> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    /**
     * 获取当前Presenter
     */
    @Override
    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    /**
     * 设置Presenter
     * 调用这个方法之前 onCreate/onFinishInflate 覆盖默认的 {@link ReflectionPresenterFactory} presenter factory.
     * 使用这种方法对presenter依赖注入。
     */
    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    @Override
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

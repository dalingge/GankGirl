package com.dalingge.gankio.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dingboyang on 2016/11/7.
 */

public class BasePresenter<View> {

    @Nullable
    private View view;
    private CopyOnWriteArrayList<OnDestroyListener> onDestroyListeners = new CopyOnWriteArrayList<>();

    /**
     *  当presenter创建的时候调用,注意!它跟Activity, Fragment的onCreate没有半毛钱关系
     *
     * @param savedState If the presenter is being re-instantiated after a process restart then this Bundle
     *                   contains the data it supplied in {@link #onSave}.
     */
    protected void onCreate(@Nullable Bundle savedState) {
    }

    /**
     * presenter背销毁的时候将会被调用
     */
    protected void onDestroy() {
    }

    /**
     * 保存状态,保存的状态将会在{@link #onCreate(Bundle)} 中作为参数背传入
     *
     * @param state a non-null bundle which should be used to put presenter's state into.
     */
    protected void onSave(Bundle state) {
    }

    /**
     * 绑定到Activity或者Fragment组件
     * 在哪里绑定最好呢? 我还是选择在{@link Activity#onResume()}里绑定吧, 这样的话所有UI
     * 处理在此之前就处理好了
     *
     * @param view a view that should be taken
     */
    protected void onTakeView(View view) {
    }

    /**
     * 解绑View
     */
    protected void onDropView() {
    }

    /**
     * 非正常创建,对应{@link Activity#onSaveInstanceState(Bundle)}, {@link Activity#onRestoreInstanceState(Bundle)}
     */
    public void restore() {

    }

    /**
     * A callback to be invoked when a presenter is about to be destroyed.
     */
    public interface OnDestroyListener {
        /**
         * Called before {@link BasePresenter#onDestroy()}.
         */
        void onDestroy();
    }

    /**
     * Adds a listener observing {@link #onDestroy}.
     *
     * @param listener a listener to add.
     */
    public void addOnDestroyListener(OnDestroyListener listener) {
        onDestroyListeners.add(listener);
    }

    /**
     * Removed a listener observing {@link #onDestroy}.
     *
     * @param listener a listener to remove.
     */
    public void removeOnDestroyListener(OnDestroyListener listener) {
        onDestroyListeners.remove(listener);
    }

    /**
     * 返回当前绑定的View
     *
     * View一般在这些地方有效:
     * {@link Activity#onResume()} and {@link Activity#onPause()},
     * {@link Fragment#onResume()} and {@link Fragment#onPause()},
     *
     * 除此范围内均返回null
     *
     * 注意: {@link Activity#onActivityResult(int, int, Intent)} 是在 {@link Activity#onResume()}之前调用的,
     * 所以不要作为回调使用这个方法
     *
     * @return a current attached view.
     */
    @Nullable
    public View getView() {
        return view;
    }

    /**
     * 初始化这个presenter
     */
    public void create(Bundle bundle) {
        onCreate(bundle);
    }

    /**
     * Destroys the presenter, calling all {@link BasePresenter.OnDestroyListener} callbacks.
     */
    public void destroy() {
        for (OnDestroyListener listener : onDestroyListeners)
            listener.onDestroy();
        onDestroy();
    }

    /**
     * Saves the presenter.
     */
    public void save(Bundle state) {
        onSave(state);
    }

    /**
     * Attaches a view to the presenter.
     *
     * @param view a view to attach.
     */
    public void takeView(View view) {
        this.view = view;
        onTakeView(view);
    }

    /**
     * Detaches the presenter from a view.
     */
    public void dropView() {
        onDropView();
        this.view = null;
    }
}

package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.dalingge.gankio.common.rxjava.delivery.DeliverFirst;
import com.dalingge.gankio.common.rxjava.delivery.DeliverLatestCache;
import com.dalingge.gankio.common.rxjava.delivery.DeliverReplay;
import com.dalingge.gankio.common.rxjava.delivery.Delivery;
import com.dalingge.gankio.common.rxjava.Function0;
import com.dalingge.gankio.network.HttpExceptionHandle;

import org.reactivestreams.Subscription;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.processors.BehaviorProcessor;


/**
 * Created by dingboyang on 2016/11/7.
 *
 * @param <View> 一个类型view.
 */
public class BaseRxPresenter<View> extends BasePresenter<View> {


    private static final String REQUESTED_KEY = BaseRxPresenter.class.getName() + "#requested";

    private final BehaviorProcessor<View> views = BehaviorProcessor.create();
    private final ListCompositeDisposable subscriptions = new ListCompositeDisposable ();
    // 可重复使用的函数
    private final SparseArray<Function0<Disposable>> restartables = new SparseArray<>();
    private final SparseArray<Disposable > restartableSubscriptions = new SparseArray<>();
    // 工作中的订阅者们
    private final ArrayList<Integer> requested = new ArrayList<>();

    /**
     * 返回 {@link io.reactivex.Flowable } 释放当前的 view 或 null.
     *
     * @return 释放当前的 view 或 null.
     */
    public Flowable<View> view() {
        return views;
    }

    /**
     * 自动注册订阅退订在摧毁。
     * See {@link ListCompositeDisposable#(Subscription) for details.}
     *
     * @param disposable 添加定于
     */
    public void add(Disposable  disposable) {
        subscriptions.add(disposable);
    }

    /**
     * 删除添加的订阅
     *
     * @param disposable 订阅取消.
     */
    public void remove(Disposable  disposable) {
        subscriptions.remove(disposable);
    }

    /**
     * 可重新开始的是任何RxJava可观测,可以启动(订阅)和应该自动重启进程重启后如果还订阅目前储蓄Presenter的状态。
     * <p>
     * 这是一个可重复使用的,已绑定订阅者的可观察对象
     *
     * @param restartableId 重新开始的id
     * @param factory       重新开始的工厂
     */
    public void restartable(int restartableId, Function0<Disposable> factory) {
        restartables.put(restartableId, factory);
        if (requested.contains(restartableId))
            start(restartableId);
    }

    /**
     * 可重新开始
     *
     * @param restartableId 重新请求ID
     */
    public void start(int restartableId)  {
        stop(restartableId);
        requested.add(restartableId);
        restartableSubscriptions.put(restartableId, restartables.get(restartableId).apply());
    }

    /**
     * 取消订阅可重新开始的
     *
     * @param restartableId 重新请求ID
     */
    public void stop(int restartableId) {
        requested.remove((Integer) restartableId);
        Disposable disposable = restartableSubscriptions.get(restartableId);
        if (disposable != null)
            disposable.dispose();
    }

    /**
     * 检查如果取消订阅可重新开始的。
     *
     * @param restartableId 重新请求ID
     * @return 如果订阅是null或unsubscribed 返回true 否则false 。
     */
    public boolean isUnsubscribed(int restartableId) {
        Disposable disposable = restartableSubscriptions.get(restartableId);
        return disposable == null || disposable.isDisposed();
    }

    /**
     *
     * @param restartableId     重新请求ID
     * @param observableFactory 返回一个可观测时可重新起动的运行。
     * @param onNext            回调时将调用接收的数据应该传递给视图。
     * @param onError           回调错误到onError。
     * @param <T>               可观察类型
     */
    public <T> void restartableFirst(int restartableId, final Function0<Flowable<T>> observableFactory,
                                          final BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, HttpExceptionHandle.ResponeThrowable> onError) {

        restartable(restartableId, new Function0<Disposable>() {
            @Override
            public Disposable apply() {
                return observableFactory.apply()
                        .compose(BaseRxPresenter.this.deliverFirst())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * This is a shortcut for calling {@link #restartableFirst (int, Flowable, BiConsumer, BiConsumer)} with the last parameter = null.
     */
    public <T> void restartableFirst(int restartableId, final Function0<Flowable<T>> observableFactory, final BiConsumer<View, T> onNext) {
        restartableFirst(restartableId, observableFactory, onNext, null);
    }

    /**
     *
     * @param restartableId     重新请求ID
     * @param observableFactory 返回一个可观测时可重新起动的运行。
     * @param onNext            回调时将调用接收的数据应该传递给视图。
     * @param onError           回调错误到onError。
     * @param <T>               可观察类型
     */
    public <T> void restartableLatestCache(int restartableId, final Function0<Flowable<T>> observableFactory,
                                           final BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, HttpExceptionHandle.ResponeThrowable> onError) {

        restartable(restartableId, new Function0<Disposable>() {
            @Override
            public Disposable apply()  {
                return observableFactory.apply()
                        .compose(BaseRxPresenter.this.deliverLatestCache())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * This is a shortcut for calling {@link #restartableLatestCache (int, Function, BiConsumer, BiConsumer)} with the last parameter = null.
     */
    public <T> void restartableLatestCache(int restartableId, final Function0<Flowable<T>> observableFactory, final BiConsumer<View, T> onNext) {
        restartableLatestCache(restartableId, observableFactory, onNext, null);
    }

    /**
     *
     * @param restartableId     重新请求ID
     * @param observableFactory 返回一个可观测时可重新起动的运行。
     * @param onNext            回调时将调用接收的数据应该传递给视图。
     * @param onError           回调错误到onError。
     * @param <T>               可观察类型
     */
    public <T> void restartableReplay(int restartableId, final Function0<Flowable<T>> observableFactory,
                                      final BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, HttpExceptionHandle.ResponeThrowable> onError) {

        restartable(restartableId, new Function0<Disposable>() {
            @Override
            public Disposable apply() {
                return observableFactory.apply()
                        .compose(BaseRxPresenter.this.deliverReplay())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * This is a shortcut for calling {@link #restartableReplay (int, Function, BiConsumer, BiConsumer)} with the last parameter = null.
     */
    public <T> void restartableReplay(int restartableId, final Function0<Flowable<T>> observableFactory, final BiConsumer<View, T> onNext) {
        restartableReplay(restartableId, observableFactory, onNext, null);
    }

    /**
     * {@link #deliverLatestCache} keeps the latest onNext value and emits it each time a new view gets attached.
     * If a new onNext value appears while a view is attached, it will be delivered immediately.
     *
     * @param <T> 可观察的类型
     */
    public <T> DeliverLatestCache<View, T> deliverLatestCache() {
        return new DeliverLatestCache<>(views);
    }

    /**
     * {@link #deliverFirst} 只提供第一onNext值已经发出的可观测的来源。
     *
     * @param <T> 可观察的类型
     */
    public <T> DeliverFirst<View, T> deliverFirst() {
        return new DeliverFirst<>(views);
    }

    /**
     * {@link #deliverReplay} 保持所有onNext值并释放他们每次一个新的视图被附加。
     *   如果一个新的onNext值出现在一个view,它将立即发送。
     *
     * @param <T> 可观察的类型
     */
    public <T> DeliverReplay<View, T> deliverReplay() {
        return new DeliverReplay<>(views);
    }

    /**
     * 返回一个方法,可以用于手动可重新起动的链构建.它返回一个Action1
     * 将收到的{@link Delivery}分为两个{@link BiConsumer} onNext和onError调用。
     *
     * @param onNext  方法将调用如果交付包含一个发出下一个值。
     * @param onError 这种方法被称为throwable如果交付包含一个错误。
     * @param <T>     一个类型的值。
     * @return 一个新Action1
     */
    public <T> Consumer<Delivery<View, T>> split(final BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, HttpExceptionHandle.ResponeThrowable> onError) {
        return new Consumer<Delivery<View, T>>() {
            @Override
            public void accept(Delivery<View, T> delivery) throws Exception {
                delivery.split(onNext, onError);
            }
        };
    }

    /**
     * This is a shortcut for calling {@link #(Consumer, BiConsumer)} when the second parameter is null.
     */
    public <T> Consumer<Delivery<View, T>> split(BiConsumer<View, T> onNext) {
        return split(onNext, null);
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onCreate(Bundle savedState) {
        if (savedState != null)
            requested.addAll(savedState.getIntegerArrayList(REQUESTED_KEY));
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onDestroy() {
        views.onComplete();
        subscriptions.dispose();
        for (int i = 0; i < requested.size(); i++) {
            int restartableId = requested.get(i);
            restartableSubscriptions.get(restartableId).dispose();
        }
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onSave(Bundle state) {
        for (int i = requested.size() - 1; i >= 0; i--) {
            int restartableId = requested.get(i);
            Disposable disposable = restartableSubscriptions.get(restartableId);
            if (disposable != null && disposable.isDisposed())
                requested.remove(i);
        }
        state.putIntegerArrayList(REQUESTED_KEY, requested);
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onTakeView(View view) {
        views.onNext(view);
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onDropView() {
        views.onNext(null);
    }


    @Deprecated
    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}

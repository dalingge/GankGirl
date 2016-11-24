package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.dalingge.gankio.common.base.delivery.DeliverFirst;
import com.dalingge.gankio.common.base.delivery.DeliverLatestCache;
import com.dalingge.gankio.common.base.delivery.DeliverReplay;
import com.dalingge.gankio.common.base.delivery.Delivery;
import com.dalingge.gankio.network.HttpExceptionHandle;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * This is an extension of {@link BasePresenter} which provides RxJava functionality.
 * <p>
 * Created by dingboyang on 2016/11/7.
 *
 * @param <View> a type of view.
 */
public class BaseRxPresenter<View> extends BasePresenter<View> {


    private static final String REQUESTED_KEY = BaseRxPresenter.class.getName() + "#requested";

    private final BehaviorSubject<View> views = BehaviorSubject.create();
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private final SparseArray<Func0<Subscription>> restartables = new SparseArray<>();
    private final SparseArray<Subscription> restartableSubscriptions = new SparseArray<>();
    private final ArrayList<Integer> requested = new ArrayList<>();

    /**
     * Returns an {@link rx.Observable} that emits the current attached view or null.
     * See {@link BehaviorSubject} for more information.
     *
     * @return an observable that emits the current attached view or null.
     */
    public Observable<View> view() {
        return views;
    }

    /**
     * Registers a subscription to automatically unsubscribe it during onDestroy.
     * See {@link CompositeSubscription#add(Subscription) for details.}
     *
     * @param subscription a subscription to add.
     */
    public void add(Subscription subscription) {
        subscriptions.add(subscription);
    }

    /**
     * Removes and unsubscribes a subscription that has been registered with {@link #add} previously.
     * See {@link CompositeSubscription#remove(Subscription)} for details.
     *
     * @param subscription a subscription to remove.
     */
    public void remove(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    /**
     * A restartable is any RxJava observable that can be started (subscribed) and
     * should be automatically restarted (re-subscribed) after a process restart if
     * it was still subscribed at the moment of saving presenter's state.
     * <p>
     * Registers a factory. Re-subscribes the restartable after the process restart.
     *
     * @param restartableId id of the restartable
     * @param factory       factory of the restartable
     */
    public void restartable(int restartableId, Func0<Subscription> factory) {
        restartables.put(restartableId, factory);
        if (requested.contains(restartableId))
            start(restartableId);
    }

    /**
     * 可重新开始
     *
     * @param restartableId 重新请求ID
     */
    public void start(int restartableId) {
        stop(restartableId);
        requested.add(restartableId);
        restartableSubscriptions.put(restartableId, restartables.get(restartableId).call());
    }

    /**
     * 取消订阅可重新开始的
     *
     * @param restartableId 重新请求ID
     */
    public void stop(int restartableId) {
        requested.remove((Integer) restartableId);
        Subscription subscription = restartableSubscriptions.get(restartableId);
        if (subscription != null)
            subscription.unsubscribe();
    }

    /**
     * 检查如果取消订阅可重新开始的。
     *
     * @param restartableId 重新请求ID
     * @return 如果订阅是null或unsubscribed 返回true 否则false 。
     */
    public boolean isUnsubscribed(int restartableId) {
        Subscription subscription = restartableSubscriptions.get(restartableId);
        return subscription == null || subscription.isUnsubscribed();
    }

    /**
     * This is a shortcut that can be used instead of combining together
     * {@link #restartable(int, Func0)},
     * {@link #deliverFirst()},
     * {@link #split(Action2, Action2)}.
     *
     * @param restartableId     重新请求ID
     * @param observableFactory 返回一个可观测时可重新起动的运行。
     * @param onNext            回调时将调用接收的数据应该传递给视图。
     * @param onError           回调错误到onError。
     * @param <T>               可观察类型
     */
    public <T> void restartableFirst(int restartableId, final Func0<Observable<T>> observableFactory,
                                     final Action2<View, T> onNext, @Nullable final Action2<View, HttpExceptionHandle.ResponeThrowable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(BaseRxPresenter.this.deliverFirst())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * This is a shortcut for calling {@link #restartableFirst(int, Func0, Action2, Action2)} with the last parameter = null.
     */
    public <T> void restartableFirst(int restartableId, final Func0<Observable<T>> observableFactory, final Action2<View, T> onNext) {
        restartableFirst(restartableId, observableFactory, onNext, null);
    }

    /**
     * This is a shortcut that can be used instead of combining together
     * {@link #restartable(int, Func0)},
     * {@link #deliverLatestCache()},
     * {@link #split(Action2, Action2)}.
     *
     * @param restartableId     重新请求ID
     * @param observableFactory 返回一个可观测时可重新起动的运行。
     * @param onNext            回调时将调用接收的数据应该传递给视图。
     * @param onError           回调错误到onError。
     * @param <T>               可观察类型
     */
    public <T> void restartableLatestCache(int restartableId, final Func0<Observable<T>> observableFactory,
                                           final Action2<View, T> onNext, @Nullable final Action2<View, HttpExceptionHandle.ResponeThrowable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(BaseRxPresenter.this.deliverLatestCache())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * This is a shortcut for calling {@link #restartableLatestCache(int, Func0, Action2, Action2)} with the last parameter = null.
     */
    public <T> void restartableLatestCache(int restartableId, final Func0<Observable<T>> observableFactory, final Action2<View, T> onNext) {
        restartableLatestCache(restartableId, observableFactory, onNext, null);
    }

    /**
     * This is a shortcut that can be used instead of combining together
     * {@link #restartable(int, Func0)},
     * {@link #deliverReplay()},
     * {@link #split(Action2, Action2)}.
     *
     * @param restartableId     重新请求ID
     * @param observableFactory 返回一个可观测时可重新起动的运行。
     * @param onNext            回调时将调用接收的数据应该传递给视图。
     * @param onError           回调错误到onError。
     * @param <T>               可观察类型
     */
    public <T> void restartableReplay(int restartableId, final Func0<Observable<T>> observableFactory,
                                      final Action2<View, T> onNext, @Nullable final Action2<View, HttpExceptionHandle.ResponeThrowable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(BaseRxPresenter.this.deliverReplay())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * This is a shortcut for calling {@link #restartableReplay(int, Func0, Action2, Action2)} with the last parameter = null.
     */
    public <T> void restartableReplay(int restartableId, final Func0<Observable<T>> observableFactory, final Action2<View, T> onNext) {
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
     *   如果一个新的onNext值出现在一个视图,它将立即发送。
     *
     * @param <T> 可观察的类型
     */
    public <T> DeliverReplay<View, T> deliverReplay() {
        return new DeliverReplay<>(views);
    }

    /**
     * 返回一个方法,可以用于手动可重新起动的链构建.它返回一个Action1
     * 将收到的{@link Delivery}分为两个{@link Action2} onNext和onError调用。
     *
     * @param onNext  a method that will be called if the delivery contains an emitted onNext value.
     * @param onError a method that will be called if the delivery contains an onError throwable.
     * @param <T>     a type on onNext value.
     * @return an Action1 that splits a received {@link Delivery} into two {@link Action2} onNext and onError calls.
     */
    public <T> Action1<Delivery<View, T>> split(final Action2<View, T> onNext, @Nullable final Action2<View, HttpExceptionHandle.ResponeThrowable> onError) {
        return new Action1<Delivery<View, T>>() {
            @Override
            public void call(Delivery<View, T> delivery) {
                delivery.split(onNext, onError);
            }
        };
    }

    /**
     * This is a shortcut for calling {@link #split(Action2, Action2)} when the second parameter is null.
     */
    public <T> Action1<Delivery<View, T>> split(Action2<View, T> onNext) {
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
        views.onCompleted();
        subscriptions.unsubscribe();
        for (int i = 0; i < requested.size(); i++) {
            int restartableId = requested.get(i);
            restartableSubscriptions.get(restartableId).unsubscribe();
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
            Subscription subscription = restartableSubscriptions.get(restartableId);
            if (subscription != null && subscription.isUnsubscribed())
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

    /**
     * Please, use restartableXX and deliverXX methods for pushing data from RxPresenter into View.
     */
    @Deprecated
    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}

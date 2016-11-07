package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.dalingge.gankio.common.base.delivery.DeliverFirst;
import com.dalingge.gankio.common.base.delivery.DeliverLatestCache;
import com.dalingge.gankio.common.base.delivery.DeliverReplay;
import com.dalingge.gankio.common.base.delivery.Delivery;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func4;
import rx.internal.util.SubscriptionList;
import rx.subjects.BehaviorSubject;

/**
 * This is an extension of {@link BasePresenter} which provides RxJava functionality.
 *
 * Created by dingboyang on 2016/11/7.
 *
 * @param <View> a type of view.
 */
public class BaseRxPresenter<View> extends BasePresenter<View> {


    private static final String REQUESTED_KEY = BaseRxPresenter.class.getName() + "#requested";

    private final BehaviorSubject<View> views = BehaviorSubject.create();

    // 希望在destroy的时候能够终结的subscription list
    private SubscriptionList subscriptions = new SubscriptionList();

    // 可重复使用的函数
    private final HashMap<Integer, Func0<Subscription>> restartables = new HashMap<>();

    private final HashMap<Integer, Func4<?, ?, ?, ?, Subscription>> restartables4 = new HashMap<>();

    // 工作中的订阅者们
    private final HashMap<Integer, Subscription> workingSubscribers = new HashMap<>();


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
     * See {@link SubscriptionList#add(Subscription) for details.}
     *
     * @param subscription a subscription to add.
     */
    public void add(Subscription subscription) {
        subscriptions.add(subscription);
    }

    /**
     * Removes and unsubscribes a subscription that has been registered with {@link #add} previously.
     * See {@link SubscriptionList#remove(Subscription)} for details.
     *
     * @param subscription a subscription to remove.
     */
    public void remove(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    /**
     * 这是一个可重复使用的,已绑定订阅者的可观察对象
     *
     * @param restartableId id of the restartable
     * @param factory       factory of the restartable
     */
    public void restartable(int restartableId, Func0<Subscription> factory) {
        if (workingSubscribers.containsKey(restartableId))
            stop(restartableId);
        restartables.put(restartableId, factory);
    }

    public <T1, T2, T3, T4> void restartable(int restartableId, Func4<T1, T2, T3, T4, Subscription> factory) {
        if (workingSubscribers.containsKey(restartableId)) {
            stop(restartableId);
        }
        restartables4.put(restartableId, factory);
    }

    /**
     * Starts the given restartable.
     *
     * @param restartableId id of the restartable
     */
    public void start(int restartableId) {
        stop(restartableId);
        Func0<Subscription> func = restartables.get(restartableId);
        if (func != null)
            workingSubscribers.put(restartableId, func.call());
    }


    public <T1, T2, T3, T4> void start(int restartableId, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
        stop(restartableId);
        Func4<T1, T2, T3, T4, Subscription> func4 = (Func4<T1, T2, T3, T4, Subscription>) restartables4.get(restartableId);
        if (func4 != null)
            workingSubscribers.put(restartableId, func4.call(arg1, arg2, arg3, arg4));
    }


    /**
     * Unsubscribes a restartable
     *
     * @param restartableId id of a restartable.
     */
    public void stop(int restartableId) {
        Subscription subscription = workingSubscribers.get(restartableId);
        if (subscription != null) {
            if (!subscription.isUnsubscribed())
                subscription.unsubscribe();
            workingSubscribers.remove(restartableId);
        }
    }

    /**
     * 这是一种快捷方式, 结合了
     * {@link #restartable(int, Func0)}
     * {@link #deliverFirst()},
     * {@link #split(Action2, Action2)}
     * 这三个方法
     * <p>
     * Views'last item + observableFactory's first item
     *
     * @param restartableId     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>               the type of the observable.
     */
    public <T> void restartableFirst(int restartableId, final Func0<Observable<T>> observableFactory,
                                     final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(BaseRxPresenter.this.<T>deliverFirst())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    public <T, T1, T2, T3, T4> void restartableFirst(int restartableId,
                                                     final Func4<T1, T2, T3, T4, Observable<T>> observableFactory,
                                                     final Action2<View, T> onNext,
                                                     @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func4<T1, T2, T3, T4, Subscription>() {
            @Override
            public Subscription call(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
                return observableFactory.call(arg1, arg2, arg3, arg4)
                        .compose(BaseRxPresenter.this.<T>deliverFirst())
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
     * 这是一种快捷方式,结合了
     * {@link #restartable(int, Func0)},
     * {@link #deliverLatestCache()},
     * {@link #split(Action2, Action2)}
     * 这三个方法
     * <p>
     * Views's last item + observableFactory's last item, when view emit subscriber will response
     *
     * @param restartableId     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>               the type of the observable.
     */
    public <T> void restartableLatestCache(int restartableId, final Func0<Observable<T>> observableFactory,
                                           final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(BaseRxPresenter.this.<T>deliverLatestCache())
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
     * 这是一种快捷方式,结合了
     * {@link #restartable(int, Func0)},
     * {@link #deliverReplay()},
     * {@link #split(Action2, Action2)}.
     * 这三个方法
     *
     * @param restartableId     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>               the type of the observable.
     */
    public <T> void restartableReplay(int restartableId, final Func0<Observable<T>> observableFactory,
                                      final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(BaseRxPresenter.this.<T>deliverReplay())
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
     * 联合{@link BehaviorSubject<View>}和{@link Observable<T>}
     * <p>
     * v.onNext(view1)
     * v.onNext(view2)
     * <p>
     * v.subscribe(new ...)
     * <p>
     * only loading the lastest 'view2' value can emit
     *
     * @param <T> the type of source observable emissions
     */
    public <T> DeliverLatestCache<View, T> deliverLatestCache() {
        return new DeliverLatestCache<>(views);
    }

    /**
     * 联合{@link BehaviorSubject<View>}和{@link Observable<T>}
     * <p>
     * v.onNext(view1)
     * v.onNext(view2)
     * <p>
     * v.subscribe(new ...)
     * <p>
     * only loading the first 'view1' value can emit
     *
     * the first T item and the last view item
     *
     * @param <T> the type of source observable emissions
     */
    public <T> DeliverFirst<View, T> deliverFirst() {
        return new DeliverFirst<>(views);
    }

    /**
     * 联合{@link BehaviorSubject<View>}和{@link Observable<T>}
     * <p>
     * v.onNext(view1)
     * v.onNext(view2)
     * <p>
     * v.subscribe(new ...)
     * <p>
     * both view1 and view2 will be emitted
     *
     * @param <T> the type of source observable emissions
     */
    public <T> DeliverReplay<View, T> deliverReplay() {
        return new DeliverReplay<>(views);
    }

    /**
     * Returns a method that can be used for manual restartable chain build. It returns an Action1 that splits
     * a received {@link Delivery} into two {@link Action2} onNext and onError calls.
     *
     * @param onNext  a method that will be called if the delivery contains an emitted onNext value.
     * @param onError a method that will be called if the delivery contains an onError throwable.
     * @param <T>     a type on onNext value.
     * @return an Action1 that splits a received {@link Delivery} into two {@link Action2} onNext and onError calls.
     */
    public <T> Action1<Delivery<View, T>> split(final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {
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

    public <T> Observable<Delivery<View, T>> afterTakeViewDeliverLastestCache() {
        return Observable.create((new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        })).compose(this.<T>deliverLatestCache());
    }

    /**
     * 仅在第一次TakeView的时候起作用
     *
     * @return
     */
    public Observable<View> afterTakeView() {
        return views.first();
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onCreate(Bundle savedState) {

    }

    /**
     *
     */
    @Override
    public void restore() {
//        subscriptions = new SubscriptionList();
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onDestroy() {
        views.onCompleted();
        subscriptions.unsubscribe();
        for (Map.Entry<Integer, Subscription> entry : workingSubscribers.entrySet()){
            if (entry.getValue()!=null)
                entry.getValue().unsubscribe();
        }
        workingSubscribers.clear();
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    protected void onSave(Bundle state) {
        // do not thing
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

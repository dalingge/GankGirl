package com.dalingge.gankio.common.base.delivery;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Notification;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;

public class DeliverLatestCache<View, T> implements FlowableTransformer<T, Delivery<View, T>> {

    private final Flowable<View> view;

    public DeliverLatestCache(Flowable<View> view) {
        this.view = view;
    }

    @Override
    public Publisher<Delivery<View, T>> apply(Flowable<T> upstream) {
        return Flowable.combineLatest(view, upstream
                        .materialize()
                        .filter(new Predicate<Notification<T>>() {
                            @Override
                            public boolean test(Notification<T> notification) throws Exception {
                                return !notification.isOnComplete();
                            }
                        }), new BiFunction<View, Notification<T>, Delivery<View, T>>() {
                    @Override
                    public Delivery<View, T> apply(View view, Notification<T> notification) throws Exception {
                        return view == null ? null : new Delivery<>(view, notification);
                    }
                }
        ).filter(new Predicate<Delivery<View, T>>() {
            @Override
            public boolean test(Delivery<View, T> delivery) throws Exception {
                return delivery != null;
            }
        });
    }
}

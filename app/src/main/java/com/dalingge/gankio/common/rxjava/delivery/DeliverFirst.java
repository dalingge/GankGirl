package com.dalingge.gankio.common.rxjava.delivery;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Notification;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


public class DeliverFirst<View, T> implements FlowableTransformer<T, Delivery<View, T>> {

    private final Flowable<View> view;

    public DeliverFirst(Flowable<View> view) {
        this.view = view;
    }

    @Override
    public Publisher<Delivery<View, T>> apply(Flowable<T> upstream) {
        return upstream
                .materialize()
                .take(1)
                .switchMap(new Function<Notification<T>, Publisher<? extends Delivery<View, T>>>() {
                    @Override
                    public Publisher<? extends Delivery<View, T>> apply(Notification<T> notification) throws Exception {
                        return view.map(new Function<View, Delivery<View, T>>() {
                            @Override
                            public Delivery<View, T> apply(View view) throws Exception {
                                return view == null ? null : new Delivery<>(view, notification);
                            }
                        });
                    }
                })
            .filter(new Predicate<Delivery<View, T>>() {
                @Override
                public boolean test(Delivery<View, T> delivery) throws Exception {
                    return delivery != null;
                }
            })
            .take(1);
    }
}
